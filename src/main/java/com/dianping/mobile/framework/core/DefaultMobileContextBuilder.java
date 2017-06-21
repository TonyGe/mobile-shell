/**
 *
 */
package com.dianping.mobile.framework.core;

import com.dianping.cat.Cat;
import com.dianping.cat.message.Message;
import com.dianping.mobile.core.enums.ClientType;
import com.dianping.mobile.core.enums.Platform;
import com.dianping.mobile.framework.datatypes.MobileContext;
import com.dianping.mobile.framework.datatypes.MobileHeader;
import com.dianping.mobile.framework.datatypes.client.ClientConfigFactory;
import com.dianping.mobile.framework.exception.ApplicationDecryptException;
import com.dianping.mobile.framework.exception.ApplicationRuntimeException;
import com.dianping.mobile.framework.exception.IllegalNewTokenException;
import com.dianping.mobile.framework.exception.IllegalUserAgentException;
import com.dianping.mobile.framework.io.MobileIOUtil;
import com.dianping.mobile.framework.io.NoPaddingEncryptor;
import com.dianping.mobile.framework.io.Pkcs5Encryptor;
import com.dianping.mobile.framework.io.ResponseContent;
import com.dianping.mobile.framework.util.TokenUtil;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.FileCleanerCleanup;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileCleaningTracker;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.URLDecoder;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author kewen.yao
 */

public class DefaultMobileContextBuilder implements IMobileContextBuilder {

    private static final Logger log = Logger.getLogger(DefaultMobileContextBuilder.class);
    private static final String PARAM_NAME_TOKEN = "token";
    private static final String KEY_UPLOAD_FILE_APP_REQUEST = "be_data";
    private static NoPaddingEncryptor noPaddingEncryptor = new NoPaddingEncryptor();
    private static Pkcs5Encryptor pkcs5Encryptor = new Pkcs5Encryptor();
    private IUserAgentParser userAgentParser = new DefaultUserAgentParser();

    public void buildContext(MobileContext context, ServletContext servletContext, HttpServletRequest request, boolean isPost, boolean postCompressed, boolean isCheckToken) throws IllegalUserAgentException, IllegalNewTokenException, ApplicationDecryptException {
        if (context == null)
            throw new ApplicationRuntimeException("buildContext MobileContext == null");
        try {
            request.setCharacterEncoding(ResponseContent.ENCODING);

            parseHeader(context, request);

            parseRequestIp(context, request);

            parseUserAgent(context);

            parseParamters(context, servletContext, request, isPost, postCompressed);

            parseUserId(context);

            if (isCheckToken)
                validateNewToken(context);

        } catch (UnsupportedEncodingException e) {
            log.error("buildContext UnsupportedEncodingException", e);
            throw new ApplicationRuntimeException("buildContext UnsupportedEncodingException", e);
        }
    }

    private void parseHeader(MobileContext context, HttpServletRequest request) throws IllegalUserAgentException {
        String deviceId = request.getHeader("pragma-device") == null ? StringUtils.EMPTY : request.getHeader("pragma-device"); // symbian kjava etc. client have no deviceId
        String dpid = request.getHeader("pragma-dpid") == null ? StringUtils.EMPTY : request.getHeader("pragma-dpid");    //客户端6.1 开始传dpid
        Cat.getProducer().logEvent("Mobile", "Device", Message.SUCCESS, deviceId);
        String token = request.getHeader("pragma-token");

        String userAgent = request.getHeader("User-Agent");
        String pragma = request.getHeader("pragma");
        String pragmaOS = request.getHeader("pragma-os");
        String newToken = request.getHeader("pragma-newtoken");
        String wifi = request.getHeader("wifi");
        String uuid = request.getHeader("pragma-uuid");

        if (pragmaOS != null && pragmaOS.toLowerCase().contains("mapi")) {
            userAgent = pragmaOS;
        } else if (pragma != null && pragma.toLowerCase().contains("mapi")) {
            userAgent = pragma;
        }
        if (userAgent == null) {
            log.warn("useragent== null");
            throw new IllegalUserAgentException("useragent== null");
        }
        context.setHeader(new MobileHeader(userAgent, token, deviceId, newToken, dpid, wifi, uuid));

//        String requestId = PhoenixContext.getInstance().getRequestId();
//        if (requestId != null) {
//            String refferRequestId = PhoenixContext.getInstance().getReferRequestId();
//            context.setRequestId(requestId);
//            context.setRefferRequestId(refferRequestId);
//        }

    }

    private void parseRequestIp(MobileContext context, HttpServletRequest request) {
//        context.setUserIp(RemoteIpGetter.getFirstGlobalAddr(request));
    }

    private void parseUserAgent(MobileContext context) throws IllegalUserAgentException {
        String userAgent = context.getHeader().getUserAgent();
        context.setUserAgent(userAgent);
        String ua = userAgent.toLowerCase();
        for (ClientType client : userAgentParser.getKeys()) {
            Pattern pattern = userAgentParser.getPattern(client);
            Matcher matcher = pattern.matcher(ua);
            if (matcher.find() && matcher.groupCount() == 3) {
                context.setClient(client);
                context.setConfig(ClientConfigFactory.getInstance(client));
                String protocolVersion = matcher.group(1).trim();
                String clientVersion = matcher.group(2).trim();
                String source = matcher.group(3).trim();
                context.setProtocolVersion(protocolVersion);
                context.setVersion(clientVersion);
                String[] ss = source.split(" ");
                if (ss.length == 2) {
                    context.setSource(ss[0]);
                    context.setCellPhoneType(ss[1]);
                } else {
                    context.setSource(source);
                }
                break;
            }
        }
        if (context.getClient() == null) {
            final String msg = "clientType unkown, user agent:" + ua;
            log.error(msg);
            throw new IllegalUserAgentException(msg);
        }
        String[] s = ua.split(";");
        if (s != null && s.length == 2) {
            context.setOs(s[1].trim().substring(0, s[1].trim().length() - 1));
        } else {
            context.setOs("");
        }
    }

    private void parseParamters(MobileContext context, ServletContext servletContext, HttpServletRequest request, boolean isPost, boolean postCompressed) throws ApplicationDecryptException {
        Map<String, String> parameterMap = new HashMap<String, String>();
        context.setPost(isPost);
        if (isPost) {
            if (ServletFileUpload.isMultipartContent(request)) {
                context.setMultiParamMap(parseMultiPart(servletContext, request, parameterMap));
            }

            try {
                InputStream reqis = request.getInputStream();
                if (reqis != null) {
                    parsePostParamters(parameterMap, reqis, context.getClient(), postCompressed);
                }
            } catch (IOException e) {
                log.warn("parse inputStream exception", e);
            }

            try {
                if (context.getMultiParamMap() != null && context.getMultiParamMap().get(KEY_UPLOAD_FILE_APP_REQUEST) != null) {
                    InputStream is = context.getMultiParamMap().get(KEY_UPLOAD_FILE_APP_REQUEST).getInputStream();
                    if (is != null) {
                        parsePostParamters(parameterMap, is, context.getClient(), postCompressed);
                    }
                }
            } catch (IOException e) {
                log.warn("parse be_data exception", e);
            }
        }
        parseGetParamters(parameterMap, request);
        context.setParamtersMap(parameterMap);
    }

    private void parsePostParamters(Map<String, String> parameterMap, InputStream is, ClientType clientType, boolean postCompressed) throws ApplicationDecryptException {
        byte[] bytes = inputStream2Bytes(is);
        if (bytes != null && bytes.length > 0) {
            try {
                byte[] post = null;
                if (clientType != null && (clientType.getPlatform() == Platform.WinPhone
                        || clientType.getPlatform() == Platform.Win8Pad)) {
                    post = pkcs5Encryptor.decrypt(bytes);
                } else {
                    post = noPaddingEncryptor.decrypt(bytes, postCompressed);
                }
                if (postCompressed) {
                    post = MobileIOUtil.uncompress(post);
                }
                if (post != null && post.length > 0) {
                    String data = new String(post, ResponseContent.ENCODING);
                    String[] keyValue = data.split("&");
                    for (int i = 0; i < keyValue.length; i++) {
                        String[] kv = keyValue[i].split("[=]", 2);
                        if (kv.length == 2) {
                            try {
                                parameterMap.put(URLDecoder.decode(kv[0], ResponseContent.ENCODING).toLowerCase(), URLDecoder.decode(kv[1], ResponseContent.ENCODING));
                            } catch (Exception e) {
                                log.error(kv[0] + "," + kv[1], e);
                            }
                        }
                    }
                }
            } catch (ApplicationDecryptException e) {
                throw new ApplicationDecryptException("parsePostParamters decryptByNoPading", e);
            } catch (UnsupportedEncodingException e) {
                throw new ApplicationRuntimeException("parsePostParamters UnsupportedEncodingException", e);
            }
        }
    }

    private byte[] inputStream2Bytes(InputStream inputStream) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            if (inputStream != null) {
                byte[] buffer = new byte[1024];//need to check the common size of the request
                int bytesRead = -1;
                while ((bytesRead = inputStream.read(buffer)) > 0) {
                    baos.write(buffer, 0, bytesRead);
                }
            }
            return baos.toByteArray();
        } catch (IOException e) {
            throw new ApplicationRuntimeException("parsePostParamters inputStream2Bytes", e);
        }
    }

    private void parseGetParamters(Map<String, String> paramgersMap, HttpServletRequest request) {
        @SuppressWarnings("unchecked")
        Enumeration<String> params = request.getParameterNames();
        while (params.hasMoreElements()) {
            String key = params.nextElement();
            String decodedKey = null;
            try {
                decodedKey = URLDecoder.decode(key, ResponseContent.ENCODING).toLowerCase();
            } catch (Exception e) {
                log.warn(e);
            }
            String value = request.getParameter(key);
            try {
                value = URLDecoder.decode(value, ResponseContent.ENCODING);
            } catch (Exception e) {
                log.warn(e);
            }
            if (decodedKey != null) {
                paramgersMap.put(decodedKey, value);
            } else {
                paramgersMap.put(key, value);
            }
        }
    }

    private void parseUserId(MobileContext context) {
        String token = context.getHeader().getToken();

        if (StringUtils.isBlank(token)) {
            token = context.getParameter(PARAM_NAME_TOKEN);
        }
        if (!StringUtils.isBlank(token)) {
            int parsedToken = TokenUtil.parseToken(token);
            Cat.getProducer().logEvent("Mobile", "user", Message.SUCCESS, String.valueOf(parsedToken));
            context.setUserId(parsedToken);
        }
    }

    private void validateNewToken(MobileContext context) throws IllegalNewTokenException {
        String newToken = context.getHeader().getNewToken();
        if (StringUtils.isBlank(newToken)) {
            //do nothing
        } else {
//            TokenResult t = null;
//            try {
//                t = TokenGenerator.validate(newToken);
//            } catch (Exception e) {
//                log.error(e.getMessage(), e);
//            }
//            if (t != null && !t.isValid())
//                throw new IllegalNewTokenException(String.format("validate newtoken failed[%s][%s][%s]", newToken, t.getUserId(), t.getNewToken()));
        }
    }

    public void setUserAgentParser(IUserAgentParser userAgentParser) {
        this.userAgentParser = userAgentParser;
    }


    private Map<String, FileItem> parseMultiPart(ServletContext servletContext, HttpServletRequest req,
                                                 Map<String, String> parameterMap) {
        if (!ServletFileUpload.isMultipartContent(req)) {
            return null;
        }
        try {
            HashMap<String, FileItem> files = new HashMap<String, FileItem>();
            // Create a new file upload handler
            ServletFileUpload upload = newServletFileUpload(servletContext);
            // Parse the request
            @SuppressWarnings("unchecked")
            List items = upload.parseRequest(req);
            // Process the uploaded items
            @SuppressWarnings("unchecked")
            Iterator iter = items.iterator();
            while (iter.hasNext()) {
                FileItem item = (FileItem) iter.next();
                String name = item.getFieldName();

                if (item.isFormField()) {// Process a regular form field
                    String value = item.getString("utf-8");
                    parameterMap.put(name.toLowerCase(), value);
                } else {
                    files.put(name, item);
                }
            }
            return files;
        } catch (Exception e) {
            throw new ApplicationRuntimeException("parse multi part parameter error", e);
        }
    }

    /**
     * TODO: can  DiskFileItemFactory factory  be singleton???
     */
    private ServletFileUpload newServletFileUpload(ServletContext context) {
        // Create a factory for disk-based file items
        DiskFileItemFactory factory = new DiskFileItemFactory();
        // Set factory constraints
        factory.setSizeThreshold(5 * 1024 * 1024);//yourMaxMemorySize  TODO remove hardcode
        factory.setRepository(new File(context.getRealPath("/upload_temp"))); //yourTempDirectory
        // Create a factory for disk-based file items
        FileCleaningTracker fileCleaningTracker = FileCleanerCleanup.getFileCleaningTracker(context);
        factory.setFileCleaningTracker(fileCleaningTracker);


        ServletFileUpload upload = new ServletFileUpload(factory);
        // Set overall request size constraint
        upload.setSizeMax(5 * 1024 * 1024);//yourMaxRequestSize  TODO remove hardcode

        return upload;
    }
}
