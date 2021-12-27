package com.seedcompany.cordtables.qaautomation;

import java.util.logging.Logger;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.seedcompany.cordtables.pages.HomePage;
import com.seedcompany.cordtables.pages.RegistrationPage;
import com.seedcompany.cordtables.utils.SeleniumUtils;

/**
 * This Test Suite contains the test scenarios and cases associated with the
 * registration functionality/flow of the cordtables application.
 * 
 * @author swati
 *
 */
public class RegistrationTest extends BaseTestSuite {

	private RegistrationPage registrationPage;
	Logger logger = Logger.getLogger(RegistrationTest.class.getName());

	@BeforeMethod
	public void initPageObjects() {

	}

	/**
	 * Test case to verify if user comes to home page and selects the registration
	 * option from the home page menu.
	 */
	@Test
	public void registration_successful() {

		SeleniumUtils.openSession(driver, testConfig.getAppConfigs().getUrl());
		SeleniumUtils.wait(10);
		HomePage homePage = PageFactory.initElements(driver, HomePage.class);
		WebElement menu = homePage.open_menu();
		homePage.register(menu);
		registrationPage = PageFactory.initElements(driver, RegistrationPage.class);
		registrationPage.rootApp = homePage.loadApp();
		this.registrationPage.fillRegistrationForm("test@test1.com", "test123!");

	}
}
