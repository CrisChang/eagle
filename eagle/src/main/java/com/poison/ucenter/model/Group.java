package com.poison.ucenter.model;

import java.util.List;

import com.keel.common.lang.BaseDO;
import com.poison.eagle.entity.UserEntity;

public class Group extends BaseDO{

	/**
	 * Group序列号
	 */
	private static final long serialVersionUID = 9032857901301987296L;
	private String groupid;		//群组id
	private long uid;			//用户ID
	private String name;		//群组名
	private String imageurl;	//群组头像
	private String tags;		//群标签
	private int amount;			//群成员最大数量限制
	private int isDel;			//删除标志
	private String intro;		//群简介
	private long creattime;		//创建时间
	private long updatetime;	//最后修改时间
	private int flag;			//判断标示
	private List<UserEntity> members;//群成员集合
	private List<UserEntity> blacklist;//群黑名单集合
	public String getGroupid() {
		return groupid;
	}
	public void setGroupid(String groupid) {
		this.groupid = groupid;
	}
	public long getUid() {
		return uid;
	}
	public void setUid(long uid) {
		this.uid = uid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getImageurl() {
		return imageurl;
	}
	public void setImageurl(String imageurl) {
		this.imageurl = imageurl;
	}
	public String getTags() {
		return tags;
	}
	public void setTags(String tags) {
		this.tags = tags;
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public int getIsDel() {
		return isDel;
	}
	public void setIsDel(int isDel) {
		this.isDel = isDel;
	}
	public String getIntro() {
		return intro;
	}
	public void setIntro(String intro) {
		this.intro = intro;
	}
	public long getCreattime() {
		return creattime;
	}
	public void setCreattime(long creattime) {
		this.creattime = creattime;
	}
	public long getUpdatetime() {
		return updatetime;
	}
	public void setUpdatetime(long updatetime) {
		this.updatetime = updatetime;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	public List<UserEntity> getMembers() {
		return members;
	}
	public void setMembers(List<UserEntity> members) {
		this.members = members;
	}
	public List<UserEntity> getBlacklist() {
		return blacklist;
	}
	public void setBlacklist(List<UserEntity> blacklist) {
		this.blacklist = blacklist;
	}
}
