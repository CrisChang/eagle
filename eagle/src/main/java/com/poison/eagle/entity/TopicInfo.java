package com.poison.eagle.entity;

import com.keel.common.lang.BaseDO;
import com.poison.eagle.utils.DateUtil;
import com.poison.resource.model.Topic;
//话题
public class TopicInfo extends BaseDO implements Comparable<TopicInfo> {

	/**
	 * Topic序列号
	 */
	private static final long serialVersionUID = 8673081880339605169L;
	private long id;//话题id
	private String idstr;//话题id的字符窜类型
	private long userid;//创建者用户id
	private String nickname;//创建者用户昵称
	private String title;//话题标题
	private String cover;//话题封面
	private String tags;//话题标签
	private String description;//话题简介
	private long readcount;//阅读数量
	private long talkcount;//讨论数量
	private int isDelete;//是否删除了
	private long createDate;//创建时间
	private long latestRevisionDate;//最后修改时间
	private String createDateStr;
	private String latestRevisionDateStr;
	private int flag;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getUserid() {
		return userid;
	}

	public void setUserid(long userid) {
		this.userid = userid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(int isDelete) {
		this.isDelete = isDelete;
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

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}	
	public void setReadcount(long readcount) {
		this.readcount = readcount;
	}
	public long getReadcount() {
		return readcount;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public long getTalkcount() {
		return talkcount;
	}

	public void setTalkcount(long talkcount) {
		this.talkcount = talkcount;
	}
	
	public String getCreateDateStr() {
		return createDateStr;
	}

	public void setCreateDateStr(String createDateStr) {
		this.createDateStr = createDateStr;
	}

	public String getLatestRevisionDateStr() {
		return latestRevisionDateStr;
	}

	public void setLatestRevisionDateStr(String latestRevisionDateStr) {
		this.latestRevisionDateStr = latestRevisionDateStr;
	}

	public String getIdstr() {
		return idstr;
	}

	public void setIdstr(String idstr) {
		this.idstr = idstr;
	}

	public static TopicInfo copy(Topic topic){
		if(topic!=null){
			TopicInfo topicInfo = new TopicInfo();
			topicInfo.setCover(topic.getCover());
			topicInfo.setCreateDate(topic.getCreateDate());
			topicInfo.setDescription(topic.getDescription());
			topicInfo.setFlag(topic.getFlag());
			topicInfo.setId(topic.getId());
			topicInfo.setIdstr(topic.getId()+"");
			topicInfo.setIsDelete(topic.getIsDelete());
			topicInfo.setLatestRevisionDate(topic.getLatestRevisionDate());
			topicInfo.setReadcount(topic.getReadcount()+topic.getFalsereading());
			topicInfo.setTalkcount(topic.getTalkcount());
			topicInfo.setTags(topic.getTags());
			topicInfo.setTitle(topic.getTitle());
			topicInfo.setUserid(topic.getUserid());
			topicInfo.setCreateDateStr(DateUtil.format(topic.getCreateDate(),"yyyy-MM-dd HH:mm:ss"));
			topicInfo.setLatestRevisionDateStr(DateUtil.format(topic.getLatestRevisionDate(),"yyyy-MM-dd HH:mm:ss"));
			return topicInfo;
		}
		return null;
	}

	@Override
	public int compareTo(TopicInfo o) {
		if (o.id == this.id) {
			return 1;
		} else {
			return -1;
		}
	}
}
