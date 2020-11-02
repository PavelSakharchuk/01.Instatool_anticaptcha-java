package com.anticaptcha.api;

import com.anticaptcha.apiresponse.CreateTaskResponse;
import com.anticaptcha.apiresponse.TaskResultResponse;
import com.anticaptcha.helper.DebugHelper;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.concurrent.TimeUnit;

/**
 * <h2>createTask : captcha task creating</h2>
 * <p>
 * This method creates a task for solving selected captcha type.
 * Method address: https://api.anti-captcha.com/createTask
 * Request format: JSON POST
 *
 * @see <a href="https://anticaptcha.atlassian.net/wiki/spaces/API/pages/5079073/createTask+captcha+task+creating">https://developer.aliyun.com</a>
 */
@Setter
@Accessors(chain = true)
public abstract class CreateTaskAbstract extends AnticaptchaAbstract {
    /**
     * Task data. See list of available object descriptions here.
     */
    private JSONObject task;
    /**
     * ID of your application from out AppCenter,
     * this is required to earn 10% from clients spendings which use your application.
     *
     * @see <a href="https://anti-captcha.com/panel/tools/appcenter#developer">https://anti-captcha.com</a>
     */
    private Integer softId;
    /**
     * Sets workers pool language. At the moment the following language pools are available:
     * <p>
     * en (default) : English language queue;
     * rn  : group of countries: Russia, Ukraine, Belarus, Kazahstan.
     */
    private String languagePool;
    /**
     * Task ID for future use in getTask method.
     */
    private String callbackUrl;
    /**
     * Task ID for future use in getTask method.
     */
    private Integer taskId;


    public CreateTaskAbstract setLanguagePool(LanguagePool languagePool) {
        this.languagePool = languagePool.toString().toLowerCase();
        return this;
    }

    public CreateTaskAbstract setCallbackUrl(URL callbackUrl) {
        this.callbackUrl = callbackUrl.toString();
        return this;
    }

    @SuppressWarnings("WeakerAccess")
    private Boolean createTask() {
        task = getPostData();
        // TODO: 21.10.2020: Need to add debug logger for taskJson
        if (task == null) {
            DebugHelper.out("JSON error", DebugHelper.Type.ERROR);

            return false;
        }

        JSONObject jsonPostData = new JSONObject();

        try {
            jsonPostData.put("clientKey", clientKey);
            jsonPostData.put("task", task);
            jsonPostData.put("softId", softId);
            jsonPostData.put("languagePool", languagePool);
            jsonPostData.put("callbackUrl", callbackUrl);
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

    /**
     * Sets workers pool language. At the moment the following language pools are available:
     * <p>
     * en (default) : English language queue;
     * rn  : group of countries: Russia, Ukraine, Belarus, Kazahstan.
     */
    public enum LanguagePool {
        /**
         * en (default) : English language queue
         */
        EN,
        /**
         * rn: group of countries: Russia, Ukraine, Belarus, Kazahstan
         */
        RN
    }
}
