package com.dianping.mobile.framework.clientrule.bean;

import com.dianping.mobile.base.datatypes.enums.Platform;
import com.dianping.mobile.base.datatypes.enums.Product;
import com.dianping.mobile.framework.annotation.MobileClientRule;
import com.dianping.mobile.framework.annotation.MobileClientRules;
import com.dianping.mobile.framework.annotation.MobileDo;
import com.dianping.mobile.framework.annotation.MobileDo.MobileField;

/**
 * @author zhongkai.zhao
 *         13-11-27 下午6:21
 */
@MobileDo(id = 0x0000)
public class OneDo {

    //默认Product是API
    @MobileClientRule(platforms = Platform.Android)
    @MobileField(key = 0x1234)
    private int field;

    @MobileClientRule(platforms = Platform.iPhone, products = Product.YELLOWPAGE, minVersion = "6.2")
    @MobileField(key = 0x1235)
    private int field2;

    @MobileClientRule(platforms = { Platform.Android, Platform.iPhone })
    @MobileField(key = 0x1236)
    private int field3;

    @MobileClientRules({
        @MobileClientRule(platforms = { Platform.Android, Platform.iPhone }, minVersion = "6.2"),
        @MobileClientRule(platforms = Platform.iPadHd, minVersion = "2.6")
    })
    @MobileField(key = 0x1237)
    private int field4;

    //MobileClientRules和MobileClientRule同时生效
    @MobileClientRules({
        //会生成4个ClientInfoRule
            //MAINAPP_ANDROID, MAINAPP_IPHONE
            //YPAPP_ANDROID, YPAPP_IPHONE
        @MobileClientRule(platforms = { Platform.Android, Platform.iPhone }, products = { Product.API, Product.YELLOWPAGE })
    })
    @MobileClientRule(platforms = Platform.iPadHd)
    @MobileField(key = 0x1238)
    private int field5;
}
