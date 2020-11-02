import com.anticaptcha.AnticaptchaTask;
import com.anticaptcha.api.CreateTaskAbstract;
import com.anticaptcha.apiresponse.TaskResultResponse;
import com.anticaptcha.helper.DebugHelper;
import com.anticaptcha.helper.FileHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AntiCaptchaFullTaskTest {

    @BeforeAll
    static void preparation() {
        DebugHelper.setVerboseMode(true);
    }

    @Test
    void imageToTextFullCreateTaskTest() throws InterruptedException, URISyntaxException, MalformedURLException {
        File captchaImageFile = FileHelper.getFileFromResource("captcha.jpg");
        String expectedCaptchaResult = "abournes";

        TaskResultResponse.SolutionData solution = AnticaptchaTask.imageToTextBuilder(captchaImageFile)
                .setSoftId(1)
                .setLanguagePool(CreateTaskAbstract.LanguagePool.RN)
                .setCallbackUrl(new URL("https://anticaptcha.atlassian.net/"))
                .getTaskSolution();

        Assertions.assertEquals(expectedCaptchaResult, solution.getText());
    }

    @Test
    void imageToTextFullTest() throws InterruptedException, URISyntaxException, MalformedURLException {
        File captchaImageFile = FileHelper.getFileFromResource("captcha.jpg");
        String expectedCaptchaResult = "abournes";

        TaskResultResponse.SolutionData solution = AnticaptchaTask.imageToTextBuilder(captchaImageFile)
                .setPhrase(true)
                .setCase_(true)
                .setNumeric(2)
                .setMath(false)
                .setMinLength(8)
                .setMaxLength(8)
                .setComment("Captcha")
                .setWebsiteURL(new URL("https://test.lib"))
                .getTaskSolution();

        Assertions.assertEquals(expectedCaptchaResult, solution.getText());
    }
}
