package com.seedcompany.cordtables.qaautomation;

import java.io.IOException;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import com.seedcompany.cordtables.config.AutomationTestConfig;
import com.seedcompany.cordtables.utils.SeleniumUtils;

/**
 * Base class for TestNG-based test classes
 */
public class BaseTestSuite {

	protected static String baseUrl;
	protected WebDriver driver;
	protected AutomationTestConfig testConfig;

	@BeforeSuite
	public void initTestSuite() throws IOException {
		testConfig = AutomationTestConfig.getAutomationContext();
	}

	@BeforeMethod
	public void initWebDriver() {
		driver = SeleniumUtils.getDriver(testConfig.getBrowserConfigs());
	}

	@AfterSuite(alwaysRun = true)
	public void tearDown() {
		SeleniumUtils.closeSession(driver);
	}
}
