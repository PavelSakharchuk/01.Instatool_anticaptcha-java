package com.anticaptcha.api.task;

import com.anticaptcha.api.AnticaptchaAbstract;
import com.anticaptcha.api.IAnticaptchaTaskProtocol;
import com.anticaptcha.api.TaskType;
import com.anticaptcha.apiresponse.TaskResultResponse;
import com.anticaptcha.helper.DebugHelper;
import com.anticaptcha.helper.StringHelper;
import lombok.Setter;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

/**
 * <h2>ImageToTextTask : solve usual image captcha</h2>
 *
 * @see <a href="https://anticaptcha.atlassian.net/wiki/spaces/API/pages/5079091/ImageToTextTask+solve+usual+image+captcha">https://anticaptcha.atlassian.net</a>
 */
@Setter
public class ImageToText extends AnticaptchaAbstract implements IAnticaptchaTaskProtocol {
    /**
     * Defines type of the task.
     */
    private final String type = TaskType.IMAGE_TO_TEXT_TASK.getType();
    /**
     * File body encoded in base64. Make sure to send it without line breaks.
     */
    private final String bodyBase64;

    /**
     * false - [default] no requirements
     * true - worker must enter an answer with at least one "space"
     */
    private Boolean phrase;
    /**
     * false - [default] no requirements
     * true - worker will see a special mark telling that answer must be entered with case sensitivity.
     */
    private Boolean case_;
    /**
     * 0 - [default] no requirements
     * 1 - only number are allowed
     * 2 - any letters are allowed except numbers
     */
    private NumericOption numeric = NumericOption.NO_REQUIREMENTS;
    /**
     * false - [default] no requirements
     * true - worker will see a special mark telling that answer must be calculated
     */
    private Integer math;
    /**
     * 0 - [default] no requirements
     * >1 - defines minimum length of the answer
     */
    private Integer minLength;
    /**
     * 0 - [default] no requirements
     * >1 - defines maximum length of the answer
     */
    private Integer maxLength;
    /**
     * Additional comment for workers like "enter letters in red color".
     * Result is not guaranteed.
     */
    private Integer comment;
    /**
     * Optional parameter to distinguish source of image captchas in spending statistics
     */
    private Integer websiteURL;


    public ImageToText(File bodyFile) {
        if (!bodyFile.exists() || bodyFile.isDirectory()) {
            this.bodyBase64 = null;
            DebugHelper.out("File " + bodyFile.getPath() + " not found", DebugHelper.Type.ERROR);
        } else {
            this.bodyBase64 = StringHelper.imageFileToBase64String(bodyFile);

            if (this.bodyBase64 == null) {
                DebugHelper.out(
                        "Could not convert the file \" + value + \" to base64. Is this an image file?",
                        DebugHelper.Type.ERROR
                );
            }
        }
    }


    @Override
    public JSONObject getPostData() {

        if (bodyBase64 == null || bodyBase64.length() == 0) {
            return null;
        }

        JSONObject postData = new JSONObject();

        try {
            postData.put("type", type);
            postData.put("body", bodyBase64.replace("\r", "").replace("\n", ""));
            postData.put("phrase", phrase);
            postData.put("case", case_);
            postData.put("numeric", numeric.equals(NumericOption.NO_REQUIREMENTS) ? 0 : numeric.equals(NumericOption.NUMBERS_ONLY) ? 1 : 2);
            postData.put("math", math);
            postData.put("minLength", minLength);
            postData.put("maxLength", maxLength);
            postData.put("comment", comment);
            postData.put("websiteURL", websiteURL);
        } catch (JSONException e) {
            DebugHelper.out("JSON compilation error: " + e.getMessage(), DebugHelper.Type.ERROR);

            return null;
        }

        return postData;
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
            DebugHelper.out("Result: " + taskInfo.getSolution().getText(), DebugHelper.Type.SUCCESS);
        }

        return taskInfo.getSolution();
    }


    public enum NumericOption {
        NO_REQUIREMENTS,
        NUMBERS_ONLY,
        ANY_LETTERS_EXCEPT_NUMBERS
    }
}
