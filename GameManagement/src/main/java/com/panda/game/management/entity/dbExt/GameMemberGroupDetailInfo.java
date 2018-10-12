/***
 * @pName GameManagement
 * @name GameMemberGroupDetailInfo
 * @user HongWei
 * @date 2018/10/12
 * @desc
 */
package com.panda.game.management.entity.dbExt;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class GameMemberGroupDetailInfo {
    private Integer groupId;
    private Long roomCode;
    private Integer userId;
    private Integer isOwner;
    private Integer isConfirm;
    private Date addTime;
    private Date exitTime;

    /*动态计算*/
    private Integer parentUserId;
    private Integer joinCount;
    private String  phone;
}
