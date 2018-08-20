/***
 * @pName management
 * @name GameRoom
 * @user HongWei
 * @date 2018/8/18
 * @desc
 */
package com.panda.game.management.entity.db;


import lombok.*;

import javax.persistence.Table;
import java.util.Date;

@Table(name = "tb_game_room")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class GameRoom {
    private Integer roomId;
    private Integer ownerId;
    private Integer parentAreaId;
    private Integer subareaId;
    private Integer status;
    private String name;
    private Date updateTime;
    private Date addTime;
    private Integer isEnable;
    private Long roomCode;
    private Integer externalRoomId;
}
