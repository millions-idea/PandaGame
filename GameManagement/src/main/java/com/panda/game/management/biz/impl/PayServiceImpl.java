/***
 * @pName management
 * @name PayServiceImpl
 * @user HongWei
 * @date 2018/8/16
 * @desc
 */
package com.panda.game.management.biz.impl;

import com.panda.game.management.annotaion.AspectLog;
import com.panda.game.management.biz.PayService;
import com.panda.game.management.entity.DataDictionary;
import com.panda.game.management.entity.db.Accounts;
import com.panda.game.management.entity.db.Pays;
import com.panda.game.management.entity.dbExt.UserDetailInfo;
import com.panda.game.management.entity.param.PayParam;
import com.panda.game.management.exception.InfoException;
import com.panda.game.management.repository.*;
import com.panda.game.management.utils.IdWorker;
import com.panda.game.management.utils.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class PayServiceImpl extends BaseServiceImpl<Pays> implements PayService {
    private final PayMapper payMapper;
    private final WalletMapper walletMapper;
    private final AccountMapper accountMapper;
    private final DictionaryMapper dictionaryMapper;
    private final UserMapper userMapper;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public PayServiceImpl(PayMapper payMapper, WalletMapper walletMapper, AccountMapper accountMapper, DictionaryMapper dictionaryMapper, UserMapper userMapper) {
        this.payMapper = payMapper;
        this.walletMapper = walletMapper;
        this.accountMapper = accountMapper;
        this.dictionaryMapper = dictionaryMapper;
        this.userMapper = userMapper;
    }


    /**
     * 转账 韦德 2018年8月16日13:17:17
     *
     * @param payParam
     */
    @Override
    @Transactional
    @AspectLog(description = "财务交易转账")
    public void transfer(PayParam payParam) {
        // 查询交易主体信息
        List<UserDetailInfo> userDetailInfoList = this.getUsersInfo(payParam);
        UserDetailInfo fromUserInfo = userDetailInfoList.stream().filter(u -> u.getUserId().equals(payParam.getFromUid())).findFirst().get();
        UserDetailInfo toUserInfo =userDetailInfoList.stream().filter(u -> u.getUserId().equals(payParam.getToUid())).findFirst().get();

        if(fromUserInfo.getBalance() <= 0) throw new InfoException("甲方钱包余额不足");
        if(toUserInfo.getBalance() <= 0) throw new InfoException("甲方钱包余额不足");

        // 生成交易流水账单
        Pays pays = null;
        try {
            pays = extractPayModel(userDetailInfoList, payParam);
        } catch (Exception e) {
            logger.error("生成交易流水异常：" + JsonUtil.getJson(e));
            throw new InfoException("生成交易流水异常");
        }
        int count = payMapper.insert(pays);
        if(count == 0) throw new InfoException("生成交易流水失败");


        // 变更钱包余额
        count = 0;
        count = walletMapper.reduceBalance(fromUserInfo.getUserId(), payParam.getAmount(),fromUserInfo.getVersion());
        if(count == 0) throw new InfoException("甲方账户扣款失败");

        count = 0;
        count = walletMapper.addBalance(toUserInfo.getUserId(), payParam.getAmount(), toUserInfo.getVersion());
        if(count == 0) throw new InfoException("乙方账户加款失败");


        // 生成往来账目单
        List<Accounts> accountsList = new ArrayList<>();
        PayParam newPayParam = payParam;
        try {
            accountsList.add(extractAccountModel(payParam, fromUserInfo, 2));
            newPayParam.setFromUid(payParam.getToUid());
            accountsList.add(extractAccountModel(payParam, toUserInfo, 1));
        } catch (Exception e) {
            logger.error("生成往来账异常：" + JsonUtil.getJson(e));
            throw new InfoException("生成往来账异常");
        }
        count = 0;
        count = accountMapper.insertList(accountsList);
        if(count == 0) throw new InfoException("生成往来账失败");

        System.out.println("交易成功");
    }

    /**
     * 查询交易主体信息 韦德 2018年8月16日13:33:09
     * @param payParam
     * @return [0]=fromUser [1]=toUser
     */
    private List<UserDetailInfo> getUsersInfo(PayParam payParam){
        String userId = payParam.getFromUid().toString().concat(",").concat(payParam.getToUid().toString());
        List<UserDetailInfo> users = userMapper.selectInUid(userId);
        if(users.isEmpty() || users.size() != 2) throw new InfoException("查询交易主体账户信息不完整");
        return users;
    }

    private Pays extractPayModel(List<UserDetailInfo> userDetailInfoList, PayParam payParam) throws Exception {
        Pays pays = new Pays();
        pays.setPayId(IdWorker.getFlowIdWorkerInstance().nextId());
        pays.setFromUid(payParam.getFromUid());
        pays.setFromName(userDetailInfoList.get(0).getPhone());
        pays.setToUid(payParam.getToUid());
        pays.setToName(userDetailInfoList.get(1).getPhone());

        pays.setChannelType(DataDictionary.DATA_DICTIONARY.get("finance.pays.channel.internal").getDictionaryId());
        pays.setChannelName(DataDictionary.DATA_DICTIONARY.get("finance.pays.channel.internal").getValue());

        pays.setProductType(DataDictionary.DATA_DICTIONARY.get("finance.pays.product.currency").getDictionaryId());
        pays.setProductName(DataDictionary.DATA_DICTIONARY.get("finance.pays.product.currency").getValue());

        pays.setTradeType(DataDictionary.DATA_DICTIONARY.get("finance.pays.trade.recharge").getDictionaryId());
        pays.setTradeName(DataDictionary.DATA_DICTIONARY.get("finance.pays.trade.recharge").getValue());

        pays.setAddTime(new Date());
        pays.setAmount(payParam.getAmount());

        payParam.setSystemRecordId(IdWorker.getFlowIdWorkerInstance().nextId());

        pays.setSystemRecordId(payParam.getSystemRecordId());
        pays.setRemark(payParam.getRemark());

        pays.setStatus(0);
        return pays;
    }


    private Accounts extractAccountModel(PayParam payParam, UserDetailInfo user, Integer accountsType) throws Exception {
        Accounts accounts = new Accounts();
        accounts.setAccountsId(IdWorker.getFlowIdWorkerInstance().nextId());
        accounts.setPayId(payParam.getSystemRecordId());
        accounts.setTradeAccountId(payParam.getFromUid());
        accounts.setTradeAccountName(user.getPhone());
        accounts.setAccountsType(accountsType);
        accounts.setAmount(payParam.getAmount());
        return accounts;
    }

}
