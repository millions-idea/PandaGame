/***
 * @pName management
 * @name GameMemberGroup
 * @user HongWei
 * @date 2018/8/19
 * @desc
 */
package com.panda.game.management.entity.db;

import lombok.*;

import javax.persistence.Table;
import java.util.Date;


@Table(name = "tb_game_member_group")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class GameMemberGroup {
    private Integer groupId;
    private Long roomCode;
    private Integer userId;
    private Integer isOwner;
    private Integer isConfirm;
    private Date addTime;
    private Date exitTime;
}
