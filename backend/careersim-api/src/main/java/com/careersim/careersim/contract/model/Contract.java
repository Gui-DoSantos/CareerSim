package com.careersim.careersim.contract.model;

import com.careersim.careersim.club.model.Club;
import com.careersim.careersim.player.model.Player;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Entity
@Table(name = "contracts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Contract {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id", nullable = false)
    private Club club;

    @Column(nullable = false)
    private Long salary;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ContractStatus status = ContractStatus.ACTIVE;

    @Column(name = "signing_bonus")
    private Long signingBonus = 0L;

    @Column(name = "release_clause")
    private Long releaseClause;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public Contract(Player player, Club club, Long salary, LocalDate startDate, LocalDate endDate) {
        this.player = player;
        this.club = club;
        this.salary = salary;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = ContractStatus.ACTIVE;
        this.signingBonus = 0L;
    }

    public Contract(Player player, Club club, Long salary, LocalDate startDate, LocalDate endDate,
                    Long signingBonus, Long releaseClause) {
        this.player = player;
        this.club = club;
        this.salary = salary;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = ContractStatus.ACTIVE;
        this.signingBonus = signingBonus;
        this.releaseClause = releaseClause;
    }



    public boolean isActive() {
        return this.status == ContractStatus.ACTIVE;
    }

    public boolean isExpired() {
        return LocalDate.now().isAfter(this.endDate);
    }

    public boolean isExpiringSoon() {
        // faltam menos de 6 meses
        return isActive() && getDaysRemaining() <= 180;
    }

    public long getDaysRemaining() {
        if (isExpired()) {
            return 0;
        }
        return ChronoUnit.DAYS.between(LocalDate.now(), this.endDate);
    }

    public long getMonthsRemaining() {
        if (isExpired()) {
            return 0;
        }
        return ChronoUnit.MONTHS.between(LocalDate.now(), this.endDate);
    }

    public long getYearsRemaining() {
        if (isExpired()) {
            return 0;
        }
        return ChronoUnit.YEARS.between(LocalDate.now(), this.endDate);
    }

    public long getTotalDuration() {
        return ChronoUnit.DAYS.between(this.startDate, this.endDate);
    }

    public long getTotalYears() {
        return ChronoUnit.YEARS.between(this.startDate, this.endDate);
    }

    public void expire() {
        this.status = ContractStatus.EXPIRED;
    }

    public void terminate() {
        this.status = ContractStatus.TERMINATED;
    }

    public void renew() {
        this.status = ContractStatus.RENEWED;
    }

    public boolean canBeRenewed() {
        // Pode renovar se está ativo e faltam menos de 1 ano
        return isActive() && getDaysRemaining() <= 365;
    }

    public boolean canBeTerminated() {
        return isActive();
    }

    public long getTotalValue() {
        // Valor total do contrato (salário × anos + bônus)
        long years = getTotalYears();
        return (salary * years) + signingBonus;
    }

    public String getContractSummary() {
        return String.format(
                "Contract with %s: $%,d/year until %s (%d years remaining)",
                club.getName(),
                salary,
                endDate,
                getYearsRemaining()
        );
    }
}