package com.poison.resource.model;

import com.keel.common.lang.BaseDO;

public class BookTalk extends BaseDO implements Comparable<BookTalk>{
	/**
	 * BookTalk序列号
	 */
	private static final long serialVersionUID = -4753309583254896492L;
	private long id;
	private int bookId;
	private long uid;//用户id
	private int page;
	private String content;
	private int isDel;
	private int type;
	private long createTime;
	private String resType;
	private int flag;
	
	public String getResType() {
		return resType;
	}
	public void setResType(String resType) {
		this.resType = resType;
	}
	public BookTalk() {
		super();
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public int getBookId() {
		return bookId;
	}
	public void setBookId(int bookId) {
		this.bookId = bookId;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public long getUid() {
		return uid;
	}
	public void setUid(long uid) {
		this.uid = uid;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getIsDel() {
		return isDel;
	}
	public void setIsDel(int isDel) {
		this.isDel = isDel;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	@Override
	public int compareTo(BookTalk o) {
		if(o.page==this.page){
			return 1;
		}
		return -1;
	}
}
