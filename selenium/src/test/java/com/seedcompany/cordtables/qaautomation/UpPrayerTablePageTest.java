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
import org.testng.annotations.Test;

import com.seedcompany.cordtables.model.TablesOption;
import com.seedcompany.cordtables.model.UpPrayerRequest;
import com.seedcompany.cordtables.pages.HomePage;
import com.seedcompany.cordtables.pages.LoginPage;
import com.seedcompany.cordtables.pages.MainPage;
import com.seedcompany.cordtables.pages.PrayerRequestsSchemaPage;
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
		assertTrue(mainPage.selectSchema(TablesOption.UP_PRAYER_REQUESTS));
	}

	@AfterMethod
	public void cleanupTest() {
		HomePage homePage = PageFactory.initElements(driver, HomePage.class);
		homePage.logout();
		SeleniumUtils.wait(1);
	}

	private PrayerRequestsSchemaPage loadUpPrayerPage() {

		PrayerRequestsSchemaPage page = PageFactory.initElements(driver, PrayerRequestsSchemaPage.class);
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
		PrayerRequestsSchemaPage prayerPage = loadUpPrayerPage();
		prayerPage.enableEditMode();
		UpPrayerRequest formDetails = defaultTestData();
		formDetails.sensitivity = "Low";
		validatePrayerRequestCreation(formDetails, false);

	}

	/**
	 * This test case is used to verify that user is able to create the up prayer
	 * request successfully if entered minimal required values and with sensitivity
	 * set to 'LOW' and reviewed set to 'FALSE' and PrayerTypr-'REQUEST'
	 * 
	 */
	@Test

	public void upPrayerRequest_create_success_whenSensitivityLowAndReviewedFalseAndPrayerTyprRequest() {
		PrayerRequestsSchemaPage prayerPage = loadUpPrayerPage();
		prayerPage.enableEditMode();
		UpPrayerRequest formDetails = defaultTestData();
		formDetails.sensitivity = "Low";
		validatePrayerRequestCreation(formDetails, true);

	}

	/**
	 * This test case is used to verify that user is able to create the up prayer
	 * request successfully if entered minimal required values and with sensitivity
	 * set to 'MEDIUM' and reviewed set to 'FALSE'and PrayerTypr-'REQUEST'.
	 * 
	 */
	@Test

	public void upPrayerRequest_create_success_whenSensitivityMediumAndReviewedFalseAndPrayerTyprRequest() {
		PrayerRequestsSchemaPage prayerPage = loadUpPrayerPage();
		prayerPage.enableEditMode();
		UpPrayerRequest formDetails = defaultTestData();
		formDetails.sensitivity = "Medium";
		formDetails.reviewed = "false";
		validatePrayerRequestCreation(formDetails, true);
	}

	/**
	 * This test case is used to verify that user is able to create the up prayer
	 * request successfully if entered minimal required values and with sensitivity
	 * set to 'HIGH' and reviewed set to 'FALSE' and PrayerType-'REQUEST'.
	 * 
	 */
	@Test

	public void upPrayerRequest_create_success_whenSensitivityHighAndReviewedFalseAndPrayerTypeRequest() {
		PrayerRequestsSchemaPage prayerPage = loadUpPrayerPage();
		prayerPage.enableEditMode();
		UpPrayerRequest formDetails = defaultTestData();
		formDetails.sensitivity = "High";
		formDetails.reviewed = "false";
		validatePrayerRequestCreation(formDetails, true);
	}

	/**
	 * This test case is used to verify that user is able to create the up prayer
	 * request successfully if entered minimal required values and with sensitivity
	 * set to 'LOW' and with reviewed set to 'TRUE' and PrayerType-'REQUEST'.
	 * 
	 */
	@Test

	public void upPrayerRequest_create_success_whenSensitivityLowAndReviewedTrueAndPrayerTypeRequest() {
		PrayerRequestsSchemaPage prayerPage = loadUpPrayerPage();
		prayerPage.enableEditMode();
		UpPrayerRequest formDetails = defaultTestData();
		formDetails.sensitivity = "Low";
		formDetails.reviewed = "true";
		validatePrayerRequestCreation(formDetails, true);

	}

	/**
	 * This test case is used to verify that user is able to create the up prayer
	 * request successfully if entered minimal required values and with sensitivity
	 * set to 'MEDIUM' and with reviewed set to 'TRUE',PrayerTypr-'REQUEST'.
	 * 
	 */
	@Test

	public void upPrayerRequest_create_success_whenSensitivityMediumAndReviewedTrueAndPrayerTypeRequest() {
		PrayerRequestsSchemaPage prayerPage = loadUpPrayerPage();
		prayerPage.enableEditMode();
		UpPrayerRequest formDetails = defaultTestData();
		formDetails.sensitivity = "Medium";
		formDetails.reviewed = "true";
		validatePrayerRequestCreation(formDetails, true);

	}

	/**
	 * This test case is used to verify that user is able to create the up prayer
	 * request successfully if entered minimal required values and with sensitivity
	 * set to 'HIGH' and with reviewed set to 'TRUE'and PrayerType-'REQUEST'.
	 * 
	 */
	@Test

	public void upPrayerRequest_create_success_whenSensitivityHighAndReviewedTrueAndPrayerTypeRequest() {
		PrayerRequestsSchemaPage prayerPage = loadUpPrayerPage();
		prayerPage.enableEditMode();
		UpPrayerRequest formDetails = defaultTestData();
		formDetails.sensitivity = "High";
		formDetails.reviewed = "true";
		validatePrayerRequestCreation(formDetails, true);

	}

	/**
	 * This test case is used to verify that user is able to create the up prayer
	 * request successfully if entered minimal required values and with sensitivity
	 * set to 'LOW' and with reviewed set to 'FALSE',PrayerType-'UPDATE'
	 * 
	 */
	@Test

	public void upPrayerRequest_create_success_whenSensitivityLowAndReviewedFalseAndPrayerTypeUpdate() {
		PrayerRequestsSchemaPage prayerPage = loadUpPrayerPage();
		prayerPage.enableEditMode();
		UpPrayerRequest formDetails = defaultTestData();
		formDetails.sensitivity = "Low";
		formDetails.reviewed = "false";
		formDetails.prayerType = "Update";
		validatePrayerRequestCreation(formDetails, true);

	}

	/**
	 * This test case is used to verify that user is able to create the up prayer
	 * request successfully if entered minimal required values and with sensitivity
	 * set to 'LOW' and with reviewed set to 'FALSE',PrayerType-'CELEBRATION'
	 * 
	 */
	@Test

	public void upPrayerRequest_create_success_whenSensitivityLowAndReviewedFalseAndPrayerTypeCelebration() {
		PrayerRequestsSchemaPage prayerPage = loadUpPrayerPage();
		prayerPage.enableEditMode();
		UpPrayerRequest formDetails = defaultTestData();
		formDetails.sensitivity = "Low";
		formDetails.reviewed = "false";
		formDetails.prayerType = "Celebration";
		validatePrayerRequestCreation(formDetails, true);

	}

	/**
	 * This test case is used to verify that user is able to create the up prayer
	 * request successfully if entered minimal required values and with sensitivity
	 * set to 'MEDIUM' and with reviewed set to 'FALSE',PrayerType-'CELEBRATION'
	 * 
	 */
	@Test

	public void upPrayerRequest_create_success_whenSensitivityMediumAndReviewedFalseAndPrayerTypeCelebration() {
		PrayerRequestsSchemaPage prayerPage = loadUpPrayerPage();
		prayerPage.enableEditMode();
		UpPrayerRequest formDetails = defaultTestData();
		formDetails.sensitivity = "Medium";
		formDetails.reviewed = "false";
		formDetails.prayerType = "Celebration";
		validatePrayerRequestCreation(formDetails, true);

	}

	/**
	 * This test case is used to verify that user is able to create the up prayer
	 * request successfully if entered minimal required values and with sensitivity
	 * set to 'HIGH' and with reviewed set to 'FALSE',PrayerType-'CELEBRATION'
	 * 
	 */
	@Test

	public void upPrayerRequest_create_success_whenSensitivityHighAndReviewedFalseAndPrayerTypeCelebration() {
		PrayerRequestsSchemaPage prayerPage = loadUpPrayerPage();
		prayerPage.enableEditMode();
		UpPrayerRequest formDetails = defaultTestData();
		formDetails.sensitivity = "High";
		formDetails.reviewed = "false";
		formDetails.prayerType = "Celebration";
		validatePrayerRequestCreation(formDetails, true);

	}

	/**
	 * This test case is used to verify that user is able to create the up prayer
	 * request successfully if entered minimal required values and with sensitivity
	 * set to 'LOW' and with reviewed set to 'TRUE',PrayerType-'CELEBRATION'
	 * 
	 */
	@Test

	public void upPrayerRequest_create_success_whenSensitivityLowAndReviewedTrueAndPrayerTypeCelebration() {
		PrayerRequestsSchemaPage prayerPage = loadUpPrayerPage();
		prayerPage.enableEditMode();
		UpPrayerRequest formDetails = defaultTestData();
		formDetails.sensitivity = "Low";
		formDetails.reviewed = "true";
		formDetails.prayerType = "Celebration";
		validatePrayerRequestCreation(formDetails, true);

	}

	/**
	 * This test case is used to verify that user is able to create the up prayer
	 * request successfully if entered minimal required values and with sensitivity
	 * set to 'MEDIUM' and with reviewed set to 'TRUE',PrayerType-'CELEBRATION'
	 * 
	 */
	@Test

	public void upPrayerRequest_create_success_whenSensitivityMediumAndReviewedTrueAndPrayerTypeCelebration() {
		PrayerRequestsSchemaPage prayerPage = loadUpPrayerPage();
		prayerPage.enableEditMode();
		UpPrayerRequest formDetails = defaultTestData();
		formDetails.sensitivity = "Medium";
		formDetails.reviewed = "true";
		formDetails.prayerType = "Celebration";
		validatePrayerRequestCreation(formDetails, true);

	}

	/**
	 * This test case is used to verify that user is able to create the up prayer
	 * request successfully if entered minimal required values and with sensitivity
	 * set to 'HIGH' and with reviewed set to 'TRUE',PrayerType-'CELEBRATION'
	 * 
	 */
	@Test

	public void upPrayerRequest_create_success_whenSensitivityHighAndReviewedTrueAndPrayerTypeCelebration() {
		UpPrayerRequest formDetails = defaultTestData();
		formDetails.sensitivity = "High";
		formDetails.reviewed = "true";
		formDetails.prayerType = "Celebration";
		validatePrayerRequestCreation(formDetails, true);

	}

	/**
	 * This test case is use to validate the prayer request update scenario where
	 * 'PARENT ID' should be Up.PrayerRequest id, sensitivity as 'LOW' and Review
	 * set as 'FALSE'
	 */
	@Test

	private void upPrayerRequest_update_success_whenParentIsUpPrayerRequestId_SensitivityLow_ReviewFalse() {
		UpPrayerRequest formDetails = defaultTestData();
		formDetails.sensitivity = "High";
		formDetails.reviewed = "true";
		formDetails.prayerType = "Request";
		// create
		UpPrayerRequest prayerData = validatePrayerRequestCreation(formDetails, false);

		// create update request
		formDetails.parent = prayerData.prayerId;
		formDetails.sensitivity = "Low";
		formDetails.prayerType = "Update";
		formDetails.reviewed = "false";
		validatePrayerRequestUpdate(formDetails);

	}

	/**
	 * This test case is use to validate the prayer request update scenario where
	 * 'PARENT ID' should be Up.PrayerRequest id, sensitivity as 'LOW' and Review
	 * set as 'TRUE'
	 */
	@Test

	private void upPrayerRequest_update_success_whenParentIsUpPrayerRequestId_SensitivityLow_ReviewTrue() {
		UpPrayerRequest formDetails = defaultTestData();
		formDetails.sensitivity = "High";
		formDetails.reviewed = "false";
		formDetails.prayerType = "Request";
		// create
		UpPrayerRequest prayerData = validatePrayerRequestCreation(formDetails, false);

		// create update request
		formDetails.parent = prayerData.prayerId;
		formDetails.sensitivity = "Low";
		formDetails.prayerType = "Update";
		formDetails.reviewed = "true";
		validatePrayerRequestUpdate(formDetails);
	}

	/**
	 * This test case is use to validate the prayer request update scenario where
	 * 'PARENT ID' should be Up.PrayerRequest id, sensitivity as 'MEDIUM' and Review
	 * set as 'FALSE'
	 */
	@Test

	private void upPrayerRequest_update_success_whenParentIsUpPrayerRequestId_SensitivityMedium_ReviewFalse() {
		UpPrayerRequest formDetails = defaultTestData();
		formDetails.sensitivity = "High";
		formDetails.reviewed = "true";
		formDetails.prayerType = "Request";
		// create
		UpPrayerRequest prayerData = validatePrayerRequestCreation(formDetails, false);

		// create update request
		formDetails.parent = prayerData.prayerId;
		formDetails.sensitivity = "Medium";
		formDetails.prayerType = "Update";
		formDetails.reviewed = "false";
		validatePrayerRequestUpdate(formDetails);
	}

	/**
	 * This test case is use to validate the prayer request update scenario where
	 * 'PARENT ID' should be Up.PrayerRequest id, sensitivity as 'MEDIUM' and Review
	 * set as 'TRUE'
	 */

	@Test

	private void upPrayerRequest_update_success_whenParentIsUpPrayerRequestId_SensitivityMedium_ReviewTrue() {
		UpPrayerRequest formDetails = defaultTestData();
		formDetails.sensitivity = "High";
		formDetails.reviewed = "false";
		formDetails.prayerType = "Request";
		// create
		UpPrayerRequest prayerData = validatePrayerRequestCreation(formDetails, false);

		// create update request
		formDetails.parent = prayerData.prayerId;
		formDetails.sensitivity = "Medium";
		formDetails.prayerType = "Update";
		formDetails.reviewed = "true";
		validatePrayerRequestUpdate(formDetails);
	}

	/**
	 * This test case is use to validate the prayer request update scenario where
	 * 'PARENT ID' should be Up.PrayerRequest id, sensitivity as 'HIGH' and Review
	 * set as 'FALSE'
	 */

	@Test

	private void upPrayerRequest_update_success_whenParentIsUpPrayerRequestId_SensitivityHigh_ReviewFalse() {
		UpPrayerRequest formDetails = defaultTestData();
		formDetails.sensitivity = "Low";
		formDetails.reviewed = "true";
		formDetails.prayerType = "Request";
		// create
		UpPrayerRequest prayerData = validatePrayerRequestCreation(formDetails, true);

		// create update request
		formDetails.parent = prayerData.prayerId;
		formDetails.sensitivity = "High";
		formDetails.prayerType = "Update";
		formDetails.reviewed = "false";
		validatePrayerRequestUpdate(formDetails);
	}

	/**
	 * This test case is use to validate the prayer request update scenario where
	 * 'PARENT ID' should be Up.PrayerRequest id, sensitivity as 'HIGH' and Review
	 * set as 'TRUE'
	 */
	@Test

	private void upPrayerRequest_update_success_whenParentIsUpPrayerRequestId_SensitivityHigh_ReviewTrue() {
		UpPrayerRequest formDetails = defaultTestData();
		formDetails.sensitivity = "Medium";
		formDetails.reviewed = "false";
		formDetails.prayerType = "Request";
		// create
		System.out.println("Executing the test case for request: \n" + formDetails);
		UpPrayerRequest prayerData = validatePrayerRequestCreation(formDetails, false);

		// create update request
		formDetails.parent = prayerData.prayerId;
		formDetails.sensitivity = "High";
		formDetails.prayerType = "Update";
		formDetails.reviewed = "true";
		validatePrayerRequestUpdate(formDetails);
	}

	/**
	 * Method to validate the prayer request creation scenario.
	 * 
	 * @param formDetails
	 */
	private UpPrayerRequest validatePrayerRequestCreation(UpPrayerRequest formDetails, boolean delete) {
		PrayerRequestsSchemaPage prayerPage = loadUpPrayerPage();
		SeleniumUtils.wait(1);
		prayerPage.enableEditMode();
		List<String> prayerIds = prayerPage.getExistingRequests();
		prayerPage.fillUpPrayerRequestForm(formDetails);
		SeleniumUtils.wait(1);
		prayerPage.loadApp();
		List<String> newPrayerId = isRequestCreated(prayerIds, prayerPage);
		assertNotNull(newPrayerId);
		UpPrayerRequest prayerData = getRequestData(prayerPage.findPrayerRequest(newPrayerId.get(0)));

		assertNotNull(prayerData);
		prayerPage.loadApp();
		if (delete) {
			prayerPage.deleteRecord(newPrayerId.get(0));
		}
		return prayerData;

	}

	/**
	 * Method to validate the prayer request Update scenario.
	 * 
	 * @param formDetails
	 */
	private UpPrayerRequest validatePrayerRequestUpdate(UpPrayerRequest formDetails) {
		PrayerRequestsSchemaPage prayerPage = loadUpPrayerPage();
		prayerPage.enableEditMode();
		List<String> prayerIds = prayerPage.getExistingRequests();
		prayerPage.fillUpPrayerRequestForm(formDetails);
		SeleniumUtils.wait(1);
		prayerPage.loadApp();
		List<String> newPrayerId = isRequestCreated(prayerIds, prayerPage);
		assertNotNull(newPrayerId);

		UpPrayerRequest updatedData = getRequestData(prayerPage.findPrayerRequest(newPrayerId.get(0)));
		assertNotNull(updatedData);
		// assertEquals(updatedData.parent, formDetails.parent);
		prayerPage.deleteRecord(updatedData.parent);
		prayerPage.deleteRecord(updatedData.prayerId);
		return formDetails;

	}

	private List<String> isRequestCreated(List<String> oldPrayerIds, PrayerRequestsSchemaPage prayerPage) {

		List<String> newPrayerIds = prayerPage.getExistingRequests();
		assertNotNull(newPrayerIds);
		newPrayerIds.removeAll(oldPrayerIds);
		assertNotNull(newPrayerIds);
		assertTrue(newPrayerIds.size() == 1);

		return newPrayerIds;
	}

	private UpPrayerRequest getRequestData(List<String> data) {
		UpPrayerRequest requestData = new UpPrayerRequest();
		// assertEquals(data.size(), 12);
		requestData.prayerId = data.get(0);
		requestData.requestLanguageId = data.get(1);
		requestData.targetLanguageId = data.get(2);
		requestData.sensitivity = data.get(3);
		requestData.organizationName = data.get(4);
		requestData.parent = data.get(5);
		requestData.translator = data.get(6);
		requestData.location = data.get(7);
		requestData.title = data.get(8);
		requestData.content = data.get(9);
		requestData.reviewed = data.get(10);
		requestData.prayerType = data.get(11);
		return requestData;
	}

	private UpPrayerRequest defaultTestData() {
		UpPrayerRequest formDetails = new UpPrayerRequest();
		formDetails.requestLanguageId = "-09jindI0y7";
		formDetails.targetLanguageId = "-09jindI0y7";
		formDetails.sensitivity = "Medium";
		formDetails.organizationName = "Seed company";
		formDetails.parent = "";
		formDetails.translator = "Aa_JAxJUIr3";
		formDetails.location = "USA";
		formDetails.title = "SEED";
		formDetails.content = "Up prayer request details";
		formDetails.reviewed = "false";
		formDetails.prayerType = "Request";
		return formDetails;
	}
}
