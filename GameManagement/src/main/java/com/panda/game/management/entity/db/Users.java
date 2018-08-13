/***
 * @pName management
 * @name Users
 * @user HongWei
 * @date 2018/8/13
 * @desc
 */
package com.panda.game.management.entity.db;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Table;
import java.util.Date;

@Table(name = "tb_users")
@Getter
@Setter
@ToString
@AllArgsConstructor
public class Users {
    private Integer userId;
    private String phone;
    private String password;
    private Date addDate;
    private Date updateDate;
    private String ip;
    private Integer isEnable;
    private Integer isDelete;
}
