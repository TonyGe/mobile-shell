/**
 * 
 */
package com.dianping.mobile.framework.datatypes;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.fileupload.FileItem;

import com.dianping.mobile.base.datatypes.enums.ClientType;
import com.dianping.mobile.framework.datatypes.client.ClientConfig;


/**
 * @author kewen.yao
 *
 */
public class MobileContext implements IMobileContext {
	
	private static final String KEY_UPLOAD_FILE_APP_REQUEST = "be_data";
	
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

	public void setUserIp(String userIp) {
		this.userIp = userIp;
	}
	@Override
	public String getUserIp() {
		return userIp;
	}

	public void setClient(ClientType client) {
		this.client = client;
	}
	@Override
	public ClientType getClient() {
		return client;
	}

	public void setVersion(String version) {
		this.version = version;
	}
	@Override
	public String getVersion() {
		return version;
	}
	

	
	public void setSource(String source) {
		this.source = source;
	}
	@Override
	public String getSource() {
		return source;
	}

	public void setCellPhoneType(String cellPhoneType) {
		this.cellPhoneType = cellPhoneType;
	}
	@Override
	public String getCellPhoneType() {
		return cellPhoneType;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}
	
	@Override
	public String getUserAgent() {
		return this.userAgent;
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
	
	public void setConfig(ClientConfig config) {
		this.config = config;
	}
	
	@Override
	public ClientConfig getConfig() {
		return config;
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
	
	public void setActionKey(String actionKey) {
		this.actionKey = actionKey;
	}
	
	@Override
	public String getActionKey() {
		return actionKey;
	}
	
	@Override
	public byte[] getPicFile() {
		FileItem file = null;
		if(multiParamMap != null && !multiParamMap.isEmpty()) {
			for(String key : multiParamMap.keySet()) {
				//客户端一般会传 key=be_data 参数数据 和 key=photo的 图片数据，暂时只支持一张图片，这里会有坑。
				if(!KEY_UPLOAD_FILE_APP_REQUEST.equalsIgnoreCase(key)) { 
					file = multiParamMap.get(key);
					break;
				}
			}
		}
		return file == null ? null : file.get();
	}
	
}
