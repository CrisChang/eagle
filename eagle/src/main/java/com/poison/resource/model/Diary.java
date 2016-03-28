package com.poison.resource.model;

import com.keel.common.lang.BaseDO;

/**
 * 
 * 类的作用:此方法的作用是封装数据库字段
 * 作者:闫前刚
 * 创建时间:2014-7-30下午12:17:42
 * email :1486488968@qq.com
 * version: 1.0
 */
public class Diary extends BaseDO implements Comparable<Diary>{
	/**
	 * Diary序列号
	 */
	private static final long serialVersionUID = 7857286837808433303L;
	private long id;//主键id
	private String type;//选择类型
	private String content;//内容
	private long beginDate;//发表时间啊
	private long uid;//用户名
	private long isDel=0;//0表示被删除1表示没删除
	private long endDate;//更新时间
	private int flag=0;//标识符
	private String lon;//经度
	private String lat;//维度
	private String locationName;//地理位置描述
	private String locationCity;//地理位置城市
	private String locationArea;//地理位置地区
	private String title;//标题
	private String cover;//封面
	private String tag;//标签

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
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
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		if(null==content){
			content = "";
		}
		this.content = content;
	}
	public long getBeginDate() {
		return beginDate;
	}
	public void setBeginDate(long beginDate) {
		this.beginDate = beginDate;
	}
	public long getUid() {
		return uid;
	}
	public void setUid(long uid) {
		this.uid = uid;
	}
	public long getIsDel() {
		return isDel;
	}
	public void setIsDel(long isDel) {
		this.isDel = isDel;
	}
	public long getEndDate() {
		return endDate;
	}
	public void setEndDate(long endDate) {
		this.endDate = endDate;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	public Diary() {
		super();
	}
	@Override
	public int compareTo(Diary o) {
		if(o.id==this.id){
			return 1;
		}else{
			return -1;
		}
	}
	
	
}
