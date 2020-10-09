import com.anticaptcha.AnticaptchaTask;
import com.anticaptcha.apiresponse.TaskResultResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

class AntiCaptchaTaskTest {

    @Test
    void geeTestProxylessTest() throws MalformedURLException, InterruptedException {
        URL websiteUrl = new URL("https://auth.geetest.com/");
        String websiteKey = "b6e21f90a91a3c2d4a31fe84e10d0442";
        String websiteChallenge = "cd0b3b5c33fb951ab364d9e13ccd7bf8";

        TaskResultResponse.SolutionData solution = AnticaptchaTask.solveByProxyless(websiteUrl, websiteKey, websiteChallenge);

        Assertions.assertNotNull(solution);
    }

    @Test
    void imageToTextTest() throws InterruptedException {
        String captchaImagePath = "captcha.jpg";
        String expectedCaptchaResult = "abournes";

        String captcha = AnticaptchaTask.solveImageToText(captchaImagePath);

        Assertions.assertEquals(expectedCaptchaResult, captcha);
    }

    @Test
    void squareTest() throws InterruptedException {
        String filePath = "square.jpg";
        String objectName = "FISH / РЫБА";
        int columns = 4;
        int rows = 4;

        List<Integer> expectedCaptchaResult = Arrays.asList(3, 4, 5, 8, 13);

        List<Integer> answerList = AnticaptchaTask.solveSquare(filePath, objectName, columns, rows);

        Assertions.assertTrue(answerList.containsAll(expectedCaptchaResult)
                        && answerList.size() == expectedCaptchaResult.size(),
                String.format("Solve is not expected.%nExpected: %s%nActual: %s", expectedCaptchaResult, answerList));
    }

}
