/***
 * @pName management
 * @name WithdrawDetailInfo
 * @user HongWei
 * @date 2018/8/30
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
public class RechargeDetailInfo {
    /*recharge*/
    private Integer rechargeId;
    private Integer userId;
    private Double amount;
    private Integer state;
    private String remark;
    private Long systemRecordId;
    private String channelRecordId;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date addTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /*users*/
    private String phone;
}
