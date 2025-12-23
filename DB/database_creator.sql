SET autocommit = 0;
START TRANSACTION;
DROP DATABASE IF EXISTS db_valkyar_plugin;
CREATE DATABASE IF NOT EXISTS db_valkyar_plugin CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE db_valkyar_plugin;
CREATE TABLE t_players (
    player_id int NOT NULL AUTO_INCREMENT,
    uuid VARCHAR(36) NOT NULL,
    name VARCHAR(16) NOT NULL,
    balance DOUBLE(10,2),
    PRIMARY KEY (player_id)
);

COMMIT;