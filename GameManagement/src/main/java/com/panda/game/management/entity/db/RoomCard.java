/***
 * @pName management
 * @name RoomCard
 * @user HongWei
 * @date 2018/8/20
 * @desc
 */
package com.panda.game.management.entity.db;

import lombok.*;

import javax.persistence.Table;
import java.util.Date;

@Table(name = "tb_room_card")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class RoomCard {
    private Integer cardId;
    private Integer userId;
    private String pandaId;
    private Integer state;
    private Date addTime;
}
