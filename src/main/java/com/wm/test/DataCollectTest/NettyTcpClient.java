package com.wm.test.DataCollectTest;

import com.alibaba.fastjson.JSONObject;
import com.wm.constant.MessageConstant;
import com.wm.dto.MessageDto;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * Created by peekaboo on 2016/3/21.
 */
public class NettyTcpClient extends ChannelInboundHandlerAdapter {

	private Object obj = new Object();

	private Channel ch;

	private static final EventLoopGroup loopGroup = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors());;

	private MessageDto messageDto;

	public void connect(String ipAddr, int port) {
		Bootstrap bootstrap = new Bootstrap();
		bootstrap.group(loopGroup).channel(NioSocketChannel.class).handler(new ChannelInitializer<Channel>() {
			@Override
			protected void initChannel(Channel ch) throws Exception {
				ChannelPipeline pipeline = ch.pipeline();
				pipeline.addLast(new MessageEncoder());
				pipeline.addLast(new MessageDecoder());
				pipeline.addLast("handler", NettyTcpClient.this);
			}
		});
		bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
		bootstrap.option(ChannelOption.TCP_NODELAY, true);
		bootstrap.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
		bootstrap.option(ChannelOption.SO_SNDBUF, 64 * 1024);
		bootstrap.option(ChannelOption.SO_RCVBUF, 32 * 1024);
		try {
			ch = bootstrap.connect(ipAddr, port).sync().channel();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public MessageDto sendMessage(MessageDto msg) {
		ch.writeAndFlush(msg, ch.voidPromise());

		try {
			synchronized (obj) {
				obj.wait(6000);
			}
		} catch (InterruptedException e) {
			return null;
		}

		return messageDto;
	}

	public void close() {
		ch.close();
	}

	public static void main(String[] args) throws InterruptedException {
		JSONObject temp = new JSONObject();
		temp.put("lon", "123.1");
		temp.put("lat", "87.2");
		temp.put("systime", System.currentTimeMillis());
		temp.put("gpstime", System.currentTimeMillis());
		temp.put("userID", 1);
		temp.put("trackID", 1);
		MessageDto messageDto = new MessageDto();
		messageDto.setMessageType(MessageConstant.MessageType.MESSAGE.getMsgCode());
		messageDto.setMessage(temp.toJSONString());

		NettyTcpClient cli = new NettyTcpClient();
		cli.connect("10.5.10.221", 9000);

		messageDto = cli.sendMessage(messageDto);

		Thread.sleep(10000);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		messageDto = (MessageDto) msg;
		synchronized (obj) {
			obj.notifyAll();
		}
	}
}
