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

	@BeforeClass
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
		formDetails.requestLanguageId = "--2mk7_we-s";
		formDetails.targetLanguageId = "--2mk7_we-s";
		formDetails.sensitivity = "Low";
		formDetails.organizationName = "Seed company";
		formDetails.parent = "";
		formDetails.translator = "X4lvY3Crdw6";
		formDetails.location = "USA";
		formDetails.title = "SEED";
		formDetails.content = "Up prayer request details";
		formDetails.reviewed = "false";
		formDetails.prayerType = "Request";
		List<String> prayerIds = prayerPage.getExistingRequests();
		prayerPage.fillUpPrayerRequestForm(formDetails);
		SeleniumUtils.wait(1);
		prayerPage.loadApp();
		String newPrayerId = isRequestCreated(prayerIds, prayerPage);
		System.out.println("newPrayerId = " + newPrayerId);
		assertNotNull(newPrayerId);
		prayerPage.loadApp();
		prayerPage.deleteRecord(newPrayerId);

	}

	/**
	 * This test case is used to verify that user is able to create the up prayer
	 * request successfully if entered minimal required values and with sensitivity
	 * set to 'LOW' and reviewed set to 'FALSE' and PrayerTypr-'REQUEST'
	 * 
	 */
	@Test

	public void upPrayerRequest_create_success_whenSensitivityLowAndReviewedFalseAndPrayerTyprRequest() {
		UpPrayerTablePage prayerPage = loadUpPrayerPage();
		prayerPage.enableEditMode();
		UpPrayerRequestForm formDetails = new UpPrayerRequestForm();
		formDetails.requestLanguageId = "--2mk7_we-s";
		formDetails.targetLanguageId = "--2mk7_we-s";
		formDetails.sensitivity = "Low";
		formDetails.organizationName = "Seed company";
		formDetails.parent = "";
		formDetails.translator = "X4lvY3Crdw6";
		formDetails.location = "USA";
		formDetails.title = "SEED";
		formDetails.content = "Up prayer request details";
		formDetails.reviewed = "false";
		formDetails.prayerType = "Request";
		List<String> prayerIds = prayerPage.getExistingRequests();
		prayerPage.fillUpPrayerRequestForm(formDetails);
		SeleniumUtils.wait(1);
		prayerPage.loadApp();
		String newPrayerId = isRequestCreated(prayerIds, prayerPage);
		System.out.println("newPrayerId = " + newPrayerId);
		assertNotNull(newPrayerId);
		prayerPage.loadApp();
		prayerPage.deleteRecord(newPrayerId);

	}

	/**
	 * This test case is used to verify that user is able to create the up prayer
	 * request successfully if entered minimal required values and with sensitivity
	 * set to 'MEDIUM' and reviewed set to 'FALSE'and PrayerTypr-'REQUEST'.
	 * 
	 */
	@Test

	public void upPrayerRequest_create_success_whenSensitivityMediumAndReviewedFalseAndPrayerTyprRequest() {
		UpPrayerTablePage prayerPage = loadUpPrayerPage();
		prayerPage.enableEditMode();
		UpPrayerRequestForm formDetails = new UpPrayerRequestForm();
		formDetails.requestLanguageId = "--2mk7_we-s";
		formDetails.targetLanguageId = "--2mk7_we-s";
		formDetails.sensitivity = "Medium";
		formDetails.organizationName = "Seed company";
		formDetails.parent = "";
		formDetails.translator = "X4lvY3Crdw6";
		formDetails.location = "USA";
		formDetails.title = "SEED";
		formDetails.content = "Up prayer request details";
		formDetails.reviewed = "false";
		formDetails.prayerType = "Request";
		List<String> prayerIds = prayerPage.getExistingRequests();
		prayerPage.fillUpPrayerRequestForm(formDetails);
		SeleniumUtils.wait(1);
		prayerPage.loadApp();
		String newPrayerId = isRequestCreated(prayerIds, prayerPage);
		System.out.println("newPrayerId = " + newPrayerId);
		assertNotNull(newPrayerId);
		prayerPage.loadApp();
		prayerPage.deleteRecord(newPrayerId);
	}

	/**
	 * This test case is used to verify that user is able to create the up prayer
	 * request successfully if entered minimal required values and with sensitivity
	 * set to 'HIGH' and reviewed set to 'FALSE' and PrayerType-'REQUEST'.
	 * 
	 */
	@Test

	public void upPrayerRequest_create_success_whenSensitivityHighAndReviewedFalseAndPrayerTypeRequest() {
		UpPrayerTablePage prayerPage = loadUpPrayerPage();
		prayerPage.enableEditMode();
		UpPrayerRequestForm formDetails = new UpPrayerRequestForm();
		formDetails.requestLanguageId = "--2mk7_we-s";
		formDetails.targetLanguageId = "--2mk7_we-s";
		formDetails.sensitivity = "High";
		formDetails.organizationName = "Seed company";
		formDetails.parent = "";
		formDetails.translator = "X4lvY3Crdw6";
		formDetails.location = "USA";
		formDetails.title = "SEED";
		formDetails.content = "Up prayer request details";
		formDetails.reviewed = "false";
		formDetails.prayerType = "Request";
		List<String> prayerIds = prayerPage.getExistingRequests();
		prayerPage.fillUpPrayerRequestForm(formDetails);
		SeleniumUtils.wait(1);
		prayerPage.loadApp();
		String newPrayerId = isRequestCreated(prayerIds, prayerPage);
		System.out.println("newPrayerId = " + newPrayerId);
		assertNotNull(newPrayerId);
		prayerPage.loadApp();
		prayerPage.deleteRecord(newPrayerId);
	}

	/**
	 * This test case is used to verify that user is able to create the up prayer
	 * request successfully if entered minimal required values and with sensitivity
	 * set to 'LOW' and with reviewed set to 'TRUE' and PrayerType-'REQUEST'.
	 * 
	 */
	@Test

	public void upPrayerRequest_create_success_whenSensitivityLowAndReviewedTrueAndPrayerTypeRequest() {
		UpPrayerTablePage prayerPage = loadUpPrayerPage();
		prayerPage.enableEditMode();
		UpPrayerRequestForm formDetails = new UpPrayerRequestForm();
		formDetails.requestLanguageId = "--9_aClXkMy";
		formDetails.targetLanguageId = "--9_aClXkMy";
		formDetails.sensitivity = "Low";
		formDetails.organizationName = "Seed company";
		formDetails.parent = "";
		formDetails.translator = "X4lvY3Crdw6";
		formDetails.location = "USA";
		formDetails.title = "SEED";
		formDetails.content = "Up prayer request details";
		formDetails.reviewed = "true";
		formDetails.prayerType = "Request";
		List<String> prayerIds = prayerPage.getExistingRequests();
		prayerPage.fillUpPrayerRequestForm(formDetails);
		SeleniumUtils.wait(1);
		prayerPage.loadApp();
		String newPrayerId = isRequestCreated(prayerIds, prayerPage);
		System.out.println("newPrayerId = " + newPrayerId);
		assertNotNull(newPrayerId);
		prayerPage.loadApp();
		prayerPage.deleteRecord(newPrayerId);

	}
	
	
	/**
	 * This test case is used to verify that user is able to create the up prayer
	 * request successfully if entered minimal required values and with sensitivity
	 * set to 'MEDIUM' and with reviewed set to 'TRUE',PrayerTypr-'REQUEST'.
	 * 
	 */
	@Test

	public void upPrayerRequest_create_success_whenSensitivityMediumAndReviewedTrueAndPrayerTypeRequest() {
		UpPrayerTablePage prayerPage = loadUpPrayerPage();
		prayerPage.enableEditMode();
		UpPrayerRequestForm formDetails = new UpPrayerRequestForm();
		formDetails.requestLanguageId = "--9_aClXkMy";
		formDetails.targetLanguageId = "--9_aClXkMy";
		formDetails.sensitivity = "Medium";
		formDetails.organizationName = "Seed company";
		formDetails.parent = "";
		formDetails.translator = "X4lvY3Crdw6";
		formDetails.location = "USA";
		formDetails.title = "SEED";
		formDetails.content = "Up prayer request details";
		formDetails.reviewed = "true";
		formDetails.prayerType = "Request";
		List<String> prayerIds = prayerPage.getExistingRequests();
		prayerPage.fillUpPrayerRequestForm(formDetails);
		SeleniumUtils.wait(1);
		prayerPage.loadApp();
		String newPrayerId = isRequestCreated(prayerIds, prayerPage);
		System.out.println("newPrayerId = " + newPrayerId);
		assertNotNull(newPrayerId);
		prayerPage.loadApp();
		prayerPage.deleteRecord(newPrayerId);

	}
	
	/**
	 * This test case is used to verify that user is able to create the up prayer
	 * request successfully if entered minimal required values and with sensitivity
	 * set to 'HIGH' and with reviewed set to 'TRUE'and PrayerType-'REQUEST'.
	 * 
	 */
	@Test

	public void upPrayerRequest_create_success_whenSensitivityHighAndReviewedTrueAndPrayerTypeRequest() {
		UpPrayerTablePage prayerPage = loadUpPrayerPage();
		prayerPage.enableEditMode();
		UpPrayerRequestForm formDetails = new UpPrayerRequestForm();
		formDetails.requestLanguageId = "--9_aClXkMy";
		formDetails.targetLanguageId = "--9_aClXkMy";
		formDetails.sensitivity = "High";
		formDetails.organizationName = "Seed company";
		formDetails.parent = "";
		formDetails.translator = "X4lvY3Crdw6";
		formDetails.location = "USA";
		formDetails.title = "SEED";
		formDetails.content = "Up prayer request details";
		formDetails.reviewed = "true";
		formDetails.prayerType = "Request";
		List<String> prayerIds = prayerPage.getExistingRequests();
		prayerPage.fillUpPrayerRequestForm(formDetails);
		SeleniumUtils.wait(1);
		prayerPage.loadApp();
		String newPrayerId = isRequestCreated(prayerIds, prayerPage);
		System.out.println("newPrayerId = " + newPrayerId);
		assertNotNull(newPrayerId);
		prayerPage.loadApp();
		prayerPage.deleteRecord(newPrayerId);

	}
	
	
	
	/**
	 * This test case is used to verify that user is able to create the up prayer
	 * request successfully if entered minimal required values and with sensitivity
	 * set to 'LOW' and with reviewed set to 'FALSE',PrayerType-'UPDATE'
	 * 
	 */
	@Test

	public void upPrayerRequest_create_success_whenSensitivityLowAndReviewedFalseAndPrayerTypeUpdate() {
		UpPrayerTablePage prayerPage = loadUpPrayerPage();
		prayerPage.enableEditMode();
		UpPrayerRequestForm formDetails = new UpPrayerRequestForm();
		formDetails.requestLanguageId = "--9_aClXkMy";
		formDetails.targetLanguageId = "--9_aClXkMy";
		formDetails.sensitivity = "Low";
		formDetails.organizationName = "Seed company";
		formDetails.parent = "";
		formDetails.translator = "X4lvY3Crdw6";
		formDetails.location = "USA";
		formDetails.title = "SEED";
		formDetails.content = "Up prayer request details";
		formDetails.reviewed = "false";
		formDetails.prayerType = "Update";
		List<String> prayerIds = prayerPage.getExistingRequests();
		prayerPage.fillUpPrayerRequestForm(formDetails);
		SeleniumUtils.wait(1);
		prayerPage.loadApp();
		String newPrayerId = isRequestCreated(prayerIds, prayerPage);
		System.out.println("newPrayerId = " + newPrayerId);
		assertNotNull(newPrayerId);
		prayerPage.loadApp();
		prayerPage.deleteRecord(newPrayerId);

	}
	/**
	 * This test case is used to verify that user is able to create the up prayer
	 * request successfully if entered minimal required values and with sensitivity
	 * set to 'MEDIUM' and with reviewed set to 'FALSE',PrayerType-'UPDATE'
	 * 
	 */
	@Test

	public void upPrayerRequest_create_success_whenSensitivityMediumAndReviewedFalseAndPrayerTypeUpdate() {
		UpPrayerTablePage prayerPage = loadUpPrayerPage();
		prayerPage.enableEditMode();
		UpPrayerRequestForm formDetails = new UpPrayerRequestForm();
		formDetails.requestLanguageId = "--9_aClXkMy";
		formDetails.targetLanguageId = "--9_aClXkMy";
		formDetails.sensitivity = "Medium";
		formDetails.organizationName = "Seed company";
		formDetails.parent = "";
		formDetails.translator = "X4lvY3Crdw6";
		formDetails.location = "USA";
		formDetails.title = "SEED";
		formDetails.content = "Up prayer request details";
		formDetails.reviewed = "false";
		formDetails.prayerType = "Update";
		List<String> prayerIds = prayerPage.getExistingRequests();
		prayerPage.fillUpPrayerRequestForm(formDetails);
		SeleniumUtils.wait(1);
		prayerPage.loadApp();
		String newPrayerId = isRequestCreated(prayerIds, prayerPage);
		System.out.println("newPrayerId = " + newPrayerId);
		assertNotNull(newPrayerId);
		prayerPage.loadApp();
		prayerPage.deleteRecord(newPrayerId);

	}
	/**
	 * This test case is used to verify that user is able to create the up prayer
	 * request successfully if entered minimal required values and with sensitivity
	 * set to 'HIGH' and with reviewed set to 'FALSE',PrayerType-'UPDATE'
	 * 
	 */
	@Test

	public void upPrayerRequest_create_success_whenSensitivityHighAndReviewedFalseAndPrayerTypeUpdate() {
		UpPrayerTablePage prayerPage = loadUpPrayerPage();
		prayerPage.enableEditMode();
		UpPrayerRequestForm formDetails = new UpPrayerRequestForm();
		formDetails.requestLanguageId = "--9_aClXkMy";
		formDetails.targetLanguageId = "--9_aClXkMy";
		formDetails.sensitivity = "High";
		formDetails.organizationName = "Seed company";
		formDetails.parent = "";
		formDetails.translator = "X4lvY3Crdw6";
		formDetails.location = "USA";
		formDetails.title = "SEED";
		formDetails.content = "Up prayer request details";
		formDetails.reviewed = "false";
		formDetails.prayerType = "Update";
		List<String> prayerIds = prayerPage.getExistingRequests();
		prayerPage.fillUpPrayerRequestForm(formDetails);
		SeleniumUtils.wait(1);
		prayerPage.loadApp();
		String newPrayerId = isRequestCreated(prayerIds, prayerPage);
		System.out.println("newPrayerId = " + newPrayerId);
		assertNotNull(newPrayerId);
		prayerPage.loadApp();
		prayerPage.deleteRecord(newPrayerId);

	}
	
	/**
	 * This test case is used to verify that user is able to create the up prayer
	 * request successfully if entered minimal required values and with sensitivity
	 * set to 'LOW' and with reviewed set to 'TRUE',PrayerType-'UPDATE'
	 * 
	 */
	@Test

	public void upPrayerRequest_create_success_whenSensitivityLowAndReviewedTrueAndPrayerTypeUpdate() {
		UpPrayerTablePage prayerPage = loadUpPrayerPage();
		prayerPage.enableEditMode();
		UpPrayerRequestForm formDetails = new UpPrayerRequestForm();
		formDetails.requestLanguageId = "--9_aClXkMy";
		formDetails.targetLanguageId = "--9_aClXkMy";
		formDetails.sensitivity = "Low";
		formDetails.organizationName = "Seed company";
		formDetails.parent = "";
		formDetails.translator = "X4lvY3Crdw6";
		formDetails.location = "USA";
		formDetails.title = "SEED";
		formDetails.content = "Up prayer request details";
		formDetails.reviewed = "true";
		formDetails.prayerType = "Update";
		List<String> prayerIds = prayerPage.getExistingRequests();
		prayerPage.fillUpPrayerRequestForm(formDetails);
		SeleniumUtils.wait(1);
		prayerPage.loadApp();
		String newPrayerId = isRequestCreated(prayerIds, prayerPage);
		System.out.println("newPrayerId = " + newPrayerId);
		assertNotNull(newPrayerId);
		prayerPage.loadApp();
		prayerPage.deleteRecord(newPrayerId);

	}
	/**
	 * This test case is used to verify that user is able to create the up prayer
	 * request successfully if entered minimal required values and with sensitivity
	 * set to 'MEDIUM' and with reviewed set to 'TRUE',PrayerType-'UPDATE'
	 * 
	 */
	@Test

	public void upPrayerRequest_create_success_whenSensitivityMediumAndReviewedTrueAndPrayerTypeUpdate() {
		UpPrayerTablePage prayerPage = loadUpPrayerPage();
		prayerPage.enableEditMode();
		UpPrayerRequestForm formDetails = new UpPrayerRequestForm();
		formDetails.requestLanguageId = "--9_aClXkMy";
		formDetails.targetLanguageId = "--9_aClXkMy";
		formDetails.sensitivity = "Medium";
		formDetails.organizationName = "Seed company";
		formDetails.parent = "";
		formDetails.translator = "X4lvY3Crdw6";
		formDetails.location = "USA";
		formDetails.title = "SEED";
		formDetails.content = "Up prayer request details";
		formDetails.reviewed = "true";
		formDetails.prayerType = "Update";
		List<String> prayerIds = prayerPage.getExistingRequests();
		prayerPage.fillUpPrayerRequestForm(formDetails);
		SeleniumUtils.wait(1);
		prayerPage.loadApp();
		String newPrayerId = isRequestCreated(prayerIds, prayerPage);
		System.out.println("newPrayerId = " + newPrayerId);
		assertNotNull(newPrayerId);
		prayerPage.loadApp();
		prayerPage.deleteRecord(newPrayerId);

	}
	/**
	 * This test case is used to verify that user is able to create the up prayer
	 * request successfully if entered minimal required values and with sensitivity
	 * set to 'HIGH' and with reviewed set to 'TRUE',PrayerType-'UPDATE'
	 * 
	 */
	@Test

	public void upPrayerRequest_create_success_whenSensitivityHighAndReviewedTrueAndPrayerTypeUpdate() {
		UpPrayerTablePage prayerPage = loadUpPrayerPage();
		prayerPage.enableEditMode();
		UpPrayerRequestForm formDetails = new UpPrayerRequestForm();
		formDetails.requestLanguageId = "--9_aClXkMy";
		formDetails.targetLanguageId = "--9_aClXkMy";
		formDetails.sensitivity = "High";
		formDetails.organizationName = "Seed company";
		formDetails.parent = "";
		formDetails.translator = "X4lvY3Crdw6";
		formDetails.location = "USA";
		formDetails.title = "SEED";
		formDetails.content = "Up prayer request details";
		formDetails.reviewed = "true";
		formDetails.prayerType = "Update";
		List<String> prayerIds = prayerPage.getExistingRequests();
		prayerPage.fillUpPrayerRequestForm(formDetails);
		SeleniumUtils.wait(1);
		prayerPage.loadApp();
		String newPrayerId = isRequestCreated(prayerIds, prayerPage);
		System.out.println("newPrayerId = " + newPrayerId);
		assertNotNull(newPrayerId);
		prayerPage.loadApp();
		prayerPage.deleteRecord(newPrayerId);

	}
	/**
	 * This test case is used to verify that user is able to create the up prayer
	 * request successfully if entered minimal required values and with sensitivity
	 * set to 'LOW' and with reviewed set to 'FALSE',PrayerType-'CELEBRATION'
	 * 
	 */
	@Test

	public void upPrayerRequest_create_success_whenSensitivityLowAndReviewedFalseAndPrayerTypeCelebration() {
		UpPrayerTablePage prayerPage = loadUpPrayerPage();
		prayerPage.enableEditMode();
		UpPrayerRequestForm formDetails = new UpPrayerRequestForm();
		formDetails.requestLanguageId = "--9_aClXkMy";
		formDetails.targetLanguageId = "--9_aClXkMy";
		formDetails.sensitivity = "Low";
		formDetails.organizationName = "Seed company";
		formDetails.parent = "";
		formDetails.translator = "X4lvY3Crdw6";
		formDetails.location = "USA";
		formDetails.title = "SEED";
		formDetails.content = "Up prayer request details";
		formDetails.reviewed = "false";
		formDetails.prayerType = "Celebration";
		List<String> prayerIds = prayerPage.getExistingRequests();
		prayerPage.fillUpPrayerRequestForm(formDetails);
		SeleniumUtils.wait(1);
		prayerPage.loadApp();
		String newPrayerId = isRequestCreated(prayerIds, prayerPage);
		System.out.println("newPrayerId = " + newPrayerId);
		assertNotNull(newPrayerId);
		prayerPage.loadApp();
		prayerPage.deleteRecord(newPrayerId);

	}
	/**
	 * This test case is used to verify that user is able to create the up prayer
	 * request successfully if entered minimal required values and with sensitivity
	 * set to 'MEDIUM' and with reviewed set to 'FALSE',PrayerType-'CELEBRATION'
	 * 
	 */
	@Test

	public void upPrayerRequest_create_success_whenSensitivityMediumAndReviewedFalseAndPrayerTypeCelebration() {
		UpPrayerTablePage prayerPage = loadUpPrayerPage();
		prayerPage.enableEditMode();
		UpPrayerRequestForm formDetails = new UpPrayerRequestForm();
		formDetails.requestLanguageId = "--9_aClXkMy";
		formDetails.targetLanguageId = "--9_aClXkMy";
		formDetails.sensitivity = "Medium";
		formDetails.organizationName = "Seed company";
		formDetails.parent = "";
		formDetails.translator = "X4lvY3Crdw6";
		formDetails.location = "USA";
		formDetails.title = "SEED";
		formDetails.content = "Up prayer request details";
		formDetails.reviewed = "false";
		formDetails.prayerType = "Celebration";
		List<String> prayerIds = prayerPage.getExistingRequests();
		prayerPage.fillUpPrayerRequestForm(formDetails);
		SeleniumUtils.wait(1);
		prayerPage.loadApp();
		String newPrayerId = isRequestCreated(prayerIds, prayerPage);
		System.out.println("newPrayerId = " + newPrayerId);
		assertNotNull(newPrayerId);
		prayerPage.loadApp();
		prayerPage.deleteRecord(newPrayerId);

	}	
	
	/**
	 * This test case is used to verify that user is able to create the up prayer
	 * request successfully if entered minimal required values and with sensitivity
	 * set to 'HIGH' and with reviewed set to 'FALSE',PrayerType-'CELEBRATION'
	 * 
	 */
	@Test

	public void upPrayerRequest_create_success_whenSensitivityHighAndReviewedFalseAndPrayerTypeCelebration() {
		UpPrayerTablePage prayerPage = loadUpPrayerPage();
		prayerPage.enableEditMode();
		UpPrayerRequestForm formDetails = new UpPrayerRequestForm();
		formDetails.requestLanguageId = "--9_aClXkMy";
		formDetails.targetLanguageId = "--9_aClXkMy";
		formDetails.sensitivity = "High";
		formDetails.organizationName = "Seed company";
		formDetails.parent = "";
		formDetails.translator = "X4lvY3Crdw6";
		formDetails.location = "USA";
		formDetails.title = "SEED";
		formDetails.content = "Up prayer request details";
		formDetails.reviewed = "false";
		formDetails.prayerType = "Celebration";
		List<String> prayerIds = prayerPage.getExistingRequests();
		prayerPage.fillUpPrayerRequestForm(formDetails);
		SeleniumUtils.wait(1);
		prayerPage.loadApp();
		String newPrayerId = isRequestCreated(prayerIds, prayerPage);
		System.out.println("newPrayerId = " + newPrayerId);
		assertNotNull(newPrayerId);
		prayerPage.loadApp();
		prayerPage.deleteRecord(newPrayerId);

	}
	
	/**
	 * This test case is used to verify that user is able to create the up prayer
	 * request successfully if entered minimal required values and with sensitivity
	 * set to 'LOW' and with reviewed set to 'TRUE',PrayerType-'CELEBRATION'
	 * 
	 */
	@Test

	public void upPrayerRequest_create_success_whenSensitivityLowAndReviewedTrueAndPrayerTypeCelebration() {
		UpPrayerTablePage prayerPage = loadUpPrayerPage();
		prayerPage.enableEditMode();
		UpPrayerRequestForm formDetails = new UpPrayerRequestForm();
		formDetails.requestLanguageId = "--9_aClXkMy";
		formDetails.targetLanguageId = "--9_aClXkMy";
		formDetails.sensitivity = "Low";
		formDetails.organizationName = "Seed company";
		formDetails.parent = "";
		formDetails.translator = "X4lvY3Crdw6";
		formDetails.location = "USA";
		formDetails.title = "SEED";
		formDetails.content = "Up prayer request details";
		formDetails.reviewed = "true";
		formDetails.prayerType = "Celebration";
		List<String> prayerIds = prayerPage.getExistingRequests();
		prayerPage.fillUpPrayerRequestForm(formDetails);
		SeleniumUtils.wait(1);
		prayerPage.loadApp();
		String newPrayerId = isRequestCreated(prayerIds, prayerPage);
		System.out.println("newPrayerId = " + newPrayerId);
		assertNotNull(newPrayerId);
		prayerPage.loadApp();
		prayerPage.deleteRecord(newPrayerId);

	}
	/**
	 * This test case is used to verify that user is able to create the up prayer
	 * request successfully if entered minimal required values and with sensitivity
	 * set to 'MEDIUM' and with reviewed set to 'TRUE',PrayerType-'CELEBRATION'
	 * 
	 */
	@Test

	public void upPrayerRequest_create_success_whenSensitivityMediumAndReviewedTrueAndPrayerTypeCelebration() {
		UpPrayerTablePage prayerPage = loadUpPrayerPage();
		prayerPage.enableEditMode();
		UpPrayerRequestForm formDetails = new UpPrayerRequestForm();
		formDetails.requestLanguageId = "--9_aClXkMy";
		formDetails.targetLanguageId = "--9_aClXkMy";
		formDetails.sensitivity = "Medium";
		formDetails.organizationName = "Seed company";
		formDetails.parent = "";
		formDetails.translator = "X4lvY3Crdw6";
		formDetails.location = "USA";
		formDetails.title = "SEED";
		formDetails.content = "Up prayer request details";
		formDetails.reviewed = "true";
		formDetails.prayerType = "Celebration";
		List<String> prayerIds = prayerPage.getExistingRequests();
		prayerPage.fillUpPrayerRequestForm(formDetails);
		SeleniumUtils.wait(1);
		prayerPage.loadApp();
		String newPrayerId = isRequestCreated(prayerIds, prayerPage);
		System.out.println("newPrayerId = " + newPrayerId);
		assertNotNull(newPrayerId);
		prayerPage.loadApp();
		prayerPage.deleteRecord(newPrayerId);

	}	
	/**
	 * This test case is used to verify that user is able to create the up prayer
	 * request successfully if entered minimal required values and with sensitivity
	 * set to 'HIGH' and with reviewed set to 'TRUE',PrayerType-'CELEBRATION'
	 * 
	 */
	@Test

	public void upPrayerRequest_create_success_whenSensitivityHighAndReviewedTrueAndPrayerTypeCelebration() {
		UpPrayerTablePage prayerPage = loadUpPrayerPage();
		prayerPage.enableEditMode();
		UpPrayerRequestForm formDetails = new UpPrayerRequestForm();
		formDetails.requestLanguageId = "--9_aClXkMy";
		formDetails.targetLanguageId = "--9_aClXkMy";
		formDetails.sensitivity = "High";
		formDetails.organizationName = "Seed company";
		formDetails.parent = "";
		formDetails.translator = "X4lvY3Crdw6";
		formDetails.location = "USA";
		formDetails.title = "SEED";
		formDetails.content = "Up prayer request details";
		formDetails.reviewed = "true";
		formDetails.prayerType = "Celebration";
		List<String> prayerIds = prayerPage.getExistingRequests();
		prayerPage.fillUpPrayerRequestForm(formDetails);
		SeleniumUtils.wait(1);
		prayerPage.loadApp();
		String newPrayerId = isRequestCreated(prayerIds, prayerPage);
		System.out.println("newPrayerId = " + newPrayerId);
		assertNotNull(newPrayerId);
		prayerPage.loadApp();
		prayerPage.deleteRecord(newPrayerId);

	}	
	
	private String isRequestCreated(List<String> oldPrayerIds, UpPrayerTablePage prayerPage) {

		List<String> newPrayerIds = prayerPage.getExistingRequests();
		assertNotNull(newPrayerIds);
		newPrayerIds.removeAll(oldPrayerIds);
		assertNotNull(newPrayerIds);
		assertTrue(newPrayerIds.size() == 1);

		return newPrayerIds.get(0);
	}
}
