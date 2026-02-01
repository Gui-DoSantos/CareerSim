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
@Table(name = "transfers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transfer {

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

    @Column(name = "transfer_amount", nullable = false)
    private Long transferAmount;

    @Column(nullable = false)
    private Long salary;

    @Column(name = "contract_years", nullable = false)
    private Integer contractYears;

    @Column(name = "transfer_date", nullable = false)
    private LocalDateTime transferDate;

    @Column(nullable = false)
    private Integer season;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;


    public Transfer(Player player, Club fromClub, Club toClub,
                    Long transferAmount, Long salary, Integer contractYears) {
        this.player = player;
        this.fromClub = fromClub;
        this.toClub = toClub;
        this.transferAmount = transferAmount;
        this.salary = salary;
        this.contractYears = contractYears;
        this.transferDate = LocalDateTime.now();
        this.season = LocalDateTime.now().getYear();
    }


    public boolean isFreeTransfer() {
        return fromClub == null || transferAmount == 0;
    }

    public String getTransferSummary() {
        if (isFreeTransfer()) {
            return String.format("Free transfer to %s", toClub.getName());
        }
        return String.format("Transfer from %s to %s for $%,d",
                fromClub.getName(), toClub.getName(), transferAmount);
    }
}