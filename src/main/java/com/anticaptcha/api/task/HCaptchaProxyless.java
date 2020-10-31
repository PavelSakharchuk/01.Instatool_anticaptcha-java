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
 * <h2>HCaptchaTaskProxyless : hCaptcha puzzle solving without proxy</h2>
 * <p>
 * HCaptcha devs call their captcha "a drop-in replacement for Recaptcha".
 * We tried to create same thing in our API,
 * so task properties are absolutely the same except type.
 *
 * @see <a href="https://anticaptcha.atlassian.net/wiki/spaces/API/pages/834502676/HCaptchaTaskProxyless+hCaptcha+puzzle+solving+without+proxy">https://anticaptcha.atlassian.net</a>
 */
@Setter
@Accessors(chain = true)
public class HCaptchaProxyless extends AnticaptchaAbstract {
    /**
     * Defines type of the task.
     */
    private final String type = TaskType.H_CAPTCHA_TASK_PROXYLESS.getType();
    /**
     * Address of target web page
     */
    private final String websiteUrl;
    /**
     * hCaptcha website key
     */
    private final String websiteKey;


    public HCaptchaProxyless(URL websiteUrl, String websiteKey) {
        this.websiteUrl = websiteUrl.toString();
        this.websiteKey = websiteKey;
    }


    public JSONObject getPostData() {
        JSONObject postData = new JSONObject();

        try {
            postData.put("type", type);
            postData.put("websiteURL", websiteUrl);
            postData.put("websiteKey", websiteKey);
        } catch (JSONException e) {
            DebugHelper.out("JSON compilation error: " + e.getMessage(), DebugHelper.Type.ERROR);

            return null;
        }

        return postData;
    }
}
