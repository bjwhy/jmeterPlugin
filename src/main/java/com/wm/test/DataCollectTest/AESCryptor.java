package com.wm.test.DataCollectTest;

import java.nio.charset.Charset;
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class AESCryptor {
	private final Charset CHAR_SET = Charset.forName("utf-8");

	private static AESCryptor instance = new AESCryptor();

	private final String ALGORITHM = "AES/ECB/PKCS7Padding";
	private final String key = "JKLJKLWjh2389JopFWE8";
	private final int KEY_BIT_SIZE = 128;

	private ThreadLocal<Cipher> cipherEN = new ThreadLocal<Cipher>() {

		@Override
		protected Cipher initialValue() {
			try {
				Cipher cipher = Cipher.getInstance(ALGORITHM);
				cipher.init(Cipher.ENCRYPT_MODE, initKey(key));
				return cipher;
			} catch (Exception e) {
				return null;
			}
		}
	};

	private ThreadLocal<Cipher> cipherDE = new ThreadLocal<Cipher>() {

		@Override
		protected Cipher initialValue() {
			try {
				Cipher cipher = Cipher.getInstance(ALGORITHM);
				cipher.init(Cipher.DECRYPT_MODE, initKey(key));
				return cipher;
			} catch (Exception e) {
				return null;
			}
		}
	};

	private AESCryptor() {
		Security.addProvider(new BouncyCastleProvider());
	}

	/**
	 * 加密字符串。
	 * 
	 * @param target
	 *            原始字符串
	 * @return 加密结果字符串
	 */
	public String encrypt(String target) {
		if (target == null) {
			target = "";
		}

		Cipher cipher = null;
		try {
			cipher = cipherEN.get();
			byte[] encryptResult = cipher.doFinal(target.getBytes(CHAR_SET));
			// 兼容安卓环境的1.2codec
			String unsafeStr = new String(Base64.encodeBase64(encryptResult, false), CHAR_SET);
			return unsafeStr;
		} catch (Exception e) {
			cipherEN.remove();
			throw new RuntimeException("敏感数据加密错误", e);

		}
	}

	/**
	 * 解密字符串。
	 * 
	 * @param target
	 *            加密结果字符串
	 * @return 原始字符串
	 */
	public String decrypt(String target) {
		if (target == null) {
			target = "";
		}

		Cipher cipher = null;
		try {
			cipher = cipherDE.get();
			byte[] decryptResult = cipher.doFinal(Base64.decodeBase64(target.getBytes(CHAR_SET)));
			return new String(decryptResult, CHAR_SET);
		} catch (Exception e) {
			cipherDE.remove();
			throw new RuntimeException("敏感数据解密错误", e);

		}
	}

	/**
	 * 生成密钥字节数组，原始密钥字符串不足128位，补填0.
	 * 
	 * @param originalKey
	 * @return
	 */
	private SecretKeySpec initKey(String originalKey) {
		byte[] keys = originalKey.getBytes(CHAR_SET);

		byte[] bytes = new byte[KEY_BIT_SIZE / 8];
		for (int i = 0; i < bytes.length; i++) {
			if (keys.length > i) {
				bytes[i] = keys[i];
			} else {
				bytes[i] = 0;
			}
		}

		return new SecretKeySpec(bytes, "AES");
	}

	public static AESCryptor getInstance() {
		return instance;
	}
}
