package com.dianping.mobile.framework.io;

public class Pkcs5Encryptor extends DefaultEncryptor {

    private static final String PKCS5_TRANSFORMATION = "AES/CBC/PKCS5Padding";
    private static final String ALGORITHM = "AES";
    private static final String KEY = "D7C6F71A12153EE5";
    private static final String IV = "55C930D827BDABFD";

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
        return PKCS5_TRANSFORMATION;
    }

}
