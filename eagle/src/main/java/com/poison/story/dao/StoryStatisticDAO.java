package com.poison.story.dao;

import com.poison.story.model.StoryEnumerate;
import com.poison.story.model.StoryStatistic;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: changjiang
 * Date: 16/1/6
 * Time: 16:46
 */
public interface StoryStatisticDAO {

    /**
     * 点击量
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public List<StoryStatistic> findStatisticByClickRate(int pageIndex,int pageSize,String channel);

    /**
     * 推荐榜
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public List<StoryStatistic> findStatisticByReommentVote(int pageIndex,int pageSize,String channel);

    /**
     * 查询付费统计排行
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public List<StoryStatistic> findStatisticByPayTotal(int pageIndex,int pageSize,String channel);

    /**
     * 查询收藏总数
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public List<StoryStatistic> findStatisticByOnshelfTotal(int pageIndex,int pageSize,String channel);

    /**
     * 查询统计详情
     * @param resourceId
     * @return
     */
    public StoryStatistic findStatisitcByResId(long resourceId);

    /**
     * 增加点击量
     * @param resourceId
     */
    public void addClickRateByResourceId(long resourceId);

    /**
     * 增加推荐票的量
     * @param resourceId
     */
    public void addRecommendVoteByResourceId(long resourceId);

    /**
     * 增加付费总价钱
     * @param resourceId
     */
    public void addPaytotalByResourceId(long resourceId,int price);

    /**
     * 增加加入书架总数
     * @param resourceId
     */
    public void addOnshelfTotalByResourceId(long resourceId);

    /**
     * 增加评论数
     * @param resourceId
     */
    public void addCommentTotalByResourceId(long resourceId);

    /**
     * 插入统计信息
     * @param resourceId
     * @return
     */
    public int insertStatisticByResourceId(long resourceId,String channel,String type);

    /**
     * 根据频道查询接口
     * @param channel
     * @return
     */
    public List<StoryEnumerate> findStoryEnumerateByChanel(String channel);

}
