ALTER TABLE players
ADD COLUMN level INTEGER NOT NULL DEFAULT 1,
ADD COLUMN experience INTEGER NOT NULL DEFAULT 0,
ADD COLUMN training_points INTEGER NOT NULL DEFAULT 0;

CREATE TABLE skill_nodes (
    id VARCHAR(50) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    category VARCHAR(20) NOT NULL,
    tier INTEGER NOT NULL,
    cost INTEGER NOT NULL,
    path VARCHAR(20),
    icon VARCHAR(50),
    position_x INTEGER,
    position_y INTEGER


CREATE TABLE skill_node_effects (
    id BINARY(16) PRIMARY KEY,
    skill_node_id VARCHAR(50) NOT NULL,
    attribute_name VARCHAR(30) NOT NULL,
    increase_amount INTEGER NOT NULL,
    FOREIGN KEY (skill_node_id) REFERENCES skill_nodes(id) ON DELETE CASCADE
);


CREATE TABLE skill_node_prerequisites (
    skill_node_id VARCHAR(50) NOT NULL,
    required_node_id VARCHAR(50) NOT NULL,
    PRIMARY KEY (skill_node_id, required_node_id),
    FOREIGN KEY (skill_node_id) REFERENCES skill_nodes(id) ON DELETE CASCADE,
    FOREIGN KEY (required_node_id) REFERENCES skill_nodes(id) ON DELETE CASCADE
);


CREATE TABLE player_skill_nodes (
    player_id BINARY(16) NOT NULL,
    skill_node_id VARCHAR(50) NOT NULL,
    unlocked_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (player_id, skill_node_id),
    FOREIGN KEY (player_id) REFERENCES players(id) ON DELETE CASCADE,
    FOREIGN KEY (skill_node_id) REFERENCES skill_nodes(id) ON DELETE CASCADE
);


CREATE INDEX idx_skill_nodes_category ON skill_nodes(category);
CREATE INDEX idx_skill_node_effects_node ON skill_node_effects(skill_node_id);
CREATE INDEX idx_player_skill_nodes_player ON player_skill_nodes(player_id);