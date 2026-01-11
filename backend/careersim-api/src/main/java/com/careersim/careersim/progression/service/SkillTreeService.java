package com.careersim.careersim. progression.service;

import com. careersim.careersim. player.model.Player;
import com.careersim.careersim.player.model.PlayerAttributes;
import com.careersim.careersim.player.repository.PlayerRepository;
import com. careersim.careersim. progression.dto.*;
import com.careersim. careersim.progression.model.PlayerSkillNode;
import com. careersim.careersim. progression.model.SkillCategory;
import com.careersim.careersim.progression.model.SkillNode;
import com.careersim.careersim.progression.model.SkillNodeEffect;
import com. careersim.careersim. progression.repository.PlayerSkillNodeRepository;
import com.careersim.careersim.progression. repository.SkillNodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype. Service;
import org.springframework. transaction.annotation.Transactional;

import java.util.*;
import java.util.stream. Collectors;

@Service
@RequiredArgsConstructor
public class SkillTreeService {

    private final SkillNodeRepository skillNodeRepository;
    private final PlayerSkillNodeRepository playerSkillNodeRepository;
    private final PlayerRepository playerRepository;


    @Transactional(readOnly = true)
    public SkillTreeResponseDTO getSkillTree(UUID playerId) {

        List<SkillNode> allNodes = skillNodeRepository.findAll();


        Set<String> unlockedNodeIds = playerSkillNodeRepository.findByPlayerId(playerId)
                .stream()
                .map(psn -> psn.getSkillNode().getId())
                .collect(Collectors.toSet());


        Map<SkillCategory, List<SkillNodeDTO>> categoriesMap = new EnumMap<>(SkillCategory.class);

        for (SkillCategory category : SkillCategory.values()) {
            List<SkillNodeDTO> nodes = allNodes.stream()
                    .filter(node -> node.getCategory() == category)
                    .sorted(Comparator.comparing(SkillNode::getTier))
                    .map(node -> mapToDTO(node, unlockedNodeIds. contains(node.getId())))
                    .collect(Collectors.toList());

            categoriesMap. put(category, nodes);
        }

        return new SkillTreeResponseDTO(categoriesMap);
    }


    @Transactional
    public UnlockNodeResponseDTO unlockNode(UUID playerId, String nodeId) {
        // 1. Buscar player
        Player player = playerRepository. findById(playerId)
                .orElseThrow(() -> new RuntimeException("Player não encontrado"));

        // 2. Buscar nó
        SkillNode node = skillNodeRepository. findById(nodeId)
                .orElseThrow(() -> new RuntimeException("Nó não encontrado:  " + nodeId));

        // 3. Validar se já foi desbloqueado
        if (playerSkillNodeRepository.existsByPlayerIdAndSkillNodeId(playerId, nodeId)) {
            throw new RuntimeException("Nó já foi desbloqueado!");
        }

        // 4. Validar pontos suficientes
        if (player. getTrainingPoints() < node.getCost()) {
            throw new RuntimeException(
                    String.format("Pontos insuficientes!  Necessário: %d, Disponível: %d",
                            node.getCost(), player.getTrainingPoints())
            );
        }

        // 5. Validar pré-requisitos
        validatePrerequisites(playerId, node);

        // 6. Aplicar efeitos nos atributos e guardar mudanças
        List<AttributeChangeDTO> changes = applyNodeEffects(player, node);

        // 7. Gastar pontos
        player.spendTrainingPoints(node.getCost());

        // 8. Registrar nó como desbloqueado
        PlayerSkillNode unlock = new PlayerSkillNode(player, node);
        playerSkillNodeRepository.save(unlock);

        // 9. Salvar player (atributos mudaram, overall recalcula automaticamente)
        playerRepository.save(player);

        return new UnlockNodeResponseDTO(
                node.getId(),
                node.getName(),
                node.getCost(),
                player.getTrainingPoints(),
                changes,
                player.getOverall()
        );
    }


    private void validatePrerequisites(UUID playerId, SkillNode node) {
        List<SkillNode> prerequisites = node.getPrerequisites();

        if (prerequisites.isEmpty()) {
            return;  // Não tem pré-requisitos
        }

        for (SkillNode prerequisite :  prerequisites) {
            boolean hasPrerequisite = playerSkillNodeRepository
                    .existsByPlayerIdAndSkillNodeId(playerId, prerequisite.getId());

            if (!hasPrerequisite) {
                throw new RuntimeException(
                        String.format("Pré-requisito não cumprido: %s", prerequisite.getName())
                );
            }
        }
    }


    private List<AttributeChangeDTO> applyNodeEffects(Player player, SkillNode node) {
        PlayerAttributes attributes = player.getAttributes();
        List<AttributeChangeDTO> changes = new ArrayList<>();

        for (SkillNodeEffect effect :  node.getEffects()) {
            String attrName = effect.getAttributeName();
            Integer increase = effect.getIncreaseAmount();

            // Pegar valor antigo via reflection (ou switch manual)
            Integer oldValue = getAttributeValue(attributes, attrName);

            // Aplicar aumento
            attributes.evolveAttribute(attrName, increase);

            // Pegar novo valor
            Integer newValue = getAttributeValue(attributes, attrName);

            changes.add(new AttributeChangeDTO(attrName, oldValue, newValue, increase));
        }

        return changes;
    }


    private Integer getAttributeValue(PlayerAttributes attributes, String attributeName) {
        return switch (attributeName. toLowerCase()) {
            case "finishing" -> attributes.getFinishing();
            case "shotpower" -> attributes.getShotPower();
            case "longshots" -> attributes.getLongShots();
            case "positioning" -> attributes.getPositioning();
            case "heading" -> attributes.getHeading();
            case "dribbling" -> attributes.getDribbling();
            case "ballcontrol" -> attributes.getBallControl();
            case "shortpassing" -> attributes.getShortPassing();
            case "longpassing" -> attributes.getLongPassing();
            case "acceleration" -> attributes.getAcceleration();
            case "sprintspeed" -> attributes.getSprintSpeed();
            case "agility" -> attributes.getAgility();
            case "marking" -> attributes.getMarking();
            case "standingtackle" -> attributes.getStandingTackle();
            case "slidingtackle" -> attributes. getSlidingTackle();
            case "stamina" -> attributes.getStamina();
            case "strength" -> attributes.getStrength();
            case "jumping" -> attributes. getJumping();
            default -> throw new RuntimeException("Atributo desconhecido: " + attributeName);
        };
    }


    private SkillNodeDTO mapToDTO(SkillNode node, boolean unlocked) {
        // Mapear efeitos
        List<SkillNodeEffectDTO> effects = node.getEffects().stream()
                .map(effect -> new SkillNodeEffectDTO(
                        effect.getAttributeName(),
                        effect.getIncreaseAmount()
                ))
                .collect(Collectors.toList());


        List<String> prerequisiteIds = node. getPrerequisites().stream()
                .map(SkillNode::getId)
                .collect(Collectors.toList());

        return new SkillNodeDTO(
                node.getId(),
                node.getName(),
                node.getDescription(),
                node.getCategory(),
                node.getTier(),
                node.getCost(),
                node.getPath(),
                node.getIcon(),
                node.getPositionX(),
                node.getPositionY(),
                effects,
                prerequisiteIds,
                unlocked
        );
    }
}