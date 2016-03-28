package com.poison.resource.model;

import com.keel.common.lang.BaseDO;

public class BkComment extends BaseDO implements Comparable<BkComment>{

	/**
	 * BkComment序列号
	 */
	private static final long serialVersionUID = 8673081880339603169L;
	private long id;
	private long userId;
	private int bookId;
	private String comment;
	private String score;
	private int isDb;
	private int isOpposition;
	private int isDelete;
	private float bigValue;
	private String type;
	private long createDate;
	private long latestRevisionDate;
	private String description;
	private String resType;
	private String lon;//经度
	private String lat;//维度
	private String locationName;//地理位置描述
	private String locationCity;//地理位置城市
	private String locationArea;//地理位置地区
	private String title;//标题
	private String cover;//封面
	private String resourceType;//当前资源的type类型 长、短书评
	private String tag;//标签


	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getResourceType() {
		return resourceType;
	}
	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getCover() {
		return cover;
	}
	public void setCover(String cover) {
		this.cover = cover;
	}
	public String getLocationCity() {
		return locationCity;
	}
	public void setLocationCity(String locationCity) {
		this.locationCity = locationCity;
	}
	public String getLocationArea() {
		return locationArea;
	}
	public void setLocationArea(String locationArea) {
		this.locationArea = locationArea;
	}
	public String getLon() {
		return lon;
	}
	public void setLon(String lon) {
		this.lon = lon;
	}
	public String getLat() {
		return lat;
	}
	public void setLat(String lat) {
		this.lat = lat;
	}
	


	public String getLocationName() {
		return locationName;
	}
	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}



	private int flag;
	
	public String getResType() {
		return resType;
	}
	public void setResType(String resType) {
		this.resType = resType;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		if(null==description){
			description = "";
		}
		this.description = description;
	}
	public float getBigValue() {
		return bigValue;
	}
	public void setBigValue(float bigValue) {
		this.bigValue = bigValue;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getIsDb() {
		return isDb;
	}
	public void setIsDb(int isDb) {
		this.isDb = isDb;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		if(null==comment){
			comment = "";
		}
		this.comment = comment;
	}
	public String getScore() {
		return score;
	}
	public void setScore(String score) {
		this.score = score;
	}
	public int getIsOpposition() {
		return isOpposition;
	}
	public void setIsOpposition(int isOpposition) {
		this.isOpposition = isOpposition;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	
	public int getBookId() {
		return bookId;
	}
	public void setBookId(int bookId) {
		this.bookId = bookId;
	}
	public int getIsDelete() {
		return isDelete;
	}
	public void setIsDelete(int isDelete) {
		this.isDelete = isDelete;
	}
	public long getCreateDate() {
		return createDate;
	}
	public void setCreateDate(long createDate) {
		this.createDate = createDate;
	}
	public long getLatestRevisionDate() {
		return latestRevisionDate;
	}
	public void setLatestRevisionDate(long latestRevisionDate) {
		this.latestRevisionDate = latestRevisionDate;
	}
	
	
	@Override
	public int compareTo(BkComment o) {
		if (o.id == this.id) {
			return 1;
		} else {
			return -1;
		}
	}
	
}
