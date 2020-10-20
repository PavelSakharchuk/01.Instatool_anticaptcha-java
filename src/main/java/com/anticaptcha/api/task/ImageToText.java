package com.anticaptcha.api.task;

import com.anticaptcha.api.AnticaptchaAbstract;
import com.anticaptcha.api.IAnticaptchaTaskProtocol;
import com.anticaptcha.api.TaskType;
import com.anticaptcha.apiresponse.TaskResultResponse;
import com.anticaptcha.helper.DebugHelper;
import com.anticaptcha.helper.StringHelper;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

public class ImageToText extends AnticaptchaAbstract implements IAnticaptchaTaskProtocol {
    private String type = TaskType.IMAGE_TO_TEXT_TASK.getType();
    private Boolean phrase;
    private Boolean case_;
    private NumericOption numeric = NumericOption.NO_REQUIREMENTS;
    private Integer math;
    private Integer minLength;
    private Integer maxLength;
    private String bodyBase64;

    public void setPhrase(Boolean phrase) {
        this.phrase = phrase;
    }

    public void setCase_(Boolean case_) {
        this.case_ = case_;
    }

    public void setNumeric(NumericOption numeric) {
        this.numeric = numeric;
    }

    public void setMath(Integer math) {
        this.math = math;
    }

    public void setMinLength(Integer minLength) {
        this.minLength = minLength;
    }

    public void setMaxLength(Integer maxLength) {
        this.maxLength = maxLength;
    }

    public enum NumericOption {
        NO_REQUIREMENTS,
        NUMBERS_ONLY,
        ANY_LETTERS_EXCEPT_NUMBERS
    }

    public void setFile(File file) {
        if (!file.exists() || file.isDirectory()) {
            DebugHelper.out("File " + file.getPath() + " not found", DebugHelper.Type.ERROR);
        } else {
            bodyBase64 = StringHelper.imageFileToBase64String(file);

            if (bodyBase64 == null) {
                DebugHelper.out(
                        "Could not convert the file \" + value + \" to base64. Is this an image file?",
                        DebugHelper.Type.ERROR
                );
            }
        }
    }

    public Boolean getPhrase() {
        return phrase;
    }

    public Boolean getCase_() {
        return case_;
    }

    public NumericOption getNumeric() {
        return numeric;
    }

    public Integer getMath() {
        return math;
    }

    public Integer getMinLength() {
        return minLength;
    }

    public Integer getMaxLength() {
        return maxLength;
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

    public void setBodyBase64(String bodyBase64) {
        this.bodyBase64 = bodyBase64;
    }
}
