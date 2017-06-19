package com.dianping.mobile.framework.clientrule;

import com.dianping.mobile.base.datatypes.bean.ClientInfoRule;
import com.dianping.mobile.base.datatypes.enums.ClientType;
import com.dianping.mobile.framework.clientrule.bean.OneDo;
import com.dianping.mobile.framework.clientrule.bean.ThreeDo;
import com.dianping.mobile.framework.util.ClientRuleUtil;
import junit.framework.Assert;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.List;

/**
 * @author zhongkai.zhao
 *         13-11-28 上午9:29
 */
public class ClientRuleUtilTest {

    @Test
    public void test2() throws NoSuchFieldException {
        OneDo oneDo = new OneDo();
        OneDo twoDo = new OneDo();
        Assert.assertEquals(oneDo.getClass(), twoDo.getClass());
        Assert.assertEquals(oneDo.getClass().getDeclaredField("field"), twoDo.getClass().getDeclaredField("field"));
        Assert.assertEquals(oneDo.getClass().getDeclaredFields()[0], twoDo.getClass().getDeclaredFields()[0]);
        Assert.assertEquals(oneDo.getClass().getDeclaredFields()[1], twoDo.getClass().getDeclaredFields()[1]);
        Assert.assertEquals(oneDo.getClass().getDeclaredFields()[2], twoDo.getClass().getDeclaredFields()[2]);
        Assert.assertEquals(oneDo.getClass().getDeclaredFields()[3], twoDo.getClass().getDeclaredFields()[3]);
        Assert.assertEquals(oneDo.getClass().getDeclaredFields()[4], twoDo.getClass().getDeclaredFields()[4]);
        Assert.assertNotSame(OneDo.class.getDeclaredField("field"), ThreeDo.class.getDeclaredField("field"));
    }

    @Test
    public void test() {
        Class<OneDo> clazz = OneDo.class;

        for(Field field : clazz.getDeclaredFields()) {
            List<ClientInfoRule> rules = ClientRuleUtil.getClientInfoRules(field);
            if(field.getName().equals("field")) {

                Assert.assertEquals(1, rules.size());
                Assert.assertEquals(ClientType.MAINAPP_ANDROID, rules.get(0).getClientType());

            } else if(field.getName().equals("field2")) {

                Assert.assertEquals(1, rules.size());
                Assert.assertEquals(ClientType.YPAPP_IPHONE, rules.get(0).getClientType());
                Assert.assertEquals("6.2", rules.get(0).getMinVersion());

            } else if(field.getName().equals("field3")) {

                Assert.assertEquals(2, rules.size());
                Assert.assertEquals(ClientType.MAINAPP_ANDROID, rules.get(0).getClientType());
                Assert.assertEquals(ClientType.MAINAPP_IPHONE, rules.get(1).getClientType());

            } else if(field.getName().equals("field4")) {

                Assert.assertEquals(3, rules.size());
                Assert.assertEquals(ClientType.MAINAPP_ANDROID, rules.get(0).getClientType());
                Assert.assertEquals("6.2", rules.get(0).getMinVersion());
                Assert.assertEquals(ClientType.MAINAPP_IPHONE, rules.get(1).getClientType());
                Assert.assertEquals("6.2", rules.get(1).getMinVersion());
                Assert.assertEquals(ClientType.MAINAPP_IPADHD, rules.get(2).getClientType());
                Assert.assertEquals("2.6", rules.get(2).getMinVersion());

            } else if(field.getName().equals("field5")) {

                Assert.assertEquals(5, rules.size());
                Assert.assertEquals(ClientType.MAINAPP_IPADHD, rules.get(0).getClientType());
                Assert.assertEquals(ClientType.MAINAPP_ANDROID, rules.get(1).getClientType());
                Assert.assertEquals(ClientType.YPAPP_ANDROID, rules.get(2).getClientType());
                Assert.assertEquals(ClientType.MAINAPP_IPHONE, rules.get(3).getClientType());
                Assert.assertEquals(ClientType.YPAPP_IPHONE, rules.get(4).getClientType());

            }
        }
    }
}
