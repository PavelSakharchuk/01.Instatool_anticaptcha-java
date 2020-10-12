import com.anticaptcha.AnticaptchaTask;
import com.anticaptcha.apiresponse.TaskResultResponse;
import com.anticaptcha.helper.DebugHelper;
import com.anticaptcha.helper.FileHelper;
import com.anticaptcha.http.Proxy;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AntiCaptchaTaskTest {

    @BeforeAll
    static void preparation() {
        DebugHelper.setVerboseMode(true);
    }


    @Test
    void imageToTextTest() throws InterruptedException, URISyntaxException {
        File captchaImageFile = FileHelper.getFileFromResource("captcha.jpg");
        String expectedCaptchaResult = "abournes";

        String captcha = AnticaptchaTask.solveImageToText(captchaImageFile);

        Assertions.assertEquals(expectedCaptchaResult, captcha);
    }

    @Test
    void noCaptchaTest() throws InterruptedException, MalformedURLException {
        URL websiteUrl = new URL("http://http.myjino.ru/recaptcha/test-get.php");
        String websiteKey = "6Lc_aCMTAAAAABx7u2W0WPXnVbI_v6ZdbM6rYf16";
        Proxy proxy = new Proxy();

        TaskResultResponse.SolutionData solution = AnticaptchaTask.solveNoCaptcha(websiteUrl, websiteKey, proxy);

        Assertions.assertNotNull(solution.getGRecaptchaResponse());
    }

    @Test
    void noCaptchaProxylessTest() throws InterruptedException, MalformedURLException {
        URL websiteUrl = new URL("http://http.myjino.ru/recaptcha/test-get.php");
        String websiteKey = "6Lc_aCMTAAAAABx7u2W0WPXnVbI_v6ZdbM6rYf16";

        TaskResultResponse.SolutionData solution = AnticaptchaTask.solveNoCaptchaProxyless(websiteUrl, websiteKey);

        Assertions.assertNotNull(solution.getGRecaptchaResponse());
    }

    @Test
    void recaptchaV3ProxylessTest() throws InterruptedException, MalformedURLException {
        URL websiteUrl = new URL("http://http.myjino.ru/recaptcha/test-get.php");
        String websiteKey = "6Lc_aCMTAAAAABx7u2W0WPXnVbI_v6ZdbM6rYf16";
        String pageAction = "testPageAction";

        TaskResultResponse.SolutionData solution = AnticaptchaTask.solveRecaptchaV3Proxyless(websiteUrl, websiteKey, pageAction);

        Assertions.assertNotNull(solution);
        Assertions.assertNotNull(solution.getGRecaptchaResponse());
    }

    @Test
    void squareNetTest() throws InterruptedException, URISyntaxException {
        File squareFile = FileHelper.getFileFromResource("square.jpg");
        String objectName = "FISH / РЫБА";
        int columns = 4;
        int rows = 4;

        List<Integer> expectedCaptchaResult = Arrays.asList(2);

        List<Integer> answerList = AnticaptchaTask.solveSquareNet(squareFile, objectName, columns, rows);

        Assertions.assertTrue(answerList.containsAll(expectedCaptchaResult)
                        && answerList.size() == expectedCaptchaResult.size(),
                String.format("Solve is not expected.%nExpected: %s%nActual: %s", expectedCaptchaResult, answerList));
    }

    @Test
    void geeTestProxylessTest() throws InterruptedException, MalformedURLException {
        URL websiteUrl = new URL("https://auth.geetest.com/");
        String websiteKey = "b6e21f90a91a3c2d4a31fe84e10d0442";
        String websiteChallenge = "cd0b3b5c33fb951ab364d9e13ccd7bf8";

        TaskResultResponse.SolutionData solution = AnticaptchaTask.solveGeeTestProxyless(websiteUrl, websiteKey, websiteChallenge);

        Assertions.assertNotNull(solution.getGRecaptchaResponse());
    }

    @Test
    void hCaptchaProxylessTest() throws InterruptedException, MalformedURLException {
        URL websiteUrl = new URL("http://democaptcha.com/");
        String websiteKey = "51829642-2cda-4b09-896c-594f89d700cc";

        TaskResultResponse.SolutionData solution = AnticaptchaTask.solveHCaptchaProxyless(websiteUrl, websiteKey);

        Assertions.assertNotNull(solution.getGRecaptchaResponse());
    }
}
