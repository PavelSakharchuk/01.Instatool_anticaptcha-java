package com.anticaptcha.api.task;

import com.anticaptcha.api.TaskType;
import com.anticaptcha.helper.DebugHelper;
import com.anticaptcha.http.Proxy;
import lombok.Setter;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;

/**
 * <h2>NoCaptchaTask : Google Recaptcha puzzle solving</h2>
 * <p>
 * This object of type Task contains data required to solve Google Recaptcha on a worker's computer.
 * To provide solid universality for solving this type of task
 * we have reproduce every piece of environment used for an automation task you plan to complete.
 *
 * @see <a href="https://anticaptcha.atlassian.net/wiki/spaces/API/pages/5079089/NoCaptchaTask+Google+Recaptcha+puzzle+solving">https://anticaptcha.atlassian.net</a>
 */
@Setter
public class NoCaptcha extends NoCaptchaProxyless {
    /**
     * Defines type of the task.
     */
    private final String type = TaskType.NO_CAPTCHA_TASK.getType();
    /**
     * Type of the proxy
     * <p>
     * http - usual http/https proxy
     * https - try this only if "http" doesn't work (required by some custom proxy servers)
     * socks4 - socks4 proxy
     * socks5 - socks5 proxy
     */
    private final String proxyType;
    /**
     * Proxy IP address ipv4/ipv6. Not allowed to use:
     * <p>
     * - host names instead of IPs
     * - transparent proxies (where client IP is visible)
     * - proxies from local networks (192.., 10.., 127...)
     */
    private final String proxyAddress;
    /**
     * Proxy port
     */
    private final Integer proxyPort;
    /**
     * Browser's User-Agent which is used in emulation.
     * It is required that you use a signature of a modern browser,
     * otherwise Google will ask you to "update your browser".
     */
    private final String userAgent;
    /**
     * Login for proxy which requires authorizaiton (basic)
     */
    private String proxyLogin;
    /**
     * Proxy password
     */
    private String proxyPassword;
    /**
     * Additional cookies which we must use during interaction with target page or Google.
     * Format: cookiename1=cookievalue1; cookiename2=cookievalue2
     */
    private String cookies;


    public NoCaptcha(URL websiteUrl, String websiteKey, Proxy proxy) {
        super(websiteUrl, websiteKey);
        this.proxyType = proxy.getProxyType().toString().toLowerCase();
        this.proxyAddress = proxy.getProxyAddress();
        this.proxyPort = proxy.getProxyPort();
        this.userAgent = proxy.getUserAgent();

        this.proxyLogin = proxy.getProxyLogin();
        this.proxyPassword = proxy.getProxyPassword();
    }


    @Override
    public JSONObject getPostData() {
        JSONObject postData = super.getPostData();

        if (proxyType == null || proxyPort == null || proxyPort < 1 || proxyPort > 65535
                || proxyAddress == null || proxyAddress.length() == 0) {
            DebugHelper.out("Proxy data is incorrect!", DebugHelper.Type.ERROR);

            return null;
        }

        try {
            postData.put("type", type);
            postData.put("proxyType", proxyType);
            postData.put("proxyAddress", proxyAddress);
            postData.put("proxyPort", proxyPort);
            postData.put("proxyLogin", proxyLogin);
            postData.put("proxyPassword", proxyPassword);
            postData.put("userAgent", userAgent);
            postData.put("cookies", cookies);
        } catch (JSONException e) {
            DebugHelper.out("JSON compilation error: " + e.getMessage(), DebugHelper.Type.ERROR);

            return null;
        }

        return postData;
    }
}
