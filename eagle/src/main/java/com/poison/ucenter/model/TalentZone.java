package com.poison.ucenter.model;

import com.keel.common.lang.BaseDO;

public class TalentZone extends BaseDO{

	/**
	 * TalentZone序列号
	 */
	private static final long serialVersionUID = 4853552779387763228L;
	private long id;							//主键
	private String zoneName;				//圈子名称
	private String logoAddress;			//圈子头像地址
	private String zoneDescription;		//圈子描述
	private String other;						//其他信息
	private int isDel;							//是否删除0为未删除1为已删除
	private String type;						//圈子类型
	private String zoneSign;				//圈子个性签名
	private long createDate;				//创建时间
	private long latestRevisionDate;	//最后修改时间
	private int flag;							//标识符
	
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getZoneName() {
		return zoneName;
	}
	public void setZoneName(String zoneName) {
		this.zoneName = zoneName;
	}
	public String getLogoAddress() {
		return logoAddress;
	}
	public void setLogoAddress(String logoAddress) {
		this.logoAddress = logoAddress;
	}
	public String getZoneDescription() {
		return zoneDescription;
	}
	public void setZoneDescription(String zoneDescription) {
		this.zoneDescription = zoneDescription;
	}
	public String getOther() {
		return other;
	}
	public void setOther(String other) {
		this.other = other;
	}
	
	public int getIsDel() {
		return isDel;
	}
	public void setIsDel(int isDel) {
		this.isDel = isDel;
	}
	public String getZoneSign() {
		return zoneSign;
	}
	public void setZoneSign(String zoneSign) {
		this.zoneSign = zoneSign;
	}
	public long getCreateDate() {
		return createDate;
	}
	public void setCreateDate(long createDate) {
		this.createDate = createDate;
	}
	public long getLatestRevisionDate() {
		return latestRevisionDate;
	}
	public void setLatestRevisionDate(long latestRevisionDate) {
		this.latestRevisionDate = latestRevisionDate;
	}
	
	
}
