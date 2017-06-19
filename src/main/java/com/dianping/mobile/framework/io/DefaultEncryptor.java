package com.dianping.mobile.framework.io;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


import com.dianping.mobile.framework.exception.ApplicationDecryptException;
import com.dianping.mobile.framework.exception.ApplicationEncryptException;
import com.dianping.mobile.framework.exception.ApplicationIOException;
import com.dianping.mobile.framework.exception.ApplicationRuntimeException;


public abstract class DefaultEncryptor implements IEncryptor {

	private static final String DEFAULT_ENCODING = "UTF-8";

	@Override
	public byte[] decrypt(byte[] bytes) throws ApplicationDecryptException{
		try {
			return this.process(bytes, Cipher.DECRYPT_MODE);
		} catch (ApplicationIOException e) {
			throw new ApplicationDecryptException(e);
		}
	}

	@Override
	public byte[] encrypt(byte[] bytes) throws ApplicationEncryptException {
		try {
			return this.process(bytes, Cipher.ENCRYPT_MODE);
		} catch (ApplicationIOException e) {
			throw new ApplicationEncryptException(e);
		}
	}

	private byte[] process(byte[] bytes, int mode)
			throws ApplicationIOException {
		try {
			Cipher cipher = Cipher.getInstance(getTransformation());
			byte[] keyBytes = getKEY().getBytes(DEFAULT_ENCODING);
			byte[] ivBytes = getIV().getBytes(DEFAULT_ENCODING);
			cipher.init(mode, new SecretKeySpec(keyBytes, getAlgorithm()),
					new IvParameterSpec(ivBytes));
			return cipher.doFinal(bytes);
		} catch (IllegalBlockSizeException e) {
			throw new ApplicationIOException(
					"block size illegal for the input", e);
		} catch (BadPaddingException e) {
			throw new ApplicationIOException("badpadding for the input", e);
		} catch (NoSuchAlgorithmException e) {
			throw new ApplicationRuntimeException(String.format(
					"Algorithm %s is not supported", getAlgorithm()), e);
		} catch (NoSuchPaddingException e) {
			throw new ApplicationRuntimeException(String.format(
					"Padding %s is not supported", getTransformation()), e);
		} catch (InvalidKeyException e) {
			throw new ApplicationRuntimeException(String.format(
					"Key %s is not valid", getKEY()), e);
		} catch (InvalidAlgorithmParameterException e) {
			throw new ApplicationRuntimeException(String.format(
					"Algorithm %s is not supported", getAlgorithm()), e);
		} catch (UnsupportedEncodingException e) {
			throw new ApplicationRuntimeException(String.format(
					"encoding %s is not supported ", DEFAULT_ENCODING), e);
		}
	}

	protected abstract String getAlgorithm();

	protected abstract String getKEY();

	protected abstract String getIV();

	protected abstract String getTransformation();
}