package com.poison.store.model;

import com.keel.common.lang.BaseDO;

public class NetBook extends BaseDO{

	/**
	 * NetBook的序列号
	 */
	private static final long serialVersionUID = -1112131796646167111L;
	private long id;							//主键id
	private String source;					//小说来源
	private long bookId;					//小说ID
	private String name;						//小说名字
	private long authorId;					//作者id
	private String authorName;			//作者名字
	private String bookUrl;					//网络小说的链接地址
	private String type;						//网络小说类型
	private String tags;						//网络小说标签
	private String pagePicUrl;				//封面图片
	private String pagePic;					//图片
	private String introduction;			//简介
	private int collTime;						//采集时间
	private int state;							//记录状态
	private int flag;							//返回标识
	private String resType="29";		//网络小说类型
	
	public String getResType() {
		return resType;
	}
	public void setResType(String resType) {
		this.resType = resType;
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
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public long getBookId() {
		return bookId;
	}
	public void setBookId(long bookId) {
		this.bookId = bookId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getAuthorId() {
		return authorId;
	}
	public void setAuthorId(long authorId) {
		this.authorId = authorId;
	}
	public String getAuthorName() {
		return authorName;
	}
	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}
	public String getBookUrl() {
		return bookUrl;
	}
	public void setBookUrl(String bookUrl) {
		this.bookUrl = bookUrl;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTags() {
		return tags;
	}
	public void setTags(String tags) {
		this.tags = tags;
	}
	public String getPagePicUrl() {
		return pagePicUrl;
	}
	public void setPagePicUrl(String pagePicUrl) {
		this.pagePicUrl = pagePicUrl;
	}
	public String getPagePic() {
		return pagePic;
	}
	public void setPagePic(String pagePic) {
		this.pagePic = pagePic;
	}
	public String getIntroduction() {
		return introduction;
	}
	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}
	public int getCollTime() {
		return collTime;
	}
	public void setCollTime(int collTime) {
		this.collTime = collTime;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	
	
}
