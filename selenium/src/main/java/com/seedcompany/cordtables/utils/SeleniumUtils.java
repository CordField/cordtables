package com.seedcompany.cordtables.utils;

import java.io.File;
import java.util.logging.Logger;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxDriverLogLevel;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.GeckoDriverService;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.collect.ImmutableMap;
import com.seedcompany.cordtables.model.BrowserDriverConfig;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.github.bonigarcia.wdm.config.DriverManagerType;

/**
 * This class contains the utility methods that can be used to interact with
 * browser.
 * 
 * @author swati
 *
 */
public class SeleniumUtils {

	private static final Logger logger = Logger.getLogger(SeleniumUtils.class.getName());
	private static final String Firefox = null;

	/**
	 * This method can be used to get the web driver that can be used to access the
	 * browser.
	 * 
	 * @param browserConfig
	 * @return
	 */
	public static WebDriver getDriver(BrowserDriverConfig browserConfig) {

		WebDriver driver = null;
		System.out.println("browserConfig.getType()::" + browserConfig.getType());
		try {
			switch (browserConfig.getType()) {
			case "CHROME":

				// System.setProperty("webdriver.chrome.driver", browserConfig.getDriverPath());

				String isHeadless = System.getProperty("test.execution.background.disabled");
				if (isHeadless != null && !isHeadless.isEmpty()) {
					browserConfig.setHeadless(!Boolean.valueOf(isHeadless));
				}
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
				chromeOptions.addArguments("--auto-open-devtools-for-tabs");
				// chromeOptions.setExperimentalOption("w3c", false);
				WebDriverManager.getInstance(DriverManagerType.CHROME).setup();
				driver = new ChromeDriver(chromeOptions);
				break;

			case "FIREFOX":

				if (driver == null) {

					// System.setProperty("webdriver.gecko.driver","C:\\temp\\geckodriver.exe");

					System.setProperty("webdriver.gecko.driver", browserConfig.getDriverPath() + "\\geckodriver.exe");

					File pathBinary = new File("C:\\Program Files\\Mozilla Firefox\\firefox.exe");
					if (!pathBinary.exists()) {
						pathBinary = new File("C:\\Program Files\\MozillaFirefox\\firefox.exe");
					}
					// FirefoxBinary firefoxBinary = new
					// FirefoxBinary(pathBinary);

					// final FirefoxProfile firefoxProfile = new
					// FirefoxProfile();
					final GeckoDriverService gecoService = new GeckoDriverService.Builder().build();
					final FirefoxOptions options = new FirefoxOptions();
					options.setHeadless(true);
					options.setLogLevel(FirefoxDriverLogLevel.ERROR);
					options.setCapability("marionette", true);
					driver = new FirefoxDriver(gecoService, options);

				}
			default:

				System.out.println("preparing for default configurations.");
				if (driver == null) {
					// default is chrome.
					System.setProperty("webdriver.chrome.driver", "C:\\tools\\driver\\chromedriver.exe");
					final ChromeOptions bChromeOptions = new ChromeOptions();
					bChromeOptions.addArguments("--no-sandbox");
					bChromeOptions.addArguments("enable-automation");
					bChromeOptions.addArguments("--headless");
					bChromeOptions.addArguments("--window-size=1920,1080");
					bChromeOptions.addArguments("--disable-extensions");
					bChromeOptions.addArguments("--dns-prefetch-disable");
					bChromeOptions.addArguments("--disable-gpu");
					bChromeOptions.setPageLoadStrategy(PageLoadStrategy.NORMAL);
					bChromeOptions.setCapability("chrome.verbose", true);

					bChromeOptions.setCapability("chromeOptions", ImmutableMap.of("w3c", false));
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

	public static SearchContext expand_shadow_element(WebElement element) {
		return element.getShadowRoot();
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

	/**
	 * @param d
	 * @param elementValue
	 * @param parent
	 * @return
	 */
	public static WebElement IsElementFound(WebDriver d, By elementValue, SearchContext parent) {
		try {
			WebElement myDynamicElement = parent.findElement(elementValue);
			return myDynamicElement;
		} catch (final Exception e) {
			// logger.error("TimeoutException : ()", e);
		}
		return null;
	}

	public static void scrollToElement(WebElement target, WebDriver driver) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView();", target);
	}

	public static void scrollDown(WebDriver driver) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("window.scrollBy(0,document.body.scrollHeight)");
	}

	public static void scrollHorizontally(WebElement target, WebDriver driver) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView();", target);
	}

	public static void log(WebDriver driver) {
		LogEntries les = driver.manage().logs().get(LogType.PERFORMANCE);
		System.out.println("**********Performance Logs*****************");
		for (LogEntry le : les) {
			System.out.println(le.getMessage());
		}
	}
}
