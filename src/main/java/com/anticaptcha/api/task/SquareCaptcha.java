package com.anticaptcha.api.task;

import com.anticaptcha.api.AnticaptchaAbstract;
import com.anticaptcha.api.TaskType;
import com.anticaptcha.helper.DebugHelper;
import com.anticaptcha.helper.StringHelper;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

/**
 * <h2>SquareNetTextTask : select objects on image with an overlay grid</h2>
 * <p>
 * This type of tasks takes your image, adds custom grid on it
 * and asks our worker to mark specified objects on it.
 *
 * @see <a href="https://anticaptcha.atlassian.net/wiki/spaces/API/pages/410517505/SquareNetTextTask+select+objects+on+image+with+an+overlay+grid">https://anticaptcha.atlassian.net</a>
 */
@Setter
@Accessors(chain = true)
public class SquareCaptcha extends AnticaptchaAbstract {
    /**
     * Defines type of the task.
     */
    private final String type = TaskType.SQUARE_NET_TASK.getType();
    /**
     * File body encoded in base64. Make sure to send it without line breaks.
     */
    private final String bodyBase64;
    /**
     * Name of the object. Example: banana.
     */
    private final String objectName;
    /**
     * Number of grid rows. Min 2, max 5
     * Default - 3
     */
    private final Integer rowsCount;
    /**
     * Number of grid columns. Min 2, max 5
     * Default - 3
     */
    private final Integer columnsCount;


    public SquareCaptcha(File bodyFile, String objectName) {
        this(bodyFile, objectName, 3, 3);
    }

    public SquareCaptcha(File bodyFile, String objectName, Integer rowsCount, Integer columnsCount) {
        if (bodyFile.exists() && !bodyFile.isDirectory()) {
            if (bodyFile.length() > 100) {
                this.bodyBase64 = StringHelper.imageFileToBase64String(bodyFile);
            } else {
                this.bodyBase64 = null;
                DebugHelper.out("file " + bodyFile.getPath() + " is too small or empty", DebugHelper.Type.ERROR);
            }
        } else {
            this.bodyBase64 = null;
            DebugHelper.out("file " + bodyFile.getPath() + " not found", DebugHelper.Type.ERROR);
        }
        this.objectName = objectName;
        this.rowsCount = rowsCount;
        this.columnsCount = columnsCount;
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
            postData.put("objectName", objectName);
            postData.put("rowsCount", rowsCount);
            postData.put("columnsCount", columnsCount);
        } catch (JSONException e) {
            DebugHelper.out("JSON compilation error: " + e.getMessage(), DebugHelper.Type.ERROR);
            return null;
        }
        return postData;
    }
}
