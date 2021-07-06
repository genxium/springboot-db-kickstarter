CREATE TABLE `player` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `principal` varchar(128) NOT NULL,
  `salted_password` varchar(255) NOT NULL,
  `display_name` varchar(255) NOT NULL,
  `roles` smallint unsigned DEFAULT 0 COMMENT 'A shortcut to indicate a set of roles currently possessed by this player.',
  `created_at` bigint unsigned NOT NULL,
  `deleted_at` bigint unsigned DEFAULT NULL,
  `updated_at` bigint unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY uk_principal (`principal`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
