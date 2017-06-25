package com.wm.test.DataCollectTest;

import java.io.IOException;
import java.util.List;
import java.util.zip.DataFormatException;

import com.wm.dto.MessageDto;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.util.ReferenceCountUtil;

/**
 * Created by peekaboo on 2016/3/30.
 */
public class MessageDecoder extends ByteToMessageDecoder {

	private final static int type = 4;
	private final static int length = 4;

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {

		if (in.readableBytes() > (type + length)) {
			MessageDto messageDto = new MessageDto();
			in.markReaderIndex();

			messageDto.setMessageType(in.readInt());

			int dataLength = in.readInt();

			if (in.readableBytes() >= dataLength) {
				ByteBuf buf = in.readBytes(dataLength);
				String cryptStr = "";
				try {
					cryptStr = new String(buf.array(), "utf-8");

					cryptStr = ZipUtil.uncompress(AESCryptor.getInstance().decrypt(cryptStr));
					messageDto.setMessage(cryptStr);
				} catch (IOException | DataFormatException e) {
					messageDto.setMessage(e.getMessage());
					e.printStackTrace();
				}

				ReferenceCountUtil.release(buf);
				out.add(messageDto);
			} else {
				in.resetReaderIndex();
			}
		}
	}
}
