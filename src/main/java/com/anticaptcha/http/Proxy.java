package com.anticaptcha.http;

import lombok.Data;

@Data
public class Proxy {
    /**
     * Type of the proxy
     * <p>
     * http - usual http/https proxy
     * https - try this only if "http" doesn't work (required by some custom proxy servers)
     * socks4 - socks4 proxy
     * socks5 - socks5 proxy
     */
    private ProxyTypeOption proxyType;
    private String proxyAddress;
    private int proxyPort;
    private String proxyLogin;
    private String proxyPassword;
    private String userAgent;


    /**
     * Type of the proxy
     * <p>
     * http - usual http/https proxy
     * https - try this only if "http" doesn't work (required by some custom proxy servers)
     * socks4 - socks4 proxy
     * socks5 - socks5 proxy
     */
    public enum ProxyTypeOption {
        /**
         * http - usual http/https proxy
         */
        HTTP,
        /**
         * try this only if "http" doesn't work (required by some custom proxy servers)
         */
        HTTPS,
        /**
         * socks4 proxy
         */
        SOCKS4,
        /**
         * socks5 proxy
         */
        SOCKS5
    }
}
