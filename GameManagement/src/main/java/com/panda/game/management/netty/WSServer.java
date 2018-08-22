package com.panda.game.management.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.springframework.stereotype.Component;

@Component
public class WSServer {

	public static class WSInstance{
		static final WSServer instance = new WSServer();
	}

	public static WSServer getInstance(){
		return WSInstance.instance;
	}

	private EventLoopGroup mainGroup;
	private EventLoopGroup subGroup;
	private ServerBootstrap server;
	private ChannelFuture future;

	public WSServer() {
		mainGroup = new NioEventLoopGroup();
		subGroup = new NioEventLoopGroup();
		server = new ServerBootstrap();
		server.group(mainGroup, subGroup)
				.channel(NioServerSocketChannel.class)
				.childHandler(new WSServerInitialzer());
	}

	public void start(){
		this.future = server.bind(8088);
		System.out.println("Netty started successful!");
	}

}
