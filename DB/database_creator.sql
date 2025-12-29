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
    major_player_fk INT NOT NULL,
    PRIMARY KEY (town_id)
);

CREATE TABLE t_players (
    player_id int NOT NULL AUTO_INCREMENT,
    uuid VARCHAR(36) NOT NULL,
    name VARCHAR(16) NOT NULL,
    balance DOUBLE(10,2) DEFAULT 0.0,
    job_fk INT,
    town_fk INT,
    PRIMARY KEY (player_id),
    FOREIGN KEY (job_fk) REFERENCES t_jobs(job_id),
    FOREIGN KEY (town_fk) REFERENCES t_town(town_id)
);

CREATE TABLE t_breakable_blocks (
    block_id int NOT NULL AUTO_INCREMENT,
    minecraft_identifid VARCHAR(51) NOT NULL,
    money_bringing DOUBLE(5,2),
    job_fk INT,
    PRIMARY KEY (block_id),
    FOREIGN KEY (job_fk) REFERENCES t_jobs(job_id)
);

ALTER TABLE t_town
ADD FOREIGN KEY (major_player_fk) REFERENCES t_players(player_id);

INSERT INTO t_jobs (name) VALUES ('Mineur');
INSERT INTO t_jobs (name) VALUES ('Bucheron');
INSERT INTO t_jobs (name) VALUES ('Paysan');

INSERT INTO t_breakable_blocks  (minecraft_identifid,       money_bringing,     job_fk) 
                        VALUES  ('stone',                   1.00,                1),
                                ('oak_log',                 1.00,                2),
                                ('wheat[age=7]',            1.00,                3);

COMMIT;