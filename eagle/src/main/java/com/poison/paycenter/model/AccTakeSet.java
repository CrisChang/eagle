package com.poison.paycenter.model;

import com.keel.common.lang.BaseDO;

/**
 * 用户账户提现设置(包括提现密码设置等等)
 * @author wei
 *
 */
public class AccTakeSet extends BaseDO {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2514459696907292887L;
	/**
	 * 用户号ID
	 */
	private long userId;
	/**
	 * 提现密码
	 */
	private String password;
	/**
	 * 创建时间
	 */
	private long createTime;
	/**
	 * 修改时间
	 */
	private long updateTime;
	/**
	 * 输错密码信息
	 */
	private String wronginfo;
	/**
	 * 扩展字段1
	 */
	private String extendField1;
	/**
	 * 扩展字段2
	 */
	private String extendField2;
	/**
	 * 扩展字段3
	 */
	private String extendField3;
	
	private int flag;

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getExtendField1() {
		return extendField1;
	}

	public void setExtendField1(String extendField1) {
		this.extendField1 = extendField1;
	}

	public String getExtendField2() {
		return extendField2;
	}

	public void setExtendField2(String extendField2) {
		this.extendField2 = extendField2;
	}

	public String getExtendField3() {
		return extendField3;
	}

	public void setExtendField3(String extendField3) {
		this.extendField3 = extendField3;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}

	public String getWronginfo() {
		return wronginfo;
	}

	public void setWronginfo(String wronginfo) {
		this.wronginfo = wronginfo;
	}
}
