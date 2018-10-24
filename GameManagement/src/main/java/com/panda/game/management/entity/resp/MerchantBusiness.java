/***
 * @pName GameManagement
 * @name MerchantBusiness
 * @user HongWei
 * @date 2018/10/23
 * @desc
 */
package com.panda.game.management.entity.resp;

import com.panda.game.management.entity.db.MerchantMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MerchantBusiness {
    private Double regiIncome;
    private Double returnIncome;
    private List<MerchantMessage> merchantMessageList;
    private Integer friends;
}
