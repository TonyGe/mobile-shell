package com.dianping.mobile.framework.servlet;

import com.dianping.cat.Cat;
import com.dianping.cat.CatConstants;
import com.dianping.cat.message.Message;
import com.dianping.cat.message.Transaction;
import com.dianping.mobile.core.util.ApiUtil;
import com.dianping.mobile.framework.action.ActionFactory;
import com.dianping.mobile.framework.action.IAction;
import com.dianping.mobile.framework.base.datatypes.ErrorMsg;
import com.dianping.mobile.framework.base.datatypes.HttpCode;
import com.dianping.mobile.framework.base.datatypes.SimpleMsg;
import com.dianping.mobile.framework.base.datatypes.StatusCode;
import com.dianping.mobile.framework.core.*;
import com.dianping.mobile.framework.datatypes.IMobileContext;
import com.dianping.mobile.framework.datatypes.IMobileResponse;
import com.dianping.mobile.framework.datatypes.MobileContext;
import com.dianping.mobile.framework.exception.*;
import com.dianping.mobile.framework.io.ResponseContent;
import com.dianping.mobile.framework.io.ResponseType;
import com.dianping.mobile.framework.log.SysErrorLog;
import com.dianping.mobile.framework.util.SpringUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Pattern;

/**
 * Servlet implementation class MainServlet
 */
public class MainServlet extends HttpServlet {

    private static final ErrorMsg DEFAULT_ERROR_MSG = new ErrorMsg("错误", "服务器忙，请稍后再试(404)", StatusCode.ACTIONEXCEPTION);

    private static final ErrorMsg NEWTOKEN_ILLEGAL_ERROR_MSG = new ErrorMsg("错误", "请重新登录(401)", HttpCode.NEWTOKEN);

    private static final ErrorMsg ILLEGALHTTPTYPE_ERROR_MSG = new ErrorMsg("错误", "请求方式错误post?get?,请查看接口文档", StatusCode.HTTPTYPEERROR);

    private static final ErrorMsg MSG_ERROR_ILLEGALUA = new ErrorMsg("错误", "无效UserAgent", StatusCode.USAGEERROR);

    private static final ErrorMsg MSG_ERROR_PARAMDECODE = new ErrorMsg("错误", "POST参数解密失败", HttpCode.ENCRYPT);

    private static final ErrorMsg MSG_ERROR_RES_NULLOBJ = new ErrorMsg("错误", "不可能发生的错误，请联系API开发", StatusCode.FRAMEERROR);

    private static final int STATUS_ERROR = HttpCode.HTTP_SERVER_ERROR;


    private static final long serialVersionUID = 1L;

    private static final Logger log = Logger.getLogger(MainServlet.class);
    private static final Pattern PATTERN_ACTION_KEY = Pattern.compile("(^[a-z]+/)?\\s*([a-z.]+)", Pattern.CASE_INSENSITIVE);
    private IResponseFormatter responseFormatter = new DefaultResponseFormatter();
    private IResponseFormatter stringFormatter = new StringFormatter();

    public MainServlet() {
        super();
    }

    @Override
    public void init() throws ServletException {
        super.init();
        String subject = "server up! ip=" + ApiUtil.getIp();
        log.info("jboss start: " + subject);
    }

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {
        handleRequest(request, response, false);
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {
        handleRequest(request, response, true);
    }

    protected void handleRequest(HttpServletRequest request, HttpServletResponse response, boolean isPost) throws ServletException, IOException {
        //IMobileResponse mobileResponse = null;
        //ResponseContent responseContent = null;
        MobileContext context = new MobileContext();
        Object responseObj = null;
        StatusCode errorCode = null;
        int statusCode = STATUS_ERROR;
        IAction action = null;
        ResponseType encoding = ResponseType.MAPI;
        try {
            String actionKey = parseActionKey(request);
            if (StringUtils.isBlank(actionKey)) {
                SysErrorLog.error("actionkey is empty return");
                return;
            }

            action = parseAction(actionKey);

            if (action == null) {
                SysErrorLog.error("action is null return");
                return;
            }
            encoding = action.getEncryption();
            try {
                validateHttpType(action, request);
                boolean postCompressed = ("gzip".equalsIgnoreCase(request.getHeader("Content-Encoding"))
                        && "post".equalsIgnoreCase(action.getHttpType())
                        && action.postCompressed());

                getContextBuilder().buildContext(context, this.getServletContext(), request, isPost, postCompressed, action.isCheckToken());
                //set actionkey
                context.setActionKey(actionKey);
                try {
                    validateNewToken(context);
                } catch (NewTokenNullException e) {
                    log.warn(e.getMessage());
                    try {
                        //TODO
//                        response.setHeader("pragma-newtoken", TokenGenerator.generate(context.getUserId()));

                    } catch (Exception e1) {
                        // 可能抛异常
                        log.error(e.getMessage(), e);
                    }
                }

                try {
                    StatusCode.init(); // 初始化线程变量
                    IMobileResponse mobileResponse = execute(action, context);
                    responseObj = mobileResponse.getData();
                    errorCode = HttpCode.servercode(mobileResponse);// 取出errorcode
                    //responseContent = format(mobileResponse.getData(), mobileResponse.getStatusCode(), context);
                } catch (ClientErrorException e) {
                    log.error(e.getMessage(), e);
                    ErrorMsg Client_ERROR_MSG = new ErrorMsg("错误", "客户端错误，" + e.getMessage(), StatusCode.PARAMERROR);
                    responseObj = Client_ERROR_MSG;
                } catch (Throwable e) {
                    responseObj = DEFAULT_ERROR_MSG;
                    SysErrorLog.error("action execute exception", e);
                }

            } catch (HttpTypeIllegalException e) {
                log.error(e.getMessage(), e);
                responseObj = ILLEGALHTTPTYPE_ERROR_MSG;
            } catch (IllegalUserAgentException e) {
                log.error(e.getMessage(), e);
                responseObj = MSG_ERROR_ILLEGALUA;
            } catch (IllegalNewTokenException e) {
                log.error(e.getMessage(), e);
                responseObj = NEWTOKEN_ILLEGAL_ERROR_MSG;
            } catch (ApplicationDecryptException e) {
                SysErrorLog.error("parse post param decrypt exception", e);
                responseObj = MSG_ERROR_PARAMDECODE;
            }
            if (responseObj == null) {
                responseObj = MSG_ERROR_RES_NULLOBJ;
            }
            if (errorCode == null && responseObj instanceof SimpleMsg)
                errorCode = ((SimpleMsg) responseObj).getStatusCode();
            // 记录statusCode
            logErrorCode(errorCode, action);
            // 转换为http statcode
            statusCode = HttpCode.httpcode(errorCode);
            ResponseContent responseContent = format(responseObj, statusCode, context, encoding);
            output(response, responseContent);
            //success = true;
        } catch (ApplicationIOException e) {
            SysErrorLog.error("format ioexcption", e);
        } catch (ApplicationEncryptException e) {
            SysErrorLog.error("format encrypt exception", e);
        } catch (Throwable e) {
            SysErrorLog.error("unhandled exception", e);
        } finally {

        }
    }

    private IAction parseAction(String actionKey) {
        IAction action = null;
        if (actionKey != null) {
            action = ActionFactory.createAction(actionKey);
        }

        return action;
    }

    private String parseActionKey(HttpServletRequest request) {
        String uri = request.getRequestURI();
        String contextPath = request.getContextPath();
        if (uri.length() <= contextPath.length() + 1) {
            log.warn("request URI is wrong:" + uri);
            return null;
        }
        String actionKey = uri.substring(contextPath.length() + 1).toLowerCase();
        return actionKey;
    }

    private void validateHttpType(IAction action, HttpServletRequest request) throws HttpTypeIllegalException {
        if (!StringUtils.isBlank(action.getHttpType())) {
            if (!request.getMethod().equalsIgnoreCase(action.getHttpType())) {
                throw new HttpTypeIllegalException("checkHttpType failed excpect=" + action.getHttpType() + "&actual=" + request.getMethod());
            }
        }
    }

    private void validateNewToken(MobileContext context) throws NewTokenNullException {
        String newToken = context.getHeader().getNewToken();
        if (StringUtils.isBlank(newToken)) {
            if (context.getUserId() > 0)
                throw new NewTokenNullException("newtoken is null,userid=" + context.getUserId());
        }
    }

    private IMobileResponse execute(IAction action, IMobileContext context) throws ClientErrorException {
        Transaction trasaction = Cat.newTransaction(CatConstants.TYPE_ACTION, action.getActionKey());
        trasaction.setStatus(Transaction.SUCCESS);
        try {
            IMobileResponse result = action.execute(context);
            return result;
        } catch (ClientErrorException e) {
            Cat.logError(e);
            trasaction.setStatus(e);
            throw e;
        } finally {
            trasaction.complete();
        }
    }

    private ResponseContent format(Object data, int statusCode,
                                   IMobileContext context, ResponseType encoding) throws ApplicationIOException, ApplicationEncryptException {
        Transaction transaction = Cat.newTransaction("Response", "Format");
        transaction.setStatus(Transaction.SUCCESS);
        try {
            return getResponseFormatter(encoding).format(data, statusCode, context);
        } catch (ApplicationIOException re) {
            Cat.logError(re);
            transaction.setStatus(re);
            throw re;
        } catch (ApplicationEncryptException e) {
            Cat.logError(e);
            transaction.setStatus(e);
            throw e;
        } finally {
            transaction.complete();
        }
    }

    protected void outputDelegate(HttpServletResponse response, ResponseContent responseContent) throws IOException {
        response.setCharacterEncoding(ResponseContent.ENCODING);
        response.setContentType(responseContent.getContentType());
        response.setStatus(responseContent.getStatusCode());
        response.setContentLength(responseContent.getContent().length);
        //IO的异常不再抛出，不再显示在cat上面, 比如ClientAbortException
        try {
            response.getOutputStream().write(responseContent.getContent());
        } catch (IOException e) {
            log.error("outputStreamWriteIOException", e);
        }

    }

    private void output(HttpServletResponse response, ResponseContent responseContent) {
        if (responseContent != null) {
            Transaction transaction = Cat.newTransaction("Response", "Output");
            transaction.setStatus(Transaction.SUCCESS);
            try {
                outputDelegate(response, responseContent);
                Cat.getProducer().logEvent("Response", "Status", Message.SUCCESS, "status code=" + responseContent.getStatusCode());
            } catch (IOException e) {
                log.warn("write responseContent to client error", e);
                transaction.setStatus(e);
                Cat.logError(e);
            } finally {
                transaction.complete();
            }
        }
    }

    /**
     * 在cat上记录服务器状态
     */
    private void logErrorCode(StatusCode statuscode, IAction action) {
        int code = statuscode == null ? 0 : statuscode.getCode();
        if (code == HttpCode.HTTP_STATUS_OK)
            Cat.getProducer().logEvent("Action.Status", action.getActionKey() + ":" + code, Message.SUCCESS, "action business status code");
        else
            Cat.getProducer().logEvent("Action.Status", action.getActionKey() + ":" + code, "ERROR", "action business status code");
    }

    protected IMobileContextBuilder getContextBuilder() {
        IMobileContextBuilder contextBuilder = SpringUtil.getBean("contextBuilder");
        if (contextBuilder == null) {
            //LOG.info("war no set contextBuilder");
            contextBuilder = new DefaultMobileContextBuilder();
        }
        return contextBuilder;
    }

    protected IResponseFormatter getResponseFormatter(ResponseType encoding) {
        if (ResponseType.STRING.equals(encoding))
            return stringFormatter;
        else
            return responseFormatter;
    }

}
