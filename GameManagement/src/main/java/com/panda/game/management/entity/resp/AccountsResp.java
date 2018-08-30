/***
 * @pName management
 * @name AccountsResp
 * @user HongWei
 * @date 2018/8/27
 * @desc
 */
package com.panda.game.management.entity.resp;

import com.panda.game.management.entity.db.Accounts;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountsResp {
    private String accountsId;
    private String payId;
    private Integer tradeAccountId;
    private String tradeAccountName;
    private Integer accountsType;
    private Integer currency;
    private Double amount;
    private Double beforeBalance;
    private Double afterBalance;
    private Date addTime;
    private String remark;
    private Integer pageCount;
}
