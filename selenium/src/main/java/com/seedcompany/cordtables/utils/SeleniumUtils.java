package com.seedcompany.cordtables.utils;

import java.util.logging.Logger;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.seedcompany.cordtables.model.BrowserDriverConfig;

/**
 * This class contains the utility methods that can be used to interact with
 * browser.
 * 
 * @author swati
 *
 */
public class SeleniumUtils {

	private static final Logger logger = Logger.getLogger(SeleniumUtils.class.getName());

	/**
	 * This method can be used to get the web driver that can be used to access the
	 * browser.
	 * 
	 * @param browserConfig
	 * @return
	 */
	public static WebDriver getDriver(BrowserDriverConfig browserConfig) {

		WebDriver driver = null;
		try {
			switch (browserConfig.getType()) {
			case BrowserType.CHROME:

				System.setProperty("webdriver.chrome.driver", browserConfig.getDriverPath());
				final ChromeOptions chromeOptions = new ChromeOptions();
				chromeOptions.addArguments("--no-sandbox");
				chromeOptions.addArguments("enable-automation");
				if (browserConfig.isHeadless()) {
					chromeOptions.addArguments("--headless");
				}
				chromeOptions.addArguments("--window-size=1920,1080");
				chromeOptions.addArguments("--disable-extensions");
				chromeOptions.addArguments("--dns-prefetch-disable");
				chromeOptions.addArguments("--disable-gpu");
				chromeOptions.setPageLoadStrategy(PageLoadStrategy.NORMAL);
				chromeOptions.setCapability("chrome.verbose", browserConfig.isVerbose());
				chromeOptions.addArguments("--disable-web-security");
				chromeOptions.addArguments("--allow-running-insecure-content");
				driver = new ChromeDriver(chromeOptions);
				break;

			default:

				if (driver == null) {
					// default is chrome.
					System.setProperty("webdriver.chrome.driver", "C:\\tools\\chromedriver.exe");
					final ChromeOptions bChromeOptions = new ChromeOptions();
					bChromeOptions.addArguments("--no-sandbox");
					bChromeOptions.addArguments("enable-automation");
					// chromeOptions.addArguments("--headless");
					bChromeOptions.addArguments("--window-size=1920,1080");
					bChromeOptions.addArguments("--disable-extensions");
					bChromeOptions.addArguments("--dns-prefetch-disable");
					bChromeOptions.addArguments("--disable-gpu");
					bChromeOptions.setPageLoadStrategy(PageLoadStrategy.NORMAL);
					bChromeOptions.setCapability("chrome.verbose", true);
					// disable logging
					// disable the web security
					bChromeOptions.addArguments("--disable-web-security");
					bChromeOptions.addArguments("--allow-running-insecure-content");
					driver = new ChromeDriver(bChromeOptions);

				}
				break;
			}

		} catch (final Exception ex) {
			ex.printStackTrace();
		}

		driver.manage().window().maximize();
		return driver;
	}

	/**
	 * This method is used to introduce wait time for given seconds in the current
	 * running process. It can be used to wait before the page gets loaded.
	 * 
	 * @param time
	 */
	public static void wait(int time) {
		try {
			Thread.sleep(time * 1000);
		} catch (final InterruptedException e) {

		}
	}

	/**
	 * Method to open the user interaction with the site.
	 */

	public static void openSession(WebDriver driver, String url) {
		SeleniumUtils.wait(5);
		driver.get(url);
		SeleniumUtils.wait(1);
	}

	/**
	 * Method to close the user interaction with the site.
	 */

	public static void closeSession(WebDriver driver) {
		SeleniumUtils.wait(1);
		driver.close();
	}

	/**
	 * This method is used to expand the shadow element.
	 * 
	 * @param driver
	 * @param element
	 * @return
	 */
	public static WebElement expand_shadow_element(WebDriver driver, WebElement element) {
		WebElement shadow_root = (WebElement) ((JavascriptExecutor) driver)
				.executeScript("return arguments[0].shadowRoot", element);
		return shadow_root;
	}

	/**
	 * @param d
	 * @param elementValue
	 * @return
	 */
	public static WebElement IsElementFound(WebDriver d, By elementValue) {
		try {
			WebElement myDynamicElement = new WebDriverWait(d, 3)
					.until(ExpectedConditions.presenceOfElementLocated(elementValue));
			return myDynamicElement;
		} catch (final TimeoutException e) {
			// logger.error("TimeoutException : ()", e);
		}
		return null;
	}

	/**
	 * @param d
	 * @param elementValue
	 * @param parent
	 * @return
	 */
	public static WebElement IsElementFound(WebDriver d, By elementValue, WebElement parent) {
		try {
			WebElement myDynamicElement = parent.findElement(elementValue);
			return myDynamicElement;
		} catch (final Exception e) {
			// logger.error("TimeoutException : ()", e);
		}
		return null;
	}

}
