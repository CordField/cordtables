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
	public WebElement expandSchemaMenu() {
		SearchContext mainMenu = SeleniumUtils
				.expand_shadow_element(this.rootApp.findElement(By.cssSelector("custom-accordion.hydrated")));
		mainMenu.findElement(By.cssSelector(".accordion")).click();
		System.out.println("Main menu expand ---" + mainMenu);
		WebElement schemaMenuEle = this.rootApp.findElement(By.cssSelector("custom-accordion.hydrated"))
				.findElements(By.cssSelector("custom-accordion.hydrated")).get(1);
		System.out.println("expand schema menu" + schemaMenuEle);
		SearchContext schemaMenu = SeleniumUtils.expand_shadow_element(schemaMenuEle);
		schemaMenu.findElement(By.cssSelector(".accordion")).click();
		return schemaMenuEle;
	}

	/**
	 * This method is use to select the table option under Menu
	 */
	public boolean selectSchema(TablesOption option) {
		SeleniumUtils.wait(3);
		this.loadApp();
		WebElement schemaMenu = this.expandSchemaMenu();
		List<WebElement> schemas = schemaMenu.findElement(By.tagName("ion-list"))
				.findElements(By.cssSelector("custom-accordion.hydrated"));
		try {
			for (WebElement schema : schemas) {
				SearchContext subSchemaContext = SeleniumUtils.expand_shadow_element(schema);
				WebElement schemaName = subSchemaContext.findElement(By.cssSelector(".accordion > span:nth-child(1)"));

				SeleniumUtils.scrollDown(driver);
				if (schemaName.getText().equalsIgnoreCase(option.getParentSchema())) {
					SeleniumUtils.scrollToElement(schemaName, driver);
					SeleniumUtils.wait(1);
					schemaName.click();

					List<WebElement> items = schema.findElement(By.tagName("ion-list"))
							.findElements(By.tagName("ion-item"));
					for (WebElement item : items) {

						WebElement label = item.findElement(By.tagName("ion-label"));
						if (label.getText().equalsIgnoreCase(option.getName())) {
							SeleniumUtils.scrollToElement(label, driver);
							label.click();
							break;
						}

					}
					break;
				}
			}
			return true;
		} catch (Exception e) {
			System.err.println("Failed to process the request and find the requested table.");
			e.printStackTrace();
			return false;
		}

	}

}