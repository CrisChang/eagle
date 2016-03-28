package com.poison.story.model;

import java.util.Date;

import com.keel.common.lang.BaseDO;

/**
 * 小说运营记录实体，运营维护
 * @author songdan
 * @date 2016年2月25日 
 * @time 下午5:23:15
 * @version 1.0
 */
public class StoryPromote extends BaseDO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5033645212165073370L;

	/**运营记录id*/
	private Long id;
	
	/**小说id*/
	private Long storyId;
	
	/**推广语*/
	private String promoteWords;
	
	/**推广封面*/
	private String promoteCover;
	
	/**封面链接*/
	private String coverLink;
	
	/**排序字段*/
	private Integer rank;
	
	/**删除标识*/
	private int deleteFlag;
	
	/**运营类型 */
	private String type;
	
	/**运营记录创建人id*/
	private Long creatorId;
	
	/**记录创建人名称*/
	private String creatorName;
	
	/**记录创建时间*/
	private Date createTime;
	
	/**记录更新人id*/
	private Long updateId;
	
	/**更新人名称*/
	private String updateName;
	
	/**记录更新时间*/
	private Date updateTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getStoryId() {
		return storyId;
	}

	public void setStoryId(Long storyId) {
		this.storyId = storyId;
	}

	public String getPromoteWords() {
		return promoteWords;
	}

	public void setPromoteWords(String promoteWords) {
		this.promoteWords = promoteWords;
	}

	public String getPromoteCover() {
		return promoteCover;
	}

	public void setPromoteCover(String promoteCover) {
		this.promoteCover = promoteCover;
	}

	public String getCoverLink() {
		return coverLink;
	}

	public void setCoverLink(String coverLink) {
		this.coverLink = coverLink;
	}

	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}


	public int getDeleteFlag() {
		return deleteFlag;
	}

	public void setDeleteFlag(int deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Long getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(Long creatorId) {
		this.creatorId = creatorId;
	}

	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String createName) {
		this.creatorName = createName;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Long getUpdateId() {
		return updateId;
	}

	public void setUpdateId(Long updateId) {
		this.updateId = updateId;
	}

	public String getUpdateName() {
		return updateName;
	}

	public void setUpdateName(String updateName) {
		this.updateName = updateName;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	
	
}
