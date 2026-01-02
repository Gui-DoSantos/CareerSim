TRUNCATE TABLE seasons;

ALTER TABLE seasons
ADD COLUMN player_id BINARY(16) NOT NULL;

ALTER TABLE seasons
ADD CONSTRAINT fk_season_player
FOREIGN KEY (player_id) REFERENCES players(id);

ALTER TABLE seasons
DROP COLUMN is_active;

ALTER TABLE seasons
ADD COLUMN status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE';

CREATE INDEX idx_player_status ON seasons(player_id, status);