package com.anticaptcha.api.task;

import com.anticaptcha.api.AnticaptchaAbstract;
import com.anticaptcha.api.TaskType;
import com.anticaptcha.helper.DebugHelper;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.Arrays;

/**
 * <h2>NoCaptchaTaskProxyless : Google Recaptcha puzzle solving without proxies</h2>
 * <p>
 * This object of type Task contains data required to solve Google Recaptcha on a worker's computer.
 * This task will be executed by our service using our own proxy servers and/or workers' IP addresses.
 *
 * @see <a href="https://anticaptcha.atlassian.net/wiki/spaces/API/pages/9666606/NoCaptchaTaskProxyless+Google+Recaptcha+puzzle+solving+without+proxies">https://anticaptcha.atlassian.net</a>
 */
@Setter
@Accessors(chain = true)
public class RecaptchaV3Proxyless extends AnticaptchaAbstract {
    /**
     * Defines type of the task.
     */
    private final String type = TaskType.RECAPTCHA_V3_TASK_PROXYLESS.getType();
    /**
     * Address of target web page
     */
    private final String websiteUrl;
    /**
     * Recaptcha website key from html source
     * https://www.google.com/recaptcha/api.js?render=THIS_ONE
     */
    private final String websiteKey;
    /**
     * Filters a worker with corresponding score. Can be one of the following:
     * 0.3 (default)
     * 0.7
     * 0.9
     */
    private final Double minScore;
    /**
     * Widget action value. Website owner defines what user is doing on the page through this parameter.
     * <p>
     * Example:
     * <p>
     * {@code grecaptcha.execute('site_key', {action:'login_test'}). }
     * <p>
     */
    private String pageAction;


    public RecaptchaV3Proxyless(URL websiteUrl, String websiteKey) {
        this.websiteUrl = websiteUrl.toString();
        this.websiteKey = websiteKey;
        this.minScore = 0.3;
    }

    public RecaptchaV3Proxyless(URL websiteUrl, String websiteKey, Double minScore) {
        this.websiteUrl = websiteUrl.toString();
        this.websiteKey = websiteKey;
        this.minScore = minScore;
        if (!Arrays.asList(0.3, 0.7, 0.9).contains(this.minScore)) {
            DebugHelper.out("minScore must be one of these: 0.3, 0.7, 0.9; you passed " + minScore + "; 0.3 will be set", DebugHelper.Type.ERROR);
        }
    }


    @Override
    public JSONObject getPostData() {
        JSONObject postData = new JSONObject();

        try {
            postData.put("type", type);
            postData.put("websiteURL", websiteUrl);
            postData.put("websiteKey", websiteKey);
            postData.put("minScore", minScore);
            postData.put("pageAction", pageAction);
        } catch (JSONException e) {
            DebugHelper.out("JSON compilation error: " + e.getMessage(), DebugHelper.Type.ERROR);

            return null;
        }

        return postData;
    }
}
