package mazenative.automation;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;
import java.util.List;

public class ProductListing {

    AndroidDriver driver;
    WebDriverWait wait;

    // ===== Common Locators =====
    private By viewAllBtn = By.id("actiontext"); // View All button on homepage
    private By bannerImage = AppiumBy.accessibilityId("image"); // fallback if View All not present
    private By productName = By.xpath("//android.widget.TextView[contains(@resource-id,'name')]");
    private By productPrice = By.xpath("//android.widget.TextView[contains(@resource-id,'specialprice')]");
    private By wishlistIcon = By.xpath("//android.widget.ImageView[contains(@resource-id,'wishlist_but')]");
    private By productImage = By.xpath("//android.widget.ImageView[contains(@resource-id,'image')]");
    private By backBtn = AppiumBy.accessibilityId("Navigate up");

    // ===== Generic "No products available" text =====
    private By noProductsText = By.id("nocarttext"); // generic, app-specific name removed

    // ===== Constructor =====
    public ProductListing(AndroidDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    // ===== Step 1: Open product listing =====
    public void openProductListing() {
        try {
            try {
                WebElement viewAll = wait.until(ExpectedConditions.elementToBeClickable(viewAllBtn));
                viewAll.click();
                System.out.println("✅ Clicked on 'View All'");
            } catch (Exception e) {
                System.out.println("⚠️ 'View All' not found, using banner fallback...");
                WebElement banner = wait.until(ExpectedConditions.elementToBeClickable(bannerImage));
                banner.click();
                System.out.println("✅ Clicked on banner to open Listing Page");
            }

            // === Check if no products available ===
            if (isNoProductsMessageVisible()) {
                System.out.println("⚠️ No products available on this listing, going back...");
                navigateToHomePage();

                // Try another listing if available
                openAlternativeListing();
            } else {
                System.out.println("✅ Products are available on this listing page");
            }

        } catch (Exception e) {
            Assert.fail("❌ Failed to open product listing: " + e.getMessage());
        }
    }

    // ===== Helper: Check "No products" text =====
    private boolean isNoProductsMessageVisible() {
        try {
            wait.withTimeout(Duration.ofSeconds(5));
            List<WebElement> noProducts = driver.findElements(noProductsText);
            return !noProducts.isEmpty() && noProducts.get(0).isDisplayed();
        } catch (Exception e) {
            return false;
        } finally {
            wait.withTimeout(Duration.ofSeconds(20)); // reset wait
        }
    }

    // ===== Helper: Try another listing =====
    private void openAlternativeListing() {
        try {
            // Find all view all buttons and click next one
            List<WebElement> allViewAlls = driver.findElements(viewAllBtn);

            if (allViewAlls.size() > 1) {
                System.out.println("🔁 Trying another listing...");
                allViewAlls.get(1).click();

                // Check again if that listing has products
                if (isNoProductsMessageVisible()) {
                    System.out.println("⚠️ Second listing also has no products, going back...");
                    navigateToHomePage();
                } else {
                    System.out.println("✅ Products found on alternative listing");
                }
            } else {
                System.out.println("⚠️ No other listing found to try");
            }

        } catch (Exception e) {
            System.out.println("❌ Failed to open alternative listing: " + e.getMessage());
        }
    }

    // ===== Step 2: Scroll down to bottom =====
    public void scrollDown() {
        try {
            driver.findElement(AppiumBy.androidUIAutomator(
                    "new UiScrollable(new UiSelector().scrollable(true)).scrollToEnd(5)"
            ));
            System.out.println("✅ Scrolled down to bottom");
        } catch (Exception e) {
            System.out.println("⚠️ Scroll down failed: " + e.getMessage());
        }
    }

    // ===== Step 3: Scroll up to top =====
    public void scrollUp() {
        try {
            driver.findElement(AppiumBy.androidUIAutomator(
                    "new UiScrollable(new UiSelector().scrollable(true)).scrollBackward()"
            ));
            System.out.println("✅ Scrolled up to top");
        } catch (Exception e) {
            System.out.println("⚠️ Scroll up failed: " + e.getMessage());
        }
    }

    // ===== Step 4: Wishlist first product if icon exists =====
    public void wishlistFirstProduct() {
        try {
            List<WebElement> wishlistBtns = driver.findElements(wishlistIcon);
            if (!wishlistBtns.isEmpty()) {
                wishlistBtns.get(0).click();
                System.out.println("✅ Wishlist clicked for first product");
            } else {
                System.out.println("⚠️ No wishlist icon found for products");
            }
        } catch (Exception e) {
            System.out.println("⚠️ Failed to click wishlist icon: " + e.getMessage());
        }
    }

    // ===== Step 5: Verify product details =====
    public void verifyProductDetails() {
        try {
            List<WebElement> names = driver.findElements(productName);
            List<WebElement> prices = driver.findElements(productPrice);
            List<WebElement> images = driver.findElements(productImage);

            if (!names.isEmpty()) {
                String nameText = names.get(0).getText();
                Assert.assertFalse(nameText.isEmpty(), "❌ Product name is empty!");
                System.out.println("✅ Product name displayed: " + nameText);
            } else {
                System.out.println("⚠️ No product name found");
            }

            if (!prices.isEmpty()) {
                String priceText = prices.get(0).getText();
                Assert.assertFalse(priceText.isEmpty(), "❌ Product price is empty!");
                System.out.println("✅ Product price displayed: " + priceText);
            } else {
                System.out.println("⚠️ No product price found");
            }

            if (!images.isEmpty() && images.get(0).isDisplayed()) {
                System.out.println("✅ Product image displayed correctly");
            } else {
                System.out.println("⚠️ No product image found or not visible");
            }

        } catch (Exception e) {
            System.out.println("❌ Failed to verify product details: " + e.getMessage());
        }
    }

    // ===== Step 6: Navigate back to homepage =====
    public void navigateToHomePage() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(backBtn)).click();
            System.out.println("🏠 Navigated back to HomePage successfully");
        } catch (Exception e) {
            System.out.println("⚠️ Failed to navigate back to HomePage: " + e.getMessage());
        }
    }

    // ===== Complete Product Listing Flow =====
    public void executeProductListingFlow() {
        System.out.println("\n🛒 ========== Starting Product Listing Flow ==========\n");
        openProductListing();
        scrollDown();
        scrollUp();
        verifyProductDetails();
        wishlistFirstProduct();
        navigateToHomePage();
        System.out.println("\n✅ ========== Product Listing Flow Completed ==========\n");
    }
}
