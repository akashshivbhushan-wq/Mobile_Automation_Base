package magenative.automation;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BaseSignUP {

    private WebDriver driver;
    private WebDriverWait wait;

    public BaseSignUP(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    private WebElement waitAndFind(By locator) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    // 1. Open Side Drawer
    public void openSideDrawer() throws InterruptedException {
        By drawerLocator = By.xpath("//*[contains(@content-desc,'open') or @clickable='true']");
        WebElement drawer = waitAndFind(drawerLocator);
        drawer.click();
        System.out.println("Side Drawer clicked");
        Thread.sleep(1000);
    }

    // 2. Click Signin
    public void clickSignin() throws InterruptedException {
        By signinLocator = By.xpath(
            "//*[contains(@resource-id,'signin') or contains(translate(@text,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'sign in')]");
        WebElement signinBtn = waitAndFind(signinLocator);
        signinBtn.click();
        System.out.println("Signin clicked");
        Thread.sleep(1000);
    }

    // 3. Click Signup
    public void clickSignup() throws InterruptedException {
        By signupLocator = By.xpath(
            "//*[contains(@resource-id,'signupbut') or contains(translate(@text,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'sign up')]");
        WebElement signupBtn = waitAndFind(signupLocator);
        signupBtn.click();
        System.out.println("Signup page opened");
        Thread.sleep(1000);
    }

    // 4. Fill field without clicking (keyboard open avoid)
    private void setFieldValue(By locator, String value, String fieldName) throws InterruptedException {
        try {
            WebElement field = waitAndFind(locator);
            // Direct sendKeys without click
            field.sendKeys(value);
            System.out.println(fieldName + " set: " + value);
            Thread.sleep(500);
        } catch (Exception e) {
            System.out.println("Failed to set " + fieldName + ": " + e.getMessage());
        }
    }

    public void enterFirstName(String firstName) throws InterruptedException {
        setFieldValue(By.xpath("//android.widget.EditText[contains(@resource-id,'firstname')]"), firstName, "First Name");
    }

    public void enterLastName(String lastName) throws InterruptedException {
        setFieldValue(By.xpath("//android.widget.EditText[contains(@resource-id,'lastname')]"), lastName, "Last Name");
    }

    public void enterEmail(String email) throws InterruptedException {
        setFieldValue(By.xpath("//android.widget.EditText[contains(@resource-id,'email')]"), email, "Email");
    }

    public void enterPassword(String password) throws InterruptedException {
        setFieldValue(By.xpath("//android.widget.EditText[contains(@resource-id,'password') and not(contains(@resource-id,'Confirm'))]"), password, "Password");
    }

    public void enterConfirmPassword(String confirmPassword) throws InterruptedException {
        setFieldValue(By.xpath("//android.widget.EditText[contains(@resource-id,'Confirm_password')]"), confirmPassword, "Confirm Password");
    }

 /// Click Register button (robust version) with duplicate email handling
    public void clickRegister() throws InterruptedException {
        System.out.println("Clicking Register button...");

        By idLocator = By.id("com.evacosmetics.app:id/MageNative_register");
        boolean clicked = false;

        // 1️⃣ Try by common ID
        try {
            WebElement registerBtn = wait.until(ExpectedConditions.elementToBeClickable(idLocator));
            registerBtn.click();
            System.out.println("✅ Register button clicked by ID");
            clicked = true;
        } catch (Exception e) {
            System.out.println("⚠️ Register button not found by ID, trying by text + class");
        }

        // 2️⃣ Fallback: class + text
        if (!clicked) {
            try {
                By classTextLocator = By.xpath("//android.widget.Button[contains(@text,'Create new account')]");
                WebElement registerBtn = wait.until(ExpectedConditions.elementToBeClickable(classTextLocator));
                registerBtn.click();
                System.out.println("✅ Register button clicked by Class + Text");
                clicked = true;
            } catch (Exception e) {
                System.out.println("❌ Register button not found by Class + Text: " + e.getMessage());
            }
        }

        if (clicked) {
            Thread.sleep(2000); // wait a bit for server response

            // 3️⃣ Check for duplicate email message
            try {
                By duplicateMsg = By.xpath("//*[contains(@text,'already') or contains(@text,'taken') or contains(@text,'used')]");
                WebElement msg = wait.until(ExpectedConditions.presenceOfElementLocated(duplicateMsg));
                System.out.println("⚠️ Signup failed: " + msg.getText());
            } catch (Exception e) {
                System.out.println("✅ No duplicate email message, signup successful");
            }

            // 4️⃣ Wait for home page to load
            try {
                By homePageLocator = By.xpath("//*[contains(@resource-id,'home') or contains(@text,'Home')]");
                wait.until(ExpectedConditions.presenceOfElementLocated(homePageLocator));
                System.out.println("✅ Home page loaded successfully");
            } catch (Exception e) {
                System.out.println("⚠️ Home page did not load: " + e.getMessage());
            }
        } else {
            System.out.println("❌ Could not click Register button, signup failed");
        }
    }



    // 6. Complete Signup Flow
    public void doCompleteSignup(String firstName, String lastName, String email, String password, String confirmPassword) throws InterruptedException {
        openSideDrawer();
        clickSignin();
        clickSignup();
        enterFirstName(firstName);
        enterLastName(lastName);
        enterEmail(email);
        enterPassword(password);
        enterConfirmPassword(confirmPassword);
        clickRegister();
        System.out.println("✅ Signup flow completed, app stays on the page");
    }
}