package com.poison.story.service.impl;

import com.poison.story.model.StoryEnumerate;
import com.poison.story.model.StoryStatistic;
import com.poison.story.repository.StoryStatisticDomainRepository;
import com.poison.story.service.StoryStatisticService;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: changjiang
 * Date: 16/1/6
 * Time: 17:16
 */
public class StoryStatisticServiceImpl implements StoryStatisticService{

    private StoryStatisticDomainRepository storyStatisticDomainRepository;

    public void setStoryStatisticDomainRepository(StoryStatisticDomainRepository storyStatisticDomainRepository) {
        this.storyStatisticDomainRepository = storyStatisticDomainRepository;
    }

    /**
     * 查询点击量排行
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @Override
    public List<StoryStatistic> findStatisticByClickRate(int pageIndex, int pageSize,String channel) {
        return storyStatisticDomainRepository.findStatisticByClickRate(pageIndex, pageSize,channel);
    }

    /**
     * 查询推荐榜
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @Override
    public List<StoryStatistic> findStatisticByReommentVote(int pageIndex, int pageSize,String channel) {
        return storyStatisticDomainRepository.findStatisticByReommentVote(pageIndex, pageSize,channel);
    }

    /**
     * 根据id查询统计详情
     * @param resourceId
     * @return
     */
    @Override
    public StoryStatistic findStatisitcByResId(long resourceId) {
        return storyStatisticDomainRepository.findStatisitcByResId(resourceId);
    }

    /**
     * 增加点击量
     * @param resourceId
     */
    @Override
    public void addClickRateByResourceId(long resourceId,String channel,String type) {
        storyStatisticDomainRepository.addClickRateByResourceId(resourceId,channel,type);
    }

    /**
     * 增加推荐量
     * @param resourceId
     */
    @Override
    public void addRecommendVoteByResourceId(long resourceId,String channel,String type) {
        storyStatisticDomainRepository.addRecommendVoteByResourceId(resourceId,channel,type);
    }

    /**
     * 根据频道查询枚举类型
     * @param channel
     * @return
     */
    @Override
    public List<StoryEnumerate> findStoryEnumerateByChanel(String channel) {
        return storyStatisticDomainRepository.findStoryEnumerateByChanel(channel);
    }

    /**
     * 增加加入书架的时间
     * @param resourceId
     */
    @Override
    public void addOnshelfTotalByResourceId(long resourceId,String channel,String type) {
        storyStatisticDomainRepository.addOnshelfTotalByResourceId(resourceId,channel,type);
    }

    /**
     * 增加小说评论的总数
     * @param resourceId
     * @param channel
     * @param type
     */
    @Override
    public void addCommentTotalByResourceId(long resourceId, String channel, String type) {
        storyStatisticDomainRepository.addCommentTotalByResourceId(resourceId, channel, type);
    }

    /**
     * 查询付费排行榜
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @Override
    public List<StoryStatistic> findStatisticByPayTotal(int pageIndex, int pageSize,String channel) {
        return storyStatisticDomainRepository.findStatisticByPayTotal(pageIndex, pageSize,channel);
    }

    /**
     * 查询收藏排行榜
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @Override
    public List<StoryStatistic> findStatisticByOnshelfTotal(int pageIndex, int pageSize,String channel) {
        return storyStatisticDomainRepository.findStatisticByOnshelfTotal(pageIndex, pageSize,channel);
    }
}
