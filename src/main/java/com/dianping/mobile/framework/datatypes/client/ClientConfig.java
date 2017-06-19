/**
 * 
 */
package com.dianping.mobile.framework.datatypes.client;

/**
 * @author kewen.yao
 *
 */
public enum ClientConfig {
	
	Default(25),
	
	Android(25),
	
	Iphone(25),
	
	Ipad(25),
	
	WinPhone(25);
	
	public final int limit;
	
	private ClientConfig(int limit) {
		this.limit = limit;
	}
	
}
