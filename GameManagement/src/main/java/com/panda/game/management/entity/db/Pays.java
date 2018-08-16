/***
 * @pName management
 * @name Pays
 * @user HongWei
 * @date 2018/8/15
 * @desc 财务交易记录表
 */
package com.panda.game.management.entity.db;

import lombok.*;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "tb_pays")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Pays {
    @Id
    private Long payId;
    private Integer fromUid;
    private String fromName;
    private Integer toUid;
    private String toName;
    private Integer channelType;
    private String channelName;
    private Integer productType;
    private String productName;
    private Integer tradeType;
    private String tradeName;
    private Date addTime;
    private Double amount;
    private Long systemRecordId;
    private String remark;
    private String channelRecordId;
    private Integer status;
    private Date toAccountTime;
}
