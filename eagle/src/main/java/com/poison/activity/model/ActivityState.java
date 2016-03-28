package com.poison.activity.model;

import java.io.Serializable;

import com.keel.common.lang.BaseDO;
/**
 * 活动报名状态
 * @Description:
 * @author Weizhensong
 *
 */
public class ActivityState extends BaseDO implements Comparable<ActivityState>,Serializable{
	public static final int state_match=1;//参赛状态
	public static final int state_init = 0;//未参赛状态
	/**
	 * ActivityState序列号
	 */
	private static final long serialVersionUID = 3685207277875351883L;
	private long userid;				//主键(用户id)
	private int state;				//报名状态0未报名，1报名
	private long stageid;			//阶段id
	private long createtime;				//保存时间
	private long updatetime;			//更新时间
	private int flag;//

	public long getUserid() {
		return userid;
	}
	public void setUserid(long userid) {
		this.userid = userid;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public long getStageid() {
		return stageid;
	}
	public void setStageid(long stageid) {
		this.stageid = stageid;
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
	public int compareTo(ActivityState o) {
		if(o.state>=this.state){
			return 1;
		}
		return -1;
	}
}
