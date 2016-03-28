package com.poison.paycenter.model;

import com.keel.common.lang.BaseDO;

/**
 * 打赏详细模型
 * @author yan_dz
 *
 */
public class RewardDetail extends BaseDO {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4059951071136955966L;
	/**
	 * 打赏详细id
	 */
	private long id;
	/**
	 * 充值订单号
	 */
	private String outTradeNo;
	/**
	 * 打赏人用户ID
	 */
	private long sendUserId;	
	/**
	 * 打赏人用户名
	 */
	private String sendUserName;	
	/**
	 * 打赏公司ID
	 */
	private long companyId;	
	/**
	 * 打赏公司名称
	 */
	private String companyName;
	/**
	 * 打赏金额
	 */
	private int sendAmt;
	/**
	 * 被打赏人用户ID
	 */
	private long receiveUserId;	
	/**
	 * 被打赏人用户名
	 */
	private String receiveUserName;
	/**
	 * 打赏留言
	 */
	private String postscript;
	/**
	 * 打赏时间
	 */
	private long sendTime;
	/**
	 * 打赏状态
	 */
	private int sendStatus;	
	/**
	 * 资源ID
	 */
	private long sourceId;
	/**
	 * 资源名称
	 */
	private String sourceName;
	/**
	 * 资源种类
	 */
	private int sourceType;
	/**
	 * 打赏种类
	 */
	private int rewardType;
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
	public long getSendUserId() {
		return sendUserId;
	}
	public void setSendUserId(long sendUserId) {
		this.sendUserId = sendUserId;
	}
	public String getSendUserName() {
		return sendUserName;
	}
	public void setSendUserName(String sendUserName) {
		this.sendUserName = sendUserName;
	}
	public int getSendAmt() {
		return sendAmt;
	}
	public void setSendAmt(int sendAmt) {
		this.sendAmt = sendAmt;
	}
	public long getReceiveUserId() {
		return receiveUserId;
	}
	public void setReceiveUserId(long receiveUserId) {
		this.receiveUserId = receiveUserId;
	}
	public String getReceiveUserName() {
		return receiveUserName;
	}
	public void setReceiveUserName(String receiveUserName) {
		this.receiveUserName = receiveUserName;
	}
	public String getPostscript() {
		return postscript;
	}
	public void setPostscript(String postscript) {
		this.postscript = postscript;
	}
	public long getSendTime() {
		return sendTime;
	}
	public void setSendTime(long sendTime) {
		this.sendTime = sendTime;
	}
	public int getSendStatus() {
		return sendStatus;
	}
	public void setSendStatus(int sendStatus) {
		this.sendStatus = sendStatus;
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
	public String getOutTradeNo() {
		return outTradeNo;
	}
	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}	
	public int getRewardType() {
		return rewardType;
	}
	public void setRewardType(int rewardType) {
		this.rewardType = rewardType;
	}
	public long getCompanyId() {
		return companyId;
	}
	public void setCompanyId(long companyId) {
		this.companyId = companyId;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public long getSourceId() {
		return sourceId;
	}
	public void setSourceId(long sourceId) {
		this.sourceId = sourceId;
	}
	public String getSourceName() {
		return sourceName;
	}
	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}
	public int getSourceType() {
		return sourceType;
	}
	public void setSourceType(int sourceType) {
		this.sourceType = sourceType;
	}	
}
