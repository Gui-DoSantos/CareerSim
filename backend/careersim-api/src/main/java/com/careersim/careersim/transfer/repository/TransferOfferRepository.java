package com.careersim.careersim.transfer.repository;

import com.careersim.careersim.transfer.model.TransferOffer;
import com.careersim.careersim.transfer.model.TransferOfferStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface TransferOfferRepository extends JpaRepository<TransferOffer, UUID> {

    // Buscar todas as ofertas de um player
    List<TransferOffer> findByPlayerIdOrderByCreatedAtDesc(UUID playerId);

    // Buscar ofertas pendentes de um player
    List<TransferOffer> findByPlayerIdAndStatusOrderByCreatedAtDesc(UUID playerId, TransferOfferStatus status);

    // Buscar ofertas pendentes e não expiradas
    @Query("SELECT o FROM TransferOffer o WHERE o.player.id = :playerId AND o.status = 'PENDING' AND o.expiresAt > :now ORDER BY o.createdAt DESC")
    List<TransferOffer> findActivePendingOffers(@Param("playerId") UUID playerId, @Param("now") LocalDateTime now);

    // Buscar ofertas expiradas que ainda estão como PENDING
    @Query("SELECT o FROM TransferOffer o WHERE o.status = 'PENDING' AND o.expiresAt < :now")
    List<TransferOffer> findExpiredOffers(@Param("now") LocalDateTime now);

    // Contar ofertas pendentes
    Long countByPlayerIdAndStatus(UUID playerId, TransferOfferStatus status);

    // Buscar ofertas de um clube específico
    List<TransferOffer> findByToClubIdOrderByCreatedAtDesc(UUID clubId);

    // Verificar se já existe oferta pendente do mesmo clube
    boolean existsByPlayerIdAndToClubIdAndStatus(UUID playerId, UUID toClubId, TransferOfferStatus status);

    // Deletar ofertas antigas
    void deleteByCreatedAtBefore(LocalDateTime date);
}