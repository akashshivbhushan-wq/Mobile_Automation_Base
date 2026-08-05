package magenative.automation;

import java.time.Duration;
import java.util.regex.Pattern;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BaseLogin {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    private WebDriver driver;
    private WebDriverWait wait;

    public BaseLogin(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    // ======================================================
    // Helper: wait and find element
    // ======================================================
    private WebElement waitAndFind(By locator) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    // ======================================================
    // Helper: click element
    // ======================================================
    private void clickElement(By locator) throws InterruptedException {
        try {
            WebElement el = wait.until(ExpectedConditions.elementToBeClickable(locator));
            el.click();
            Thread.sleep(500);
        } catch (Exception e) {
            System.out.println("⚠️ Failed to click: " + locator + " Error: " + e.getMessage());
        }
    }

    // ======================================================
    // Helper: set field value
    // ======================================================
    private void setFieldValue(By locator, String value, String fieldName) throws InterruptedException {
        try {
            WebElement field = waitAndFind(locator);
            field.clear();
            field.sendKeys(value);
            System.out.println(fieldName + " set: " + value);
        } catch (Exception e) {
            System.out.println("⚠️ Failed to set " + fieldName + ": " + e.getMessage());
        }
    }

    // ======================================================
    // Detect if already logged-in
    // ======================================================
    public boolean isAlreadyLoggedIn() {
        try {
            By homePageLocator = By.xpath(
                    "//*[contains(@resource-id,'home') or contains(@text,'Home')]");

            wait.until(ExpectedConditions.presenceOfElementLocated(homePageLocator));
            System.out.println("✔️ Already logged in");
            return true;

        } catch (Exception e) {
            System.out.println("ℹ️ Not logged in");
            return false;
        }
    }

    // ======================================================
    // Open sidebar
    // ======================================================
    public void openSideDrawer() throws InterruptedException {

        // If already logged-in → no sidebar needed
        if (isAlreadyLoggedIn()) {
            return;
        }

        By drawerLocator = By.xpath("//*[contains(@content-desc,'open') or @clickable='true']");
        clickElement(drawerLocator);
        System.out.println("➡️ Sidebar opened");
    }

    // ======================================================
    // Click Signin button
    // ======================================================
    public void clickSignin() throws InterruptedException {
        By signinLocator = By.xpath(
            "//*[contains(@resource-id,'signin') or contains(translate(@text,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'sign in')]");

        clickElement(signinLocator);
        System.out.println("➡️ Signin button clicked");
    }

    // ======================================================
    // Check if email format is valid
    // ======================================================
    public boolean isValidEmail(String email) {
        boolean isValid = email != null && EMAIL_PATTERN.matcher(email).matches();
        if (isValid) {
            System.out.println("✔️ Email format is valid: " + email);
        } else {
            System.out.println("⚠️ Invalid email format: " + email);
        }
        return isValid;
    }

    // ======================================================
    // Enter email
    // ======================================================
    public void enterEmail(String email) throws InterruptedException {
        By emailLocator = By.xpath("//android.widget.EditText[contains(@resource-id,'username')]");
        setFieldValue(emailLocator, email, "Email");
    }

    // ======================================================
    // Enter password
    // ======================================================
    public void enterPassword(String password) throws InterruptedException {
        By passwordLocator = By.xpath(
            "//android.widget.EditText[contains(@resource-id,'password') and not(contains(@resource-id,'Confirm'))]");
        setFieldValue(passwordLocator, password, "Password");
    }

    // ======================================================
    // Click Login button
    // ======================================================
    public void clickLogin() throws InterruptedException {
        By loginLocator = By.xpath(
            "//android.widget.Button[contains(@resource-id,'login') or contains(translate(@text,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'sign in')]");

        clickElement(loginLocator);
        System.out.println("➡️ Login button clicked");

        // Wait for home page
        try {
            By homePageLocator = By.xpath(
                "//*[contains(@resource-id,'home') or contains(@text,'Home')]");

            wait.until(ExpectedConditions.presenceOfElementLocated(homePageLocator));
            System.out.println("✔️ Login success, Home page visible");

        } catch (Exception e) {
            System.out.println("⚠️ Login may have failed: " + e.getMessage());
        }
    }

    // ======================================================
    // MAIN FUNCTION CALLED BY YOUR TEST
    // ======================================================
    public void doLoginFlow(String email, String password) throws InterruptedException {

        // Step 1: If already logged-in → skip everything
        if (isAlreadyLoggedIn()) {
            System.out.println("✔️ Skipping login, user is already logged in");
            return;
        }

        // Step 2: Go to login
        openSideDrawer();
        clickSignin();

        // Step 3: Fill fields
        enterEmail(email);
        enterPassword(password);

        // Step 4: Submit form
        clickLogin();
    }

}
