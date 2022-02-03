package com.seedcompany.cordtables.model;

/**
 * Represents the Location information.
 * 
 * @author swati
 *
 */
public class LocationInfo {

	public String name;
	public String sensitivity;
	public String type;
	public String isoAlpha3;

	@Override
	public String toString() {
		return "CommonLocationsForm [name=" + name + ", sensitivity=" + sensitivity + ", type=" + type
				+ ", isoAlpha3 = " + isoAlpha3 + "]";
	}

}
