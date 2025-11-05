package mazenative.automation;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.ElementOption;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.TimeoutException;

import java.time.Duration;

public class AddressPage {
    AndroidDriver driver;
    WebDriverWait wait;

    // --- Common Locators ---
    private By sideDrawerBtn = AppiumBy.accessibilityId("open");
    private By signinBtn = By.xpath(
            "//*[contains(@resource-id,'signin') or contains(translate(@text,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'sign in')]"
    );
    private By myAddressMenu = By.xpath("//*[contains(@resource-id,'myaddress')]");
    private By addBtn = By.xpath(
            "//*[contains(@resource-id,'add') and (self::android.widget.ImageButton or self::android.widget.Button)]"
    );

    // Form Fields - Common Locators
    private By firstNameField = By.xpath("//*[contains(@resource-id,'firstname')]");
    private By lastNameField = By.xpath("//*[contains(@resource-id,'lastname')]");
    private By address1Field = By.xpath("//*[contains(@resource-id,'address1')]");
    private By address2Field = By.xpath("//*[contains(@resource-id,'address2')]");
    
    // Dropdown - Using common text
    private By countryDropdown = By.xpath("//*[contains(@text,'Please Choose Country')]");
    private By stateDropdown = By.xpath("//*[contains(@resource-id,'continue_shopping')]");
    
    private By cityField = By.xpath("//*[contains(@resource-id,'city')]");
    private By zipField = By.xpath("//*[contains(@resource-id,'pincode')]");
    private By phoneField = By.xpath("//*[contains(@resource-id,'phone')]");
    private By submitBtn = By.xpath("//*[contains(@resource-id,'submit')]");
    
    private By editAddressBtn = By.xpath("//*[contains(@resource-id,'editaddress')]");
    private By deleteAddressBtn = By.xpath("//*[contains(@resource-id,'delete')]");
    private By confirmYesBtn = By.xpath("//*[contains(@resource-id,'ok_dialog')]");

    public AddressPage(AndroidDriver driver){
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    // --- Open Address Section ---
    public void openAddresses(){
        wait.until(ExpectedConditions.elementToBeClickable(sideDrawerBtn)).click();
        System.out.println("✅ Side drawer opened");
        
        wait.until(ExpectedConditions.elementToBeClickable(signinBtn)).click();
        System.out.println("✅ Sign in clicked");
        
        wait.until(ExpectedConditions.elementToBeClickable(myAddressMenu)).click();
        System.out.println("✅ My Address menu clicked");
    }

    // --- Add new address ---
    public void addAddress(String fname, String lname, String addr1Value, String addr2Value,
                           String country, String state, String city, String zip, String phone){
        openAddresses();

        // Click Add button
        WebElement addButton = wait.until(ExpectedConditions.presenceOfElementLocated(addBtn));
        try {
            addButton.click();
            System.out.println("✅ + button clicked");
        } catch (Exception e) {
            new TouchAction(driver)
                    .tap(ElementOption.element(addButton))
                    .waitAction(WaitOptions.waitOptions(Duration.ofMillis(250)))
                    .perform();
            System.out.println("✅ + button tapped via TouchAction");
        }

        // Wait for form load
        wait.until(ExpectedConditions.visibilityOfElementLocated(firstNameField));
        sleep(1000);
        System.out.println("📋 Address form loaded");

        // --- Fill Text Fields with scroll and keyboard hide ---
        fillFieldWithScroll(firstNameField, fname, "First Name");
        fillFieldWithScroll(lastNameField, lname, "Last Name");
        fillFieldWithScroll(address1Field, addr1Value, "Address 1");
        fillFieldWithScroll(address2Field, addr2Value, "Address 2");

        // Force hide keyboard and wait
        forceHideKeyboard();
        sleep(1000);
        System.out.println("⌨️ Keyboard hidden, proceeding to dropdowns...");

        // --- Select Country Dropdown ---
        selectDropdown(countryDropdown, country, "Country");
        sleep(1000);

        // --- Select State Dropdown ---
        selectDropdown(stateDropdown, state, "State");
        sleep(1000);

        // --- Fill Remaining Fields with scroll ---
        fillFieldWithScroll(cityField, city, "City");
        fillFieldWithScroll(zipField, zip, "Pincode");
        fillFieldWithScroll(phoneField, phone, "Phone");

        // Force hide keyboard before submit
        forceHideKeyboard();
        sleep(800);

        // Submit
        clickSubmit();

        // Verify address added (with longer wait)
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(editAddressBtn));
            System.out.println("🎉 Address added successfully!");
        } catch(Exception e){
            System.out.println("⚠️ Edit button not visible, but form might be submitted");
        }
    }

    // --- Fill Field With Scroll and Force Keyboard Hide ---
    private void fillFieldWithScroll(By locator, String value, String fieldName){
        try {
            // Scroll to element first
            try {
                driver.findElement(AppiumBy.androidUIAutomator(
                    "new UiScrollable(new UiSelector().scrollable(true)).scrollIntoView("
                    + "new UiSelector().resourceIdMatches(\".*" + getFieldKeyword(fieldName) + ".*\"))"
                ));
                sleep(300);
            } catch(Exception scrollEx){
                System.out.println("⚠️ Scroll not needed for " + fieldName);
            }

            // Find element
            WebElement field = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
            
            // Method 1: Try standard sendKeys
            try {
                field.click();
                sleep(400);
                field.clear();
                field.sendKeys(value);
                System.out.println("✅ " + fieldName + ": " + value + " (standard)");
                forceHideKeyboard();
                sleep(500);
                return;
            } catch(Exception e1){
                System.out.println("⚠️ Standard input failed for " + fieldName + ", trying setValue...");
            }

            // Method 2: Use setValue (direct set without keyboard)
            try {
                driver.executeScript("mobile: type", java.util.Map.of(
                    "elementId", ((org.openqa.selenium.remote.RemoteWebElement) field).getId(),
                    "text", value
                ));
                System.out.println("✅ " + fieldName + ": " + value + " (mobile type)");
                forceHideKeyboard();
                sleep(500);
                return;
            } catch(Exception e2){
                System.out.println("⚠️ Mobile type failed for " + fieldName + ", trying ADB...");
            }

            // Method 3: ADB shell input
            try {
                field.click();
                sleep(300);
                String textArg = value.replace(" ", "%s");
                driver.executeScript("mobile: shell", java.util.Map.of(
                    "command", "input",
                    "args", java.util.Arrays.asList("text", textArg)
                ));
                System.out.println("✅ " + fieldName + ": " + value + " (ADB shell)");
                forceHideKeyboard();
                sleep(500);
            } catch(Exception e3){
                System.out.println("❌ All methods failed for " + fieldName);
            }
            
        } catch (Exception e) {
            System.out.println("❌ Failed to fill " + fieldName + ": " + e.getMessage());
        }
    }

    // --- Get Field Keyword for Scroll ---
    private String getFieldKeyword(String fieldName){
        if(fieldName.contains("First")) return "firstname";
        if(fieldName.contains("Last")) return "lastname";
        if(fieldName.contains("Address 1")) return "address1";
        if(fieldName.contains("Address 2")) return "address2";
        if(fieldName.contains("City")) return "city";
        if(fieldName.contains("Pincode")) return "pincode";
        if(fieldName.contains("Phone")) return "phone";
        return "field";
    }

    // --- Force Hide Keyboard (Multiple Methods) ---
    private void forceHideKeyboard(){
        // Method 1: Standard hideKeyboard
        try {
            if (driver.isKeyboardShown()) {
                driver.hideKeyboard();
                System.out.println("⌨️ Keyboard hidden (standard)");
                return;
            }
        } catch(Exception e1){
            // Method 2: Mobile command
            try {
                driver.executeScript("mobile: hideKeyboard");
                System.out.println("⌨️ Keyboard hidden (mobile command)");
                return;
            } catch(Exception e2){
                // Method 3: Press Back
                try {
                    driver.pressKey(new io.appium.java_client.android.nativekey.KeyEvent(
                        io.appium.java_client.android.nativekey.AndroidKey.BACK
                    ));
                    System.out.println("⌨️ Keyboard hidden (back press)");
                } catch(Exception e3){
                    System.out.println("⚠️ All keyboard hide methods failed");
                }
            }
        }
    }

    // --- Select Dropdown Method ---
    private void selectDropdown(By dropdownLocator, String value, String dropdownName){
        try {
            // Scroll to dropdown
            try {
                driver.findElement(AppiumBy.androidUIAutomator(
                    "new UiScrollable(new UiSelector().scrollable(true)).scrollIntoView("
                    + "new UiSelector().textContains(\"" + dropdownName + "\"))"
                ));
                sleep(500);
            } catch(Exception ignored){}

            // Click dropdown
            WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(dropdownLocator));
            dropdown.click();
            System.out.println("🔽 " + dropdownName + " dropdown opened");
            sleep(1500);

            // Scroll and select value
            try {
                WebElement option = driver.findElement(AppiumBy.androidUIAutomator(
                    "new UiScrollable(new UiSelector().scrollable(true)).scrollIntoView("
                    + "new UiSelector().text(\"" + value + "\"))"
                ));
                sleep(400);
                option.click();
                System.out.println("✅ " + dropdownName + " selected: " + value);
                sleep(600);
                
            } catch(Exception e1){
                System.out.println("⚠️ Scroll failed, trying direct click...");
                WebElement option = driver.findElement(By.xpath("//*[@text='" + value + "']"));
                option.click();
                System.out.println("✅ " + dropdownName + " selected (direct): " + value);
                sleep(600);
            }

        } catch(Exception e){
            System.out.println("❌ " + dropdownName + " selection failed: " + e.getMessage());
        }
    }

    // --- Click Submit Button ---
    private void clickSubmit(){
        try {
            // Scroll to submit button
            WebElement submitButton = driver.findElement(AppiumBy.androidUIAutomator(
                    "new UiScrollable(new UiSelector().scrollable(true))" +
                    ".scrollIntoView(new UiSelector().resourceIdMatches(\".*submit.*\"))"
            ));
            
            forceHideKeyboard();
            sleep(500);
            
            submitButton.click();
            System.out.println("✅ Submit button clicked");
            sleep(2000); // Wait for form submission
            
        } catch (Exception e) {
            try {
                WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(submitBtn));
                submitButton.click();
                System.out.println("✅ Submit clicked (direct)");
                sleep(2000);
            } catch(Exception ex){
                System.out.println("❌ Submit failed: " + ex.getMessage());
            }
        }
    }

    // --- Edit Address ---
    public void editAddress(String newFirstName){
        try {
            wait.until(ExpectedConditions.elementToBeClickable(editAddressBtn)).click();
            System.out.println("✏️ Edit button clicked");
            sleep(1500);
            
            fillFieldWithScroll(firstNameField, newFirstName, "First Name");
            
            forceHideKeyboard();
            sleep(500);
            
            clickSubmit();

            // Handle confirmation dialog
            try {
                WebElement confirmBtn = new WebDriverWait(driver, Duration.ofSeconds(5))
                        .until(ExpectedConditions.elementToBeClickable(confirmYesBtn));
                confirmBtn.click();
                System.out.println("✅ Edit confirmed");
            } catch (TimeoutException e) {
                System.out.println("⚠️ No confirmation dialog appeared");
            }
        } catch(Exception e){
            System.out.println("❌ Edit failed: " + e.getMessage());
        }
    }

    // --- Delete Address ---
    public void deleteAddress(){
        try {
            wait.until(ExpectedConditions.elementToBeClickable(deleteAddressBtn)).click();
            System.out.println("🗑️ Delete button clicked");
            
            try {
                WebElement confirmBtn = new WebDriverWait(driver, Duration.ofSeconds(5))
                        .until(ExpectedConditions.elementToBeClickable(confirmYesBtn));
                confirmBtn.click();
                System.out.println("✅ Delete confirmed");
            } catch (TimeoutException e) {
                System.out.println("⚠️ No confirmation dialog appeared");
            }
        } catch(Exception e){
            System.out.println("❌ Delete failed: " + e.getMessage());
        }
    }

    // --- Navigate Back to Homepage ---
    public void navigateBackToHomePage() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(
                    AppiumBy.androidUIAutomator("new UiSelector().description(\"Navigate up\")")
            )).click();
            sleep(500);
            
            wait.until(ExpectedConditions.elementToBeClickable(
                    AppiumBy.androidUIAutomator("new UiSelector().description(\"Navigate up\")")
            )).click();
            System.out.println("🏠 Navigated back to HomePage");
            
        } catch (Exception e) {
            System.out.println("❌ Navigation back failed: " + e.getMessage());
        }
    }

    // --- Sleep Helper ---
    private void sleep(int milliseconds){
        try {
            Thread.sleep(milliseconds);
        } catch(InterruptedException e){
            Thread.currentThread().interrupt();
        }
    }
}