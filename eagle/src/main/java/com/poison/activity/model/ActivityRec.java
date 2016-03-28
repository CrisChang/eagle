package com.poison.activity.model;

import java.io.Serializable;

import com.keel.common.lang.BaseDO;
/**
 * 推荐表（推荐参赛的用户和内容【参加影评大赛的用户、参赛的影评内容】）
 * @Description:
 * @author Weizhensong
 *
 */
public class ActivityRec extends BaseDO implements Comparable<ActivityRec>,Serializable{
	
	/**
	 * ActivityRec序列号
	 */
	private static final long serialVersionUID = 3685207267885351883L;
	private long id;				//主键
	private long resid;				//资源id
	private String restype;			//资源类型
	private int isdel;				//是否删除
	private long stageid;			//阶段id
	private int score;				//排序值，值越高越靠前
	private long createtime;				//保存时间
	private long updatetime;			//更新时间
	private int flag;//

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


	public String getRestype() {
		return restype;
	}


	public void setRestype(String restype) {
		this.restype = restype;
	}


	public int getIsdel() {
		return isdel;
	}


	public void setIsdel(int isdel) {
		this.isdel = isdel;
	}


	public long getStageid() {
		return stageid;
	}


	public void setStageid(long stageid) {
		this.stageid = stageid;
	}


	public int getScore() {
		return score;
	}


	public void setScore(int score) {
		this.score = score;
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


	public int getFlag() {
		return flag;
	}


	public void setFlag(int flag) {
		this.flag = flag;
	}


	@Override
	public int compareTo(ActivityRec o) {
		if(o.score>=this.score){
			return 1;
		}
		return -1;
	}
}
