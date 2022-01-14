package com.seedcompany.cordtables.model;

/**
 * Represents the table options mapping available on the screen.
 * 
 * @author swati
 *
 */
public enum TablesOption {

	ADMIN_DB_VERSION_CONTROL("admin-database-version-control", "admin.database-version-control"),
	ADMIN_GROUP_ROW_ACCESS("admin-group-row-access", "admin.group-row-access"),
	ADMIN_GROUP_MEMBERSHIPS("admin-group-memberships", "admin.group-memberships");

	private String tag;
	private String name;

	private TablesOption(String tag, String name) {
		this.tag = tag;
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getTag() {
		return tag;
	}

}
