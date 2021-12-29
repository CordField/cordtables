package com.seedcompany.cordtables.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

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

}
