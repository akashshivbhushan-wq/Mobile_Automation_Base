package mazenative.automation;

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

	@BeforeTest
	public void setup() throws MalformedURLException {
        // File chooser for APK
		
		 JFileChooser chooser = new JFileChooser();
	 chooser.setDialogTitle("Select APK File"); int result =
		 chooser.showOpenDialog(null); if (result != JFileChooser.APPROVE_OPTION) {
	 throw new RuntimeException("No APK selected!"); }
		 String apkPath = chooser.getSelectedFile().getAbsolutePath();
		 System.out.println("APK selected: " + apkPath);
		 
        
		//changes for Jenkins
	//	String apkPath = System.getProperty("apkPath"); // points to workspace\apkFile

		// Check if apkPath is set
//		if (apkPath == null || apkPath.isEmpty()) {
//		    throw new RuntimeException("❌ System property 'apkPath' is not set!");
//		}
//
//		File apkFile = new File(apkPath);
//
//		if (!apkFile.exists()) {
//		    throw new RuntimeException("❌ APK not found at: " + apkFile.getAbsolutePath());
//		}
//
//		System.out.println("Using APK: " + apkFile.getAbsolutePath());

		// Existing UiAutomator2Options code continues unchanged
		UiAutomator2Options options = new UiAutomator2Options()
		        .setDeviceName("Android Emulator")
		        .setPlatformName("Android")
		        .setAutomationName("UiAutomator2")
		        .setApp((apkPath))
		        .setUiautomator2ServerLaunchTimeout(Duration.ofSeconds(180));

		driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), options);
		
		


        // Capabilities
		
		  var caps = new org.openqa.selenium.remote.DesiredCapabilities();
		  caps.setCapability("deviceName", "Android Emulator");
		  caps.setCapability("platformName", "Android");
		  caps.setCapability("automationName", "UiAutomator2");
		  caps.setCapability("app", apkPath);
		  caps.setCapability("uiautomator2ServerLaunchTimeout", 180000);
		  caps.setCapability("unicodeKeyboard", true);
		  caps.setCapability("resetKeyboard", true);

		  
		  // Driver + Wait setup driver = new AndroidDriver(new
		//  URL("http://127.0.0.1:4723/wd/hub"), caps);
		 
        
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        
        //if (driver != null) {
          //  cart = new CartPageTest(driver, wait);
            //baseSignUP = new BaseSignUP(driver, wait);  // ✅ Correct initialization
      //  } else {
        //    throw new RuntimeException("❌ Driver is null, cannot initialize CartPageTest");
      //  }
    
     // Initialize CartPageTest with updated constructor
        if (driver != null) {
            cart = new CartPageTest(driver, wait);  // ✅ Updated CartPageTest constructor accepts driver + wait
        } else {
            throw new RuntimeException("❌ Driver is null, cannot initialize CartPageTest");
        }
	}
	
//	@Test(priority = 1)
//	public void testPermissionPopup() {
//	    PermissionPage permissionPage = new PermissionPage(driver);
//
//	    // For single popup
//	    permissionPage.clickAllow();
//
//	    // OR if multiple permissions (camera, storage, location etc.)
//	     permissionPage.allowAllPermissions();
//	}
    // ✅ BaseSignUP object initialize
 
//	@Test(priority = 1)
//	public void openSignupAndFillTest() throws Exception {
//	    try {
//	        // Initialize BaseSignUP with driver (assume driver already initialized in @BeforeTest)
//	        if (baseSignUP == null) {
//	            baseSignUP = new BaseSignUP(driver);
//	        }
//
//	        System.out.println("🚀 Starting Signup Test...");
//
//	        // Complete signup flow with values
//	        baseSignUP.doCompleteSignup(
//	            "Amit",                  // First Name
//	            "Lodhi",                 // Last Name
//	            "lodhi175@gmail.com",    // Email
//	            "Test@1234",             // Password
//	            "Test@1234"              // Confirm Password
//	        );
//
//	        System.out.println("✅ Signup test finished, app stays on the page");
//
//	    } catch (Exception e) {
//	        System.out.println("❌ Error during signup test: " + e.getMessage());
//	        e.printStackTrace();
//	    }
//
//	}
//	
	
	
	 private BaseLogin baseLogin;
	
    @Test(priority = 1)
    public void loginTest() throws InterruptedException {
        // Initialize BaseLogin with your driver
        baseLogin = new BaseLogin(driver);

        // Call login flow with provided credentials
        baseLogin.doLoginFlow(
            "lodhi175@gmail.com",   // Email
            "Test@1234"             // Password
        );

        System.out.println("✅ Login test finished, app stays on the homepage");
    }
//
////////    
///////
//  // 🔹 Account Page Test
  @Test(priority = 2, description = "Account Section Flow Test")
  public void accountFlowTest() {
      AccountPage accountPage = new AccountPage(driver);

      try {
          // Execute complete account flow (one liner)
          accountPage.executeAccountFlow();
          
          Reporter.log("✅ Account flow test completed successfully", true);
          
      } catch (Exception e) {
          Reporter.log("❌ Account flow failed: " + e.getMessage(), true);
          e.printStackTrace();
      }
  }
  
  @Test(priority = 3)
  public void runWishlistFlow() {
    WishlistFlow wishlist = new WishlistFlow(driver);
    wishlist.wishlistProductFlow();  // ✅ call updated flow
}

//// 
@Test(priority = 4)
public void runSortingAndFilterFlow() {
    SortingAndFilterpage sortingFilter = new SortingAndFilterpage(driver);
    sortingFilter.openListingPage();
    sortingFilter.sortProductsAtoZ();
    sortingFilter.applyFilter();
}

//
//
 // Test 4: Product Page Flow
    @Test(priority = 5, description = "Product Page Flow Test")
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
    // Uncomment and update below if Cart Flow needed
    @Test(priority = 6)
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
            throw e; // Rethrow to mark test as failed

        } finally {
            // ✅ Always navigate back to homepage
            listingPage.navigateToHomePage();
        }
    }
//
	 @Test(priority = 7)
	 public void CartFlowTest() {
	     System.out.println("🛒 Starting Cart Flow Test...");
	     try {
	         // Use the already initialized cart object from setup()
	         cart.completeCartFlow();  // Run full cart flow
	         System.out.println("✅ Cart Flow Test Completed Successfully!");
	     } catch (Exception e) {
	         System.out.println("❌ Cart Flow Test Failed: " + e.getMessage());
	         e.printStackTrace();
	         Assert.fail("Cart Flow Test encountered an error!");
	     }
	 }



 
    







    
//@Test(priority =1 )
//public void productListingFlowTest() {
//    ProductListing listingPage = new ProductListing(driver);
//
//    try {
//        listingPage.openProductListing();
//        listingPage.scrollDown();
//        listingPage.scrollUp();
//        listingPage.wishlistFirstProduct();
//        listingPage.verifyProductDetails();
//
//        Reporter.log("🟢 Product listing flow executed successfully");
//        System.out.println("🟢 Product listing flow executed successfully");
//
//    } catch (Exception e) {
//        Reporter.log("❌ Product listing flow failed: " + e.getMessage());
//        System.out.println("❌ Product listing flow failed: " + e.getMessage());
//        throw e; // Rethrow to mark test as failed
//
//    } finally {
//        // ✅ Always navigate back to homepage
//        listingPage.navigateToHomePage();
//    }
//}

    
    
    
    
//    
//    @Test(priority = 2)
//    public void addressFlowTest() {
//        AddressPage addressPage = new AddressPage(driver);
//
//        try {
//            // --- Add Address ---
//            addressPage.addAddress(
//                "Akash", "test", "lucknow", "lucknow",
//                "India", "Uttar Pradesh", "Lucknow", "226001", "7020707069"
//            );
//            Reporter.log("🎉 Address successfully created", true);
//
//            // --- Edit Address ---
//            addressPage.editAddress("Tester1");
//            Reporter.log("✏️ Edit address done successfully", true);
//
//            // --- Delete Address ---
//            addressPage.deleteAddress();
//            Reporter.log("🗑️ Address deleted successfully", true);
//
//        } catch (Exception e) {
//            Reporter.log("❌ Address flow failed: " + e.getMessage(), true);
//            e.printStackTrace();
//        } finally {
//            // --- Navigate back to homepage ---
//            try {
//                addressPage.navigateBackToHomePage();
//                Reporter.log("🏠 Navigated back to HomePage", true);
//            } catch(Exception e){
//                Reporter.log("❌ Failed to navigate back: " + e.getMessage(), true);
//            }
//        }
//    }


    @AfterTest
    public void ScriptEnd() {
        System.out.println("🛑 Test Script ended...");
        if (driver != null) {
          //  driver.quit(); // Close driver after test
        }
    }
}