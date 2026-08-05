package magenative.automation;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;
import java.util.*;

public class SortingAndFilterpage {

    AndroidDriver driver;
    WebDriverWait wait;

    // ===== Common Locators =====
    private By viewAllBtn = By.id("actiontext");
    // Some app builds expose card/banner images via content-desc="image"; others
    // (e.g. York Laine) never set a content-desc and only expose resource-id
    // ".../id/image" instead. Try accessibility-id first, then fall back.
    private By bannerImage = AppiumBy.accessibilityId("image");
    private By bannerImageFallback = By.xpath(
            "//*[contains(@resource-id,':id/image') and not(ancestor::*[contains(@resource-id,'nav_view')])]"
    );
    private By sortBtn = By.id("sort_but");
    private By aToZOption = By.id("atoz");
    private By filterBtn = By.id("filter_icon");
    private By availabilityText = AppiumBy.androidUIAutomator("new UiSelector().text(\"Availability\")");
    private By firstCheckBox = By.xpath("(//android.widget.CheckBox)[1]");
    private By applyBtn = By.id("btn_apply");
    private By productNames = By.xpath("//android.widget.TextView[contains(@resource-id,'name')]");
    private By backBtn = AppiumBy.accessibilityId("Navigate up");
    private By homeIdentifier = AppiumBy.androidUIAutomator("new UiSelector().description(\"home\")"); // generic home check

    // ===== Constructor =====
    public SortingAndFilterpage(AndroidDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    // ===== STEP 1: Open Listing Page =====
    public void openListingPage() {
        try {
            WebElement viewAll = null;
            try {
                viewAll = wait.until(ExpectedConditions.elementToBeClickable(viewAllBtn));
                viewAll.click();
                System.out.println("✅ Clicked 'View All' to open Listing Page");
            } catch (Exception e) {
                System.out.println("⚠️ 'View All' not found, trying banner fallback...");
                WebElement banner;
                try {
                    banner = wait.until(ExpectedConditions.elementToBeClickable(bannerImage));
                } catch (Exception e2) {
                    System.out.println("⚠️ No content-desc=\"image\" banner found, trying resource-id fallback...");
                    banner = wait.until(ExpectedConditions.elementToBeClickable(bannerImageFallback));
                }
                banner.click();
                System.out.println("✅ Clicked on banner to navigate to Listing Page");
            }
        } catch (Exception e) {
            Assert.fail("❌ Failed to open listing page: " + e.getMessage());
        }
    }

    // ===== STEP 2: Sort Products A to Z =====
    public void sortProductsAtoZ() {
        try {
            WebElement sortButton = wait.until(ExpectedConditions.elementToBeClickable(sortBtn));
            sortButton.click();
            System.out.println("✅ Sorting options opened");

            WebElement aToZ = wait.until(ExpectedConditions.elementToBeClickable(aToZOption));
            aToZ.click();
            System.out.println("✅ Selected A to Z sorting");

            // Wait for products to load
            wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(productNames));
            List<WebElement> productList = driver.findElements(productNames);

            List<String> actualNames = new ArrayList<>();
            for (WebElement p : productList) actualNames.add(p.getText().trim());

            List<String> expectedNames = new ArrayList<>(actualNames);
            Collections.sort(expectedNames, String.CASE_INSENSITIVE_ORDER);

            if (actualNames.equals(expectedNames)) {
                System.out.println("✅ Products correctly sorted A to Z");
            } else {
                System.out.println("❌ Sorting verification failed");
                System.out.println("Actual  : " + actualNames);
                System.out.println("Expected: " + expectedNames);
            }

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("❌ Sorting A to Z flow failed: " + e.getMessage());
        }
    }

    // ===== STEP 3: Apply Filter =====
    public void applyFilter() {
        try {
            WebElement filterIcon = wait.until(ExpectedConditions.elementToBeClickable(filterBtn));
            filterIcon.click();
            System.out.println("✅ Filter section opened");

            boolean filterApplied = false;

            try {
                WebElement availability = wait.until(ExpectedConditions.elementToBeClickable(availabilityText));
                availability.click();
                System.out.println("✅ Availability filter opened");

                WebElement checkBox = wait.until(ExpectedConditions.elementToBeClickable(firstCheckBox));
                checkBox.click();
                System.out.println("✅ Checkbox selected under Availability");
                filterApplied = true;
            } catch (Exception e) {
                System.out.println("⚠️ Availability filter not found, selecting first available checkbox...");
                List<WebElement> allCheckboxes = driver.findElements(By.xpath("//android.widget.CheckBox"));
                if (!allCheckboxes.isEmpty()) {
                    allCheckboxes.get(0).click();
                    System.out.println("✅ First available filter checkbox selected");
                    filterApplied = true;
                } else {
                    System.out.println("⚠️ No checkboxes found in filter section");
                }
            }

            if (filterApplied) {
                WebElement applyButton = wait.until(ExpectedConditions.elementToBeClickable(applyBtn));
                applyButton.click();
                System.out.println("✅ Filter applied successfully");
            }

            // Verify filtered products
            List<WebElement> filteredProducts = driver.findElements(productNames);
            if (!filteredProducts.isEmpty()) {
                System.out.println("✅ Filtered products displayed: " + filteredProducts.size());
            } else {
                System.out.println("⚠️ No products found after applying filter");
            }

            navigateBackToHome();

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("❌ Filter flow failed: " + e.getMessage());
        }
    }

    // ===== STEP 4: Navigate Back to Home =====
    public void navigateBackToHome() {
        int maxTry = 3;
        for (int i = 0; i < maxTry; i++) {
            try {
                WebElement back = wait.until(ExpectedConditions.elementToBeClickable(backBtn));
                back.click();
                Thread.sleep(1000);
                System.out.println("🔙 Pressed back button (" + (i + 1) + ")");
                if (isOnHomePage()) {
                    System.out.println("🏠 Navigated back to HomePage successfully");
                    break;
                }
            } catch (Exception e) {
                System.out.println("⚠️ Back navigation attempt failed: " + e.getMessage());
            }
        }
    }

    // ===== Homepage detection =====
    private boolean isOnHomePage() {
        try {
            driver.findElement(homeIdentifier);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
