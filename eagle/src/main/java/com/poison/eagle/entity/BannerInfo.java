package com.poison.eagle.entity;

import com.keel.common.lang.BaseDO;

/**
 * Created with IntelliJ IDEA.
 * User: changjiang
 * Date: 16/1/13
 * Time: 16:56
 */
public class BannerInfo extends BaseDO{

    private static final long serialVersionUID = 911996860466263155L;

    private long id;//主键id
    private long storyId;//小说id
    private String cover;//封面
    private String introduce;//简介
    private String otherUrl;//外链地址

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getStoryId() {
        return storyId;
    }

    public void setStoryId(long storyId) {
        this.storyId = storyId;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getOtherUrl() {
        return otherUrl;
    }

    public void setOtherUrl(String otherUrl) {
        this.otherUrl = otherUrl;
    }
}
