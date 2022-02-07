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

import com.seedcompany.cordtables.model.People;
import com.seedcompany.cordtables.model.TablesOption;
import com.seedcompany.cordtables.utils.SeleniumUtils;
import com.seedcompany.cordtables.utils.TableDataUtils;

public class PeopleSchemaPage extends Page {

	private static Logger logger = LoggerFactory.getLogger(PeopleSchemaPage.class);

	public PeopleSchemaPage(WebDriver driver) {
		super(driver);
	}

	TableDataUtils extractor = null;

	/**
	 * this method is used to fill the People request form
	 * 
	 */
	public WebElement fillPeopleRequestForm(People formDetails) {

		SeleniumUtils.scrollDown(driver);
		SeleniumUtils.wait(2);
		this.loadApp();
		logger.debug("Load the Admin people page");
		SearchContext tableRoot = SeleniumUtils
				.expand_shadow_element(this.rootApp.findElement(By.cssSelector("table-root.hydrated")));
		SearchContext fillingform = SeleniumUtils
				.expand_shadow_element(tableRoot.findElement(By.cssSelector("admin-people")));
		WebElement form = fillingform.findElement(By.cssSelector("form.form-thing"));
		logger.debug("find thr page form");
		form.findElement(By.cssSelector("#about")).sendKeys(formDetails.about);
		form.findElement(By.cssSelector("#phone")).sendKeys(formDetails.phonenumber);
		form.findElement(By.cssSelector("#picture")).sendKeys(formDetails.picture);
		form.findElement(By.cssSelector("#private_first_name")).sendKeys(formDetails.privatefirstname);
		form.findElement(By.cssSelector("#private_last_name")).sendKeys(formDetails.privatelastname);
		form.findElement(By.cssSelector("#public_first_name")).sendKeys(formDetails.publicfirstname);
		form.findElement(By.cssSelector("#public_last_name")).sendKeys(formDetails.publiclastname);
		form.findElement(By.cssSelector("#primary_location")).sendKeys(formDetails.primarylocation);
		form.findElement(By.cssSelector("#private_full_name")).sendKeys(formDetails.privatefullname);
		form.findElement(By.cssSelector("#public_full_name")).sendKeys(formDetails.publicfullname);
		this.selectSensitivity(form, formDetails.sensitivityclearance);
		form.findElement(By.cssSelector("#timezone")).sendKeys(formDetails.timezone);
		form.findElement(By.cssSelector("#title")).sendKeys(formDetails.title);

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
		WebElement sensitivitySelector = form.findElement(By.cssSelector("#sensitivity_clearance"));
		Select s = new Select(sensitivitySelector);
		s.selectByValue(value);
		logger.debug("Selected value = {}", s.getFirstSelectedOption().getText());
	}

	/**
	 * This method is use to delete the row from up prayer request form table.
	 */
	public void deleteRecord(String peopleId) {

		try {
			this.menuUtils.enableEditMode();
			this.extractor = new TableDataUtils(this.rootApp.findElement(By.cssSelector("#root-wrap-inner > main")),
					this.driver);
			List<List<WebElement>> records = extractor.readTable(TablesOption.ADMIN_PEOPLE);
			records.forEach(r -> {
				WebElement idCol = r.get(0);
				if (peopleId.equalsIgnoreCase(this.extractor.columnData(idCol))) {

					SearchContext dataCol = SeleniumUtils.expand_shadow_element(idCol);
					dataCol.findElement(By.className("delete-span")).click();
					dataCol.findElement(By.className("save-icon")).click();

				}

			});
		} catch (Exception e) {
			logger.error("failed due to ", e);
		}

	}

	/**
	 * @param peopleId
	 * @return
	 */
	public List<String> findPeople(String peopleId) {
		List<String> data = null;
		try {
			this.menuUtils.enableEditMode();
			this.extractor = new TableDataUtils(this.rootApp.findElement(By.cssSelector("#root-wrap-inner > main")),
					this.driver);
			List<List<WebElement>> records = extractor.readTable(TablesOption.ADMIN_PEOPLE);
			for (List<WebElement> r : records) {
				WebElement idCol = r.get(0);
				if (peopleId.equalsIgnoreCase(this.extractor.columnData(idCol))) {

					data = r.stream().map(c -> this.extractor.columnData(c)).collect(Collectors.toList());
				}
			}
		} catch (Exception e) {
			logger.error("failed due to ", e);
		}
		return data;
	}

	/**
	 * @return
	 */
	public List<String> getExistingPeople() {
		WebElement tableContainer = this.rootApp.findElement(By.cssSelector("#root-wrap-inner > main"));
		this.extractor = new TableDataUtils(tableContainer, this.driver);
		SeleniumUtils.scrollToElement(tableContainer, driver);
		List<List<String>> records = extractor.extractData(TablesOption.ADMIN_PEOPLE);
		return records.stream().map(r -> {
			return r.get(0);
		}).collect(Collectors.toList());
	}

}
