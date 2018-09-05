/*users*/
INSERT INTO `panda`.`tb_users` (`phone`, `password`, `add_date`, `update_date`, `ip`, `finance_id`, `finance_name`, `panda_id`, `is_enable`, `is_delete`, `level`) VALUES ('10000000000', '23265e00f7e90435f2cdc30fd7e60832', '2018-08-21 14:59:28', '2018-09-05 00:24:10', '192.168.1.1', NULL, NULL, '10000000000', '1', '0', '10');

/*wallets*/
INSERT INTO `panda`.`tb_wallets` (`user_id`, `balance`, `update_time`, `version`) VALUES ('1', '1000000.00', '2018-09-05 00:22:12', '0');

/*dictionary*/
INSERT INTO `panda`.`tb_dictionary` (`key`, `value`) VALUES ('finance.pays.channel.internal', '站内交易');
INSERT INTO `panda`.`tb_dictionary` (`key`, `value`) VALUES ('finance.pays.channel.alipay', '支付宝');
INSERT INTO `panda`.`tb_dictionary` (`key`, `value`) VALUES ('finance.pays.product.sms', '短信');
INSERT INTO `panda`.`tb_dictionary` (`key`, `value`) VALUES ('finance.pays.product.currency', '通用货币');
INSERT INTO `panda`.`tb_dictionary` (`key`, `value`) VALUES ('finance.pays.product.roomCard', '房卡');
INSERT INTO `panda`.`tb_dictionary` (`key`, `value`) VALUES ('finance.pays.trade.recharge', '充值');
INSERT INTO `panda`.`tb_dictionary` (`key`, `value`) VALUES ('finance.pays.trade.withdraw', '提现');
INSERT INTO `panda`.`tb_dictionary` (`key`, `value`) VALUES ('finance.pays.trade.deduction', '扣费');
INSERT INTO `panda`.`tb_dictionary` (`key`, `value`) VALUES ('finance.pays.trade.consume', '消费');
INSERT INTO `panda`.`tb_dictionary` (`key`, `value`) VALUES ('home.group.info.top.runner-ad.1', '../images/default_ad.png');
INSERT INTO `panda`.`tb_dictionary` (`key`, `value`) VALUES ('home.group.info.top.runner-ad.2', '../images/wechat_ad.png');
INSERT INTO `panda`.`tb_dictionary` (`key`, `value`) VALUES ('home.group.info.top.runner-ad.3', '../images/wechat_ad.png');
INSERT INTO `panda`.`tb_dictionary` (`key`, `value`) VALUES ('home.group.info.top.text-ad', '金币100枚以上可每隔180分钟领取房卡30张哟！');
INSERT INTO `panda`.`tb_dictionary` (`key`, `value`) VALUES ('home.group.info.center.left-ad.title', '免费房卡不够用？');
INSERT INTO `panda`.`tb_dictionary` (`key`, `value`) VALUES ('home.group.info.center.left-ad.desc', '充值金币特价房卡享不停');
INSERT INTO `panda`.`tb_dictionary` (`key`, `value`) VALUES ('home.group.info.center.right-ad.title', '赢了游戏来领钱啦');
INSERT INTO `panda`.`tb_dictionary` (`key`, `value`) VALUES ('home.group.info.center.right-ad.desc', '秒结算！真金白银带回家');
INSERT INTO `panda`.`tb_dictionary` (`key`, `value`) VALUES ('home.group.info.center.bottom-ad.title', '免费领取熊猫麻将房卡');
INSERT INTO `panda`.`tb_dictionary` (`key`, `value`) VALUES ('home.group.info.center.bottom-ad.desc', '多种玩法，快来大战三百回合~');
INSERT INTO `panda`.`tb_dictionary` (`key`, `value`) VALUES ('home.group.info.center.bottom-ad.qcode', 'http://132.232.61.181:8090/app/qrcode.png');
INSERT INTO `panda`.`tb_dictionary` (`key`, `value`) VALUES ('my.consume.service.html', '<ul class=\"mui-table-view\">\n<li class=\"mui-table-view-cell\">QQ: 9588671</li>\n<li class=\"mui-table-view-cell\">微信: kd888288</li></ul>');
INSERT INTO `panda`.`tb_dictionary` (`key`, `value`) VALUES ('finance.pays.qrcode.image', 'default_pay_qrcode.png');
INSERT INTO `panda`.`tb_dictionary` (`key`, `value`) VALUES ('finance.pays.qrcode.payCode', 'FKX01971FVU2LCLFRYARF4');
INSERT INTO `panda`.`tb_dictionary` (`key`, `value`) VALUES ('finance.give.amount', '5');
INSERT INTO `panda`.`tb_dictionary` (`key`, `value`) VALUES ('version', 'U1.0.1');
INSERT INTO `panda`.`tb_dictionary` (`key`, `value`) VALUES ('android.download', 'http://132.232.61.181:8090/app/1LE.apk');
INSERT INTO `panda`.`tb_dictionary` (`key`, `value`) VALUES ('ios.download', 'http://132.232.61.181:8090/app/1LE.ipa');



/*permission*/
INSERT INTO `panda`.`tb_permission` (`permission_name`, `target_url`) VALUES ('ADMIN', '/management/index');
INSERT INTO `panda`.`tb_permission` (`permission_name`, `target_url`) VALUES ('ADMIN', '/management/finance/pay');
INSERT INTO `panda`.`tb_permission` (`permission_name`, `target_url`) VALUES ('ADMIN', 'management/finance/pay/*');
INSERT INTO `panda`.`tb_permission` (`permission_name`, `target_url`) VALUES ('ADMIN', '/management/finance/recharge');
INSERT INTO `panda`.`tb_permission` (`permission_name`, `target_url`) VALUES ('ADMIN', '/management/finance/recharge/*');
INSERT INTO `panda`.`tb_permission` (`permission_name`, `target_url`) VALUES ('ADMIN', '/management/config/*');
INSERT INTO `panda`.`tb_permission` (`permission_name`, `target_url`) VALUES ('ADMIN', '/management/member');
INSERT INTO `panda`.`tb_permission` (`permission_name`, `target_url`) VALUES ('ADMIN', '/management/member/*');
INSERT INTO `panda`.`tb_permission` (`permission_name`, `target_url`) VALUES ('STAFF', '/management/index');
INSERT INTO `panda`.`tb_permission` (`permission_name`, `target_url`) VALUES ('STAFF', '/management/finance/accounts');
INSERT INTO `panda`.`tb_permission` (`permission_name`, `target_url`) VALUES ('STAFF', '/management/finance/accounts/*');
INSERT INTO `panda`.`tb_permission` (`permission_name`, `target_url`) VALUES ('ADMIN', '/management/finance/accounts');
INSERT INTO `panda`.`tb_permission` (`permission_name`, `target_url`) VALUES ('ADMIN', '/management/finance/accounts/*');
INSERT INTO `panda`.`tb_permission` (`permission_name`, `target_url`) VALUES ('ADMIN', '/management/finance/room/*');
INSERT INTO `panda`.`tb_permission` (`permission_name`, `target_url`) VALUES ('ADMIN', '/management/finance/withdraw/*');
INSERT INTO `panda`.`tb_permission` (`permission_name`, `target_url`) VALUES ('STAFF', '/management/finance/withdraw/*');
INSERT INTO `panda`.`tb_permission` (`permission_name`, `target_url`) VALUES ('STAFF', '/management/finance/room/*');
INSERT INTO `panda`.`tb_permission` (`permission_name`, `target_url`) VALUES ('ADMIN', '/management/config/index');
INSERT INTO `panda`.`tb_permission` (`permission_name`, `target_url`) VALUES ('ADMIN', '/management/area/index');
INSERT INTO `panda`.`tb_permission` (`permission_name`, `target_url`) VALUES ('ADMIN', '/management/area/*');
INSERT INTO `panda`.`tb_permission` (`permission_name`, `target_url`) VALUES ('ADMIN', '/management/room/index');
INSERT INTO `panda`.`tb_permission` (`permission_name`, `target_url`) VALUES ('ADMIN', '/management/room/*');



/*permission relation*/
INSERT INTO `panda`.`tb_permission_relation` (`permission_id`, `user_id`, `permission_role`) VALUES ('1', '1', 'ROLE_ADMIN');
INSERT INTO `panda`.`tb_permission_relation` (`permission_id`, `user_id`, `permission_role`) VALUES ('2', '1', 'ROLE_ADMIN');
INSERT INTO `panda`.`tb_permission_relation` (`permission_id`, `user_id`, `permission_role`) VALUES ('3', '1', 'ROLE_ADMIN');
INSERT INTO `panda`.`tb_permission_relation` (`permission_id`, `user_id`, `permission_role`) VALUES ('4', '1', 'ROLE_ADMIN');
INSERT INTO `panda`.`tb_permission_relation` (`permission_id`, `user_id`, `permission_role`) VALUES ('5', '1', 'ROLE_ADMIN');
INSERT INTO `panda`.`tb_permission_relation` (`permission_id`, `user_id`, `permission_role`) VALUES ('6', '1', 'ROLE_ADMIN');
INSERT INTO `panda`.`tb_permission_relation` (`permission_id`, `user_id`, `permission_role`) VALUES ('7', '1', 'ROLE_ADMIN');
INSERT INTO `panda`.`tb_permission_relation` (`permission_id`, `user_id`, `permission_role`) VALUES ('8', '1', 'ROLE_ADMIN');
INSERT INTO `panda`.`tb_permission_relation` (`permission_id`, `user_id`, `permission_role`) VALUES ('9', '33', 'ROLE_STAFF');
INSERT INTO `panda`.`tb_permission_relation` (`permission_id`, `user_id`, `permission_role`) VALUES ('10', '33', 'ROLE_STAFF');
INSERT INTO `panda`.`tb_permission_relation` (`permission_id`, `user_id`, `permission_role`) VALUES ('11', '33', 'ROLE_STAFF');
INSERT INTO `panda`.`tb_permission_relation` (`permission_id`, `user_id`, `permission_role`) VALUES ('12', '33', 'ROLE_STAFF');
INSERT INTO `panda`.`tb_permission_relation` (`permission_id`, `user_id`, `permission_role`) VALUES ('16', '33', 'ROLE_STAFF');
INSERT INTO `panda`.`tb_permission_relation` (`permission_id`, `user_id`, `permission_role`) VALUES ('12', '1', 'ROLE_ADMIN');
INSERT INTO `panda`.`tb_permission_relation` (`permission_id`, `user_id`, `permission_role`) VALUES ('13', '1', 'ROLE_ADMIN');
INSERT INTO `panda`.`tb_permission_relation` (`permission_id`, `user_id`, `permission_role`) VALUES ('14', '1', 'ROLE_ADMIN');
INSERT INTO `panda`.`tb_permission_relation` (`permission_id`, `user_id`, `permission_role`) VALUES ('15', '1', 'ROLE_ADMIN');
INSERT INTO `panda`.`tb_permission_relation` (`permission_id`, `user_id`, `permission_role`) VALUES ('17', '33', 'ROLE_STAFF');
INSERT INTO `panda`.`tb_permission_relation` (`permission_id`, `user_id`, `permission_role`) VALUES ('18', '1', 'ROLE_ADMIN');
INSERT INTO `panda`.`tb_permission_relation` (`permission_id`, `user_id`, `permission_role`) VALUES ('19', '1', 'ROLE_ADMIN');
INSERT INTO `panda`.`tb_permission_relation` (`permission_id`, `user_id`, `permission_role`) VALUES ('20', '1', 'ROLE_ADMIN');
INSERT INTO `panda`.`tb_permission_relation` (`permission_id`, `user_id`, `permission_role`) VALUES ('21', '1', 'ROLE_ADMIN');
INSERT INTO `panda`.`tb_permission_relation` (`permission_id`, `user_id`, `permission_role`) VALUES ('22', '1', 'ROLE_ADMIN');




/*subareas*/
INSERT INTO `panda`.`tb_subareas` (`parent_id`, `is_leaf`, `is_relation`, `name`, `background_image`, `price`, `add_time`, `max_person_count`, `limit_price`, `reduce_price`, `is_enable`) VALUES ('0', '0', '1', '0.1元体验区', '../images/subareas/1.png', '0.10', '2018-08-17 23:06:05', '0', '0.10', '0.10', '1');
INSERT INTO `panda`.`tb_subareas` (`parent_id`, `is_leaf`, `is_relation`, `name`, `background_image`, `price`, `add_time`, `max_person_count`, `limit_price`, `reduce_price`, `is_enable`) VALUES ('0', '0', '1', '1元分区', '../images/subareas/2.png', '1.00', '2018-08-17 23:07:03', '0', '100.00', '1.00', '1');
INSERT INTO `panda`.`tb_subareas` (`parent_id`, `is_leaf`, `is_relation`, `name`, `background_image`, `price`, `add_time`, `max_person_count`, `limit_price`, `reduce_price`, `is_enable`) VALUES ('0', '0', '1', '2元分区', '../images/subareas/3.png', '2.00', '2018-08-17 23:07:16', '0', '200.00', '1.00', '1');
INSERT INTO `panda`.`tb_subareas` (`parent_id`, `is_leaf`, `is_relation`, `name`, `background_image`, `price`, `add_time`, `max_person_count`, `limit_price`, `reduce_price`, `is_enable`) VALUES ('0', '0', '1', '5元分区', '../images/subareas/4.png', '5.00', '2018-08-17 00:00:00', '0', '500.00', '2.00', '1');
INSERT INTO `panda`.`tb_subareas` (`parent_id`, `is_leaf`, `is_relation`, `name`, `background_image`, `price`, `add_time`, `max_person_count`, `limit_price`, `reduce_price`, `is_enable`) VALUES ('0', '0', '0', '血战到底', '../images/subareas/room/6.png', NULL, '2018-08-17 23:09:43', '4', '5.00', '0.00', '1');
INSERT INTO `panda`.`tb_subareas` (`parent_id`, `is_leaf`, `is_relation`, `name`, `background_image`, `price`, `add_time`, `max_person_count`, `limit_price`, `reduce_price`, `is_enable`) VALUES ('0', '0', '0', '三人两房', '../images/subareas/room/2.png', NULL, '2018-08-17 23:09:54', '3', '5.00', '0.00', '1');
INSERT INTO `panda`.`tb_subareas` (`parent_id`, `is_leaf`, `is_relation`, `name`, `background_image`, `price`, `add_time`, `max_person_count`, `limit_price`, `reduce_price`, `is_enable`) VALUES ('0', '0', '0', '四人两房', '../images/subareas/room/4.png', NULL, '2018-08-17 23:10:10', '4', '5.00', '0.00', '1');
INSERT INTO `panda`.`tb_subareas` (`parent_id`, `is_leaf`, `is_relation`, `name`, `background_image`, `price`, `add_time`, `max_person_count`, `limit_price`, `reduce_price`, `is_enable`) VALUES ('0', '0', '0', '血流成河', '../images/subareas/room/5.png', NULL, '2018-08-17 23:10:18', '4', '5.00', '0.00', '1');
INSERT INTO `panda`.`tb_subareas` (`parent_id`, `is_leaf`, `is_relation`, `name`, `background_image`, `price`, `add_time`, `max_person_count`, `limit_price`, `reduce_price`, `is_enable`) VALUES ('0', '0', '0', '血战换三张', '../images/subareas/room/7.png', NULL, '2018-08-17 23:10:33', '4', '5.00', '0.00', '1');
INSERT INTO `panda`.`tb_subareas` (`parent_id`, `is_leaf`, `is_relation`, `name`, `background_image`, `price`, `add_time`, `max_person_count`, `limit_price`, `reduce_price`, `is_enable`) VALUES ( '0', '0', '0', '三人三房', '../images/subareas/room/3.png', NULL, '2018-08-17 23:10:45', '3', '5.00', '0.00', '1');
INSERT INTO `panda`.`tb_subareas` (`parent_id`, `is_leaf`, `is_relation`, `name`, `background_image`, `price`, `add_time`, `max_person_count`, `limit_price`, `reduce_price`, `is_enable`) VALUES ( '0', '0', '0', '两人麻将', '../images/subareas/room/1.png', NULL, '2018-08-17 23:10:57', '2', '5.00', '0.00', '1');


/*subareas relations*/
INSERT INTO `panda`.`tb_subarea_relations` (`subarea_id`, `join_subarea_id`) VALUES ('1', '5');
INSERT INTO `panda`.`tb_subarea_relations` (`subarea_id`, `join_subarea_id`) VALUES ('1', '6');
INSERT INTO `panda`.`tb_subarea_relations` (`subarea_id`, `join_subarea_id`) VALUES ('1', '7');
INSERT INTO `panda`.`tb_subarea_relations` (`subarea_id`, `join_subarea_id`) VALUES ('1', '8');
INSERT INTO `panda`.`tb_subarea_relations` (`subarea_id`, `join_subarea_id`) VALUES ('1', '9');
INSERT INTO `panda`.`tb_subarea_relations` (`subarea_id`, `join_subarea_id`) VALUES ('1', '10');
INSERT INTO `panda`.`tb_subarea_relations` (`subarea_id`, `join_subarea_id`) VALUES ('1', '11');
INSERT INTO `panda`.`tb_subarea_relations` (`subarea_id`, `join_subarea_id`) VALUES ('2', '5');
INSERT INTO `panda`.`tb_subarea_relations` (`subarea_id`, `join_subarea_id`) VALUES ('2', '6');
INSERT INTO `panda`.`tb_subarea_relations` (`subarea_id`, `join_subarea_id`) VALUES ( '2', '7');
INSERT INTO `panda`.`tb_subarea_relations` (`subarea_id`, `join_subarea_id`) VALUES ( '2', '8');
INSERT INTO `panda`.`tb_subarea_relations` (`subarea_id`, `join_subarea_id`) VALUES ( '2', '9');
INSERT INTO `panda`.`tb_subarea_relations` (`subarea_id`, `join_subarea_id`) VALUES ( '2', '10');
INSERT INTO `panda`.`tb_subarea_relations` (`subarea_id`, `join_subarea_id`) VALUES ( '2', '11');
INSERT INTO `panda`.`tb_subarea_relations` (`subarea_id`, `join_subarea_id`) VALUES ( '3', '5');
INSERT INTO `panda`.`tb_subarea_relations` (`subarea_id`, `join_subarea_id`) VALUES ( '3', '6');
INSERT INTO `panda`.`tb_subarea_relations` (`subarea_id`, `join_subarea_id`) VALUES ( '3', '7');
INSERT INTO `panda`.`tb_subarea_relations` (`subarea_id`, `join_subarea_id`) VALUES ( '3', '8');
INSERT INTO `panda`.`tb_subarea_relations` (`subarea_id`, `join_subarea_id`) VALUES ( '3', '9');
INSERT INTO `panda`.`tb_subarea_relations` (`subarea_id`, `join_subarea_id`) VALUES ( '3', '10');
INSERT INTO `panda`.`tb_subarea_relations` (`subarea_id`, `join_subarea_id`) VALUES ( '3', '11');
INSERT INTO `panda`.`tb_subarea_relations` (`subarea_id`, `join_subarea_id`) VALUES ( '4', '5');
INSERT INTO `panda`.`tb_subarea_relations` (`subarea_id`, `join_subarea_id`) VALUES ( '4', '6');
INSERT INTO `panda`.`tb_subarea_relations` (`subarea_id`, `join_subarea_id`) VALUES ( '4', '7');
INSERT INTO `panda`.`tb_subarea_relations` (`subarea_id`, `join_subarea_id`) VALUES ( '4', '8');
INSERT INTO `panda`.`tb_subarea_relations` (`subarea_id`, `join_subarea_id`) VALUES ( '4', '9');
INSERT INTO `panda`.`tb_subarea_relations` (`subarea_id`, `join_subarea_id`) VALUES ( '4', '10');
INSERT INTO `panda`.`tb_subarea_relations` (`subarea_id`, `join_subarea_id`) VALUES ( '4', '11');

