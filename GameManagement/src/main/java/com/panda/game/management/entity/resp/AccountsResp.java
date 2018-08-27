/***
 * @pName management
 * @name AccountsResp
 * @user HongWei
 * @date 2018/8/27
 * @desc
 */
package com.panda.game.management.entity.resp;

import com.panda.game.management.entity.db.Accounts;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountsResp extends Accounts {
    private Integer pageCount;
}
