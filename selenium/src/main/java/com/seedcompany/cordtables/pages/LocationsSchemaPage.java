package com.seedcompany.cordtables.pages;

import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import com.seedcompany.cordtables.model.LocationInfo;
import com.seedcompany.cordtables.model.TablesOption;
import com.seedcompany.cordtables.utils.SeleniumUtils;
import com.seedcompany.cordtables.utils.TableDataExtractor;

public class LocationsSchemaPage extends Page {

	public LocationsSchemaPage(WebDriver driver) {
		super(driver);
		// TODO Auto-generated constructor stub
	}

	TableDataExtractor extractor = null;

	/**
	 * this method is used to fill the Common location page
	 * 
	 */

	public WebElement fillCommonLocationForm(LocationInfo formDetails) {
		SeleniumUtils.scrollDown(driver);
		SeleniumUtils.wait(2);
		this.loadApp();
		SearchContext tableRoot = SeleniumUtils
				.expand_shadow_element(this.rootApp.findElement(By.cssSelector("table-root.hydrated")));
		SearchContext fillingform = SeleniumUtils.expand_shadow_element(tableRoot
				.findElement(By.cssSelector("common-locations")));
		WebElement form = fillingform.findElement(By.cssSelector("form.form-thing"));

		form.findElement(By.cssSelector("#name")).sendKeys(formDetails.name);
		this.selectSensitivity(form, formDetails.sensitivity);
		this.selectType(form, formDetails.type);
		form.findElement(By.cssSelector("#iso_alpha3")).sendKeys(formDetails.isoAlpha3);
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
		System.out.println(s.getFirstSelectedOption().getText());
	}

	/**
	 * This Method is used to select the
	 * Type(City/County/State/Country/CrossBorderArea) from drop down.
	 */
	public void selectType(WebElement form, String value) {
		WebElement typeSelector = form.findElement(By.cssSelector("#type"));
		Select s = new Select(typeSelector);
		s.selectByValue(value);
		System.out.println(s.getFirstSelectedOption().getText());
	}

	/**
	 * This method is use to delete the row from up prayer request form table.
	 */
	public void deleteRecord(String locationId) {

		try {
			this.menuUtils.enableEditMode();
			this.extractor = new TableDataExtractor(this.rootApp.findElement(By.cssSelector("#root-wrap-inner > main")),
					this.driver);
			List<List<WebElement>> records = extractor.readTable(TablesOption.COMMON_LOCATIONS);
			records.forEach(r -> {
				WebElement idCol = r.get(0);
				if (locationId.equalsIgnoreCase(this.extractor.columnData(idCol))) {

					SearchContext dataCol = SeleniumUtils.expand_shadow_element(idCol);
					dataCol.findElement(By.className("delete-span")).click();
					dataCol.findElement(By.className("save-icon")).click();

				}

			});
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public List<String> findLocationInfo(String locationId) {
		List<String> data = null;
		try {
			this.menuUtils.enableEditMode();
			this.extractor = new TableDataExtractor(this.rootApp.findElement(By.cssSelector("#root-wrap-inner > main")),
					this.driver);
			List<List<WebElement>> records = extractor.readTable(TablesOption.COMMON_LOCATIONS);
			for (List<WebElement> r : records) {
				WebElement idCol = r.get(0);
				if (locationId.equalsIgnoreCase(this.extractor.columnData(idCol))) {

					data = r.stream().map(c -> this.extractor.columnData(c)).collect(Collectors.toList());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}

	public List<String> getExistingLocations() {
		WebElement tableContainer = this.rootApp.findElement(By.cssSelector("#root-wrap-inner > main"));
		this.extractor = new TableDataExtractor(tableContainer, this.driver);
		SeleniumUtils.scrollToElement(tableContainer, driver);
		List<List<String>> records = extractor.extractData(TablesOption.COMMON_LOCATIONS);
		return records.stream().map(r -> {
			return r.get(0);
		}).collect(Collectors.toList());
	}
}
