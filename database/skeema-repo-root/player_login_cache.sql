CREATE TABLE `player_login_cache` (
  `series` varchar(128) NOT NULL PRIMARY KEY,
  `player_principal` varchar(128) NOT NULL,
  `int_auth_token` varchar(64) NOT NULL COMMENT 'Internal authentication token.',
  `from_public_ip` varchar(128) DEFAULT NULL,
  `meta_data` text DEFAULT NULL,
  `created_at` bigint unsigned NOT NULL,
  `updated_at` bigint unsigned NOT NULL,
  `deleted_at` bigint unsigned DEFAULT NULL,
  INDEX (`player_principal`),
  INDEX (`int_auth_token`),
  UNIQUE KEY uk_principal (`player_principal`, `int_auth_token`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
