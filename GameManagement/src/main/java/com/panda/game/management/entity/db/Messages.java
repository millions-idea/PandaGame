/***
 * @pName management
 * @name Messages
 * @user HongWei
 * @date 2018/8/29
 * @desc
 */
package com.panda.game.management.entity.db;

import lombok.*;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "tb_messages")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Messages {
    @Id
    private Integer messageId;
    private Integer userId;
    private String message;
    private Integer state;
    private Date addTime;
}
