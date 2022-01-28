package com.seedcompany.cordtables.qaautomation;

import static org.testng.Assert.assertNotNull;

import java.util.logging.Logger;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.seedcompany.cordtables.model.TablesOption;
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

public class TablesPageTest extends BaseTestSuite {

	Logger logger = Logger.getLogger(TablesPageTest.class.getName());

	@BeforeMethod
	public void initPageObject() {

	}

	@AfterMethod
	public void cleanupTest() {
		HomePage homePage = PageFactory.initElements(driver, HomePage.class);
		homePage.logout();
	}

	/**
	 * Test case to verify if user is admin and logging into system, he is able to
	 * view the table options.
	 * 
	 */
	@Test

	public void admin_tables_loading_success() {
		HomePage homepage = PageFactory.initElements(driver, HomePage.class);
		homepage.login(homepage.openMenu());
		LoginPage loginPage = PageFactory.initElements(driver, LoginPage.class);
		loginPage.loadApp();
		loginPage.submit(loginPage.fillLoginDetails(testConfig.getAppConfigs().getUsername(),
				testConfig.getAppConfigs().getPassword()));
		SeleniumUtils.wait(2);
		MainPage mainPage = PageFactory.initElements(driver, MainPage.class);
		mainPage.loadApp();
		SearchContext mainMenu = SeleniumUtils.expand_shadow_element(
				mainPage.getRootApp().findElement(By.cssSelector("custom-accordion.hydrated")));
		assertNotNull(mainMenu);
		assertNotNull(mainMenu.findElement(By.cssSelector(".accordion")));
		
		mainPage.selectTable(TablesOption.ADMIN_GROUP_MEMBERSHIPS);

	}

	/**
	 * Test case to verify if user is admin and logging into system, he is able to
	 * view the table options.
	 * 
	 */
	@Test

	public void up_prayer_requests_loading_success() {
		HomePage homepage = PageFactory.initElements(driver, HomePage.class);
		homepage.login(homepage.openMenu());
		LoginPage loginPage = PageFactory.initElements(driver, LoginPage.class);
		loginPage.loadApp();
		loginPage.submit(loginPage.fillLoginDetails(testConfig.getAppConfigs().getUsername(),
				testConfig.getAppConfigs().getPassword()));
		SeleniumUtils.wait(2);
		MainPage mainPage = PageFactory.initElements(driver, MainPage.class);
		mainPage.loadApp();
		SearchContext mainMenu = SeleniumUtils.expand_shadow_element(
				mainPage.getRootApp().findElement(By.cssSelector("custom-accordion.hydrated")));
		assertNotNull(mainMenu);
		assertNotNull(mainMenu.findElement(By.cssSelector(".accordion")));
		
		mainPage.selectTable(TablesOption.UP_PRAYER_REQUESTS);
	}

}