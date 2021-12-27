package com.seedcompany.cordtables.model;

/**
 * Represents the browser configurations used to execute the test cases.
 * 
 * @author swati
 * 
 */
public class BrowserDriverConfig {

	private String driverPath;
	private String binaryPath;
	private String type;
	private boolean headless;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	private boolean verbose;

	public String getDriverPath() {
		return driverPath;
	}

	public void setDriverPath(String driverPath) {
		this.driverPath = driverPath;
	}

	public String getBinaryPath() {
		return binaryPath;
	}

	public void setBinaryPath(String binaryPath) {
		this.binaryPath = binaryPath;
	}

	public boolean isHeadless() {
		return headless;
	}

	public void setHeadless(boolean headless) {
		this.headless = headless;
	}

	public boolean isVerbose() {
		return verbose;
	}

	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}

	@Override
	public String toString() {
		return "BrowserDriverConfig [driverPath=" + driverPath + ", binaryPath=" + binaryPath + ", type=" + type
				+ ", headless=" + headless + ", verbose=" + verbose + "]";
	}

}