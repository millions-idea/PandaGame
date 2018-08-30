/***
 * @pName management
 * @name IMessageService
 * @user HongWei
 * @date 2018/8/29
 * @desc
 */
package com.panda.game.management.biz;

import com.panda.game.management.entity.db.Messages;

import java.util.List;

public interface IMessageService {
    /**
     * 推送消息 韦德 2018年8月29日22:59:22
     * @param messages
     * @return
     */
    Boolean pushMessage(Messages messages);

    /**
     * 推送消息
     * @param messagesList
     * @return
     */
    Boolean pushMessage(List<Messages> messagesList);

    /**
     * 查询短消息
     * @param token
     * @return
     */
    List<Messages> getMessageList(String token);

    /**
     * 批量查阅 韦德 2018年8月30日18:27:43
     * @param list
     */
    void batchMarkRead(List<Messages> list);

}
