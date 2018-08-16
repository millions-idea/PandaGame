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
import com.panda.game.management.utils.StringUtil;
import lombok.*;
import org.apache.tomcat.jni.User;

import java.util.Date;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserResp {
    public interface SampleView {}
    public interface FinanceView extends SampleView {}
    public interface SecurityView extends SampleView  {}

    private Integer userId;
    private String phone;
    private String token;

    private Double balance;
    private Double canWithdrawAmount;
    private Double canNotWithdrawAmount;

    @JsonView(SecurityView.class)
    public Integer getUserId() {
        return userId;
    }

    @JsonView(SecurityView.class)
    public String getToken() {
        return token;
    }

    @JsonView(SecurityView.class)
    public String getPhone() {
        String encryptPhoneBefore = phone.substring(0,3);
        String encryptPhoneAfter = phone.substring(9,11);
        String encryptPhone = StringUtil.padLeft(encryptPhoneBefore, 9, '*').concat(encryptPhoneAfter);
        return encryptPhone;
    }

    @JsonView(FinanceView.class)
    public Double getBalance() {
        return balance;
    }

    @JsonView(FinanceView.class)
    public Double getCanWithdrawAmount() {
        return canWithdrawAmount;
    }

    @JsonView(FinanceView.class)
    public Double getCanNotWithdrawAmount() {
        return canNotWithdrawAmount;
    }
}
