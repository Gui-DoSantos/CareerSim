CREATE TABLE contracts (
    id BINARY(16) PRIMARY KEY,
    player_id BINARY(16) NOT NULL,
    club_id BINARY(16) NOT NULL,
    salary BIGINT NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    signing_bonus BIGINT DEFAULT 0,
    release_clause BIGINT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_contract_player FOREIGN KEY (player_id) REFERENCES players(id) ON DELETE CASCADE,
    CONSTRAINT fk_contract_club FOREIGN KEY (club_id) REFERENCES clubs(id) ON DELETE CASCADE,
    CONSTRAINT chk_contract_status CHECK (status IN ('ACTIVE', 'EXPIRED', 'TERMINATED', 'RENEWED')),
    CONSTRAINT chk_contract_salary CHECK (salary >= 0),
    CONSTRAINT chk_contract_dates CHECK (end_date > start_date),
    CONSTRAINT chk_signing_bonus CHECK (signing_bonus >= 0),
    CONSTRAINT chk_release_clause CHECK (release_clause IS NULL OR release_clause > 0)
);

CREATE INDEX idx_contracts_player ON contracts(player_id);
CREATE INDEX idx_contracts_club ON contracts(club_id);
CREATE INDEX idx_contracts_status ON contracts(status);
CREATE INDEX idx_contracts_end_date ON contracts(end_date);
CREATE INDEX idx_contracts_player_active ON contracts(player_id, status);