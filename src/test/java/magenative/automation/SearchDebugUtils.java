package magenative.automation;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import io.appium.java_client.android.AndroidDriver;

public class SearchDebugUtils {

    private AndroidDriver driver;

    public SearchDebugUtils(AndroidDriver driver) {
        this.driver = driver;
    }

    // ✅ Print all visible elements on current screen
    public void printAllScreenElements() {
        try {
            System.out.println("🖥️ Printing all visible elements on current screen:");

            // ✅ Selenium-compatible findElements
            List<WebElement> allElements = driver.findElements(By.xpath("//*"));

            System.out.println("Total elements found: " + allElements.size());

            for (int i = 0; i < allElements.size(); i++) {
                WebElement el = allElements.get(i);
                String className = el.getTagName();
                String resourceId = el.getAttribute("resourceId");
                String text = el.getText();
                System.out.println("Element " + i + " -> Class: " + className + ", ResourceId: " + resourceId + ", Text: " + text);
            }

        } catch (Exception e) {
            System.out.println("❌ Error while printing screen elements: " + e.getMessage());
        }
    }
}


