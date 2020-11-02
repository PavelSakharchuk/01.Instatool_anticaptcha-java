package com.anticaptcha.api;

import com.anticaptcha.Config;
import com.anticaptcha.apiresponse.TaskResultResponse;
import com.anticaptcha.helper.DebugHelper;
import com.anticaptcha.helper.HttpHelper;
import com.anticaptcha.helper.JsonHelper;
import com.anticaptcha.helper.StringHelper;
import com.anticaptcha.http.HttpRequest;
import lombok.Data;
import org.json.JSONObject;

/**
 * Basic Class of AntiCaptcha API version 2.0
 *
 * @see <a href="https://anticaptcha.atlassian.net/wiki/spaces/API/pages/196635/Documentation+in+English">https://developer.aliyun.com</a>
 */
@Data
public abstract class AnticaptchaAbstract implements IAnticaptchaTaskProtocol {
    /**
     * Client account key, can be found here
     *
     * @see <a href="https://anti-captcha.com/panel/settings/account">https://anti-captcha.com</a>
     */
    protected String clientKey;
    /**
     * anti-captcha api host
     */
    protected String host = "api.anti-captcha.com";
    /**
     * Task Response
     */
    protected TaskResultResponse taskInfo;
    /**
     * JSON, Response or API error message
     */
    protected String errorMessage;
    /**
     * Protocol type: http, https.
     */
    private SchemeType scheme = SchemeType.HTTPS;


    public AnticaptchaAbstract() {
        this.clientKey = Config.INSTANCE.getKey();
    }


    @SuppressWarnings("WeakerAccess")
    public String getErrorMessage() {
        return errorMessage == null ? "no error message" : errorMessage;
    }

    protected JSONObject jsonPostRequest(ApiMethod methodName, JSONObject jsonPostData) {

        String url = scheme + "://" + host + "/" + StringHelper.toCamelCase(methodName.toString());
        HttpRequest request = new HttpRequest(url);
        request.setRawPost(JsonHelper.asString(jsonPostData));
        request.addHeader("Content-Type", "application/json; charset=utf-8");
        request.addHeader("Accept", "application/json");
        request.setTimeout(30_000);

        String rawJson;

        try {
            rawJson = HttpHelper.download(request).getBody();
        } catch (Exception e) {
            errorMessage = e.getMessage();
            DebugHelper.out("HTTP problem: " + e.getMessage(), DebugHelper.Type.ERROR);

            return null;
        }

        try {
            return new JSONObject(rawJson);
        } catch (Exception e) {
            errorMessage = e.getMessage();
            DebugHelper.out("JSON parse problem: " + e.getMessage(), DebugHelper.Type.ERROR);

            return null;
        }
    }


    private enum SchemeType {
        HTTP,
        HTTPS
    }

    protected enum ApiMethod {
        CREATE_TASK,
        GET_TASK_RESULT,
        GET_BALANCE
    }
}
