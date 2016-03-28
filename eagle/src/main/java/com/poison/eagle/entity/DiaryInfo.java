package com.poison.eagle.entity;

import com.keel.common.lang.BaseDO;

/**
 * 电影详情花絮返回结果
 * 
 * @author :zhangqi
 * @time:2015-6-17下午4:07:59
 * @return
 */
public class DiaryInfo extends BaseDO {

	private static final long serialVersionUID = 1L;

	// private long id;// 主键id 文字ID
	// private long commentNum; // 评论数
	// private long praiseNum;// 点赞数
	// private String type;// 选择类型
	private String content;// 内容

	// public long getId() {
	// return id;
	// }
	//
	// public void setId(long id) {
	// this.id = id;
	// }

	// public long getCommentNum() {
	// return commentNum;
	// }
	//
	// public void setCommentNum(long commentNum) {
	// this.commentNum = commentNum;
	// }
	//
	// public long getPraiseNum() {
	// return praiseNum;
	// }
	//
	// public void setPraiseNum(long praiseNum) {
	// this.praiseNum = praiseNum;
	// }

	// public String getType() {
	// return type;
	// }
	//
	// public void setType(String type) {
	// this.type = type;
	// }

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
