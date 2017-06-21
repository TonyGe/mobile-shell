package com.dianping.mobile.framework.util;


import com.dianping.mobile.core.datatypes.ClientInfoRule;
import com.dianping.mobile.core.enums.ClientType;
import com.dianping.mobile.framework.annotation.Action;
import com.dianping.mobile.framework.annotation.MobileClientRule;
import com.dianping.mobile.framework.annotation.MobileClientRules;
import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: zhongkai.zhao
 * date: 13-9-16
 * time: 下午2:18
 */
public final class ClientRuleUtil {

    private static final Map<Field, List<ClientInfoRule>> fieldClientInfoRuleCache = new ConcurrentHashMap<Field, List<ClientInfoRule>>();

    private ClientRuleUtil() {
    }

    public static List<ClientInfoRule> convertToClientInfoRule(MobileClientRule clientRuleAnnotation) {
        List<ClientInfoRule> result = new ArrayList<ClientInfoRule>();
        for (int i = 0, l = clientRuleAnnotation.platforms().length; i < l; ++i) {
            for (int j = 0, m = clientRuleAnnotation.products().length; j < m; ++j) {
                result.add(new ClientInfoRule(
                        new ClientType(clientRuleAnnotation.platforms()[i], clientRuleAnnotation.products()[j]),
                        StringUtils.isEmpty(clientRuleAnnotation.maxVersion()) ? null : clientRuleAnnotation.maxVersion(),
                        StringUtils.isEmpty(clientRuleAnnotation.minVersion()) ? null : clientRuleAnnotation.minVersion()
                ));
            }
        }
        return result;
    }

    public static List<ClientInfoRule> convertToClientInfoRule(MobileClientRules clientRulesAnnotation) {
        List<ClientInfoRule> result = new ArrayList<ClientInfoRule>();
        for (int i = 0, l = clientRulesAnnotation.value().length; i < l; ++i) {
            result.addAll(convertToClientInfoRule(clientRulesAnnotation.value()[i]));
        }
        return result;
    }

    private static List<ClientInfoRule> getClientInfoRules(MobileClientRule clientRule, MobileClientRules clientRules) {
        List<ClientInfoRule> rules = new ArrayList<ClientInfoRule>();
        if (clientRule != null) {
            rules.addAll(convertToClientInfoRule(clientRule));
        }
        if (clientRules != null) {
            rules.addAll(convertToClientInfoRule(clientRules));
        }
        return rules;
    }

    public static List<ClientInfoRule> getClientInfoRules(Field field) {
        List<ClientInfoRule> result = fieldClientInfoRuleCache.get(field);
        if (result == null) {
            MobileClientRule clientRuleAnnotation = field.getAnnotation(MobileClientRule.class);
            MobileClientRules clientRulesAnnotation = field.getAnnotation(MobileClientRules.class);
            result = Collections.unmodifiableList(getClientInfoRules(clientRuleAnnotation, clientRulesAnnotation));
            fieldClientInfoRuleCache.put(field, result);
        }
        return result;
    }

    //private static final Map<Class, List<ClientInfoRule>> classClientInfoRuleCache = new ConcurrentHashMap<Class, List<ClientInfoRule>>();

    public static List<ClientInfoRule> getClientInfoRules(Action action) {
        MobileClientRule[] clientRuleAnnotations = action.mobileClientRule();
        List<ClientInfoRule> result = new ArrayList<ClientInfoRule>();
        for (MobileClientRule MobileClientRule : clientRuleAnnotations) {
            result.addAll(convertToClientInfoRule(MobileClientRule));
        }
        return result;
    }
}
