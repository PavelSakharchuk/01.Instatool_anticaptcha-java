package com.anticaptcha;

import com.anticaptcha.api.GeeTestProxyless;
import com.anticaptcha.helper.DebugHelper;

import java.net.URL;

public class AnticaptchaTask {
    private static String anticaptchaKey = Config.INSTANCE.getKey();

    private AnticaptchaTask() {
    }

    public static String solveByProxyless(URL url, String websiteKey, String websiteChallenge) throws InterruptedException {
        DebugHelper.setVerboseMode(true);

        GeeTestProxyless api = new GeeTestProxyless();
        api.setClientKey(anticaptchaKey);
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
        return null;
    }
}
