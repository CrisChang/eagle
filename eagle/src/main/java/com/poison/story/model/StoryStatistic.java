package com.poison.story.model;

import com.keel.common.lang.BaseDO;

/**
 * Created with IntelliJ IDEA.
 * User: changjiang
 * Date: 16/1/6
 * Time: 12:13
 */
public class StoryStatistic extends BaseDO{


    private static final long serialVersionUID = 2432662869845913873L;
    private long id;//主键id
    private long resourceId;//小说id
    private String type;//小说类型
    private long clickRate;//点击量
    private long clickRateOperation;//运营点击量
    private long recommentVote;//推荐票
    private long recommentVoteOperation;//运营推荐票
    private int isDelete;//是否删除 0默认 1删除
    private long createDate;//创建时间
    private long latestRevisionDate;//最后更新时间
    private int flag;//标识符
    private long payTotal;//付费金额
    private long onshelfTotal;//加入书架总数
    private long onshelfTotalOperation;//运营加入书架
    private String channel;//频道


    public long getOnshelfTotalOperation() {
        return onshelfTotalOperation;
    }

    public void setOnshelfTotalOperation(long onshelfTotalOperation) {
        this.onshelfTotalOperation = onshelfTotalOperation;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public long getPayTotal() {
        return payTotal;
    }

    public void setPayTotal(long payTotal) {
        this.payTotal = payTotal;
    }

    public long getOnshelfTotal() {
        return onshelfTotal;
    }

    public void setOnshelfTotal(long onshelfTotal) {
        this.onshelfTotal = onshelfTotal;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getResourceId() {
        return resourceId;
    }

    public void setResourceId(long resourceId) {
        this.resourceId = resourceId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getClickRate() {
        return clickRate;
    }

    public void setClickRate(long clickRate) {
        this.clickRate = clickRate;
    }

    public long getClickRateOperation() {
        return clickRateOperation;
    }

    public void setClickRateOperation(long clickRateOperation) {
        this.clickRateOperation = clickRateOperation;
    }

    public long getRecommentVote() {
        return recommentVote;
    }

    public void setRecommentVote(long recommentVote) {
        this.recommentVote = recommentVote;
    }

    public long getRecommentVoteOperation() {
        return recommentVoteOperation;
    }

    public void setRecommentVoteOperation(long recommentVoteOperation) {
        this.recommentVoteOperation = recommentVoteOperation;
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
}
