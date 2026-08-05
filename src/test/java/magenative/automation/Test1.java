package magenative.automation;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import javax.swing.JFileChooser;

import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.*;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;

public class Test1 {

    AndroidDriver driver;
    WebDriverWait wait;
    CartPageTest cart;
    BaseSignUP baseSignUP;
    private BaseLogin baseLogin;
    private String appPackage;

    @BeforeTest
    public void setup() throws MalformedURLException {

        // File chooser for APK (commented for Jenkins)
//         JFileChooser chooser = new JFileChooser();
//         chooser.setDialogTitle("Select APK File");
//         int result = chooser.showOpenDialog(null);
//         if (result != JFileChooser.APPROVE_OPTION) {
//             throw new RuntimeException("No APK selected!");
//         }
//         String apkPath = chooser.getSelectedFile().getAbsolutePath();
//         System.out.println("APK selected: " + apkPath);

    	//changes for Jenkins
   	String apkPath = System.getProperty("apkPath"); // points to workspace\apkFile

    	// Check if apkPath is set
   	if (apkPath == null || apkPath.isEmpty()) {
   	    throw new RuntimeException("❌ System property 'apkPath' is not set!");
    	}

    	File apkFile = new File(apkPath);

   	if (!apkFile.exists()) {
   	    throw new RuntimeException("❌ APK not found at: " + apkFile.getAbsolutePath());
   	}

   	System.out.println("Using APK: " + apkFile.getAbsolutePath());
        
    	UiAutomator2Options options = new UiAutomator2Options()
                .setDeviceName("Android Emulator")
                .setPlatformName("Android")
                .setAutomationName("UiAutomator2")
                .setApp(apkFile.getAbsolutePath())
                .setUiautomator2ServerLaunchTimeout(Duration.ofSeconds(180))
                // Avoid a runtime permission popup stealing focus right after
                // install/launch — it can make Appium time out waiting for the
                // app's own activity to appear ("never started") before any
                // test even begins.
                .setAutoGrantPermissions(true);

        driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), options);
        appPackage = driver.getCapabilities().getCapability("appPackage").toString();
        // Old capabilities (commented)
         var caps = new org.openqa.selenium.remote.DesiredCapabilities();
         caps.setCapability("deviceName", "Android Emulator");
         caps.setCapability("platformName", "Android");
         caps.setCapability("automationName", "UiAutomator2");
         caps.setCapability("app", apkPath);
         caps.setCapability("uiautomator2ServerLaunchTimeout", 180000);
         caps.setCapability("unicodeKeyboard", true);
         caps.setCapability("resetKeyboard", true);

      //  driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), caps);

        wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));

        if (driver != null) {
            cart = new CartPageTest(driver, wait);
        } else {
            throw new RuntimeException("❌ Driver is null, cannot initialize CartPageTest");
        }
    }

    // ✅ Helper method - App responsive hai ya nahi check karne ke liye
    private void ensureAppIsResponsive() {
        ensureAppInForeground();
        try {
            wait.until(d -> {
                try {
                    String pageSource = driver.getPageSource();
                    return pageSource != null && !pageSource.isEmpty();
                } catch (Exception e) {
                    return false;
                }
            });
            Thread.sleep(500);
        } catch (Exception e) {
            System.out.println("⚠️ App may be in background, attempting recovery...");
            try {
                driver.getPageSource();
                Thread.sleep(1000);
            } catch (Exception ex) {
                System.out.println("❌ App recovery failed: " + ex.getMessage());
            }
        }
    }

    // ✅ If a scroll/gesture accidentally exits the app (home screen, notification
    // shade, another app), relaunch it instead of letting every later step fail.
    private void ensureAppInForeground() {
        try {
            String currentPackage = driver.getCurrentPackage();
            if (appPackage != null && !appPackage.equals(currentPackage)) {
                System.out.println("⚠️ App not in foreground (current: " + currentPackage
                        + "), reactivating " + appPackage + "...");
                driver.activateApp(appPackage);
                Thread.sleep(1500);
            }
        } catch (Exception e) {
            System.out.println("⚠️ Could not verify/reactivate foreground app: " + e.getMessage());
        }
    }

    @BeforeMethod
    public void beforeEachTest() {
        System.out.println("\n🔄 Preparing for next test...");
        ensureAppIsResponsive();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @AfterMethod
    public void afterEachTest() {
        System.out.println("✅ Test method completed\n");
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // @Test(priority = 1)
    // public void testPermissionPopup() {
    //     PermissionPage permissionPage = new PermissionPage(driver);
    //     permissionPage.clickAllow(); // For single popup
    //     // permissionPage.allowAllPermissions(); // For multiple
    // }

    @Test(priority = 1)
    public void signupOrLoginFlow() throws Exception {

        baseSignUP = new BaseSignUP(driver);
        baseLogin = new BaseLogin(driver);

        String email = "lodhi175@gmail.com";
        String password = "Test@1234";

        boolean signupAttempted = false;
        boolean signupSuccess = false;
        boolean emailExists = false;

        // -------------------------------
        // TRY SIGNUP
        // -------------------------------
        try {
            System.out.println("🚀 Trying Signup...");

            baseSignUP.doCompleteSignup("Amit", "Lodhi", email, password, password);

            signupAttempted = true;

            // Check if signup succeeded
            signupSuccess = baseSignUP.isSignupSuccess();

            if (signupSuccess) {
                System.out.println("🎉 Signup Successful, no need to login");
                return;
            }

            // Check duplicate email case
            emailExists = baseSignUP.isEmailAlreadyExists();

            if (emailExists) {
                System.out.println("⚠️ Email already exists, will try Login...");
            }

        } catch (Exception e) {
            System.out.println("⚠️ Signup flow crashed: " + e.getMessage());
        }

        // -------------------------------
        // DECISION
        // -------------------------------

        if (!signupAttempted || emailExists || !signupSuccess) {
            System.out.println("➡️ Trying Login...");

            try {
                baseLogin.doLoginFlow(email, password);
                System.out.println("✔️ Login Successful");
            } catch (Exception e) {
                System.out.println("❌ Login failed: " + e.getMessage());
            }

            return;
        }

        System.out.println("❌ Neither signup nor login happened — unexpected state");
    }



    @Test(priority = 3, description = "Account Section Flow Test")
    public void accountFlowTest() {
        AccountPage accountPage = new AccountPage(driver);
        try {
            accountPage.executeAccountFlow();
            Reporter.log("✅ Account flow test completed successfully", true);
        } catch (Exception e) {
            Reporter.log("❌ Account flow failed: " + e.getMessage(), true);
            e.printStackTrace();
        }
    }
//
    @Test(priority = 4)
    public void runWishlistFlow() {
        WishlistFlow wishlist = new WishlistFlow(driver);
        wishlist.wishlistProductFlow();
    }
//
    @Test(priority = 5)
    public void runSortingAndFilterFlow() {
        SortingAndFilterpage sortingFilter = new SortingAndFilterpage(driver);
        sortingFilter.openListingPage();
        sortingFilter.sortProductsAtoZ();
        sortingFilter.applyFilter();
    }

    @Test(priority = 6, description = "Product Page Flow Test")
    public void productPageFlowTest() {
        ProductPageTest productPage = new ProductPageTest(driver);
        try {
            productPage.executeProductPageFlow();
            Reporter.log("✅ Product page flow test completed successfully", true);
        } catch (Exception e) {
            Reporter.log("❌ Product page flow failed: " + e.getMessage(), true);
            e.printStackTrace();
        }
    }

    @Test(priority = 7)
    public void productListing() {
        ProductListing listingPage = new ProductListing(driver);
        try {
            listingPage.openProductListing();
            listingPage.scrollDown();
            listingPage.scrollUp();
            listingPage.wishlistFirstProduct();
            listingPage.verifyProductDetails();

            Reporter.log("🟢 Product listing flow executed successfully");
            System.out.println("🟢 Product listing flow executed successfully");
        } catch (Exception e) {
            Reporter.log("❌ Product listing flow failed: " + e.getMessage());
            System.out.println("❌ Product listing flow failed: " + e.getMessage());
            throw e;
        } finally {
            listingPage.navigateToHomePage();
        }
    }

    @Test(priority = 8)
    public void CartFlowTest() {
        System.out.println("🛒 Starting Cart Flow Test...");
        try {
            cart.completeCartFlow();
            System.out.println("✅ Cart Flow Test Completed Successfully!");
        } catch (Exception e) {
            System.out.println("❌ Cart Flow Test Failed: " + e.getMessage());
            e.printStackTrace();
            Assert.fail("Cart Flow Test encountered an error!");
        }
    }

    @AfterTest
    public void ScriptEnd() {
        System.out.println("🛑 Test Script ended...");
        if (driver != null) {
            // driver.quit();
        }
    }
}
