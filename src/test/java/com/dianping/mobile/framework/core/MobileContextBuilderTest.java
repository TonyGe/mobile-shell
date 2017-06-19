package com.dianping.mobile.framework.core;

import static org.junit.Assert.assertEquals;

import java.util.regex.Pattern;

import javax.servlet.ServletContext;

import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockServletContext;

import com.dianping.mobile.base.datatypes.enums.ClientType;
import com.dianping.mobile.base.datatypes.enums.Platform;
import com.dianping.mobile.base.datatypes.enums.Product;
import com.dianping.mobile.framework.datatypes.MobileContext;
import com.dianping.mobile.framework.exception.ApplicationEncryptException;
import com.dianping.mobile.framework.exception.ApplicationIOException;
import com.dianping.mobile.framework.exception.BuildContextException;
import com.dianping.mobile.framework.io.NoPaddingEncryptor;


public class MobileContextBuilderTest {
	
	private static class MockUserAgentParser extends AbstractUserAgentParser {
		private static final Pattern USERAGENT_IPHONE_REGEX = Pattern.compile(
				"mapi\\s*([0-9\\.]+)\\s*\\(aroundme ([0-9\\.]+)(.*);(?:.*)\\)", Pattern.CASE_INSENSITIVE);
		private static final Pattern USERAGENT_ANDROID_REGEX = Pattern.compile(
				"mapi\\s*([0-9\\.]+)\\s*\\(com.dianping.am ([0-9\\.]+)(.*);(?:.*)\\)", Pattern.CASE_INSENSITIVE);
		public MockUserAgentParser() {
			patternMap.put(new ClientType(Platform.iPhone, Product.API), USERAGENT_IPHONE_REGEX);
			patternMap.put(new ClientType(Platform.Android, Product.API), USERAGENT_ANDROID_REGEX);
		}
	}
	
	@Test
	public void testBuildContextIphone() throws BuildContextException, ApplicationIOException, ApplicationEncryptException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addParameter("action", "test");
		request.addHeader("User-Agent", "MApi 1.0 (aroundme 1.1 appstore; iPhone 5.0.1)");
		request.addHeader("pragma-device", "test");
		String postParam = "post=true";
		NoPaddingEncryptor encryptor = new NoPaddingEncryptor();
		request.setContent(encryptor.encrypt(postParam.getBytes()));
		DefaultMobileContextBuilder mobileContextBuilder = new DefaultMobileContextBuilder();
		mobileContextBuilder.setUserAgentParser(new MockUserAgentParser());
		
		ServletContext mockServletContext = new MockServletContext();
		MobileContext context = new MobileContext();
		try {
			mobileContextBuilder.buildContext(context,mockServletContext, request, true, true, false);
			assertEquals("127.0.0.1", context.getUserIp());
			assertEquals(Platform.iPhone, context.getClient().getPlatform());
			assertEquals("1.1", context.getVersion());
			assertEquals("1.0", context.getProtocolVersion());
			assertEquals("test", context.getParameter("action"));
			assertEquals("test", context.getHeader().getDeviceId());
			assertEquals("true", context.getParameter("post"));
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void testBuildContextAndroid() throws BuildContextException, ApplicationIOException, ApplicationEncryptException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addParameter("action", "test");
		request.addHeader("User-Agent", "MApi 1.1 (com.dianping.am 1.0 Android Play; Android 4.2)");
		request.addHeader("pragma-device", "test");
		String postParam = "post=true";
		NoPaddingEncryptor encryptor = new NoPaddingEncryptor();
		request.setContent(encryptor.encrypt(postParam.getBytes()));
		DefaultMobileContextBuilder mobileContextBuilder = new DefaultMobileContextBuilder();
		mobileContextBuilder.setUserAgentParser(new MockUserAgentParser());
		
		ServletContext mockServletContext = new MockServletContext();
		MobileContext context = new MobileContext();
		try {
			mobileContextBuilder.buildContext(context, mockServletContext, request, true, true, false);
			assertEquals("127.0.0.1", context.getUserIp());
			assertEquals(Platform.Android, context.getClient().getPlatform());
			assertEquals("1.0", context.getVersion());
			assertEquals("1.1", context.getProtocolVersion());
			assertEquals("test", context.getParameter("action"));
			assertEquals("test", context.getHeader().getDeviceId());
			assertEquals("true", context.getParameter("post"));
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}

}
