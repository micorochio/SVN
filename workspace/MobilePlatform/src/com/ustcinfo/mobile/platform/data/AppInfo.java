package com.ustcinfo.mobile.platform.data;

import java.util.List;

public class AppInfo {
	private Integer appId;
	private String appName;
	
	private String appType;
	private String appSize;
	private String appIconUrl;
	private String packageName;
	private String downLoadUrl;
	private int version;//版本//修改版本为数字
	private String versionName;
	
	public String getVersionName() {
		return versionName;
	}
	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}
	private String description;//描述
	private List<String> imgURL;//截图信息
	private String updateTime;
	private Integer progress=0;
	
	public Integer getProgress() {
		return progress;
	}
	public void setProgress(Integer progress) {
		this.progress = progress;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	public List<String> getImgURL() {
		return imgURL;
	}
	public void setImgURL(List<String> imgURL) {
		this.imgURL = imgURL;
	}

	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDownLoadUrl() {
		return downLoadUrl;
	}
	public void setDownLoadUrl(String downLoadUrl) {
		this.downLoadUrl = downLoadUrl;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public Integer getAppId() {
		return appId;
	}
	public void setAppId(Integer appId) {
		this.appId = appId;
	}
	public String getAppType() {
		return appType;
	}
	public void setAppType(String appType) {
		this.appType = appType;
	}
	public String getAppSize() {
		return appSize;
	}
	public void setAppSize(String appSize) {
		this.appSize = appSize;
	}
	public String getAppIconUrl() {
		return appIconUrl;
	}
	public void setAppIconUrl(String appImageUrl) {
		this.appIconUrl = appImageUrl;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
}
