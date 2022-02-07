package com.seedcompany.cordtables.pages;

import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.seedcompany.cordtables.model.TablesOption;
import com.seedcompany.cordtables.model.UpPrayerRequest;
import com.seedcompany.cordtables.utils.SeleniumUtils;
import com.seedcompany.cordtables.utils.TableDataUtils;

public class PrayerRequestsSchemaPage extends Page {

	private static Logger logger = LoggerFactory.getLogger(PrayerRequestsSchemaPage.class);
	TableDataUtils extractor = null;

	public PrayerRequestsSchemaPage(WebDriver driver) {
		super(driver);
	}

	/**
	 * this method is used to fill the UpPrayer request form
	 * 
	 */

	public WebElement fillUpPrayerRequestForm(UpPrayerRequest formDetails) {

		SeleniumUtils.scrollDown(driver);
		SeleniumUtils.wait(2);
		this.loadApp();
		
		SearchContext tableRoot = SeleniumUtils
				.expand_shadow_element(this.rootApp.findElement(By.cssSelector("table-root.hydrated")));
		SearchContext fillingform = SeleniumUtils
				.expand_shadow_element(tableRoot.findElement(By.cssSelector("up-prayer-requests")));
		WebElement form = fillingform.findElement(By.cssSelector("form.form-thing"));

		form.findElement(By.cssSelector("#request_language_id")).sendKeys(formDetails.requestLanguageId);
		form.findElement(By.cssSelector("#target_language_id")).sendKeys(formDetails.targetLanguageId);
		this.selectSensitivity(form, formDetails.sensitivity);

		form.findElement(By.cssSelector("#organization_name")).sendKeys(formDetails.organizationName);
		form.findElement(By.cssSelector("#parent")).sendKeys(formDetails.parent);
		form.findElement(By.cssSelector("#translator")).sendKeys(formDetails.translator);
		form.findElement(By.cssSelector("#location")).sendKeys(formDetails.location);
		form.findElement(By.cssSelector("#title")).sendKeys(formDetails.title);
		form.findElement(By.cssSelector("#content")).sendKeys(formDetails.content);
		this.selectPrayerType(form, formDetails.prayerType);
		this.selectReviewed(form, formDetails.reviewed);
		WebElement createBtn = form.findElement(By.cssSelector("#create-button"));
		SeleniumUtils.scrollToElement(createBtn, driver);
		createBtn.click();
		return null;

	}

	/**
	 * This Method is used to select the sensitivity(Low/Medium/High) from drop
	 * down.
	 */
	public void selectSensitivity(WebElement form, String value) {
		WebElement sensitivitySelector = form.findElement(By.cssSelector("#sensitivity"));
		Select s = new Select(sensitivitySelector);
		s.selectByValue(value);
	}

	/**
	 * This Method is used to select the reviewed(True/False) option from drop down.
	 */

	public void selectReviewed(WebElement form, String value) {
		WebElement reviewed = form.findElement(By.cssSelector("#reviewed"));
		Select s = new Select(reviewed);
		s.selectByValue(value);
	}

	/**
	 * This Method is used to select the prayer type(Request/Update/Celebration)
	 * from drop down.
	 */

	public void selectPrayerType(WebElement form, String prayerType) {
		WebElement prayerTypeEle = form.findElement(By.cssSelector("#prayer_type"));
		Select s = new Select(prayerTypeEle);
		s.selectByValue(prayerType);
	}

	/**
	 * This method is use to delete the row from up prayer request form table.
	 */
	public void deleteRecord(String prayerId) {

		try {
			this.menuUtils.enableEditMode();
			this.extractor = new TableDataUtils(this.rootApp.findElement(By.cssSelector("#root-wrap-inner > main")),
					this.driver);
			List<List<WebElement>> records = extractor.readTable(TablesOption.UP_PRAYER_REQUESTS);
			records.forEach(r -> {
				WebElement idCol = r.get(0);
				if (prayerId.equalsIgnoreCase(this.extractor.columnData(idCol))) {

					SearchContext dataCol = SeleniumUtils.expand_shadow_element(idCol);
					dataCol.findElement(By.className("delete-span")).click();
					dataCol.findElement(By.className("save-icon")).click();

				}

			});
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public List<String> findPrayerRequest(String prayerId) {
		List<String> data = null;
		try {
			this.menuUtils.enableEditMode();
			this.extractor = new TableDataUtils(this.rootApp.findElement(By.cssSelector("#root-wrap-inner > main")),
					this.driver);
			List<List<WebElement>> records = extractor.readTable(TablesOption.UP_PRAYER_REQUESTS);
			for (List<WebElement> r : records) {
				WebElement idCol = r.get(0);
				if (prayerId.equalsIgnoreCase(this.extractor.columnData(idCol))) {

					data = r.stream().map(c -> this.extractor.columnData(c)).collect(Collectors.toList());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}

	public List<String> getExistingRequests() {
		WebElement tableContainer = this.rootApp.findElement(By.cssSelector("#root-wrap-inner > main"));
		this.extractor = new TableDataUtils(tableContainer, this.driver);
		SeleniumUtils.scrollToElement(tableContainer, driver);
		List<List<String>> records = extractor.extractData(TablesOption.UP_PRAYER_REQUESTS);
		return records.stream().map(r -> {
			return r.get(0);
		}).collect(Collectors.toList());
	}

}
