package com.seedcompany.cordtables.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.seedcompany.cordtables.utils.SeleniumUtils;

/**
 * Represents the home page.
 */
public class HomePage extends Page {

	private static Logger logger = LoggerFactory.getLogger(HomePage.class);

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
	 * Method to go into the login page
	 */
	public void login(SearchContext menu) {
		SearchContext menuHeader = SeleniumUtils
				.expand_shadow_element(menu.findElement(By.cssSelector("cf-header-menu.hydrated")));
		menuHeader.findElement(By.cssSelector("button.menu-item:nth-child(2)")).click();
	}

	/**
	 * @param menu method to click on the the register button
	 */
	public void register(SearchContext menu) {

		SearchContext menuHeader = SeleniumUtils
				.expand_shadow_element(menu.findElement(By.cssSelector("cf-header-menu.hydrated")));
		menuHeader.findElement(By.cssSelector("button.menu-item:nth-child(3)")).click();
	}

}
