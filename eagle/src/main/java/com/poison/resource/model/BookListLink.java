package com.poison.resource.model;

import com.keel.common.lang.BaseDO;
import com.poison.eagle.entity.ResourceInfo;

public class BookListLink extends BaseDO implements Comparable<BookListLink> {

	/**
	 * BookListLink序列号
	 */
	private static final long serialVersionUID = -6940378646177905281L;
	private long id;
	private long bookListId;
	private int bookId;
	private int isDel;
	private int isDb;
	private String description;
	private String friendinfo;
	private String address;
	private String tags;
	private long createDate;
	private long latestRevisionDate;
	private String resType;
	private int flag;
	
	public String getResType() {
		return resType;
	}
	public void setResType(String resType) {
		this.resType = resType;
	}
	public String getFriendinfo() {
		return friendinfo;
	}
	public void setFriendinfo(String friendinfo) {
		this.friendinfo = friendinfo;
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
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
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getBookListId() {
		return bookListId;
	}
	public void setBookListId(long bookListId) {
		this.bookListId = bookListId;
	}
	
	public int getBookId() {
		return bookId;
	}
	public void setBookId(int bookId) {
		this.bookId = bookId;
	}
	public int getIsDel() {
		return isDel;
	}
	public void setIsDel(int isDel) {
		this.isDel = isDel;
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
	public int compareTo(BookListLink o) {
		if(o.id>=this.id){
			return 1;
		}
		return -1;
	}
	
}
