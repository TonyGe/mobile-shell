/**
 *
 */
package com.dianping.mobile.framework.io;

import com.dianping.mobile.framework.exception.ApplicationEncryptException;
import com.dianping.mobile.framework.exception.ApplicationIOException;
import org.apache.log4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * @author kewen.yao
 */
public class MobileIOUtil {

    private static final Logger log = Logger.getLogger(MobileIOUtil.class);


    private MobileIOUtil() {
    }

    // response gzip compress
    public static byte[] compress(byte[] source) throws ApplicationIOException {
        try {
            byte[] result = null;
            ByteArrayOutputStream bos = new ByteArrayOutputStream();// TODO:
            // initial
            // size base
            // on
            // request
            GZIPOutputStream gzip = new GZIPOutputStream(bos);
            gzip.write(source);
            gzip.finish();
            gzip.close();
            result = bos.toByteArray();
            bos.close();
            return result;
        } catch (IOException e) {
            log.error("compress ioexception", e);
            throw new ApplicationIOException("compress ioexception", e);
        }
    }

    public static byte[] uncompress(byte[] source) {
        if (source == null || source.length == 0) {
            return null;
        }

        try {
            byte[] result = null;
            ByteArrayOutputStream bos = new ByteArrayOutputStream();

            ByteArrayInputStream bis = new ByteArrayInputStream(source);
            GZIPInputStream gzip = new GZIPInputStream(bis);

            byte[] buffer = new byte[1024];// need to check the common size
            // of the request
            int bytesRead = -1;

            while (gzip.available() == 1) {
                bytesRead = gzip.read(buffer);
                if (bytesRead > 0) {
                    bos.write(buffer, 0, bytesRead);
                }
            }

            //gzip.close();
            result = bos.toByteArray();
            //bos.close();
            return result;
        } catch (IOException e) {
            log.error("compress ioexception", e);
        }
        return null;
    }

    public static void main(String[] args) throws ApplicationIOException, UnsupportedEncodingException, ApplicationEncryptException, Exception {
        NoPaddingEncryptor d = new NoPaddingEncryptor();
        byte[] source = "applogjson=[ {} ]".getBytes("UTF-8");
        byte[] sss = MobileIOUtil.compress(source);
        byte[] ttt = d.encrypt(sss);
        System.out.println(source.length);
        System.out.println(sss.length);
        System.out.println(ttt.length);

        byte[] hhh = d.decrypt(ttt);
        byte[] ppp = MobileIOUtil.uncompress(hhh);
        System.out.println((int) new Character('\0') == 0);
    }
}
