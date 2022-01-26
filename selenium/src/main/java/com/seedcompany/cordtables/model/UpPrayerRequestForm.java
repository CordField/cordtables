package com.seedcompany.cordtables.model;

public class UpPrayerRequestForm {

	public String requestLanguageId;
	public String targetLanguageId;
	public String sensitivity;
	public String organizationName;
	public String parent;
	public String translator;
	public String location;
	public String title;
	public String content;
	public String reviewed;
	public String prayerType;

	@Override
	public String toString() {
		return "UpPrayerRequestForm [requestLanguageId=" + requestLanguageId + ", targetLanguageId=" + targetLanguageId
				+ ", sensitivity=" + sensitivity + ", organizationName=" + organizationName + ", parent=" + parent
				+ ", translator=" + translator + ", location=" + location + ", title=" + title + ", content=" + content
				+ ", reviewed=" + reviewed + ", prayerType=" + prayerType + "]";
	}

}
