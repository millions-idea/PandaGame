/***
 * @pName management
 * @name Wallets
 * @user HongWei
 * @date 2018/8/15
 * @desc 用户钱包表
 */
package com.panda.game.management.entity.db;

import lombok.*;
import tk.mybatis.mapper.annotation.Version;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "tb_wallets")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Wallets {
    @Id
    private Integer walletId;
    private Integer userId;
    private Double balance;
    private Date updateTime;
    @Version
    private Integer version;
}
