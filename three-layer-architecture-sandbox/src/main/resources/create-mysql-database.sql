DROP TABLE IF EXISTS `car`;
DROP TABLE IF EXISTS `driver`;
DROP TABLE IF EXISTS `color`;

CREATE TABLE `color` (
  `id` bigint(20) NOT NULL,
  `hexa_code` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
CREATE TABLE `driver` (
  `id` bigint(20) NOT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
CREATE TABLE `car` (
  `id` bigint(20) NOT NULL,
  `color_id` bigint(20) NOT NULL,
  `driver_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKg0jvjcwntclcjd9cd2vmta820` (`color_id`),
  KEY `FKt075681k23ii3uvdxjvvmmpm` (`driver_id`),
  CONSTRAINT `FKg0jvjcwntclcjd9cd2vmta820` FOREIGN KEY (`color_id`) REFERENCES `color` (`id`),
  CONSTRAINT `FKt075681k23ii3uvdxjvvmmpm` FOREIGN KEY (`driver_id`) REFERENCES `driver` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;