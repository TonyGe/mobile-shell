/**
 * 
 */
package com.dianping.mobile.framework.core;

import java.util.regex.Pattern;

import com.dianping.mobile.base.datatypes.enums.ClientType;
import com.dianping.mobile.base.datatypes.enums.Platform;
import com.dianping.mobile.base.datatypes.enums.Product;



/**
 * @author kewen.yao
 *
 */
public class DefaultUserAgentParser extends AbstractUserAgentParser {

		
	private static final Pattern USERAGENT_IPHONE_REGEX 	    = Pattern.compile("mapi\\s*([0-9\\.]+)\\s*\\(dpscope ([0-9\\.]+)(.*);(?:.*)\\)", Pattern.CASE_INSENSITIVE);
	private static final Pattern USERAGENT_ANDROID_REGEX        = Pattern.compile("mapi\\s*([0-9\\.]+)\\s*\\(com.dianping.v1 ([0-9\\.]+)(.*);(?:.*)\\)", Pattern.CASE_INSENSITIVE);
	private static final Pattern USERAGENT_SYMBIANS60V3_REGEX   = Pattern.compile("mapi\\s*([0-9\\.]+)\\s*\\(dianping_s60v3 ([0-9\\.]+)(.*);(?:.*)\\)", Pattern.CASE_INSENSITIVE);
	private static final Pattern USERAGENT_SYMBIANS60V5_REGEX   = Pattern.compile("mapi\\s*([0-9\\.]+)\\s*\\(dianping_s60v5 ([0-9\\.]+)(.*);(?:.*)\\)", Pattern.CASE_INSENSITIVE);
	private static final Pattern USERAGENT_SYMBIAN3_REGEX       = Pattern.compile("mapi\\s*([0-9\\.]+)\\s*\\(dianping_Symbian3 ([0-9\\.]+)(.*);(?:.*)\\)", Pattern.CASE_INSENSITIVE);
	private static final Pattern USERAGENT_KJAVA_REGEX          = Pattern.compile("mapi\\s*([0-9\\.]+)\\s*\\(com.dianping.kjava ([0-9\\.]+)(.*);(?:.*)\\)", Pattern.CASE_INSENSITIVE);
	private static final Pattern USERAGENT_WP_REGEX             = Pattern.compile("mapi\\s*([0-9\\.]+)\\s*\\(com.dianping.wp ([0-9\\.]+)(.*);(?:.*)\\)", Pattern.CASE_INSENSITIVE);
	private static final Pattern USERAGENT_BLACKBERRY_REGEX     = Pattern.compile("mapi\\s*([0-9\\.]+)\\s*\\(com.dianping.bb ([0-9\\.]+)(.*);(?:.*)\\)", Pattern.CASE_INSENSITIVE);	 
	private static final Pattern USERAGENT_BADA_REGEX           = Pattern.compile("mapi\\s*([0-9\\.]+)\\s*\\(bada ([0-9\\.]+)(.*);(?:.*)\\)", Pattern.CASE_INSENSITIVE);   
	private static final Pattern USERAGENT_IPADHD_REGEX         = Pattern.compile("mapi\\s*([0-9\\.]+)\\s*\\(dpscopehd ([0-9\\.]+)(.*);(?:.*)\\)",Pattern.CASE_INSENSITIVE);
	private static final Pattern USERAGENT_MEEGO_REGEX          = Pattern.compile("mapi\\s*([0-9\\.]+)\\s*\\(dianping_Meego ([0-9\\.]+)(.*);(?:.*)\\)",Pattern.CASE_INSENSITIVE);
	private static final Pattern USERAGENT_WIN8_REGEX           = Pattern.compile("mapi\\s*([0-9\\.]+)\\s*\\(dianping.mobile.win8 ([0-9\\.]+)(.*);(?:.*)\\)", Pattern.CASE_INSENSITIVE);
	private static final Pattern YELLOWPAGE_ANDROID_IN_REGEX    = Pattern.compile("mapi\\s*([0-9\\.]+)\\s*\\(com.international.nb ([0-9\\.]+)(.*);\\s*(?:.*)\\)",Pattern.CASE_INSENSITIVE);
	
	private static final Pattern USERAGENT_YP_IPHONE_REGEX      = Pattern.compile("mapi\\s*([0-9\\.]+)\\s*\\(aroundme ([0-9\\.]+)(.*);\\s*(?:.*)\\)", Pattern.CASE_INSENSITIVE);
	private static final Pattern USERAGENT_YP_ANDROID_REGEX     = Pattern.compile("mapi\\s*([0-9\\.]+)\\s*\\(com.dianping.am ([0-9\\.]+)(.*);\\s*(?:.*)\\)", Pattern.CASE_INSENSITIVE);
	
	private static final Pattern USERAGENT_TUAN_IPHONE_REGEX    = Pattern.compile("mapi\\s*([0-9\\.]+)\\s*\\(dptuan ([0-9\\.]+)(.*);\\s*(?:.*)\\)", Pattern.CASE_INSENSITIVE);
	private static final Pattern USERAGENT_TUAN_ANDROID_REGEX   = Pattern.compile("mapi\\s*([0-9\\.]+)\\s*\\(com.dianping.t ([0-9\\.]+)(.*);\\s*(?:.*)\\)", Pattern.CASE_INSENSITIVE);
	
	private static final Pattern USERAGENT_WAIMAI_IPHONE_REGEX    = Pattern.compile("mapi\\s*([0-9\\.]+)\\s*\\(dpwaimai ([0-9\\.]+)(.*);\\s*(?:.*)\\)", Pattern.CASE_INSENSITIVE);
	private static final Pattern USERAGENT_WAIMAI_ANDROID_REGEX   = Pattern.compile("mapi\\s*([0-9\\.]+)\\s*\\(com.dianping.wm ([0-9\\.]+)(.*);\\s*(?:.*)\\)", Pattern.CASE_INSENSITIVE);
	
	private static final Pattern USERAGENT_APOLLOCRM_IPHONE_REGEX    = Pattern.compile("mapi\\s*([0-9\\.]+)\\s*\\(dpappolo ([0-9\\.]+)(.*);\\s*(?:.*)\\)", Pattern.CASE_INSENSITIVE);
	private static final Pattern USERAGENT_APOLLOCRM_ANDROID_REGEX   = Pattern.compile("mapi\\s*([0-9\\.]+)\\s*\\(com.dianping.crm ([0-9\\.]+)(.*);\\s*(?:.*)\\)", Pattern.CASE_INSENSITIVE);
	
	//商家平台App-点评管家
	private static final Pattern USERAGENT_DPMERCHANT_IPHONE_REGEX    = Pattern.compile("mapi\\s*([0-9\\.]+)\\s*\\(dpmerchant ([0-9\\.]+)(.*);\\s*(?:.*)\\)", Pattern.CASE_INSENSITIVE);
	private static final Pattern USERAGENT_DPMERCHANT_ANDROID_REGEX   = Pattern.compile("mapi\\s*([0-9\\.]+)\\s*\\(com.dianping.dpmerchant ([0-9\\.]+)(.*);\\s*(?:.*)\\)", Pattern.CASE_INSENSITIVE);

    private static final Pattern USERAGENT_MERCHANTPOS_IPHONE_REGEX    = Pattern.compile("mapi\\s*([0-9\\.]+)\\s*\\(merchant.pos ([0-9\\.]+)(.*);\\s*(?:.*)\\)", Pattern.CASE_INSENSITIVE);
    private static final Pattern USERAGENT_MERCHANTPOS_ANDROID_REGEX   = Pattern.compile("mapi\\s*([0-9\\.]+)\\s*\\(com.dianping.merchant.pos ([0-9\\.]+)(.*);\\s*(?:.*)\\)", Pattern.CASE_INSENSITIVE);

    public DefaultUserAgentParser() {
		patternMap.put(ClientType.MAINAPP_IPHONE, USERAGENT_IPHONE_REGEX);
		patternMap.put(ClientType.MAINAPP_ANDROID, USERAGENT_ANDROID_REGEX);
		patternMap.put(new ClientType(Platform.SymbianS60v3, Product.API), USERAGENT_SYMBIANS60V3_REGEX);
		patternMap.put(new ClientType(Platform.SymbianS60v5, Product.API), USERAGENT_SYMBIANS60V5_REGEX);
		patternMap.put(new ClientType(Platform.Symbian3, Product.API), USERAGENT_SYMBIAN3_REGEX);
		patternMap.put(new ClientType(Platform.KJava, Product.API), USERAGENT_KJAVA_REGEX);
		patternMap.put(ClientType.MAINAPP_WINPHONE, USERAGENT_WP_REGEX);
		patternMap.put(new ClientType(Platform.BlackBerry, Product.API), USERAGENT_BLACKBERRY_REGEX);
		patternMap.put(new ClientType(Platform.Bada, Product.API), USERAGENT_BADA_REGEX);
		patternMap.put(ClientType.MAINAPP_IPADHD, USERAGENT_IPADHD_REGEX);
		patternMap.put(new ClientType(Platform.Meego, Product.API), USERAGENT_MEEGO_REGEX);
		patternMap.put(ClientType.MAINAPP_WIN8PAD, USERAGENT_WIN8_REGEX);
		
		patternMap.put(ClientType.YPAPP_IPHONE, USERAGENT_YP_IPHONE_REGEX);
		patternMap.put(ClientType.YPAPP_ANDROID, USERAGENT_YP_ANDROID_REGEX);
		patternMap.put(ClientType.YPAPP_IN_ANDROID, YELLOWPAGE_ANDROID_IN_REGEX);
		
		patternMap.put(ClientType.TUANAPP_IPHONE, USERAGENT_TUAN_IPHONE_REGEX);
		patternMap.put(ClientType.TUANAPP_ANDROID, USERAGENT_TUAN_ANDROID_REGEX);
		
		patternMap.put(ClientType.WMAPP_IPHONE, USERAGENT_WAIMAI_IPHONE_REGEX);
		patternMap.put(ClientType.WMAPP_ANDROID, USERAGENT_WAIMAI_ANDROID_REGEX);
		
		patternMap.put(ClientType.CRMAPP_IPHONE, USERAGENT_APOLLOCRM_IPHONE_REGEX);
		patternMap.put(ClientType.CRMAPP_ANDROID, USERAGENT_APOLLOCRM_ANDROID_REGEX);

        // TODO
		// patternMap.put(ClientType.SHOPAPP_IPHONE, USERAGENT_DPMERCHANT_IPHONE_REGEX);
        // patternMap.put(ClientType.SHOPAPP_ANDROID, USERAGENT_DPMERCHANT_ANDROID_REGEX);

        // 点评管家
        patternMap.put(ClientType.DPMERCHANT_IPHONE, USERAGENT_DPMERCHANT_IPHONE_REGEX);
        patternMap.put(ClientType.DPMERCHANT_ANDROID, USERAGENT_DPMERCHANT_ANDROID_REGEX);

        // 商户pos
        patternMap.put(ClientType.MERCHANTPOS_IPHONE, USERAGENT_MERCHANTPOS_IPHONE_REGEX);
        patternMap.put(ClientType.MERCHANTPOS_ANDROID, USERAGENT_MERCHANTPOS_ANDROID_REGEX);
	}

}