package com.dianping.mobile.framework.clientrule.bean;

import com.dianping.mobile.base.datatypes.enums.Platform;
import com.dianping.mobile.framework.annotation.MobileClientRule;
import com.dianping.mobile.framework.annotation.MobileDo;

/**
 * @author zhongkai.zhao
 *         13-11-28 下午3:03
 */
@MobileDo(id = 0x0000)
public class ThreeDo {

    @MobileClientRule(platforms = Platform.Android)
    @MobileDo.MobileField(key = 0x1234)
    private int field;
}
