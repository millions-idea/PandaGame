/*备份*/
INSERT INTO `panda`.`tb_subareas` (`subarea_id`, `parent_id`, `is_leaf`, `is_relation`, `name`, `background_image`, `price`, `add_time`, `max_person_count`, `limit_price`, `reduce_price`, `is_enable`) VALUES ('8', '0', '0', '0', '血流成河', '../images/subareas/room/5.png', NULL, '2018-08-17 23:10:18', '4', '5.00', '0.00', '1');
INSERT INTO `panda`.`tb_subareas` (`subarea_id`, `parent_id`, `is_leaf`, `is_relation`, `name`, `background_image`, `price`, `add_time`, `max_person_count`, `limit_price`, `reduce_price`, `is_enable`) VALUES ('9', '0', '0', '0', '血战换三张', '../images/subareas/room/7.png', NULL, '2018-08-17 23:10:33', '4', '5.00', '0.00', '1');
INSERT INTO `panda`.`tb_subareas` (`subarea_id`, `parent_id`, `is_leaf`, `is_relation`, `name`, `background_image`, `price`, `add_time`, `max_person_count`, `limit_price`, `reduce_price`, `is_enable`) VALUES ('10', '0', '0', '0', '三人三房', '../images/subareas/room/3.png', NULL, '2018-08-17 23:10:45', '3', '5.00', '0.00', '1');


/*优化用户体验 删除不用的分区*/
UPDATE `panda`.`tb_subareas` SET `is_enable`='0' WHERE (`subarea_id`='8');
UPDATE `panda`.`tb_subareas` SET `is_enable`='0' WHERE (`subarea_id`='9');
UPDATE `panda`.`tb_subareas` SET `is_enable`='0' WHERE (`subarea_id`='10');