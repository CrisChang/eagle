package com.poison.story.model;

import java.util.Date;

import org.springframework.web.context.request.NativeWebRequest;

import com.keel.common.lang.BaseDO;

/**
 * 小说评论实体
 * @author songdan
 * @date 2016-3-1
 * @Description TODO
 * @version V1.0
 */
public class StoryComment extends BaseDO{
    /**
	 * 
	 */
	private static final long serialVersionUID = 4097038463957824836L;
	/**主键*/
	private Long id;

	/**小说id*/
    private Long storyId;

    /**评论人id*/
    private Long userId;

    /**评论人昵称*/
    private String nickname;

    /**小说名称*/
    private String storyName;

    /**小说作者id*/
    private Long authorId;

    /**小说作者名称*/
    private String storyAuthor;

    /**小说封面*/
    private String storyCover;

    /**小说打分*/
    private Byte score;

    /**评论标题*/
    private String title;

    /**评论主题*/
    private String commentBody;

    /**评论类型*/
    private Integer type;
    
    /**小说类型*/
    private String storyType;

    /**删除标识*/
    private Boolean isDelete;

    /**评论创建时间*/
    private Date createDate;

    /**评论最新更新时间*/
    private Date lastVersionDate;

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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname == null ? "" : nickname.trim();
    }

    public String getStoryName() {
        return storyName;
    }

    public void setStoryName(String storyName) {
        this.storyName = storyName == null ? "" : storyName.trim();
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public String getStoryAuthor() {
        return storyAuthor;
    }

    public void setStoryAuthor(String storyAuthor) {
        this.storyAuthor = storyAuthor == null ? "" : storyAuthor.trim();
    }

    public String getStoryCover() {
        return storyCover;
    }

    public void setStoryCover(String storyCover) {
        this.storyCover = storyCover == null ? "" : storyCover.trim();
    }

    public Byte getScore() {
        return score;
    }

    public void setScore(Byte score) {
        this.score = score;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public String getCommentBody() {
        return commentBody;
    }

    public void setCommentBody(String commentBody) {
        this.commentBody = commentBody == null ? null : commentBody.trim();
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getStoryType() {
		return storyType;
	}

	public void setStoryType(String storyType) {
		this.storyType = storyType;
	}

	public Boolean getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Boolean isDelete) {
        this.isDelete = isDelete;
    }

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getLastVersionDate() {
		return lastVersionDate;
	}

	public void setLastVersionDate(Date lastVersionDate) {
		this.lastVersionDate = lastVersionDate;
	}
	
	public static StoryComment emptyObject(){
		StoryComment storyComment = new StoryComment();
		storyComment.setAuthorId(0l);
		storyComment.setCommentBody("");
		storyComment.setCreateDate(null);
		storyComment.setId(0l);
		storyComment.setIsDelete(false);
		storyComment.setLastVersionDate(null);
		storyComment.setNickname("");
		storyComment.setScore((byte)0);
		storyComment.setStoryAuthor("");
		storyComment.setStoryCover("");
		storyComment.setStoryId(0l);
		storyComment.setStoryName("");
		storyComment.setStoryType("0");
		storyComment.setTitle("");
		storyComment.setType(0);
		storyComment.setUserId(0l);
		return storyComment;
	}

}