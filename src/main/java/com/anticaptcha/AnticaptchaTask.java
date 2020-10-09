package com.anticaptcha;

import com.anticaptcha.api.GeeTestProxyless;
import com.anticaptcha.api.ImageToText;
import com.anticaptcha.api.SquareCaptcha;
import com.anticaptcha.apiresponse.TaskResultResponse;
import com.anticaptcha.helper.DebugHelper;

import java.net.URL;
import java.util.List;

public class AnticaptchaTask {
    private static final String ANTICAPTCHA_KEY = Config.INSTANCE.getKey();


    private AnticaptchaTask() {
    }

    public static TaskResultResponse.SolutionData solveByProxyless(URL url, String websiteKey, String websiteChallenge) throws InterruptedException {
        DebugHelper.setVerboseMode(true);

        GeeTestProxyless api = new GeeTestProxyless();
        api.setClientKey(ANTICAPTCHA_KEY);
        api.setWebsiteUrl(url);
        api.setWebsiteKey(websiteKey);
        // "challenge" for testing you can get here: https://www.binance.com/security/getGtCode.html?t=1561554068768
        // you need to get a new "challenge" each time
        api.setWebsiteChallenge(websiteChallenge);

        if (!api.createTask()) {
            DebugHelper.out(
                    "API v2 send failed. " + api.getErrorMessage(),
                    DebugHelper.Type.ERROR
            );
        } else if (!api.waitForResult()) {
            DebugHelper.out("Could not solve the captcha.", DebugHelper.Type.ERROR);
        } else {
            DebugHelper.out("Result CHALLENGE: " + api.getTaskSolution().getChallenge(), DebugHelper.Type.SUCCESS);
            DebugHelper.out("Result SECCODE: " + api.getTaskSolution().getSeccode(), DebugHelper.Type.SUCCESS);
            DebugHelper.out("Result VALIDATE: " + api.getTaskSolution().getValidate(), DebugHelper.Type.SUCCESS);
        }
        return api.getTaskSolution();
    }

    public static String solveImageToText(String captchaImageFilePath) throws InterruptedException {
        DebugHelper.setVerboseMode(true);

        ImageToText api = new ImageToText();
        api.setClientKey(ANTICAPTCHA_KEY);
        api.setFilePath(captchaImageFilePath);

        if (!api.createTask()) {
            DebugHelper.out(
                    "API v2 send failed. " + api.getErrorMessage(),
                    DebugHelper.Type.ERROR
            );
        } else if (!api.waitForResult()) {
            DebugHelper.out("Could not solve the captcha.", DebugHelper.Type.ERROR);
        } else {
            DebugHelper.out("Result: " + api.getTaskSolution().getText(), DebugHelper.Type.SUCCESS);
        }
        return api.getTaskSolution().getText();
    }

    public static List<Integer> solveSquare(String filePath, String objectName, int columns, int rows) throws InterruptedException {
        DebugHelper.setVerboseMode(true);

        SquareCaptcha api = new SquareCaptcha();
        api.setClientKey(ANTICAPTCHA_KEY);
        api.setFilePath(filePath);
        api.setObjectName(objectName);
        api.setColumnsCount(columns);
        api.setRowsCount(rows);

        if (!api.createTask()) {
            DebugHelper.out(
                    "API v2 send failed. " + api.getErrorMessage(),
                    DebugHelper.Type.ERROR
            );
        } else if (!api.waitForResult()) {
            DebugHelper.out("Could not solve the captcha.", DebugHelper.Type.ERROR);
        } else {
            DebugHelper.out("Result: " + api.getTaskSolution().getCellNumbers(), DebugHelper.Type.SUCCESS);
        }
        return api.getTaskSolution().getCellNumbers();
    }
}
