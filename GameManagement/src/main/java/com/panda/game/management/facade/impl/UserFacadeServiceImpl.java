/***
 * @pName management
 * @name UserFacadeServiceImpl
 * @user HongWei
 * @date 2018/8/20
 * @desc
 */
package com.panda.game.management.facade.impl;

import com.panda.game.management.entity.Constant;
import com.panda.game.management.entity.DataDictionary;
import com.panda.game.management.entity.db.Dictionary;
import com.panda.game.management.entity.db.Users;
import com.panda.game.management.entity.param.PayParam;
import com.panda.game.management.exception.MsgException;
import com.panda.game.management.facade.UserFacadeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserFacadeServiceImpl implements UserFacadeService {
    @Autowired
    private com.panda.game.management.biz.IUserService userService;

    @Autowired
    private com.panda.game.management.biz.IPayService payService;

    /**
     * 注册用户 韦德 2018年8月20日15:39:37
     *
     * @param param
     * @return
     */
    @Override
    @Transactional
    public boolean register(Users param) {
        Dictionary dictionary = DataDictionary.DATA_DICTIONARY.get("finance.give.amount");
        if(dictionary == null) throw new MsgException("注册异常，请稍后重试！");

        // 注册用户、开通钱包
        userService.register(param);

        // 赠送5枚金币
        PayParam payParam = new PayParam();
        payParam.setFromUid(Constant.SYSTEM_ACCOUNTS_ID);
        payParam.setAmount(Double.valueOf(dictionary.getValue()));
        payParam.setToUid(param.getUserId());
        payParam.setRemark("新用户注册奖励");
        payParam.setCurrency(1);
        payService.recharge(payParam);

        return true;
    }
}
