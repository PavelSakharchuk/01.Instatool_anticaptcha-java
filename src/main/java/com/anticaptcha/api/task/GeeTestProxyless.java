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
 * <h2>GeeTestTaskProxyless: captcha from geetest.com without proxy</h2>
 * <p>
 * This type of task solves GeeTest captcha in our workers browsers.
 * Your app submits website address, gt key, challenge key and receives 3 parameters after task completion.
 *
 * @see <a href="https://anticaptcha.atlassian.net/wiki/spaces/API/pages/416972814/GeeTestTaskProxyless+-+captcha+from+geetest.com+without+proxy">https://anticaptcha.atlassian.net</a>
 */
@Setter
@Accessors(chain = true)
public class GeeTestProxyless extends CreateTaskAbstract {
    /**
     * Defines type of the task.
     */
    private final String type = TaskType.GEE_TEST_TASK_PROXYLESS.getType();
    /**
     * Address of target web page
     */
    private final String websiteUrl;
    /**
     * The domain key
     */
    private final String gt;
    /**
     * Changing token key.
     * Make sure to grab fresh one for each captcha,
     * otherwise you will be charged for error task.
     */
    private final String challenge;
    /**
     * Optional API subdomain. May be required for some installations.
     * Look into the source code and find function initGeetest.
     * If one of it's parameters containts api_server, then send the value to our API.
     */
    private String geetestApiServerSubdomain;
    /**
     * Required for some custom implementations.
     * Send an quotation-escaped string of additional additional libraries.
     * These should be can be obtained in debugger.
     * Place a breakpoint before initGeetest function call.
     */
    private String geetestGetLib;


    public GeeTestProxyless(URL websiteUrl, String gt, String challenge) {
        this.websiteUrl = websiteUrl.toString();
        this.gt = gt;
        this.challenge = challenge;
    }


    @Override
    public JSONObject getPostData() {
        JSONObject postData = new JSONObject();

        try {
            postData.put("type", type);
            postData.put("websiteURL", websiteUrl);
            postData.put("gt", gt);
            postData.put("challenge", challenge);
            postData.put("geetestGetLib", geetestGetLib);

            if (geetestApiServerSubdomain != null && geetestApiServerSubdomain.length() > 0) {
                postData.put("geetestApiServerSubdomain", geetestApiServerSubdomain);
            }
        } catch (JSONException e) {
            DebugHelper.out("JSON compilation error: " + e.getMessage(), DebugHelper.Type.ERROR);

            return null;
        }

        return postData;
    }
}
