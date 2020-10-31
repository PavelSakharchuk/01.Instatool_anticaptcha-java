package com.anticaptcha.api.task;

import com.anticaptcha.api.AnticaptchaAbstract;
import com.anticaptcha.api.TaskType;
import com.anticaptcha.helper.DebugHelper;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;

/**
 * <h2>NoCaptchaTaskProxyless: Google Recaptcha puzzle solving without proxies</h2>
 * <p>
 * This object of type Task contains data required to solve Google Recaptcha on a worker's computer.
 * This task will be executed by our service using our own proxy servers and/or workers' IP addresses.
 * Costs for this task is 10% higher than NoCaptchaTask,
 * because the problem of overcoming per-IP limits falls on us.
 * As may you know, number of daily Recaptcha solutions is limited for each IP address
 * and we'll have to deal with proxies ourselves.
 *
 * @see <a href="https://anticaptcha.atlassian.net/wiki/spaces/API/pages/9666606/NoCaptchaTaskProxyless+Google+Recaptcha+puzzle+solving+without+proxies">https://anticaptcha.atlassian.net</a>
 */
@Setter
@Accessors(chain = true)
public class NoCaptchaProxyless extends AnticaptchaAbstract {
    /**
     * Defines type of the task.
     */
    private final String type = TaskType.NO_CAPTCHA_TASK_PROXYLESS.getType();
    /**
     * Address of target web page
     */
    private final String websiteUrl;
    /**
     * Recaptcha website key
     * <p>
     * {@code <div class="g-recaptcha" data-sitekey="THAT_ONE"></div> }
     * <p>
     * Learn how to retrieve it when it is not included in HTML.
     */
    private final String websiteKey;
    /**
     * Secret token for previous version of Recaptcha (now deprecated).
     * In most cases websites use newer version and this token is not required.
     *
     * @see <a href="https://anticaptcha.atlassian.net/wiki/spaces/API/pages/9666606/NoCaptchaTaskProxyless+Google+Recaptcha+puzzle+solving+without+proxies">More information</a>
     */
    private String websiteSToken;
    /**
     * Some custom implementations may contain additional "data-s" parameter in Recaptcha div,
     * which is in fact a one-time token and must be grabbed every time you want to solve a Recaptcha.
     * <p>
     * {@code <div class="g-recaptcha" data-sitekey="some sitekey" data-s="THIS_ONE"></div> }
     * <p>
     * If you're solving Recaptcha at google.com websites and using browser-less approach,
     * then use "cookies" in getTaskResult response.
     */
    private String recaptchaDataSValue;
    /**
     * Specify that Recaptcha is invisible. This will render an appropriate widget for our workers.
     * Note that this parameter is not required, as system detects invisible sitekeys automatically, but needs several 10s of recaptchas for automated training and analysis.
     */
    private Boolean isInvisible;


    public NoCaptchaProxyless(URL websiteUrl, String websiteKey) {
        this.websiteUrl = websiteUrl.toString();
        this.websiteKey = websiteKey;
    }


    @Override
    public JSONObject getPostData() {
        JSONObject postData = new JSONObject();
        try {
            postData.put("type", type);
            postData.put("websiteURL", websiteUrl);
            postData.put("websiteKey", websiteKey);
            postData.put("websiteSToken", websiteSToken);
            postData.put("recaptchaDataSValue", recaptchaDataSValue);
            postData.put("isInvisible", isInvisible);
        } catch (JSONException e) {
            DebugHelper.out("JSON compilation error: " + e.getMessage(), DebugHelper.Type.ERROR);
            return null;
        }
        return postData;
    }
}
