package com.careersim.careersim.contract.repository;

import com.careersim.careersim.contract.model.Contract;
import com.careersim.careersim.contract.model.ContractStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ContractRepository extends JpaRepository<Contract, UUID> {

    // Buscar contrato ativo de um player
    Optional<Contract> findByPlayerIdAndStatus(UUID playerId, ContractStatus status);

    // Buscar todos os contratos de um player (histórico)
    List<Contract> findByPlayerIdOrderByStartDateDesc(UUID playerId);

    // Buscar contratos de um clube
    List<Contract> findByClubIdAndStatusOrderByEndDateAsc(UUID clubId, ContractStatus status);

    // Buscar todos os contratos de um clube
    List<Contract> findByClubIdOrderByStartDateDesc(UUID clubId);

    // Buscar contratos que estão expirando em breve
    @Query("SELECT c FROM Contract c WHERE c.status = 'ACTIVE' AND c.endDate BETWEEN :now AND :threshold")
    List<Contract> findContractsExpiringSoon(@Param("now") LocalDate now, @Param("threshold") LocalDate threshold);

    // Buscar contratos expirados que ainda estão como ACTIVE
    @Query("SELECT c FROM Contract c WHERE c.status = 'ACTIVE' AND c.endDate < :now")
    List<Contract> findExpiredContracts(@Param("now") LocalDate now);

    // Verificar se player tem contrato ativo
    boolean existsByPlayerIdAndStatus(UUID playerId, ContractStatus status);

    // Contar contratos ativos de um clube
    Long countByClubIdAndStatus(UUID clubId, ContractStatus status);

    // Buscar contratos por salário (maiores salários)
    @Query("SELECT c FROM Contract c WHERE c.status = 'ACTIVE' ORDER BY c.salary DESC")
    List<Contract> findHighestPaidContracts();

    // Calcular folha salarial de um clube
    @Query("SELECT COALESCE(SUM(c.salary), 0) FROM Contract c WHERE c.club.id = :clubId AND c.status = 'ACTIVE'")
    Long getTotalSalaryByClub(@Param("clubId") UUID clubId);

    // Buscar último contrato de um player (histórico)
    @Query("SELECT c FROM Contract c WHERE c.player.id = :playerId ORDER BY c.startDate DESC LIMIT 1")
    Optional<Contract> findLastContractByPlayerId(@Param("playerId") UUID playerId);

    // Deletar contratos antigos (limpeza)
    void deleteByStatusAndEndDateBefore(ContractStatus status, LocalDate date);
}