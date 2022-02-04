package com.seedcompany.cordtables.model;

/**
 * Represents the people schema entity.
 * 
 * @author swati
 *
 */
public class People {

	public String about;
	public String phonenumber;
	public String picture;
	public String privatefirstname;
	public String privatelastname;
	public String publicfirstname;
	public String publiclastname;
	public String primarylocation;
	public String privatefullname;
	public String publicfullname;
	public String sensitivityclearance;
	public String timezone;
	public String title;

	@Override
	public String toString() {
		return "AdminPeopleForm [about=" + about + ", phonenumber=" + phonenumber + ", picture=" + picture
				+ ", privatefirstname=" + privatefirstname + ", privatelastname=" + privatelastname
				+ ", publicfirstname" + publicfirstname + ", publiclastname=" + publiclastname + ", primarylocation="
				+ primarylocation + ", privatefullname=" + privatefullname + ", publicfullname=" + publicfullname
				+ ", sensitivityclearance=" + sensitivityclearance + ", timezone=" + timezone + ", title=" + title
				+ "]";
	}

}
