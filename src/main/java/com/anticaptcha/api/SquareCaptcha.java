package com.anticaptcha.api;

import com.anticaptcha.apiresponse.TaskResultResponse;
import com.anticaptcha.helper.DebugHelper;
import com.anticaptcha.helper.StringHelper;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

public class SquareCaptcha extends AnticaptchaAbstract implements IAnticaptchaTaskProtocol {
    private String bodyBase64;
    private String objectName = "";
    private int rowsCount = 3;
    private int columnsCount = 3;

    @Override
    public JSONObject getPostData() {

        if (bodyBase64 == null || bodyBase64.length() == 0) {
            return null;
        }

        JSONObject postData = new JSONObject();

        try {
            postData.put("type", "SquareNetTask");
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

    @Override
    public TaskResultResponse.SolutionData getTaskSolution() {
        return taskInfo.getSolution();
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public void setRowsCount(int rowsCount) {
        this.rowsCount = rowsCount;
    }

    public void setColumnsCount(int columnsCount) {
        this.columnsCount = columnsCount;
    }

    @SuppressWarnings("UnusedReturnValue")
    public boolean setFile(File file) {
        if (file.exists() && !file.isDirectory()) {
            if (file.length() > 100) {
                bodyBase64 = StringHelper.imageFileToBase64String(file);

                return true;
            } else {
                DebugHelper.out("file " + file.getPath() + " is too small or empty", DebugHelper.Type.ERROR);
            }
        } else {
            DebugHelper.out("file " + file.getPath() + " not found", DebugHelper.Type.ERROR);
        }

        return false;
    }
}
