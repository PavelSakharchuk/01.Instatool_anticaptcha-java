package com.anticaptcha;

import com.anticaptcha.api.AnticaptchaAbstract;
import com.anticaptcha.api.GeeTestProxyless;
import com.anticaptcha.api.ImageToText;
import com.anticaptcha.api.NoCaptchaProxyless;
import com.anticaptcha.api.SquareCaptcha;
import com.anticaptcha.apiresponse.TaskResultResponse;
import com.anticaptcha.helper.DebugHelper;

import java.io.File;
import java.net.URL;
import java.util.List;

public class AnticaptchaTask {
    private static final String ANTICAPTCHA_KEY = Config.INSTANCE.getKey();


    private AnticaptchaTask() {
    }


    /**
     * <h2>ImageToTextTask : solve usual image captcha</h2>
     *
     * @param captchaImageFile File image captcha
     * @return Captcha answer
     * @throws InterruptedException for {@link AnticaptchaAbstract#waitForResult()}
     * @see <a href="https://anticaptcha.atlassian.net/wiki/spaces/API/pages/5079091/ImageToTextTask+solve+usual+image+captcha">https://anticaptcha.atlassian.net</a>
     */
    public static String solveImageToText(File captchaImageFile) throws InterruptedException {
        ImageToText api = new ImageToText();
        api.setClientKey(ANTICAPTCHA_KEY);
        api.setFile(captchaImageFile);

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

    /**
     * <h2>NoCaptchaTaskProxyless : Google Recaptcha puzzle solving without proxies</h2>
     * <p>
     * This object of type Task contains data required to solve Google Recaptcha on a worker's computer.
     * This task will be executed by our service using our own proxy servers and/or workers' IP addresses.
     * Costs for this task is 10% higher than NoCaptchaTask,
     * because the problem of overcoming per-IP limits falls on us.
     * As may you know, number of daily Recaptcha solutions is limited for each IP address
     * and we'll have to deal with proxies ourselves.
     *
     * @param website    Address of target web page
     * @param websiteKey Recaptcha website key
     * @see <a href="https://anticaptcha.atlassian.net/wiki/spaces/API/pages/9666606/NoCaptchaTaskProxyless+Google+Recaptcha+puzzle+solving+without+proxies">https://anticaptcha.atlassian.net</a>
     */
    public static TaskResultResponse.SolutionData solveNoCaptchaProxyless(URL website, String websiteKey) throws InterruptedException {
        NoCaptchaProxyless api = new NoCaptchaProxyless();
        api.setClientKey(ANTICAPTCHA_KEY);
        api.setWebsiteUrl(website);
        api.setWebsiteKey(websiteKey);

        if (!api.createTask()) {
            DebugHelper.out(
                    "API v2 send failed. " + api.getErrorMessage(),
                    DebugHelper.Type.ERROR
            );
        } else if (!api.waitForResult()) {
            DebugHelper.out("Could not solve the captcha.", DebugHelper.Type.ERROR);
        } else {
            DebugHelper.out("Result: " + api.getTaskSolution().getGRecaptchaResponse(), DebugHelper.Type.SUCCESS);
        }
        return api.getTaskSolution();
    }

    public static TaskResultResponse.SolutionData solveByProxyless(URL website, String websiteKey, String websiteChallenge) throws InterruptedException {
        GeeTestProxyless api = new GeeTestProxyless();
        api.setClientKey(ANTICAPTCHA_KEY);
        api.setWebsiteUrl(website);
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

    public static List<Integer> solveSquare(File squareFile, String objectName, int columns, int rows) throws InterruptedException {
        SquareCaptcha api = new SquareCaptcha();
        api.setClientKey(ANTICAPTCHA_KEY);
        api.setFile(squareFile);
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
