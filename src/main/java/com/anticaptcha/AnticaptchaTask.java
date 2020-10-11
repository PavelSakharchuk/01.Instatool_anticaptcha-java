package com.anticaptcha;

import com.anticaptcha.api.AnticaptchaAbstract;
import com.anticaptcha.api.GeeTestProxyless;
import com.anticaptcha.api.HCaptchaProxyless;
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
     * @see <a href="https://anticaptcha.atlassian.net/wiki/spaces/API/pages/9666606/NoCaptchaTaskProxyless+Google+Recaptcha+puzzle+solving+without+proxies">https://anticaptcha.atlassian.net</a>
     *
     * @param website    Address of target web page
     * @param websiteKey Recaptcha website key
     *
     * @throws InterruptedException for {@link AnticaptchaAbstract#waitForResult()}
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

    /**
     * <h2>SquareNetTextTask : select objects on image with an overlay grid</h2>
     * <p>
     * This type of tasks takes your image, adds custom grid on it
     * and asks our worker to mark specified objects on it.
     *
     * @param squareFile File image captcha
     * @param objectName Name of the object. Example: banana
     * @param columns    Number of grid columns. Min 2, max 5
     * @param rows       Number of grid rows. Min 2, max 5
     * @throws InterruptedException for {@link AnticaptchaAbstract#waitForResult()}
     * @see <a href="https://anticaptcha.atlassian.net/wiki/spaces/API/pages/410517505/SquareNetTextTask+select+objects+on+image+with+an+overlay+grid">https://anticaptcha.atlassian.net</a>
     */
    public static List<Integer> solveSquareNet(File squareFile, String objectName, int columns, int rows) throws InterruptedException {
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

    /**
     * <h2>GeeTestTaskProxyless - captcha from geetest.com without proxy</h2>
     * <p>
     * This type of task solves GeeTest captcha in our workers browsers.
     * Your app submits website address, gt key, challenge key and receives 3 parameters after task completion.
     *
     * @param website          Address of target web page
     * @param websiteKey       The domain key
     * @param websiteChallenge Changing token key.
     *                         Make sure to grab fresh one for each captcha,
     *                         otherwise you will be charged for error task.
     * @throws InterruptedException for {@link AnticaptchaAbstract#waitForResult()}
     * @see <a href="https://anticaptcha.atlassian.net/wiki/spaces/API/pages/416972814/GeeTestTaskProxyless+-+captcha+from+geetest.com+without+proxy">https://anticaptcha.atlassian.net</a>
     */
    public static TaskResultResponse.SolutionData solveGeeTestProxyless(URL website, String websiteKey, String websiteChallenge) throws InterruptedException {
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

    /**
     * <h2>HCaptchaTaskProxyless : hCaptcha puzzle solving without proxy</h2>
     * <p>
     * hCaptcha devs call their captcha "a drop-in replacement for Recaptcha".
     * We tried to create same thing in our API,
     * so task properties are absolutely the same except type.
     *
     * @param website    Address of target web page
     * @param websiteKey The domain key
     * @throws InterruptedException for {@link AnticaptchaAbstract#waitForResult()}
     * @see <a href="https://anticaptcha.atlassian.net/wiki/spaces/API/pages/834502676/HCaptchaTaskProxyless+hCaptcha+puzzle+solving+without+proxy">https://anticaptcha.atlassian.net</a>
     */
    public static TaskResultResponse.SolutionData solveHCaptchaProxyless(URL website, String websiteKey) throws InterruptedException {
        DebugHelper.setVerboseMode(true);

        HCaptchaProxyless api = new HCaptchaProxyless();
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
}
