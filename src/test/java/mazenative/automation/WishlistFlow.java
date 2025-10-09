package mazenative.automation;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class WishlistFlow {

    AndroidDriver driver;
    WebDriverWait wait;
    ProductPageTest productPage;

    // --- Common Locators ---
    private By bottomWishlistBtn = By.xpath("//*[contains(@resource-id,'wishdisable')]");
    private By topWishlistIcon = By.xpath("(//*[contains(@resource-id,'cart_icon')])[2]");
    private By removeProductX = By.xpath("//*[contains(@resource-id,'cancel_action')]");
    private By removeConfirmYes = By.xpath("//*[contains(@resource-id,'ok_dialog')]");
    private By backBtn = AppiumBy.accessibilityId("Navigate up");

    // ✅ Constructor
    public WishlistFlow(AndroidDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        this.productPage = new ProductPageTest(driver); // Reuse PDP navigation & variants selection
    }

    // ===== Main Flow =====
    public void wishlistProductFlow() {
        try {
            System.out.println("\n💎 ========== Starting Wishlist Flow ==========\n");

            // Step 1: Navigate to product page (from homepage / listing)
            productPage.clickProductFromHome();

            // Step 2: Select first variant if present
            productPage.verifyAndSelectVariants();

            // Step 3: Scroll and click bottom wishlist button
            try {
                driver.findElement(AppiumBy.androidUIAutomator(
                        "new UiScrollable(new UiSelector().scrollable(true)).scrollIntoView("
                                + "new UiSelector().resourceIdMatches(\".wishdisable.\"))"));
            } catch (Exception ignored) {}

            // ===== Added Condition: If bottom wishlist not found =====
            List<WebElement> wishlistBtns = driver.findElements(bottomWishlistBtn);
            if (wishlistBtns.isEmpty()) {
                System.out.println("⚠️ No wishlist found at bottom, navigating back to HomePage");
                navigateBackToHomePage();
                return; // Exit flow
            }

            WebElement wishlistBtn = wait.until(ExpectedConditions.elementToBeClickable(bottomWishlistBtn));
            wishlistBtn.click();
            System.out.println("✅ Product added to wishlist successfully");

            // Step 4: Navigate to Wishlist page via top icon
            WebElement topWishlist = wait.until(ExpectedConditions.elementToBeClickable(topWishlistIcon));
            topWishlist.click();
            System.out.println("✅ Navigated to Wishlist page");

            // Step 5: Remove product from wishlist
            WebElement removeBtn = wait.until(ExpectedConditions.elementToBeClickable(removeProductX));
            removeBtn.click();

            // Step 6: Confirm removal (Yes)
            WebElement yesBtn = wait.until(ExpectedConditions.elementToBeClickable(removeConfirmYes));
            yesBtn.click();
            System.out.println("✅ Product removed from wishlist successfully");

            // Step 7: Navigate back to HomePage (robust)
            navigateBackToHomePage();

            System.out.println("\n💎 ========== Wishlist Flow Completed ==========\n");

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("❌ Wishlist Flow failed due to: " + e.getMessage());
        }
    }

    // ===== Robust Back to Homepage =====
    private void navigateBackToHomePage() {
        int maxBack = 3; // Maximum back presses
        for (int i = 0; i < maxBack; i++) {
            try {
                WebElement backButton = wait.until(ExpectedConditions.elementToBeClickable(backBtn));
                backButton.click();
                sleep(1000);
                System.out.println("🛑 Pressed back button: " + (i+1));

                if (isOnHomePage()) {
                    System.out.println("🏠 Reached HomePage");
                    break;
                }
            } catch (Exception e) {
                System.out.println("⚠️ Back button not clickable: " + e.getMessage());
            }
        }
    }

    // ===== Homepage Detection (Update according to your app) =====
    private boolean isOnHomePage() {
        try {
            // Example: homepage has a unique banner or element
            driver.findElement(AppiumBy.androidUIAutomator("new UiSelector().description(\"home_banner\")"));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // ===== Helper =====
    private void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
