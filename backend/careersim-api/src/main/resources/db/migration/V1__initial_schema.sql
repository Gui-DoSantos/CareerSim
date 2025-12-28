CREATE TABLE leagues (
    id BINARY(16) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    country VARCHAR(50) NOT NULL
);

CREATE TABLE clubs (
    id BINARY(16) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    tier VARCHAR(10) NOT NULL,
    league_id BINARY(16),
   CONSTRAINT fk_club_league FOREIGN KEY (league_id) REFERENCES leagues(id)
);

CREATE TABLE players (
    id BINARY(16) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    age INT NOT NULL,
    position VARCHAR(20) NOT NULL,
    overall INT NOT NULL,
    potential INT NOT NULL,
    club_id BINARY(16),
    FOREIGN KEY (club_id) REFERENCES clubs(id)
);

CREATE TABLE seasons (
    id BINARY(16) PRIMARY KEY,
    year INT NOT NULL,
    current_event INT NOT NULL,
    is_active BOOLEAN NOT NULL
);
