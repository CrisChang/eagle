package com.poison.eagle.entity;

import java.util.List;

import com.keel.common.lang.BaseDO;
import com.poison.eagle.utils.CheckParams;
import com.poison.eagle.utils.StringUtils;
/**
 * 书 实例类是毒药APP中承载图书信息的java文件，使用详细规则如下：
 * 1.当返回书为列表时，具体字段为：id,name,pagePic,authorName
 * 	   当返回书为实体类时，返回全部字段
 *   resourceInfo 在返回图书详情时会返回，最为当前用户的评论资源
 * @author 温晓宁
 *
 */
public class BookInfo extends BaseDO implements Comparable<BookInfo> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5571647997495528344L;
	private int id;
	private String name;//书名
	private String pagePic;//封面
	private String authorName;//作者
	private String press;//出版社
	private String score;//评分
	private String publishTime;//出版日期
	private String introduction;//简介
	private String rank;//同期排名
	private String sales;//销量
	private int pages;//页数
	private String price;//价格
	private String binding;//装订
	private String catalog;//目录
	private String isbn13;//isbn
	private String tags;//标签
	private String type;//资源类型  图书：27，网络小说：29
	private ResourceInfo resourceInfo;//我的评论
	private List<ResourceInfo> commentList;//评论列表
	private int cNum = 0;//评论数目
//	private int isDB;//是否在数据库中（已废弃）
	private int inList = 1;//0:在书单中，1：没有
	private Long sortId;//排序使用字段，在运营页面等需要特殊排序的地方需要依靠该字段进行转换过渡，例如：网络小说的排序
	private int talentCommentNum;//专家评论数量
	private String talentScore;//专家评分
	private String searchNum;//搜索量
	private String hotNum;//热度
	
	public int getTalentCommentNum() {
		return talentCommentNum;
	}
	public void setTalentCommentNum(int talentCommentNum) {
		this.talentCommentNum = talentCommentNum;
	}
	public String getTalentScore() {
		return talentScore;
	}
	public void setTalentScore(String talentScore) {
		this.talentScore = talentScore;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public ResourceInfo getResourceInfo() {
		return resourceInfo;
	}
	public void setResourceInfo(ResourceInfo resourceInfo) {
		this.resourceInfo = resourceInfo;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		name = CheckParams.replaceKG(name);
		this.name = name;
	}
	public int getcNum() {
		return cNum;
	}
	public int getInList() {
		return inList;
	}
	public void setInList(int inList) {
		this.inList = inList;
	}
	public String getTags() {
		return tags;
	}
	public String getRank() {
		return rank;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public void setRank(String rank) {
		this.rank = rank;
	}
	public Long getSortId() {
		return sortId;
	}
	public void setSortId(Long sortId) {
		this.sortId = sortId;
	}
	public String getSales() {
		return sales;
	}
	public void setSales(String sales) {
		this.sales = sales;
	}
	public void setTags(String tags) {
		this.tags = tags;
	}
	public void setcNum(int cNum) {
		this.cNum = cNum;
	}
	public String getPagePic() {
		return pagePic;
	}
	public void setPagePic(String pagePic) {
		this.pagePic = CheckParams.matcherBookPic(pagePic);
	}
	public String getAuthorName() {
		return authorName;
	}
	public void setAuthorName(String authorName) {
		authorName = CheckParams.replaceKG(authorName);
		authorName = StringUtils.getFirstAuthorName(authorName);
		this.authorName = authorName;
	}
	public String getPress() {
		return press;
	}
	public List<ResourceInfo> getCommentList() {
		return commentList;
	}
	public void setCommentList(List<ResourceInfo> commentList) {
		this.commentList = commentList;
	}
	public void setPress(String press) {
		press = CheckParams.replaceKG(press);
		this.press = press;
	}
	public String getScore() {
		return score;
	}
	public void setScore(String score) {
		this.score = CheckParams.getScore(score);
	}
	public String getIntroduction() {
		return introduction;
	}
	public void setIntroduction(String introduction) {
		introduction = CheckParams.putDescribeToHTML(introduction);
		if(introduction != null){
			introduction = CheckParams.replaceKG(introduction);
			if(introduction.equals("0")){
				introduction = "";
			}
		}else{
			introduction = "";
		}
		this.introduction = introduction;
	}
	
	public int getPages() {
		return pages;
	}
	public void setPages(int pages) {
		this.pages = pages;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = CheckParams.replaceKG(price);
	}
	public String getBinding() {
		return binding;
	}
	public void setBinding(String binding) {
		this.binding = CheckParams.replaceKG(binding);
	}
	public String getCatalog() {
		return catalog;
	}
	public void setCatalog(String catalog) {
		catalog = CheckParams.putDescribeToHTML(catalog);
		this.catalog = CheckParams.replaceKG(catalog);
	}
	public String getIsbn13() {
		return isbn13;
	}
	public void setIsbn13(String isbn13) {
		this.isbn13 = isbn13;
	}
	public String getPublishTime() {
		return publishTime;
	}
	public void setPublishTime(String publishTime) {
		publishTime = CheckParams.replaceKG(publishTime);
		this.publishTime = publishTime;
	}
	public String getSearchNum() {
		return searchNum;
	}
	public void setSearchNum(String searchNum) {
		this.searchNum = searchNum;
	}
	public String getHotNum() {
		return hotNum;
	}
	public void setHotNum(String hotNum) {
		this.hotNum = hotNum;
	}
	@Override
	public int compareTo(BookInfo o) {
		if(o.id>=this.id){
			return 1;
		}
		return -1;
	}
	
}
