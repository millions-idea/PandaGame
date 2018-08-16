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
    private Integer isEnable;
    private Integer isDelete;

    /*wallets*/
    private Integer walletId;
    private Double balance;
    private Date updateTime;
    private Integer version;
}
