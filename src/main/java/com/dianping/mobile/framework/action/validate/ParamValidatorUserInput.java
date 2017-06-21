/**
 *
 */
package com.dianping.mobile.framework.action.validate;

import com.dianping.mobile.framework.annotation.MobileRequest.Param;
import com.dianping.mobile.framework.base.datatypes.SimpleMsg;
import com.dianping.mobile.framework.datatypes.CommonMobileResponse;
import com.dianping.mobile.framework.datatypes.IMobileResponse;
import org.apache.commons.lang.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.Set;

/**
 * @author kewen.yao
 */
public class ParamValidatorUserInput implements ParamValidator {

    private static final SimpleMsg MSG_ERROR_INVALID_INPUT = new SimpleMsg("提示", "暂不支持您输入的符号，请重新输入!");
    public static final CommonMobileResponse RESPONSE_ERROR_INVALID_INPUT = new CommonMobileResponse(MSG_ERROR_INVALID_INPUT);

    private static final Set<String> PARAM_SET_USERINPUT = new HashSet<String>();

    static {
        PARAM_SET_USERINPUT.add("tips");
        PARAM_SET_USERINPUT.add("text");
        PARAM_SET_USERINPUT.add("content");
        PARAM_SET_USERINPUT.add("body");
    }

    /**
     * 检查用户输入肯定入库的字符串
     */
    @Override
    public IMobileResponse validate(Param param, Object value) {
        if (PARAM_SET_USERINPUT.contains(param.name())) {
            if (!StringUtils.isBlank((String) value)) {
                String text = xssFilter((String) value);
                if (!encodeCheck(text)) {
                    return RESPONSE_ERROR_INVALID_INPUT;
                }
            }
        }
        return null;
    }

    /**
     * 入库的字符串必须先做xssFilter
     *
     * @param ori
     * @return
     */
    private String xssFilter(String ori) {
        return ori.replaceAll("\\<", "\\〈").replaceAll("\\>", "\\〉").trim();
    }

    /**
     * mysql 5.1 doesn't support 4 bytes utf-8 char we must validate user input string
     */
    private boolean encodeCheck(String ori) {
        try {
            byte[] bytes = ori.getBytes("utf-8");
            ByteArrayOutputStream bos = new ByteArrayOutputStream(bytes.length);
            for (int i = 0; i < bytes.length; i++) {
                if ((bytes[i] & 0xf8) == 0xf0) {
                    i += 3;
                    return false;
                } else {
                    bos.write(bytes[i]);
                }
            }
            if (bos.size() != bytes.length) {
                return false;
            }
        } catch (UnsupportedEncodingException e) {
        }
        return true;
    }
}
