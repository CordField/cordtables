package com.seedcompany.cordtables.qaautomation;

import static org.testng.Assert.assertNotNull;

import java.util.List;
import java.util.logging.Logger;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.seedcompany.cordtables.model.People;
import com.seedcompany.cordtables.model.TablesOption;
import com.seedcompany.cordtables.pages.HomePage;
import com.seedcompany.cordtables.pages.LoginPage;
import com.seedcompany.cordtables.pages.MainPage;
import com.seedcompany.cordtables.pages.PeopleSchemaPage;
import com.seedcompany.cordtables.utils.SeleniumUtils;

/**
 * 
 * This test suite covers all the test cases associated with the Admn people form
 * page.
 * 
 * @author swati
 *
 */
public class AdminPeoplePageTest extends BaseTestSuite {
	 	
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
			System.out.println("Selecting option as" + TablesOption.ADMIN_PEOPLE);
			mainPage.selectSchema(TablesOption.ADMIN_PEOPLE);
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

		/**
		 * This test case is used to verify that user is able to fill the admin.people
		 * form  successfully if entered minimal required values.
		 * 
		 */
		@Test

		public void adminPeople_fill_form_success_default() {
			PeopleSchemaPage peoplePage = loadAdminPeoplePage();
			System.out.println("page is loaded");
			peoplePage.enableEditMode();
			SeleniumUtils.wait(2);
			People formDetails = defaultTestData();
			formDetails.sensitivityclearance = "Low";
		//	validatePrayerRequestCreation(formDetails, true);
			
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
			//CommonLocationsPage location;
		//	formDetails.primarylocation = location.;
			formDetails.privatefullname = "david john gob";
			formDetails.publicfullname = "favid gob";
			formDetails.sensitivityclearance = "Low";
			formDetails.timezone = "cst";
			formDetails.title = "Mr";
			return formDetails;
//		}
//		/**
//		 * Method to validate the Admin people creation scenario.
//		 * 
//		 * @param formDetails
//		 */
//		private AdminPeopleForm validateAdminPeopleCreatin(AdminPeopleForm formDetails, boolean delete) {
//			AdminPeoplePage peoplePage = loadAdminPeoplePage();
//			SeleniumUtils.wait(2);
//			peoplePage.enableEditMode();
//			List<String> prayerIds = peoplePage.getExistingRequests();
//			peoplePage.fillPeopleRequestForm(formDetails);
//			SeleniumUtils.wait(1);
//			peoplePage.loadApp();
//			List<String> newPrayerId = isRequestCreated(prayerIds, peoplePage);
//			assertNotNull(newPrayerId);
//			UpPrayerRequestForm peopleData = getRequestData(peoplePage.findPrayerRequest(newPrayerId.get(0)));
//
//			assertNotNull(peopleData);
//			peoplePage.loadApp();
//			if (delete) {
//				peoplePage.deleteRecord(newPrayerId.get(0));
//			}
//			return peopleData;

		}
}
