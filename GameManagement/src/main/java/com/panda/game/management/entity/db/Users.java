/***
 * @pName management
 * @name Users
 * @user HongWei
 * @date 2018/8/13
 * @desc
 */
package com.panda.game.management.entity.db;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "tb_users")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Users {
    @Id
    @KeySql(useGeneratedKeys = true)
    private Integer userId;
    private String phone;
    private String password;
    private Date addDate;
    private Date updateDate;
    private String ip;
    private String pandaId;
    private Integer isEnable;
    private Integer isDelete;
    private Integer level;
}
