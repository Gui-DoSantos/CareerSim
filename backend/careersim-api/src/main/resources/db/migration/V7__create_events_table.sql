CREATE TABLE events (
    id BINARY(16) PRIMARY KEY,
    player_id BINARY(16) NOT NULL,
    event_type VARCHAR(50) NOT NULL,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    reference_id BINARY(16),
    reference_type VARCHAR(50),
    is_read BOOLEAN NOT NULL DEFAULT false,
    occurred_at TIMESTAMP NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_event_player FOREIGN KEY (player_id) REFERENCES players(id) ON DELETE CASCADE,
    CONSTRAINT chk_event_type CHECK (event_type IN (
        'MATCH_SCHEDULED',
        'MATCH_RESULT',
        'LEVEL_UP',
        'SKILL_UNLOCKED',
        'TRANSFER_OFFER',
        'TRANSFER_COMPLETED',
        'CONTRACT_SIGNED',
        'INJURY',
        'AWARD',
        'MILESTONE'
    ))
);

CREATE INDEX idx_events_player ON events(player_id);
CREATE INDEX idx_events_type ON events(event_type);
CREATE INDEX idx_events_occurred_at ON events(occurred_at);
CREATE INDEX idx_events_is_read ON events(is_read);
CREATE INDEX idx_events_player_unread ON events(player_id, is_read);