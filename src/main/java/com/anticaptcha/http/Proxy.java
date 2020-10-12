package com.anticaptcha.http;

import com.anticaptcha.api.NoCaptcha;

public class Proxy {
    private NoCaptcha.ProxyTypeOption proxyType;
    private String proxyAddress;
    private int proxyPort;
    private String proxyLogin;
    private String proxyPassword;
    private String userAgent;


    public NoCaptcha.ProxyTypeOption getProxyType() {
        return proxyType;
    }

    public void setProxyType(NoCaptcha.ProxyTypeOption proxyType) {
        this.proxyType = proxyType;
    }

    public String getProxyAddress() {
        return proxyAddress;
    }

    public void setProxyAddress(String proxyAddress) {
        this.proxyAddress = proxyAddress;
    }

    public int getProxyPort() {
        return proxyPort;
    }

    public void setProxyPort(int proxyPort) {
        this.proxyPort = proxyPort;
    }

    public String getProxyLogin() {
        return proxyLogin;
    }

    public void setProxyLogin(String proxyLogin) {
        this.proxyLogin = proxyLogin;
    }

    public String getProxyPassword() {
        return proxyPassword;
    }

    public void setProxyPassword(String proxyPassword) {
        this.proxyPassword = proxyPassword;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }
}
