package com.orangehrm.utilities;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyser implements IRetryAnalyzer {

	private int retryCount = 0; // Number of retries
	private static final int maxRetryCount = 2; // Max no of retries

	@Override
	public boolean retry(ITestResult arg0) {
		if (retryCount < maxRetryCount) {
			retryCount++;
			return true; // Retry the test
		}
		return false;
	}

}
