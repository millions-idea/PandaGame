/***
 * @pName management
 * @name Exception
 * @user HongWei
 * @date 2018/8/13
 * @desc
 */
package com.panda.game.management.entity.db;

import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Table(name = "tb_exceptions")
@Getter
@Setter
@ToString
@AllArgsConstructor
public class Exceptions {
    private Integer logId;
    private Integer userId;
    private String body;
    private Date addDate;
}
