package com.careersim.careersim. progression.repository;

import com. careersim.careersim. progression.model.SkillNodeEffect;
import org.springframework. data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SkillNodeEffectRepository extends JpaRepository<SkillNodeEffect, UUID> {

    // Buscar efeitos de um nó específico
    List<SkillNodeEffect> findBySkillNodeId(String skillNodeId);

    // Deletar efeitos de um nó
    void deleteBySkillNodeId(String skillNodeId);
}