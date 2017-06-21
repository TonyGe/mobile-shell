package com.dianping.mobile.framework.io;

import com.dianping.mobile.framework.exception.ApplicationDecryptException;
import com.dianping.mobile.framework.exception.ApplicationEncryptException;


public interface IEncryptor {

    byte[] encrypt(byte[] bytes) throws ApplicationEncryptException;

    byte[] decrypt(byte[] bytes) throws ApplicationDecryptException;
}
