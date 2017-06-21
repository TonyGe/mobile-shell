package com.dianping.mobile.framework.io;

import com.dianping.mobile.framework.exception.ApplicationDecryptException;
import com.dianping.mobile.framework.exception.ApplicationEncryptException;


public class NoPaddingEncryptor extends DefaultEncryptor {

    private static final String NOPADDING_TRANSFORMATION = "AES/CBC/NoPadding";
    private static final String ALGORITHM = "AES";
    private static final String KEY = "D7C6F71A12153EE5";
    private static final String IV = "55C930D827BDABFD";

    public byte[] decrypt(byte[] bytes) throws ApplicationDecryptException {
        return decrypt(bytes, false);
//		byte[] decrBuffer = super.decrypt(bytes);
//		int blank = 0;
//		for (int i = decrBuffer.length - 1; i >= 0; i--) {
//			if (decrBuffer[i] == '\0') {
//				blank++;
//			} else {
//				break;
//			}
//		}
//		byte[] result = decrBuffer;
//		if (blank > 0) {decryptdecryptdecrypt
//			byte[] decrBytes = new byte[decrBuffer.length - blank];
//			System.arraycopy(decrBuffer, 0, decrBytes, 0, decrBytes.length);
//			result = decrBytes;
//		}
//		return result;
    }

    public byte[] decrypt(byte[] bytes, boolean postCompressed) throws ApplicationDecryptException {
        byte[] decrBuffer = super.decrypt(bytes);

        // 如果body部分采用gzip压缩，会以\0\0作为结束符，所以不能再过滤\0
        // 如果未压缩，正常字符串不会有\0，所以会过滤
        if (postCompressed) {
            return decrBuffer;
        }

        int blank = 0;
        for (int i = decrBuffer.length - 1; i >= 0; i--) {
            if (decrBuffer[i] == '\0') {
                blank++;
            } else {
                break;
            }
        }
        byte[] result = decrBuffer;
        if (blank > 0) {
            byte[] decrBytes = new byte[decrBuffer.length - blank];
            System.arraycopy(decrBuffer, 0, decrBytes, 0, decrBytes.length);
            result = decrBytes;
        }
        return result;
    }

    public byte[] encrypt(byte[] bytes) throws ApplicationEncryptException {
        byte[] pbytes;
        if (bytes.length % 16 == 0) {
            pbytes = bytes;
        } else {
            pbytes = new byte[bytes.length + (16 - bytes.length % 16)];
            System.arraycopy(bytes, 0, pbytes, 0, bytes.length);
        }

        return super.encrypt(pbytes);
    }

    @Override
    protected String getAlgorithm() {
        return ALGORITHM;
    }

    @Override
    protected String getKEY() {
        return KEY;
    }

    @Override
    protected String getIV() {
        return IV;
    }

    @Override
    protected String getTransformation() {
        return NOPADDING_TRANSFORMATION;
    }
}
