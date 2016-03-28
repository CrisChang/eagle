package com.poison.resource.model;

import com.keel.common.lang.BaseDO;

public class MvListLink extends BaseDO implements Comparable<MvListLink>{
	/**
	 * MvListLink序列号
	 */
	private static final long serialVersionUID = -1011684277879123967L;
	private long id;//主鍵id
	private long filmListId;//影单id
	private long movieId;//电影id
	private int isDel;//0表示删除，1表示未删除
	private int isDB;//库中是否有
	private String description;//影单描述
	private String friendInfo;//朋友信息
	private String address;//当前地址
	private String tags;//标签
	private long createDate;//创建时间
	private long latestRevisionDate;//更新时间
	private int flag;//标示
	
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getFriendInfo() {
		return friendInfo;
	}
	public void setFriendInfo(String friendInfo) {
		this.friendInfo = friendInfo;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getTags() {
		return tags;
	}
	public void setTags(String tags) {
		this.tags = tags;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	public long getFilmListId() {
		return filmListId;
	}
	public void setFilmListId(long filmListId) {
		this.filmListId = filmListId;
	}
	public long getMovieId() {
		return movieId;
	}
	public void setMovieId(long movieId) {
		this.movieId = movieId;
	}
	public int getIsDel() {
		return isDel;
	}
	public void setIsDel(int isDel) {
		this.isDel = isDel;
	}
	public int getIsDB() {
		return isDB;
	}
	public void setIsDB(int isDB) {
		this.isDB = isDB;
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
	
	
	public MvListLink() {
		super();
	}
	@Override
	public int compareTo(MvListLink o) {
		if(o.id==this.id){
			return 1;
		}
		return -1;
	}
	
	
}
