package com.poison.eagle.entity;

import com.keel.common.lang.BaseDO;

/**
 * 电影资讯do 供前端显示时使用
 * 
 * @author :zhangqi
 * @time:2015-6-16下午12:09:55
 * @return
 */
public class ArticleInfo extends BaseDO {

	private static final long serialVersionUID = 1L;

	private String articleType;
	private String acticlePic;
	private String content;// 内容
	private String title;// 标题
	private String summary;// 摘要

	private long typeId;

	public String getArticleType() {
		return articleType;
	}

	public void setArticleType(String articleType) {
		this.articleType = articleType;
	}

	public String getActiclePic() {
		return acticlePic;
	}

	public void setActiclePic(String acticlePic) {
		this.acticlePic = acticlePic;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public long getTypeId() {
		return typeId;
	}

	public void setTypeId(long typeId) {
		this.typeId = typeId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

}
