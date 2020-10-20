import com.anticaptcha.AnticaptchaTask;
import com.anticaptcha.apiresponse.TaskResultResponse;
import com.anticaptcha.helper.DebugHelper;
import com.anticaptcha.helper.FileHelper;
import com.anticaptcha.http.Proxy;
import org.json.JSONArray;
import org.json.JSONObject;
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
import java.util.concurrent.ThreadLocalRandom;

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

        TaskResultResponse.SolutionData solution = AnticaptchaTask.imageToTextBuilder(captchaImageFile)
                .getTaskSolution();

        Assertions.assertEquals(expectedCaptchaResult, solution.getText());
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
    void funcaptchaTest() throws InterruptedException, MalformedURLException {
        URL websiteUrl = new URL("http://http.myjino.ru/funcaptcha_test/");
        String websiteKey = "DE0B0BB7-1EE4-4D70-1853-31B835D4506B";
        Proxy proxy = new Proxy();

        TaskResultResponse.SolutionData solution = AnticaptchaTask.solveFuncaptcha(websiteUrl, websiteKey, proxy);

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

    @Test
    void customCaptchaTest() throws InterruptedException {
        String expectedLicensePlate = "TONFNTI";

        JSONArray formJson = new JSONArray();
        formJson.put(0, new JSONObject());
        formJson.getJSONObject(0).put("label", "number");
        formJson.getJSONObject(0).put("labelHint", false);
        formJson.getJSONObject(0).put("contentType", false);
        formJson.getJSONObject(0).put("name", "license_plate");
        formJson.getJSONObject(0).put("inputType", "text");
        formJson.getJSONObject(0).put("inputOptions", new JSONObject());
        formJson.getJSONObject(0).getJSONObject("inputOptions").put("width", "100");
        formJson.getJSONObject(0).getJSONObject("inputOptions").put(
                "placeHolder",
                "Enter letters and numbers without spaces"
        );

        formJson.put(1, new JSONObject());
        formJson.getJSONObject(1).put("label", "Car color");
        formJson.getJSONObject(1).put("labelHint", "Select the car color");
        formJson.getJSONObject(1).put("contentType", false);
        formJson.getJSONObject(1).put("name", "color");
        formJson.getJSONObject(1).put("inputType", "select");
        formJson.getJSONObject(1).put("inputOptions", new JSONArray());
        formJson.getJSONObject(1).getJSONArray("inputOptions").put(0, new JSONObject());
        formJson.getJSONObject(1).getJSONArray("inputOptions").getJSONObject(0).put(
                "value",
                "white"
        );
        formJson.getJSONObject(1).getJSONArray("inputOptions").getJSONObject(0).put(
                "caption",
                "White color"
        );
        formJson.getJSONObject(1).getJSONArray("inputOptions").put(1, new JSONObject());
        formJson.getJSONObject(1).getJSONArray("inputOptions").getJSONObject(1).put(
                "value",
                "black"
        );
        formJson.getJSONObject(1).getJSONArray("inputOptions").getJSONObject(1).put(
                "caption",
                "Black color"
        );
        formJson.getJSONObject(1).getJSONArray("inputOptions").put(2, new JSONObject());
        formJson.getJSONObject(1).getJSONArray("inputOptions").getJSONObject(2).put(
                "value",
                "gray"
        );
        formJson.getJSONObject(1).getJSONArray("inputOptions").getJSONObject(2).put(
                "caption",
                "Gray color"
        );

        int randInt = ThreadLocalRandom.current().nextInt(0, 10000);
        String imageUrl = "https://files.anti-captcha.com/26/41f/c23/7c50ff19.jpg?random=" + randInt;
        String assignment = "Enter the licence plate number";

        TaskResultResponse.SolutionData solution = AnticaptchaTask.solveCustomCaptcha(assignment, imageUrl, formJson);

        Assertions.assertEquals(expectedLicensePlate, solution.getAnswers().get("license_plate"));
    }
}
