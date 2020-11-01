package com.anticaptcha.api.task;

import com.anticaptcha.api.CreateTaskAbstract;
import com.anticaptcha.api.TaskType;
import com.anticaptcha.helper.DebugHelper;
import com.anticaptcha.helper.StringHelper;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.net.URL;

/**
 * <h2>ImageToTextTask: solve usual image captcha</h2>
 *
 * @see <a href="https://anticaptcha.atlassian.net/wiki/spaces/API/pages/5079091/ImageToTextTask+solve+usual+image+captcha">https://anticaptcha.atlassian.net</a>
 */
@Setter
@Accessors(chain = true)
public class ImageToText extends CreateTaskAbstract {
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
    private Integer numeric = NumericOption.NO_REQUIREMENTS.getNumeric();
    /**
     * false - [default] no requirements
     * true - worker will see a special mark telling that answer must be calculated
     */
    private Boolean math;
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
    private String comment;
    /**
     * Optional parameter to distinguish source of image captchas in spending statistics
     */
    private String websiteURL;


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
        JSONObject postData = new JSONObject();

        if (bodyBase64 == null || bodyBase64.length() == 0) {
            return null;
        }

        try {
            postData.put("type", type);
            postData.put("body", bodyBase64.replace("\r", "").replace("\n", ""));
            postData.put("phrase", phrase);
            postData.put("case", case_);
            postData.put("numeric", numeric);
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

    public void setWebsiteURL(URL websiteURL) {
        this.websiteURL = websiteURL.toString();
    }


    public enum NumericOption {
        NO_REQUIREMENTS(0),
        NUMBERS_ONLY(1),
        ANY_LETTERS_EXCEPT_NUMBERS(2);

        private Integer numeric;

        NumericOption(Integer numeric) {
            this.numeric = numeric;
        }

        public Integer getNumeric() {
            return numeric;
        }
    }
}
