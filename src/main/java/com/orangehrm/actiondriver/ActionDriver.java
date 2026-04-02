package com.orangehrm.actiondriver;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.orangehrm.base.BaseClass;
import com.orangehrm.utilities.ExtentManager;
import com.orangehrm.utilities.LoggerManager;

public class ActionDriver {

	private WebDriver driver;
	private WebDriverWait wait;
	public static final Logger logger = BaseClass.logger;

	// Constructor
	public ActionDriver(WebDriver driver) {
		this.driver = driver;
		int expwait = Integer.parseInt(BaseClass.getProp().getProperty("explicitWait"));
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(expwait));
		logger.info("WebDriver instance is created");
	}

	// Method to Click on the Element
	public void click(By by) {
		String elementDescription = getElementDescription(by);
		try {
			waitForElementToBeClickable(by);
			applyBorder(by, "green");
			driver.findElement(by).click();
			ExtentManager.logStep("Clicked an element" + elementDescription);
			logger.info("Clicked an element " + elementDescription);
		} catch (Exception e) {
			applyBorder(by, "red");
			logger.error("Unable to click element:" + e.getMessage());
			ExtentManager.logFaliure(BaseClass.getDriver(), "Unable to click element",
					"Unable to click element:" + elementDescription);

		}
	}

	// Method to enter text into input field --Avoid code duplication
	public void enterText(By by, String value) {
		try {
			waitForElementToBeVisible(by);
			applyBorder(by, "green");
			WebElement element = driver.findElement(by);
			element.clear();
			element.sendKeys(value);
			logger.info("Entered text on " + getElementDescription(by) + "-->" + value);
		} catch (Exception e) {
			applyBorder(by, "red");
			logger.error("Unable to enter text:" + e.getMessage());

		}
	}

	// Method to get text from input field
	public String getText(By by) {
		try {
			waitForElementToBeVisible(by);
			applyBorder(by, "green");
			logger.info("Text retrieved" + getElementDescription(by));
			return driver.findElement(by).getText();
		} catch (Exception e) {
			applyBorder(by, "red");
			logger.error("Unable to get text:" + e.getMessage());
			return " ";
		}
	}

	// Method to compare two texts
	public boolean compareText(By by, String expectedText) {
		try {
			waitForElementToBeVisible(by);
			String actualText = driver.findElement(by).getText();
			if (expectedText.equals(actualText)) {
				applyBorder(by, "green");
				ExtentManager.logStepWithScreenshot(BaseClass.getDriver(), "Compare Text",
						"Text Verified successfully!" + actualText + " equals " + expectedText);
				logger.info("Text are matching:" + actualText + " equals " + expectedText);
				return true;
			} else {
				applyBorder(by, "red");
				logger.error("Text are not matching:" + actualText + " not equals " + expectedText);
				ExtentManager.logFaliure(BaseClass.getDriver(), "Text Comparision Failed!",
						"Text Comparision failed " + actualText + " not equals " + expectedText);
				return false;
			}
		} catch (Exception e) {
			applyBorder(by, "red");
			logger.error("Unable to comapre Texts:" + e.getMessage());
		}
		return false;
	}

	// Method to check if element is displayed
	public boolean isDisplayed(By by) {
		try {
			waitForElementToBeVisible(by);
			applyBorder(by, "green");
			logger.info("Element is displayed " + getElementDescription(by));
			ExtentManager.logStepWithScreenshot(BaseClass.getDriver(), "Element is displayed ",
					"Element is displayed " + getElementDescription(by));
			return driver.findElement(by).isDisplayed();
		} catch (Exception e) {
			applyBorder(by, "red");
			logger.error("Element is not displayed:" + e.getMessage());
			ExtentManager.logFaliure(BaseClass.getDriver(), "Element is not displayed",
					"Element is not displayed:" + getElementDescription(by));
			return false;
		}
	}

	// Wait for page to load
	public void waitForPageLoad(int timeOutInSec) {
		try {
			wait.withTimeout(Duration.ofSeconds(timeOutInSec)).until(WebDriver -> ((JavascriptExecutor) WebDriver)
					.executeScript("return document.readyState").equals("complete"));
			logger.info("Page loaded Successfully");
		} catch (Exception e) {
			logger.error("Page not loaded within" + timeOutInSec + "seconds.Exception:" + e.getMessage());
		}
	}

	// Scroll to element
	public void scrollToElement(By by) {
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			WebElement element = driver.findElement(by);
			js.executeScript("arguments[0].scrollIntoView(true);", element);
		} catch (Exception e) {
			logger.error("Unable to scroll to element" + e.getMessage());
		}
	}

	// Wait for Element to be clickable
	public void waitForElementToBeClickable(By by) {
		try {
			wait.until(ExpectedConditions.elementToBeClickable(by));
		} catch (Exception e) {
			logger.error("Element is not Clickable:" + e.getMessage());
		}
	}

	// Wait for Element to be visible
	public void waitForElementToBeVisible(By by) {
		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(by));
		} catch (Exception e) {
			logger.error("Element is not visible:" + e.getMessage());
		}
	}

	// Method to get Description of an element
	public String getElementDescription(By locator) {
		// Check for null driver and locator to avoid null pointer exception
		if (driver == null)
			return "driver is null";
		if (locator == null)
			return "locator is null";

		try {
			// find element using locator
			WebElement element = driver.findElement(locator);

			// Get element attributes
			String name = element.getDomAttribute("name");
			String id = element.getDomAttribute("id");
			String text = element.getText();
			String className = element.getDomAttribute("class");
			String placeholder = element.getDomAttribute("placeholder");

			// Return the description based on element attributes
			if (isNotEmpty(name)) {
				return "Element with name:" + name;
			} else if (isNotEmpty(id)) {
				return "Element with id:" + id;
			} else if (isNotEmpty(text)) {
				return "Element with text:" + truncate(text, 50);
			} else if (isNotEmpty(className)) {
				return "Element with Class:" + className;
			} else if (isNotEmpty(placeholder)) {
				return "Element with placeholder:" + placeholder;
			}
		} catch (Exception e) {
			logger.error("Unable to describe the element" + e.getMessage());
		}
		return "Unable to describe the element";
	}

	// Utility method to check a String is not NUll or empty
	private boolean isNotEmpty(String value) {
		return value != null && !value.isEmpty();
	}

	// Utility method to truncate long string
	private String truncate(String value, int maxLength) {
		if (value == null || value.length() <= maxLength) {
			return value;
		}
		return value.substring(0, maxLength) + "...";
	}

	// Utility method to apply border
	public void applyBorder(By by, String color) {
		try {
			// Locate the element
			WebElement element = driver.findElement(by);
			// Apply border
			String script = "arguments[0].style.border ='3px solid " + color + "'";
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript(script, element);
			logger.info("Applied the border with color " + color + " to element: " + getElementDescription(by));
		} catch (Exception e) {
			logger.warn("Failed to apply the border to element: " + getElementDescription(by));
		}

	}

	// ----------------------*Dropdowns*---------------------------
	// Method to select a dropdown by visible Text
	public void selectByVisibleText(By by, String value) {
		try {
			WebElement element = driver.findElement(by);
			new Select(element).selectByVisibleText(value);
			applyBorder(by, "green");
			logger.info("Selected dropdown value: " + value);
		} catch (Exception e) {
			applyBorder(by, "red");
			logger.error("Unable to Select dropdown value: " + value, e);
		}
	}

	// Method to select a dropdown by visible Text
	public void selectByValue(By by, String value) {
		try {
			WebElement element = driver.findElement(by);
			new Select(element).selectByValue(value);
			applyBorder(by, "green");
			logger.info("Selected dropdown by value: " + value);
		} catch (Exception e) {
			applyBorder(by, "red");
			logger.error("Unable to Select dropdown by value: " + value, e);
		}
	}

	// Method to select a dropdown by visible Text
	public void selectByIndex(By by, int index) {
		try {
			WebElement element = driver.findElement(by);
			new Select(element).selectByIndex(index);
			applyBorder(by, "green");
			logger.info("Selected dropdown by Index: " + index);
		} catch (Exception e) {
			applyBorder(by, "red");
			logger.error("Unable to Select dropdown by index: " + index, e);
		}
	}

	// Method to get all options from dropdown
	public List<String> getDropdownOptions(By by) {
		List<String> optionList = new ArrayList<>();
		try {
			WebElement dropdownElements = driver.findElement(by);
			Select select = new Select(dropdownElements);
			for (WebElement option : select.getOptions()) {
				optionList.add(option.getText());
			}
			applyBorder(by, "green");
			logger.info("Retrieved dropdown options for " + getElementDescription(by));
		} catch (Exception e) {
			applyBorder(by, "red");
			logger.error("Unable to get dropdown options for: " + getElementDescription(by));
		}
		return optionList;
	}

	// ----------------------*JavaScript*---------------------------

	// Method to click using Java Script
	public void clickUsingJS(By by) {
		try {
			WebElement element = driver.findElement(by);
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].click()", element);
			applyBorder(by, "green");
			logger.info("Clicked element using JavaScript: " + getElementDescription(by));
		} catch (Exception e) {
			applyBorder(by, "red");
			logger.error("Unable to Click on element using JavaScript: ", e);
		}
	}

	// Scroll to bottom
	public void scrollToBotton() {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("window.scrollTo(0,document.body.scrollHeight)");
		logger.info("Scrolled to bottom of the Page");
	}

	// Method to highlight an element using Java Script
	public void highlightElementJs(By by) {
		try {
			WebElement element = driver.findElement(by);
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].style.border='3px solid green'");
			logger.info("Highlighted element using JavaScript:" + getElementDescription(by));
		} catch (Exception e) {
			logger.error("Unable to Highlighted element using JavaScript: ", e);
		}
	}

	// -----------------Window and Frame handling --------------------------------

	// Method to switch between windows
	public void switchToWindow(String windowTitle) {
		try {
			Set<String> windows = driver.getWindowHandles();
			for (String window : windows) {
				driver.switchTo().window(window);
				if (driver.getTitle().contains(windowTitle)) {
					logger.info("Switched to window:" + windowTitle);
					return;
				}
			}
			logger.info("Window with title" + windowTitle + "not found");
		} catch (Exception e) {
			logger.error("Unable to switch window", e);
		}
	}

	// Method to switch to iframe
	public void switchToFrame(By by) {
		try {
			driver.switchTo().frame(driver.findElement(by));
			logger.info("Switched to iframe:" + getElementDescription(by));
		} catch (Exception e) {
			logger.error("Unable to switch iframe", e);
		}
	}

	// Method to switch back to default content
	public void switchToDefaultContent(By by) {
		try {
			driver.switchTo().defaultContent();
			logger.info("Switched back to default content");
		} catch (Exception e) {
			logger.error("Unable to switch back to default content", e);
		}
	}

	// ------------------------*Alert Handling*-------------------
	// Method to accept an alert pop up
	public void acceptAlert() {
		try {
			driver.switchTo().alert().accept();
			logger.info("Alert Accepted");
		} catch (Exception e) {
			logger.error("No alert found to accept alert", e);
		}
	}

	// Method to dismiss alert
	public void dismissAlert() {
		try {
			driver.switchTo().alert().dismiss();
			logger.info("Alert Dismissed");
		} catch (Exception e) {
			logger.error("No alert found to dismiss", e);
		}
	}

	// Method to get alert text
	public String getAlertText() {
		try {
			return driver.switchTo().alert().getText();
		} catch (Exception e) {
			logger.error("No alert text found", e);
			return "";
		}
	}

	// -----------------------Browser Actions------------------------

	public void refreshPage() {
		try {
			driver.navigate().refresh();
			ExtentManager.logStep("Page refreshed successfully");
			logger.info("Page refreshed successfully");
		} catch (Exception e) {
			ExtentManager.logFaliure(BaseClass.getDriver(), "Unable to refresh page", "Unable to refresh page");
			logger.error("Unable to refresh Page ", e.getMessage());
		}
	}

	public String getCurrentURL() {
		try {
			String url = driver.getCurrentUrl();
			ExtentManager.logStep("Current URL fetched " + url);
			logger.info("Current URL fetched" + url);
			return url;
		} catch (Exception e) {
			ExtentManager.logFaliure(BaseClass.getDriver(), "Unable to fetch current url",
					"Unable to fetch current url");
			logger.error("Unable to fetch current url ", e.getMessage());
			return null;
		}
	}

	public void maximizeWindow() {
		try {
			driver.manage().window().maximize();
			ExtentManager.logStep("Browser window maximized");
			logger.info("Browser window maximized");
		} catch (Exception e) {
			ExtentManager.logFaliure(BaseClass.getDriver(), "Unable to maximize window", "Unable to maximize window");
			logger.error("Unable to maximize window: ", e.getMessage());
		}
	}

	// ----------------------Advanced WebElement
	// Actions-----------------------------------
	public void moveToElement(By by) {
		try {
			Actions actions = new Actions(driver);
			actions.moveToElement(driver.findElement(by)).perform();
			ExtentManager.logStep("Move to Element" + getElementDescription(by));
			logger.info("Moved to element-->" + getElementDescription(by));
		} catch (Exception e) {
			ExtentManager.logFaliure(BaseClass.getDriver(), "Unable to move to element", "Unable to move to element");
			logger.error("Unable to move to element: ", e.getMessage());
		}

	}

	public void dragAndDrag(By source, By target) {
		String sourceDescription = getElementDescription(source);
		String targetDescription = getElementDescription(target);
		try {
			Actions actions = new Actions(driver);
			actions.dragAndDrop(driver.findElement(source), driver.findElement(target)).perform();
			ExtentManager.logStep("Dragged Element " + sourceDescription + "dropped to target" + targetDescription);
			logger.info("Dragged Element " + sourceDescription + "dropped to target" + targetDescription);
		} catch (Exception e) {
			ExtentManager.logFaliure(BaseClass.getDriver(), "Unable to drag and drop", "Unable to drag and drop");
			logger.error("Unable to drag and drop ", e.getMessage());
		}

	}

	public void doubleClick(By by) {
		try {
			Actions actions = new Actions(driver);
			actions.doubleClick(driver.findElement(by)).perform();
			;
			ExtentManager.logStep("Doubleclick on element" + getElementDescription(by));
			logger.info("Doubleclick on element" + getElementDescription(by));
		} catch (Exception e) {
			ExtentManager.logFaliure(BaseClass.getDriver(), "Unable to double click on element",
					"Unable to double click on element");
			logger.error("Unable to double click on element ", e.getMessage());
		}
	}

	public void rightClick(By by) {
		try {
			Actions actions = new Actions(driver);
			actions.contextClick(driver.findElement(by)).perform();
			ExtentManager.logStep("Right click on element" + getElementDescription(by));
			logger.info("Right click on element" + getElementDescription(by));
		} catch (Exception e) {
			ExtentManager.logFaliure(BaseClass.getDriver(), "Unable to right click on element",
					"Unable to right click on element");
			logger.error("Unable to right click on element ", e.getMessage());
		}
	}

	public void sendKeysWithActions(By by, String value) {
		try {
			Actions actions = new Actions(driver);
			actions.sendKeys(driver.findElement(by), value).perform();
			;
			ExtentManager.logStep("Sent keys to element " + getElementDescription(by) + "value" + value);
			logger.info("Sent keys to element " + getElementDescription(by) + "value" + value);
		} catch (Exception e) {
			ExtentManager.logFaliure(BaseClass.getDriver(), "Unable to send keys", "Unable to send keys");
			logger.error("Unable to send keys to element ", e.getMessage());
		}
	}

	public void clearText(By by) {
		try {
			driver.findElement(by).clear();
			ExtentManager.logStep("Cleared text in element " + getElementDescription(by));
			logger.info("Cleared text in element " + getElementDescription(by));
		} catch (Exception e) {
			ExtentManager.logFaliure(BaseClass.getDriver(), "Unable to clear text", "Unable to clear text");
			logger.error("Unable to clear text in element ", e.getMessage());
		}
	}

	// Method to upload a file
	public void uploadFile(By by, String filePath) {
		try {
			driver.findElement(by).sendKeys(filePath);
			applyBorder(by, "green");
			logger.info("Uploaded file: " + filePath);
		} catch (Exception e) {
			applyBorder(by, "red");
			logger.error("Unable to upload file:", e.getMessage());
		}
	}

}
