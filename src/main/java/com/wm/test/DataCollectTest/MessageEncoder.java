package com.wm.test.DataCollectTest;

import com.wm.dto.MessageDto;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * Created by peekaboo on 2016/3/30.
 */
public class MessageEncoder extends MessageToByteEncoder<MessageDto> {
	@Override
	protected void encode(ChannelHandlerContext ctx, MessageDto msg, ByteBuf out) throws Exception {
		out.writeInt(msg.getMessageType());

		String cryptoStr = AESCryptor.getInstance().encrypt(ZipUtil.compress(msg.getMessage()));
		byte[] cryptoMsg = cryptoStr.getBytes("utf-8");

		out.writeInt(cryptoMsg.length);
		out.writeBytes(cryptoMsg);
	}
}