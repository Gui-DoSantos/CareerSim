package com.careersim.careersim.progression.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok. NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "skill_nodes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SkillNode {

    @Id
    @Column(length = 50)
    private String id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SkillCategory category;

    @Column(nullable = false)
    private Integer tier;

    @Column(nullable = false)
    private Integer cost;

    @Column(length = 20)
    private String path;

    @Column(length = 50)
    private String icon;

    @Column(name = "position_x")
    private Integer positionX;

    @Column(name = "position_y")
    private Integer positionY;

    @OneToMany(mappedBy = "skillNode", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<SkillNodeEffect> effects = new ArrayList<>();


    @ManyToMany
    @JoinTable(
            name = "skill_node_prerequisites",
            joinColumns = @JoinColumn(name = "skill_node_id"),
            inverseJoinColumns = @JoinColumn(name = "required_node_id")
    )
    private List<SkillNode> prerequisites = new ArrayList<>();

    public SkillNode(String id, String name, SkillCategory category, Integer tier, Integer cost, String path) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.tier = tier;
        this.cost = cost;
        this.path = path;
    }


    public void addEffect(SkillNodeEffect effect) {
        effects.add(effect);
        effect.setSkillNode(this);
    }

    public void addPrerequisite(SkillNode prerequisite) {
        this.prerequisites.add(prerequisite);
    }
}