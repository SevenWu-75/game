CREATE TABLE `user` (
                        `id` bigint NOT NULL AUTO_INCREMENT,
                        `username` varchar(10) NOT NULL,
                        `password` varchar(255) NOT NULL,
                        `realname` varchar(255) NOT NULL,
                        PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `game` (
                        `id` int(11) NOT NULL AUTO_INCREMENT,
                        `game_name` varchar(10) NOT NULL,
                        `chinese_name` varchar(10) NOT NULL,
                        `version` varchar(10) NOT NULL,
                        PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `history_rank` (
                                `id` bigint NOT NULL AUTO_INCREMENT,
                                `game_id` int NOT NULL,
                                `user_id` bigint NOT NULL,
                                `play_count` bigint NOT NULL,
                                `win_count` int NOT NULL,
                                `best_score` int NOT NULL,
                                `last_play_time` datetime NOT NULL,
                                PRIMARY KEY (`id`),
                                UNIQUE KEY `game_user_unique` (`game_id`,`user_id`) USING BTREE,
                                KEY `user_id` (`user_id`),
                                CONSTRAINT `game_id` FOREIGN KEY (`game_id`) REFERENCES `game` (`id`),
                                CONSTRAINT `user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `user` (`username`, `password`, `realname`) VALUES ('bei', '123456', '贝');
INSERT INTO `user` (`username`, `password`, `realname`) VALUES ('ming', '123456', '明');
INSERT INTO `user` (`username`, `password`, `realname`) VALUES ('kang', '123456', '康');
INSERT INTO `user` (`username`, `password`, `realname`) VALUES ('tian', '123456', '天');
INSERT INTO `user` (`username`, `password`, `realname`) VALUES ('zhou', '123456', '舟');
INSERT INTO `user` (`username`, `password`, `realname`) VALUES ('liang', '123456', '亮');

INSERT INTO `game` (`game_name`, `chinese_name`, `version`) VALUES ('speed-dice', '快艇骰子', '1.0.0');