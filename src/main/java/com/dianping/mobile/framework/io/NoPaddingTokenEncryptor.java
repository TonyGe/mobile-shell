/**
 * 
 */
package com.dianping.mobile.framework.io;


/**
 * @author kewen.yao
 *
 */
public class NoPaddingTokenEncryptor extends NoPaddingEncryptor {
	
	private static final String TOKEN_KEY = "55C930D827BDABFD";
	private static final String TOKEN_IV = "D7C6F71A12153EE5";

	@Override
	protected String getKEY() {
		return TOKEN_KEY;
	}

	@Override
	protected String getIV() {
		return TOKEN_IV;
	}

}
