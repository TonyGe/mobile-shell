/**
 *
 */
package com.dianping.mobile.framework.datatypes;

import com.dianping.mobile.core.enums.ClientType;
import com.dianping.mobile.framework.datatypes.client.ClientConfig;
import org.apache.commons.fileupload.FileItem;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


/**
 * @author kewen.yao
 */
public class MobileContext implements IMobileContext {

    private static final String KEY_UPLOAD_FILE_APP_REQUEST = "be_data";
    private MobileHeader header;
    private Map<String, String> paramtersMap = new HashMap<String, String>();
    private String userIp;
    private ClientType client;
    private String version;
    private String protocolVersion;
    private String source;
    private String cellPhoneType;
    private String userAgent;
    private int userId = 0;
    private Map<String, FileItem> multiParamMap;
    private String os;
    private boolean isPost;
    private ClientConfig config;
    private String requestId;
    private String refferRequestId;
    private String actionKey;

    public MobileContext() {

    }

    @Override
    public Set<String> getParameterKeys() {
        return paramtersMap.keySet();
    }

    @Override
    public String getParameter(String paramName) {
        return paramtersMap.get(paramName);
    }

    public void setParamtersMap(Map<String, String> paramtersMap) {
        this.paramtersMap = paramtersMap;
    }

    @Override
    public MobileHeader getHeader() {
        return this.header;
    }

    public void setHeader(MobileHeader header) {
        this.header = header;
    }

    @Override
    public String getUserIp() {
        return userIp;
    }

    public void setUserIp(String userIp) {
        this.userIp = userIp;
    }

    @Override
    public ClientType getClient() {
        return client;
    }

    public void setClient(ClientType client) {
        this.client = client;
    }

    @Override
    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @Override
    public String getCellPhoneType() {
        return cellPhoneType;
    }

    public void setCellPhoneType(String cellPhoneType) {
        this.cellPhoneType = cellPhoneType;
    }

    @Override
    public String getUserAgent() {
        return this.userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    @Override
    public int getUserId() {
        return userId;
    }

    @Override
    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public Map<String, FileItem> getMultiParamMap() {
        return multiParamMap;
    }

    public void setMultiParamMap(Map<String, FileItem> multiParamMap) {
        this.multiParamMap = multiParamMap;
    }

    @Override
    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    @Override
    public String getProtocolVersion() {
        return protocolVersion;
    }

    public void setProtocolVersion(String protocolVersion) {
        this.protocolVersion = protocolVersion;
    }

    @Override
    public boolean isPost() {
        return isPost;
    }

    public void setPost(boolean isPost) {
        this.isPost = isPost;
    }

    @Override
    public ClientConfig getConfig() {
        return config;
    }

    public void setConfig(ClientConfig config) {
        this.config = config;
    }

    @Override
    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    @Override
    public String getRequestIdReffer() {
        return refferRequestId;
    }

    public void setRefferRequestId(String refferRequestId) {
        this.refferRequestId = refferRequestId;
    }

    @Override
    public String getActionKey() {
        return actionKey;
    }

    public void setActionKey(String actionKey) {
        this.actionKey = actionKey;
    }

    @Override
    public byte[] getPicFile() {
        FileItem file = null;
        if (multiParamMap != null && !multiParamMap.isEmpty()) {
            for (String key : multiParamMap.keySet()) {
                //客户端一般会传 key=be_data 参数数据 和 key=photo的 图片数据，暂时只支持一张图片，这里会有坑。
                if (!KEY_UPLOAD_FILE_APP_REQUEST.equalsIgnoreCase(key)) {
                    file = multiParamMap.get(key);
                    break;
                }
            }
        }
        return file == null ? null : file.get();
    }

}
