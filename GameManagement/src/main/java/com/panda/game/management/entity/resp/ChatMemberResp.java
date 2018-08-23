/***
 * @pName management
 * @name ChatMemberResp
 * @user HongWei
 * @date 2018/8/23
 * @desc
 */
package com.panda.game.management.entity.resp;

import com.panda.game.management.entity.db.GameMemberGroup;
import io.netty.channel.Channel;
import lombok.*;

import java.util.List;


@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ChatMemberResp {
    private GameMemberGroup gameMemberGroup;
    private Channel channel;
}
