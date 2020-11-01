package com.anticaptcha.api.task;

import com.anticaptcha.api.CreateTaskAbstract;
import com.anticaptcha.api.TaskType;
import com.anticaptcha.helper.DebugHelper;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;

/**
 * <h2>CustomCaptchaTask: Custom tasks</h2>
 * <p>
 * This type of tasks is suitable for use when you need to describe
 * what is on an image and you need workers to fill a custom form for this.
 * <p>
 * Examples:
 * - Read letters and numbers from a car license plate
 * - Find a phone number on a commercial
 * - Complete task like "count monkeys on a picture"
 * - etc.
 *
 * @see <a href="https://developer.aliyun.com/mirror/npm/package/anticaptcha2">https://developer.aliyun.com</a>
 */
@Setter
@Accessors(chain = true)
public class CustomCaptcha extends CreateTaskAbstract {
    /**
     * Defines type of the task.
     */
    private final String type = TaskType.CUSTOM_CAPTCHA_TASK.getType();
    /**
     * Address of an image
     */
    private final String imageUrl;
    /**
     * Describe what worker must do in English
     */
    private String assignment;
    /**
     * Custom form object.
     * It can be or JSON-object or instance of Form To build this object you have 3 options:
     * - Pass instance of Form
     * - Use FormBuilder tool in Anti-Captcha clients area.
     * - Code it manually using documentation.
     *
     * @see <a href="https://developer.aliyun.com/mirror/npm/package/anticaptcha2#new-form">Form</a>
     */
    //TODO: 31.10.2020: p.sakharchuk: Rework to FormBuilder
    private JSONArray forms;


    public CustomCaptcha(URL imageUrl) {
        this.imageUrl = imageUrl.toString();
    }


    @Override
    public JSONObject getPostData() {
        JSONObject postData = new JSONObject();
        try {
            postData.put("type", type);
            postData.put("imageUrl", imageUrl);
            postData.put("assignment", assignment);
            postData.put("forms", forms);
        } catch (JSONException e) {
            DebugHelper.out("JSON compilation error: " + e.getMessage(), DebugHelper.Type.ERROR);
            return null;
        }
        return postData;
    }
}
