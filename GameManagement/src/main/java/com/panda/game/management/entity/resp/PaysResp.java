/***
 * @pName management
 * @name PaysResp
 * @user HongWei
 * @date 2018/8/30
 * @desc
 */
package com.panda.game.management.entity.resp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaysResp {
    private String payId;
    private Integer fromUid;
    private String fromName;
    private Integer toUid;
    private String toName;
    private Integer channelType;
    private String channelName;
    private Integer productType;
    private String productName;
    private Integer tradeType;
    private String tradeName;
    private Date addTime;
    private Double amount;
    private String systemRecordId;
    private String remark;
    private String channelRecordId;
    private Integer status;
    private Date toAccountTime;
}
