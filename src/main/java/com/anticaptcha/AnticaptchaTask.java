package com.anticaptcha;

import com.anticaptcha.api.task.CustomCaptcha;
import com.anticaptcha.api.task.FunCaptcha;
import com.anticaptcha.api.task.FunCaptchaProxyless;
import com.anticaptcha.api.task.GeeTestProxyless;
import com.anticaptcha.api.task.HCaptchaProxyless;
import com.anticaptcha.api.task.ImageToText;
import com.anticaptcha.api.task.NoCaptcha;
import com.anticaptcha.api.task.NoCaptchaProxyless;
import com.anticaptcha.api.task.RecaptchaV3Proxyless;
import com.anticaptcha.api.task.SquareCaptcha;
import com.anticaptcha.apiresponse.TaskResultResponse;
import com.anticaptcha.http.Proxy;
import org.json.JSONArray;

import java.io.File;
import java.net.URL;

public class AnticaptchaTask {

    private AnticaptchaTask() {
    }

    public static ImageToText imageToTextBuilder(File captchaImageFile) {
        // TODO: 21.10.2020: p.sakharchuk: Need to add logger
//        DebugHelper.out("Result: " + api.getTaskSolution().getText(), DebugHelper.Type.SUCCESS);
        return new ImageToText(captchaImageFile);
    }

    public static NoCaptcha noCaptcha(URL website, String websiteKey, Proxy proxy) {
        // TODO: 21.10.2020: p.sakharchuk: Need to add logger
//        DebugHelper.out("Result: " + api.getTaskSolution().getGRecaptchaResponse(), DebugHelper.Type.SUCCESS);
        return new NoCaptcha(website, websiteKey, proxy);
    }

    public static NoCaptchaProxyless noCaptchaProxyless(URL website, String websiteKey) {
        // TODO: 21.10.2020: p.sakharchuk: Need to add logger
//        DebugHelper.out("Result: " + api.getTaskSolution().getGRecaptchaResponse(), DebugHelper.Type.SUCCESS);
        return new NoCaptchaProxyless(website, websiteKey);
    }

    public static RecaptchaV3Proxyless recaptchaV3Proxyless(URL website, String websiteKey) {
        // TODO: 21.10.2020: p.sakharchuk: Need to add logger
//            DebugHelper.out("Result: " + api.getTaskSolution().getGRecaptchaResponse(), DebugHelper.Type.SUCCESS);
        return new RecaptchaV3Proxyless(website, websiteKey);
    }

    public static FunCaptcha funCaptcha(URL website, String websitePublicKey, Proxy proxy) {
        // TODO: 21.10.2020: p.sakharchuk: Need to add logger
//            DebugHelper.out("Result: " + api.getTaskSolution().getToken(), DebugHelper.Type.SUCCESS);
        return new FunCaptcha(website, websitePublicKey, proxy);
    }

    public static FunCaptchaProxyless funCaptchaProxyless(URL website, String websitePublicKey) {
        // TODO: 21.10.2020: p.sakharchuk: Need to add logger
//            DebugHelper.out("Result: " + api.getTaskSolution().getToken(), DebugHelper.Type.SUCCESS);
        return new FunCaptchaProxyless(website, websitePublicKey);
    }

    public static SquareCaptcha squareNet(File squareFile, String objectName) {
        // TODO: 21.10.2020: p.sakharchuk: Need to add logger
//            DebugHelper.out("Result: " + api.getTaskSolution().getCellNumbers(), DebugHelper.Type.SUCCESS);
        return new SquareCaptcha(squareFile, objectName);
    }

    public static SquareCaptcha squareNet(File squareFile, String objectName, Integer rowsCount, Integer columnsCount) {
        // TODO: 21.10.2020: p.sakharchuk: Need to add logger
//            DebugHelper.out("Result: " + api.getTaskSolution().getCellNumbers(), DebugHelper.Type.SUCCESS);
        return new SquareCaptcha(squareFile, objectName, rowsCount, columnsCount);
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
     * @return Solution
     * @see <a href="https://anticaptcha.atlassian.net/wiki/spaces/API/pages/416972814/GeeTestTaskProxyless+-+captcha+from+geetest.com+without+proxy">https://anticaptcha.atlassian.net</a>
     */
    public static TaskResultResponse.SolutionData solveGeeTestProxyless(URL website, String websiteKey, String websiteChallenge) {
        GeeTestProxyless api = new GeeTestProxyless();
        api.setWebsiteUrl(website);
        api.setWebsiteKey(websiteKey);
        // "challenge" for testing you can get here: https://www.binance.com/security/getGtCode.html?t=1561554068768
        // you need to get a new "challenge" each time
        api.setWebsiteChallenge(websiteChallenge);

        // TODO: 21.10.2020: p.sakharchuk: Need to add logger
//            DebugHelper.out("Result CHALLENGE: " + api.getTaskSolution().getChallenge(), DebugHelper.Type.SUCCESS);
//            DebugHelper.out("Result SECCODE: " + api.getTaskSolution().getSeccode(), DebugHelper.Type.SUCCESS);
//            DebugHelper.out("Result VALIDATE: " + api.getTaskSolution().getValidate(), DebugHelper.Type.SUCCESS);
        return api.getTaskSolution();
    }

    /**
     * <h2>HCaptchaTaskProxyless : hCaptcha puzzle solving without proxy</h2>
     * <p>
     * HCaptcha devs call their captcha "a drop-in replacement for Recaptcha".
     * We tried to create same thing in our API,
     * so task properties are absolutely the same except type.
     *
     * @param website    Address of target web page
     * @param websiteKey The domain key
     * @return Solution
     * @see <a href="https://anticaptcha.atlassian.net/wiki/spaces/API/pages/834502676/HCaptchaTaskProxyless+hCaptcha+puzzle+solving+without+proxy">https://anticaptcha.atlassian.net</a>
     */
    public static TaskResultResponse.SolutionData solveHCaptchaProxyless(URL website, String websiteKey) {
        HCaptchaProxyless api = new HCaptchaProxyless();
        api.setWebsiteUrl(website);
        api.setWebsiteKey(websiteKey);

        // TODO: 21.10.2020: p.sakharchuk: Need to add logger
//            DebugHelper.out("Result: " + api.getTaskSolution().getGRecaptchaResponse(), DebugHelper.Type.SUCCESS);
        return api.getTaskSolution();
    }

    /**
     * <h2>HCaptchaTaskProxyless : hCaptcha puzzle solving without proxy</h2>
     * <p>
     * This type of tasks is suitable for use when you need to describe what is on an image
     * and you need workers to fill a custom form for this.
     *
     * @param assignment Describe what worker must do in English
     * @param imageUrl   Address of an image
     * @param formJson   Custom form object. It can be or JSON-object
     * @return Solution
     * @see <a href="https://developer.aliyun.com/mirror/npm/package/anticaptcha2">https://developer.aliyun.com</a>
     */
    public static TaskResultResponse.SolutionData solveCustomCaptcha(String assignment, String imageUrl, JSONArray formJson) {
        CustomCaptcha api = new CustomCaptcha();
        api.setImageUrl(imageUrl);
        api.setAssignment(assignment);
        api.setForms(formJson);

        // TODO: 21.10.2020: p.sakharchuk: Need to add logger
//            JSONObject answers = api.getTaskSolution().getAnswers();
//            Iterator<?> keys = answers.keys();
//
//            while (keys.hasNext()) {
//                String question = (String) keys.next();
//                String answer = answers.getString(question);
//
//                DebugHelper.out(
//                        "The answer for the question '" + question + "' : " + answer,
//                        DebugHelper.Type.SUCCESS
//                );
//            }
        return api.getTaskSolution();
    }
}
