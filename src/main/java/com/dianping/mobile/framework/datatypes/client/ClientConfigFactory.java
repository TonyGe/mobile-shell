/**
 * 
 */
package com.dianping.mobile.framework.datatypes.client;

import java.util.HashMap;
import java.util.Map;

import com.dianping.mobile.base.datatypes.enums.ClientType;

/**
 * @author kewen.yao
 *
 */
public class ClientConfigFactory {
	
	private static final Map<ClientType, ClientConfig> map = new HashMap<ClientType, ClientConfig>();
	static {
		map.put(ClientType.MAINAPP_ANDROID, ClientConfig.Android);
		map.put(ClientType.MAINAPP_IPADHD, ClientConfig.Ipad);
		map.put(ClientType.MAINAPP_IPHONE, ClientConfig.Iphone);
		map.put(ClientType.MAINAPP_WIN8PAD, ClientConfig.WinPhone);
		map.put(ClientType.MAINAPP_WINPHONE, ClientConfig.WinPhone);
	}
	
	
	public static ClientConfig getInstance(ClientType client) {
		if(map.containsKey(client)) {
			return map.get(client);
		}
		return ClientConfig.Default;
	}
	
}
