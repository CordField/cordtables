package com.seedcompany.cordtables.qaautomation;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.List;
import java.util.logging.Logger;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.seedcompany.cordtables.model.TablesOption;
import com.seedcompany.cordtables.model.UpPrayerRequestForm;
import com.seedcompany.cordtables.pages.HomePage;
import com.seedcompany.cordtables.pages.LoginPage;
import com.seedcompany.cordtables.pages.MainPage;
import com.seedcompany.cordtables.pages.UpPrayerTablePage;
import com.seedcompany.cordtables.utils.SeleniumUtils;

/**
 * 
 * This test suite covers all the test cases associated with the up prayer table
 * page.
 * 
 * @author swati
 *
 */
public class UpPrayerTablePageTest extends BaseTestSuite {

	Logger logger = Logger.getLogger(TablesPageTest.class.getName());

	@BeforeMethod
	public void initPageObject() {
		HomePage homepage = PageFactory.initElements(driver, HomePage.class);
		homepage.login(homepage.openMenu());
		LoginPage loginPage = PageFactory.initElements(driver, LoginPage.class);
		loginPage.loadApp();
		loginPage.submit(loginPage.fillLoginDetails(testConfig.getAppConfigs().getUsername(),
				testConfig.getAppConfigs().getPassword()));
		SeleniumUtils.wait(1);
		MainPage mainPage = PageFactory.initElements(driver, MainPage.class);
		mainPage.loadApp();
		SearchContext mainMenu = SeleniumUtils
				.expand_shadow_element(mainPage.getRootApp().findElement(By.cssSelector("custom-accordion.hydrated")));
		assertNotNull(mainMenu);
		assertNotNull(mainMenu.findElement(By.cssSelector(".accordion")));
		mainPage.selectTable(TablesOption.UP_PRAYER_REQUESTS);
	}

	@AfterMethod
	public void cleanupTest() {
		HomePage homePage = PageFactory.initElements(driver, HomePage.class);
		homePage.logout();
		SeleniumUtils.wait(1);
	}

	private UpPrayerTablePage loadUpPrayerPage() {

		UpPrayerTablePage page = PageFactory.initElements(driver, UpPrayerTablePage.class);
		SeleniumUtils.wait(1);
		page.loadApp();
		return page;
	}

	/**
	 * This test case is used to verify that user is able to create the up prayer
	 * request successfully if entered minimal required values.
	 * 
	 */
	@Test

	public void upPrayerRequest_create_success_default() {
		UpPrayerTablePage prayerPage = loadUpPrayerPage();
		prayerPage.enableEditMode();
		UpPrayerRequestForm formDetails = new UpPrayerRequestForm();
		formDetails.requestLanguageId = "000f2e6e-beba-476d-9810-312de15d04a1";
		formDetails.targetLanguageId = "000f2e6e-beba-476d-9810-312de15d04a1";
		formDetails.sensitivity = "";
		formDetails.organizationName = "Seed company";
		formDetails.parent = "";
		formDetails.translator = "c6bde52b-cfde-4081-a287-17a77aea013c";
		formDetails.location = "USA";
		formDetails.title = "SEED";
		formDetails.content = "Up prayer request details";
		formDetails.reviewed = "false";
		formDetails.prayerType = "Request";
		List<String> prayerIds = prayerPage.getExistingRequests();
		prayerPage.fillUpPrayerRequestForm(formDetails);
		SeleniumUtils.wait(1);
		prayerPage.loadApp();
		String newPrayerId = requestCreated(prayerIds, prayerPage);
		System.out.println("newPrayerId = " + newPrayerId);
		assertNotNull(newPrayerId);
		prayerPage.loadApp();
		prayerPage.deleteRecord(newPrayerId);

	}

	/**
	 * This test case is used to verify that user is able to create the up prayer
	 * request successfully if entered minimal required values and with sensitivity
	 * set to Low and reviewed set to false.
	 * 
	 */
	@Test

	public void upPrayerRequest_create_success_whenSensitivityLowAndReviewedFalse() {
		UpPrayerTablePage prayerPage = loadUpPrayerPage();
		prayerPage.enableEditMode();
		UpPrayerRequestForm formDetails = new UpPrayerRequestForm();
		formDetails.requestLanguageId = "000f2e6e-beba-476d-9810-312de15d04a1";
		formDetails.targetLanguageId = "000f2e6e-beba-476d-9810-312de15d04a1";
		formDetails.sensitivity = "Low";
		formDetails.organizationName = "Seed company";
		formDetails.parent = "";
		formDetails.translator = "c6bde52b-cfde-4081-a287-17a77aea013c";
		formDetails.location = "USA";
		formDetails.title = "SEED";
		formDetails.content = "Up prayer request details";
		formDetails.reviewed = "false";
		formDetails.prayerType = "Request";
		List<String> prayerIds = prayerPage.getExistingRequests();
		prayerPage.fillUpPrayerRequestForm(formDetails);
		SeleniumUtils.wait(1);
		prayerPage.loadApp();
		String newPrayerId = requestCreated(prayerIds, prayerPage);
		System.out.println("newPrayerId = " + newPrayerId);
		assertNotNull(newPrayerId);
		prayerPage.loadApp();
		prayerPage.deleteRecord(newPrayerId);

	}

	/**
	 * This test case is used to verify that user is able to create the up prayer
	 * request successfully if entered minimal required values and with sensitivity
	 * set to Medium and reviewed set to false.
	 * 
	 */
	@Test

	public void upPrayerRequest_create_success_whenSensitivityMediumAndReviewedFalse() {
		UpPrayerTablePage prayerPage = loadUpPrayerPage();
		prayerPage.enableEditMode();
		UpPrayerRequestForm formDetails = new UpPrayerRequestForm();
		formDetails.requestLanguageId = "000f2e6e-beba-476d-9810-312de15d04a1";
		formDetails.targetLanguageId = "000f2e6e-beba-476d-9810-312de15d04a1";
		formDetails.sensitivity = "Medium";
		formDetails.organizationName = "Seed company";
		formDetails.parent = "";
		formDetails.translator = "c6bde52b-cfde-4081-a287-17a77aea013c";
		formDetails.location = "USA";
		formDetails.title = "SEED";
		formDetails.content = "Up prayer request details";
		formDetails.reviewed = "false";
		formDetails.prayerType = "Request";
		List<String> prayerIds = prayerPage.getExistingRequests();
		prayerPage.fillUpPrayerRequestForm(formDetails);
		SeleniumUtils.wait(1);
		prayerPage.loadApp();
		String newPrayerId = requestCreated(prayerIds, prayerPage);
		System.out.println("newPrayerId = " + newPrayerId);
		assertNotNull(newPrayerId);
		prayerPage.loadApp();
		prayerPage.deleteRecord(newPrayerId);
	}

	/**
	 * This test case is used to verify that user is able to create the up prayer
	 * request successfully if entered minimal required values and with sensitivity
	 * set to High and reviewed set to false.
	 * 
	 */
	@Test

	public void upPrayerRequest_create_success_whenSensitivityHighAndReviewedFalse() {
		UpPrayerTablePage prayerPage = loadUpPrayerPage();
		prayerPage.enableEditMode();
		UpPrayerRequestForm formDetails = new UpPrayerRequestForm();
		formDetails.requestLanguageId = "000f2e6e-beba-476d-9810-312de15d04a1";
		formDetails.targetLanguageId = "000f2e6e-beba-476d-9810-312de15d04a1";
		formDetails.sensitivity = "High";
		formDetails.organizationName = "Seed company";
		formDetails.parent = "";
		formDetails.translator = "c6bde52b-cfde-4081-a287-17a77aea013c";
		formDetails.location = "USA";
		formDetails.title = "SEED";
		formDetails.content = "Up prayer request details";
		formDetails.reviewed = "false";
		formDetails.prayerType = "Request";
		List<String> prayerIds = prayerPage.getExistingRequests();
		prayerPage.fillUpPrayerRequestForm(formDetails);
		SeleniumUtils.wait(1);
		prayerPage.loadApp();
		String newPrayerId = requestCreated(prayerIds, prayerPage);
		System.out.println("newPrayerId = " + newPrayerId);
		assertNotNull(newPrayerId);
		prayerPage.loadApp();
		prayerPage.deleteRecord(newPrayerId);
	}

	/**
	 * This test case is used to verify that user is able to create the up prayer
	 * request successfully if entered minimal required values and with reviewed set
	 * to True.
	 * 
	 */
	@Test

	public void upPrayerRequest_create_success_whenReviewedTrue() {
		UpPrayerTablePage prayerPage = loadUpPrayerPage();
		prayerPage.enableEditMode();
		UpPrayerRequestForm formDetails = new UpPrayerRequestForm();
		formDetails.requestLanguageId = "000f2e6e-beba-476d-9810-312de15d04a1";
		formDetails.targetLanguageId = "000f2e6e-beba-476d-9810-312de15d04a1";
		formDetails.sensitivity = "";
		formDetails.organizationName = "Seed company";
		formDetails.parent = "";
		formDetails.translator = "c6bde52b-cfde-4081-a287-17a77aea013c";
		formDetails.location = "USA";
		formDetails.title = "SEED";
		formDetails.content = "Up prayer request details";
		formDetails.reviewed = "true";
		formDetails.prayerType = "Request";
		List<String> prayerIds = prayerPage.getExistingRequests();
		prayerPage.fillUpPrayerRequestForm(formDetails);
		SeleniumUtils.wait(1);
		prayerPage.loadApp();
		String newPrayerId = requestCreated(prayerIds, prayerPage);
		System.out.println("newPrayerId = " + newPrayerId);
		assertNotNull(newPrayerId);
		prayerPage.loadApp();
		prayerPage.deleteRecord(newPrayerId);

	}

	private String requestCreated(List<String> oldPrayerIds, UpPrayerTablePage prayerPage) {

		List<String> newPrayerIds = prayerPage.getExistingRequests();
		assertNotNull(newPrayerIds);
		newPrayerIds.removeAll(oldPrayerIds);
		assertNotNull(newPrayerIds);
		assertTrue(newPrayerIds.size() == 1);

		return newPrayerIds.get(0);
	}
}
