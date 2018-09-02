/***
 * @pName management
 * @name WithdrawServiceImpl
 * @user HongWei
 * @date 2018/8/21
 * @desc
 */
package com.panda.game.management.biz.impl;

import com.panda.game.management.biz.IPayService;
import com.panda.game.management.biz.IWithdrawService;
import com.panda.game.management.entity.JsonResult;
import com.panda.game.management.entity.db.GameMemberGroup;
import com.panda.game.management.entity.db.Withdraw;
import com.panda.game.management.entity.dbExt.UserDetailInfo;
import com.panda.game.management.entity.dbExt.WithdrawDetailInfo;
import com.panda.game.management.entity.resp.UserResp;
import com.panda.game.management.exception.MsgException;
import com.panda.game.management.repository.*;
import com.panda.game.management.repository.utils.ConditionUtil;
import com.panda.game.management.utils.PropertyUtil;
import com.panda.game.management.utils.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class WithdrawServiceImpl extends BaseServiceImpl<Withdraw> implements IWithdrawService {
    private final WithdrawMapper withdrawMapper;
    private final UserMapper userMapper;
    private final PayMapper payMapper;
    private final GameMemberGroupMapper gameMemberGroupMapper;
    private final GameRoomMapper gameRoomMapper;

    @Autowired
    public WithdrawServiceImpl(WithdrawMapper withdrawMapper, UserMapper userMapper, PayMapper payMapper, GameMemberGroupMapper gameMemberGroupMapper, GameRoomMapper gameRoomMapper) {
        this.withdrawMapper = withdrawMapper;
        this.userMapper = userMapper;
        this.payMapper = payMapper;
        this.gameMemberGroupMapper = gameMemberGroupMapper;
        this.gameRoomMapper = gameRoomMapper;
    }


    /**
     * 申请提现 韦德 2018年8月21日10:57:26
     *
     * @param token
     * @param amount
     * @param systemRecordId
     */
    @Override
    @Transactional
    public void addWithdraw(String token, Double withdrawAmount, Double amount, Long systemRecordId) {
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

        if(withdrawAmount <= 0 || (amount > withdrawAmount)) throw new MsgException("余额不足");

        // 查询账户资金是否被冻结
        List<GameMemberGroup> memberGroupList = gameMemberGroupMapper.selectGoodRoomList(userInfo.getUserId());
        List<GameMemberGroup> gameMemberGroupList =  gameMemberGroupMapper.selectByUid(userInfo.getUserId());
        if((memberGroupList != null && memberGroupList.size() > 0)
               || (gameMemberGroupList != null && gameMemberGroupList.size() > 0)) throw new MsgException("您有未结算的游戏，暂时不可以提现！");

        Withdraw withdraw = new Withdraw();
        withdraw.setUserId(Integer.valueOf(userId));
        withdraw.setAmount(amount);
        withdraw.setState(0);
        withdraw.setAddTime(new Date());
        withdraw.setSystemRecordId(systemRecordId);
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
        return withdrawMapper.selectByPrimaryKey(withdraw.getWithdrawId());
    }

    /**
     * 分页加载 韦德 2018年8月30日11:29:00
     *
     * @param page
     * @param limit
     * @param condition
     * @param state
     * @param beginTime
     * @param endTime
     * @return
     */
    @Override
    public List<WithdrawDetailInfo> getWithdrawLimit(Integer page, String limit, String condition, Integer state, String beginTime, String endTime) {
        // 计算分页位置
        page = ConditionUtil.extractPageIndex(page, limit);
        String where = extractLimitWhere(condition, state, beginTime, endTime);
        List<WithdrawDetailInfo> list = withdrawMapper.selectLimit(page, limit, state, beginTime, endTime, where);
        return list;
    }

    /**
     * 加载总记录数 韦德 2018年8月30日11:29:11
     *
     * @return
     */
    @Override
    public Integer getWithdrawCount() {
        return withdrawMapper.selectCount(new Withdraw());
    }

    /**
     * 加载分页记录数 韦德 2018年8月30日11:29:22
     *
     * @param condition
     * @param state
     * @param beginTime
     * @param endTime
     * @return
     */
    @Override
    public Integer getWithdrawLimitCount(String condition, Integer state, String beginTime, String endTime) {
        String where = extractLimitWhere(condition, state, beginTime, endTime);
        return withdrawMapper.selectLimitCount(state, beginTime, endTime, where);
    }

    /**
     * 拒绝审批 韦德 2018年8月30日15:18:45
     *
     * @param withdraw
     */
    @Override
    public boolean pass(Withdraw withdraw) {
        Withdraw result = withdrawMapper.selectByPrimaryKey(withdraw.getWithdrawId());
        if(result == null) throw new MsgException("审批请求不存在");
        result.setState(2);
        result.setUpdateTime(new Date());
        result.setRemark(withdraw.getRemark());
        withdraw.setUserId(result.getUserId());
        return withdrawMapper.updateByPrimaryKey(result) > 0;
    }

    /**
     * 提取分页条件
     * @return
     */
    private String extractLimitWhere(String condition, Integer state,  String beginTime, String endTime) {
        // 查询模糊条件
        String where = " 1=1";
        if(condition != null) {
            condition = condition.trim();
            where += " AND (" + ConditionUtil.like("withdraw_id", condition, true, "t1");
            if (condition.split("-").length == 2){
                where += " OR " + ConditionUtil.like("add_time", condition, true, "t1");
                where += " OR " + ConditionUtil.like("update_time", condition, true, "t1");
            }
            where += " OR " + ConditionUtil.like("phone", condition, true, "t2");
            where += " OR " + ConditionUtil.like("system_record_id", condition, true, "t1");
            where += " OR " + ConditionUtil.like("remark", condition, true, "t1") + ")";
        }

        // 查询全部数据或者只有一类数据
        where = extractQueryAllOrOne(state, where);

        // 取两个日期之间或查询指定日期
        where = extractBetweenTime(beginTime, endTime, where);
        return where.trim();
    }


    /**
     * 提取两个日期之间的条件
     * @return
     */
    private String extractBetweenTime(String beginTime, String endTime, String where) {
        if ((beginTime != null && beginTime.contains("-")) &&
                endTime != null && endTime.contains("-")){
            where += " AND t1.add_time BETWEEN #{beginTime} AND #{endTime}";
        }else if (beginTime != null && beginTime.contains("-")){
            where += " AND t1.add_time BETWEEN #{beginTime} AND #{endTime}";
        }else if (endTime != null && endTime.contains("-")){
            where += " AND t1.add_time BETWEEN #{beginTime} AND #{endTime}";
        }
        return where;
    }


    /**
     * 查询全部数据或者只有一类数据
     * @return
     */
    private String extractQueryAllOrOne(Integer state, String where) {
        if (state != null && state != 0){
            where += " AND t1.state = #{state}";
        }
        return where;
    }

    /**
     * 更新数据 韦德 2018年8月13日13:28:01
     *
     * @param param
     * @return
     */
    @Override
    public int update(Withdraw param) {
        return withdrawMapper.updateByPrimaryKey(param);
    }
}
