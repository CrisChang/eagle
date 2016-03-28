package com.poison.eagle.entity;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.keel.common.lang.BaseDO;
import com.poison.eagle.utils.CheckParams;
/**
 * 书单的封面，在保存书或删除书时，会实时更新
 * 在书单详情的接口中，封面地址使用fristBookPicUrl字段，在其他资源类涉及书单时封面用ResourceInfo.imageUrl字段。
 * @author 温晓宁
 *
 */
public class BookListInfo extends BaseDO implements Comparable<BookListInfo>,Serializable {
	/**
	 * serialVersionUID = 9186268066343395099L;
	 */
	private static final long serialVersionUID = 9186268066343395099L;
	private long id;
	private String idStr;
	private String name;//书单名
	private int size = 0;//书单中书的数量
	private int isDefault;//0:默认、1：自定义、2：收藏的
	private int isCollect = 1;//0：收藏、1：没有收藏
	private int cNum;//评论数量
	private int rNum;//转发数量
	private int zNum;//赞的数量
	private int isPraise = 1;//0:赞过,1:没赞过
	private String fristBookPicUrl;//书单的封面，1.当用户自定义封面后，显示自定义封面、2.用户没有自定义，选用书单中第一本书的封面、3.用户没有自定义，书单中没有书，用默认封面
	private List<String> tags;//标签集合
	private Map<String, String> tagInfo;//带有分类的标签集合;
	private List<String> bookPicList;//书单封面集合，（已废弃）
	private List<BookInfo> list;//书单中书的集合
	private String reason;//推荐理由
	private int money;
	private int usefulCount;
	private int uselessCount;
	private List<ResourceInfo> resourceList;//在最新的书单详情中，每本书用资源类来格式化
	private UserEntity userEntity;//创建用户实例类


	public int getUsefulCount() {
		return usefulCount;
	}

	public void setUsefulCount(int usefulCount) {
		this.usefulCount = usefulCount;
	}

	public int getUselessCount() {
		return uselessCount;
	}

	public void setUselessCount(int uselessCount) {
		this.uselessCount = uselessCount;
	}

	public String getIdStr() {
		return idStr;
	}

	public void setIdStr(String idStr) {
		this.idStr = id+"";
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
		this.idStr = id+"";
	}


	public List<String> getBookPicList() {
		return bookPicList;
	}


	public List<String> getTags() {
		return tags;
	}

	public void setTags(String tag) {
		this.tags = CheckParams.getListFromString(tag);
	}

	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}

	public int getzNum() {
		return zNum;
	}

	public void setzNum(int zNum) {
		this.zNum = zNum;
	}

	public int getIsPraise() {
		return isPraise;
	}

	public void setIsPraise(int isPraise) {
		this.isPraise = isPraise;
	}

	public List<BookInfo> getList() {
		return list;
	}

	public void setList(List<BookInfo> list) {
		this.list = list;
	}

	public void setBookPicList(List<String> bookPicList) {
		this.bookPicList = bookPicList;
	}


	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public int getIsDefault() {
		return isDefault;
	}

	public int getIsCollect() {
		return isCollect;
	}

	public void setIsCollect(int isCollect) {
		this.isCollect = isCollect;
	}

	public int getcNum() {
		return cNum;
	}

	public void setcNum(int cNum) {
		this.cNum = cNum;
	}

	public int getrNum() {
		return rNum;
	}

	public void setrNum(int rNum) {
		this.rNum = rNum;
	}

	public void setIsDefault(int isDefault) {
		this.isDefault = isDefault;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public List<ResourceInfo> getResourceList() {
		return resourceList;
	}
	public int getSize() {
		return size;
	}

	public UserEntity getUserEntity() {
		return userEntity;
	}

	public void setUserEntity(UserEntity userEntity) {
		this.userEntity = userEntity;
	}

	public Map<String, String> getTagInfo() {
		return tagInfo;
	}

	public void setTagInfo(Map<String, String> tagInfo) {
		this.tagInfo = tagInfo;
	}

	public String getFristBookPicUrl() {
		return fristBookPicUrl;
	}

	public void setFristBookPicUrl(String fristBookPicUrl) {
		fristBookPicUrl = CheckParams.matcherBookPic(fristBookPicUrl);
		this.fristBookPicUrl = fristBookPicUrl;
	}

	public void setSize(int size) {
		this.size = size;
	}

	/**
	 * @param resourceList
	 */
	public void setResourceList(List<ResourceInfo> resourceList) {
		this.resourceList = resourceList;
	}

	@Override
	public int compareTo(BookListInfo o) { 
		if (o.size >= this.size) {
			return 1;
		}
		return -1;
	}
}
