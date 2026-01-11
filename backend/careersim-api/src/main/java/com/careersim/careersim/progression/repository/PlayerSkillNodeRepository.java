package com.careersim.careersim.progression.repository;

import com.careersim.careersim.progression.model.PlayerSkillNode;
import com.careersim.careersim.progression.model.SkillCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework. data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype. Repository;

import java.util. List;
import java.util. UUID;

@Repository
public interface PlayerSkillNodeRepository extends JpaRepository<PlayerSkillNode, PlayerSkillNode.PlayerSkillNodeId> {

    // Buscar todos os nós desbloqueados de um player
    List<PlayerSkillNode> findByPlayerId(UUID playerId);

    // Verificar se player já desbloqueou um nó específico
    boolean existsByPlayerIdAndSkillNodeId(UUID playerId, String skillNodeId);

    // Buscar nós desbloqueados de uma categoria específica
    @Query("SELECT psn FROM PlayerSkillNode psn " +
            "JOIN psn.skillNode sn " +
            "WHERE psn.player.id = :playerId AND sn.category = : category")
    List<PlayerSkillNode> findByPlayerIdAndCategory(
            @Param("playerId") UUID playerId,
            @Param("category") SkillCategory category
    );

    // Contar quantos nós o player possui
    long countByPlayerId(UUID playerId);

    // Contar quantos nós de uma categoria o player desbloqueou
    @Query("SELECT COUNT(psn) FROM PlayerSkillNode psn " +
            "JOIN psn. skillNode sn " +
            "WHERE psn.player.id = :playerId AND sn. category = :category")
    long countByPlayerIdAndCategory(
            @Param("playerId") UUID playerId,
            @Param("category") SkillCategory category
    );

    // Deletar todos os nós de um player
    void deleteByPlayerId(UUID playerId);
}