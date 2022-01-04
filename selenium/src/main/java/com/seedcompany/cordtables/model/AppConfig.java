package com.seedcompany.cordtables.model;

/**
 * This class contains the configurations specific to application.
 * 
 * @author swati
 *
 */
public class AppConfig {

	private String url;
	private String username;
	private String password;
	private String newUser;
	private String newUserPassword;

	public String getNewUser() {
		return newUser;
	}

	public void setNewUser(String newUser) {
		this.newUser = newUser;
	}

	public String getNewUserPassword() {
		return newUserPassword;
	}

	public void setNewUserPassword(String newUserPassword) {
		this.newUserPassword = newUserPassword;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "AppConfig [url=" + url + ", username=" + username + ", password=" + password + "]";
	}

}
