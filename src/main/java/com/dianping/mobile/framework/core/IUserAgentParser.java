package com.dianping.mobile.framework.core;

import com.dianping.mobile.core.enums.ClientType;

import java.util.Set;
import java.util.regex.Pattern;


public interface IUserAgentParser {

    Set<ClientType> getKeys();

    Pattern getPattern(ClientType key);

}
