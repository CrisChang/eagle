package com.poison.product.model;

import com.keel.common.lang.BaseDO;

/**
 * 账户金币变动记录
 * @author weizhensong
 *
 */
public class AccGoldRecord extends BaseDO {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3752873237261286510L;
	/**
	 * 账户资金id
	 */
	private long id;
	/**
	 * 用户号ID
	 */
	private long userId;
	/**
	 * 变动金额
	 */
	private long tradeAmount;
	/**
	 * 当前余额
	 */
	private long restAmount;
	/**
	 * 订单号
	 */
	private String ordernum;
	/**
	 * 创建时间
	 */
	private long createtime;
	/**
	 * 修改时间
	 */
	private long updatetime;
	/**
	 * 对应的账户变动时间
	 */
	private long changetime;
	/**
	 * 说明
	 */
	private String remark;
	/**
	 * 金币变动序列号
	 */
	private long sequence;
	/**
	 * 资金变动类型
	 */
	private String type;
	/**
	 * 改变描述，app端展示
	 */
	private String changeDesc;
	/**
	 * 改变原有，后台展示
	 */
	private String cause;
	/**
	 * 如果是管理员强行改变了变动值，管理员是谁
	 */
	private String adminUser;
	/**
	 * 是否隐藏
	 */
	private int shadow;
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
	public long getChangetime() {
		return changetime;
	}
	public void setChangetime(long changetime) {
		this.changetime = changetime;
	}
	public long getTradeAmount() {
		return tradeAmount;
	}
	public void setTradeAmount(long tradeAmount) {
		this.tradeAmount = tradeAmount;
	}
	public long getRestAmount() {
		return restAmount;
	}
	public void setRestAmount(long restAmount) {
		this.restAmount = restAmount;
	}
	public String getOrdernum() {
		return ordernum;
	}
	public void setOrdernum(String ordernum) {
		this.ordernum = ordernum;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public long getSequence() {
		return sequence;
	}
	public void setSequence(long sequence) {
		this.sequence = sequence;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getChangeDesc() {
		return changeDesc;
	}
	public void setChangeDesc(String changeDesc) {
		this.changeDesc = changeDesc;
	}
	public String getCause() {
		return cause;
	}
	public void setCause(String cause) {
		this.cause = cause;
	}
	public String getAdminUser() {
		return adminUser;
	}
	public void setAdminUser(String adminUser) {
		this.adminUser = adminUser;
	}
	public int getShadow() {
		return shadow;
	}
	public void setShadow(int shadow) {
		this.shadow = shadow;
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
