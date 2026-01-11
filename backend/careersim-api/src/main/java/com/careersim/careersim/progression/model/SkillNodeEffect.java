package com.careersim.careersim.progression.model;

import jakarta.persistence.*;
import lombok. AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "skill_node_effects")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SkillNodeEffect {

    @Id
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "skill_node_id", nullable = false)
    private SkillNode skillNode;

    @Column(name = "attribute_name", nullable = false, length = 30)
    private String attributeName;

    @Column(name = "increase_amount", nullable = false)
    private Integer increaseAmount;

    @PrePersist
    protected void onCreate() {
        if (this. id == null) {
            this. id = UUID.randomUUID();
        }
    }


    public SkillNodeEffect(String attributeName, Integer increaseAmount) {
        this.attributeName = attributeName;
        this.increaseAmount = increaseAmount;
    }
}