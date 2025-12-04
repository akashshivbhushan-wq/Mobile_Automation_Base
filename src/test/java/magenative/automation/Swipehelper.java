package magenative.automation;



import java.util.HashMap;
import java.util.Map;

import io.appium.java_client.android.AndroidDriver;

public class Swipehelper {
    AndroidDriver driver;

    public Swipehelper(AndroidDriver driver) {
        this.driver = driver;
    }

    // Generic swipe function
    public void swipe(String direction) {
        Map<String, Object> args = new HashMap<>();
        args.put("left", 0);
        args.put("top", 0);
        args.put("width", driver.manage().window().getSize().getWidth());
        args.put("height", driver.manage().window().getSize().getHeight());
        args.put("direction", direction);
        args.put("percent", 0.8); // kitna scroll karna hai (80%)

        driver.executeScript("mobile: swipeGesture", args);
    }

    public void swipeDown() {
        swipe("down");
    }

    public void swipeUp() {
        swipe("up");
    }
}



