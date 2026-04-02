package com.orangehrm.utilities;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ExtentManager {

	private static ExtentReports extent;
	private static ExtentSparkReporter spark;
	private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();
	private static Map<Long, WebDriver> driverMap = new HashMap<>();

	// Initialize Extent Report
	public synchronized static ExtentReports getReporter() {
		if (extent == null) {
			String reportPath = System.getProperty("user.dir") + "/src/test/resources/extentreport/ExtentReport.html";
			spark = new ExtentSparkReporter(reportPath);
			spark.config().setDocumentTitle("OrangeHRM Report");
			spark.config().setReportName("Automation Test Report");
			spark.config().setTheme(Theme.DARK);

			extent = new ExtentReports();
			// Adding System info
			extent.setSystemInfo("Operating System", System.getProperty("os.name"));
			extent.setSystemInfo("Java Version", System.getProperty("java.version"));
			extent.setSystemInfo("Username", System.getProperty("user.name"));
			extent.attachReporter(spark);
		}
		return extent;
	}

	// Start the test
	public synchronized static ExtentTest startTest(String testName) {
		ExtentTest extentTest = getReporter().createTest(testName);
		test.set(extentTest);
		return extentTest;
	}

	// End the test
	public synchronized static void endTest() {
		getReporter().flush();
	}

	// Get Current Thread Test
	public synchronized static ExtentTest getTest() {
		return test.get();
	}

	// Method to get name of current test
	public static String getTestName() {
		ExtentTest currentTest = getTest();
		if (currentTest != null) {
			return currentTest.getModel().getName();
		} else {
			return "No test is currently active for this thread";
		}
	}

	// Log a Step info
	public static void logStep(String logMessage) {
		getTest().info(logMessage);
	}

	// Log a Step validation with Screenshot
	public static void logStepWithScreenshot(WebDriver driver, String logMessage, String screenshotMessage) {
		getTest().pass(logMessage); // Test Level screenshot
		// screenshot method
		attachScreenshot(driver, screenshotMessage); // -Log level screenshot
	}

	// Log a Step Pass for API
	public static void logStepValidationForAPI(String logMessage) {
		getTest().pass(logMessage);
	}

	// Log a Failure
	public static void logFaliure(WebDriver driver, String logMessage, String screenshotMessage) {
		String colorMessage = String.format("<span style='color:red'>%s </span", logMessage);
		getTest().fail(colorMessage);
		// screenshot method
		attachScreenshot(driver, screenshotMessage);
	}

	// Log a Failure for API
	public static void logFaliureAPI(String logMessage) {
		String colorMessage = String.format("<span style='color:red'>%s </span", logMessage);
		getTest().fail(colorMessage);
	}

	// Log a skip
	public static void logSkip(String logMessage) {
		String colorMessage = String.format("<span style='color:orange'>%s </span", logMessage);
		getTest().skip(colorMessage);
	}

	// Take a screenshot with date and time in file
	public synchronized static String takeScreenshot(WebDriver driver, String ScreenshotName) {
		File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

		// Format date and time
		String timeStamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());

		// Saving screenshot to file
		String destpath = System.getProperty("user.dir") + "/src/test/resources/screenshots/" + ScreenshotName + "_"
				+ timeStamp + ".png";
		File finalPath = new File(destpath);
		try {
			FileUtils.copyFile(src, finalPath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Convert screenshot to base64 for embedding in report
		String base64Format = convertToBase64(src);

		/*
		 * Selenium asks the browser driver for a screenshot and immediately returns the
		 * image data as a base64 string. No file is created or read
		 */
//			String base64Format=((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64); 
		return base64Format;

	}

	// Convert screenshot to base64 -- You’re taking an already saved .png file
	public static String convertToBase64(File screenShotFile) {
		String base64Format = "";
		// Read the file content into a byte array
		byte[] fileContent;
		try {
			fileContent = FileUtils.readFileToByteArray(screenShotFile);
			base64Format = Base64.getEncoder().encodeToString(fileContent);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return base64Format;
	}

	// Attach screenshot to report using base64
	public synchronized static void attachScreenshot(WebDriver driver, String message) {
		try {
			String screenShoteBase64 = takeScreenshot(driver, getTestName());
			getTest().info(message, com.aventstack.extentreports.MediaEntityBuilder
					.createScreenCaptureFromBase64String(screenShoteBase64).build());
		} catch (Exception e) {
			getTest().fail("Fail to attach screenshot:" + message);
			e.printStackTrace();
		}
	}

	// Register WebDriver for current Thread
	public static void registerDriver(WebDriver driver) {
		driverMap.put(Thread.currentThread().getId(), driver);
	}

}
