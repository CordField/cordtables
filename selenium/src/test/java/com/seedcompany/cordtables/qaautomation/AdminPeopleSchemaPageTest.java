package com.seedcompany.cordtables.qaautomation;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.seedcompany.cordtables.model.LocationInfo;
import com.seedcompany.cordtables.model.People;
import com.seedcompany.cordtables.model.TablesOption;
import com.seedcompany.cordtables.pages.HomePage;
import com.seedcompany.cordtables.pages.LocationsSchemaPage;
import com.seedcompany.cordtables.pages.LoginPage;
import com.seedcompany.cordtables.pages.MainPage;
import com.seedcompany.cordtables.pages.PeopleSchemaPage;
import com.seedcompany.cordtables.utils.SeleniumUtils;

/**
 * 
 * This test suite covers all the test cases associated with the Admn people
 * form page.
 * 
 * @author swati
 *
 */
public class AdminPeopleSchemaPageTest extends BaseTestSuite {

	Logger logger = LoggerFactory.getLogger(AdminPeopleSchemaPageTest.class);

	@BeforeClass
	public void initPageObject() {
		HomePage homepage = PageFactory.initElements(driver, HomePage.class);
		homepage.login(homepage.openMenu());
		LoginPage loginPage = PageFactory.initElements(driver, LoginPage.class);
		loginPage.loadApp();
		loginPage.submit(loginPage.fillLoginDetails(testConfig.getAppConfigs().getUsername(),
				testConfig.getAppConfigs().getPassword()));
		SeleniumUtils.wait(1);
	}

	@AfterMethod
	public void cleanupTest() {
		HomePage homePage = PageFactory.initElements(driver, HomePage.class);
		homePage.logout();
		SeleniumUtils.wait(1);
	}

	private PeopleSchemaPage loadAdminPeoplePage() {

		PeopleSchemaPage page = PageFactory.initElements(driver, PeopleSchemaPage.class);
		SeleniumUtils.wait(1);
		page.loadApp();
		return page;
	}

	private People getRequestData(List<String> data) {
		People requestData = new People();

		requestData.about = data.get(0);
		requestData.phonenumber = data.get(1);
		requestData.picture = data.get(2);
		requestData.privatefirstname = data.get(3);
		requestData.privatelastname = data.get(4);
		requestData.publicfirstname = data.get(5);
		requestData.publiclastname = data.get(6);
		requestData.primarylocation = data.get(7);
		requestData.privatefullname = data.get(8);
		requestData.publicfullname = data.get(9);
		requestData.sensitivityclearance = data.get(10);
		requestData.timezone = data.get(11);
		requestData.title = data.get(12);
		return requestData;
	}

	private People defaultTestData() {
		People formDetails = new People();
		formDetails.about = "People Personal Details";
		formDetails.phonenumber = "345123";
		formDetails.picture = "abc.jpeg";
		formDetails.privatefirstname = "david";
		formDetails.privatelastname = "gob";
		formDetails.publicfirstname = "john";
		formDetails.publiclastname = "gob";
		formDetails.privatefullname = "david john gob";
		formDetails.publicfullname = "favid gob";
		formDetails.sensitivityclearance = "Low";
		formDetails.timezone = "cst";
		formDetails.title = "Mr";
		return formDetails;
	}

	/**
	 * Method to validate the Admin people creation scenario.
	 * 
	 * @param formDetails
	 */
	private People validateAdminPeopleCreatin(People formDetails, boolean delete) {
		MainPage mainPage = PageFactory.initElements(driver, MainPage.class);
		mainPage.loadApp();
		SearchContext mainMenu = SeleniumUtils
				.expand_shadow_element(mainPage.getRootApp().findElement(By.cssSelector("custom-accordion.hydrated")));
		assertNotNull(mainMenu);
		assertNotNull(mainMenu.findElement(By.cssSelector(".accordion")));
		assertTrue(mainPage.selectSchema(TablesOption.ADMIN_PEOPLE));

		PeopleSchemaPage peoplePage = loadAdminPeoplePage();
		SeleniumUtils.wait(2);
		peoplePage.menuUtils.enableEditMode();

		List<String> peopleIds = peoplePage.getExistingPeople();
		peoplePage.fillPeopleRequestForm(formDetails);
		SeleniumUtils.wait(1);
		peoplePage.loadApp();
		List<String> newPeopleId = isRequestCreated(peopleIds, peoplePage);
		assertNotNull(newPeopleId);
		People peopleData = getRequestData(peoplePage.findPeople(newPeopleId.get(0)));

		assertNotNull(peopleData);
		peoplePage.loadApp();
		if (delete) {
			peoplePage.deleteRecord(newPeopleId.get(0));
		}

		return peopleData;

	}

	private List<String> isRequestCreated(List<String> oldPeopleIds, PeopleSchemaPage peoplePage) {

		List<String> newPeopleIds = peoplePage.getExistingPeople();
		assertNotNull(newPeopleIds);
		newPeopleIds.removeAll(oldPeopleIds);
		assertNotNull(newPeopleIds);
		assertTrue(newPeopleIds.size() == 1);

		return newPeopleIds;
	}

	private LocationInfo getLocationInfo() {
		LocationInfo locationInfo = new LocationInfo();

		MainPage mainPage = PageFactory.initElements(driver, MainPage.class);
		mainPage.loadApp();
		SearchContext mainMenu = SeleniumUtils
				.expand_shadow_element(mainPage.getRootApp().findElement(By.cssSelector("custom-accordion.hydrated")));
		assertNotNull(mainMenu);
		assertNotNull(mainMenu.findElement(By.cssSelector(".accordion")));
		assertTrue(mainPage.selectSchema(TablesOption.COMMON_LOCATIONS));

		LocationsSchemaPage locationSchemaPage = PageFactory.initElements(driver, LocationsSchemaPage.class);
		locationSchemaPage.loadApp();
		locationSchemaPage.menuUtils.enableEditMode();

		locationInfo.isoAlpha3 = "iso";
		locationInfo.name = "TX";
		locationInfo.sensitivity = "High";
		locationInfo.type = "State";

		List<String> locationIds = locationSchemaPage.getExistingLocations();
		if (locationIds != null && locationIds.size() > 0) {
			locationInfo.id = locationIds.get(0);
		} else {
			locationSchemaPage.fillCommonLocationForm(locationInfo);
			locationIds = locationSchemaPage.getExistingLocations();
			locationInfo.id = locationIds.get(0);

		}
		logger.debug("locationInfo:: " + locationInfo);
		return locationInfo;

	}

	/**
	 * This test case is used to verify that user is able to fill the admin.people
	 * form successfully if entered minimal required values.
	 * 
	 */
	@Test
	public void adminPeople_fill_form_success_default() {
		People formDetails = defaultTestData();
		formDetails.sensitivityclearance = "Low";
		LocationInfo locationInfo = getLocationInfo();
		assertNotNull(locationInfo);
		formDetails.primarylocation = locationInfo.id;
		validateAdminPeopleCreatin(formDetails, false);
	}

}
