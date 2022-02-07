package com.seedcompany.cordtables.qaautomation;

import static org.testng.Assert.assertNotNull;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.seedcompany.cordtables.pages.HomePage;
import com.seedcompany.cordtables.pages.LoginPage;
import com.seedcompany.cordtables.pages.MainPage;
import com.seedcompany.cordtables.utils.SeleniumUtils;

/**
 * This Test Suite contains the test scenarios and cases associated with the
 * Login functionality/flow of the cordtables application.
 * 
 * @author swati
 *
 */

public class LoginPageTest extends BaseTestSuite {

	private static Logger logger = LoggerFactory.getLogger(LoginPageTest.class);

	@BeforeMethod
	public void initPageObject() {

	}

	@AfterMethod
	public void cleanupTest() {
		HomePage homePage = PageFactory.initElements(driver, HomePage.class);
		homePage.logout();
	}

	/**
	 * Test case to verify if user come to home page and click on login page to
	 * enter login details and successfully logged in.
	 * 
	 */
	@Test

	public void login_whenValidCredentials_thenSuccess() {
		HomePage homepage = PageFactory.initElements(driver, HomePage.class);
		homepage.login(homepage.openMenu());
		LoginPage loginPage = PageFactory.initElements(driver, LoginPage.class);
		loginPage.loadApp();
		loginPage.submit(loginPage.fillLoginDetails(testConfig.getAppConfigs().getUsername(),
				testConfig.getAppConfigs().getPassword()));
		SeleniumUtils.wait(2);
		MainPage mainPage = PageFactory.initElements(driver, MainPage.class);
		mainPage.loadApp();
		SearchContext mainMenu = SeleniumUtils
				.expand_shadow_element(mainPage.getRootApp().findElement(By.cssSelector("custom-accordion.hydrated")));
		assertNotNull(mainMenu);
		assertNotNull(mainMenu.findElement(By.cssSelector(".accordion")));

	}

}