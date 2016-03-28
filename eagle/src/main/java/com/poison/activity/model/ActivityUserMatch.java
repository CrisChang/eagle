package com.poison.activity.model;

import com.keel.common.lang.BaseDO;
/**
 * 影评大赛用户参赛情况
 * @Description:	
 * @author Weizhensong
 *
 */
public class ActivityUserMatch extends BaseDO{

	/**
	 * ActivityUserMatch序列号
	 */
	private static final long serialVersionUID = 3678287277895351883L;
	private long userid;				//用户id
	private long mvcount;			//影评数量
	private long usecount;			//有用数量
	private int maxpoint;			//影评的最高分
	private long stageid;			//阶段id
	private long resid;				//资源id
	private int flag;
	
	public long getUserid() {
		return userid;
	}
	public void setUserid(long userid) {
		this.userid = userid;
	}
	public long getMvcount() {
		return mvcount;
	}
	public void setMvcount(long mvcount) {
		this.mvcount = mvcount;
	}
	public long getUsecount() {
		return usecount;
	}
	public void setUsecount(long usecount) {
		this.usecount = usecount;
	}
	public int getMaxpoint() {
		return maxpoint;
	}
	public void setMaxpoint(int maxpoint) {
		this.maxpoint = maxpoint;
	}
	public long getStageid() {
		return stageid;
	}
	public void setStageid(long stageid) {
		this.stageid = stageid;
	}
	public long getResid() {
		return resid;
	}
	public void setResid(long resid) {
		this.resid = resid;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
}
