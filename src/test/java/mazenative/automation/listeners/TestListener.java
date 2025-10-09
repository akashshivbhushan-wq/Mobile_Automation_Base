package mazenative.automation.listeners;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class TestListener implements ITestListener {

    private static ExtentReports extent;
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();

    @Override
    public void onStart(ITestContext context) {
        try {
            // Workspace resolve (Jenkins/local)
            String workspace = System.getenv("WORKSPACE");
            if (workspace == null || workspace.isEmpty()) {
                workspace = System.getProperty("user.dir");
            }

            // Report folder
            String reportFolder = workspace + File.separator + "test-output" + File.separator + "ExtentReport";
            File reportDir = new File(reportFolder);
            if (!reportDir.exists()) reportDir.mkdirs();

            // Report file
            String reportPath = reportFolder + File.separator + "index.html";

            // Spark Reporter
            ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);
            spark.config().setDocumentTitle("Automation Test Report");
            spark.config().setReportName("Regression Suite Execution");
            spark.config().setTheme(Theme.DARK);
          //  spark.config().setTimelineEnabled(true); // flow/timeline chart

            extent = new ExtentReports();
            extent.attachReporter(spark);

            extent.setSystemInfo("OS", System.getProperty("os.name"));
            extent.setSystemInfo("User", System.getProperty("user.name"));
            extent.setSystemInfo("Java Version", System.getProperty("java.version"));

            System.out.println("✅ ExtentReport initialized at: " + reportPath);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onTestStart(ITestResult result) {
        ExtentTest extentTest = extent.createTest(result.getMethod().getMethodName())
                .assignCategory(result.getTestClass().getName());
        test.set(extentTest);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        test.get().pass("✅ Test Passed");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        test.get().fail(result.getThrowable());

        try {
            Object testClassInstance = result.getInstance();
            Field driverField = testClassInstance.getClass().getDeclaredField("driver");
            driverField.setAccessible(true);
            WebDriver driver = (WebDriver) driverField.get(testClassInstance);

            if (driver != null) {
                String screenshotPath = captureScreenshot(driver, result.getMethod().getMethodName());
                test.get().addScreenCaptureFromPath(screenshotPath, "Failure Screenshot");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        test.get().skip("⏭ Test Skipped: " + result.getThrowable());
    }

    @Override
    public void onFinish(ITestContext context) {
        if (extent != null) {
            extent.flush();
        }
    }

    // Screenshot method
    public String captureScreenshot(WebDriver driver, String methodName) throws IOException {
        String timestamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());

        String workspace = System.getenv("WORKSPACE");
        if (workspace == null || workspace.isEmpty()) {
            workspace = System.getProperty("user.dir");
        }

        String screenshotDir = workspace + File.separator + "test-output" + File.separator + "ExtentReport" + File.separator + "screenshots";
        File dir = new File(screenshotDir);
        if (!dir.exists()) dir.mkdirs();

        String screenshotPath = screenshotDir + File.separator + methodName + "_" + timestamp + ".png";
        File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(src, new File(screenshotPath));

        return screenshotPath;
    }
}
