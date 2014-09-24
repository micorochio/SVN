package com.ustcinfo.mobile.platform.domain;

import android.graphics.drawable.Drawable;

public class AppsModel {
	private Drawable activity_Icon;

	private String activity_Name;
	private String package_Name;
	private String description;

	private CharSequence activity_text;

	public CharSequence getActivity_text() {
		return activity_text;
	}

	public void setActivity_text(CharSequence activityText) {
		activity_text = activityText;
	}

	public Drawable getActivity_Icon() {
		return activity_Icon;
	}

	public void setActivity_Icon(Drawable activityIcon) {
		activity_Icon = activityIcon;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	private int id;
	private int category;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCategory() {
		return category;
	}

	public void setCategory(int category) {
		this.category = category;
	}

	public CharSequence getActivity_Name() {
		return activity_Name;
	}

	public void setActivity_Name(String activityName) {
		activity_Name = activityName;
	}

	public String getPackage_Name() {
		return package_Name;
	}

	public void setPackage_Name(String packageName) {
		package_Name = packageName;
	}

}
