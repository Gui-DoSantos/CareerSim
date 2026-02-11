CREATE TABLE transfers (
    id BINARY(16) PRIMARY KEY,
    player_id BINARY(16) NOT NULL,
    from_club_id BINARY(16),
    to_club_id BINARY(16) NOT NULL,
    transfer_amount BIGINT NOT NULL,
    salary BIGINT NOT NULL,
    contract_years INTEGER NOT NULL,
    transfer_date TIMESTAMP NOT NULL,
    season INTEGER NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_transfer_player FOREIGN KEY (player_id) REFERENCES players(id) ON DELETE CASCADE,
    CONSTRAINT fk_transfer_from_club FOREIGN KEY (from_club_id) REFERENCES clubs(id) ON DELETE SET NULL,
    CONSTRAINT fk_transfer_to_club FOREIGN KEY (to_club_id) REFERENCES clubs(id) ON DELETE CASCADE,
    CONSTRAINT chk_transfer_amount CHECK (transfer_amount >= 0),
    CONSTRAINT chk_transfer_salary CHECK (salary >= 0),
    CONSTRAINT chk_transfer_years CHECK (contract_years BETWEEN 1 AND 5)
);

CREATE INDEX idx_transfers_player ON transfers(player_id);
CREATE INDEX idx_transfers_from_club ON transfers(from_club_id);
CREATE INDEX idx_transfers_to_club ON transfers(to_club_id);
CREATE INDEX idx_transfers_season ON transfers(season);
CREATE INDEX idx_transfers_date ON transfers(transfer_date);