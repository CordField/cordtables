package com.seedcompany.cordtables.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import com.seedcompany.cordtables.model.LocationInfo;
import com.seedcompany.cordtables.utils.SeleniumUtils;
import com.seedcompany.cordtables.utils.TableDataExtractor;

public class LocationsSchemaPage extends Page {

	public LocationsSchemaPage(WebDriver driver) {
		super(driver);
		// TODO Auto-generated constructor stub
	}

	TableDataExtractor extractor = null;

	/**
	 * this method is used for clicking the edit mode button mode as true
	 */

	public void enableEditMode() {

		WebElement editModeOption = this.rootApp
				.findElement(By.cssSelector("#container > div.edit-button > div > ion-toggle"));
		boolean state = editModeOption.getAttribute("class").contains("toggle-checked");

		System.out.println("Is Page is editable ? " + (state ? "Yes" : "No"));
		if (!state) {
			editModeOption.click();
		}
		editModeOption = this.rootApp.findElement(By.cssSelector("#container > div.edit-button > div > ion-toggle"));
		System.out.println("Enabled the edit mode ? " + editModeOption.getAttribute("class"));
	}

	/**
	 * this method is used for clicking the edit mode button mode as true
	 */

	public void disableEditMode() {

		WebElement editModeOption = this.rootApp
				.findElement(By.cssSelector("#container > div.edit-button > div > ion-toggle"));
		SearchContext context = SeleniumUtils.expand_shadow_element(editModeOption);
		WebElement state = context.findElement(By.cssSelector("#ion-tg-0"));
		if (state.isSelected()) {
			SeleniumUtils.clickElement(state, driver);
		}
	}

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
				.findElement(By.cssSelector("stencil-router > stencil-route:nth-child(55) > common-locations")));
		WebElement form = fillingform.findElement(By.cssSelector("form.form-thing"));

		form.findElement(By.cssSelector("#name-holder > span:nth-child(2)")).sendKeys(formDetails.name);
		this.selectSensitivity(form, formDetails.sensitivity);
		this.selectType(form, formDetails.type);
		form.findElement(By.cssSelector("#iso_alpha3-holder > span:nth-child(2)")).sendKeys(formDetails.isoAlpha3);
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
		WebElement sensitivitySelector = form.findElement(By.cssSelector("#type"));
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
}
