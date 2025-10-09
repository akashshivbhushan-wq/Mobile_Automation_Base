package mazenative.automation;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;
import java.net.URL;

public class MagenativeApp {
    public static void main(String[] args) throws MalformedURLException, InterruptedException {

        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("platformName", "Android");
        caps.setCapability("automationName", "uiautomator2");
        caps.setCapability("deviceName", "AndroidEmulator");
        caps.setCapability("app", "C:\\Users\\AkashShivBhushan2442\\apkfiles\\barsache.apk");
        caps.setCapability("uiautomator2ServerLaunchTimeout", 180000);
        caps.setCapability("appPackage", "com.baersachenew.app");
        caps.setCapability("appActivity", "com.baersachenew.app.homesection.activities.HomePage");
        caps.setCapability("autoGrantPermissions", true);
        caps.setCapability("noReset", true);

        // ✅ Use WebElement instead of MobileElement
        AndroidDriver driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), caps);

        Thread.sleep(5000);

        // ✅ New locator syntax (AppiumBy)
        driver.findElement(AppiumBy.xpath("//android.widget.ImageButton[@content-desc='open']")).click();
        Thread.sleep(2000);

        driver.findElement(AppiumBy.xpath("//android.widget.TextView[@resource-id='com.baersachenew.app:id/headertext']")).click();
        Thread.sleep(2000);

        driver.findElement(AppiumBy.xpath("//android.widget.TextView[@resource-id='com.baersachenew.app:id/signupbut']")).click();
        Thread.sleep(2000);

        driver.findElement(AppiumBy.xpath("//android.widget.EditText[@resource-id='com.baersachenew.app:id/firstname']")).sendKeys("Magenativr");
        driver.findElement(AppiumBy.xpath("//android.widget.EditText[@resource-id='com.baersachenew.app:id/lastname']")).sendKeys("Tester");
        driver.findElement(AppiumBy.xpath("//android.widget.EditText[@resource-id='com.baersachenew.app:id/email']")).sendKeys("Testerpq@gmail.com");
        driver.findElement(AppiumBy.xpath("//android.widget.EditText[@resource-id='com.baersachenew.app:id/password']")).sendKeys("Passp1@123");
        driver.findElement(AppiumBy.xpath("//android.widget.EditText[@resource-id='com.baersachenew.app:id/Confirm_password']")).sendKeys("Passp1@123");

        driver.findElement(AppiumBy.xpath("//android.widget.Button[@resource-id='com.baersachenew.app:id/MageNative_register']")).click();

      //  Thread.sleep(5000);

        System.out.println("✅ Signup test executed successfully!");

        driver.quit();

    }
}







    


    







       
    



        
   


 


    


    


    



    


    



    





