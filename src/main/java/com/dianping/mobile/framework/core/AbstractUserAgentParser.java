package com.dianping.mobile.framework.core;

import com.dianping.mobile.core.enums.ClientType;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;


public abstract class AbstractUserAgentParser implements IUserAgentParser {

    protected Map<ClientType, Pattern> patternMap = new HashMap<ClientType, Pattern>();

    @Override
    public final Set<ClientType> getKeys() {
        return patternMap.keySet();
    }

    @Override
    public final Pattern getPattern(ClientType key) {
        return patternMap.get(key);
    }

}
