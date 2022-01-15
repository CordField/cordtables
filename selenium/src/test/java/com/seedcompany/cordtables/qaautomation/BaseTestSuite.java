package com.seedcompany.cordtables.qaautomation;

import java.io.IOException;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import com.seedcompany.cordtables.config.AutomationTestConfig;
import com.seedcompany.cordtables.utils.SeleniumUtils;

/**
 * Base class for TestNG-based test classes
 */
public class BaseTestSuite {

	protected static String baseUrl;
	public WebDriver driver;
	public static final AutomationTestConfig testConfig = AutomationTestConfig.getAutomationContext();

	@BeforeSuite
	public void initTestSuite() throws IOException {

	}

	@BeforeClass
	public void initDriver() {
		driver = SeleniumUtils.getDriver(testConfig.getBrowserConfigs());
		SeleniumUtils.openSession(driver, testConfig.getAppConfigs().getUrl());
		SeleniumUtils.wait(5);
	}

	@BeforeMethod
	public void initWebDriver() {

	}

	@AfterClass
	public void tearDown() {
		SeleniumUtils.closeSession(driver);
	}
}
