package com.dianping.mobile.framework.io;

import com.dianping.mobile.core.enums.ClientType;
import com.dianping.mobile.core.enums.Platform;
import com.dianping.mobile.framework.exception.ApplicationEncryptException;
import com.dianping.mobile.framework.exception.ApplicationIOException;
import com.dianping.mobile.framework.serialize.MobileResponseBinarySerializer;

import java.io.ByteArrayOutputStream;

/**
 * @author yixin.jiang
 */
public class Formatter {

    private static NoPaddingEncryptor noPaddingEncryptor = new NoPaddingEncryptor();
    private static Pkcs5Encryptor pkcs5Encryptor = new Pkcs5Encryptor();

    private Formatter() {
    }

    /**
     * the data send to client needs to be encode, compress and encrypt;
     *
     * @param data
     * @return
     * @throws ApplicationIOException
     */
    public static ResponseContent responseFormatter(Object data,
                                                    int statusCode, ClientType clientType, String v)
            throws ApplicationEncryptException, ApplicationIOException {
        ResponseContent result = new ResponseContent();
        result.setStatusCode(statusCode);
        result.setContentType(ResponseContent.CONTENTTYPE_BINARY);
        result.setContent(toBytes(data, clientType, v));
        return result;
    }

    public static ResponseContent stringFormatter(Object data, int statusCode)
            throws ApplicationEncryptException, ApplicationIOException {
        String context = "";
        if (data != null && data instanceof String)
            context = (String) data;
        ResponseContent result = new ResponseContent();
        result.setStatusCode(statusCode);
        result.setContentType(ResponseContent.CONTENTTYPE_TEXT);
        result.setContent(context.getBytes());
        return result;
    }

    private static byte[] toBytes(Object data, ClientType clientType, String v)
            throws ApplicationEncryptException, ApplicationIOException {
        byte[] encodedBytes;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        MobileResponseBinarySerializer dpbs = new MobileResponseBinarySerializer(
                baos);
        dpbs.serialize(data, clientType, v);
        encodedBytes = baos.toByteArray();
        if (clientType != null && (clientType.getPlatform() == Platform.WinPhone
                || clientType.getPlatform() == Platform.Win8Pad)) {
            byte[] compressedBytes = MobileIOUtil.compress(encodedBytes);
            return pkcs5Encryptor.encrypt(compressedBytes);

        } else {
            byte[] compressedBytes = MobileIOUtil.compress(encodedBytes);
            return noPaddingEncryptor.encrypt(compressedBytes);
        }
    }
}
