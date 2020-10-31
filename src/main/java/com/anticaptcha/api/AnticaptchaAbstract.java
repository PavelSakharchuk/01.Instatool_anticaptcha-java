package com.anticaptcha.api;

import com.anticaptcha.Config;
import com.anticaptcha.apiresponse.CreateTaskResponse;
import com.anticaptcha.apiresponse.TaskResultResponse;
import com.anticaptcha.helper.DebugHelper;
import com.anticaptcha.helper.HttpHelper;
import com.anticaptcha.helper.JsonHelper;
import com.anticaptcha.helper.StringHelper;
import com.anticaptcha.http.HttpRequest;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

public abstract class AnticaptchaAbstract implements IAnticaptchaTaskProtocol {
    protected TaskResultResponse taskInfo;
    private String host = "api.anti-captcha.com";
    private SchemeType scheme = SchemeType.HTTPS;
    protected String errorMessage;
    private Integer taskId;
    protected String clientKey;


    public AnticaptchaAbstract() {
        setClientKey(Config.INSTANCE.getKey());
    }


    @SuppressWarnings("WeakerAccess")
    public void setClientKey(String clientKey_) {
        clientKey = clientKey_;
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

    @Override
    public abstract JSONObject getPostData();

    @SuppressWarnings("WeakerAccess")
    private Boolean createTask() {
        JSONObject taskJson = getPostData();
        // TODO: 21.10.2020: Need to add debug logger for taskJson
        if (taskJson == null) {
            DebugHelper.out("JSON error", DebugHelper.Type.ERROR);

            return false;
        }

        JSONObject jsonPostData = new JSONObject();

        try {
            jsonPostData.put("clientKey", clientKey);
            jsonPostData.put("task", taskJson);
        } catch (JSONException e) {
            errorMessage = e.getMessage();
            DebugHelper.out("JSON compilation error: " + e.getMessage(), DebugHelper.Type.ERROR);

            return false;
        }

        DebugHelper.out("Connecting to " + host, DebugHelper.Type.INFO);
        JSONObject postResult = jsonPostRequest(ApiMethod.CREATE_TASK, jsonPostData);

        if (postResult == null) {
            DebugHelper.out("API error", DebugHelper.Type.ERROR);

            return false;
        }

        CreateTaskResponse response = new CreateTaskResponse(postResult);

        if (response.getErrorId() == null || !response.getErrorId().equals(0)) {
            errorMessage = response.getErrorDescription();
            String errorId = response.getErrorId() == null ? "" : response.getErrorId().toString();

            DebugHelper.out(
                    "API error " + errorId + ": " + response.getErrorDescription(),
                    DebugHelper.Type.ERROR
            );

            return false;
        }

        if (response.getTaskId() == null) {
            DebugHelper.jsonFieldParseError("taskId", postResult);

            return false;
        }

        taskId = response.getTaskId();
        DebugHelper.out("Task ID: " + taskId, DebugHelper.Type.SUCCESS);

        return true;
    }

    @SuppressWarnings("WeakerAccess")
    public Boolean waitForResult(Integer maxSeconds, Integer currentSecond) throws InterruptedException {
        if (currentSecond >= maxSeconds) {
            DebugHelper.out("Time's out.", DebugHelper.Type.ERROR);

            return false;
        }

        if (currentSecond.equals(0)) {
            DebugHelper.out("Waiting for 3 seconds...", DebugHelper.Type.INFO);
            TimeUnit.SECONDS.sleep(3);
        } else {
            TimeUnit.SECONDS.sleep(1);
        }

        DebugHelper.out("Requesting the task status", DebugHelper.Type.INFO);
        JSONObject jsonPostData = new JSONObject();

        try {
            jsonPostData.put("clientKey", clientKey);
            jsonPostData.put("taskId", taskId);
        } catch (JSONException e) {
            errorMessage = e.getMessage();
            DebugHelper.out("JSON compilation error: " + e.getMessage(), DebugHelper.Type.ERROR);

            return false;
        }

        JSONObject postResult = jsonPostRequest(ApiMethod.GET_TASK_RESULT, jsonPostData);

        if (postResult == null) {
            DebugHelper.out("API error", DebugHelper.Type.ERROR);

            return false;
        }

        taskInfo = new TaskResultResponse(postResult);

        if (taskInfo.getErrorId() == null || !taskInfo.getErrorId().equals(0)) {
            errorMessage = taskInfo.getErrorDescription();
            String errorId = taskInfo.getErrorId() == null ? "" : taskInfo.getErrorId().toString();

            DebugHelper.out(
                    "API error " + errorId + ": " + errorMessage,
                    DebugHelper.Type.ERROR
            );

            return false;
        }

        TaskResultResponse.StatusType status = taskInfo.getStatus();
        TaskResultResponse.SolutionData solution = taskInfo.getSolution();

        if (status != null && status.equals(TaskResultResponse.StatusType.PROCESSING)) {
            DebugHelper.out("The task is still processing...", DebugHelper.Type.INFO);

            return waitForResult(maxSeconds, currentSecond + 1);
        } else if (status != null && status.equals(TaskResultResponse.StatusType.READY)) {
            if (solution.getGRecaptchaResponse() == null && solution.getText() == null && solution.getAnswers() == null && solution.getToken() == null && solution.getChallenge() == null && solution.getSeccode() == null && solution.getValidate() == null && solution.getCellNumbers().size() == 0) {
                DebugHelper.out("Got no 'solution' field from API", DebugHelper.Type.ERROR);

                return false;
            }

            DebugHelper.out("The task is complete!", DebugHelper.Type.SUCCESS);

            return true;
        }

        errorMessage = "An unknown API status, please update your software";
        DebugHelper.out(errorMessage, DebugHelper.Type.ERROR);

        return false;
    }

    @SuppressWarnings("WeakerAccess")
    private Boolean waitForResult() throws InterruptedException {
        return waitForResult(120, 0);
    }

    private Boolean waitForResult(Integer maxSeconds) throws InterruptedException {
        return waitForResult(maxSeconds, 0);
    }

    @Override
    public TaskResultResponse.SolutionData getTaskSolution() throws InterruptedException {
        if (!this.createTask()) {
            DebugHelper.out(
                    "API v2 send failed. " + this.getErrorMessage(),
                    DebugHelper.Type.ERROR
            );
        } else if (!this.waitForResult()) {
            DebugHelper.out("Could not solve the captcha.", DebugHelper.Type.ERROR);
        } else {
            DebugHelper.out("Result: " + taskInfo.getSolution(), DebugHelper.Type.SUCCESS);
        }

        return taskInfo.getSolution();
    }


    public enum ProxyTypeOption {
        HTTP,
        HTTPS,
        SOCKS4,
        SOCKS5
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
