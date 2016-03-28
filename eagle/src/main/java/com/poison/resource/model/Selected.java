package com.poison.resource.model;

import com.keel.common.lang.BaseDO;

public class Selected extends BaseDO implements Comparable<Selected>{
	/**
	 * Selected序列号
	 */
	private static final long serialVersionUID = -1011686277869123967L;
	private long id;//主鍵id
	private long resid;//资源id
	private String type;//资源类型
	private long score;//精选分
	private int isdel;//0表示删除，1表示未删除
	private long createtime;//创建时间
	private long updatetime;//更新时间
	private String info;//精选说明
	private String image;//图片
	private long userid;//所属推荐用户id
	private long topshow;//置顶值
	
	
	private int flag;//标示
	
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getResid() {
		return resid;
	}

	public void setResid(long resid) {
		this.resid = resid;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public long getScore() {
		return score;
	}

	public void setScore(long score) {
		this.score = score;
	}

	public int getIsdel() {
		return isdel;
	}

	public void setIsdel(int isdel) {
		this.isdel = isdel;
	}

	public long getCreatetime() {
		return createtime;
	}

	public void setCreatetime(long createtime) {
		this.createtime = createtime;
	}
	public long getUpdatetime() {
		return updatetime;
	}
	public void setUpdatetime(long updatetime) {
		this.updatetime = updatetime;
	}
	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
	public long getUserid() {
		return userid;
	}
	public void setUserid(long userid) {
		this.userid = userid;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public long getTopshow() {
		return topshow;
	}

	public void setTopshow(long topshow) {
		this.topshow = topshow;
	}

	@Override
	public int compareTo(Selected s) {
		if(s.score==this.score){
			return 1;
		}
		return -1;
	}
}
