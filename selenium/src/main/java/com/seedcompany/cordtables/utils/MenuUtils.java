package com.seedcompany.cordtables.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author swati
 *
 */
public class MenuUtils {

	public SearchContext rootApp;

	private static Logger logger = LoggerFactory.getLogger(MenuUtils.class);

	public MenuUtils(SearchContext rootApp) {
		this.rootApp = rootApp;
	}

	/**
	 * this method is used for clicking the edit mode button mode as true
	 */

	public void enableEditMode() {
		SeleniumUtils.wait(1);
		WebElement editModeOption = this.rootApp
				.findElement(By.cssSelector("#container > div.edit-button > div > ion-toggle"));
		boolean state = editModeOption.getAttribute("class").contains("toggle-checked");

		logger.debug("Is Page is editable ? " + (state ? "Yes" : "No"));
		if (!state) {
			editModeOption.click();
		}
	}

	/**
	 * this method is used for clicking the edit mode button mode as true
	 */

	public void disableEditMode() {

		WebElement editModeOption = this.rootApp
				.findElement(By.cssSelector("#container > div.edit-button > div > ion-toggle"));
		boolean state = editModeOption.getAttribute("class").contains("toggle-checked");

		logger.debug("Is Page is editable ? " + (state ? "Yes" : "No"));
		if (state) {
			editModeOption.click();
		}
	}

}
