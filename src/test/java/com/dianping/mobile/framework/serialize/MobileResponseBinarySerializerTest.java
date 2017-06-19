package com.dianping.mobile.framework.serialize;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import junit.framework.Assert;

import org.junit.Test;

import com.dianping.mobile.base.datatypes.enums.Platform;
import com.dianping.mobile.framework.annotation.MobileDo;
import com.dianping.mobile.framework.annotation.MobileDo.MobileField;
import com.dianping.mobile.framework.annotation.MobileDo.MobilePlatform;
import com.dianping.mobile.framework.annotation.MobileDo.MobileVersion;

public class MobileResponseBinarySerializerTest {
	
	@MobileDo(id = 0x1111)
	private class MockMobileDo {
		
		@MobileField(key = 0x1111)
		@MobileVersion(values = {"0.1", "5.0"})
		private String keyword;
		
		@MobilePlatform(values = {Platform.Android, Platform.iPhone})
		private int categoryId;
		
		@MobilePlatform(values = {Platform.Android, Platform.iPhone}, include = false)
		private int noNeed;
		
	}
	
	@Test
	public void testNeedSerialzation() throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, NoSuchFieldException {
		Method method = MobileResponseBinarySerializer.class.getDeclaredMethod("needSerialzation", MobileVersion.class, String.class);
		method.setAccessible(true);
		Annotation versionAnnotation = MockMobileDo.class.getDeclaredField("keyword").getAnnotation(MobileVersion.class);
		Assert.assertTrue((Boolean) method.invoke(null, versionAnnotation, "3.5"));
		Assert.assertFalse((Boolean) method.invoke(null, versionAnnotation, "5.1"));
		Assert.assertFalse((Boolean) method.invoke(null, versionAnnotation, "0.0.1"));
		
		
		Method method2 = MobileResponseBinarySerializer.class.getDeclaredMethod("needSerialzation", MobilePlatform.class, Platform.class);
		method2.setAccessible(true);
		Annotation platformAnnotation = MockMobileDo.class.getDeclaredField("categoryId").getAnnotation(MobilePlatform.class);
		Assert.assertTrue( (Boolean) method2.invoke(null, platformAnnotation, Platform.Android));
		Assert.assertTrue( (Boolean) method2.invoke(null, platformAnnotation, Platform.iPhone));
		Assert.assertFalse( (Boolean) method2.invoke(null, platformAnnotation, Platform.WinPhone));
		Assert.assertFalse( (Boolean) method2.invoke(null, platformAnnotation, null));
		
		Method method3 = MobileResponseBinarySerializer.class.getDeclaredMethod("needSerialzation", MobilePlatform.class, Platform.class);
		method2.setAccessible(true);
		platformAnnotation = MockMobileDo.class.getDeclaredField("noNeed").getAnnotation(MobilePlatform.class);
		Assert.assertFalse( (Boolean) method2.invoke(null, platformAnnotation, Platform.Android));
		Assert.assertFalse( (Boolean) method2.invoke(null, platformAnnotation, Platform.iPhone));
		Assert.assertTrue( (Boolean) method2.invoke(null, platformAnnotation, Platform.WinPhone));
		Assert.assertTrue( (Boolean) method2.invoke(null, platformAnnotation, null));

		
	}
	
}
