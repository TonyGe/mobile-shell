/**
 * 
 */
package com.dianping.mobile.framework.util;

import junit.framework.Assert;

import org.junit.Test;

/**
 * @author kewen.yao
 *
 */
public class TokenUtilTest {
	
	@Test
	public void testCreateAndParse() {
		int userId = 123;
		String token = TokenUtil.createToken(userId);
		Assert.assertNotNull(token);
		Integer afterParseId = TokenUtil.parseToken(token);
		Assert.assertNotNull(afterParseId);
		Assert.assertEquals(userId, afterParseId.intValue());
	}
	
	@Test
	public void test() {
		String token = "8a96ec6ba8a66a3cfcee5876d70b2c6e670e346d0b60e9dd83fc3fc88c74952c";
		Integer afterParseId = TokenUtil.parseToken(token);
		Assert.assertEquals(6309975, afterParseId.intValue());
	}
	
}
