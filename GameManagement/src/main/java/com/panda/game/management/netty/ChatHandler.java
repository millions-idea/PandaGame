package com.panda.game.management.netty;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.panda.game.management.biz.GameMemberGroupService;
import com.panda.game.management.biz.impl.GameMemberGroupServiceImpl;
import com.panda.game.management.entity.db.GameMemberGroup;
import com.panda.game.management.entity.resp.ChatMemberResp;
import com.panda.game.management.utils.JsonUtil;
import com.panda.game.management.utils.SpringApplicationContext;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import  com.panda.game.management.entity.enums.*;

/**
 * 
 * @Description: 处理消息的handler
 * TextWebSocketFrame： 在netty中，是用于为websocket专门处理文本的对象，frame是消息的载体
 */
public class ChatHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

	// 用于记录和管理所有客户端的channle
	private static ChannelGroup users =
			new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) 
			throws Exception {
		// 获取客户端传输过来的消息
		String content = msg.text();

		List<ChatMember> chatMembers = new ArrayList<>();
		Channel currentChannel = ctx.channel();

		// 1. 获取客户端发来的消息
		DataContent dataContent = JsonUtil.getModel(content, DataContent.class);
		Integer action = dataContent.getAction();
		// 2. 判断消息类型，根据不同的类型来处理不同的业务

		if (action == MsgActionEnum.CONNECT.type) {
			// 	2.1  当websocket 第一次open的时候，初始化channel，把用的channel和userid关联起来
			String senderId = dataContent.getChatMsg().getSenderId();
			String roomCode = dataContent.getChatMsg().getRoomCode();

			// 动态查询成员信息列表
			GameMemberGroupService gameMemberGroupService = (GameMemberGroupService) SpringApplicationContext.getBean(GameMemberGroupServiceImpl.class);
			List<GameMemberGroup> memberGroupList = gameMemberGroupService.getListByRoom(Long.valueOf(roomCode));

			// 强制关闭连接并清除记录
			if(memberGroupList == null || memberGroupList.isEmpty()) {
				ctx.channel().close();
				users.remove(ctx.channel());
			}

			// 插入本地缓存列表
			GameMemberGroup gameMemberGroup = memberGroupList.stream().filter(m -> m.getRoomCode().equals(Long.valueOf(roomCode))).findFirst().get();
			if(gameMemberGroup == null){
				ctx.channel().close();
				users.remove(ctx.channel());
			}

			ChatMember chatMember = new ChatMember(senderId, roomCode, memberGroupList, currentChannel);
			chatMembers.add(chatMember);

			List<ChatMember> chatMemberList = UserChannelRel.get(roomCode);
			if(chatMemberList == null || chatMemberList.isEmpty()) {
				UserChannelRel.put(roomCode, chatMembers);
			}else{
				chatMemberList.add(chatMember);
				UserChannelRel.put(roomCode, chatMemberList);
			}

			// 打印会话频道列表
			UserChannelRel.output();
		} else if (action == MsgActionEnum.CHAT.type) {
			//  2.2  聊天类型的消息，把聊天记录保存到数据库，同时标记消息的签收状态[未签收]
			ChatMsg chatMsg = dataContent.getChatMsg();
			String msgText = chatMsg.getMsg();
			String senderId = chatMsg.getSenderId();
			String roomCode = chatMsg.getRoomCode();
			List<ChatMember> list = UserChannelRel.get(roomCode);

			List<GameMemberGroup> receiveList = list.get(0).getReceiveList();
			System.out.println(String.format("收到聊天消息，发送者ID：%s，房间：%s，接收者列表：%s，消息内容：%s", senderId, roomCode, JSON.toJSON(receiveList), msgText));

			DataContent dataContentMsg = new DataContent();
			dataContentMsg.setChatMsg(chatMsg);

			// 发送消息
			// 从全局用户Channel关系中获取接受方的channel
			list.forEach(item -> {
				System.out.println(String.format("向%s用户推送消息", item.getSenderId()));
				Channel receiverChannel = item.getChannel();
				if (receiverChannel == null) {
					// TODO channel为空代表用户离线，推送消息（JPush，个推，小米推送）
				} else {
					// 当receiverChannel不为空的时候，从ChannelGroup去查找对应的channel是否存在
					Channel findChannel = users.find(receiverChannel.id());
					if (findChannel != null) {
						// 用户在线
						receiverChannel.writeAndFlush(
								new TextWebSocketFrame(
										JsonUtil.getJson(dataContentMsg)));
					} else {
						// 用户离线 TODO 推送消息
					}
				}
			});
		} else if (action == MsgActionEnum.KEEPALIVE.type) {
			//  2.4  心跳类型的消息
			System.out.println("收到来自channel为[" + currentChannel + "]的心跳包...");
		}
		
	}

	/**
	 * 当客户端连接服务端之后（打开连接）
	 * 获取客户端的channle，并且放到ChannelGroup中去进行管理
	 */
	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		users.add(ctx.channel());
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		// 发生异常之后关闭channel并且移除
		ctx.channel().close();
		users.remove(ctx.channel());
	}

	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		// 当触发handlerRemoved，ChannelGroup会自动移除对应客户端的channel
		users.remove(ctx.channel());
		System.out.println("客户端断开，channle对应的长id为："
							+ ctx.channel().id().asLongText());
		System.out.println("客户端断开，channle对应的短id为："
							+ ctx.channel().id().asShortText());
	}

	
	
}
