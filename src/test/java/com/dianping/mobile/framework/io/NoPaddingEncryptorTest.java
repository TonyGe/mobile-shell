package com.dianping.mobile.framework.io;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.dianping.mobile.framework.exception.ApplicationDecryptException;
import com.dianping.mobile.framework.exception.ApplicationEncryptException;


public class NoPaddingEncryptorTest {
	
	private static final String TEST_STRING = "test string";
	
	@Test
	public void testDecryptEncrypt() throws ApplicationEncryptException, ApplicationDecryptException {
		byte[] src = TEST_STRING.getBytes();
		IEncryptor encryptor = new NoPaddingEncryptor();
		byte[] dst = encryptor.encrypt(src);
		assertNotNull(src);
		assertNotNull(dst);
		byte[] result = encryptor.decrypt(dst);
		assertArrayEquals(src, result);
	}

}
