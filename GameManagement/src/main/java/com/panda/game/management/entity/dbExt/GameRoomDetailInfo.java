/***
 * @pName management
 * @name GameRoomDetailInfo
 * @user HongWei
 * @date 2018/8/19
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
public class GameRoomDetailInfo {
    /*gameRoom*/
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
    private Integer maxPersonCount;

    private String parentName;
    private Double parentPrice;

    /*gameRoomMemberGroup*/
    private Integer personCount;
    private Integer isOwner;

    /*subareas*/
    private String parentAreaName;
    private String subareaName;
}
