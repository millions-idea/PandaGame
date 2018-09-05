CREATE TABLE `tb_accounts` (
  `accounts_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `pay_id` bigint(50) NOT NULL COMMENT '交易记录id',
  `trade_account_id` int(11) NOT NULL COMMENT '交易主体账户id',
  `trade_account_name` varchar(32) NOT NULL COMMENT '交易主体账户名称',
  `accounts_type` int(11) NOT NULL COMMENT '账目类型(1=进账,2=出账)',
  `currency` int(11) NOT NULL DEFAULT '0' COMMENT '货币种类(0=现金,1=虚拟货币)',
  `amount` decimal(12,2) NOT NULL COMMENT '账目总额',
  `before_balance` decimal(12,2) NOT NULL COMMENT '操作前余额',
  `after_balance` decimal(12,2) NOT NULL COMMENT '操作后余额',
  `add_time` datetime NOT NULL COMMENT '发生时间',
  `remark` varchar(255) DEFAULT NULL COMMENT '摘要',
  PRIMARY KEY (`accounts_id`),
  KEY `dex_balance` (`before_balance`,`after_balance`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='财务会计账目表';



CREATE TABLE `tb_dictionary` (
  `dictionary_id` int(11) NOT NULL AUTO_INCREMENT,
  `key` varchar(64) DEFAULT NULL,
  `value` text NOT NULL,
  PRIMARY KEY (`dictionary_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='数据字典表';



CREATE TABLE `tb_exceptions` (
  `log_id` int(11) NOT NULL AUTO_INCREMENT,
  `body` text NOT NULL COMMENT '日志内容',
  `add_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '上报时间',
  PRIMARY KEY (`log_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='异常日志表';



CREATE TABLE `tb_game_member_group` (
  `group_id` int(11) NOT NULL AUTO_INCREMENT,
  `room_code` bigint(20) NOT NULL COMMENT '房间号',
  `user_id` int(11) NOT NULL,
  `is_owner` int(11) NOT NULL DEFAULT '0' COMMENT '是否为拥有者',
  `is_confirm` int(11) NOT NULL DEFAULT '0' COMMENT '是否申请结算',
  `add_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `exit_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`group_id`),
  UNIQUE KEY `uq_room_user` (`room_code`,`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='游戏成员分组表';


CREATE TABLE `tb_game_room` (
  `room_id` int(11) NOT NULL AUTO_INCREMENT,
  `room_code` bigint(20) NOT NULL DEFAULT '0' COMMENT '房间号',
  `owner_id` int(11) NOT NULL COMMENT '拥有者id',
  `parent_area_id` int(11) NOT NULL COMMENT '父分区id',
  `subarea_id` int(11) NOT NULL COMMENT '子分区id',
  `status` int(11) NOT NULL DEFAULT '0' COMMENT '状态(0=未开始,1=已就绪,2=已开始,3=已暂停,4=已结束,5=已结算,6=已失效)',
  `name` varchar(64) NOT NULL COMMENT '房间名称',
  `external_room_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '外部房间号',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `add_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `is_enable` int(11) NOT NULL DEFAULT '1' COMMENT '是否可用(0=否,1=是)',
  `version` int(11) DEFAULT '0' COMMENT '乐观锁',
  PRIMARY KEY (`room_id`),
  UNIQUE KEY `uq_external_room_id` (`external_room_id`,`subarea_id`,`parent_area_id`,`owner_id`) USING BTREE,
  UNIQUE KEY `uq_room_code` (`room_code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='游戏房间表';


CREATE TABLE `tb_messages` (
  `message_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `message` varchar(255) NOT NULL COMMENT '消息内容',
  `state` int(11) NOT NULL DEFAULT '0' COMMENT '状态(0=待查收,1=已查收)',
  `add_time` datetime DEFAULT NULL,
  PRIMARY KEY (`message_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='推送短消息';



CREATE TABLE `tb_pays` (
  `pay_id` bigint(20) NOT NULL,
  `from_uid` int(11) NOT NULL COMMENT '交易主体账户id',
  `from_name` varchar(32) NOT NULL COMMENT '交易主体账户名称',
  `to_uid` int(11) NOT NULL COMMENT '交易对方账户id',
  `to_name` varchar(32) NOT NULL COMMENT '交易对方账户名称',
  `channel_type` int(11) NOT NULL COMMENT '渠道类型',
  `channel_name` varchar(32) NOT NULL COMMENT '渠道名称',
  `product_type` int(11) NOT NULL COMMENT '商品类别',
  `product_name` varchar(32) NOT NULL COMMENT '商品名称',
  `trade_type` int(11) NOT NULL COMMENT '交易类型',
  `trade_name` varchar(32) NOT NULL COMMENT '交易名称',
  `add_time` datetime NOT NULL COMMENT '发生时间',
  `amount` decimal(12,2) NOT NULL COMMENT '交易总额',
  `system_record_id` bigint(20) NOT NULL COMMENT '系统交易流水单号',
  `remark` varchar(255) DEFAULT NULL COMMENT '摘要',
  `channel_record_id` varchar(40) DEFAULT NULL COMMENT '渠道交易流水单号',
  `status` int(11) NOT NULL DEFAULT '0' COMMENT '状态(0=正常,1=退款)',
  `to_account_time` datetime DEFAULT NULL COMMENT '渠道交易到账响应时间',
  PRIMARY KEY (`pay_id`),
  UNIQUE KEY `uq_systemRecordId` (`system_record_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='财务交易记录表';



CREATE TABLE `tb_permission` (
  `permission_id` int(11) NOT NULL AUTO_INCREMENT,
  `permission_name` varchar(32) NOT NULL,
  `target_url` varchar(255) NOT NULL,
  PRIMARY KEY (`permission_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='权限表';




CREATE TABLE `tb_permission_relation` (
  `permission_relation_id` int(11) NOT NULL AUTO_INCREMENT,
  `permission_id` int(11) NOT NULL COMMENT '权限id',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `permission_role` varchar(32) NOT NULL DEFAULT 'ROLE_ADMIN' COMMENT '权限角色名',
  PRIMARY KEY (`permission_relation_id`),
  UNIQUE KEY `uq_index` (`permission_id`,`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='权限关系表';




CREATE TABLE `tb_recharge` (
  `recharge_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `amount` decimal(10,2) NOT NULL COMMENT '金额',
  `state` int(11) NOT NULL DEFAULT '0' COMMENT '状态(0=待审批,1=已到账,2=已拒绝)',
  `remark` varchar(255) DEFAULT NULL COMMENT '摘要',
  `system_record_id` bigint(20) NOT NULL,
  `channel_record_id` varchar(40) DEFAULT NULL,
  `add_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`recharge_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='人工充值审批表';




CREATE TABLE `tb_room_card` (
  `card_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `panda_id` varchar(64) NOT NULL COMMENT '熊猫id',
  `state` int(11) NOT NULL DEFAULT '0' COMMENT '状态(0=待充值,1=成功, 2=失败)',
  `add_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`card_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='房卡领取申请表';


CREATE TABLE `tb_settlement` (
  `settlement_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `room_code` bigint(20) NOT NULL COMMENT '房间号',
  `grade` double NOT NULL DEFAULT '0' COMMENT '成绩',
  `state` int(11) NOT NULL COMMENT '状态(0=待结算,1=已结算,2=已拒绝)',
  `add_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`settlement_id`),
  UNIQUE KEY `uq_room` (`user_id`,`room_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='结算申请表';


CREATE TABLE `tb_subarea_relations` (
  `relation_id` int(11) NOT NULL AUTO_INCREMENT,
  `subarea_id` int(11) NOT NULL,
  `join_subarea_id` int(11) NOT NULL,
  PRIMARY KEY (`relation_id`,`subarea_id`,`join_subarea_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='游戏分区关系表';


CREATE TABLE `tb_subareas` (
  `subarea_id` int(11) NOT NULL AUTO_INCREMENT,
  `parent_id` int(11) NOT NULL DEFAULT '0' COMMENT '父分区id',
  `is_leaf` int(11) NOT NULL DEFAULT '0' COMMENT '是否为末级分区(0=否,1=是)',
  `is_relation` int(11) NOT NULL DEFAULT '0' COMMENT '是否与公用分区建立关系(0=否,1=是)',
  `name` varchar(64) NOT NULL COMMENT '分区名称',
  `background_image` varchar(255) DEFAULT NULL COMMENT '背景地址',
  `price` decimal(10,2) DEFAULT '0.00' COMMENT '单价',
  `add_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `max_person_count` int(11) NOT NULL DEFAULT '0',
  `limit_price` decimal(10,2) NOT NULL DEFAULT '5.00' COMMENT '限制最低金币门槛',
  `reduce_price` decimal(10,2) DEFAULT '0.00' COMMENT '实际扣减',
  `is_enable` int(11) DEFAULT '1' COMMENT '是否可用',
  PRIMARY KEY (`subarea_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='游戏分区表';


CREATE TABLE `tb_users` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT,
  `phone` varchar(11) NOT NULL COMMENT '手机号',
  `password` varchar(32) NOT NULL COMMENT '用户密码',
  `add_date` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_date` datetime DEFAULT NULL,
  `ip` varchar(20) DEFAULT '0.0.0.0' COMMENT 'IP地址',
  `finance_id` varchar(64) DEFAULT NULL COMMENT '财务账号(支付宝账号或银行账号)',
  `finance_name` varchar(32) DEFAULT NULL COMMENT '支付宝账户名',
  `panda_id` varchar(64) NOT NULL COMMENT '熊猫id',
  `is_enable` int(11) DEFAULT '1',
  `is_delete` int(11) DEFAULT '0',
  `level` int(11) DEFAULT '0',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `uq_panda` (`panda_id`),
  UNIQUE KEY `uq_phone` (`phone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户表';


CREATE TABLE `tb_wallets` (
  `wallet_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `balance` decimal(12,2) NOT NULL DEFAULT '0.00' COMMENT '余额',
  `update_time` datetime NOT NULL COMMENT '最后一次更新时间',
  `version` int(11) DEFAULT '0' COMMENT '乐观锁',
  PRIMARY KEY (`wallet_id`),
  UNIQUE KEY `uq_walletId_userId` (`wallet_id`,`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户钱包表';

CREATE TABLE `tb_withdraw` (
  `withdraw_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `amount` decimal(10,2) NOT NULL COMMENT '提现金额',
  `state` int(11) NOT NULL DEFAULT '0' COMMENT '状态(0=待审核,1=已通过,2=拒绝)',
  `remark` varchar(255) DEFAULT NULL,
  `system_record_id` bigint(20) NOT NULL,
  `channel_record_id` varchar(40) DEFAULT NULL COMMENT '渠道交易流水单号',
  `add_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`withdraw_id`),
  UNIQUE KEY `uq_user` (`withdraw_id`,`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='提现申请表';




