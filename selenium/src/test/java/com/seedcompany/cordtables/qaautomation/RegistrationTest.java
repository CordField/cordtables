package com.seedcompany.cordtables.qaautomation;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

import java.util.Random;
import java.util.logging.Logger;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.seedcompany.cordtables.pages.HomePage;
import com.seedcompany.cordtables.pages.MainPage;
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

	@AfterMethod
	public void cleanupTest() {
		HomePage homePage = PageFactory.initElements(driver, HomePage.class);
		homePage.logout();
	}

	private void lauchRegistration(String email) {
		HomePage homePage = PageFactory.initElements(driver, HomePage.class);
		WebElement menu = homePage.openMenu();
		homePage.register(menu);
		registrationPage = PageFactory.initElements(driver, RegistrationPage.class);
		registrationPage.loadApp();
		this.registrationPage.fillRegistrationForm(email, this.testConfig.getAppConfigs().getNewUserPassword());
	}

	/**
	 * Test case to verify if user comes to home page and selects the registration
	 * option from the home page menu.
	 */
	@Test
	public void registration_successful() {

		Random randomGenerator = new Random();
		this.lauchRegistration(this.testConfig.getAppConfigs().getNewUser() + randomGenerator.nextInt() + "@test.com");
		SeleniumUtils.wait(10);
		MainPage mainPage = PageFactory.initElements(driver, MainPage.class);
		mainPage.loadApp();
		WebElement mainMenu = SeleniumUtils.expand_shadow_element(driver,
				mainPage.getRootApp().findElement(By.tagName("custom-accordion")));
		assertNotNull(mainMenu);
		assertNotNull(mainMenu.findElement(By.cssSelector(".accordion")));

	}

	/**
	 * This test case is used to verify if the already existing email is used again
	 * to register, then user should not allow to re-register existing email.
	 */
	@Test
	public void registration_whenDuplicateEmail_Thenfailure() {
		SeleniumUtils.openSession(driver, testConfig.getAppConfigs().getUrl());
		SeleniumUtils.wait(10);
		this.lauchRegistration(this.testConfig.getAppConfigs().getNewUser() + "@test.com");
		SeleniumUtils.wait(10);
		MainPage mainPage = PageFactory.initElements(driver, MainPage.class);
		mainPage.loadApp();

		assertNull(SeleniumUtils.IsElementFound(driver, By.tagName("custom-accordion"), mainPage.getRootApp()));
	}

}
