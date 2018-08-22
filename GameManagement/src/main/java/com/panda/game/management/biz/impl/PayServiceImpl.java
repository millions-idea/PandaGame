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
import com.panda.game.management.entity.Constant;
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
import org.springframework.stereotype.Service;
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
    @AspectLog(description = "AB转账")
    public void transfer(PayParam payParam) {
        // 高并发环境下的重试机制
       /* long start =  System.currentTimeMillis();
        while (true){
            // 获取循环当前时间
            long end = System.currentTimeMillis();
            // 当前时间已经超过最大间隔，返回失败
            if (end - start > 3000) {
                throw new InfoException("交易超时");
            }
            try {
                // 查询交易主体信息
                List<UserDetailInfo> userDetailInfoList = this.getUsersInfo(payParam);
                UserDetailInfo fromUserInfo = userDetailInfoList.stream().filter(u -> u.getUserId().equals(payParam.getFromUid())).findFirst().get();
                UserDetailInfo toUserInfo =userDetailInfoList.stream().filter(u -> u.getUserId().equals(payParam.getToUid())).findFirst().get();

                if(fromUserInfo.getBalance() <= 0) throw new InfoException("甲方钱包余额不足");
                if(toUserInfo.getBalance() <= 0) throw new InfoException("甲方钱包余额不足");

                // 生成交易流水账单
                Pays pays = null;
                try {
                    pays = extractRechargePayModel(userDetailInfoList, payParam);
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
                break;
            }catch (InfoException e){
                logger.error(e.getMsg());
            }
        }*/


        // 查询交易主体信息
        List<UserDetailInfo> userDetailInfoList = this.getUsersInfo(payParam);
        UserDetailInfo fromUserInfo = userDetailInfoList.stream().filter(u -> u.getUserId().equals(payParam.getFromUid())).findFirst().get();
        UserDetailInfo toUserInfo =userDetailInfoList.stream().filter(u -> u.getUserId().equals(payParam.getToUid())).findFirst().get();

        if(fromUserInfo.getBalance() <= 0) throw new InfoException("甲方钱包余额不足");

        // 生成交易流水账单
        Pays pays = null;
        try {
            pays = extractTransferPayModel(userDetailInfoList, payParam);
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
        createAccounts(payParam, fromUserInfo, toUserInfo, accountsList, newPayParam);
        count = 0;
        count = accountMapper.insertList(accountsList);
        if(count == 0) throw new InfoException("生成往来账失败");

        System.out.println("交易成功");
    }

    /**
     * 消费 韦德 2018年8月16日13:17:17
     *
     * @param payParam
     */
    @Override
    @Transactional
    @AspectLog(description = "C转账")
    public void consume(PayParam payParam) {
        // 查询交易主体信息
        List<UserDetailInfo> userDetailInfoList = this.getUsersInfo(payParam);
        UserDetailInfo fromUserInfo = userDetailInfoList.stream().filter(u -> u.getUserId().equals(payParam.getFromUid())).findFirst().get();
        UserDetailInfo toUserInfo =userDetailInfoList.stream().filter(u -> u.getUserId().equals(payParam.getToUid())).findFirst().get();

        if(fromUserInfo.getBalance() <= 0) throw new InfoException("甲方钱包余额不足");

        // 生成交易流水账单
        Pays pays = null;
        try {
            pays = extractConsumePayModel(userDetailInfoList, payParam);
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
        createAccounts(payParam, fromUserInfo, toUserInfo, accountsList, newPayParam);
        count = 0;
        count = accountMapper.insertList(accountsList);
        if(count == 0) throw new InfoException("生成往来账失败");

        System.out.println("交易成功");
    }

    /**
     * 批量转账 韦德 2018年8月21日01:53:25
     *
     * @param payParams
     */
    @Override
    @Transactional
    @AspectLog(description = "BAB转账")
    public void batchTransfer(List<PayParam> payParams) {
       payParams.forEach(payParam -> {
           this.transfer(payParam);
       });
    }

    /**
     * 批量消费 韦德 2018年8月21日01:53:25
     *
     * @param payParams
     */
    @Override
    @Transactional
    @AspectLog(description = "BC转账")
    public void batchConsume(List<PayParam> payParams) {
        payParams.forEach(payParam -> {
            this.consume(payParam);
        });
    }

    /**
     * 充值 韦德 2018年8月16日13:17:17
     *
     * @param payParam
     */
    @Override
    @Transactional
    @AspectLog(description = "R转账")
    public void recharge(PayParam payParam) {
        // 查询交易主体信息
        List<UserDetailInfo> userDetailInfoList = this.getUsersInfo(payParam);
        UserDetailInfo fromUserInfo = userDetailInfoList.stream().filter(u -> u.getUserId().equals(payParam.getFromUid())).findFirst().get();
        UserDetailInfo toUserInfo =userDetailInfoList.stream().filter(u -> u.getUserId().equals(payParam.getToUid())).findFirst().get();

        if(fromUserInfo.getBalance() <= 0) throw new InfoException("甲方钱包余额不足");

        // 生成交易流水账单
        Pays pays = null;
        try {
            pays = extractRechargePayModel(userDetailInfoList, payParam);
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
        createAccounts(payParam, fromUserInfo, toUserInfo, accountsList, newPayParam);
        count = 0;
        count = accountMapper.insertList(accountsList);
        if(count == 0) throw new InfoException("生成往来账失败");

        System.out.println("交易成功");
    }

    /**
     * 获取不可用余额 韦德 2018年8月21日15:50:04
     *
     * @param userId
     * @return
     */
    @Override
    public Double getNotWithdrawAmount(Integer userId) {
        return payMapper.selectNotWithdrawAmount(userId, "新用户注册奖励");
    }

    /**
     * 获取可用余额 韦德 2018年8月21日15:50:04
     *
     * @param userId
     * @return
     */
    @Override
    public Double getWithdrawAmount(Integer userId) {
        return payMapper.selectWithdrawAmount(userId, "新用户注册奖励");
    }

    /**
     * 提现 韦德 2018年8月21日13:42:56
     *
     * @param payParam
     */
    @Override
    @Transactional
    @AspectLog(description = "W转账")
    public void withdraw(PayParam payParam) {
        payParam.setToUid(Constant.SYSTEM_ACCOUNTS_ID);

        // 查询交易主体信息
        List<UserDetailInfo> userDetailInfoList = this.getUsersInfo(payParam);
        UserDetailInfo fromUserInfo = userDetailInfoList.stream().filter(u -> u.getUserId().equals(payParam.getFromUid())).findFirst().get();
        UserDetailInfo toUserInfo =userDetailInfoList.stream().filter(u -> u.getUserId().equals(payParam.getToUid())).findFirst().get();

        if(fromUserInfo.getBalance() <= 0) throw new InfoException("甲方钱包余额不足");

        // 生成交易流水账单
        Pays pays = null;
        try {
            pays = extractWithdrawPayModel(userDetailInfoList, payParam);
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
        createAccounts(payParam, fromUserInfo, toUserInfo, accountsList, newPayParam);
        count = 0;
        count = accountMapper.insertList(accountsList);
        if(count == 0) throw new InfoException("生成往来账失败");

        System.out.println("交易成功");
    }

    /**
     * 设置往来账
     * @param payParam
     * @param fromUserInfo
     * @param toUserInfo
     * @param accountsList
     * @param newPayParam
     */
    private void createAccounts(PayParam payParam, UserDetailInfo fromUserInfo, UserDetailInfo toUserInfo, List<Accounts> accountsList, PayParam newPayParam) {
        try {
            accountsList.add(extractAccountModel(payParam, fromUserInfo, 2));
            newPayParam.setFromUid(payParam.getToUid());
            accountsList.add(extractAccountModel(payParam, toUserInfo, 1));
        } catch (Exception e) {
            logger.error("生成往来账异常：" + JsonUtil.getJson(e));
            throw new InfoException("生成往来账异常");
        }
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

    /**
     * 生成转账类型model
     * @param userDetailInfoList
     * @param payParam
     * @return
     * @throws Exception
     */
    private Pays extractTransferPayModel(List<UserDetailInfo> userDetailInfoList, PayParam payParam) throws Exception {
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

        pays.setTradeType(DataDictionary.DATA_DICTIONARY.get("finance.pays.trade.deduction").getDictionaryId());
        pays.setTradeName(DataDictionary.DATA_DICTIONARY.get("finance.pays.trade.deduction").getValue());

        pays.setAddTime(new Date());
        pays.setAmount(payParam.getAmount());

        payParam.setSystemRecordId(IdWorker.getFlowIdWorkerInstance().nextId());

        pays.setSystemRecordId(payParam.getSystemRecordId());
        pays.setRemark(payParam.getRemark());

        pays.setStatus(0);
        return pays;
    }

    /**
     * 生成充值类型model
     * @param userDetailInfoList
     * @param payParam
     * @return
     * @throws Exception
     */
    private Pays extractRechargePayModel(List<UserDetailInfo> userDetailInfoList, PayParam payParam) throws Exception {
        Pays pays = new Pays();
        pays.setPayId(IdWorker.getFlowIdWorkerInstance().nextId());
        pays.setFromUid(payParam.getFromUid());
        pays.setFromName(userDetailInfoList.get(0).getPhone());
        pays.setToUid(payParam.getToUid());
        pays.setToName(userDetailInfoList.get(1).getPhone());


        if(pays.getFromUid().equals(Constant.SYSTEM_ACCOUNTS_ID)){
            pays.setChannelType(DataDictionary.DATA_DICTIONARY.get("finance.pays.channel.internal").getDictionaryId());
            pays.setChannelName(DataDictionary.DATA_DICTIONARY.get("finance.pays.channel.internal").getValue());
        }else{
            pays.setChannelType(DataDictionary.DATA_DICTIONARY.get("finance.pays.channel.alipay").getDictionaryId());
            pays.setChannelName(DataDictionary.DATA_DICTIONARY.get("finance.pays.channel.alipay").getValue());
        }


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

    /**
     * 生成提现类型model
     * @param userDetailInfoList
     * @param payParam
     * @return
     * @throws Exception
     */
    private Pays extractWithdrawPayModel(List<UserDetailInfo> userDetailInfoList, PayParam payParam) throws Exception {
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

        pays.setTradeType(DataDictionary.DATA_DICTIONARY.get("finance.pays.trade.withdraw").getDictionaryId());
        pays.setTradeName(DataDictionary.DATA_DICTIONARY.get("finance.pays.trade.withdraw").getValue());

        pays.setAddTime(new Date());
        pays.setAmount(payParam.getAmount());

        payParam.setSystemRecordId(IdWorker.getFlowIdWorkerInstance().nextId());

        pays.setSystemRecordId(payParam.getSystemRecordId());
        pays.setRemark(payParam.getRemark());

        pays.setStatus(0);
        return pays;
    }

    /**
     * 生成消费类型model
     * @param userDetailInfoList
     * @param payParam
     * @return
     * @throws Exception
     */
    private Pays extractConsumePayModel(List<UserDetailInfo> userDetailInfoList, PayParam payParam) throws Exception {
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

        pays.setTradeType(DataDictionary.DATA_DICTIONARY.get("finance.pays.trade.consume").getDictionaryId());
        pays.setTradeName(DataDictionary.DATA_DICTIONARY.get("finance.pays.trade.consume").getValue());

        pays.setAddTime(new Date());
        pays.setAmount(payParam.getAmount());

        payParam.setSystemRecordId(IdWorker.getFlowIdWorkerInstance().nextId());

        pays.setSystemRecordId(payParam.getSystemRecordId());
        pays.setRemark(payParam.getRemark());

        pays.setStatus(0);
        return pays;
    }

    /**
     * 生成往来账model
     * @param payParam
     * @param user
     * @param accountsType
     * @return
     * @throws Exception
     */
    private Accounts extractAccountModel(PayParam payParam, UserDetailInfo user, Integer accountsType) throws Exception {
        Accounts accounts = new Accounts();
        accounts.setAccountsId(IdWorker.getFlowIdWorkerInstance().nextId());
        accounts.setPayId(payParam.getSystemRecordId());
        accounts.setTradeAccountId(payParam.getFromUid());
        accounts.setTradeAccountName(user.getPhone());
        accounts.setAccountsType(accountsType);
        accounts.setAmount(payParam.getAmount());
        accounts.setRemark(payParam.getRemark());
        accounts.setCurrency(payParam.getCurrency());
        return accounts;
    }

}
