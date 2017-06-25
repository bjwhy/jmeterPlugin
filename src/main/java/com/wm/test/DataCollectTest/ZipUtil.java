package com.wm.test.DataCollectTest;

import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

/**
 * 文本压缩工具类 Created on 2016-05-12
 *
 * @author haiyang.wu
 */
public class ZipUtil {

	private static ThreadLocal<Deflater> compressPool = new ThreadLocal<Deflater>() {
		@Override
		protected Deflater initialValue() {
			return new Deflater(1);
		}
	};

	private static ThreadLocal<Inflater> uncompressPool = new ThreadLocal<Inflater>() {
		@Override
		protected Inflater initialValue() {
			return new Inflater();
		}
	};

	// 压缩
	public static String compress(String str) throws IOException {
		if (str == null || str.length() == 0) {
			return str;
		}

		byte[] input = str.getBytes("utf-8");
		byte[] output = new byte[input.length];
		Deflater compress = compressPool.get();
		compress.setInput(input);
		compress.finish();
		int compressedDataLength = compress.deflate(output);
		compress.reset();
		byte[] result = new byte[compressedDataLength];
		System.arraycopy(output, 0, result, 0, compressedDataLength);
		return new String(result, "ISO-8859-1");
	}

	// 解压缩
	public static String uncompress(String str) throws IOException, DataFormatException {
		if (str == null || str.length() == 0) {
			return str;
		}

		byte[] input = str.getBytes("ISO-8859-1");
		Inflater uncompress = uncompressPool.get();
		uncompress.setInput(input);

		byte[] output = new byte[input.length << 1];

		int resultLength = uncompress.inflate(output);
		uncompress.reset();

		byte[] result = new byte[resultLength];
		System.arraycopy(output, 0, result, 0, resultLength);
		return new String(result, "utf-8");
	}
}
