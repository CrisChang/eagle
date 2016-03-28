package com.poison.eagle.entity;

import com.keel.common.lang.BaseDO;

/**
 * <p>Title: BookTalkListInfo.java</p> 
 * <p>Description: 示例类</p> 
 * @author :changjiang
 * date 2015-4-10 下午4:32:45
 */
public class BookTalkListInfo extends BaseDO{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7422497908934085023L;

	private long bookTalkId;//书摘的id
	//private long createDate;//创建时间
	private String bookName;//书名
	private long bookId;//图书的id
	private String bookPic;//图书封面
	private String authorName;//图书作者
	private String press;//出版社
	private String score;//评分
	private String publishTime;//出版日期
	private String pages;//书摘页数
	private String type;//资源类型  图书：27，网络小说：29
	
	public String getBookName() {
		return bookName;
	}
	public void setBookName(String bookName) {
		this.bookName = bookName;
	}
	public long getBookTalkId() {
		return bookTalkId;
	}
	public void setBookTalkId(long bookTalkId) {
		this.bookTalkId = bookTalkId;
	}
	public long getBookId() {
		return bookId;
	}
	public void setBookId(long bookId) {
		this.bookId = bookId;
	}
	public String getBookPic() {
		return bookPic;
	}
	public void setBookPic(String bookPic) {
		this.bookPic = bookPic;
	}
	public String getAuthorName() {
		return authorName;
	}
	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}
	public String getPress() {
		return press;
	}
	public void setPress(String press) {
		this.press = press;
	}
	public String getScore() {
		return score;
	}
	public void setScore(String score) {
		this.score = score;
	}
	public String getPublishTime() {
		return publishTime;
	}
	public void setPublishTime(String publishTime) {
		this.publishTime = publishTime;
	}
	public String getPages() {
		return pages;
	}
	public void setPages(String pages) {
		this.pages = pages;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
}
