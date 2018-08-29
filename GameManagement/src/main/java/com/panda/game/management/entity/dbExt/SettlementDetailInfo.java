/***
 * @pName management
 * @name SettlementDetailInfo
 * @user HongWei
 * @date 2018/8/29
 * @desc
 */
package com.panda.game.management.entity.dbExt;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SettlementDetailInfo {
    /*settlement*/
    private Integer settlementId;
    private Integer userId;
    private Long roomCode;
    private Double grade;
    private Integer state;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date addTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    private String remark;

    /*settlement extend*/
    private String roomName;
    private Integer status;
    private Long externalRoomId;
    private Double countGrade;

    /*users*/
    private String phone;
    private String pandaId;
}
