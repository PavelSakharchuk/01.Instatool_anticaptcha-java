import com.anticaptcha.AnticaptchaTask;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;
import java.net.URL;

class AntiCaptchaTaskTest {

    @Test
    void geeTestProxylessTest() throws MalformedURLException, InterruptedException {
        URL websiteUrl = new URL("https://auth.geetest.com/");
        String websiteKey = "b6e21f90a91a3c2d4a31fe84e10d0442";
        String websiteChallenge = "cd0b3b5c33fb951ab364d9e13ccd7bf8";

        String captcha = AnticaptchaTask.solveByProxyless(websiteUrl, websiteKey, websiteChallenge);

        Assertions.assertEquals("captcha", captcha);
    }

}
