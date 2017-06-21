package com.dianping.mobile.framework.io;

import com.dianping.mobile.framework.exception.ApplicationEncryptException;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class Md5EncryptorTest {

    @Test
    public void testEncrypt() throws ApplicationEncryptException {
        String expectedResult = "5eb63bbbe01eeed093cb22bb8f5acdc3";
        byte[] encryptedData = new Md5Encryptor().encrypt("hello world".getBytes());
        String encryptedString = Md5Encryptor.toHexString(encryptedData);
        assertTrue(expectedResult.equals(encryptedString));
    }

}
