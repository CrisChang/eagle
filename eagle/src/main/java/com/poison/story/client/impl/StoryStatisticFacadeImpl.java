package com.poison.story.client.impl;

import com.poison.story.client.StoryStatisticFacade;
import com.poison.story.model.StoryEnumerate;
import com.poison.story.model.StoryStatistic;
import com.poison.story.service.StoryStatisticService;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: changjiang
 * Date: 16/1/6
 * Time: 17:28
 */
public class StoryStatisticFacadeImpl implements StoryStatisticFacade{

    private StoryStatisticService storyStatisticService;

    public void setStoryStatisticService(StoryStatisticService storyStatisticService) {
        this.storyStatisticService = storyStatisticService;
    }

    /**
     * 查询点击量排行榜
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @Override
    public List<StoryStatistic> findStatisticByClickRate(int pageIndex, int pageSize,String channel) {
        return storyStatisticService.findStatisticByClickRate(pageIndex, pageSize,channel);
    }

    /**
     * 查询推荐榜
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @Override
    public List<StoryStatistic> findStatisticByReommentVote(int pageIndex, int pageSize,String channel) {
        return storyStatisticService.findStatisticByReommentVote(pageIndex, pageSize,channel);
    }

    /**
     * 根据id查询统计详情
     * @param resourceId
     * @return
     */
    @Override
    public StoryStatistic findStatisitcByResId(long resourceId) {
        return storyStatisticService.findStatisitcByResId(resourceId);
    }

    /**
     * 增加点击量
     * @param resourceId
     */
    @Override
    public void addClickRateByResourceId(long resourceId,String channel,String type) {
        storyStatisticService.addClickRateByResourceId(resourceId,channel,type);
    }

    /**
     * 增加推荐量
     * @param resourceId
     */
    @Override
    public void addRecommendVoteByResourceId(long resourceId,String channel,String type) {
        storyStatisticService.addRecommendVoteByResourceId(resourceId,channel,type);
    }

    /**
     * 根据类型查询枚举详情
     * @param channel
     * @return
     */
    @Override
    public List<StoryEnumerate> findStoryEnumerateByChanel(String channel) {
        return storyStatisticService.findStoryEnumerateByChanel(channel);
    }

    /**
     * 增加加入书架的总数
     * @param resourceId
     */
    @Override
    public void addOnshelfTotalByResourceId(long resourceId,String channel,String type) {
        storyStatisticService.addOnshelfTotalByResourceId(resourceId,channel,type);
    }

    /**
     * 增加小说的评论总数
     * @param resourceId
     * @param channel
     * @param type
     */
    @Override
    public void addCommentTotalByResourceId(long resourceId, String channel, String type) {
        storyStatisticService.addCommentTotalByResourceId(resourceId, channel, type);
    }

    /**
     * 查询付费排行榜
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @Override
    public List<StoryStatistic> findStatisticByPayTotal(int pageIndex, int pageSize,String channel) {
        return storyStatisticService.findStatisticByPayTotal(pageIndex, pageSize,channel);
    }

    /**
     * 查询收藏排行榜
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @Override
    public List<StoryStatistic> findStatisticByOnshelfTotal(int pageIndex, int pageSize,String channel) {
        return storyStatisticService.findStatisticByOnshelfTotal(pageIndex, pageSize,channel);
    }
}
