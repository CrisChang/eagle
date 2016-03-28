package com.poison.eagle.entity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.keel.common.lang.BaseDO;
import com.poison.eagle.utils.CheckParams;
import com.poison.resource.model.ChapterSummary;

public class SerializeInfo extends BaseDO implements Comparable<SerializeInfo>,Serializable{
	/**
	 * serialVersionUID = 4115252126878371654L;
	 */
	private static final long serialVersionUID = 4115252126878371654L;
	private static final String DATEFORMAT = "yyyy-MM-dd HH:mm:ss";
	private SimpleDateFormat sf;
	private long id;
	private String title;
	private String describe;//简介
	private String author;//作者简介
	private String btime;//创建时间
	private String utime;//修改时间
	private String type;
	private String url;//封面地址
	private String chapterTitle;//章节题目
	private UserEntity userEntity;
	private List<ResourceInfo> chapterList;//章节目录
	private List<String> tags;//标签
	private int isSubscribe=1;//0:订阅、1：微订阅
	private int rNum;
	private int cNum;
	private int zNum;
	private int isNovelshelf;//是否加入书架

	public List<ResourceInfo> getChapterList() {
		return chapterList;
	}

	public void setChapterList(List<ResourceInfo> chapterList) {
		this.chapterList = chapterList;
	}

	public int getIsNovelshelf() {
		return isNovelshelf;
	}

	public void setIsNovelshelf(int isNovelshelf) {
		this.isNovelshelf = isNovelshelf;
	}

	public SerializeInfo() {
		super();
	}
	
	public SerializeInfo(long uid, String uname, long id, String title,
			String describe, String author, long btime,
			long utime,String type) {
		super();
		if(sf == null){
			sf = new SimpleDateFormat(DATEFORMAT);
		}
		Date bdate = new Date(btime);
		Date udate = new Date(utime);
		this.id = id;
		this.title = title;
		this.describe = describe;
		this.author = author;
		this.btime = sf.format(bdate);
		this.utime = sf.format(utime);
		this.type = type;
	}
	public void setUserEntity(UserEntity userEntity) {
		this.userEntity = userEntity;
	}
	public UserEntity getUserEntity() {
		return userEntity;
	}

	public long getId() {
		return id;
	}

	public int getIsSubscribe() {
		return isSubscribe;
	}

	public void setIsSubscribe(int isSubscribe) {
		this.isSubscribe = isSubscribe;
	}

	public String getChapterTitle() {
		return chapterTitle;
	}

	public void setChapterTitle(String chapterTitle) {
		this.chapterTitle = chapterTitle;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescribe() {
		return describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getBtime() {
		return btime;
	}

	public void setBtime(long btime) {
		if(sf == null){
			sf = new SimpleDateFormat(DATEFORMAT);
		}
		Date bdate = new Date(btime);
		this.btime = sf.format(bdate);
	}

	public String getUtime() {
		return utime;
	}

	public void setUtime(long utime) {
		if(sf == null){
			sf = new SimpleDateFormat(DATEFORMAT);
		}
		Date udate = new Date(utime);
		this.utime = sf.format(udate);
	}

	public int getrNum() {
		return rNum;
	}

	public void setrNum(int rNum) {
		this.rNum = rNum;
	}

	public int getcNum() {
		return cNum;
	}

	public void setcNum(int cNum) {
		this.cNum = cNum;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getzNum() {
		return zNum;
	}

	public void setzNum(int zNum) {
		this.zNum = zNum;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(String tags) {
		List<String> tagsList = CheckParams.putStringToList(tags);
		this.tags = tagsList;
	}

	@Override
	public int compareTo(SerializeInfo o) {
		if(o.id>=this.id){
			return 1;
		}
		return -1;
	}
	public static void main(String[] args) {
		SerializeInfo si = new SerializeInfo();
		si.setBtime(1406702314129l);
		si.setUtime(1406703543757l);
		
		System.out.println(si.getBtime()+si.getUtime());
	}
	
}
