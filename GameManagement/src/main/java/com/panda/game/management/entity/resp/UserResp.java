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
@AllArgsConstructor
@NoArgsConstructor
public class UserResp {
    public interface SampleView {}
    public interface FinanceView extends SampleView {}
    public interface SecurityView extends SampleView  {}

    @JsonView(SecurityView.class)
    private Integer userId;
    @JsonView(SecurityView.class)
    private String phone;
    @JsonView(SecurityView.class)
    private String token;
    @JsonView(SecurityView.class)
    private String pandaId;


    @JsonView(FinanceView.class)
    private Double balance;
    @JsonView(FinanceView.class)
    private Double canWithdrawAmount;
    @JsonView(FinanceView.class)
    private Double canNotWithdrawAmount;

    @JsonView(SecurityView.class)
    public String getPhone() {
        String encryptPhoneBefore = phone.substring(0,3);
        String encryptPhoneAfter = phone.substring(9,11);
        String encryptPhone = StringUtil.padLeft(encryptPhoneBefore, 9, '*').concat(encryptPhoneAfter);
        return encryptPhone;
    }


    @JsonView(FinanceView.class)
    /**
     * 总收入
     */
    private Double incomeAmount;


    @JsonView(FinanceView.class)
    /**
     * 总支出
     */
    private Double expendAmount;

}
