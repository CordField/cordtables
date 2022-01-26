package com.seedcompany.cordtables.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.seedcompany.cordtables.utils.SeleniumUtils;

/**
 * Abstract class representation of a Page in the UI. Page object pattern
 */
public abstract class Page {

	protected WebDriver driver;

	protected SearchContext rootApp;

	public SearchContext getRootApp() {
		return rootApp;
	}

	public void setRootApp(SearchContext rootApp) {
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
	public SearchContext loadApp() {
		this.setRootApp(SeleniumUtils.expand_shadow_element(driver.findElement(By.tagName("app-root"))));
		return this.rootApp;
	}

	/**
	 * Method to open the menu on home page.
	 * 
	 * @return
	 */
	public SearchContext openMenu() {
		SeleniumUtils.wait(2);
		SearchContext appRoot = loadApp();
		SeleniumUtils.wait(1);
		SearchContext appHeader = SeleniumUtils
				.expand_shadow_element(appRoot.findElement(By.cssSelector("cf-header.hydrated")));
		SearchContext ionIcon = SeleniumUtils
				.expand_shadow_element(appHeader.findElement(By.cssSelector("ion-icon.md")));
		WebElement menu = ionIcon.findElement(By.cssSelector(".ionicon"));
		menu.click();
		SeleniumUtils.wait(1);
		return appHeader;
	}

	/**
	 * Method for logout
	 */
	public void logout() {
		try {

			SearchContext menuHeader = SeleniumUtils
					.expand_shadow_element(openMenu().findElement(By.cssSelector("cf-header-menu.hydrated")));
			menuHeader.findElement(By.cssSelector("button.menu-item:nth-child(4)")).click();
			SeleniumUtils.wait(1);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

}
