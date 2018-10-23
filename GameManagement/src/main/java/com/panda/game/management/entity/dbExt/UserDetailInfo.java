/***
 * @pName management
 * @name UserWalletDetail
 * @user HongWei
 * @date 2018/8/16
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
public class UserDetailInfo {
    /*users*/
    private Integer userId;
    private String phone;
    private String password;
    private Date addDate;
    private Date updateDate;
    private String ip;
    private String pandaId;
    private String financeId;
    private String financeName;
    private Integer isEnable;
    private Integer isDelete;
    private Integer level;

    /*wallets*/
    private Integer walletId;
    private Double balance;
    private Date roomCardGetTime;
    private Date updateTime;
    private Integer version;

    /*permission relation*/
    private String permissionRole;
}
