package com.poison.ucenter.model;

import com.keel.common.lang.BaseDO;

public class ShenrenApply extends BaseDO{

	/**
	 * ShenrenApply序列号
	 */
	private static final long serialVersionUID = 9032857901161987296L;
	private long id;		//id
	private long uid;			//用户ID
	private String realname;		//真实姓名
	private String content;			//认证内容
	private String mobileno;		//手机号码
	private String sid;				//身份证
	private String proof;			//证明材料
	private long applytime;		//申请时间
	private long updatetime;	//最后修改时间
	private int isDel;			//删除标志
	private int status;			//申请状态
	
	private int flag;			//判断标示

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getUid() {
		return uid;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getMobileno() {
		return mobileno;
	}

	public void setMobileno(String mobileno) {
		this.mobileno = mobileno;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public String getProof() {
		return proof;
	}

	public void setProof(String proof) {
		this.proof = proof;
	}

	public long getApplytime() {
		return applytime;
	}

	public void setApplytime(long applytime) {
		this.applytime = applytime;
	}

	public long getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(long updatetime) {
		this.updatetime = updatetime;
	}

	public int getIsDel() {
		return isDel;
	}

	public void setIsDel(int isDel) {
		this.isDel = isDel;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}
}
