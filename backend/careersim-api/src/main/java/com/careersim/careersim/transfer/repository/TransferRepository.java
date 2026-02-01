package com.careersim.careersim.transfer.repository;

import com.careersim.careersim.transfer.model.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TransferRepository extends JpaRepository<Transfer, UUID> {

    // Buscar histórico de transferências de um player
    List<Transfer> findByPlayerIdOrderByTransferDateDesc(UUID playerId);

    // Buscar transferências de um clube (que comprou)
    List<Transfer> findByToClubIdOrderByTransferDateDesc(UUID clubId);

    // Buscar transferências de um clube (que vendeu)
    List<Transfer> findByFromClubIdOrderByTransferDateDesc(UUID clubId);

    // Buscar transferências de uma temporada
    List<Transfer> findBySeasonOrderByTransferDateDesc(Integer season);

    // Contar transferências de um player
    Long countByPlayerId(UUID playerId);

    // Buscar última transferência de um player
    @Query("SELECT t FROM Transfer t WHERE t.player.id = :playerId ORDER BY t.transferDate DESC LIMIT 1")
    Transfer findLastTransferByPlayerId(@Param("playerId") UUID playerId);

    // Buscar transferências mais caras
    @Query("SELECT t FROM Transfer t ORDER BY t.transferAmount DESC LIMIT :limit")
    List<Transfer> findMostExpensiveTransfers(@Param("limit") int limit);

    // Calcular valor total gasto por um clube
    @Query("SELECT COALESCE(SUM(t.transferAmount), 0) FROM Transfer t WHERE t.toClub.id = :clubId")
    Long getTotalSpentByClub(@Param("clubId") UUID clubId);

    // Calcular valor total recebido por um clube
    @Query("SELECT COALESCE(SUM(t.transferAmount), 0) FROM Transfer t WHERE t.fromClub.id = :clubId")
    Long getTotalReceivedByClub(@Param("clubId") UUID clubId);
}