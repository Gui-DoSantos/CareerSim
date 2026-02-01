package com.careersim.careersim.transfer.model;

import com.careersim.careersim.club.model.Club;
import com.careersim.careersim.player.model.Player;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "transfer_offers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferOffer {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_club_id")
    private Club fromClub;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_club_id", nullable = false)
    private Club toClub;

    @Column(name = "offer_amount", nullable = false)
    private Long offerAmount;

    @Column(name = "salary_offered", nullable = false)
    private Long salaryOffered;

    @Column(name = "contract_years", nullable = false)
    private Integer contractYears;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TransferOfferStatus status = TransferOfferStatus.PENDING;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @Column(name = "responded_at")
    private LocalDateTime respondedAt;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // ========================================
    // CONSTRUTOR AUXILIAR
    // ========================================
    public TransferOffer(Player player, Club fromClub, Club toClub,
                         Long offerAmount, Long salaryOffered, Integer contractYears,
                         LocalDateTime expiresAt) {
        this.player = player;
        this.fromClub = fromClub;
        this.toClub = toClub;
        this.offerAmount = offerAmount;
        this.salaryOffered = salaryOffered;
        this.contractYears = contractYears;
        this.expiresAt = expiresAt;
        this.status = TransferOfferStatus.PENDING;
    }

    // ========================================
    // MÉTODOS AUXILIARES
    // ========================================
    public void accept() {
        this.status = TransferOfferStatus.ACCEPTED;
        this.respondedAt = LocalDateTime.now();
    }

    public void reject() {
        this.status = TransferOfferStatus.REJECTED;
        this.respondedAt = LocalDateTime.now();
    }

    public void expire() {
        this.status = TransferOfferStatus.EXPIRED;
        this.respondedAt = LocalDateTime.now();
    }

    public boolean isPending() {
        return this.status == TransferOfferStatus.PENDING;
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(this.expiresAt);
    }

    public boolean canBeAccepted() {
        return isPending() && !isExpired();
    }
}