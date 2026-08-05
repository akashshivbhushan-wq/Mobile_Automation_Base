package magenative.automation;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;

import java.time.Duration;
import java.util.List;

public class ProductPageTest {

    AndroidDriver driver;
    WebDriverWait wait;

    // --- Common Locators (Works for all apps) ---
    // Some app builds expose product/card images via content-desc="image"
    // (accessibility id); others (e.g. York Laine) never set a content-desc at
    // all and instead only expose them via resource-id ".../id/image". Try the
    // accessibility-id form first, then fall back to the resource-id form,
    // excluding the bottom nav bar so we don't click a nav icon by mistake.
    private By productFromHome = AppiumBy.androidUIAutomator(
            "new UiSelector().description(\"image\").instance(0)"
    );
    private By productFromHomeFallback = By.xpath(
            "//*[contains(@resource-id,':id/image') and not(ancestor::*[contains(@resource-id,'nav_view')])]"
    );
    private By productImage = AppiumBy.accessibilityId("image");
    private By productNamePriceLayout = By.xpath("//*[contains(@resource-id,'quantitylayout')]");
    private By variantName = By.xpath("//*[contains(@resource-id,'variant_name')]");
    private By addToCartBtn = By.xpath("//*[contains(@resource-id,'addtocart')]");
    private By buyNowBtn = By.xpath("//*[contains(@resource-id,'buynow')]");
    private By outOfStockText = AppiumBy.androidUIAutomator("new UiSelector().textContains(\"Out Of Stock\")");
    private By backBtn = AppiumBy.accessibilityId("Navigate up");

    // Constructor
    public ProductPageTest(AndroidDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    // ========== CLICK PRODUCT FROM HOMEPAGE (Smart Detection) ==========
    public void clickProductFromHome() {
        try {
            // Reset scroll position first — carousels/testimonial sections can
            // leave the home page scrolled away from the product grid, which
            // otherwise makes this look like "no product found" for 30s.
            try {
                driver.findElement(AppiumBy.androidUIAutomator(
                        "new UiScrollable(new UiSelector().scrollable(true)).scrollToBeginning(10)"));
            } catch (Exception ignored) {}

            WebElement product = findClickableProductOnHome();
            product.click();
            sleep(2000);
            System.out.println("✅ Product clicked from homepage");

            // 🔍 After click, detect if we landed on PDP or listing
            if (!isOnProductPage()) {
                System.out.println("ℹ️ Listing page detected — clicking first product from listing...");
                clickFirstProductFromListing();
            } else {
                System.out.println("✅ PDP opened directly after homepage click");
            }

        } catch (Exception e) {
            System.out.println("❌ Failed to click product: " + e.getMessage());
        }
    }

    // --- Find a clickable product/card image on the home page, trying the
    // accessibility-id locator first and the resource-id fallback second. ---
    private WebElement findClickableProductOnHome() {
        try {
            return wait.until(ExpectedConditions.elementToBeClickable(productFromHome));
        } catch (Exception e) {
            System.out.println("ℹ️ No content-desc=\"image\" element found, trying resource-id fallback...");
            return wait.until(ExpectedConditions.elementToBeClickable(productFromHomeFallback));
        }
    }

    // --- Detect if current page is Product Detail Page (PDP) ---
    public boolean isOnProductPage() {
        try {
            driver.findElement(addToCartBtn);
            return true;
        } catch (Exception e1) {
            try {
                driver.findElement(buyNowBtn);
                return true;
            } catch (Exception e2) {
                return false;
            }
        }
    }

    // --- Click First Product From Listing Page ---
    private void clickFirstProductFromListing() {
        try {
            // Scroll to first visible product if needed
            try {
                driver.findElement(AppiumBy.androidUIAutomator(
                        "new UiScrollable(new UiSelector().scrollable(true))" +
                        ".scrollIntoView(new UiSelector().description(\"image\").instance(0))"
                ));
                sleep(1000);
            } catch (Exception ignored) {
                System.out.println("⚠️ Scroll not needed or failed on listing page");
            }

            List<WebElement> listingProducts = driver.findElements(
                    AppiumBy.androidUIAutomator("new UiSelector().description(\"image\")")
            );
            if (listingProducts.isEmpty()) {
                listingProducts = driver.findElements(productFromHomeFallback);
            }

            if (listingProducts.size() > 0) {
                listingProducts.get(0).click();
                sleep(1500);
                System.out.println("✅ Clicked first product from listing page");

                if (!isOnProductPage()) {
                    System.out.println("⚠️ Still not on PDP — trying again...");
                    sleep(1000);
                    listingProducts.get(0).click();
                    sleep(1500);
                }
            } else {
                System.out.println("⚠️ No products found on listing page");
            }

        } catch (Exception e) {
            System.out.println("❌ Failed to click product from listing: " + e.getMessage());
        }
    }

    // ========== VERIFY PRODUCT IMAGE ==========
    public void verifyProductImage() {
        try {
            WebElement image = wait.until(ExpectedConditions.presenceOfElementLocated(productImage));
            if (image.isDisplayed()) {
                System.out.println("✅ Product image is displayed: true");
            } else {
                System.out.println("⚠️ Product image is not visible");
            }
        } catch (Exception e) {
            System.out.println("❌ Product image not found: " + e.getMessage());
        }
    }

    // ========== VERIFY PRODUCT NAME & PRICE ==========
    public void verifyProductNameAndPrice() {
        try {
            WebElement namePrice = wait.until(ExpectedConditions.presenceOfElementLocated(productNamePriceLayout));
            if (namePrice.isDisplayed()) {
                System.out.println("✅ Product name & price displayed: true");
            } else {
                System.out.println("⚠️ Product name & price not visible");
            }
        } catch (Exception e) {
            System.out.println("❌ Product name & price not found: " + e.getMessage());
        }
    }

    // ========== VERIFY & SELECT VARIANTS ==========
    public void verifyAndSelectVariants() {
        try {
            // Scroll to variants section
            try {
                driver.findElement(AppiumBy.androidUIAutomator(
                        "new UiScrollable(new UiSelector().scrollable(true))" +
                        ".scrollIntoView(new UiSelector().resourceIdMatches(\".*variant_name.*\"))"
                ));
                sleep(500);
            } catch (Exception scrollEx) {
                System.out.println("⚠️ Scroll to variants not needed or failed");
            }

            // Find all variants
            List<WebElement> variants = driver.findElements(variantName);

            if (variants.size() > 0) {
                System.out.println("✅ Variants available: true");
                System.out.println("   Total variants found: " + variants.size());

                for (int i = 0; i < variants.size(); i++) {
                    try {
                        WebElement variant = variants.get(i);
                        String variantText = variant.getText().trim();

                        if (variant.isDisplayed() && variant.isEnabled()) {
                            wait.until(ExpectedConditions.elementToBeClickable(variant)).click();
                            System.out.println("   ✅ Variant selected: " + variantText);
                            sleep(500);
                        }
                    } catch (Exception ex) {
                        System.out.println("   ⚠️ Could not select variant " + (i + 1));
                    }
                }
            } else {
                System.out.println("⚠️ No variants available on this product");
            }

        } catch (Exception e) {
            System.out.println("⚠️ No variants present: " + e.getMessage());
        }
    }

    // ========== VERIFY ADD TO CART BUTTON OR OUT OF STOCK ==========
    public void verifyAddToCartOrOutOfStock() {
        try {
            // First check Out of Stock
            List<WebElement> outOfStock = driver.findElements(outOfStockText);
            if (!outOfStock.isEmpty()) {
                System.out.println("🚫 Product is OUT OF STOCK");
                return; // stop further checks
            }

            // Scroll and check Add to Cart
            try {
                driver.findElement(AppiumBy.androidUIAutomator(
                        "new UiScrollable(new UiSelector().scrollable(true))" +
                        ".scrollIntoView(new UiSelector().resourceIdMatches(\".*addtocart.*\"))"
                ));
                sleep(300);
            } catch (Exception ignored) {}

            WebElement addToCart = wait.until(ExpectedConditions.presenceOfElementLocated(addToCartBtn));
            boolean isVisible = addToCart.isDisplayed();
            boolean isClickable = addToCart.isEnabled();

            System.out.println("✅ Add to Cart button displayed: " + isVisible);
            System.out.println("✅ Add to Cart button clickable: " + isClickable);

        } catch (Exception e) {
            System.out.println("❌ Add to Cart button not found: " + e.getMessage());
        }
    }

    // ========== VERIFY BUY NOW BUTTON ==========
    public void verifyBuyNow() {
        try {
            // Skip if product is out of stock
            if (!driver.findElements(outOfStockText).isEmpty()) {
                System.out.println("🚫 Skipping Buy Now check — Product is Out of Stock");
                return;
            }

            WebElement buyNow = wait.until(ExpectedConditions.presenceOfElementLocated(buyNowBtn));

            boolean isVisible = buyNow.isDisplayed();
            boolean isClickable = buyNow.isEnabled();

            System.out.println("✅ Buy Now button displayed: " + isVisible);
            System.out.println("✅ Buy Now button clickable: " + isClickable);

        } catch (Exception e) {
            System.out.println("❌ Buy Now button not found: " + e.getMessage());
        }
    }

    // ========== NAVIGATE BACK TO HOMEPAGE ==========
    public void navigateToHomePage() {
        try {
            // First back (from PDP to listing)
            driver.navigate().back();
            sleep(1000);

            // Check if we are still on listing (not home)
            List<WebElement> listingProducts = driver.findElements(
                    AppiumBy.androidUIAutomator("new UiSelector().description(\"image\")")
            );

            if (listingProducts.size() > 0) {
                driver.navigate().back();
                System.out.println("↩️ Returned from Listing Page to Home Page");
            } else {
                System.out.println("🏠 Navigated back to Home Page successfully");
            }

        } catch (Exception e) {
            System.out.println("❌ Failed to navigate back: " + e.getMessage());
        }
    }

    // ========== COMPLETE PDP FLOW ==========
    public void executeProductPageFlow() {
        System.out.println("\n🛍️ ========== Starting Product Page Flow ==========\n");

        clickProductFromHome();
        verifyProductImage();
        verifyProductNameAndPrice();
        verifyAndSelectVariants();
        verifyAddToCartOrOutOfStock();
        verifyBuyNow();
        navigateToHomePage();

        System.out.println("\n✅ ========== Product Page Flow Completed! ==========\n");
    }

    // ========== HELPER ==========
    private void sleep(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
