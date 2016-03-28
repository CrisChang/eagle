package com.poison.paycenter.model;

import com.keel.common.lang.BaseDO;

/**
 * 用户与支付宝绑定模型
 * @author wei
 *
 */
public class PayBind extends BaseDO {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2514459686907292887L;
	/**
	 * 绑定记录id
	 */
	private long id;
	/**
	 * 用户号ID
	 */
	private long userId;
	/**
	 * 用户登录号
	 */
	private String userName;
	/**
	 * 绑定的支付宝账号ID
	 */
	private String payId;
	/**
	 * 绑定的支付宝账号
	 */
	private String payEmail;
	/**
	 * 绑定的账号的开户名
	 */
	private String payName;
	/**
	 * 支付类型
	 */
	private int paymentType;
	/**
	 * 绑定时间
	 */
	private long bindTime;
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

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPayId() {
		return payId;
	}

	public void setPayId(String payId) {
		this.payId = payId;
	}

	public String getPayEmail() {
		return payEmail;
	}

	public void setPayEmail(String payEmail) {
		this.payEmail = payEmail;
	}

	public String getPayName() {
		return payName;
	}

	public void setPayName(String payName) {
		this.payName = payName;
	}

	public int getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(int paymentType) {
		this.paymentType = paymentType;
	}

	public long getBindTime() {
		return bindTime;
	}

	public void setBindTime(long bindTime) {
		this.bindTime = bindTime;
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
