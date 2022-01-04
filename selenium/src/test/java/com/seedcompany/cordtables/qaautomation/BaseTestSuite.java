package com.seedcompany.cordtables.qaautomation;

import java.io.IOException;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;

import com.seedcompany.cordtables.config.AutomationTestConfig;
import com.seedcompany.cordtables.utils.SeleniumUtils;

/**
 * Base class for TestNG-based test classes
 */
public class BaseTestSuite {

	protected static String baseUrl;
	protected WebDriver driver;
	public static final AutomationTestConfig testConfig = AutomationTestConfig.getAutomationContext();

	@BeforeSuite
	public void initTestSuite() throws IOException {

	}

	@BeforeTest
	public void initDriver() {
		driver = SeleniumUtils.getDriver(testConfig.getBrowserConfigs());
		SeleniumUtils.openSession(driver, testConfig.getAppConfigs().getUrl());
		SeleniumUtils.wait(5);
	}

	@BeforeMethod
	public void initWebDriver() {

	}

	@AfterTest
	public void cleanupTest() {
		SeleniumUtils.closeSession(driver);
	}

	@AfterSuite(alwaysRun = true)
	public void tearDown() {
		SeleniumUtils.closeSession(driver);
	}
}
