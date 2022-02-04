package com.seedcompany.cordtables.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

/**
 * @author swati
 *
 */
public class MenuUtils {

	public SearchContext rootApp;

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

		System.out.println("Is Page is editable ? " + (state ? "Yes" : "No"));
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

		System.out.println("Is Page is editable ? " + (state ? "Yes" : "No"));
		if (state) {
			editModeOption.click();
		}
	}

}
