package com.poison.eagle.entity;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.keel.common.lang.BaseDO;
import com.poison.eagle.utils.CheckParams;

public class MovieListInfo extends BaseDO implements Comparable<MovieListInfo>,Serializable {
	/**
	 * serialVersionUID = -7600519539377008853L;
	 */
	private static final long serialVersionUID = -7600519539377008853L;
	private long id;
	private String idStr;
	private String name;//影单名
	private String firstMoviePic;//影单的封面，1.当用户自定义封面后，显示自定义封面、2.用户没有自定义，选用影单中第一电影的封面、3.用户没有自定义，影单中没有电影，用默认封面
	private List<String> moviePicList;//已废弃
	private List<MovieInfo> list;//影单中电影的集合
	private List<String> tags;//标签集合
	private Map<String, String> tagInfo;//带有分类的新的标签集合
	private String reason;//简介
	private int isPublish;//是否推送
	private int isDefault;//0:默认、1:自定义、2：收藏的
	private int isCollect = 1;//0：收藏、1：没有收藏
	private int cNum;//评论数量
	private int rNum;//转发数量
	private int zNum;//赞的数量
	private int isPraise = 1;//0:赞过,1:没赞过
	private int size;//影单中电影数量
	private int money;
	private int usefulCount;
	private int uselessCount;
	private String type;
	private UserEntity userEntity;//创建用户的实例类

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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public UserEntity getUserEntity() {
		return userEntity;
	}
	public void setUserEntity(UserEntity userEntity) {
		this.userEntity = userEntity;
	}
	public int getIsCollect() {
		return isCollect;
	}
	public int getMoney() {
		return money;
	}
	public void setMoney(int money) {
		this.money = money;
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
	public List<String> getMoviePicList() {
		return moviePicList;
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
	public void setMoviePicList(List<String> moviePicList) {
		this.moviePicList = moviePicList;
	}
	public List<MovieInfo> getList() {
		return list;
	}
	public void setList(List<MovieInfo> list) {
		this.list = list;
	}
	public String getFirstMoviePic() {
		return firstMoviePic;
	}
	public void setFirstMoviePic(String firstMoviePic) {
		firstMoviePic = CheckParams.matcherMoviePic(firstMoviePic);
		this.firstMoviePic = firstMoviePic;
	}
	public List<String> getTags() {
		return tags;
	}
	public void setTags(String tag) {
		this.tags = CheckParams.getListFromString(tag);
	}
	public Map<String, String> getTagInfo() {
		return tagInfo;
	}
	public void setTagInfo(Map<String, String> tagInfo) {
		this.tagInfo = tagInfo;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		
		this.reason = reason;
	}
	public int getIsPublish() {
		return isPublish;
	}
	public void setIsPublish(int isPublish) {
		this.isPublish = isPublish;
	}
	public int getIsDefault() {
		return isDefault;
	}
	public void setIsDefault(int isDefault) {
		this.isDefault = isDefault;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public int compareTo(MovieListInfo o) {
		if(o.size>=this.size){
			return 1;
		}
		return -1;
	}
}
