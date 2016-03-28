package com.poison.eagle.entity;

import com.keel.common.lang.BaseDO;

/**
 * 排条榜（原创榜，上周回幕榜）
 * 
 * @author zhangqi
 * 
 */
public class RankingListInfo extends BaseDO {

	private static final long serialVersionUID = 1L;
	private String id;// 主键ID
	private String resid;// 资源id
	private String restype;// 资源类型
	private String type;// 排行榜类型
	private String score;// 精选分
	private String createtime;// 创建时间
	private String updatetime;// 更新时间
	private String title;// 标题
	private String cover;// 图片
	private String userid;// 所属推荐用户id
	private String topshow;// 置顶

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getResid() {
		return resid;
	}

	public void setResid(String resid) {
		this.resid = resid;
	}

	public String getRestype() {
		return restype;
	}

	public void setRestype(String restype) {
		this.restype = restype;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public String getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(String updatetime) {
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

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getTopshow() {
		return topshow;
	}

	public void setTopshow(String topshow) {
		this.topshow = topshow;
	}

}
