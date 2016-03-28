package com.poison.resource.model;

import com.keel.common.lang.BaseDO;
//话题
public class Topic extends BaseDO implements Comparable<Topic> {

	/**
	 * Topic序列号
	 */
	private static final long serialVersionUID = 8673081880339605169L;
	private long id;
	private long userid;
	private String title;
	private String cover;
	private String tags;
	private String description;
	private long readcount;
	private long falsereading;
	private int isDelete;
	private long createDate;
	private long latestRevisionDate;
	private int score;//话题排行分值(热门)
	private int allscore;//话题排行分值（全部）
	
	private long talkcount;//讨论数量
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
	public long getFalsereading() {
		return falsereading;
	}
	public void setFalsereading(long falsereading) {
		this.falsereading = falsereading;
	}	
	public long getTalkcount() {
		return talkcount;
	}
	public void setTalkcount(long talkcount) {
		this.talkcount = talkcount;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public int getAllscore() {
		return allscore;
	}
	public void setAllscore(int allscore) {
		this.allscore = allscore;
	}

	@Override
	public int compareTo(Topic o) {
		if (o.id == this.id) {
			return 1;
		} else {
			return -1;
		}
	}

}
