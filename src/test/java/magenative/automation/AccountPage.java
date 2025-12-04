package magenative.automation;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class AccountPage {
    AndroidDriver driver;
    WebDriverWait wait;

    // --- Common Locators (Works for all apps) ---
    private By sideDrawerBtn = AppiumBy.accessibilityId("open");
    private By accountIcon = By.xpath("//*[contains(@resource-id,'user_name')]");
    private By accountUserName = By.xpath("//*[contains(@resource-id,'signin')]");

    // Orders Section
    private By ordersMenu = By.xpath("//*[contains(@resource-id,'order')]");
    private By noOrdersMsg = By.xpath("//*[contains(@resource-id,'nocarttext')]");

    // Wishlist Section
    private By wishlistMenu = AppiumBy.androidUIAutomator(
            "new UiSelector().textContains(\"Wishlist\")"
    );
    private By noWishlistMsg = By.xpath("//*[contains(@resource-id,'nocarttext')]");

    // Navigation
    private By backBtn = AppiumBy.accessibilityId("Navigate up");

    // Constructor
    public AccountPage(AndroidDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    // ========== SIDE DRAWER & ACCOUNT ==========
    
    public void openSideDrawer() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(sideDrawerBtn)).click();
            sleep(500);
            System.out.println("✅ Side drawer opened");
        } catch (Exception e) {
            System.out.println("❌ Failed to open side drawer: " + e.getMessage());
        }
    }

    public void openAccountPage() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(accountIcon)).click();
            sleep(800);
            System.out.println("✅ Account page opened");
        } catch (Exception e) {
            System.out.println("❌ Failed to open account page: " + e.getMessage());
        }
    }

    // ========== USERNAME VERIFICATION ==========
    
    public void verifyAccountUsername() {
        try {
            WebElement usernameElement = wait.until(ExpectedConditions.visibilityOfElementLocated(accountUserName));
            String username = usernameElement.getText();
            
            if (username != null && !username.isEmpty()) {
                System.out.println("✅ Account Username Verified: " + username);
            } else {
                System.out.println("⚠️ Username is empty or not displayed");
            }
        } catch (Exception e) {
            System.out.println("❌ Failed to verify username: " + e.getMessage());
        }
    }

    // ========== ORDERS SECTION ==========
    
    public void openOrders() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(ordersMenu)).click();
            sleep(1000);
            System.out.println("✅ Orders section opened");
        } catch (Exception e) {
            System.out.println("❌ Failed to open orders section: " + e.getMessage());
        }
    }

    public void verifyOrders() {
        try {
            sleep(800); // Wait for page load
            List<WebElement> noOrders = driver.findElements(noOrdersMsg);
            
            if (noOrders.size() > 0 && noOrders.get(0).isDisplayed()) {
                String noOrderText = noOrders.get(0).getText();
                System.out.println("📭 Orders Available: false");
                System.out.println("   Message: " + noOrderText);
            } else {
                System.out.println("✅ Orders Available: true");
            }
        } catch (Exception e) {
            System.out.println("⚠️ Unable to verify orders: " + e.getMessage());
        }
    }

    // ========== WISHLIST SECTION ==========
    
    public void openWishlist() {
        try {
            // Go back to account page first
            goBackToAccountPage();
            
            wait.until(ExpectedConditions.elementToBeClickable(wishlistMenu)).click();
            sleep(1000);
            System.out.println("✅ Wishlist section opened");
        } catch (Exception e) {
            System.out.println("❌ Failed to open wishlist: " + e.getMessage());
        }
    }

    public void verifyWishlist() {
        try {
            sleep(800); // Wait for page load
            List<WebElement> noWishlist = driver.findElements(noWishlistMsg);
            
            if (noWishlist.size() > 0 && noWishlist.get(0).isDisplayed()) {
                String noWishlistText = noWishlist.get(0).getText();
                System.out.println("💔 Wishlist Products Available: false");
                System.out.println("   Message: " + noWishlistText);
            } else {
                System.out.println("✅ Wishlist Products Available: true");
            }
        } catch (Exception e) {
            System.out.println("⚠️ Unable to verify wishlist: " + e.getMessage());
        }
    }

    // ========== NAVIGATION METHODS ==========
    
    public void goBackToAccountPage() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(backBtn)).click();
            sleep(500);
            System.out.println("↩️ Back to Account Page");
        } catch (Exception e) {
            System.out.println("⚠️ Failed to go back: " + e.getMessage());
        }
    }

    public void navigateToHomePage() {
        try {
            // Back from current page to account page
            wait.until(ExpectedConditions.elementToBeClickable(backBtn)).click();
            sleep(500);
            
            // Back from account page to homepage
            wait.until(ExpectedConditions.elementToBeClickable(backBtn)).click();
            sleep(500);
            
            System.out.println("🏠 Navigated back to HomePage");
        } catch (Exception e) {
            System.out.println("❌ Failed to navigate to homepage: " + e.getMessage());
        }
    }

    // ========== COMPLETE FLOW METHOD ==========
    
    public void executeAccountFlow() {
        System.out.println("\n🚀 ========== Starting Account Flow Test ==========\n");
        
        // Step 1: Open Side Drawer & Account
        openSideDrawer();
        openAccountPage();
        
        // Step 2: Verify Username
        verifyAccountUsername();
        System.out.println(); // Blank line for readability
        
        // Step 3: Check Orders
        openOrders();
        verifyOrders();
        System.out.println(); // Blank line
        
        // Step 4: Check Wishlist
        openWishlist();
        verifyWishlist();
        System.out.println(); // Blank line
        
        // Step 5: Navigate Back to Homepage
        navigateToHomePage();
        
        System.out.println("✅ ========== Account Flow Test Completed! ==========\n");
    }

    // ========== HELPER METHOD ==========
    
    private void sleep(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}