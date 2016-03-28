package com.poison.resource.model;

import com.keel.common.lang.BaseDO;

public class SerializeList extends BaseDO{

	/**
	 * SerializeList序列号
	 */
	private static final long serialVersionUID = -7693007061755047423L;
	private long id; // 主键id
	private long uId;//用户主键
	private int type; // 0表示默认连载单，1表示自创书单
	private String tag;//连载清单标签
	private int isPublishing;//是否发布0为未发布，1为已发布
	private String serializeListName; // 书单的名称
	private String reason; // 推荐书单和书的理由
	private long createDate; // 创建书单的时间
	private long latestRevisionDate;//最后一次修改书单的时间
	private int isDel; // 0表示该数据没被删除，1表示数据删除
	private int flag;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getuId() {
		return uId;
	}
	public void setuId(long uId) {
		this.uId = uId;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public int getIsPublishing() {
		return isPublishing;
	}
	public void setIsPublishing(int isPublishing) {
		this.isPublishing = isPublishing;
	}
	public String getSerializeListName() {
		return serializeListName;
	}
	public void setSerializeListName(String serializeListName) {
		this.serializeListName = serializeListName;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
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
	public int getIsDel() {
		return isDel;
	}
	public void setIsDel(int isDel) {
		this.isDel = isDel;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	
	
}
