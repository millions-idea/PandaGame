ALTER TABLE `tb_users` MODIFY COLUMN `panda_id` varchar(64) DEFAULT NULL COMMENT '熊猫id';
ALTER TABLE `tb_room_card` MODIFY COLUMN `panda_id` varchar(64) DEFAULT NULL COMMENT '熊猫id';

/*原本用于送审支付宝，现拿掉直接上应用市场*/
/*INSERT INTO `panda`.`tb_dictionary` (`dictionary_id`, `key`, `value`) VALUES ('28', 'home.group.info.center.right-ad.button', '抢先预览');
UPDATE `panda`.`tb_dictionary` SET `dictionary_id`='16', `key`='home.group.info.center.right-ad.title', `value`='赢了游戏来兑换啦' WHERE (`dictionary_id`='16');
UPDATE `panda`.`tb_dictionary` SET `dictionary_id`='17', `key`='home.group.info.center.right-ad.desc', `value`='可爱宠物抱枕等你来拿' WHERE (`dictionary_id`='17');*/



UPDATE `panda`.`tb_dictionary` SET `dictionary_id`='16', `key`='home.group.info.center.right-ad.title', `value`='赢了游戏来领钱啦' WHERE (`dictionary_id`='16');
UPDATE `panda`.`tb_dictionary` SET `dictionary_id`='17', `key`='home.group.info.center.right-ad.desc', `value`='秒结算！真金白银带回家' WHERE (`dictionary_id`='17');
INSERT INTO `panda`.`tb_dictionary` (`dictionary_id`, `key`, `value`) VALUES ('28', 'home.group.info.center.right-ad.button', '我要提现');


<<<<<<< HEAD
UPDATE `panda`.`tb_subareas` SET `subarea_id`='1', `parent_id`='0', `is_leaf`='0', `is_relation`='1', `name`='0.1元体验区', `background_image`='../images/subareas/1.png', `price`='0.10', `add_time`='2018-08-17 23:06:05', `max_person_count`='0', `limit_price`='0.10', `reduce_price`='0.10', `is_enable`='0' WHERE (`subarea_id`='1');
=======
>>>>>>> 881bf00ee0818caafc0a5e0e8e87e3f6971583bb
