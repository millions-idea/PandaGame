/***
 * @pName management
 * @name PayParam
 * @user HongWei
 * @date 2018/8/16
 * @desc 交易专用参数类
 */
package com.panda.game.management.entity.param;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PayParam {
    private Integer fromUid;
    private Integer toUid;
    private Double amount;
    private String remark;
    private long systemRecordId;
}
