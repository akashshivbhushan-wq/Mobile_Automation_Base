package mazenative.automation;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.TouchAction;
import io.appium.java_client.touch.offset.ElementOption;

public class SearchPageTest {

    private AndroidDriver driver;
    private WebDriverWait wait;

    // 🔑 Locators
    private By homeSearchIcon = By.id("com.dentojo.app:id/cart_icon"); // Home page search icon
    private By searchBox = By.id("com.dentojo.app:id/auto_search");
    private By searchItem = By.id("com.dentojo.app:id/items");
    private By productImage = By.xpath("(//android.widget.ImageView[@content-desc='image'])[1]"); // First product
    private By noResultsMsg = By.id("com.dentojo.app:id/nodata");

    public SearchPageTest(AndroidDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    // ✅ Safe element getter
    private WebElement getElement(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    // ✅ Navigate to search page
    private void navigateToSearchPage() {
        try {
            WebElement homeIcon = wait.until(ExpectedConditions.elementToBeClickable(homeSearchIcon));
            homeIcon.click();
            System.out.println("➡ Clicked home page search icon to open search page");
            wait.until(ExpectedConditions.visibilityOfElementLocated(searchBox));
        } catch (Exception e) {
            System.out.println("⚠ Could not navigate from home to search page: " + e.getMessage());
        }
    }

    // ✅ Perform search
    public void performSearch(String keyword) {
        try {
            System.out.println("🔍 Starting Search for keyword: " + keyword);

            // Navigate from home page if necessary
            navigateToSearchPage();

            // Enter keyword
            WebElement searchField = getElement(searchBox);
            searchField.clear();
            searchField.sendKeys(keyword);
            System.out.println("➡ Entered keyword: " + keyword);

            // Press keyboard search
            driver.pressKey(new KeyEvent(AndroidKey.SEARCH));
            System.out.println("➡ Pressed SEARCH key from keyboard");

            // Wait for results
            Thread.sleep(2000); // small wait for rendering
            List<WebElement> results = driver.findElements(searchItem);

            if (results.size() > 0) {
                System.out.println("✅ Products displayed: " + results.size());

                // Click first product image
                WebElement firstProduct = results.get(0).findElement(productImage);
                firstProduct.click();
                System.out.println("➡ Clicked first product image");

            } else if (driver.findElements(noResultsMsg).size() > 0) {
                System.out.println("❌ No products found for: " + keyword);
            } else {
                System.out.println("❌ Search test failed: No products & no 'No product' message!");
            }

            System.out.println("✅ Search test completed for: " + keyword);

        } catch (Exception e) {
            System.out.println("❌ Search test failed: " + e.getMessage());
        }
    }
}







