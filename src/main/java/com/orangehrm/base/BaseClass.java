package com.orangehrm.base;

import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v144.emulation.Emulation;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.asserts.SoftAssert;

import com.orangehrm.actiondriver.ActionDriver;
import com.orangehrm.utilities.ExtentManager;
import com.orangehrm.utilities.LoggerManager;

public class BaseClass {

	protected static Properties prop;
//	protected static WebDriver driver;
//	private static ActionDriver actionDriver;

	private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
	private static ThreadLocal<ActionDriver> actionDriver = new ThreadLocal<>();
	public static final Logger logger = LoggerManager.getLogger(BaseClass.class);
	protected ThreadLocal<SoftAssert> softAssert = ThreadLocal.withInitial(SoftAssert::new); // 2nd way of defining
																								// thread local

	@BeforeSuite
//Load Configuration file
	public void loadConfig() throws IOException {
//		File file = new File("./src/main/resources/config.properties");
		prop = new Properties();
		FileInputStream fis = new FileInputStream(
				System.getProperty("user.dir") + "./src/main/resources/config.properties");
		prop.load(fis);
		logger.info("config.properties file loaded");

	}

	@BeforeMethod
	public synchronized void setup() throws Exception {
		System.out.println("Setting up WebDriver for:" + this.getClass().getSimpleName());
		launchBrowser();
		configureBrowser();
		staticWait(4);
		logger.info("WebDriver initialized and Browser Maximized");

		// Initialize actionDriver only once
		/*
		 * if (actionDriver == null) { actionDriver = new ActionDriver(driver);
		 * logger.info("ActionDriver instance is created"+Thread.currentThread().getId()
		 * ); }
		 */

		// Initialization of actionDriver for current Thread
		actionDriver.set(new ActionDriver(getDriver()));
		logger.info("ActionDriver initlialized for thread :" + Thread.currentThread().getId());
	}

//Initialize the web driver based on browser defined in Config.properties
	private synchronized void launchBrowser() {
		String browserName = prop.getProperty("browser").trim().toLowerCase();

		// Browser
		if (browserName.equalsIgnoreCase("chrome")) {
			ChromeOptions options = new ChromeOptions();
			options.addArguments("--headless=new"); // Run chrome in headless mode
			options.addArguments("--disable-gpu"); // Disable Gpu for headless mode
//			options.addArguments("--window-size=3840,2160"); //set window size
			options.addArguments("--disable-notifications"); // Disable browser notifications
			options.addArguments("--no-sandbox");// Required for some CI/CD environments
			options.addArguments("--disable-dev-shm-usage"); // Resolve issues in resources
//			driver = new ChromeDriver();
			driver.set(new ChromeDriver(options)); // New changes as per Thread
			DevTools devTools = ((ChromeDriver) getDriver()).getDevTools();
			devTools.createSession();

			devTools.send(Emulation.setDeviceMetricsOverride(1920, 1080, 1.0, false, java.util.Optional.empty(),
					java.util.Optional.empty(), java.util.Optional.empty(), java.util.Optional.empty(),
					java.util.Optional.empty(), java.util.Optional.empty(), java.util.Optional.empty(),
					java.util.Optional.empty(), java.util.Optional.empty(), java.util.Optional.empty()));
			ExtentManager.registerDriver(getDriver());
			logger.info("ChromeDriver Instance is created");
		} else if (browserName.equalsIgnoreCase("firefox")) {
			FirefoxOptions options = new FirefoxOptions();
			options.addArguments("--headless"); // Run chrome in headless mode
			options.addArguments("--disable-gpu"); // Disable Gpu for headless mode
			options.addArguments("--window-size=2560,1440"); // set window size
			options.addArguments("--disable-notifications"); // Disable browser notifications
			options.addArguments("--no-sandbox");// Required for some CI/CD environments
			options.addArguments("--disable-dev-shm-usage"); // Resolve issues in resources

//			driver = new FirefoxDriver();
			driver.set(new FirefoxDriver(options)); // New changes as per Thread
			ExtentManager.registerDriver(getDriver());
			logger.info("FirefoxDriver Instance is created");
		} else if (browserName.equalsIgnoreCase("edge")) {
			EdgeOptions options = new EdgeOptions();
			options.addArguments("--headless"); // Run chrome in headless mode
			options.addArguments("--disable-gpu"); // Disable Gpu for headless mode
			options.addArguments("--window-size=2560,1440"); // set window size
			options.addArguments("--disable-notifications"); // Disable browser notifications
			options.addArguments("--no-sandbox");// Required for some CI/CD environments
			options.addArguments("--disable-dev-shm-usage"); // Resolve issues in resources
//			driver = new EdgeDriver();
			driver.set(new EdgeDriver(options)); // New changes as per Thread
			DevTools devTools = ((EdgeDriver) getDriver()).getDevTools();
			devTools.createSession();

			devTools.send(Emulation.setDeviceMetricsOverride(1920, 1080, 1.0, false, java.util.Optional.empty(),
					java.util.Optional.empty(), java.util.Optional.empty(), java.util.Optional.empty(),
					java.util.Optional.empty(), java.util.Optional.empty(), java.util.Optional.empty(),
					java.util.Optional.empty(), java.util.Optional.empty(), java.util.Optional.empty()));
			ExtentManager.registerDriver(getDriver());
			logger.info("EdgeDriver Instance is created");
		} else {
			throw new IllegalArgumentException("Browser not supported:" + browserName);

		}
	}

// Configure browser settings implicit wait,maximize browser and Navigate to Url
	private void configureBrowser() {
		// wait time
		int wait = Integer.parseInt(prop.getProperty("implicitWait"));
		getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(wait));

		// maximize browser
		getDriver().manage().window().maximize();

		// Navigate to URL
		try {
			getDriver().get(prop.getProperty("url"));
		} catch (Exception e) {
			System.out.println("Failed to navigate to URL:" + e.getMessage());
		}
	}

//Properties getter Method
	public static Properties getProp() {
		return prop;
	}

//Driver getter Method --updated the getter method to static and added condition
	public static WebDriver getDriver() {
		if (driver.get() == null) {
			System.out.println("WebDriver is not initialized");
			throw new IllegalStateException("WebDriver is not initialized");
		}
		return driver.get();
	}

//ActionDriver getter Method
	public static ActionDriver getActionDriver() {
		if (actionDriver.get() == null) {
			System.out.println("ActionDriver is not initialized");
			throw new IllegalStateException("ActionDriver is not initialized");
		}
		return actionDriver.get();
	}

//Driver setter Method
	public void setDriver(ThreadLocal<WebDriver> driver) {
		this.driver = driver;
	}

//Getter for Soft Assert
	public SoftAssert getSoftAssert() {
		return softAssert.get();
	}

// static wait for pause
	public static void staticWait(int seconds) {
		LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(seconds));
	}

	@AfterMethod
	public synchronized void teardown() throws IOException {
		if (getDriver() != null) {
			try {
				getDriver().quit();
			} catch (Exception e) {
				logger.info("Unable to quit the driver:" + e.getMessage());
			}
		}
		logger.info("WebDriver instance is closed");
		driver.remove();
		actionDriver.remove();
//		driver = null;
//		actionDriver = null;	
	}

}
