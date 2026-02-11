CREATE TABLE matches (
    id BINARY(16) PRIMARY KEY,
    player_id BINARY(16) NOT NULL,
    club_id BINARY(16) NOT NULL,
    opponent_name VARCHAR(100) NOT NULL,
    competition VARCHAR(100) NOT NULL,
    match_date TIMESTAMP NOT NULL,
    is_home BOOLEAN NOT NULL DEFAULT true,
    status VARCHAR(20) NOT NULL DEFAULT 'SCHEDULED',
    player_team_score INTEGER,
    opponent_team_score INTEGER,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_match_player FOREIGN KEY (player_id) REFERENCES players(id) ON DELETE CASCADE,
    CONSTRAINT fk_match_club FOREIGN KEY (club_id) REFERENCES clubs(id) ON DELETE CASCADE,
    CONSTRAINT chk_match_status CHECK (status IN ('SCHEDULED', 'IN_PROGRESS', 'FINISHED', 'CANCELLED'))
);

CREATE INDEX idx_matches_player ON matches(player_id);
CREATE INDEX idx_matches_club ON matches(club_id);
CREATE INDEX idx_matches_date ON matches(match_date);
CREATE INDEX idx_matches_status ON matches(status);

-- Performance individual do player na partida

CREATE TABLE match_performances (
    id BINARY(16) PRIMARY KEY,
    match_id BINARY(16) NOT NULL,
    player_id BINARY(16) NOT NULL,
    minutes_played INTEGER NOT NULL DEFAULT 0,
    goals INTEGER NOT NULL DEFAULT 0,
    assists INTEGER NOT NULL DEFAULT 0,
    shots INTEGER NOT NULL DEFAULT 0,
    shots_on_target INTEGER NOT NULL DEFAULT 0,
    passes_completed INTEGER NOT NULL DEFAULT 0,
    passes_attempted INTEGER NOT NULL DEFAULT 0,
    tackles_won INTEGER NOT NULL DEFAULT 0,
    tackles_attempted INTEGER NOT NULL DEFAULT 0,
    interceptions INTEGER NOT NULL DEFAULT 0,
    fouls_committed INTEGER NOT NULL DEFAULT 0,
    fouls_suffered INTEGER NOT NULL DEFAULT 0,
    yellow_cards INTEGER NOT NULL DEFAULT 0,
    red_cards INTEGER NOT NULL DEFAULT 0,
    rating DECIMAL(3,1),
    man_of_the_match BOOLEAN NOT NULL DEFAULT false,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_performance_match FOREIGN KEY (match_id) REFERENCES matches(id) ON DELETE CASCADE,
    CONSTRAINT fk_performance_player FOREIGN KEY (player_id) REFERENCES players(id) ON DELETE CASCADE,
    CONSTRAINT chk_minutes_valid CHECK (minutes_played >= 0 AND minutes_played <= 120),
    CONSTRAINT chk_rating_valid CHECK (rating >= 0 AND rating <= 10),
    CONSTRAINT uq_match_player UNIQUE (match_id, player_id)
);

CREATE INDEX idx_performance_match ON match_performances(match_id);
CREATE INDEX idx_performance_player ON match_performances(player_id);
CREATE INDEX idx_performance_rating ON match_performances(rating);

-- Eventos que acontecem durante a partida

CREATE TABLE match_events (
    id BINARY(16) PRIMARY KEY,
    match_id BINARY(16) NOT NULL,
    player_id BINARY(16),
    event_type VARCHAR(30) NOT NULL,
    minute INTEGER NOT NULL,
    description TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_match_event_match FOREIGN KEY (match_id) REFERENCES matches(id) ON DELETE CASCADE,
    CONSTRAINT fk_match_event_player FOREIGN KEY (player_id) REFERENCES players(id) ON DELETE SET NULL,
    CONSTRAINT chk_match_event_type CHECK (event_type IN (
        'GOAL', 'ASSIST', 'YELLOW_CARD', 'RED_CARD',
        'SUBSTITUTION_IN', 'SUBSTITUTION_OUT',
        'INJURY', 'PENALTY_SCORED', 'PENALTY_MISSED',
        'OWN_GOAL'
    )),
    CONSTRAINT chk_minute_valid CHECK (minute >= 0 AND minute <= 120)
);

CREATE INDEX idx_match_events_match ON match_events(match_id);
CREATE INDEX idx_match_events_player ON match_events(player_id);
CREATE INDEX idx_match_events_type ON match_events(event_type);
CREATE INDEX idx_match_events_minute ON match_events(minute);