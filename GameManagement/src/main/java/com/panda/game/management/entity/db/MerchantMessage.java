/***
 * @pName GameManagement
 * @name MerchantMessages
 * @user HongWei
 * @date 2018/10/23
 * @desc
 */
package com.panda.game.management.entity.db;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "tb_merchant_messages")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MerchantMessage {
    @Id
    private Integer businessId;
    private Integer parentUserId;
    private Integer userId;
    private Double amount;
    private Integer type;
    private String remark;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date addTime;
}
