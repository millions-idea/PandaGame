/***
 * @pName management
 * @name MessageServiceImpl
 * @user HongWei
 * @date 2018/8/29
 * @desc
 */
package com.panda.game.management.biz.impl;

import com.google.common.base.Joiner;
import com.panda.game.management.annotaion.AspectLog;
import com.panda.game.management.biz.IMessageService;
import com.panda.game.management.entity.db.MerchantMessage;
import com.panda.game.management.entity.db.Messages;
import com.panda.game.management.repository.MerchantMessageMapper;
import com.panda.game.management.repository.MessageMapper;
import com.panda.game.management.utils.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MessageServiceImpl extends BaseServiceImpl<Messages> implements IMessageService {
    private final MessageMapper messageMapper;
    private final MerchantMessageMapper merchantMessageMapper;

    @Autowired
    public MessageServiceImpl(MessageMapper messageMapper, MerchantMessageMapper merchantMessageMapper) {
        this.messageMapper = messageMapper;
        this.merchantMessageMapper = merchantMessageMapper;
    }

    /**
     * 推送消息 韦德 2018年8月29日22:59:22
     *
     * @param messages
     * @return
     */
    @Override
    public Boolean pushMessage(Messages messages) {
        return messageMapper.insert(messages) > 0;
    }

    /**
     * 批量推送商家消息 韦德 2018年10月23日21:01:20
     *
     * @param merchantMessage
     * @return
     */
    @Override
    @Transactional
    @AspectLog(description = "推送商家服务消息")
    public Boolean pushMerchantMessage(List<MerchantMessage> merchantMessage) {
        return  merchantMessageMapper.insertList(merchantMessage) > 0;
    }

    /**
     * 推送消息
     *
     * @param messagesList
     * @return
     */
    @Override
    @Transactional
    public Boolean pushMessage(List<Messages> messagesList) {
        return messageMapper.insertList(messagesList) > 0;
    }

    /**
     * 查询短消息
     *
     * @param token
     * @return
     */
    @Override
    @Transactional
    public List<Messages> getMessageList(String token) {
        Map<String, String> map = TokenUtil.validate(token);
        if(map.isEmpty()) return null;

        String userId = map.get("userId");

        Messages messages = new Messages();
        messages.setUserId(Integer.valueOf(userId));
        return messageMapper.selectOrderBy(messages);
    }

    /**
     * 批量查阅 韦德 2018年8月30日18:27:43
     *
     * @param list
     */
    @Override
    public void batchMarkRead(List<Messages> list) {
        String join = Joiner.on(",").join(list.stream().map(item->item.getMessageId()).collect(Collectors.toList()));
        messageMapper.updateStatus(join, 1);
    }

}
