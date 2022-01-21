package com.seedcompany.cordtables.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.seedcompany.cordtables.model.TablesOption;
import com.seedcompany.cordtables.utils.SeleniumUtils;

/**
 * Represent the mainpage after login
 * 
 * @author swati
 *
 */
public class MainPage extends Page {

	public MainPage(WebDriver webDriver) {
		super(webDriver);
	}
	
	/**
	 * This method Represent the mainpage should loaded
	 */
	public boolean isPageLoaded() {
		WebElement mainMenu = SeleniumUtils.expand_shadow_element(driver,
				this.rootApp.findElement(By.tagName("custom-accordion")));
		return mainMenu != null;
	}

	/**
	 * This method is use to expand the Menu button 
	 */
	public void expandMenuOptions() {
		SearchContext mainMenu = SeleniumUtils
				.expand_shadow_element(this.rootApp.findElement(By.tagName("custom-accordion")));
		mainMenu.findElement(By.cssSelector(".accordion")).click();

	}

	/**
	 * This method is use to expand the page button under Menu
	 */
	public void expandPagesMenu() {
		WebElement mainMenu = this.rootApp.findElement(By.tagName("custom-accordion"));
		SearchContext tablesMenu = SeleniumUtils
				.expand_shadow_element(mainMenu.findElements(By.tagName("custom-accordion")).get(0));
		tablesMenu.findElement(By.cssSelector(".accordion")).click();

	}
	/**
	 * This method is use to expand the table button under Menu
	 */
	public void expandTablesMenu() {
		WebElement mainMenu = this.rootApp.findElement(By.tagName("custom-accordion"));
		SearchContext tablesMenu = SeleniumUtils
				.expand_shadow_element(mainMenu.findElements(By.tagName("custom-accordion")).get(1));
		tablesMenu.findElement(By.cssSelector(".accordion")).click();

	}
	/**
	 * This method is use to select the table option under Menu
	 */
	public void selectTable(TablesOption option) {
		SeleniumUtils.wait(10);
		this.loadApp();
		SearchContext mainMenu = SeleniumUtils
				.expand_shadow_element(this.rootApp.findElement(By.cssSelector("custom-accordion.hydrated")));
		mainMenu.findElement(By.cssSelector(".accordion")).click();
		WebElement tableMenuEle = this.rootApp
				.findElement(By.cssSelector("#full-width > custom-accordion > custom-accordion:nth-child(2)"));
		SearchContext tableMenu = SeleniumUtils.expand_shadow_element(tableMenuEle);
		tableMenu.findElement(By.cssSelector(".accordion")).click();

		List<WebElement> items = tableMenuEle.findElement(By.tagName("ion-list")).findElements(By.tagName("ion-item"));
		for (WebElement item : items) {
			try {
				WebElement label = item.findElement(By.tagName("ion-label"));
				if (label.getText().equalsIgnoreCase(option.getName())) {
					System.out.println("label.getText()" + label.getText());
					label.click();
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}

}