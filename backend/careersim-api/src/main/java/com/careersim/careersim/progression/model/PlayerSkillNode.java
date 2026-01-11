package com.careersim.careersim.progression.model;

import com.careersim.careersim.player.model.Player;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io. Serializable;
import java.time.LocalDateTime;
import java. util.Objects;
import java.util.UUID;

@Entity
@Table(name = "player_skill_nodes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlayerSkillNode {

    @EmbeddedId
    private PlayerSkillNodeId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("playerId")
    @JoinColumn(name = "player_id", columnDefinition = "BINARY(16)")
    private Player player;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("skillNodeId")
    @JoinColumn(name = "skill_node_id")
    private SkillNode skillNode;

    @Column(name = "unlocked_at")
    private LocalDateTime unlockedAt;

    @PrePersist
    protected void onCreate() {
        if (this. unlockedAt == null) {
            this.unlockedAt = LocalDateTime.now();
        }
    }

    public PlayerSkillNode(Player player, SkillNode skillNode) {
        this.player = player;
        this.skillNode = skillNode;
        this.id = new PlayerSkillNodeId(player.getId(), skillNode.getId());
        this.unlockedAt = LocalDateTime.now();
    }



    @Embeddable
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PlayerSkillNodeId implements Serializable {

        @Column(name = "player_id", columnDefinition = "BINARY(16)")
        private UUID playerId;

        @Column(name = "skill_node_id", length = 50)
        private String skillNodeId;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            PlayerSkillNodeId that = (PlayerSkillNodeId) o;
            return Objects.equals(playerId, that. playerId) &&
                    Objects.equals(skillNodeId, that.skillNodeId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(playerId, skillNodeId);
        }
    }
}