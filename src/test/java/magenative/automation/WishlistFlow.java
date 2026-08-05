package magenative.automation;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;
import java.util.List;

public class WishlistFlow {

    AndroidDriver driver;
    WebDriverWait wait;
    ProductPageTest productPage;
    private String appPackage;

    // --- Common Locators ---
    private By bottomWishlistBtn = By.xpath("//*[contains(@resource-id,'wishdisable')]");
    private By bottomWishlistBtnEnabled = By.xpath("//*[contains(@resource-id,'wishenable')]");
    // Some apps expose Wishlist as a bottom nav tab (more reliable when present);
    // others only via a top icon next to the cart. Try the tab first, fall back to the icon.
    private By wishlistNavTab = AppiumBy.androidUIAutomator("new UiSelector().textContains(\"Wishlist\")");
    private By topWishlistIcon = By.xpath("(//*[contains(@resource-id,'cart_icon')])[2]");
    private By removeProductX = By.xpath("//*[contains(@resource-id,'cancel_action')]");
    private By removeConfirmYes = By.xpath("//*[contains(@resource-id,'ok_dialog')]");
    private By emptyWishlistMsg = By.xpath("//*[contains(@resource-id,'nocarttext')]");
    private By backBtn = AppiumBy.accessibilityId("Navigate up");

    // ✅ Constructor
    public WishlistFlow(AndroidDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        this.productPage = new ProductPageTest(driver); // Reuse PDP navigation & variants selection
        Object pkg = driver.getCapabilities().getCapability("appPackage");
        this.appPackage = pkg != null ? pkg.toString() : null;
    }

    // --- If a scroll/gesture kicks the app to the home screen, notification
    // shade, or another app, relaunch it before the next step wastes time
    // searching a screen that no longer belongs to the app under test. ---
    private void ensureForeground() {
        try {
            String currentPackage = driver.getCurrentPackage();
            if (appPackage != null && !appPackage.equals(currentPackage)) {
                System.out.println("⚠️ Lost app foreground (current: " + currentPackage
                        + "), reactivating " + appPackage + "...");
                driver.activateApp(appPackage);
                Thread.sleep(1500);
            }
        } catch (Exception e) {
            System.out.println("⚠️ Could not verify/reactivate foreground app: " + e.getMessage());
        }
    }

    // ===== Main Flow =====
    // Note: assertions below throw AssertionError (not Exception), so they are
    // NOT swallowed by the catch block at the end — a real failure here fails
    // the TestNG test, instead of only being printed and ignored.
    public void wishlistProductFlow() {
        try {
            System.out.println("\n💎 ========== Starting Wishlist Flow ==========\n");
            ensureForeground();

            // Step 1: Navigate to product page (from homepage / listing)
            productPage.clickProductFromHome();

            // ✅ Fail loudly if we never actually reached a product page —
            // otherwise "no wishlist button" below would wrongly look like a
            // legitimate skip instead of a broken navigation step.
            Assert.assertTrue(productPage.isOnProductPage(),
                    "❌ Never landed on a product page — cannot test wishlist");

            // Step 2: Select first variant if present
            productPage.verifyAndSelectVariants();

            ensureForeground();

            // Step 3: Scroll and click bottom wishlist button
            try {
                driver.findElement(AppiumBy.androidUIAutomator(
                        "new UiScrollable(new UiSelector().scrollable(true)).scrollIntoView("
                                + "new UiSelector().resourceIdMatches(\".wishdisable.\"))"));
            } catch (Exception ignored) {}

            // ===== If bottom wishlist button isn't present on this product,
            // there's nothing to test — skip rather than fail. =====
            List<WebElement> wishlistBtns = driver.findElements(bottomWishlistBtn);
            if (wishlistBtns.isEmpty()) {
                System.out.println("⚠️ No wishlist button found at bottom, navigating back to HomePage");
                navigateBackToHomePage();
                return; // Exit flow
            }

            WebElement wishlistBtn = wait.until(ExpectedConditions.elementToBeClickable(bottomWishlistBtn));
            wishlistBtn.click();
            Thread.sleep(800);

            // ✅ Verify the click actually toggled the wishlist state (icon
            // switches from "disabled" to "enabled"), not just that we clicked.
            boolean wishlisted = !driver.findElements(bottomWishlistBtnEnabled).isEmpty();
            Assert.assertTrue(wishlisted,
                    "❌ Product was not marked as wishlisted after clicking the wishlist button");
            System.out.println("✅ Product added to wishlist successfully (verified)");

            ensureForeground();

            // Step 4: Navigate to Wishlist page — prefer the bottom nav tab if present
            List<WebElement> navTabs = driver.findElements(wishlistNavTab);
            if (!navTabs.isEmpty()) {
                navTabs.get(0).click();
            } else {
                WebElement topWishlist = wait.until(ExpectedConditions.elementToBeClickable(topWishlistIcon));
                topWishlist.click();
            }
            System.out.println("✅ Navigated to Wishlist page");

            // ✅ Verify the product we just wishlisted actually shows up here.
            WebElement removeBtn = wait.until(ExpectedConditions.elementToBeClickable(removeProductX));
            Assert.assertTrue(removeBtn.isDisplayed(),
                    "❌ Wishlist page did not show the product that was just added");

            // Step 5: Remove product from wishlist
            removeBtn.click();

            // Step 6: Confirm removal (Yes)
            WebElement yesBtn = wait.until(ExpectedConditions.elementToBeClickable(removeConfirmYes));
            yesBtn.click();
            Thread.sleep(800);

            // ✅ Verify removal actually happened: either the item is gone from
            // the list, or the wishlist now shows its empty state.
            boolean itemGone = driver.findElements(removeProductX).isEmpty();
            boolean emptyStateShown = !driver.findElements(emptyWishlistMsg).isEmpty();
            Assert.assertTrue(itemGone || emptyStateShown,
                    "❌ Product still appears in wishlist after removal was confirmed");
            System.out.println("✅ Product removed from wishlist successfully (verified)");

            // Step 7: Navigate back to HomePage (robust)
            navigateBackToHomePage();

            System.out.println("\n💎 ========== Wishlist Flow Completed ==========\n");

        } catch (AssertionError e) {
            throw e; // real verification failures must fail the test, not be swallowed
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("❌ Wishlist Flow failed due to: " + e.getMessage());
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
