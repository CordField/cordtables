package com.seedcompany.cordtables.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.seedcompany.cordtables.utils.SeleniumUtils;

/**
 * Abstract class representation of a Page in the UI. Page object pattern
 */
public abstract class Page {

	protected WebDriver driver;

	public WebElement rootApp;

	public WebElement getRootApp() {
		return rootApp;
	}

	public void setRootApp(WebElement rootApp) {
		this.rootApp = rootApp;
	}

	/*
	 * Constructor injecting the WebDriver interface
	 * 
	 * @param webDriver
	 */
	public Page(WebDriver driver) {
		this.driver = driver;
	}

	public String getTitle() {
		return driver.getTitle();
	}

	/**
	 * Method to load the app.
	 * 
	 * @return
	 */
	public WebElement loadApp() {
		this.rootApp = SeleniumUtils.expand_shadow_element(driver, driver.findElement(By.tagName("app-root")));
		return this.rootApp;
	}

	/**
	 * Method to open the menu on home page.
	 * 
	 * @return
	 */
	public WebElement openMenu() {
		WebElement appRoot = loadApp();
		WebElement appHeader = SeleniumUtils.expand_shadow_element(driver,
				appRoot.findElement(By.tagName("cf-header")));
		WebElement ionIcon = SeleniumUtils.expand_shadow_element(driver, appHeader.findElement(By.tagName("ion-icon")));
		WebElement menu = ionIcon.findElement(By.cssSelector(".ionicon"));
		menu.click();
		return appHeader;
	}

	/**
	 * 
	 */
	public void logout() {
		try {

			WebElement menuHeader = SeleniumUtils.expand_shadow_element(driver,
					openMenu().findElement(By.tagName("cf-header-menu")));
			menuHeader.findElement(By.cssSelector("button.menu-item:nth-child(4)")).click();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

}
