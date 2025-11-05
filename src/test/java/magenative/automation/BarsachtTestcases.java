package mazenative.automation;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.*;
import org.testng.annotations.*;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.List;

//import javax.swing.JFileChooser;

// Attach listener
//public class BarsachtTestcases {
//
//	AndroidDriver driver;
//	WebDriverWait wait;
//
//	// -------------------- SETUP --------------------
//	@BeforeTest
//	public void setup(ITestContext context) throws MalformedURLException {
//		
////		String apk_path = BarsachtTestcases.getFilePath();
//		String apkPath = FileUploadDialog.showFileUploadDialogAndSaveToProject();
//		System.out.println("apk path : " + apkPath);
//
//
////public class BarsachtTestcases {
////    private String apkPath;
////    private AndroidDriver driver;
////    private WebDriverWait wait;
////
////    public BarsachtTestcases(String apkPath) {
////        this.apkPath = apkPath;
////    }
////
////    @BeforeTest
////    public void setup(ITestContext context) throws MalformedURLException {
////        if (apkPath == null) {
////            System.out.println("APK path missing");
////            return;
////        }
//
//		if (apkPath != null) {
////			File app = new File(apk_path);
//			DesiredCapabilities caps = new DesiredCapabilities();
////			caps.setCapability("platformName", "Android");
////			caps.setCapability("automationName", "uiautomator2");
////			caps.setCapability("deviceName", "AndroidEmulator");
////			caps.setCapability("app", "C:\\Users\\AkashShivBhushan2442\\apkfiles\\barsache.apk");
////			caps.setCapability("app", app.getAbsolutePath());
//			caps.setCapability("deviceName", "Android Emulator");
//	        caps.setCapability("platformName", "Android");
//	        caps.setCapability("automationName", "UiAutomator2");
//			caps.setCapability("app", apkPath);
//			caps.setCapability("uiautomator2ServerLaunchTimeout", 180000);
////			caps.setCapability("appPackage", "com.baersachenew.app");
////			caps.setCapability("appActivity", "com.baersachenew.app.homesection.activities.HomePage");
////			caps.setCapability("autoGrantPermissions", true);
////			caps.setCapability("noReset", true);
//	
//			driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), caps);
//			wait = new WebDriverWait(driver, Duration.ofSeconds(30));
//	
//			// driver ko context me daalo taki listener access kar sake
//			context.setAttribute("driver", driver);
//		} else {
//            System.out.println("APK file not selected. Test failed.");
//        }
//	}
//
//	// -------------------- TEST CASE --------------------
//	@Test
//	public void signupAndCheckoutTest() throws InterruptedException {
//		
//		
//		try {
//		    // Wait for 1 second for popup to appear
//		    Thread.sleep(1000);
//
//		    // Try to find and click the "Allow" button
//		    WebElement allowButton = driver.findElement(AppiumBy.xpath("//*[@text='Allow']"));
//		    allowButton.click();
//
//		    System.out.println("Clicked on Allow button.");
//
//		} catch (Exception e) {
//		    System.out.println("Allow button not found or already handled.");
//		}
//		
//		Thread.sleep(10000);
//
//		// -------------------- SIDEDRAWER --------------------
//		driver.findElement(AppiumBy.xpath("//android.widget.ImageButton[@content-desc='open']")).click();
//		Thread.sleep(2000);
//		//android.widget.ImageButton[@content-desc="open"]
//
//		// -------------------- SIGNUP FLOW --------------------
//		driver.findElement(AppiumBy.xpath("//android.view.ViewGroup[@resource-id=\"com.baersachenew.app:id/seconddivision\"]")).click();
//		driver.findElement(AppiumBy.xpath("//*[@text='Sign in']")).click();
//		Thread.sleep(2000);
//
//		try {
//			// 🔹 Step 1: Check if Signup button exists
//			WebElement signupBtn = driver.findElement(AppiumBy.xpath("//*[@text='Sign in']"));
//			if (signupBtn.isDisplayed()) {
//				System.out.println("✅ Signup button found, clicking...");
//				signupBtn.click();
//				Thread.sleep(5000);
//				this.Signup();
//			}
//		} catch (NoSuchElementException e1) {
//			System.out.println("❌ Signup button not found. Checking for Sign Out...");
//
//			try {
//				// 🔹 Step 2: Check if Sign Out exists
//				WebElement signOutTxt = driver
//						.findElement(AppiumBy.xpath("//android.widget.TextView[@text='Sign Out']"));
//				if (signOutTxt.isDisplayed()) {
//					System.out.println("✅ Sign Out found, now checking for menu item...");
//					try {
//						// 🔹 Step 3: Check for menu item
//						WebElement menuItem = driver.findElement(AppiumBy.xpath(
//								"(//android.view.ViewGroup[@resource-id='com.baersachenew.app:id/menuItem'])[1]"));
//						if (menuItem.isDisplayed()) {
//							System.out.println("✅ Menu item found, clicking...");
//							menuItem.click();
//						}
//					} catch (NoSuchElementException e3) {
//						this.takeScreenshot(driver, "not_fount");
//						try {
//							WebElement backArrow = driver.findElement(
//									AppiumBy.xpath("//android.widget.ImageButton[@content-desc=\"Navigate up\"]"));
//							if (backArrow.isDisplayed()) {
//								System.out.println("✅ Menu item found, clicking...");
//								backArrow.click();
//								Thread.sleep(5000);
//								this.Checkout();
//							}
//						} catch (Exception e4) {
//							System.out.println("❌ Neither Signup button nor Back Arrow found.");// TODO: handle
//																								// exception
//						}
//					}
//				}
//			} catch (NoSuchElementException e2) {
//				System.out.println("❌ Neither Signup button nor Sign Out found.");
//			}
//		}

//        driver.findElement(AppiumBy.id("com.baersachenew.app:id/signupbut")).click();
//        driver.findElement(AppiumBy.xpath("//android.widget.TextView[@text=\"Sign Out\"]")).click();

//        Thread.sleep(5000);
//        Thread.sleep(5000);
//        String successMsg = driver.findElement(AppiumBy.id("com.baersachenew.app:id/success_message")).getText();
//        
//        if(successMsg == "Successfully login") {
//        	System.out.println("Calling Checkout function.");
//            this.Checkout();
//        	Assert.assertEquals(successMsg, "Successfully login");
//        }

//        System.err.println("Outside of if condition.");
//        this.Checkout();
		// ✅ Verify error message
//        String errorMsg = driver.findElement(AppiumBy.id("com.baersachenew.app:id/error_message")).getText();
//        Assert.assertEquals(errorMsg, "passwords not match");

//		System.out.println("✅ Signup test executed successfully!");
//		Thread.sleep(5000);
//
//		// Yaha aap checkout flow bhi add kar sakte ho
//		driver.quit();
//	}

	// -------------------- TEARDOWN --------------------
//    @AfterTest
//    public void teardown() {
//        if (driver != null) {
//            driver.quit();
//        }
//    }

//	public void takeScreenshot(AndroidDriver drive, String ss_name){
//		if (driver != null) {
//			try {
//				File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
//				String timestamp = new java.text.SimpleDateFormat("yyyyMMdd_HHmmss").format(new java.util.Date());
//				File dest = new File("Screenshots/" + ss_name + "_" + timestamp + ".png");
//				FileUtils.copyFile(src, dest);
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//	}

//	public void Signup() throws InterruptedException {
//		driver.findElement(AppiumBy.id("com.baersachenew.app:id/firstname")).sendKeys("Magenative");
//		driver.findElement(AppiumBy.id("com.baersachenew.app:id/lastname")).sendKeys("Tester");
//		driver.findElement(AppiumBy.id("com.baersachenew.app:id/email"))
//				.sendKeys("Tester5" + System.currentTimeMillis() + "@gmail.com");
//		driver.findElement(AppiumBy.id("com.baersachenew.app:id/password")).sendKeys("pass1pk1@1234k");
//		driver.findElement(AppiumBy.id("com.baersachenew.app:id/Confirm_password")).sendKeys("pass1pk1@1234k");
//		driver.findElement(AppiumBy.id("com.baersachenew.app:id/MageNative_register")).click();
//		Thread.sleep(3000);
		
		// XPath for all error fields
//		List<WebElement> errorFields = driver.findElements(
//		        AppiumBy.xpath("//android.widget.TextView[@resource-id='com.baersachenew.app:id/textinput_error']"));
//
//		boolean errorFound = false;
//
//		for (int i = 0; i < errorFields.size(); i++) {
//		    WebElement error = errorFields.get(i);
//
//		    try {
//		        if (error.isDisplayed()) {
//		            System.out.println("❌ Error found on field " + (i + 1) + ": " + error.getText());
//		            errorFound = true;
//		        }
//		    } catch (Exception e) {
		        // ignore if element not visible
//		    }
//		}
//
//		// Agar error mila hai to screenshot le lo
//		if (errorFound) {
//		    System.out.println("📸 Screenshot taken for error.");
//			Assert.fail("Signup credential is not correct.");
//		} else {
//	        System.out.println("Calling Checkout function.");
//	        this.Checkout();
//		}

		
//		String successMsg = driver.findElement(AppiumBy.id("com.baersachenew.app:id/success_message")).getText();

//		if (successMsg == "Successfully login") {
//
//		} else {
//			Assert.fail("Signup failed.");
//		}
		
//	    if(successMsg.equals("Successfully login")) {   // ✅ .equals() use karna zaroori hai
//	        System.out.println("Calling Checkout function.");
//	        this.Checkout();
//	        Assert.assertEquals(successMsg, "Successfully login");
//	    } else {
//			System.out.println("Calling Checkout function.");
//			this.takeScreenshot(driver,"Login_success");
//			this.Checkout();
//	    }
		
//	}
//
//	public void Checkout() throws InterruptedException {
//		// -------------------- CLICK ON BANNER --------------------
//		WebElement banner = wait.until(ExpectedConditions.elementToBeClickable(
//				AppiumBy.xpath("(//android.widget.ImageView[@resource-id='com.baersachenew.app:id/image'])[1]")));
//		banner.click();
//		System.out.println("✅ Banner clicked successfully!");
//		Thread.sleep(3000);
//		// -------------------- CLICK ON FIRST PRODUCT --------------------
//		WebElement product = wait.until(ExpectedConditions.elementToBeClickable(
//				AppiumBy.xpath("(//android.widget.ImageView[@resource-id='com.baersachenew.app:id/image'])[1]")));
//		product.click();
//		System.out.println("✅ Product clicked successfully!");
//		Thread.sleep(3000);
//		// -------------------- SELECT VARIANT --------------------
//		WebElement variant = driver
//				.findElement(AppiumBy.androidUIAutomator("new UiScrollable(new UiSelector().scrollable(true))"
//						+ ".scrollIntoView(new UiSelector().resourceId(\"com.baersachenew.app:id/variant_name\").text(\"6\"))"));
//		variant.click();
//		System.out.println("✅ Variant selected!");
//		// -------------------- ADD TO BAG --------------------
//		WebElement addToBag = wait.until(ExpectedConditions.elementToBeClickable(
//				AppiumBy.xpath("//android.widget.TextView[@resource-id='com.baersachenew.app:id/addtocart']")));
//		addToBag.click();
//		System.out.println("✅ Product added to bag!");
//		Thread.sleep(2000);
//		// -------------------- GO TO BAG --------------------
//		WebElement goToBag = wait.until(ExpectedConditions.elementToBeClickable(
//				AppiumBy.xpath("//android.widget.TextView[@resource-id='com.baersachenew.app:id/addtocart']")));
//		goToBag.click();
//		System.out.println("✅ Navigated to cart!");
//		// -------------------- APPLY DISCOUNT COUPON --------------------
//		WebDriverWait wait1 = new WebDriverWait(driver, Duration.ofSeconds(20));
//		WebElement applyCouponBtn = wait1.until(ExpectedConditions
//				.elementToBeClickable(AppiumBy.xpath("//android.widget.TextView[@text='Apply Discount Coupon']")));
//		applyCouponBtn.click();
//		System.out.println("✅ Apply Discount Coupon clicked!");
//		WebElement couponInput = wait1.until(ExpectedConditions.presenceOfElementLocated(
//				AppiumBy.xpath("//android.widget.EditText[@resource-id='com.baersachenew.app:id/discount_code_edt']")));
//		couponInput.sendKeys("DEAL5");
//		System.out.println("✅ Coupon code entered!");
//		WebElement applyBtn = wait1.until(ExpectedConditions.elementToBeClickable(
//				AppiumBy.xpath("//android.widget.Button[@resource-id='com.baersachenew.app:id/discount_code_btn']")));
//		applyBtn.click();
//		System.out.println("✅ Apply button clicked!");
//		System.out.println("No error message. Assuming coupon applied successfully.");
//		// -------------------- PROCEED TO CHECKOUT --------------------
//		WebElement checkoutBtn = wait1.until(ExpectedConditions.elementToBeClickable(
//				AppiumBy.xpath("//android.view.ViewGroup[@resource-id='com.baersachenew.app:id/proceed']")));
//		checkoutBtn.click();
//		System.out.println("✅ Clicked on Proceed to Checkout!");
//		// -------------------- WAIT FOR WEBVIEW --------------------
//		boolean webviewFound = false;
//		for (int i = 0; i < 15; i++) {
//			// wait up to 15 seconds
//			for (String contextName : driver.getContextHandles()) {
//				System.out.println("Available context: " + contextName);
//				if (contextName.contains("WEBVIEW")) {
//					driver.context(contextName);
//					System.out.println("✅ Switched to WebView context: " + contextName);
//					webviewFound = true;
//					break;
//				}
//			}
//			if (webviewFound)
//				break;
//			Thread.sleep(1000);
//		}
//		if (!webviewFound) {
//			System.out.println("⚠ WebView context not found. Checkout page may not have loaded yet.");
//		} else {
//			// Example: click a place order button inside WebView
//			WebDriverWait webWait = new WebDriverWait(driver, Duration.ofSeconds(20));
//			WebElement placeOrder = webWait
//					.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button.place-order")));
//			placeOrder.click();
//			System.out.println("✅ Place Order clicked inside WebView!");
//		}
//	}
//}
