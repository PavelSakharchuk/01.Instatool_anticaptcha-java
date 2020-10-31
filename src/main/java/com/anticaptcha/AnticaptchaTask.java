package com.anticaptcha;

import com.anticaptcha.api.task.CustomCaptcha;
import com.anticaptcha.api.task.FunCaptcha;
import com.anticaptcha.api.task.FunCaptchaProxyless;
import com.anticaptcha.api.task.GeeTest;
import com.anticaptcha.api.task.GeeTestProxyless;
import com.anticaptcha.api.task.HCaptcha;
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

    public static GeeTest geeTest(URL website, String gt, String challenge, Proxy proxy) {
        // TODO: 21.10.2020: p.sakharchuk: Need to add logger
//            DebugHelper.out("Result CHALLENGE: " + api.getTaskSolution().getChallenge(), DebugHelper.Type.SUCCESS);
//            DebugHelper.out("Result SECCODE: " + api.getTaskSolution().getSeccode(), DebugHelper.Type.SUCCESS);
//            DebugHelper.out("Result VALIDATE: " + api.getTaskSolution().getValidate(), DebugHelper.Type.SUCCESS);
        return new GeeTest(website, gt, challenge, proxy);
    }

    public static GeeTestProxyless geeTestProxyless(URL website, String gt, String challenge) {
        // TODO: 21.10.2020: p.sakharchuk: Need to add logger
//            DebugHelper.out("Result CHALLENGE: " + api.getTaskSolution().getChallenge(), DebugHelper.Type.SUCCESS);
//            DebugHelper.out("Result SECCODE: " + api.getTaskSolution().getSeccode(), DebugHelper.Type.SUCCESS);
//            DebugHelper.out("Result VALIDATE: " + api.getTaskSolution().getValidate(), DebugHelper.Type.SUCCESS);
        return new GeeTestProxyless(website, gt, challenge);
    }

    public static HCaptcha hCaptcha(URL website, String websiteKey, Proxy proxy) {
        // TODO: 21.10.2020: p.sakharchuk: Need to add logger
//            DebugHelper.out("Result: " + api.getTaskSolution().getGRecaptchaResponse(), DebugHelper.Type.SUCCESS);
        return new HCaptcha(website, websiteKey, proxy);
    }

    public static HCaptchaProxyless hCaptchaProxyless(URL website, String websiteKey) {
        // TODO: 21.10.2020: p.sakharchuk: Need to add logger
//            DebugHelper.out("Result: " + api.getTaskSolution().getGRecaptchaResponse(), DebugHelper.Type.SUCCESS);
        return new HCaptchaProxyless(website, websiteKey);
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
