/**
 * 
 */
package com.dianping.mobile.framework.util;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;


import com.dianping.avatar.log.AvatarLogger;
import com.dianping.avatar.log.AvatarLoggerFactory;
import com.dianping.mobile.framework.exception.ApplicationDecryptException;
import com.dianping.mobile.framework.exception.ApplicationEncryptException;
import com.dianping.mobile.framework.exception.ApplicationRuntimeException;
import com.dianping.mobile.framework.io.Md5Encryptor;
import com.dianping.mobile.framework.io.NoPaddingTokenEncryptor;

/**
 * @author kewen.yao
 *
 */
public class TokenUtil {
	
	private static final String TOKEN_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	private static final String TOKEN_ENCODING = "UTF-8";
	private static NoPaddingTokenEncryptor noPaddingTokenEncryptor = new NoPaddingTokenEncryptor();
	
	private static final AvatarLogger LOG = AvatarLoggerFactory.getLogger(TokenUtil.class);
	
	private TokenUtil(){}
	
	public static int parseToken(String token) {
		if (!StringUtils.isBlank(token)) {
			try {
				byte[] bytes = Md5Encryptor.fromHexString(token);
				byte[] data = noPaddingTokenEncryptor.decrypt(bytes);
				String temp = new String(data, TOKEN_ENCODING);
				String[] user = temp.trim().split("\\|");
				if (user != null && user.length == 2) {
					return Integer.parseInt(user[0]);
				} else {
					LOG.error("parseToken format error, Str=" + temp);
				}
			} catch(ApplicationDecryptException e) { 
				LOG.error("parseToken ApplicationIOException", e);
			} catch (UnsupportedEncodingException e) {
				LOG.error("parseToken UnsupportedEncodingException", e);
			}
		}
		return 0;
	}
	
	public static String createToken(int userId) {
		if (userId > 0) {
			Date now = new Date(System.currentTimeMillis());
			SimpleDateFormat formatter = new SimpleDateFormat(TOKEN_TIME_FORMAT);
			String token = Integer.toString(userId) + "|" + formatter.format(now);
			try {
				byte[] before = token.getBytes(TOKEN_ENCODING);
				byte[] bytes = noPaddingTokenEncryptor.encrypt(before);
				return Md5Encryptor.toHexString(bytes);
			} catch(ApplicationEncryptException e) { 
				throw new ApplicationRuntimeException("createToken ApplicationIOException", e);
			} catch(UnsupportedEncodingException e) {
				throw new ApplicationRuntimeException("createToken UnsupportedEncodingException", e);
			}
		}
		return null;
	}
}
