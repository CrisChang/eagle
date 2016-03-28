package com.poison.act.model;

import com.keel.common.lang.BaseDO;

/**
 * Created with IntelliJ IDEA.
 * User: changjiang
 * Date: 16/1/12
 * Time: 12:17
 */
public class ActRecommendVote extends BaseDO{


    private static final long serialVersionUID = -6409822114886696380L;

    private long id;//主键id
    private long userId;//用户id
    private long resourceId;//资源id
    private String resourceType;//资源类型
    private int recommendVote;//推荐票数
    private long createDate;//创建时间
    private long latestRevisionDate;//最后修改时间
    private int flag;//标识符

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

    public long getResourceId() {
        return resourceId;
    }

    public void setResourceId(long resourceId) {
        this.resourceId = resourceId;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public int getRecommendVote() {
        return recommendVote;
    }

    public void setRecommendVote(int recommendVote) {
        this.recommendVote = recommendVote;
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
}
