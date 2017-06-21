package com.dianping.mobile.framework.servlet;


import com.dianping.mobile.core.enums.ClientType;
import com.dianping.mobile.core.enums.Platform;
import com.dianping.mobile.core.enums.Product;
import com.dianping.mobile.framework.action.ActionFactory;
import com.dianping.mobile.framework.action.IAction;
import com.dianping.mobile.framework.base.datatypes.ErrorMsg;
import com.dianping.mobile.framework.core.DefaultMobileContextBuilder;
import com.dianping.mobile.framework.core.IMobileContextBuilder;
import com.dianping.mobile.framework.core.IResponseFormatter;
import com.dianping.mobile.framework.datatypes.*;
import com.dianping.mobile.framework.exception.ApplicationRuntimeException;
import com.dianping.mobile.framework.exception.BuildContextException;
import com.dianping.mobile.framework.io.Formatter;
import com.dianping.mobile.framework.io.ResponseContent;
import com.dianping.mobile.framework.io.ResponseType;
import org.easymock.EasyMock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletConfig;
import org.springframework.mock.web.MockServletContext;

import javax.servlet.ServletContext;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(PowerMockRunner.class)
@PrepareForTest({DefaultMobileContextBuilder.class, ActionFactory.class,
        Formatter.class})
@PowerMockIgnore({"javax.crypto.*"})
public class MainServletTest {

    @Test
    public void testHandleRequest() throws Exception {
        PowerMock.mockStatic(DefaultMobileContextBuilder.class);
        PowerMock.mockStatic(ActionFactory.class);

        MainServlet servlet = new MainServlet();
        //Field field = MainServlet.class.getDeclaredField("contextBuilder");
        ServletContext mockServletContext = new MockServletContext();
        servlet.init(new MockServletConfig(mockServletContext));
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        request.setRequestURI("/test/ut.yp");
        request.setRemotePort(8080);
        request.setContextPath("/test");
        request.addHeader("User-Agent", "MApi 1.0 (com.dianping.v1 5.1; Android 2.2)");


        MobileContext context = new MobileContext();
        context.setUserIp("127.0.0.1");
        context.setUserAgent("");
        context.setClient(new ClientType(Platform.iPhone, Product.API));
        context.setVersion("1.0.0");
        context.setHeader(new MobileHeader());
        context.setParamtersMap(new HashMap<String, String>());
        ArrayList<String> result = new ArrayList<String>();
        result.add("test string1");
        result.add("test string2");
        final IMobileResponse mobileResponse = new CommonMobileResponse(result);
        IAction mockAction = new IAction() {

            @Override
            public String getActionKey() {
                return "ut.yp";
            }

            @Override
            public IMobileResponse execute(IMobileContext context) {
                return mobileResponse;
            }

            @Override
            public String getHttpType() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public boolean postCompressed() {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public boolean isCheckToken() {
                return false;
            }

            @Override
            public ResponseType getEncryption() {
                return null;
            }
        };
        EasyMock.expect(ActionFactory.createAction("ut.yp")).andReturn(mockAction);

        IMobileContextBuilder contextBuilder = EasyMock.createMock(IMobileContextBuilder.class);
//		field.setAccessible(true);
//		field.set(servlet, contextBuilder);
//		EasyMock.expect(
//				contextBuilder.buildContext(mockServletContext, request,
//						true)).andReturn(context);


        ResponseContent responseContent = Formatter.responseFormatter(result,
                200, context.getClient(), null);

        PowerMock.replayAll();
        servlet.handleRequest(request, response, true);
        assertNotNull(response.getContentAsByteArray());
        assertEquals(responseContent.getContent().length, response.getContentLength());
        PowerMock.verifyAll();
    }

    @Test
    public void testHandleRequestWithRuntimeException() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.setRequestURI("/test/ut.yp");
        request.setRemotePort(8080);
        request.setContextPath("/test");
        PowerMock.mockStatic(DefaultMobileContextBuilder.class);
        PowerMock.mockStatic(ActionFactory.class);
        PowerMock.mockStatic(Formatter.class);
        MainServlet mockServlet = new MainServlet();
        ServletContext mockServletContext = new MockServletContext();
        mockServlet.init(new MockServletConfig(mockServletContext));

        // MobileContext context = new MobileContext();
        // context.setUserIp("127.0.0.1");
        // context.setUserAgent("");
        // context.setClient(new ClientType(Platform.iPhone, Product.API));
        // context.setVersion(new Version("1.0.0"));
        // context.setHeader(new MobileHeader());
        // EasyMock.expect(MobileContextBuilder.buildContext(mockServletContext,
        // request, false))
        // .andReturn(context);

        EasyMock.expect(ActionFactory.createAction(EasyMock.isA(String.class)))
                .andThrow(
                        new ApplicationRuntimeException("springContext == null"));

        ResponseContent responseContent = new ResponseContent();
        responseContent.setStatusCode(200);
        responseContent.setContentType(ResponseContent.CONTENTTYPE_BINARY);
        responseContent.setContent(new byte[100]);
        EasyMock.expect(
                Formatter.responseFormatter(EasyMock.isA(ErrorMsg.class),
                        EasyMock.anyInt(), EasyMock.isNull(ClientType.class),
                        EasyMock.isNull(String.class))).andReturn(
                responseContent);

        PowerMock.replayAll();
        mockServlet.handleRequest(request, response, false);
        assertNotNull(response.getContentAsByteArray());
        PowerMock.verifyAll();
    }

    @Test
    public void testHandleRequestWithBuildContextException() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.setRequestURI("/test/ut.yp");
        request.setRemotePort(8080);
        request.setContextPath("/test");
        PowerMock.mockStatic(DefaultMobileContextBuilder.class);
        PowerMock.mockStatic(ActionFactory.class);
        PowerMock.mockStatic(Formatter.class);
        MainServlet mockServlet = new MainServlet();
        ServletContext mockServletContext = new MockServletContext();
        mockServlet.init(new MockServletConfig(mockServletContext));

        IAction action = new IAction() {

            @Override
            public String getActionKey() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public IMobileResponse execute(IMobileContext context) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public String getHttpType() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public boolean postCompressed() {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public boolean isCheckToken() {
                return false;
            }

            @Override
            public ResponseType getEncryption() {
                return null;
            }
        };
        EasyMock.expect(ActionFactory.createAction(EasyMock.isA(String.class)))
                .andReturn(action);

        BuildContextException exception = new BuildContextException(
                "useragent== null || deviceId == null");

        Field field = MainServlet.class.getDeclaredField("contextBuilder");
        IMobileContextBuilder contextBuilder = EasyMock.createMock(IMobileContextBuilder.class);
        field.setAccessible(true);
        field.set(mockServlet, contextBuilder);


//		EasyMock.expect(
//				mockServlet.getContextBuilder().buildContext(context,mockServletContext, request,
//						false)).andThrow(exception);

        ResponseContent responseContent = new ResponseContent();
        responseContent.setStatusCode(200);
        responseContent.setContentType(ResponseContent.CONTENTTYPE_BINARY);
        responseContent.setContent(new byte[100]);

        field = MainServlet.class.getDeclaredField("responseFormatter");
        IResponseFormatter responseFormatter = EasyMock.createMock(IResponseFormatter.class);
        field.setAccessible(true);
        field.set(mockServlet, responseFormatter);

        EasyMock.expect(
                responseFormatter.format(EasyMock.isA(Object.class),
                        EasyMock.anyInt(), EasyMock.isNull(IMobileContext.class))).andReturn(
                responseContent);

        PowerMock.replayAll();
        mockServlet.handleRequest(request, response, false);
        assertNotNull(response.getContentAsByteArray());
        PowerMock.verifyAll();
    }

    @Test
    public void testTokenCheck() throws Exception {
        PowerMock.mockStatic(DefaultMobileContextBuilder.class);
        PowerMock.mockStatic(ActionFactory.class);

        MainServlet servlet = new MainServlet();
        //Field field = MainServlet.class.getDeclaredField("contextBuilder");
        ServletContext mockServletContext = new MockServletContext();
        servlet.init(new MockServletConfig(mockServletContext));
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        request.setRequestURI("/test/ut.yp");
        request.setRemotePort(8080);
        request.setContextPath("/test");
        request.addHeader("User-Agent", "MApi 1.0 (com.dianping.v1 5.1; Android 2.2)");


        MobileContext context = new MobileContext();
        context.setUserIp("127.0.0.1");
        context.setUserAgent("");
        context.setClient(new ClientType(Platform.iPhone, Product.API));
        context.setVersion("1.0.0");
        context.setHeader(new MobileHeader());
        context.setParamtersMap(new HashMap<String, String>());
        ArrayList<String> result = new ArrayList<String>();
        result.add("test string1");
        result.add("test string2");
        final IMobileResponse mobileResponse = new CommonMobileResponse(result);
        IAction mockAction = new IAction() {

            @Override
            public String getActionKey() {
                return "ut.yp";
            }

            @Override
            public IMobileResponse execute(IMobileContext context) {
                return mobileResponse;
            }

            @Override
            public String getHttpType() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public boolean postCompressed() {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public boolean isCheckToken() {
                return false;
            }

            @Override
            public ResponseType getEncryption() {
                return null;
            }
        };
        EasyMock.expect(ActionFactory.createAction("ut.yp")).andReturn(mockAction);

        IMobileContextBuilder contextBuilder = EasyMock.createMock(IMobileContextBuilder.class);
//		field.setAccessible(true);
//		field.set(servlet, contextBuilder);
//		EasyMock.expect(
//				contextBuilder.buildContext(mockServletContext, request,
//						true)).andReturn(context);


        ResponseContent responseContent = Formatter.responseFormatter(result,
                200, context.getClient(), null);

        PowerMock.replayAll();
        servlet.handleRequest(request, response, true);
        assertNotNull(response.getContentAsByteArray());
        assertEquals(responseContent.getContent().length, response.getContentLength());
        PowerMock.verifyAll();
    }
}
