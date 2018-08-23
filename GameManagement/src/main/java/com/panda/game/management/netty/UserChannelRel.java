package com.panda.game.management.netty;

import com.alibaba.fastjson.JSON;
import io.netty.channel.Channel;

import java.util.HashMap;
import java.util.List;

/**
 * @Description: 用户id和channel的关联关系处理
 */
public class UserChannelRel {

	/**
	 * roomCode,{userId, channelObject}
	 */
	private static HashMap<String, List<ChatMember>> manager = new HashMap<>();

	public static void put(String senderId, List<ChatMember> members) {
		List<ChatMember> chatMemberList = manager.get(senderId);
		if(chatMemberList != null && !chatMemberList.isEmpty()) manager.remove(senderId);
		manager.put(senderId, members);
	}
	
	public static List<ChatMember> get(String senderId) {
		try{
			return manager.get(senderId);
		}catch (Exception e){
			System.err.println(e);
			return null;
		}
	}
	
	public static void output() {
		for (HashMap.Entry<String, List<ChatMember>> entry : manager.entrySet()) {
			System.out.println("SenderId: " + entry.getKey()
							+ ", Data: " + JSON.toJSON(entry.getValue()));
		}
	}
}
