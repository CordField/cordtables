package com.seedcompany.cordtables.model;

/**
 * Represents the table options mapping available on the screen.
 * 
 * @author swati
 *
 */
public enum TablesOption {

	ADMIN_DB_VERSION_CONTROL("admin-database-version-control", "database-version-control", "admin"),
	ADMIN_GROUP_ROW_ACCESS("admin-group-row-access", "group-row-access", "admin"),
	ADMIN_GROUP_MEMBERSHIPS("admin-group-memberships", "group-memberships", "admin"),
	UP_PRAYER_REQUESTS("up-prayer-requests", "prayer-requests", "up"), ADMIN_PEOPLE("admin-people", "people", "admin"),
	COMMON_LOCATIONS("common-locations", "locations", "common");

	private String tag;
	private String name;
	private String parentSchema;

	private TablesOption(String tag, String name, String parentSchema) {
		this.tag = tag;
		this.name = name;
		this.parentSchema = parentSchema;
	}

	public String getParentSchema() {
		return parentSchema;
	}

	public String getName() {
		return name;
	}

	public String getTag() {
		return tag;
	}

}
