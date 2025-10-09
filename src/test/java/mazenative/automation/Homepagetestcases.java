package mazenative.automation;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;

import java.time.Duration;

public class Homepagetestcases {

    private AndroidDriver driver;

    public Homepagetestcases(AndroidDriver driver) {
        this.driver = driver;
    }

    // Perform swipe down
    private void swipeDown() {
        int height = driver.manage().window().getSize().getHeight();
        int width = driver.manage().window().getSize().getWidth();

        int startX = width / 2;
        int startY = (int) (height * 0.8);  // bottom
        int endY = (int) (height * 0.2);    // top

        new TouchAction(driver)
            .press(PointOption.point(startX, startY))
            .waitAction(WaitOptions.waitOptions(Duration.ofMillis(800)))
            .moveTo(PointOption.point(startX, endY))
            .release()
            .perform();
    }

    // Perform swipe up
    private void swipeUp() {
        int height = driver.manage().window().getSize().getHeight();
        int width = driver.manage().window().getSize().getWidth();

        int startX = width / 2;
        int startY = (int) (height * 0.2);  // top
        int endY = (int) (height * 0.8);    // bottom

        new TouchAction(driver)
            .press(PointOption.point(startX, startY))
            .waitAction(WaitOptions.waitOptions(Duration.ofMillis(800)))
            .moveTo(PointOption.point(startX, endY))
            .release()
            .perform();
    }

    // Scroll till bottom (last product)
    public void scrollTillEnd(By productLocator) {
        int lastCount = 0;
        int sameCountTimes = 0;

        while (true) {
            List<WebElement> products = driver.findElements(productLocator);
            int currentCount = products.size();
            System.out.println("Current product count: " + currentCount);

            if (currentCount == lastCount) {
                sameCountTimes++;
            } else {
                sameCountTimes = 0; // reset if new products loaded
            }

            if (sameCountTimes >= 2) { // no new products in 2 consecutive scrolls → END
                System.out.println("✅ Reached last product!");
                break;
            }

            lastCount = currentCount;
            swipeDown();
        }
    }

    // Scroll back to top
    public void scrollToTop() {
        for (int i = 0; i < 8; i++) {  // 8 swipes up (adjust as needed)
            swipeUp();
        }
        System.out.println("✅ Back to top!");
    }
}







