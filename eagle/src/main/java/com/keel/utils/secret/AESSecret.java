package com.keel.utils.secret;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.lang.StringUtils;

public final class AESSecret {
	//算法名称  
	public static final String KEY_ALGORITHM = "AES";  
	//算法名称/加密模式/填充方式  
	public static final String CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";
	
	public final String keyStr;
	
	public AESSecret(final String keyStr){
		if (StringUtils.isBlank(keyStr)) {
			throw new IllegalArgumentException(
					String
							.format("AESSecret: the key is invalid!"));
		}
		this.keyStr = keyStr;
	}
	
	public byte[] encrypt(byte[] data) throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {  
	    Key k = toKey(hexStr2ByteArr(this.keyStr));  
	    Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);  
	    cipher.init(Cipher.ENCRYPT_MODE, k);  
	    return cipher.doFinal(data);
	}

	public byte[] decrypt(byte[] data) throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {  
	    Key k = toKey(hexStr2ByteArr(this.keyStr));  
	    Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);  
	    cipher.init(Cipher.DECRYPT_MODE, k);  
	    return cipher.doFinal(data);
	}
	
	private Key toKey(byte[] key) throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException {  
	    SecretKey secretKey = new SecretKeySpec(key, KEY_ALGORITHM);  
	    return secretKey;  
	}
	
	public String encrypt(String data) throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
		return AESSecret.byteArr2HexStr(this.encrypt(data.getBytes()));
	}
	
	public String decrypt(String data) throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
		return new String(this.decrypt(AESSecret.hexStr2ByteArr(data)));
	}

	/*----------------------- static ------------------------*/
	public static byte[] fromKey() throws NoSuchAlgorithmException{
		KeyGenerator keyGenerator = KeyGenerator.getInstance(KEY_ALGORITHM);  
		keyGenerator.init(128);  
		SecretKey secretKey = keyGenerator.generateKey();  
		return secretKey.getEncoded();  
	}
	
	public static String byteArr2HexStr(byte[] arrB) {
		int iLen = arrB.length;
		// 每个byte用两个字符才能表示，所以字符串的长度是数组长度的两倍
		StringBuffer sb = new StringBuffer(iLen * 2);
		for (int i = 0; i < iLen; i++) {
			int intTmp = arrB[i];
			// 把负数转换为正数
			while (intTmp < 0) {
				intTmp = intTmp + 256;
			}
			// 小于0F的数需要在前面补0
			if (intTmp < 16) {
				sb.append("0");
			}
			sb.append(Integer.toString(intTmp, 16));
		}
		return sb.toString();
	}

	public static byte[] hexStr2ByteArr(String strIn) {
		byte[] arrB = strIn.getBytes();
		int iLen = arrB.length;

		// 两个字符表示一个字节，所以字节数组长度是字符串长度除以2
		byte[] arrOut = new byte[iLen / 2];
		for (int i = 0; i < iLen; i = i + 2) {
			String strTmp = new String(arrB, i, 2);
			arrOut[i / 2] = (byte) Integer.parseInt(strTmp, 16);
		}
		return arrOut;
	}
	
	public static void printBytes(byte[] key){
		for(byte keyByte : key){
			System.out.print(Byte.toString(keyByte) + " ");
		}
	}

	public static void main(String[] argv) throws NoSuchAlgorithmException, InvalidKeyException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException{
		//生成密钥串
		System.out.println(AESSecret.byteArr2HexStr((AESSecret.fromKey())));
	}
}
