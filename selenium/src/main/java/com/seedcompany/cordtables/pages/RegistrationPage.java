package com.seedcompany.cordtables.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.seedcompany.cordtables.utils.SeleniumUtils;

/**
 * Represents the registration page.
 * 
 * @author swati
 *
 */
public class RegistrationPage extends Page {

	@FindBy(how = How.XPATH, using = "//*[@id=\"root-wrap-inner\"]/main/stencil-router/stencil-route-switch/stencil-route[3]/cf-register")
	@CacheLookup
	public WebElement pageContainer;

	@FindBy(how = How.XPATH, using = "//*[@id=\"email\"]")
	@CacheLookup
	public WebElement emailAddressInput;

	@FindBy(how = How.XPATH, using = "//*[@id=\"password\"]")
	@CacheLookup
	public WebElement passwordInput;

	@FindBy(how = How.XPATH, using = "//*[@id=\"register-button\"]")
	@CacheLookup
	public WebElement registerationBtn;

	public RegistrationPage(WebDriver webDriver) {
		super(webDriver);
	}

	/**
	 * Check if the registration page is loaded.
	 * 
	 * @return
	 */
	public boolean isRegistrationPageLoaded() {
		WebElement registrationApp = SeleniumUtils.expand_shadow_element(driver,
				this.rootApp.findElement(By.tagName("cf-register")));
		WebElement registrationForm = registrationApp.findElement(By.cssSelector("form"));
		return (registrationForm != null);
	}

	/**
	 * This method is used to fill the registration form.
	 * 
	 * @param email
	 * @param password
	 */
	public void fillRegistrationForm(String email, String password) {
		WebElement registrationApp = SeleniumUtils.expand_shadow_element(driver,
				this.rootApp.findElement(By.tagName("cf-register")));
		WebElement registrationForm = registrationApp.findElement(By.cssSelector("form"));
		registrationForm.findElement(By.cssSelector("#email")).sendKeys(email);
		registrationForm.findElement(By.cssSelector("#password")).sendKeys(password);
		registrationForm.findElement(By.cssSelector("#register-button")).click();
	}

}