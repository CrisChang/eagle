package com.poison.story.service;

import com.poison.story.model.StoryEnumerate;
import com.poison.story.model.StoryStatistic;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: changjiang
 * Date: 16/1/6
 * Time: 17:16
 */
public interface StoryStatisticService {

    /**
     * 查询点击量排行
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public List<StoryStatistic> findStatisticByClickRate(int pageIndex, int pageSize,String channel);

    /**
     * 查询推荐榜
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public List<StoryStatistic> findStatisticByReommentVote(int pageIndex, int pageSize,String channel);

    /**
     * 根据id查询统计详情
     * @param resourceId
     * @return
     */
    public StoryStatistic findStatisitcByResId(long resourceId);

    /**
     * 增加点击量
     * @param resourceId
     */
    public void addClickRateByResourceId(long resourceId,String channel,String type);

    /**
     * 增加推荐量
     * @param resourceId
     */
    public void addRecommendVoteByResourceId(long resourceId,String channel,String type);

    /**
     * 根据频道查询枚举详情
     * @param channel
     * @return
     */
    public List<StoryEnumerate> findStoryEnumerateByChanel(String channel);

    /**
     * 增加加入书架的数量
     * @param resourceId
     */
    public void addOnshelfTotalByResourceId(long resourceId,String channel,String type);

    /**
     * 增加小说评论的总数
     * @param resourceId
     * @param channel
     * @param type
     */
    public void addCommentTotalByResourceId(long resourceId,String channel,String type);

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
}
