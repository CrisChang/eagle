package com.poison.store.model;

import com.keel.common.lang.BaseDO;
import com.poison.eagle.utils.CheckParams;

public class BkInfo extends BaseDO{

	/**
	 * BkInfo序列号
	 */
	private static final long serialVersionUID = -5954805930166809089L;

	
	private int id;
	private String bookUrl;
	private String bookPic;
	private String name;
	private String score;
	private String authorName;
	private String translator;
	private String press;
	private String originalName;
	private String subtitle;
	private String publishingTime;
	private int number;
	private String price;
	private String binding;
	private String seriesName;
	private String tags;
	private String content;
	private String authorInfo;
	private String catalog;
	private String seriesInfo;
	private String isbn;
	private int collTime;
	private String salesVolume;
	private String rankingList;
	private String resType = "27";
	
	
	public String getResType() {
		return resType;
	}
	public void setResType(String resType) {
		this.resType = resType;
	}
	public String getRankingList() {
		return rankingList;
	}
	public void setRankingList(String rankingList) {
		this.rankingList = CheckParams.formatStringForInfo(rankingList);
	}
	public String getSalesVolume() {
		return salesVolume;
	}
	public void setSalesVolume(String salesVolume) {
		this.salesVolume = CheckParams.formatStringForInfo(salesVolume);
	}
	private int flag;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	public String getBookUrl() {
		return bookUrl;
	}
	public void setBookUrl(String bookUrl) {
		this.bookUrl = CheckParams.formatStringForInfo(bookUrl);
	}
	public String getBookPic() {
		return bookPic;
	}
	public void setBookPic(String bookPic) {
		this.bookPic = CheckParams.formatStringForInfo(bookPic);
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = CheckParams.formatStringForInfo(name);
	}
	public String getScore() {
		return score;
	}
	public void setScore(String score) {
		this.score = CheckParams.formatStringForInfo(score);
	}
	public String getAuthorName() {
		return authorName;
	}
	public void setAuthorName(String authorName) {
		this.authorName = CheckParams.formatStringForInfo(authorName);
	}
	public String getTranslator() {
		return translator;
	}
	public void setTranslator(String translator) {
		this.translator = CheckParams.formatStringForInfo(translator);
	}
	public String getPress() {
		return press;
	}
	public void setPress(String press) {
		this.press = CheckParams.formatStringForInfo(press);
	}
	public String getOriginalName() {
		return originalName;
	}
	public void setOriginalName(String originalName) {
		this.originalName = CheckParams.formatStringForInfo(originalName);
	}
	public String getSubtitle() {
		return subtitle;
	}
	public void setSubtitle(String subtitle) {
		this.subtitle = CheckParams.formatStringForInfo(subtitle);
	}
	public String getPublishingTime() {
		return publishingTime;
	}
	public void setPublishingTime(String publishingTime) {
		this.publishingTime = CheckParams.formatStringForInfo(publishingTime);
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = CheckParams.formatStringForInfo(price);
	}
	public String getBinding() {
		return binding;
	}
	public void setBinding(String binding) {
		this.binding = CheckParams.formatStringForInfo(binding);
	}
	public String getSeriesName() {
		return seriesName;
	}
	public void setSeriesName(String seriesName) {
		this.seriesName = CheckParams.formatStringForInfo(seriesName);
	}
	public String getTags() {
		return tags;
	}
	public void setTags(String tags) {
		this.tags = CheckParams.formatStringForInfo(tags);
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = CheckParams.formatStringForInfo(content);
	}
	public String getAuthorInfo() {
		return authorInfo;
	}
	public void setAuthorInfo(String authorInfo) {
		this.authorInfo = CheckParams.formatStringForInfo(authorInfo);
	}
	public String getCatalog() {
		return catalog;
	}
	public void setCatalog(String catalog) {
		this.catalog = CheckParams.formatStringForInfo(catalog);
	}
	public String getSeriesInfo() {
		return seriesInfo;
	}
	public void setSeriesInfo(String seriesInfo) {
		this.seriesInfo = CheckParams.formatStringForInfo(seriesInfo);
	}
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public String getIsbn() {
		return isbn;
	}
	public void setIsbn(String isbn) {
		this.isbn = CheckParams.formatStringForInfo(isbn);
	}
	public int getCollTime() {
		return collTime;
	}
	public void setCollTime(int collTime) {
		this.collTime = collTime;
	}
	
}
