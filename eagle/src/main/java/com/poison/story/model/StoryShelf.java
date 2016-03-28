package com.poison.story.model;

import com.keel.common.lang.BaseDO;

/**
 * Created with IntelliJ IDEA.
 * User: changjiang
 * Date: 15/12/2
 * Time: 17:57
 */
public class StoryShelf extends BaseDO{


    private static final long serialVersionUID = 7797157426438271626L;
    private long id;//主键
    private long userId;//用户id
    private String name;//名字
    private long storyId;//小说id
    private String introduce;//简介
    private String type;//类型
    private String cover;//封面
    private String otherUrl;//地址
    private long createDate;//创建时间
    private long latestRevisionDate;//最后修改时间
    private int flag;//标识符

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getOtherUrl() {
        return otherUrl;
    }

    public void setOtherUrl(String otherUrl) {
        this.otherUrl = otherUrl;
    }

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getStoryId() {
        return storyId;
    }

    public void setStoryId(long storyId) {
        this.storyId = storyId;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
