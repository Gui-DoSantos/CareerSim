CREATE TABLE transfer_offers (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    player_id UUID NOT NULL,
    from_club_id UUID,
    to_club_id UUID NOT NULL,
    offer_amount BIGINT NOT NULL,
    salary_offered BIGINT NOT NULL,
    contract_years INTEGER NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    expires_at TIMESTAMP NOT NULL,
    responded_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_offer_player FOREIGN KEY (player_id) REFERENCES players(id) ON DELETE CASCADE,
    CONSTRAINT fk_offer_from_club FOREIGN KEY (from_club_id) REFERENCES clubs(id) ON DELETE SET NULL,
    CONSTRAINT fk_offer_to_club FOREIGN KEY (to_club_id) REFERENCES clubs(id) ON DELETE CASCADE,
    CONSTRAINT chk_offer_status CHECK (status IN ('PENDING', 'ACCEPTED', 'REJECTED', 'EXPIRED')),
    CONSTRAINT chk_offer_amount CHECK (offer_amount >= 0),
    CONSTRAINT chk_salary_offered CHECK (salary_offered >= 0),
    CONSTRAINT chk_contract_years CHECK (contract_years BETWEEN 1 AND 5)
);
