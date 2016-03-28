package com.poison.resource.model;

import java.io.Serializable;

import com.keel.common.lang.BaseDO;

/**
 * 原创榜，上周回幕榜
 * 
 * @author zhangqi
 * 
 */
public class RankingList extends BaseDO implements Serializable {

	private static final long serialVersionUID = -1011686277869123967L;
	private long id;// 主鍵id
	private long resid;// 资源id
	private String restype;// 资源类型
	private String type;// 排行榜类型
	private long score;// 精选分
	private int isdel;// 0表示删除，1表示未删除
	private long createtime;// 创建时间
	private long updatetime;// 更新时间
	private String title;// 标题
	private String cover;// 图片
	private long userid;// 所属推荐用户id
	private long topshow;// 置顶

	private int flag;// 标示

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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
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

	public String getRestype() {
		return restype;
	}

	public void setRestype(String restype) {
		this.restype = restype;
	}

}
