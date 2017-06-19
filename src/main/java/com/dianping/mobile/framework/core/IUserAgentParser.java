package com.dianping.mobile.framework.core;

import java.util.Set;
import java.util.regex.Pattern;

import com.dianping.mobile.base.datatypes.enums.ClientType;


public interface IUserAgentParser {

	Set<ClientType> getKeys();

	Pattern getPattern(ClientType key);

}
