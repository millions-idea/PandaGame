/***
 * @pName management
 * @name UsersView
 * @user HongWei
 * @date 2018/8/14
 * @desc
 */
package com.panda.game.management.entity.resp;

import com.fasterxml.jackson.annotation.JsonView;
import com.panda.game.management.entity.db.Users;
import lombok.*;
import org.apache.tomcat.jni.User;

import java.util.Date;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserResp {
    public interface SecurityView {}
    @JsonView(SecurityView.class)
    private Integer userId;
    @JsonView(SecurityView.class)
    private String phone;
    private String password;
    private Date addDate;
    private Date updateDate;
    private String ip;
    private Integer isEnable;
    private Integer isDelete;
    @JsonView(SecurityView.class)
    private String token;
}
