package mazenative.automation;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

public class AppTest {

    public static void main(String[] args) throws MalformedURLException {

        UiAutomator2Options options = new UiAutomator2Options();
        options.setDeviceName("AndroidEmulator");
        options.setApp("C:\\Users\\AkashShivBhushan2442\\apkfiles\\barsache.apk");
        options.setAppPackage("com.baersachenew.app");
        options.setAutomationName("uiautomator2");
        options.setNoReset(true);
        options.setUiautomator2ServerLaunchTimeout(Duration.ofSeconds(180));

        AndroidDriver driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), options);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        // -------------------- CLICK BANNER --------------------
        WebElement banner = wait.until(ExpectedConditions.elementToBeClickable(
                AppiumBy.xpath("(//android.widget.ImageView[@resource-id='com.baersachenew.app:id/image'])[1]")));
        banner.click();
        System.out.println("✅ Banner clicked successfully!");

        // -------------------- WAIT FOR PRODUCT LIST --------------------
        // Scroll to product (if necessary) and click
        WebElement product = driver.findElement(
                AppiumBy.androidUIAutomator(
                        "new UiScrollable(new UiSelector().scrollable(true))" +
                        ".scrollIntoView(new UiSelector().resourceId(\"com.baersachenew.app:id/image\"))"));

        wait.until(ExpectedConditions.elementToBeClickable(product)).click();
        System.out.println("✅ Product clicked successfully!");

        // -------------------- END SESSION --------------------
      //  driver.quit();
    }

   
}
