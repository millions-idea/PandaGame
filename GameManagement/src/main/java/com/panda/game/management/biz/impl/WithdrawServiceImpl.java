/***
 * @pName management
 * @name WithdrawServiceImpl
 * @user HongWei
 * @date 2018/8/21
 * @desc
 */
package com.panda.game.management.biz.impl;

import com.panda.game.management.biz.WithdrawService;
import com.panda.game.management.entity.JsonResult;
import com.panda.game.management.entity.db.GameMemberGroup;
import com.panda.game.management.entity.db.Withdraw;
import com.panda.game.management.entity.dbExt.UserDetailInfo;
import com.panda.game.management.entity.resp.UserResp;
import com.panda.game.management.exception.MsgException;
import com.panda.game.management.repository.GameMemberGroupMapper;
import com.panda.game.management.repository.PayMapper;
import com.panda.game.management.repository.UserMapper;
import com.panda.game.management.repository.WithdrawMapper;
import com.panda.game.management.utils.PropertyUtil;
import com.panda.game.management.utils.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class WithdrawServiceImpl extends BaseServiceImpl<Withdraw> implements WithdrawService {
    private final WithdrawMapper withdrawMapper;
    private final UserMapper userMapper;
    private final PayMapper payMapper;
    private final GameMemberGroupMapper gameMemberGroupMapper;

    @Autowired
    public WithdrawServiceImpl(WithdrawMapper withdrawMapper, UserMapper userMapper, PayMapper payMapper, GameMemberGroupMapper gameMemberGroupMapper) {
        this.withdrawMapper = withdrawMapper;
        this.userMapper = userMapper;
        this.payMapper = payMapper;
        this.gameMemberGroupMapper = gameMemberGroupMapper;
    }


    /**
     * 申请提现 韦德 2018年8月21日10:57:26
     *
     * @param token
     * @param amount
     */
    @Override
    public void addWithdraw(String token, Double amount) {
        // 加载用户信息
        Map<String, String> map = TokenUtil.validate(token);
        if(map.isEmpty()) JsonResult.failing();
        String userId = map.get("userId");
        if(userId == null || userId.isEmpty()) throw new MsgException("身份校验失败");

        // 查询基础信息
        UserDetailInfo userInfo = userMapper.selectUserDetail(userId);
        UserResp userResp = new UserResp();
        PropertyUtil.clone(userInfo, userResp);
        userResp.setToken(token);

        // 查询账户资金是否被冻结
        List<GameMemberGroup> gameMemberGroupList =  gameMemberGroupMapper.selectByUid(userInfo.getUserId());
        if(gameMemberGroupList != null && gameMemberGroupList.size() > 0) throw new MsgException("您有正在参与的游戏，暂时不可以提现！");

        // 查询可提现余额
        Double withdrawAmount = payMapper.selectWithdrawAmount(Integer.valueOf(userId),  "新用户注册奖励");
        if(withdrawAmount <= 0 || (amount > withdrawAmount)) throw new MsgException("余额不足");

        Withdraw withdraw = new Withdraw();
        withdraw.setUserId(Integer.valueOf(userId));
        withdraw.setAmount(amount);
        withdraw.setState(0);
        withdraw.setAddTime(new Date());
        int count = withdrawMapper.insert(withdraw);
        if(count == 0) throw new MsgException("申请提现失败");
    }

    /**
     * 根据id查询提现信息 韦德 2018年8月21日13:39:01
     *
     * @param withdraw
     * @return
     */
    @Override
    public Withdraw getWithdrawById(Withdraw withdraw) {
        return withdrawMapper.selectOne(withdraw);
    }
}
