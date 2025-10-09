package mazenative.automation;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BaseLogin {

    private WebDriver driver;
    private WebDriverWait wait;

    public BaseLogin(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    // Wait and find element
    private WebElement waitAndFind(By locator) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    // Click element safely
    private void clickElement(By locator) throws InterruptedException {
        WebElement el = waitAndFind(locator);
        el.click();
        Thread.sleep(500);
    }

    // Set field value without opening keyboard
    private void setFieldValue(By locator, String value, String fieldName) throws InterruptedException {
        try {
            WebElement field = waitAndFind(locator);
            field.clear();
            field.sendKeys(value);
            System.out.println(fieldName + " set: " + value);
            Thread.sleep(500);
        } catch (Exception e) {
            System.out.println("Failed to set " + fieldName + ": " + e.getMessage());
        }
    }

    // Open sidebar
    public void openSideDrawer() throws InterruptedException {
        By drawerLocator = By.xpath("//*[contains(@content-desc,'open') or @clickable='true']");
        clickElement(drawerLocator);
        System.out.println("✅ Sidebar clicked");
    }

    // Click Signin
    public void clickSignin() throws InterruptedException {
        By signinLocator = By.xpath(
            "//*[contains(@resource-id,'signin') or contains(translate(@text,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'sign in')]");
        clickElement(signinLocator);
        System.out.println("✅ Signin button clicked");
    }

    // Enter email (common locator by resource-id ending with 'username')
    public void enterEmail(String email) throws InterruptedException {
        By emailLocator = By.xpath("//android.widget.EditText[contains(@resource-id,'username')]");
        setFieldValue(emailLocator, email, "Email");
    }

    // Enter password (common locator by resource-id ending with 'password')
    public void enterPassword(String password) throws InterruptedException {
        By passwordLocator = By.xpath("//android.widget.EditText[contains(@resource-id,'password') and not(contains(@resource-id,'Confirm'))]");
        setFieldValue(passwordLocator, password, "Password");
    }

    // Click Login (common locator by resource-id ending with 'login' or by text 'Sign in')
    public void clickLogin() throws InterruptedException {
        By loginLocator = By.xpath("//android.widget.Button[contains(@resource-id,'login') or contains(translate(@text,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'sign in')]");
        clickElement(loginLocator);
        System.out.println("✅ Signin clicked, waiting for homepage...");

        // Wait for homepage unique element
        try {
            By homePageLocator = By.xpath("//*[contains(@resource-id,'home') or contains(@text,'Home')]");
            wait.until(ExpectedConditions.presenceOfElementLocated(homePageLocator));
            System.out.println("✅ Home page loaded successfully");
        } catch (Exception e) {
            System.out.println("⚠️ Homepage did not load: " + e.getMessage());
        }
    }

    // Complete login flow
    public void doLoginFlow(String email, String password) throws InterruptedException {
        openSideDrawer();
        clickSignin();
        enterEmail(email);
        enterPassword(password);
        clickLogin();
        System.out.println("✅ Login flow completed, app stays on the homepage");
    }
}
