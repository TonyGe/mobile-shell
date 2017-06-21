/**
 *
 */
package com.dianping.mobile.framework.datatypes.client;


import com.dianping.mobile.core.enums.ClientType;

import java.util.HashMap;
import java.util.Map;

/**
 * @author kewen.yao
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
        if (map.containsKey(client)) {
            return map.get(client);
        }
        return ClientConfig.Default;
    }

}
