package com.dianping.mobile.framework.datatypes;

public class MobileHeader {

    private String userAgent;
    private String token;
    private String deviceId;
    private String newToken;
    /**
     * 6.1开始 使用dpid
     */
    private String dpid;
    private String wifi; // 6.5 wifi
    private String uuid;

    public MobileHeader() {

    }

    /**
     * read only property no setter method
     *
     * @param userAgent
     * @param token
     * @param deviceId
     */
    public MobileHeader(String userAgent, String token, String deviceId, String newToken, String dpid, String wifi, String uuid) {
        this.userAgent = userAgent;
        this.token = token;
        this.deviceId = deviceId;
        this.newToken = newToken;
        this.dpid = dpid;
        this.wifi = wifi;
        this.uuid = uuid;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public String getToken() {
        return token;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getNewToken() {
        return newToken;
    }

    public String getDpid() {
        return dpid;
    }

    public String getWifi() {
        return wifi;
    }

    public void setWifi(String wifi) {
        this.wifi = wifi;
    }

    public String getUuid() {
        return uuid;
    }
}