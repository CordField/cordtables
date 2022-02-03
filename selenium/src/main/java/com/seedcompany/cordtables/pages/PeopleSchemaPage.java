package com.seedcompany.cordtables.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import com.seedcompany.cordtables.model.People;
import com.seedcompany.cordtables.model.UpPrayerRequest;
import com.seedcompany.cordtables.utils.SeleniumUtils;

public class PeopleSchemaPage extends Page {
	
	public PeopleSchemaPage(WebDriver driver) {
		super(driver);
	}

	/**
	 * this method is used for clicking the edit mode button mode as true
	 */
	
	

	public void enableEditMode() {

		WebElement editModeOption = this.rootApp.findElement(By.cssSelector("#container > div.edit-button > div > ion-toggle"));
		
		
		boolean state = editModeOption.getAttribute("class").contains("toggle-checked");

		System.out.println("Is Page is editable ? " + (state ? "Yes" : "No"));
		if (!state) {
			editModeOption.click();
		}
		editModeOption = this.rootApp
				.findElement(By.cssSelector("#container > div.edit-button > div > ion-toggle"));
		System.out.println("Enabled the edit mode ? " + editModeOption.getAttribute("class"));
	}

	
	/**
	 * this method is used for clicking the edit mode button mode as true
	 */

	public void disableEditMode() {

		WebElement editModeOption = this.rootApp.findElement(By.cssSelector("#container > div.edit-button > div > ion-toggle"));
		SearchContext context = SeleniumUtils.expand_shadow_element(editModeOption);
		WebElement state = context.findElement(By.cssSelector("#ion-tg-0"));
		if (state.isSelected()) {
			SeleniumUtils.clickElement(state, driver);
		}
	}

	/**
	 * this method is used to fill the People request form
	 * 
	 */
	

	public WebElement fillPeopleRequestForm(People formDetails) {

		SeleniumUtils.scrollDown(driver);
		SeleniumUtils.wait(2);
		this.loadApp();
		System.out.println("Load the Admin people page");
		SearchContext tableRoot = SeleniumUtils
				.expand_shadow_element(this.rootApp.findElement(By.cssSelector("table-root.hydrated")));
		System.out.println("table root find::"+tableRoot);
		SearchContext fillingform = SeleniumUtils.expand_shadow_element(tableRoot
				.findElement(By.cssSelector("stencil-router > stencil-route:nth-child(9) > div")));
		System.out.println("find the tabble page");
		WebElement form = fillingform.findElement(By.cssSelector("form.form-thing"));
System.out.println("find thr page form");
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
		this.selectSensitivity(form,formDetails.sensitivityclearance);
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
		WebElement sensitivitySelector = form.findElement(By.cssSelector("#sensitivity"));
		Select s = new Select(sensitivitySelector);
		s.selectByValue(value);
		System.out.println(s.getFirstSelectedOption().getText());
	}

	
}
