SET autocommit = 0;
START TRANSACTION;

DROP DATABASE IF EXISTS db_valkyar_plugin;
CREATE DATABASE IF NOT EXISTS db_valkyar_plugin CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE db_valkyar_plugin;


CREATE TABLE t_jobs (
    job_id int NOT NULL AUTO_INCREMENT,
    name VARCHAR(21) NOT NULL,
    PRIMARY KEY (job_id)
);


CREATE TABLE t_town (
    town_id int NOT NULL AUTO_INCREMENT,
    name VARCHAR(51) NOT NULL UNIQUE,
    balance DOUBLE(10,2) DEFAULT 0.0,
    PRIMARY KEY (town_id)
);

CREATE TABLE t_players (
    player_id int NOT NULL AUTO_INCREMENT,
    uuid VARCHAR(36) NOT NULL,
    name VARCHAR(16) NOT NULL,
    balance DOUBLE(10,2) DEFAULT 0.0,
    job_fk INT,
    town_fk INT NULL,
    PRIMARY KEY (player_id),
    FOREIGN KEY (job_fk) REFERENCES t_jobs(job_id),
    FOREIGN KEY (town_fk) REFERENCES t_town(town_id) ON DELETE SET NULL
);

CREATE TABLE t_breakable_blocks (
    block_id int NOT NULL AUTO_INCREMENT,
    minecraft_identifier VARCHAR(51) NOT NULL,
    money_bringing DOUBLE(5,2),
    job_fk INT,
    PRIMARY KEY (block_id),
    FOREIGN KEY (job_fk) REFERENCES t_jobs(job_id)
);

CREATE TABLE t_chunk_town (
    chunk_id int NOT NULL AUTO_INCREMENT,
    chunk_identifier_x INT NOT NULL,
    chunk_identifier_z INT NOT NULL,
    town_fk INT,
    PRIMARY KEY (chunk_id),
    FOREIGN KEY (town_fk) REFERENCES t_town(town_id) ON DELETE SET NULL
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

INSERT INTO t_jobs (name) VALUES ('Mineur');
INSERT INTO t_jobs (name) VALUES ('Bucheron');
INSERT INTO t_jobs (name) VALUES ('Paysan');

INSERT INTO t_breakable_blocks  (minecraft_identifier,       money_bringing,     job_fk) 
                        VALUES  ('stone',                   1.00,                1),
                                ('oak_log',                 1.00,                2),
                                ('wheat[age=7]',            1.00,                3);

COMMIT;