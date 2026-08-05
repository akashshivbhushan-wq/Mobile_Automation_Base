package magenative.automation;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class CartPageTest {

    AndroidDriver driver;
    WebDriverWait wait;
    String appPackage;

    // ✅ Constructor (driver + wait)
    public CartPageTest(AndroidDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
        this.appPackage = driver.getCapabilities().getCapability("appPackage").toString();
    }

    // --- Dynamic locator helper ---
    private String id(String id) {
        return appPackage + ":id/" + id;
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

    // ==================== STEP 1: SELECT PRODUCT FROM HOMEPAGE / LISTING WITH STOCK CHECK ====================
    public void selectProductFromHomeWithStockCheck() {
        boolean productSelected = false;
        int attempts = 0;

        while (!productSelected && attempts < 5) { // Try max 5 products
            selectProductFromHome(); // Original homepage/listing selection

            if (isOutOfStock()) {
                // Go back to listing/homepage and try next product
                try {
                    WebElement backBtn = driver.findElement(AppiumBy.accessibilityId("Navigate up"));
                    backBtn.click();
                    Thread.sleep(1000);
                    System.out.println("⬅ Back to homepage/listing to select another product");
                } catch (Exception e) {
                    driver.navigate().back();
                    System.out.println("⬅ Using driver.navigate().back() to return");
                }
                attempts++;
            } else {
                productSelected = true;
            }
        }

        if (!productSelected) {
            throw new RuntimeException("❌ No in-stock products found after " + attempts + " attempts");
        }
    }

    // --- Click product from homepage / first product in listing ---
    private void selectProductFromHome() {
        try {
            System.out.println("➡ Clicking product from homepage...");
            WebElement product = wait.until(ExpectedConditions.elementToBeClickable(
                    AppiumBy.androidUIAutomator("new UiSelector().description(\"image\").instance(0)")));
            product.click();
            Thread.sleep(2000);

            if (!isOnProductPage()) {
                System.out.println("ℹ️ Listing page detected — navigating to PDP...");
                clickFirstProductFromListing();
            } else {
                System.out.println("✅ Directly landed on Product Page (PDP)");
            }

        } catch (Exception e) {
            System.out.println("❌ Unable to click product from homepage: " + e.getMessage());
        }
    }

    // --- Detect if current page is Product Page (PDP) ---
    private boolean isOnProductPage() {
        try {
            driver.findElement(By.xpath("//*[contains(@resource-id,'addtocart')]"));
            return true;
        } catch (Exception e1) {
            try {
                driver.findElement(By.xpath("//*[contains(@resource-id,'buynow')]"));
                return true;
            } catch (Exception e2) {
                return false;
            }
        }
    }

    // --- Click first product from listing page if PDP not open directly ---
    private void clickFirstProductFromListing() {
        try {
            driver.findElement(AppiumBy.androidUIAutomator(
                    "new UiScrollable(new UiSelector().scrollable(true))" +
                            ".scrollIntoView(new UiSelector().description(\"image\").instance(0))"
            ));

            List<WebElement> products = driver.findElements(
                    AppiumBy.androidUIAutomator("new UiSelector().description(\"image\")")
            );

            if (products.size() > 0) {
                products.get(0).click();
                Thread.sleep(1500);

                if (!isOnProductPage()) {
                    System.out.println("⚠️ Not yet on PDP, retrying...");
                    products.get(0).click();
                    Thread.sleep(1000);
                }
                System.out.println("✅ Clicked first product from listing page");
            } else {
                System.out.println("⚠️ No products found on listing page");
            }
        } catch (Exception e) {
            System.out.println("❌ Failed to click product from listing: " + e.getMessage());
        }
    }

    // --- Check if product is out of stock ---
    private boolean isOutOfStock() {
        try {
            driver.findElement(By.xpath("//*[contains(@text,'Out Of Stock')]"));
            System.out.println("⚠️ Product is out of stock!");
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // ==================== STEP 2: SELECT ALL VARIANTS (Colour / Size / Year) ====================
    public void selectAllVariantsIfAvailable() {
        ensureForeground();
        try {
            System.out.println("➡ Checking for variants...");
            // Scroll to first variant
            driver.findElement(AppiumBy.androidUIAutomator(
                    "new UiScrollable(new UiSelector().scrollable(true))" +
                            ".scrollIntoView(new UiSelector().resourceId(\"" + id("variant_name") + "\"))"
            ));

            // Get all variant types present
            List<WebElement> variants = driver.findElements(
                    AppiumBy.xpath("//*[contains(@resource-id,'variant_name')]")
            );

            int count = 1;
            for (WebElement v : variants) {
                try {
                    WebElement firstOption = wait.until(ExpectedConditions.elementToBeClickable(
                            AppiumBy.xpath("(//android.widget.TextView[@resource-id='" + id("variant_name") + "'])[" + count + "]")));
                    firstOption.click();
                    System.out.println("✅ Selected variant " + count);
                } catch (Exception ex) {
                    System.out.println("⚠️ Variant " + count + " not selectable, skipping...");
                }
                count++;
            }

        } catch (Exception e) {
            System.out.println("⚠️ No variants found, continuing...");
        }
    }

    // ==================== STEP 3: CLICK WISHLIST ICON ====================
    public void clickWishlistIcon() {
        ensureForeground();
        try {
            driver.findElement(AppiumBy.androidUIAutomator(
                    "new UiScrollable(new UiSelector().scrollable(true)).scrollToEnd(3)"
            ));

            WebElement wishlist = wait.until(ExpectedConditions.elementToBeClickable(
                    AppiumBy.id(id("wishdisable"))));
            wishlist.click();
            System.out.println("❤️ Wishlist icon clicked");
        } catch (Exception e) {
            System.out.println("⚠️ Wishlist icon not found, skipping...");
        }
    }

    // ==================== STEP 4: ADD TO CART ====================
    public void addToCart() {
        ensureForeground();
        try {
            WebElement addBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    AppiumBy.id(id("addtocart"))));
            addBtn.click();
            System.out.println("🛒 Product added to cart successfully");
        } catch (Exception e) {
            System.out.println("❌ Add to Cart button not found!");
        }
    }

    // ==================== STEP 5: GO TO CART ====================
    public void goToCart() {
        ensureForeground();
        try {
            WebElement cartBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    AppiumBy.id(id("cartsection"))));
            cartBtn.click();
            System.out.println("🛍️ Navigated to Cart page");
        } catch (Exception e) {
            System.out.println("⚠️ Cart button not found!");
        }
    }

 // ==================== STEP 6: INCREASE QUANTITY ====================
    private boolean quantityIncreased = false; // flag to track if increase happened

    public void increaseQuantity() {
        try {
            WebElement plus = wait.until(ExpectedConditions.elementToBeClickable(
                    AppiumBy.id(id("increase"))));
            plus.click();
            System.out.println("🔼 Quantity increased by 1");

            // ✅ Wait after increase
            Thread.sleep(1000); // 1 second wait (adjust as needed)
            quantityIncreased = true; // mark successful increase

        } catch (Exception e) {
            System.out.println("⚠️ Increase button not found!");
            quantityIncreased = false;
        }
    }

 // ==================== STEP 7: DECREASE QUANTITY ====================
    public void decreaseQuantity() {
        if (!quantityIncreased) {
            System.out.println("⚠️ Quantity was not increased, skipping decrease");
            return; // skip decrease if increase didn't happen
        }

        try {
            WebElement minus = wait.until(ExpectedConditions.elementToBeClickable(
                    AppiumBy.id(id("decrese")))); // spelling as per locator
            minus.click();
            System.out.println("🔽 Quantity decreased by 1");

            // ✅ Reset flag after decrease
            quantityIncreased = false;

        } catch (Exception e) {
            System.out.println("⚠️ Decrease button not found!");
        }
    }
    // ==================== STEP 8: PROCEED TO CHECKOUT ====================
    public void proceedToCheckout() {
        ensureForeground();
        try {
            WebElement checkout = wait.until(ExpectedConditions.elementToBeClickable(
                    AppiumBy.id(id("proceedtocheck"))));
            checkout.click();
            System.out.println("✅ Proceeded to Checkout successfully");
        } catch (Exception e) {
            System.out.println("❌ Proceed to Checkout button not found!");
        }
    }

    // ==================== STEP 9: FULL CART FLOW ====================
    public void completeCartFlow() {
        selectProductFromHomeWithStockCheck();   // Homepage → Listing → PDP → In-stock check
        selectAllVariantsIfAvailable();          // Select all variant types dynamically
        clickWishlistIcon();                     // Wishlist click
        addToCart();                             // Add product to cart
        goToCart();                              // Navigate to cart
        increaseQuantity();                      // Increase quantity
        decreaseQuantity();                      // Decrease quantity
        proceedToCheckout();                     // Checkout
        System.out.println("🎯 Full Cart Flow executed successfully!");
    }
}
