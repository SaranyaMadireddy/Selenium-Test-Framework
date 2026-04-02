package com.orangehrm.tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.orangehrm.base.BaseClass;
import com.orangehrm.utilities.ExtentManager;

public class DummyTest2 extends BaseClass {

	@Test
	public void DummyTestCase2() {
		ExtentManager.logStep("Verifying the title");
		Assert.assertEquals(getDriver().getTitle(), "OrangeHRM", "TestFailed -Title Mismatch");
		System.out.println("Test Passed -Title is matching");

	}

}
