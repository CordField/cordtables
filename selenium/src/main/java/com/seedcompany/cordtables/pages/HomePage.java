package com.seedcompany.cordtables.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.seedcompany.cordtables.utils.SeleniumUtils;

/**
 * Represents the home page.
 */
public class HomePage extends Page {

	@FindBy(how = How.TAG_NAME, using = "h1")
	@CacheLookup
	public WebElement header;

	@FindBy(how = How.XPATH, using = "//*[@id=\"menu-button\"]")
	@CacheLookup
	public WebElement menu;

	@FindBy(how = How.XPATH, using = "//*[@id=\"header-menu\"]/button[2]")
	@CacheLookup
	public WebElement loginOption;

	@FindBy(how = How.XPATH, using = "//*[@id=\"header-menu\"]/button[3]")
	@CacheLookup
	public WebElement registrationOption;

	public HomePage(WebDriver webDriver) {
		super(webDriver);
	}

	/**
	 * 
	 */
	public void login() {
		this.loginOption.click();
	}

	/**
	 * @param menu
	 */
	public void register(WebElement menu) {

		WebElement menuHeader = SeleniumUtils.expand_shadow_element(driver,
				menu.findElement(By.tagName("cf-header-menu")));
		menuHeader.findElement(By.cssSelector("button.menu-item:nth-child(3)")).click();
	}

	/**
	 * Method to load the app.
	 * @return
	 */
	public WebElement loadApp() {
		return SeleniumUtils.expand_shadow_element(driver, driver.findElement(By.tagName("app-root")));
	}

	/**
	 * Method to open the menu on home page.
	 * 
	 * @return
	 */
	public WebElement open_menu() {
		WebElement appRoot = loadApp();
		WebElement appHeader = SeleniumUtils.expand_shadow_element(driver,
				appRoot.findElement(By.tagName("cf-header")));
		WebElement ionIcon = SeleniumUtils.expand_shadow_element(driver, appHeader.findElement(By.tagName("ion-icon")));
		WebElement menu = ionIcon.findElement(By.cssSelector(".ionicon"));
		menu.click();
		return appHeader;
	}

}
