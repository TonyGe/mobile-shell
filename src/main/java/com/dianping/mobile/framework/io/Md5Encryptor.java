package com.dianping.mobile.framework.io;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.dianping.mobile.framework.exception.ApplicationDecryptException;
import com.dianping.mobile.framework.exception.ApplicationEncryptException;


public class Md5Encryptor implements IEncryptor {
	
	private static final String ALGORITHM = "MD5";
	
	
	public static String toHexString(byte[] byteArray){
		StringBuffer md5StrBuff = new StringBuffer();
		for (int i = 0; i < byteArray.length; i++) {
			String h = Integer.toHexString(0xFF & byteArray[i]);
			while (h.length() < 2) {
				h = "0" + h;
			}
			md5StrBuff.append(h);
		}
		return md5StrBuff.toString();
	}
	
	public static byte[] fromHexString(String hexString) {		
		int length = hexString.length() / 2;
		char[] hexChars = hexString.toCharArray();
		byte[] d = new byte[length];
		for (int i = 0; i < length; i++) {
			int pos = i * 2;
			d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
		}
		return d;
	}
	
	private static byte charToByte(char c) {
		if(c>='0' && c<='9') {
			return (byte)( c-'0');
		}
		else {
			return (byte)( c - 'a' + 10) ;
		}
	}
	
	@Override
	public byte[] encrypt(byte[] bytes) throws ApplicationEncryptException {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance(ALGORITHM);
			md.update(bytes);
			return md.digest();	
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
	}
	

	@Override
	public byte[] decrypt(byte[] bytes) throws ApplicationDecryptException {
		throw new ApplicationDecryptException("cannot decrypt MD5");
	}

}
