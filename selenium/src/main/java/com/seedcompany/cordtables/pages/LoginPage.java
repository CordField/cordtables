package com.seedcompany.cordtables.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.seedcompany.cordtables.utils.SeleniumUtils;

/**
 * Represent the login page
 * 
 * @author swati
 */

public class LoginPage extends Page {

	public LoginPage(WebDriver driver) {
		super(driver);
	}

	/**
	 * 
	 *
	 * @return
	 */
	public boolean isLoginPageloaded() {
		SearchContext loginapp = SeleniumUtils.expand_shadow_element(
				this.rootApp.findElement(By.cssSelector("cf-login.hydrated")));
		WebElement loginForm = loginapp.findElement(By.cssSelector("form:nth-child(4)"));
		return (loginForm != null);
	}

	/**
	 * This method is used to fill the login detail
	 * 
	 * @param email
	 * @param password
	 */

	public WebElement fillLoginDetails(String email, String password) {
		SearchContext loginApp = SeleniumUtils.expand_shadow_element(
				this.rootApp.findElement(By.cssSelector("cf-login.hydrated")));
		WebElement loginForm = loginApp.findElement(By.cssSelector("form"));
		loginForm.findElement(By.cssSelector("#email")).sendKeys(email);
		loginForm.findElement(By.cssSelector("#password")).sendKeys(password);
		return (loginForm);
	}

	/**
	 * function to click on the login button
	 * 
	 * @param login button click
	 */
	public void submit(WebElement loginForm) {
		loginForm.findElement(By.cssSelector("#Login-button")).click();

	}

}
