/*tb_users*/
ALTER TABLE `tb_users` ADD COLUMN `parent_id` int(11) DEFAULT NULL COMMENT '上级用户id';


INSERT INTO `panda`.`tb_dictionary` (`key`, `value`) VALUES ('invite.joinCount', '5');
INSERT INTO `panda`.`tb_dictionary` (`key`, `value`) VALUES ('invite.regPackagePrice', '10');
INSERT INTO `panda`.`tb_dictionary` (`key`, `value`) VALUES ('invite.playAwardPrice', '0.01');



UPDATE `panda`.`tb_dictionary` SET `dictionary_id`='13', `key`='home.group.info.top.text-ad', `value`='金币100枚以上可每隔180分钟领取房卡30张哟！' WHERE (`dictionary_id`='13');
UPDATE `panda`.`tb_dictionary` SET `dictionary_id`='14', `key`='home.group.info.center.left-ad.title', `value`='免费房卡不够用？' WHERE (`dictionary_id`='14');
UPDATE `panda`.`tb_dictionary` SET `dictionary_id`='15', `key`='home.group.info.center.left-ad.desc', `value`='充值金币特价房卡享不停' WHERE (`dictionary_id`='15');
UPDATE `panda`.`tb_dictionary` SET `dictionary_id`='16', `key`='home.group.info.center.right-ad.title', `value`='赢了游戏来领钱啦' WHERE (`dictionary_id`='16');
UPDATE `panda`.`tb_dictionary` SET `dictionary_id`='17', `key`='home.group.info.center.right-ad.desc', `value`='秒结算！真金白银带回家' WHERE (`dictionary_id`='17');
UPDATE `panda`.`tb_dictionary` SET `dictionary_id`='18', `key`='home.group.info.center.bottom-ad.title', `value`='免费领取熊猫麻将房卡' WHERE (`dictionary_id`='18');
UPDATE `panda`.`tb_dictionary` SET `dictionary_id`='19', `key`='home.group.info.center.bottom-ad.desc', `value`='多种玩法，快来大战三百回合~' WHERE (`dictionary_id`='19');
UPDATE `panda`.`tb_dictionary` SET `key` = 'home.group.info.center.right-ad.button', `value` = '我要提现' WHERE `dictionary_id` = 28;




/*UPDATE `panda`.`tb_dictionary` SET `key` = 'home.group.info.top.text-ad', `value` = '金币100枚以上可每隔180分钟领取房卡30张哟！' WHERE `dictionary_id` = 13;
UPDATE `panda`.`tb_dictionary` SET `key` = 'home.group.info.center.left-ad.title', `value` = '免费房卡不够用？' WHERE `dictionary_id` = 14;
UPDATE `panda`.`tb_dictionary` SET `key` = 'home.group.info.center.left-ad.desc', `value` = '充值金币特价房卡享不停' WHERE `dictionary_id` = 15;
UPDATE `panda`.`tb_dictionary` SET `key` = 'home.group.info.center.right-ad.title', `value` = '赢了游戏来兑换啦' WHERE `dictionary_id` = 16;
UPDATE `panda`.`tb_dictionary` SET `key` = 'home.group.info.center.right-ad.desc', `value` = '可爱宠物抱枕等你来拿' WHERE `dictionary_id` = 17;
UPDATE `panda`.`tb_dictionary` SET `key` = 'home.group.info.center.bottom-ad.title', `value` = '免费领取熊猫麻将房卡' WHERE `dictionary_id` = 18;
UPDATE `panda`.`tb_dictionary` SET `key` = 'home.group.info.center.bottom-ad.desc', `value` = '多种玩法，快来大战三百回合~' WHERE `dictionary_id` = 19;
UPDATE `panda`.`tb_dictionary` SET `key` = 'home.group.info.center.right-ad.button', `value` = '抢先预览' WHERE `dictionary_id` = 28;

*/
