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

CREATE TABLE t_players (
    player_id int NOT NULL AUTO_INCREMENT,
    uuid VARCHAR(36) NOT NULL,
    name VARCHAR(16) NOT NULL,
    balance DOUBLE(10,2),
    job_fk INT,
    PRIMARY KEY (player_id),
    FOREIGN KEY (job_fk) REFERENCES t_jobs(job_id)
);

INSERT INTO t_jobs (name) VALUES ('Mineur');
INSERT INTO t_jobs (name) VALUES ('Bucheron');
INSERT INTO t_jobs (name) VALUES ('Paysan');

COMMIT;