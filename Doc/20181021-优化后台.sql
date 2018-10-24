/*tb_users*/
ALTER TABLE `tb_users` MODIFY COLUMN `level` int(11) DEFAULT '0' COMMENT '等级(10=管理员,1=代理商,0=普通用户)';

/*tb_merchant_messages*/
CREATE TABLE `tb_merchant_messages` (
  `business_id` int(11) NOT NULL AUTO_INCREMENT,
  `parent_user_id` int(11) NOT NULL COMMENT '师傅id',
  `user_id` int(11) NOT NULL COMMENT '徒弟id',
  `amount` decimal(10,2) NOT NULL COMMENT '金额',
  `type` int(11) NOT NULL,
  `remark` varchar(255) NOT NULL COMMENT '摘要',
  `add_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`business_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='商家生意消息通知表';



INSERT INTO `tb_dictionary` (`key`, `value`) VALUES ('app.marquee','\"麻\"省四川最火爆的熊猫麻将线上拼桌APP现招给力代理商，庞大的用户市场，入门门槛极低，申请代理商资格请联系客服。');
