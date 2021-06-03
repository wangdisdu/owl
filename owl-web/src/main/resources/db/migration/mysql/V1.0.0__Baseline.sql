CREATE TABLE `tb_user` (
  `account` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `role` int(11) NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `session_time` bigint(20) DEFAULT NULL,
  `session_token` varchar(255) DEFAULT NULL,
  `session_expire` bigint(20) DEFAULT NULL,
  `create_time` bigint(20) DEFAULT NULL,
  `create_by` varchar(255) DEFAULT NULL,
  `update_time` bigint(20) DEFAULT NULL,
  `update_by` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`account`)
) ENGINE=InnoDB;

CREATE TABLE `tb_integration` (
  `name` varchar(255) NOT NULL,
  `description` text DEFAULT NULL,
  `config` text DEFAULT NULL,
  `builder` varchar(255) NOT NULL,
  `meta` text DEFAULT NULL,
  `create_time` bigint(20) DEFAULT NULL,
  `create_by` varchar(255) DEFAULT NULL,
  `update_time` bigint(20) DEFAULT NULL,
  `update_by` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`name`)
) ENGINE=InnoDB;

INSERT INTO tb_user (account, password, name, role) VALUES ('admin', '5a8721c145115a3ea48552c43cf8dc64', 'admin', 0);