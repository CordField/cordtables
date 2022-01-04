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
	public void login(WebElement menu) {
		WebElement menuHeader = SeleniumUtils.expand_shadow_element(driver,
				menu.findElement(By.tagName("cf-header-menu")));
		menuHeader.findElement(By.cssSelector("button.menu-item:nth-child(2)")).click();
	}

	/**
	 * @param menu
	 */
	public void register(WebElement menu) {

		WebElement menuHeader = SeleniumUtils.expand_shadow_element(driver,
				menu.findElement(By.tagName("cf-header-menu")));
		menuHeader.findElement(By.cssSelector("button.menu-item:nth-child(3)")).click();
	}

}
