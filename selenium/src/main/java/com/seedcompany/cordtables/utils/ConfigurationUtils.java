package com.seedcompany.cordtables.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * This class is used as utility to load the configuration files from classpath.
 * 
 * @author swati
 *
 */
public class ConfigurationUtils {

	/**
	 * Method to load the test configurations.
	 * 
	 * @param configFilePath
	 * @return
	 */
	public static Properties loadProperties(String configFilePath) {

		final Properties configs = new Properties();
		try (FileInputStream propConfigs = new FileInputStream(configFilePath)) {
			configs.load(propConfigs);
		} catch (final FileNotFoundException e) {
		} catch (final IOException e) {
			e.printStackTrace();
		}
		if (configs == null || configs.size() <= 0) {
			try {
				final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
				configs.load(classLoader.getResourceAsStream(configFilePath));
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}
		return configs;
	}

}
