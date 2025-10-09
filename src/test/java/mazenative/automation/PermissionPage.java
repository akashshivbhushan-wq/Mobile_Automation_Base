package mazenative.automation;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class PermissionPage {
    private AndroidDriver driver; 
    private WebDriverWait wait;

    // Locator defined properly
    private By allowButton = By.id("com.android.permissioncontroller:id/permission_allow_button");

    // Constructor
    public PermissionPage(AndroidDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // Single popup safe handling
    public void clickAllowIfPresent() {
        try {
            List<WebElement> allowBtns = driver.findElements(allowButton);
System.out.println("ALL_PERMISSION = "+driver.findElement(By.xpath("//android.widget.Button[@text='Allow']")));
System.out.println("ALL_PERMISSION_1 = "+driver.findElement(AppiumBy.androidUIAutomator("new UiSelector().text(\"Allow\")")));
grantPermissionViaADB("com.dento.app","android.permission.POST_NOTIFICATIONS");
/*
 * if (!allowBtn̥s.isEmpty()) { WebElement allowBtn = wait.until(
 * ExpectedConditions.elementToBeClickable(allowButton)); allowBtn.click();
 * System.out.
 * println("✅ Permission Allowed Successfully (Single Popup) allowBtns = "+
 * allowBtns); } else {
 * System.out.println("⚠️ No permission popup found at this moment allowBtns = "
 * +allowBtns); }
 */
        } catch (Exception e) {
            System.out.println("⚠️ Exception while handling permission popup: " + e.getMessage());
        }
    }
    
 // Grant permission via ADB before test execution 
    public void grantPermissionViaADB(String packageName, String permission) {  
    	try {    
    		String command = "adb shell pm grant " + packageName + " " + permission;         
    		Runtime.getRuntime().exec(command);        
    		Thread.sleep(2000);    
			System.out.println("ALL_PERMISSION = command = "+command);
    		} catch (Exception e) {     
    			System.out.println("ALL_PERMISSION = "+e.getMessage());
    			e.printStackTrace();   
    			} 
    	}

    // Multiple popups
    public void allowAllPermissions() {
        int count = 0;
        while (true) {
            try {
                List<WebElement> buttons = driver.findElements(allowButton);
                if (buttons.isEmpty()) break;

                for (WebElement btn : buttons) {
                    wait.until(ExpectedConditions.elementToBeClickable(btn)).click();
                    count++;
                    System.out.println("✅ Permission #" + count + " allowed");
                }
                Thread.sleep(500); 
            } catch (Exception e) {
                System.out.println("⚠️ No more permission popups detected: " + e.getMessage());
                break;
            }
        }
        System.out.println("✅ All permission popups handled successfully");
    }
}




