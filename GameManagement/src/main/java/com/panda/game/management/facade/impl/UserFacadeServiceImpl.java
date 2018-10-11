/***
 * @pName management
 * @name UserFacadeServiceImpl
 * @user HongWei
 * @date 2018/8/20
 * @desc
 */
package com.panda.game.management.facade.impl;

import com.panda.game.management.biz.IMessageService;
import com.panda.game.management.entity.Constant;
import com.panda.game.management.entity.DataDictionary;
import com.panda.game.management.entity.db.Dictionary;
import com.panda.game.management.entity.db.Messages;
import com.panda.game.management.entity.db.Users;
import com.panda.game.management.entity.param.PayParam;
import com.panda.game.management.exception.MsgException;
import com.panda.game.management.facade.UserFacadeService;
import com.panda.game.management.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class UserFacadeServiceImpl implements UserFacadeService {
    @Autowired
    private com.panda.game.management.biz.IUserService userService;

    @Autowired
    private com.panda.game.management.biz.IPayService payService;

    @Autowired
    private IMessageService messageService;

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


        // 向邀请方推送通知消息
        if (param.getParentId() != 0){
            String encryptPhoneBefore = param.getPhone().substring(0,3);
            String encryptPhoneAfter = param.getPhone().substring(9,11);
            String encryptPhone = StringUtil.padLeft(encryptPhoneBefore, 9, '*').concat(encryptPhoneAfter);
            messageService.pushMessage(new Messages(null, param.getParentId()
                    , "恭喜您成功邀请会员" + encryptPhone +"注册，静等收益到账哟~", 0, new Date()));
        }

        return true;
    }
}
