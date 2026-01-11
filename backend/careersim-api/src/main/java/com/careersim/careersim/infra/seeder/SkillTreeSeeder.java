package com.careersim.careersim.infra.seeder;

import com.careersim.careersim.progression.model.SkillCategory;
import com.careersim.careersim.progression.model.SkillNode;
import com.careersim.careersim.progression.model.SkillNodeEffect;
import com. careersim.careersim. progression.  repository.SkillNodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.  CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SkillTreeSeeder implements CommandLineRunner {

    private final SkillNodeRepository skillNodeRepository;

    @Override
    public void run(String...   args) throws Exception {

        if (skillNodeRepository.count() > 0) {
            System.out.println("Skill Tree já está populada. Pulando seeder...");
            return;
        }

        System.out.println(" Populando Skill Tree.. .");

        seedFinishing();
        seedPhysical();
        seedDefending();
        seedDribbling();
        seedPassing();
        seedPace();
        seedGoalkeeper();

        System.out.println(" Skill Tree populada com sucesso!   Total:   " + skillNodeRepository.count() + " nós");
    }

    private SkillNode createNode(String id, String name, String description,
                                 SkillCategory category, Integer tier, Integer cost,
                                 String path, Integer posX, Integer posY) {
        SkillNode node = new SkillNode();
        node.setId(id);
        node.setName(name);
        node.setDescription(description);
        node.setCategory(category);
        node.setTier(tier);
        node.setCost(cost);
        node.setPath(path);
        node.setPositionX(posX);
        node.setPositionY(posY);
        return node;
    }

    // ========================================


    private void seedFinishing() {

        // TIER 1 - NÓ INICIAL

        SkillNode tier1 = createNode(
                "finishing_aprendiz_gol",
                "Aprendiz do Gol",
                "Primeiros passos na arte de finalizar",
                SkillCategory.FINISHING,
                1, 1, null,
                200, 50  // posição central superior
        );
        tier1.addEffect(new SkillNodeEffect("finishing", 2));
        skillNodeRepository.save(tier1);


        // TIER 2 - CAMINHO A:  PODER

        SkillNode tier2a = createNode(
                "finishing_chute_potente",
                "Chute Potente",
                "Desenvolva a força bruta do seu chute",
                SkillCategory.FINISHING,
                2, 2, "A",
                100, 120  // esquerda
        );
        tier2a.addEffect(new SkillNodeEffect("shotPower", 3));
        tier2a.addEffect(new SkillNodeEffect("strength", 2));
        tier2a.addPrerequisite(tier1);
        skillNodeRepository.save(tier2a);


        // TIER 2 - CAMINHO B:  PRECISÃO

        SkillNode tier2b = createNode(
                "finishing_finalizador_clinico",
                "Finalizador Clínico",
                "Precisão cirúrgica nas finalizações",
                SkillCategory.FINISHING,
                2, 2, "B",
                200, 120  // centro
        );
        tier2b.addEffect(new SkillNodeEffect("finishing", 4));
        tier2b.addEffect(new SkillNodeEffect("positioning", 2));
        tier2b.addPrerequisite(tier1);
        skillNodeRepository.save(tier2b);


        // TIER 2 - CAMINHO C:  AÉREO

        SkillNode tier2c = createNode(
                "finishing_dominancia_aerea",
                "Dominância Aérea",
                "Domine os duelos aéreos na área",
                SkillCategory.FINISHING,
                2, 2, "C",
                300, 120  // direita
        );
        tier2c.addEffect(new SkillNodeEffect("heading", 4));
        tier2c.addEffect(new SkillNodeEffect("jumping", 2));
        tier2c.addPrerequisite(tier1);
        skillNodeRepository.save(tier2c);


        // TIER 3 - CAMINHO A
        SkillNode tier3a = createNode(
                "finishing_bomba_longa_distancia",
                "Bomba de Longa Distância",
                "Chutes devastadores de fora da área",
                SkillCategory.FINISHING,
                3, 3, "A",
                100, 190
        );
        tier3a.addEffect(new SkillNodeEffect("longShots", 5));
        tier3a.addEffect(new SkillNodeEffect("shotPower", 3));
        tier3a.addPrerequisite(tier2a);
        skillNodeRepository.save(tier3a);


        // TIER 3 - CAMINHO B

        SkillNode tier3b = createNode(
                "finishing_predador_area",
                "Predador da Área",
                "Instinto assassino dentro da área",
                SkillCategory.FINISHING,
                3, 3, "B",
                200, 190
        );
        tier3b.addEffect(new SkillNodeEffect("finishing", 5));
        tier3b.addEffect(new SkillNodeEffect("positioning", 3));
        tier3b.addPrerequisite(tier2b);
        skillNodeRepository.save(tier3b);


        // TIER 3 - CAMINHO C

        SkillNode tier3c = createNode(
                "finishing_gigante_area",
                "Gigante de Área",
                "Torre imponente nos cruzamentos",
                SkillCategory.FINISHING,
                3, 3, "C",
                300, 190
        );
        tier3c.addEffect(new SkillNodeEffect("heading", 5));
        tier3c.addEffect(new SkillNodeEffect("jumping", 4));
        tier3c.addEffect(new SkillNodeEffect("strength", 2));
        tier3c.addPrerequisite(tier2c);
        skillNodeRepository.save(tier3c);


        // TIER 4 - CAMINHO A

        SkillNode tier4a = createNode(
                "finishing_canhao_humano",
                "Canhão Humano",
                "Chutes impossíveis de defender",
                SkillCategory.FINISHING,
                4, 5, "A",
                100, 260
        );
        tier4a.addEffect(new SkillNodeEffect("shotPower", 6));
        tier4a.addEffect(new SkillNodeEffect("longShots", 5));
        tier4a.addEffect(new SkillNodeEffect("finishing", 3));
        tier4a.addPrerequisite(tier3a);
        skillNodeRepository.save(tier4a);


        // TIER 4 - CAMINHO B

        SkillNode tier4b = createNode(
                "finishing_faro_gol",
                "Faro de Gol",
                "Sempre no lugar certo, na hora certa",
                SkillCategory.FINISHING,
                4, 5, "B",
                200, 260
        );
        tier4b.addEffect(new SkillNodeEffect("finishing", 6));
        tier4b.addEffect(new SkillNodeEffect("positioning", 5));
        tier4b.addEffect(new SkillNodeEffect("shotPower", 3));
        tier4b.addPrerequisite(tier3b);
        skillNodeRepository.save(tier4b);


        // TIER 4 - CAMINHO C

        SkillNode tier4c = createNode(
                "finishing_senhor_ares",
                "Senhor dos Ares",
                "Imbatível no jogo aéreo",
                SkillCategory.FINISHING,
                4, 5, "C",
                300, 260
        );
        tier4c.addEffect(new SkillNodeEffect("heading", 6));
        tier4c.addEffect(new SkillNodeEffect("jumping", 5));
        tier4c.addEffect(new SkillNodeEffect("strength", 4));
        tier4c.addEffect(new SkillNodeEffect("positioning", 3));
        tier4c.addPrerequisite(tier3c);
        skillNodeRepository.save(tier4c);


        // TIER 5 - ARQUÉTIPO
        SkillNode tier5 = createNode(
                "finishing_camisa_9_Fenomeno",
                " Arquétipo Camisa 9 - O Fenômeno ",
                "O atacante completo e letal",
                SkillCategory. FINISHING,
                5, 10, null,
                200, 330  // centro inferior
        );
        tier5.addEffect(new SkillNodeEffect("finishing", 8));
        tier5.addEffect(new SkillNodeEffect("shotPower", 7));
        tier5.addEffect(new SkillNodeEffect("longShots", 6));
        tier5.addEffect(new SkillNodeEffect("heading", 6));
        tier5.addEffect(new SkillNodeEffect("positioning", 5));
        tier5.addEffect(new SkillNodeEffect("jumping", 4));

        tier5.addPrerequisite(tier4b);
        skillNodeRepository.save(tier5);

        System.out.println(" FINISHING: 11 nós criados");
    }

    private void seedPhysical() {

        // TIER 1 - NÓ INICIAL

        SkillNode tier1 = createNode(
                "physical_iniciante_atletico",
                "Iniciante Atlético",
                "Desenvolva sua base física",
                SkillCategory.PHYSICAL,
                1, 1, null,
                200, 50
        );
        tier1.addEffect(new SkillNodeEffect("stamina", 2));
        skillNodeRepository.save(tier1);


        // TIER 2 - CAMINHO A:  FORÇA

        SkillNode tier2a = createNode(
                "physical_corpo_ferro",
                "Corpo de Ferro",
                "Músculos de aço",
                SkillCategory.PHYSICAL,
                2, 2, "A",
                100, 120
        );
        tier2a.addEffect(new SkillNodeEffect("strength", 3));
        tier2a.addEffect(new SkillNodeEffect("heading", 2));
        tier2a.addPrerequisite(tier1);
        skillNodeRepository.save(tier2a);


        // TIER 2 - CAMINHO B: RESISTÊNCIA

        SkillNode tier2b = createNode(
                "physical_incansavel",
                "Incansável",
                "Energia sem fim",
                SkillCategory.PHYSICAL,
                2, 2, "B",
                200, 120
        );
        tier2b.addEffect(new SkillNodeEffect("stamina", 4));
        tier2b.addEffect(new SkillNodeEffect("acceleration", 2));
        tier2b.addPrerequisite(tier1);
        skillNodeRepository.save(tier2b);


        // TIER 2 - CAMINHO C: IMPULSO

        SkillNode tier2c = createNode(
                "physical_saltador",
                "Saltador",
                "Impulso explosivo",
                SkillCategory.PHYSICAL,
                2, 2, "C",
                300, 120
        );
        tier2c.addEffect(new SkillNodeEffect("jumping", 4));
        tier2c.addEffect(new SkillNodeEffect("agility", 2));
        tier2c.addPrerequisite(tier1);
        skillNodeRepository.save(tier2c);


        // TIER 3 - CAMINHO A

        SkillNode tier3a = createNode(
                "physical_tanque",
                "Tanque",
                "Força imparável",
                SkillCategory.PHYSICAL,
                3, 3, "A",
                100, 190
        );
        tier3a.addEffect(new SkillNodeEffect("strength", 5));
        tier3a.addEffect(new SkillNodeEffect("stamina", 3));
        tier3a.addEffect(new SkillNodeEffect("heading", 2));
        tier3a.addPrerequisite(tier2a);
        skillNodeRepository.save(tier3a);


        // TIER 3 - CAMINHO B

        SkillNode tier3b = createNode(
                "physical_maratonista",
                "Maratonista",
                "Corre os 90 minutos sem cansar",
                SkillCategory.PHYSICAL,
                3, 3, "B",
                200, 190
        );
        tier3b.addEffect(new SkillNodeEffect("stamina", 5));
        tier3b.addEffect(new SkillNodeEffect("sprintSpeed", 3));
        tier3b.addEffect(new SkillNodeEffect("acceleration", 2));
        tier3b.addPrerequisite(tier2b);
        skillNodeRepository.save(tier3b);

        // TIER 3 - CAMINHO C

        SkillNode tier3c = createNode(
                "physical_voador",
                "Voador",
                "Elevação incrível",
                SkillCategory.PHYSICAL,
                3, 3, "C",
                300, 190
        );
        tier3c.addEffect(new SkillNodeEffect("jumping", 5));
        tier3c.addEffect(new SkillNodeEffect("heading", 4));
        tier3c.addEffect(new SkillNodeEffect("strength", 2));
        tier3c.addPrerequisite(tier2c);
        skillNodeRepository.save(tier3c);


        // TIER 4 - CAMINHO A

        SkillNode tier4a = createNode(
                "physical_colosso",
                "Titan",
                "Físico dominante",
                SkillCategory.PHYSICAL,
                4, 5, "A",
                100, 260
        );
        tier4a.addEffect(new SkillNodeEffect("strength", 6));
        tier4a.addEffect(new SkillNodeEffect("stamina", 4));
        tier4a.addEffect(new SkillNodeEffect("jumping", 3));
        tier4a.addEffect(new SkillNodeEffect("heading", 3));
        tier4a.addPrerequisite(tier3a);
        skillNodeRepository.save(tier4a);

        // TIER 4 - CAMINHO B

        SkillNode tier4b = createNode(
                "physical_motor_perpetuo",
                "Motor Perpétuo",
                "Energia infinita",
                SkillCategory.PHYSICAL,
                4, 5, "B",
                200, 260
        );
        tier4b.addEffect(new SkillNodeEffect("stamina", 6));
        tier4b.addEffect(new SkillNodeEffect("acceleration", 4));
        tier4b.addEffect(new SkillNodeEffect("sprintSpeed", 3));
        tier4b.addEffect(new SkillNodeEffect("agility", 3));
        tier4b.addPrerequisite(tier3b);
        skillNodeRepository.save(tier4b);


        // TIER 4 - CAMINHO C

        SkillNode tier4c = createNode(
                "physical_gigante_ares",
                "Gigante dos Ares",
                "Rei do jogo aéreo",
                SkillCategory.PHYSICAL,
                4, 5, "C",
                300, 260
        );
        tier4c.addEffect(new SkillNodeEffect("jumping", 6));
        tier4c.addEffect(new SkillNodeEffect("heading", 5));
        tier4c.addEffect(new SkillNodeEffect("strength", 4));
        tier4c.addEffect(new SkillNodeEffect("positioning", 3));
        tier4c.addPrerequisite(tier3c);
        skillNodeRepository.save(tier4c);


        // TIER 5 - ARQUÉTIPO

        SkillNode tier5 = createNode(
                "physical_Camisa_8_Motor",
                "Arquétipo Camisa 8 – Motor",
                "Domínio físico absoluto. O atleta se torna uma força imparável em divididas e disputas aéreas.",
                SkillCategory.PHYSICAL,
                5, 10, null,
                200, 330
        );
        tier5.addEffect(new SkillNodeEffect("strength", 7));
        tier5.addEffect(new SkillNodeEffect("stamina", 7));
        tier5.addEffect(new SkillNodeEffect("jumping", 6));
        tier5.addEffect(new SkillNodeEffect("heading", 5));
        tier5.addEffect(new SkillNodeEffect("agility", 4));
        tier5.addPrerequisite(tier4b);
        skillNodeRepository.save(tier5);

        System.out.println(" PHYSICAL:  11 nós criados");
    }

    private void seedDefending() {

        // TIER 1 - NÓ INICIAL

        SkillNode tier1 = createNode(
                "defending_marcador_basico",
                "Marcador Básico",
                "Fundamentos da marcação",
                SkillCategory.DEFENDING,
                1, 1, null,
                200, 50
        );
        tier1.addEffect(new SkillNodeEffect("marking", 2));
        skillNodeRepository.save(tier1);


        // TIER 2 - CAMINHO A:  POSICIONAL

        SkillNode tier2a = createNode(
                "defending_guardiao",
                "Guardião",
                "Posicionamento perfeito",
                SkillCategory.DEFENDING,
                2, 2, "A",
                100, 120
        );
        tier2a.addEffect(new SkillNodeEffect("marking", 3));
        tier2a.addEffect(new SkillNodeEffect("positioning", 3));
        tier2a.addPrerequisite(tier1);
        skillNodeRepository.save(tier2a);


        // TIER 2 - CAMINHO B:  AGRESSIVO

        SkillNode tier2b = createNode(
                "defending_carrinho_certeiro",
                "Carrinho Certeiro",
                "Desarmes precisos",
                SkillCategory.DEFENDING,
                2, 2, "B",
                200, 120
        );
        tier2b.addEffect(new SkillNodeEffect("standingTackle", 4));
        tier2b.addEffect(new SkillNodeEffect("slidingTackle", 2));
        tier2b.addPrerequisite(tier1);
        skillNodeRepository.save(tier2b);


        // TIER 2 - CAMINHO C:  INTERCEPTADOR

        SkillNode tier2c = createNode(
                "defending_leitura_jogo",
                "Leitura de Jogo",
                "Antecipe as jogadas",
                SkillCategory.DEFENDING,
                2, 2, "C",
                300, 120
        );
        tier2c.addEffect(new SkillNodeEffect("marking", 3));
        tier2c.addEffect(new SkillNodeEffect("shortPassing", 3));
        tier2c.addEffect(new SkillNodeEffect("agility", 2));
        tier2c.addPrerequisite(tier1);
        skillNodeRepository.save(tier2c);


        // TIER 3 - CAMINHO A

        SkillNode tier3a = createNode(
                "defending_zagueiro_implacavel",
                "Zagueiro Implacável",
                "Muralha ",
                SkillCategory.DEFENDING,
                3, 3, "A",
                100, 190
        );
        tier3a.addEffect(new SkillNodeEffect("marking", 5));
        tier3a.addEffect(new SkillNodeEffect("positioning", 4));
        tier3a.addEffect(new SkillNodeEffect("strength", 2));
        tier3a.addPrerequisite(tier2a);
        skillNodeRepository.save(tier3a);


        // TIER 3 - CAMINHO B

        SkillNode tier3b = createNode(
                "defending_destruidor",
                "Destruidor",
                "Desarmes devastadores",
                SkillCategory.DEFENDING,
                3, 3, "B",
                200, 190
        );
        tier3b.addEffect(new SkillNodeEffect("standingTackle", 5));
        tier3b.addEffect(new SkillNodeEffect("slidingTackle", 4));
        tier3b.addEffect(new SkillNodeEffect("strength", 3));
        tier3b.addPrerequisite(tier2b);
        skillNodeRepository.save(tier3b);

        // TIER 3 - CAMINHO C

        SkillNode tier3c = createNode(
                "defending_interceptador_mestre",
                "Interceptador Mestre",
                "Rouba a bola antes mesmo do passe",
                SkillCategory.DEFENDING,
                3, 3, "C",
                300, 190
        );
        tier3c.addEffect(new SkillNodeEffect("marking", 5));
        tier3c.addEffect(new SkillNodeEffect("agility", 3));
        tier3c.addEffect(new SkillNodeEffect("shortPassing", 3));
        tier3c.addPrerequisite(tier2c);
        skillNodeRepository.save(tier3c);

        // TIER 4 - CAMINHO A

        SkillNode tier4a = createNode(
                "defending_lider_defesa",
                "Líder da Defesa",
                "Comanda toda a linha defensiva",
                SkillCategory.DEFENDING,
                4, 5, "A",
                100, 260
        );
        tier4a.addEffect(new SkillNodeEffect("marking", 6));
        tier4a.addEffect(new SkillNodeEffect("positioning", 5));
        tier4a.addEffect(new SkillNodeEffect("heading", 4));
        tier4a.addEffect(new SkillNodeEffect("shortPassing", 3));
        tier4a.addPrerequisite(tier3a);
        skillNodeRepository.save(tier4a);


        // TIER 4 - CAMINHO B

        SkillNode tier4b = createNode(
                "defending_aniquilador",
                "Aniquilador",
                "Ninguém passa",
                SkillCategory. DEFENDING,
                4, 5, "B",
                200, 260
        );
        tier4b.addEffect(new SkillNodeEffect("standingTackle", 6));
        tier4b.addEffect(new SkillNodeEffect("slidingTackle", 5));
        tier4b.addEffect(new SkillNodeEffect("strength", 5));
        tier4b.addEffect(new SkillNodeEffect("marking", 3));
        tier4b.addPrerequisite(tier3b);
        skillNodeRepository.save(tier4b);


        // TIER 4 - CAMINHO C

        SkillNode tier4c = createNode(
                "defending_cerebro_defensivo",
                "Cérebro Defensivo",
                "Inteligência tática superior",
                SkillCategory. DEFENDING,
                4, 5, "C",
                300, 260
        );
        tier4c.addEffect(new SkillNodeEffect("marking", 6));
        tier4c.addEffect(new SkillNodeEffect("agility", 5));
        tier4c.addEffect(new SkillNodeEffect("shortPassing", 4));
        tier4c.addEffect(new SkillNodeEffect("longPassing", 3));
        tier4c.addPrerequisite(tier3c);
        skillNodeRepository.save(tier4c);


        // TIER 5 - ARQUÉTIPO

        SkillNode tier5 = createNode(
                "defending_imperador",
                "Arquétipo Camisa 5 - O imperador",
                "defesa perfeita",
                SkillCategory.DEFENDING,
                5, 10, null,
                200, 330
        );
        tier5.addEffect(new SkillNodeEffect("marking", 8));
        tier5.addEffect(new SkillNodeEffect("standingTackle", 7));
        tier5.addEffect(new SkillNodeEffect("slidingTackle", 6));
        tier5.addEffect(new SkillNodeEffect("positioning", 6));
        tier5.addEffect(new SkillNodeEffect("strength", 5));
        tier5.addEffect(new SkillNodeEffect("heading", 4));
        tier5.addPrerequisite(tier4a);
        skillNodeRepository.save(tier5);

        System.out.println(" DEFENDING:  11 nós criados");
    }

    private void seedDribbling() {

        // TIER 1 - NÓ INICIAL

        SkillNode tier1 = createNode(
                "dribbling_toque_bola",
                "Toque de Bola",
                "Primeiros toques técnicos",
                SkillCategory.DRIBBLING,
                1, 1, null,
                200, 50
        );
        tier1.addEffect(new SkillNodeEffect("ballControl", 2));
        skillNodeRepository.save(tier1);


        // TIER 2 - CAMINHO A:  VELOCIDADE

        SkillNode tier2a = createNode(
                "dribbling_driblador_veloz",
                "Driblador Veloz",
                "Drible em alta velocidade",
                SkillCategory.DRIBBLING,
                2, 2, "A",
                100, 120
        );
        tier2a.addEffect(new SkillNodeEffect("dribbling", 3));
        tier2a.addEffect(new SkillNodeEffect("acceleration", 3));
        tier2a.addPrerequisite(tier1);
        skillNodeRepository.save(tier2a);


        // TIER 2 - CAMINHO B:  TÉCNICA

        SkillNode tier2b = createNode(
                "dribbling_pes_veludo",
                "Pés de Veludo",
                "Controle suave e preciso",
                SkillCategory.DRIBBLING,
                2, 2, "B",
                200, 120
        );
        tier2b.addEffect(new SkillNodeEffect("ballControl", 4));
        tier2b.addEffect(new SkillNodeEffect("dribbling", 2));
        tier2b.addPrerequisite(tier1);
        skillNodeRepository.save(tier2b);


        // TIER 2 - CAMINHO C:   CRIATIVIDADE

        SkillNode tier2c = createNode(
                "dribbling_fintador",
                "Fintador",
                "Fintas desconcertantes",
                SkillCategory. DRIBBLING,
                2, 2, "C",
                300, 120
        );
        tier2c.addEffect(new SkillNodeEffect("dribbling", 3));
        tier2c.addEffect(new SkillNodeEffect("agility", 3));
        tier2c.addPrerequisite(tier1);
        skillNodeRepository.save(tier2c);


        // TIER 3 - CAMINHO A

        SkillNode tier3a = createNode(
                "dribbling_bala_prata",
                "Bala de Prata",
                "Drible explosivo",
                SkillCategory.DRIBBLING,
                3, 3, "A",
                100, 190
        );
        tier3a.addEffect(new SkillNodeEffect("dribbling", 5));
        tier3a.addEffect(new SkillNodeEffect("acceleration", 4));
        tier3a.addEffect(new SkillNodeEffect("sprintSpeed", 3));
        tier3a.addPrerequisite(tier2a);
        skillNodeRepository.save(tier3a);


        // TIER 3 - CAMINHO B

        SkillNode tier3b = createNode(
                "dribbling_malabarista",
                "Malabarista",
                "Controle absoluto da bola",
                SkillCategory.DRIBBLING,
                3, 3, "B",
                200, 190
        );
        tier3b.addEffect(new SkillNodeEffect("ballControl", 5));
        tier3b.addEffect(new SkillNodeEffect("dribbling", 4));
        tier3b.addEffect(new SkillNodeEffect("shortPassing", 2));
        tier3b.addPrerequisite(tier2b);
        skillNodeRepository.save(tier3b);


        // TIER 3 - CAMINHO C

        SkillNode tier3c = createNode(
                "dribbling_ilusionista",
                "Ilusionista",
                "Cria ilusões nos adversários",
                SkillCategory.DRIBBLING,
                3, 3, "C",
                300, 190
        );
        tier3c.addEffect(new SkillNodeEffect("dribbling", 5));
        tier3c.addEffect(new SkillNodeEffect("agility", 4));
        tier3c.addEffect(new SkillNodeEffect("ballControl", 3));
        tier3c.addPrerequisite(tier2c);
        skillNodeRepository.save(tier3c);


        // TIER 4 - CAMINHO A

        SkillNode tier4a = createNode(
                "dribbling_velocista_tecnico",
                "Velocista Técnico",
                "Velocidade com controle perfeito",
                SkillCategory.DRIBBLING,
                4, 5, "A",
                100, 260
        );
        tier4a.addEffect(new SkillNodeEffect("dribbling", 6));
        tier4a.addEffect(new SkillNodeEffect("acceleration", 5));
        tier4a.addEffect(new SkillNodeEffect("sprintSpeed", 4));
        tier4a.addEffect(new SkillNodeEffect("ballControl", 3));
        tier4a.addPrerequisite(tier3a);
        skillNodeRepository.save(tier4a);


        // TIER 4 - CAMINHO B

        SkillNode tier4b = createNode(
                "dribbling_feiticeiro",
                "Feiticeiro",
                "Magia nos pés",
                SkillCategory.DRIBBLING,
                4, 5, "B",
                200, 260
        );
        tier4b.addEffect(new SkillNodeEffect("ballControl", 6));
        tier4b.addEffect(new SkillNodeEffect("dribbling", 5));
        tier4b.addEffect(new SkillNodeEffect("shortPassing", 4));
        tier4b.addEffect(new SkillNodeEffect("agility", 3));
        tier4b.addPrerequisite(tier3b);
        skillNodeRepository.save(tier4b);


        // TIER 4 - CAMINHO C

        SkillNode tier4c = createNode(
                "dribbling_artista_bola",
                "Artista da Bola",
                "Arte pura com a bola nos pés",
                SkillCategory.DRIBBLING,
                4, 5, "C",
                300, 260
        );
        tier4c.addEffect(new SkillNodeEffect("dribbling", 6));
        tier4c.addEffect(new SkillNodeEffect("agility", 5));
        tier4c.addEffect(new SkillNodeEffect("ballControl", 5));
        tier4c.addEffect(new SkillNodeEffect("acceleration", 3));
        tier4c.addPrerequisite(tier3c);
        skillNodeRepository.save(tier4c);

        // ========================================
        // TIER 5 - ARQUÉTIPO
        // ========================================
        SkillNode tier5 = createNode(
                "dribbling_mago_drible",
                "Arquétipo Camisa 10 - O Bruxo",
                "Mestre absoluto da condução de bola",
                SkillCategory.DRIBBLING,
                5, 10, null,
                200, 330
        );
        tier5.addEffect(new SkillNodeEffect("dribbling", 8));
        tier5.addEffect(new SkillNodeEffect("ballControl", 7));
        tier5.addEffect(new SkillNodeEffect("agility", 6));
        tier5.addEffect(new SkillNodeEffect("acceleration", 5));
        tier5.addEffect(new SkillNodeEffect("sprintSpeed", 5));
        tier5.addEffect(new SkillNodeEffect("shortPassing", 4));
        tier5.addPrerequisite(tier4b);
        skillNodeRepository.save(tier5);

        System.out.println(" DRIBBLING:  11 nós criados");
    }

    private void seedPassing() {

        // TIER 1 - NÓ INICIAL

        SkillNode tier1 = createNode(
                "passing_distribuidor",
                "Distribuidor",
                "Primeiros passes com qualidade",
                SkillCategory.PASSING,
                1, 1, null,
                200, 50
        );
        tier1.addEffect(new SkillNodeEffect("shortPassing", 2));
        skillNodeRepository.save(tier1);


        // TIER 2 - CAMINHO A:  PRECISÃO

        SkillNode tier2a = createNode(
                "passing_passe_cirurgico",
                "Passe Cirúrgico",
                "Precisão milimétrica nos passes curtos",
                SkillCategory.PASSING,
                2, 2, "A",
                100, 120
        );
        tier2a.addEffect(new SkillNodeEffect("shortPassing", 4));
        tier2a.addEffect(new SkillNodeEffect("ballControl", 2));
        tier2a.addPrerequisite(tier1);
        skillNodeRepository.save(tier2a);


        // TIER 2 - CAMINHO B:  LANÇAMENTO

        SkillNode tier2b = createNode(
                "passing_lancador",
                "Lançador",
                "Bolas longas precisas",
                SkillCategory.PASSING,
                2, 2, "B",
                200, 120
        );
        tier2b.addEffect(new SkillNodeEffect("longPassing", 4));
        tier2b.addEffect(new SkillNodeEffect("shortPassing", 2));
        tier2b.addPrerequisite(tier1);
        skillNodeRepository.save(tier2b);


        // TIER 2 - CAMINHO C:   VISÃO

        SkillNode tier2c = createNode(
                "passing_armador",
                "Armador",
                "Visão de jogo privilegiada",
                SkillCategory.PASSING,
                2, 2, "C",
                300, 120
        );
        tier2c.addEffect(new SkillNodeEffect("shortPassing", 3));
        tier2c.addEffect(new SkillNodeEffect("longPassing", 3));
        tier2c.addPrerequisite(tier1);
        skillNodeRepository.save(tier2c);

        // TIER 3 - CAMINHO A

        SkillNode tier3a = createNode(
                "passing_construtor",
                "Construtor",
                "Constrói jogadas com passes curtos",
                SkillCategory.PASSING,
                3, 3, "A",
                100, 190
        );
        tier3a.addEffect(new SkillNodeEffect("shortPassing", 5));
        tier3a.addEffect(new SkillNodeEffect("ballControl", 3));
        tier3a.addEffect(new SkillNodeEffect("dribbling", 3));
        tier3a.addPrerequisite(tier2a);
        skillNodeRepository.save(tier3a);


        // TIER 3 - CAMINHO B

        SkillNode tier3b = createNode(
                "passing_quarterback",
                "Quarterback",
                "Lançamentos longos perfeitos",
                SkillCategory.PASSING,
                3, 3, "B",
                200, 190
        );
        tier3b.addEffect(new SkillNodeEffect("longPassing", 5));
        tier3b.addEffect(new SkillNodeEffect("shortPassing", 3));
        tier3b.addEffect(new SkillNodeEffect("positioning", 2));
        tier3b.addPrerequisite(tier2b);
        skillNodeRepository.save(tier3b);


        // TIER 3 - CAMINHO C

        SkillNode tier3c = createNode(
                "passing_visao_jogo",
                "Visão de Jogo",
                "Enxerga passes que outros não veem",
                SkillCategory.PASSING,
                3, 3, "C",
                300, 190
        );
        tier3c.addEffect(new SkillNodeEffect("shortPassing", 4));
        tier3c.addEffect(new SkillNodeEffect("longPassing", 4));
        tier3c.addEffect(new SkillNodeEffect("positioning", 3));
        tier3c.addPrerequisite(tier2c);
        skillNodeRepository.save(tier3c);


        // TIER 4 - CAMINHO A

        SkillNode tier4a = createNode(
                "passing_cerebro_time",
                "Cérebro do Time",
                "Comanda o meio-campo com passes curtos",
                SkillCategory.PASSING,
                4, 5, "A",
                100, 260
        );
        tier4a.addEffect(new SkillNodeEffect("shortPassing", 6));
        tier4a.addEffect(new SkillNodeEffect("ballControl", 4));
        tier4a.addEffect(new SkillNodeEffect("dribbling", 4));
        tier4a.addEffect(new SkillNodeEffect("agility", 3));
        tier4a.addPrerequisite(tier3a);
        skillNodeRepository.save(tier4a);


        // TIER 4 - CAMINHO B

        SkillNode tier4b = createNode(
                "passing_artilheiro_assistencias",
                "Artilheiro de Assistências",
                "Passes que viram gols",
                SkillCategory.PASSING,
                4, 5, "B",
                200, 260
        );
        tier4b.addEffect(new SkillNodeEffect("longPassing", 6));
        tier4b.addEffect(new SkillNodeEffect("shortPassing", 5));
        tier4b.addEffect(new SkillNodeEffect("positioning", 4));
        tier4b.addEffect(new SkillNodeEffect("finishing", 3));
        tier4b.addPrerequisite(tier3b);
        skillNodeRepository.save(tier4b);


        // TIER 4 - CAMINHO C

        SkillNode tier4c = createNode(
                "passing_regente",
                "orquestrador",
                "Rege a orquestra do meio-campo",
                SkillCategory.PASSING,
                4, 5, "C",
                300, 260
        );
        tier4c.addEffect(new SkillNodeEffect("shortPassing", 5));
        tier4c.addEffect(new SkillNodeEffect("longPassing", 5));
        tier4c.addEffect(new SkillNodeEffect("positioning", 5));
        tier4c.addEffect(new SkillNodeEffect("ballControl", 3));
        tier4c.addPrerequisite(tier3c);
        skillNodeRepository.save(tier4c);


        // TIER 5 - ARQUÉTIPO

        SkillNode tier5 = createNode(
                "passing_maestro",
                "Arquétipo Camisa 8 – Mago",
                "O regente perfeito - domina todos os tipos de passe",
                SkillCategory.PASSING,
                5, 10, null,
                200, 330
        );
        tier5.addEffect(new SkillNodeEffect("shortPassing", 8));
        tier5.addEffect(new SkillNodeEffect("longPassing", 8));
        tier5.addEffect(new SkillNodeEffect("ballControl", 6));
        tier5.addEffect(new SkillNodeEffect("positioning", 6));
        tier5.addEffect(new SkillNodeEffect("dribbling", 5));
        tier5.addEffect(new SkillNodeEffect("agility", 4));
        tier5.addPrerequisite(tier4c);
        skillNodeRepository.save(tier5);

        System.out.println(" PASSING:  11 nós criados");
    }

    private void seedPace() {

        // TIER 1 - NÓ INICIAL

        SkillNode tier1 = createNode(
                "pace_corredor",
                "Corredor",
                "Primeiros passos na velocidade",
                SkillCategory.PACE,
                1, 1, null,
                200, 50
        );
        tier1.addEffect(new SkillNodeEffect("sprintSpeed", 2));
        skillNodeRepository.save(tier1);


        // TIER 2 - CAMINHO A:  EXPLOSÃO

        SkillNode tier2a = createNode(
                "pace_arrancada_fulminante",
                "Arrancada Fulminante",
                "Explosão nos primeiros metros",
                SkillCategory. PACE,
                2, 2, "A",
                100, 120
        );
        tier2a.addEffect(new SkillNodeEffect("acceleration", 4));
        tier2a.addEffect(new SkillNodeEffect("strength", 2));
        tier2a.addPrerequisite(tier1);
        skillNodeRepository.save(tier2a);


        // TIER 2 - CAMINHO B:  VELOCIDADE MÁXIMA

        SkillNode tier2b = createNode(
                "pace_velocista",
                "Velocista",
                "Velocidade de ponta máxima",
                SkillCategory.PACE,
                2, 2, "B",
                200, 120
        );
        tier2b.addEffect(new SkillNodeEffect("sprintSpeed", 4));
        tier2b.addEffect(new SkillNodeEffect("stamina", 2));
        tier2b.addPrerequisite(tier1);
        skillNodeRepository.save(tier2b);


        // TIER 2 - CAMINHO C:   MOBILIDADE

        SkillNode tier2c = createNode(
                "pace_agil",
                "Ágil",
                "Mobilidade e mudanças de direção",
                SkillCategory.PACE,
                2, 2, "C",
                300, 120
        );
        tier2c.addEffect(new SkillNodeEffect("agility", 3));
        tier2c.addEffect(new SkillNodeEffect("acceleration", 3));
        tier2c.addPrerequisite(tier1);
        skillNodeRepository.save(tier2c);


        // TIER 3 - CAMINHO A

        SkillNode tier3a = createNode(
                "pace_explosivo",
                "Explosivo",
                "Aceleração devastadora",
                SkillCategory.PACE,
                3, 3, "A",
                100, 190
        );
        tier3a.addEffect(new SkillNodeEffect("acceleration", 5));
        tier3a.addEffect(new SkillNodeEffect("strength", 3));
        tier3a.addEffect(new SkillNodeEffect("agility", 3));
        tier3a.addPrerequisite(tier2a);
        skillNodeRepository.save(tier3a);


        // TIER 3 - CAMINHO B

        SkillNode tier3b = createNode(
                "pace_foguete",
                "Foguete",
                "Velocidade supersônica",
                SkillCategory.PACE,
                3, 3, "B",
                200, 190
        );
        tier3b.addEffect(new SkillNodeEffect("sprintSpeed", 5));
        tier3b.addEffect(new SkillNodeEffect("stamina", 4));
        tier3b.addEffect(new SkillNodeEffect("acceleration", 2));
        tier3b.addPrerequisite(tier2b);
        skillNodeRepository.save(tier3b);


        // TIER 3 - CAMINHO C

        SkillNode tier3c = createNode(
                "pace_liso",
                "Liso",
                "Agilidade",
                SkillCategory.PACE,
                3, 3, "C",
                300, 190
        );
        tier3c.addEffect(new SkillNodeEffect("agility", 5));
        tier3c.addEffect(new SkillNodeEffect("acceleration", 4));
        tier3c.addEffect(new SkillNodeEffect("dribbling", 2));
        tier3c.addPrerequisite(tier2c);
        skillNodeRepository.save(tier3c);


        // TIER 4 - CAMINHO A

        SkillNode tier4a = createNode(
                "pace_primeira_marcha",
                "Primeira Marcha",
                "Aceleração instantânea",
                SkillCategory.PACE,
                4, 5, "A",
                100, 260
        );
        tier4a.addEffect(new SkillNodeEffect("acceleration", 6));
        tier4a.addEffect(new SkillNodeEffect("strength", 4));
        tier4a.addEffect(new SkillNodeEffect("agility", 4));
        tier4a.addEffect(new SkillNodeEffect("sprintSpeed", 3));
        tier4a.addPrerequisite(tier3a);
        skillNodeRepository.save(tier4a);


        // TIER 4 - CAMINHO B

        SkillNode tier4b = createNode(
                "pace_supersonico",
                "Supersônico",
                "Quebra a barreira do som",
                SkillCategory.PACE,
                4, 5, "B",
                200, 260
        );
        tier4b.addEffect(new SkillNodeEffect("sprintSpeed", 6));
        tier4b.addEffect(new SkillNodeEffect("stamina", 5));
        tier4b.addEffect(new SkillNodeEffect("acceleration", 4));
        tier4b.addEffect(new SkillNodeEffect("dribbling", 3));
        tier4b.addPrerequisite(tier3b);
        skillNodeRepository.save(tier4b);


        // TIER 4 - CAMINHO C

        SkillNode tier4c = createNode(
                "pace_mercurio",
                "Mercúrio",
                "O mensageiro dos deuses - velocidade e agilidade",
                SkillCategory.PACE,
                4, 5, "C",
                300, 260
        );
        tier4c.addEffect(new SkillNodeEffect("agility", 6));
        tier4c.addEffect(new SkillNodeEffect("acceleration", 5));
        tier4c.addEffect(new SkillNodeEffect("dribbling", 4));
        tier4c.addEffect(new SkillNodeEffect("ballControl", 3));
        tier4c.addPrerequisite(tier3c);
        skillNodeRepository.save(tier4c);


        // TIER 5 - ARQUÉTIPO

        SkillNode tier5 = createNode(
                "pace_relampago",
                "Arquétipo Camisa 13 - Pantera Negra",
                "Velocidade absoluta - impossível de acompanhar",
                SkillCategory.PACE,
                5, 10, null,
                200, 330
        );
        tier5.addEffect(new SkillNodeEffect("acceleration", 8));
        tier5.addEffect(new SkillNodeEffect("sprintSpeed", 8));
        tier5.addEffect(new SkillNodeEffect("agility", 7));
        tier5.addEffect(new SkillNodeEffect("stamina", 5));
        tier5.addEffect(new SkillNodeEffect("dribbling", 5));
        tier5.addEffect(new SkillNodeEffect("strength", 4));
        tier5.addPrerequisite(tier4b);
        skillNodeRepository.save(tier5);

        System.out.println(" PACE:  11 nós criados");
    }
    private void seedGoalkeeper() {

        // TIER 1 - NÓ INICIAL

        SkillNode tier1 = createNode(
                "goalkeeper_guardiao_iniciante",
                "Guardião Iniciante",
                "Fundamentos da defesa de gol",
                SkillCategory.GOALKEEPER,
                1, 1, null,
                200, 50
        );
        tier1.addEffect(new SkillNodeEffect("positioning", 2));
        skillNodeRepository.save(tier1);


        // TIER 2 - CAMINHO A:  REFLEXOS
        SkillNode tier2a = createNode(
                "goalkeeper_reflexos_felinos",
                "Reflexos Felinos",
                "Reações rápidas como um gato",
                SkillCategory.GOALKEEPER,
                2, 2, "A",
                100, 120
        );
        tier2a.addEffect(new SkillNodeEffect("marking", 4));
        tier2a. addEffect(new SkillNodeEffect("agility", 2));
        tier2a.addPrerequisite(tier1);
        skillNodeRepository.save(tier2a);


        // TIER 2 - CAMINHO B:  POSICIONAMENTO

        SkillNode tier2b = createNode(
                "goalkeeper_defensor_meta",
                "Defensor da Meta",
                "Sempre na posição perfeita",
                SkillCategory.GOALKEEPER,
                2, 2, "B",
                200, 120
        );
        tier2b.addEffect(new SkillNodeEffect("positioning", 4));
        tier2b.addEffect(new SkillNodeEffect("marking", 2));
        tier2b.addPrerequisite(tier1);
        skillNodeRepository.save(tier2b);

        // ========================================
        // TIER 2 - CAMINHO C:   ALCANCE
        // ========================================
        SkillNode tier2c = createNode(
                "goalkeeper_longo_alcance",
                "Longo Alcance",
                "Alcance impressionante",
                SkillCategory.GOALKEEPER,
                2, 2, "C",
                300, 120
        );
        tier2c.addEffect(new SkillNodeEffect("jumping", 4));
        tier2c.addEffect(new SkillNodeEffect("strength", 3));
        tier2c.addPrerequisite(tier1);
        skillNodeRepository.save(tier2c);


        // TIER 3 - CAMINHO A

        SkillNode tier3a = createNode(
                "goalkeeper_muralha",
                "Muralha",
                "Defesas impossíveis",
                SkillCategory.GOALKEEPER,
                3, 3, "A",
                100, 190
        );
        tier3a.addEffect(new SkillNodeEffect("marking", 5));
        tier3a.addEffect(new SkillNodeEffect("agility", 4));
        tier3a.addEffect(new SkillNodeEffect("positioning", 2));
        tier3a.addPrerequisite(tier2a);
        skillNodeRepository.save(tier3a);


        // TIER 3 - CAMINHO B

        SkillNode tier3b = createNode(
                "goalkeeper_guardiao_implacavel",
                "Guardião Implacável",
                "Leitura perfeita do jogo",
                SkillCategory.GOALKEEPER,
                3, 3, "B",
                200, 190
        );
        tier3b.addEffect(new SkillNodeEffect("positioning", 5));
        tier3b.addEffect(new SkillNodeEffect("marking", 4));
        tier3b.addEffect(new SkillNodeEffect("longPassing", 3));  // Distribuição
        tier3b.addPrerequisite(tier2b);
        skillNodeRepository.save(tier3b);


        // TIER 3 - CAMINHO C

        SkillNode tier3c = createNode(
                "goalkeeper_gigante_area",
                "Gigante da Área",
                "Domínio absoluto da área",
                SkillCategory. GOALKEEPER,
                3, 3, "C",
                300, 190
        );
        tier3c.addEffect(new SkillNodeEffect("jumping", 5));
        tier3c.addEffect(new SkillNodeEffect("strength", 4));
        tier3c.addEffect(new SkillNodeEffect("heading", 3));
        tier3c.addPrerequisite(tier2c);
        skillNodeRepository.save(tier3c);


        // TIER 4 - CAMINHO A

        SkillNode tier4a = createNode(
                "goalkeeper_paredao",
                "Paredão",
                "Nada passa por você",
                SkillCategory. GOALKEEPER,
                4, 5, "A",
                100, 260
        );
        tier4a.addEffect(new SkillNodeEffect("marking", 6));
        tier4a.addEffect(new SkillNodeEffect("agility", 5));
        tier4a.addEffect(new SkillNodeEffect("positioning", 4));
        tier4a.addEffect(new SkillNodeEffect("jumping", 3));
        tier4a.addPrerequisite(tier3a);
        skillNodeRepository.save(tier4a);

        // TIER 4 - CAMINHO B

        SkillNode tier4b = createNode(
                "goalkeeper_intransponivel",
                "Intransponível",
                "O gol está fechado",
                SkillCategory.GOALKEEPER,
                4, 5, "B",
                200, 260
        );
        tier4b.addEffect(new SkillNodeEffect("positioning", 6));
        tier4b.addEffect(new SkillNodeEffect("marking", 5));
        tier4b.addEffect(new SkillNodeEffect("longPassing", 4));
        tier4b.addEffect(new SkillNodeEffect("agility", 3));
        tier4b.addPrerequisite(tier3b);
        skillNodeRepository.save(tier4b);

        // TIER 4 - CAMINHO C

        SkillNode tier4c = createNode(
                "goalkeeper_senhor_area",
                "Senhor da Área",
                "Rei absoluto da grande área",
                SkillCategory.GOALKEEPER,
                4, 5, "C",
                300, 260
        );
        tier4c.addEffect(new SkillNodeEffect("jumping", 6));
        tier4c.addEffect(new SkillNodeEffect("strength", 5));
        tier4c.addEffect(new SkillNodeEffect("positioning", 4));
        tier4c.addEffect(new SkillNodeEffect("heading", 3));
        tier4c.addPrerequisite(tier3c);
        skillNodeRepository.save(tier4c);


        // TIER 5 - ARQUÉTIPO

        SkillNode tier5 = createNode(
                "goalkeeper_sao_goleiro",
                "Arquétipo Camisa 1 - Polvo",
                "O santo padroeiro dos goleiros - perfeição divina",
                SkillCategory.GOALKEEPER,
                5, 10, null,
                200, 330
        );
        tier5.addEffect(new SkillNodeEffect("marking", 8));
        tier5.addEffect(new SkillNodeEffect("positioning", 7));
        tier5.addEffect(new SkillNodeEffect("agility", 7));
        tier5.addEffect(new SkillNodeEffect("jumping", 6));
        tier5.addEffect(new SkillNodeEffect("strength", 5));
        tier5.addEffect(new SkillNodeEffect("longPassing", 5));
        tier5.addPrerequisite(tier4b);
        skillNodeRepository.save(tier5);

        System.out.println(" GOALKEEPER:  11 nós criados");
    }
}