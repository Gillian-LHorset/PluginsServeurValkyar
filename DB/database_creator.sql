SET autocommit = 0;
START TRANSACTION;

DROP DATABASE IF EXISTS db_valkyar_plugin;
CREATE DATABASE IF NOT EXISTS db_valkyar_plugin CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE db_valkyar_plugin;


CREATE TABLE t_jobs (
    job_id int NOT NULL AUTO_INCREMENT,
    name VARCHAR(21) NOT NULL,
    ismain BOOLEAN NOT NULL,
    PRIMARY KEY (job_id)
);


CREATE TABLE t_town (
    town_id int NOT NULL AUTO_INCREMENT,
    name VARCHAR(51) NOT NULL UNIQUE,
    balance DOUBLE(10,2) DEFAULT 0.0,
    spawn_x INT DEFAULT NULL,
    spawn_y INT DEFAULT NULL,
    spawn_z INT DEFAULT NULL,
    PRIMARY KEY (town_id)
);

CREATE TABLE t_players (
    player_id int NOT NULL AUTO_INCREMENT,
    uuid VARCHAR(36) NOT NULL,
    name VARCHAR(16) NOT NULL,
    balance DOUBLE(10,2) DEFAULT 0.0,
    main_job_fk INT,
    second_job_fk INT,
    town_fk INT NULL,
    PRIMARY KEY (player_id),
    FOREIGN KEY (main_job_fk) REFERENCES t_jobs(job_id),
    FOREIGN KEY (second_job_fk) REFERENCES t_jobs(job_id),
    FOREIGN KEY (second_job_fk) REFERENCES t_jobs(job_id),
    FOREIGN KEY (town_fk) REFERENCES t_town(town_id) ON DELETE SET NULL
);

CREATE TABLE t_player_jobs (
    player_fk INT NOT NULL,
    job_fk INT NOT NULL,
    current_exp DOUBLE(10,2) NOT NULL DEFAULT 0,
    last_leave DATETIME DEFAULT NULL,
    level INT NOT NULL DEFAULT 0,
    PRIMARY KEY (player_fk, job_fk),
    FOREIGN KEY (player_fk) REFERENCES t_players(player_id) ON DELETE CASCADE,
    FOREIGN KEY (job_fk) REFERENCES t_jobs(job_id) ON DELETE CASCADE
);

CREATE TABLE t_action_type (
    block_id int NOT NULL AUTO_INCREMENT,
    minecraft_identifier VARCHAR(51) NOT NULL,
    action VARCHAR(51) NOT NULL,
    money_bringing DOUBLE(5,2),
    exp_bringing DOUBLE(5,2),
    job_fk INT,
    PRIMARY KEY (block_id),
    FOREIGN KEY (job_fk) REFERENCES t_jobs(job_id)
);


CREATE TABLE t_avant_garde (
    avant_garde_id INT NOT NULL AUTO_INCREMENT,
    avant_garde_name VARCHAR(31) NOT NULL,
    PRIMARY KEY (avant_garde_id)
);

CREATE TABLE t_chunk_town (
    chunk_id int NOT NULL AUTO_INCREMENT,
    chunk_identifier_x INT NOT NULL,
    chunk_identifier_z INT NOT NULL,
    is_avant_garde BOOLEAN DEFAULT FALSE,
    avant_garde_fk INT DEFAULT NULL,
    town_fk INT,
    PRIMARY KEY (chunk_id),
    FOREIGN KEY (town_fk) REFERENCES t_town(town_id) ON DELETE SET NULL,
    FOREIGN KEY (avant_garde_fk) REFERENCES t_avant_garde(avant_garde_id) ON DELETE SET NULL
);

CREATE TABLE t_permission_list (
    permission_id int NOT NULL AUTO_INCREMENT,
    mayor       BOOLEAN DEFAULT FALSE,
    second      BOOLEAN DEFAULT FALSE,
    claim       BOOLEAN DEFAULT FALSE,
    withdraw    BOOLEAN DEFAULT FALSE,
    unclaim     BOOLEAN DEFAULT FALSE,
    invite      BOOLEAN DEFAULT FALSE,
    kick        BOOLEAN DEFAULT FALSE,
    player_fk INT UNIQUE,
    PRIMARY KEY (permission_id),
    FOREIGN KEY (player_fk) REFERENCES t_players(player_ID)
);

INSERT INTO t_jobs (name, ismain) VALUES ('Mineur', TRUE);
INSERT INTO t_jobs (name, ismain) VALUES ('Bucheron', TRUE);
INSERT INTO t_jobs (name, ismain) VALUES ('Fermier', TRUE);
INSERT INTO t_jobs (name, ismain) VALUES ('Pecheur', FALSE);
INSERT INTO t_jobs (name, ismain) VALUES ('Cuisinier', FALSE);
INSERT INTO t_jobs (name, ismain) VALUES ('Constructeur', FALSE);

INSERT INTO t_action_type   (minecraft_identifier, action,  money_bringing, exp_bringing, job_fk) 
                    VALUES  ('stone',              "break", 1.00,           1.00,         1),
                            ('oak_log',            "break", 1.00,           1.00,         2),
                            ('wheat',              "break", 1.00,           1.00,         3),
                            ('salmon',             "fish",  1.00,           1.00,         4),
                            ('cooked_beef',        "cook",  1.00,           1.00,         5),
                            ('stone',              "place", 1.00,           1.00,         6),
                            ('wheat',              "place", 1.00,           1.00,         3);

COMMIT;