CREATE TABLE `player_suborg_binding` (
  `player_id` bigint unsigned NOT NULL,
  `suborg_id` bigint unsigned NOT NULL,
  `org_id` bigint unsigned NOT NULL COMMENT 'This is redundant for indexing only.',
  `created_at` bigint unsigned NOT NULL,
  `deleted_at` bigint unsigned DEFAULT NULL,
  `updated_at` bigint unsigned DEFAULT NULL,
  PRIMARY KEY (`player_id`, `suborg_id`),
  INDEX idx_org_id (`org_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
