package com.poison.eagle.entity;

import java.io.Serializable;

import com.keel.common.lang.BaseDO;

public class VersionInfo extends BaseDO implements Comparable<VersionInfo>,Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3393089638998881616L;
	private int id;
	private String appName;
	private String version;
	private String url;
	private long buildTime;
	private long updateTime;
	private int isUpdate;
	private int flag;
	private String iosAppstoreVersion;
	private String iosFirVersion;
	private String androidStoreVersion;
	private String androidFirVersion;
	
	public String getIosAppstoreVersion() {
		return iosAppstoreVersion;
	}
	public void setIosAppstoreVersion(String iosAppstoreVersion) {
		this.iosAppstoreVersion = iosAppstoreVersion;
	}
	public String getIosFirVersion() {
		return iosFirVersion;
	}
	public void setIosFirVersion(String iosFirVersion) {
		this.iosFirVersion = iosFirVersion;
	}
	public String getAndroidStoreVersion() {
		return androidStoreVersion;
	}
	public void setAndroidStoreVersion(String androidStoreVersion) {
		this.androidStoreVersion = androidStoreVersion;
	}
	public String getAndroidFirVersion() {
		return androidFirVersion;
	}
	public void setAndroidFirVersion(String androidFirVersion) {
		this.androidFirVersion = androidFirVersion;
	}
	public int getIsUpdate() {
		return isUpdate;
	}
	public void setIsUpdate(int isUpdate) {
		this.isUpdate = isUpdate;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public long getBuildTime() {
		return buildTime;
	}
	public void setBuildTime(long buildTime) {
		this.buildTime = buildTime;
	}
	public long getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}
	@Override
	public int compareTo(VersionInfo o) {
		if(o.id>=this.id){
			return 1;
		}
		return -1;
	}
	
}
