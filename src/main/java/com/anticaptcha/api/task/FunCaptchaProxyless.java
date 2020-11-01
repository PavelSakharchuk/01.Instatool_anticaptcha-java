package com.anticaptcha.api.task;

import com.anticaptcha.api.CreateTaskAbstract;
import com.anticaptcha.api.TaskType;
import com.anticaptcha.helper.DebugHelper;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;

/**
 * <h2>FunCaptchaTaskProxyless: funcaptcha without proxy</h2>
 * <p>
 * This type of task solves funcaptcha.com puzzle in our workers browsers.
 * Your app submits website address, public key and receives submit token after task completion.
 * This type of task does not require proxy but requires customer's understanding of the workflow.
 * Customers must have enabled this type of tasks in their API Settings.
 * In case it is not enabled, API will return error with code 50 : ERROR_FUNCAPTCHA_NOT_ALLOWED .
 *
 * @see <a href="https://anticaptcha.atlassian.net/wiki/spaces/API/pages/325877766/FunCaptchaTaskProxyless+-+funcaptcha+without+proxy">https://anticaptcha.atlassian.net</a>
 */
@Setter
@Accessors(chain = true)
public class FunCaptchaProxyless extends CreateTaskAbstract {
    /**
     * Defines type of the task.
     */
    private final String type = TaskType.FUN_CAPTCHA_TASK_PROXYLESS.getType();
    /**
     * Address of target web page
     */
    private final String websiteUrl;
    /**
     * Funcaptcha public key
     * <p>
     * {@code <div id="funcaptcha" data-pkey="THIS_ONE"></div> }
     * <p>
     */
    private final String websitePublicKey;
    /**
     * Custom Funcaptcha subdomain from which widget JS is loaded.
     * Required for some complex cases, but most Funcaptcha integrations run without it.
     */
    private String funcaptchaApiJSSubdomain;
    /**
     * Additional parameter that may be required by Funcaptcha implementation.
     * Use this property to send "blob" value as a stringified array. See example how it may look like.
     * <p>
     * {@code {"\blob\":\"HERE_COMES_THE_blob_VALUE\"} }
     * <p>
     */
    private String data;


    public FunCaptchaProxyless(URL websiteUrl, String websitePublicKey) {
        this.websiteUrl = websiteUrl.toString();
        this.websitePublicKey = websitePublicKey;
    }


    @Override
    public JSONObject getPostData() {
        JSONObject postData = new JSONObject();
        try {
            postData.put("type", type);
            postData.put("websiteURL", websiteUrl);
            postData.put("websitePublicKey", websitePublicKey);
            postData.put("funcaptchaApiJSSubdomain", funcaptchaApiJSSubdomain);
            postData.put("data", data);
        } catch (JSONException e) {
            DebugHelper.out("JSON compilation error: " + e.getMessage(), DebugHelper.Type.ERROR);
            return null;
        }
        return postData;
    }
}
