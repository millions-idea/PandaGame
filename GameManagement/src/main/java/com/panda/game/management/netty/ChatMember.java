/***
 * @pName management
 * @name ChatRoom
 * @user HongWei
 * @date 2018/8/23
 * @desc
 */
package com.panda.game.management.netty;

import com.panda.game.management.entity.db.GameMemberGroup;
import com.panda.game.management.entity.resp.ChatMemberResp;
import lombok.*;

import io.netty.channel.Channel;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ChatMember {
    private String senderId;
    private String roomCode;
    private List<GameMemberGroup> receiveList;
    private Channel channel;
}
