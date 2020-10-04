package com.anticaptcha.api;

import com.anticaptcha.apiresponse.TaskResultResponse;
import com.anticaptcha.helper.DebugHelper;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;

public class HCaptchaProxyless extends AnticaptchaAbstract implements IAnticaptchaTaskProtocol {
    URL websiteUrl;
    String websiteKey;

    public JSONObject getPostData() {
        JSONObject postData = new JSONObject();

        try {
            postData.put("type", "HCaptchaTaskProxyless");
            postData.put("websiteURL", websiteUrl.toString());
            postData.put("websiteKey", websiteKey);
        } catch (JSONException e) {
            DebugHelper.out("JSON compilation error: " + e.getMessage(), DebugHelper.Type.ERROR);

            return null;
        }

        return postData;
    }

    @Override
    public TaskResultResponse.SolutionData getTaskSolution() {
        return taskInfo.getSolution();
    }

    @SuppressWarnings("unused")
    public void setWebsiteUrl(URL websiteUrl) {
        this.websiteUrl = websiteUrl;
    }

    @SuppressWarnings("unused")
    public void setWebsiteKey(String websiteKey) {
        this.websiteKey = websiteKey;
    }
}
