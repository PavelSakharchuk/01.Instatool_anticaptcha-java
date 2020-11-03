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
    void imageToTextTest() throws URISyntaxException {
        File captchaImageFile = FileHelper.getFileFromResource("captcha.jpg");
        String expectedCaptchaResult = "abournes";

        TaskResultResponse.SolutionData solution = AnticaptchaTask.imageToTextBuilder(captchaImageFile)
                .getTaskSolution();

        Assertions.assertEquals(expectedCaptchaResult, solution.getText());
    }

    @Test
    void noCaptchaTest() throws MalformedURLException {
        URL websiteUrl = new URL("http://http.myjino.ru/recaptcha/test-get.php");
        String websiteKey = "6Lc_aCMTAAAAABx7u2W0WPXnVbI_v6ZdbM6rYf16";
        Proxy proxy = new Proxy();

        TaskResultResponse.SolutionData solution = AnticaptchaTask.noCaptcha(websiteUrl, websiteKey, proxy)
                .getTaskSolution();

        Assertions.assertNotNull(solution.getGRecaptchaResponse());
    }

    @Test
    void noCaptchaProxylessTest() throws MalformedURLException {
        URL websiteUrl = new URL("http://http.myjino.ru/recaptcha/test-get.php");
        String websiteKey = "6Lc_aCMTAAAAABx7u2W0WPXnVbI_v6ZdbM6rYf16";

        TaskResultResponse.SolutionData solution = AnticaptchaTask.noCaptchaProxyless(websiteUrl, websiteKey)
                .getTaskSolution();

        Assertions.assertNotNull(solution.getGRecaptchaResponse());
    }

    @Test
    void recaptchaV3ProxylessTest() throws MalformedURLException {
        URL websiteUrl = new URL("http://http.myjino.ru/recaptcha/test-get.php");
        String websiteKey = "6Lc_aCMTAAAAABx7u2W0WPXnVbI_v6ZdbM6rYf16";

        TaskResultResponse.SolutionData solution = AnticaptchaTask.recaptchaV3Proxyless(websiteUrl, websiteKey)
                .getTaskSolution();

        Assertions.assertNotNull(solution);
        Assertions.assertNotNull(solution.getGRecaptchaResponse());
    }

    @Test
    void funCaptchaTest() throws MalformedURLException {
        URL websiteUrl = new URL("http://http.myjino.ru/funcaptcha_test/");
        String websitePublicKey = "DE0B0BB7-1EE4-4D70-1853-31B835D4506B";
        Proxy proxy = new Proxy();

        TaskResultResponse.SolutionData solution = AnticaptchaTask.funCaptcha(websiteUrl, websitePublicKey, proxy)
                .getTaskSolution();

        Assertions.assertNotNull(solution.getGRecaptchaResponse());
    }

    @Test
    void funCaptchaProxylessTest() throws MalformedURLException {
        URL websiteUrl = new URL("http://http.myjino.ru/funcaptcha_test/");
        String websitePublicKey = "DE0B0BB7-1EE4-4D70-1853-31B835D4506B";

        TaskResultResponse.SolutionData solution = AnticaptchaTask.funCaptchaProxyless(websiteUrl, websitePublicKey)
                .getTaskSolution();

        Assertions.assertNotNull(solution.getGRecaptchaResponse());
    }

    @Test
    void squareNetTest() throws URISyntaxException {
        File squareFile = FileHelper.getFileFromResource("square.jpg");
        String objectName = "FISH / РЫБА";
        int rows = 4;
        int columns = 4;

        List<Integer> expectedCaptchaResult = Arrays.asList(2);


        TaskResultResponse.SolutionData solution = AnticaptchaTask.squareNet(squareFile, objectName, rows, columns)
                .getTaskSolution();
        List<Integer> answerList = solution.getCellNumbers();

        Assertions.assertTrue(answerList.containsAll(expectedCaptchaResult)
                        && answerList.size() == expectedCaptchaResult.size(),
                String.format("Solve is not expected.%nExpected: %s%nActual: %s", expectedCaptchaResult, answerList));
    }

    @Test
    void geeTestTest() throws MalformedURLException {
        URL websiteUrl = new URL("https://auth.geetest.com/");
        String websiteKey = "b6e21f90a91a3c2d4a31fe84e10d0442";
        // "challenge" for testing you can get here: https://www.binance.com/security/getGtCode.html?t=1561554068768
        // you need to get a new "challenge" each time
        String websiteChallenge = "cd0b3b5c33fb951ab364d9e13ccd7bf8";
        Proxy proxy = new Proxy();

        TaskResultResponse.SolutionData solution = AnticaptchaTask.geeTest(websiteUrl, websiteKey, websiteChallenge, proxy)
                .getTaskSolution();

        Assertions.assertNotNull(solution.getGRecaptchaResponse());
    }

    @Test
    void geeTestProxylessTest() throws MalformedURLException {
        URL websiteUrl = new URL("https://auth.geetest.com/");
        String websiteKey = "b6e21f90a91a3c2d4a31fe84e10d0442";
        // "challenge" for testing you can get here: https://www.binance.com/security/getGtCode.html?t=1561554068768
        // you need to get a new "challenge" each time
        String websiteChallenge = "cd0b3b5c33fb951ab364d9e13ccd7bf8";

        TaskResultResponse.SolutionData solution = AnticaptchaTask.geeTestProxyless(websiteUrl, websiteKey, websiteChallenge)
                .getTaskSolution();

        Assertions.assertNotNull(solution.getGRecaptchaResponse());
    }

    @Test
    void hCaptchaTest() throws MalformedURLException {
        URL websiteUrl = new URL("http://democaptcha.com/");
        String websiteKey = "51829642-2cda-4b09-896c-594f89d700cc";
        Proxy proxy = new Proxy();

        TaskResultResponse.SolutionData solution = AnticaptchaTask.hCaptcha(websiteUrl, websiteKey, proxy)
                .getTaskSolution();

        Assertions.assertNotNull(solution.getGRecaptchaResponse());
    }

    @Test
    void hCaptchaProxylessTest() throws MalformedURLException {
        URL websiteUrl = new URL("http://democaptcha.com/");
        String websiteKey = "51829642-2cda-4b09-896c-594f89d700cc";

        TaskResultResponse.SolutionData solution = AnticaptchaTask.hCaptchaProxyless(websiteUrl, websiteKey)
                .getTaskSolution();

        Assertions.assertNotNull(solution.getGRecaptchaResponse());
    }

    @Test
    void customCaptchaTest() throws MalformedURLException {
        String expectedLicensePlate = "TONFNTI";

        int randInt = ThreadLocalRandom.current().nextInt(0, 10000);
        URL imageUrl = new URL("https://files.anti-captcha.com/26/41f/c23/7c50ff19.jpg?random=" + randInt);
        String assignment = "Enter the licence plate number";

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

        TaskResultResponse.SolutionData solution = AnticaptchaTask.customCaptcha(imageUrl)
                .setAssignment(assignment)
                .setForms(formJson)
                .getTaskSolution();

        Assertions.assertEquals(expectedLicensePlate, solution.getAnswers().get("license_plate"));
    }
}
