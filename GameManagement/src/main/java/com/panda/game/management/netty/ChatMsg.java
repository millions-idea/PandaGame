package com.panda.game.management.netty;

import com.panda.game.management.entity.db.GameMemberGroup;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ChatMsg implements Serializable {

	private static final long serialVersionUID = 3611169682695799175L;
	
	private String senderId;		// 发送者的用户id
	private String roomCode;		// 房间id
	private List<GameMemberGroup> receiveList;		// 接受者的用户id集合
	private String msg;				// 聊天内容
	private String msgId;			// 用于消息的签收

}
