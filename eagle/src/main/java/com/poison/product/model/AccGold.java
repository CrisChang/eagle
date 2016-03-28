package com.poison.product.model;

import com.keel.common.lang.BaseDO;

/**
 * 账户金币
 * @author weizhensong
 *
 */
public class AccGold extends BaseDO {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3752873237260286510L;
	/**
	 * 用户号ID
	 */
	private long userId;
	/**
	 * 账户余额
	 */
	private long goldamount;
	/**
	 * 创建时间
	 */
	private long createtime;
	/**
	 * 修改时间
	 */
	private long changetime;
	/**
	 * 变动序列号（次数）
	 */
	private long sequence;
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
	public long getGoldamount() {
		return goldamount;
	}
	public void setGoldamount(long goldamount) {
		this.goldamount = goldamount;
	}
	public long getCreatetime() {
		return createtime;
	}
	public void setCreatetime(long createtime) {
		this.createtime = createtime;
	}
	public long getChangetime() {
		return changetime;
	}
	public void setChangetime(long changetime) {
		this.changetime = changetime;
	}
	public long getSequence() {
		return sequence;
	}
	public void setSequence(long sequence) {
		this.sequence = sequence;
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
	
}
