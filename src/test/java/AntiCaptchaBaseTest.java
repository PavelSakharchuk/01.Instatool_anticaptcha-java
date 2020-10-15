import com.anticaptcha.Config;
import com.anticaptcha.api.AnticaptchaBase;
import com.anticaptcha.helper.DebugHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AntiCaptchaBaseTest {
    private static final String ANTICAPTCHA_KEY = Config.INSTANCE.getKey();


    @BeforeAll
    static void preparation() {
        DebugHelper.setVerboseMode(true);
    }


    @Test
    void getBalanceTest() {
        AnticaptchaBase api = new AnticaptchaBase();
        api.setClientKey(ANTICAPTCHA_KEY);

        Double balance = api.getBalance();

        if (balance == null) {
            DebugHelper.out("GetBalance() failed. " + api.getErrorMessage(), DebugHelper.Type.ERROR);
        } else {
            DebugHelper.out("Balance: " + balance, DebugHelper.Type.SUCCESS);
        }

        Assertions.assertNotNull(balance);
        Assertions.assertTrue(balance > 0);
    }
}
