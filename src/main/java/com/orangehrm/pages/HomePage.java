package com.orangehrm.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.orangehrm.actiondriver.ActionDriver;
import com.orangehrm.base.BaseClass;

public class HomePage {

	private ActionDriver actionDriver;

	// Define Locators using By class
	private By adminTab = By.xpath("//span[text()='Admin']");
	private By userIDbutton = By.className("oxd-userdropdown-name");
	private By orangeHRMLogo = By.xpath("//div[@class='oxd-brand-banner']/img");
	private By logoutButton = By.xpath("//a[text()='Logout']");
	private By pimTab = By.xpath("//span[text()='PIM']");
	private By employeeSearch = By.xpath("//label[text()='Employee Name']/parent::div/following::div/div/div/input");
	private By searchButton = By.xpath("//button[@type='submit']");
	private By emplFirstAndMiddleName = By.xpath("//div[@class='oxd-table-card']/div/div[3]");
	private By emplLastName = By.xpath("//div[@class='oxd-table-card']/div/div[4]");

	// Constructor -Initialize the ActionDriver object by passing WebDriver Instance
	/*
	 * public HomePage(WebDriver driver) { this.actionDriver=new
	 * ActionDriver(driver); }
	 */
	public HomePage(WebDriver driver) {
		this.actionDriver = BaseClass.getActionDriver();
	}

	// Method to verify admin tab is visible
	public boolean isAdminTabVisible() {
		return actionDriver.isDisplayed(adminTab);
	}

	// Method to verify orangeHrm logo
	public boolean verifyOrangeHRMLogo() {
		return actionDriver.isDisplayed(orangeHRMLogo);
	}

	// Method to perform logout operation
	public void logout() {
		actionDriver.click(userIDbutton);
		actionDriver.click(logoutButton);
	}

	// Method to Navigate to pim tab
	public void clickOnPimTab() {
		actionDriver.click(pimTab);
	}

	// Method to search employee
	public void employeeSearch(String value) {
		actionDriver.enterText(employeeSearch, value);
		actionDriver.click(searchButton);
		actionDriver.scrollToElement(emplFirstAndMiddleName);
	}

	// Verify employee first and middle name
	public boolean verifyEmployeeFirstAndMiddleName(String emplFirstAndMiddleNameFromDB) {
		return actionDriver.compareText(emplFirstAndMiddleName, emplFirstAndMiddleNameFromDB);
	}

	// Verify employee last name
	public boolean verifyEmployeeLastName(String emplLastNameFromDB) {
		return actionDriver.compareText(emplLastName, emplLastNameFromDB);
	}

}
