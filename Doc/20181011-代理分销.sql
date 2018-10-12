/*tb_users*/
ALTER TABLE `tb_users` MODIFY COLUMN `parent_id` int(11) DEFAULT NULL COMMENT '上级用户id';
