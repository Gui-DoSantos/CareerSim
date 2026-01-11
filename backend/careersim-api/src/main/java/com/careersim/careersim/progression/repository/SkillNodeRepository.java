package com.careersim.careersim.progression.repository;

import com.careersim.careersim.progression.model.SkillCategory;
import com.careersim.careersim.progression.model.SkillNode;
import org.springframework.data.jpa. repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org. springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SkillNodeRepository extends JpaRepository<SkillNode, String> {

    // Buscar todos os nós de uma categoria
    List<SkillNode> findByCategory(SkillCategory category);

    // Buscar todos os nós de uma categoria ordenados por tier
    List<SkillNode> findByCategoryOrderByTierAsc(SkillCategory category);

    // Buscar nós de um tier específico
    List<SkillNode> findByTier(Integer tier);

    // Buscar nós de uma categoria e tier
    List<SkillNode> findByCategoryAndTier(SkillCategory category, Integer tier);

    // Buscar nós de um caminho específico
    List<SkillNode> findByCategoryAndPath(SkillCategory category, String path);

    // Verificar se existe um nó
    boolean existsById(String id);
}