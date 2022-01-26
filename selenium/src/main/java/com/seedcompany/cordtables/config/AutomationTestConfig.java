package com.seedcompany.cordtables.config;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.seedcompany.cordtables.model.AppConfig;
import com.seedcompany.cordtables.model.BrowserDriverConfig;
import com.seedcompany.cordtables.utils.ConfigurationUtils;

/**
 * This class contains all the configurations used by the test execution
 * environment.
 * 
 * @author swati
 * 
 */
public class AutomationTestConfig {

	public AppConfig getAppConfigs() {
		return appConfigs;
	}

	public void setAppConfigs(AppConfig appConfigs) {
		this.appConfigs = appConfigs;
	}

	private static final AutomationTestConfig automationContext = new AutomationTestConfig();

	private WebDriver driver;

	private BrowserDriverConfig browserConfigs;

	private AppConfig appConfigs;

	/**
	 * constructor to initialize the application configurations.
	 * 
	 */
	private AutomationTestConfig() {
		Properties configs = ConfigurationUtils.loadProperties("application.properties");
		this.appConfigs = new AppConfig();
		appConfigs.setUrl(configs.getProperty("site.url"));
		appConfigs.setUsername(configs.getProperty("site.user.name"));
		appConfigs.setPassword(configs.getProperty("site.user.password"));
		appConfigs.setNewUser(configs.getProperty("site.user.registration.name"));
		appConfigs.setNewUserPassword(configs.getProperty("site.user.registration.password"));
		this.browserConfigs = new BrowserDriverConfig();
		this.browserConfigs.setType(configs.getProperty("browser.config.driver.type", BrowserType.CHROME));
		this.browserConfigs.setDriverPath(configs.getProperty("browser.config.driver.path"));
		this.browserConfigs.setHeadless(!Boolean.valueOf(configs.getProperty("test.execution.background.disabled", "false")));
		this.browserConfigs.setVerbose(Boolean.valueOf(configs.getProperty("browser.config.debug.enabled", "false")));
		this.browserConfigs.setDevMode(Boolean.valueOf(configs.getProperty("browser.config.devMode.enabled", "false")));
	}

	public Capabilities getCapabilities(Properties properties) throws IOException {
		String capabilitiesFile = properties.getProperty("capabilities");

		Properties capsProps = ConfigurationUtils.loadProperties(capabilitiesFile);

		DesiredCapabilities capabilities = new DesiredCapabilities();
		for (String name : capsProps.stringPropertyNames()) {
			String value = capsProps.getProperty(name);
			if (value.toLowerCase().equals("true") || value.toLowerCase().equals("false")) {
				capabilities.setCapability(name, Boolean.valueOf(value));
			} else if (value.startsWith("file:")) {
				capabilities.setCapability(name,
						new File(".", value.substring(5)).getCanonicalFile().getAbsolutePath());
			} else {
				capabilities.setCapability(name, value);
			}
		}

		return capabilities;
	}

	public static AutomationTestConfig getAutomationContext() {
		return automationContext;
	}

	public WebDriver getDriver() {
		return driver;
	}

	public BrowserDriverConfig getBrowserConfigs() {
		return browserConfigs;
	}

}
