/*tb_users*/
ALTER TABLE `tb_users` MODIFY COLUMN `level` int(11) DEFAULT '0' COMMENT '等级(10=管理员,1=代理商,0=普通用户)';