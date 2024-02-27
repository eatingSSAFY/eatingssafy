SET NAMES utf8mb4;

-- eatingssafy.allergy_code definition

CREATE TABLE IF NOT EXISTS `allergy_code`
(
    `id`         int          NOT NULL,
    `ingredient` varchar(100) NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;


-- eatingssafy.amount definition

CREATE TABLE IF NOT EXISTS `amount`
(
    `id`            int          NOT NULL AUTO_INCREMENT,
    `camera_id`     int          NOT NULL,
    `category_code` varchar(100) NOT NULL,
    `cnt`           int          NOT NULL,
    `created_at`    datetime(6)  NOT NULL,
    `serving_at`    date         NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;


-- eatingssafy.food definition

CREATE TABLE IF NOT EXISTS `food`
(
    `id`      int NOT NULL AUTO_INCREMENT,
    `content` varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;


-- eatingssafy.menu definition

CREATE TABLE IF NOT EXISTS `menu`
(
    `id`            int          NOT NULL AUTO_INCREMENT,
    `category_code` varchar(255) NOT NULL,
    `serving_at`    date         NOT NULL,
    `updated_at`    datetime(6)  NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UniqueServingAtAndCategoryCode` (`serving_at`, `category_code`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;


-- eatingssafy.nocard_person definition

CREATE TABLE IF NOT EXISTS `nocard_person`
(
    `id`           int         NOT NULL AUTO_INCREMENT,
    `created_at`   datetime(6) NOT NULL,
    `exported_at`  datetime(6) DEFAULT NULL,
    `exported_cnt` int         NOT NULL,
    `person_id`    varchar(50) NOT NULL,
    `person_name`  varchar(20) NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;


-- eatingssafy.stock definition

CREATE TABLE IF NOT EXISTS `stock`
(
    `id`            int          NOT NULL AUTO_INCREMENT,
    `category_code` varchar(100) NOT NULL,
    `cnt`           int          NOT NULL,
    `serving_at`    date         NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;


-- eatingssafy.tag definition

CREATE TABLE IF NOT EXISTS `tag`
(
    `id`         int         NOT NULL AUTO_INCREMENT,
    `created_at` datetime(6) NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;


-- eatingssafy.food_allergy_code definition

CREATE TABLE IF NOT EXISTS `food_allergy_code`
(
    `id`              int NOT NULL AUTO_INCREMENT,
    `allergy_code_id` int DEFAULT NULL,
    `food_id`         int DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `FKrgoq74pc5td8m6x0usn8920rk` (`allergy_code_id`),
    KEY `FK6w2ssukxccp816oufso1chjf0` (`food_id`),
    CONSTRAINT `FK6w2ssukxccp816oufso1chjf0` FOREIGN KEY (`food_id`) REFERENCES `food` (`id`),
    CONSTRAINT `FKrgoq74pc5td8m6x0usn8920rk` FOREIGN KEY (`allergy_code_id`) REFERENCES `allergy_code` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;


-- eatingssafy.menu_food definition

CREATE TABLE IF NOT EXISTS `menu_food`
(
    `id`      int NOT NULL AUTO_INCREMENT,
    `food_id` int DEFAULT NULL,
    `menu_id` int DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `FKk1tnspt51jpbhx4gw06hwmlc2` (`food_id`),
    KEY `FKi8bx1sbjkgbheqjx895ei3d6e` (`menu_id`),
    CONSTRAINT `FKi8bx1sbjkgbheqjx895ei3d6e` FOREIGN KEY (`menu_id`) REFERENCES `menu` (`id`),
    CONSTRAINT `FKk1tnspt51jpbhx4gw06hwmlc2` FOREIGN KEY (`food_id`) REFERENCES `food` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;


-- eatingssafy.lunch_time definition

CREATE TABLE IF NOT EXISTS `lunch_time`
(
    `id`           int NOT NULL AUTO_INCREMENT,
    `floor`        int  DEFAULT NULL,
    `lunch_time`   time DEFAULT NULL,
    `period_end`   date DEFAULT NULL,
    `period_start` date DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 228
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;


-- eatingssafy.`user` definition

CREATE TABLE IF NOT EXISTS `user`
(
    `kakao_id`    bigint      NOT NULL,
    `created_at`  datetime(6) NOT NULL,
    `person_nickname` varchar(255) NOT NULL,
    PRIMARY KEY (`kakao_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;


-- eatingssafy.daily_visit_history definition

CREATE TABLE IF NOT EXISTS `daily_visit_history` (
    `user_id` bigint NOT NULL,
    `cnt` int DEFAULT NULL,
    `visited_at` date DEFAULT NULL,
    PRIMARY KEY (`user_id`)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_0900_ai_ci;


-- eatingssafy.preference definition

CREATE TABLE IF NOT EXISTS `preference` (
    `food_id` int NOT NULL,
    `user_id` bigint NOT NULL,
    `value` bit(1) DEFAULT NULL,
    PRIMARY KEY (`food_id`,`user_id`),
    KEY `FKsv3xgeggccs516fm0ng21o1t0` (`user_id`),
    CONSTRAINT `FKgch3mpu649eft69ij05s206d0` FOREIGN KEY (`food_id`) REFERENCES `food` (`id`),
    CONSTRAINT `FKsv3xgeggccs516fm0ng21o1t0` FOREIGN KEY (`user_id`) REFERENCES `user` (`kakao_id`)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_0900_ai_ci;


-- eatingssafy.notification definition

CREATE TABLE IF NOT EXISTS `notification`
(
    `app_token`  varbinary(255) NOT NULL,
    `amount`     bit(1)      DEFAULT NULL,
    `preference` bit(1)      DEFAULT NULL,
    `updated_at` datetime(6) DEFAULT NULL,
    `user_id`    bigint      DEFAULT NULL,
    PRIMARY KEY (`app_token`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;


-- eatingssafy.amount_notification definition

CREATE TABLE IF NOT EXISTS `amount_notification`
(
    `category_code` varbinary(255) NOT NULL,
    `serving_at` date NOT NULL,
    PRIMARY KEY (`category_code`,`serving_at`)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_0900_ai_ci;


-- allergy_info insert Initialize data
INSERT INTO `allergy_code`
SELECT *
FROM (VALUES ROW (1, '우유'), ROW
                 (2, '메밀'), ROW
                 (3, '땅콩'), ROW
                 (4, '잣'), ROW
                 (5, '대두'), ROW
                 (6, '밀'), ROW
                 (7, '호두'), ROW
                 (8, '복숭아'), ROW
                 (9, '토마토'), ROW
                 (10, '고등어'), ROW
                 (11, '게'), ROW
                 (12, '새우'), ROW
                 (13, '오징어'), ROW
                 (14, '돼지고기'), ROW
                 (15, '닭고기'), ROW
                 (16, '쇠고기'), ROW
                 (17, '난류(가금류 포함)'), ROW
                 (18, '패주류(굴, 전복, 홍합)'), ROW
                 (19, '아황산류')) AS TMP_TABLE (ID, INGREDIENT)
WHERE NOT EXISTS (SELECT 1
                  FROM `allergy_code`);
