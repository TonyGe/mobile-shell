package com.dianping.mobile.framework.datatypes;

import com.dianping.mobile.core.enums.ClientType;
import com.dianping.mobile.framework.datatypes.client.ClientConfig;
import org.apache.commons.fileupload.FileItem;

import java.util.Map;
import java.util.Set;


public interface IMobileContext {

    Set<String> getParameterKeys();

    String getParameter(String paramName);

    MobileHeader getHeader();

    String getUserIp();

    ClientType getClient();

    String getVersion();

    String getSource();

    String getCellPhoneType();

    String getUserAgent();

    int getUserId();

    void setUserId(int userId);

    Map<String, FileItem> getMultiParamMap();

    String getOs();

    String getProtocolVersion();

    boolean isPost();

    ClientConfig getConfig();

    String getRequestId();

    String getRequestIdReffer();

    String getActionKey();

    byte[] getPicFile();
}
