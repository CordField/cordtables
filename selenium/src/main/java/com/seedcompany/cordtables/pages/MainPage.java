package com.seedcompany.cordtables.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.seedcompany.cordtables.utils.SeleniumUtils;

/**
 * Represent the mainpage after login
 * 
 * @author swati
 *
 */
public class MainPage extends Page {

	private WebElement mainMenu;

	public MainPage(WebDriver webDriver) {
		super(webDriver);
	}

	public boolean isPageLoaded() {
		WebElement mainMenu = SeleniumUtils.expand_shadow_element(driver,
				this.rootApp.findElement(By.tagName("custom-accordion")));
		return mainMenu != null;
	}

	public void expandMenuOptions() {
		WebElement mainMenu = SeleniumUtils.expand_shadow_element(driver,
				this.rootApp.findElement(By.tagName("custom-accordion")));
		this.mainMenu = mainMenu;
		mainMenu.findElement(By.cssSelector(".accordion")).click();

	}

	public void expandPagesMenu() {
		WebElement mainMenu = this.rootApp.findElement(By.tagName("custom-accordion"));
		WebElement tablesMenu = SeleniumUtils.expand_shadow_element(driver,
				mainMenu.findElements(By.tagName("custom-accordion")).get(0));
		tablesMenu.findElement(By.cssSelector(".accordion")).click();

	}

	public void expandTablesMenu() {
		WebElement mainMenu = this.rootApp.findElement(By.tagName("custom-accordion"));
		WebElement tablesMenu = SeleniumUtils.expand_shadow_element(driver,
				mainMenu.findElements(By.tagName("custom-accordion")).get(1));
		tablesMenu.findElement(By.cssSelector(".accordion")).click();

	}

}