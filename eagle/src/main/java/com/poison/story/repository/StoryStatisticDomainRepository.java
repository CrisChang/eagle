package com.poison.story.repository;

import com.poison.eagle.utils.ResultUtils;
import com.poison.story.dao.StoryStatisticDAO;
import com.poison.story.model.StoryEnumerate;
import com.poison.story.model.StoryStatistic;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: changjiang
 * Date: 16/1/6
 * Time: 17:02
 */
public class StoryStatisticDomainRepository {

    private StoryStatisticDAO storyStatisticDAO;

    public void setStoryStatisticDAO(StoryStatisticDAO storyStatisticDAO) {
        this.storyStatisticDAO = storyStatisticDAO;
    }

    /**
     * 查询点击量排行
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public List<StoryStatistic> findStatisticByClickRate(int pageIndex, int pageSize,String channel){
        return storyStatisticDAO.findStatisticByClickRate(pageIndex, pageSize,channel);
    }

    /**
     * 查询推荐榜
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public List<StoryStatistic> findStatisticByReommentVote(int pageIndex, int pageSize,String channel) {
        return storyStatisticDAO.findStatisticByReommentVote(pageIndex, pageSize,channel);
    }

    /**
     * 根据id查询统计详情
     * @param resourceId
     * @return
     */
    public StoryStatistic findStatisitcByResId(long resourceId){
        return storyStatisticDAO.findStatisitcByResId(resourceId);
    }

    /**
     * 增加点击量
     * @param resourceId
     */
    public void addClickRateByResourceId(long resourceId,String channel,String type){
        StoryStatistic storyStatistic = storyStatisticDAO.findStatisitcByResId(resourceId);
        if(storyStatistic.getFlag()== ResultUtils.DATAISNULL){//当为空的时候
            storyStatisticDAO.insertStatisticByResourceId(resourceId,channel,type);
        }
        storyStatisticDAO.addClickRateByResourceId(resourceId);
                //addRecommendVoteByResourceId(resourceId);
    }

    /**
     * 增加推荐量
     * @param resourceId
     */
    public void addRecommendVoteByResourceId(long resourceId,String channel,String type){
        StoryStatistic storyStatistic = storyStatisticDAO.findStatisitcByResId(resourceId);
        if(storyStatistic.getFlag()== ResultUtils.DATAISNULL){//当为空的时候
            storyStatisticDAO.insertStatisticByResourceId(resourceId,channel,type);
        }
        storyStatisticDAO.addRecommendVoteByResourceId(resourceId);
                //addClickRateByResourceId(resourceId);
    }

    /**
     * 增加金钱
     * @param resourceId
     */
    public void addPayTotalByResourceId(long resourceId,int price,String channel,String type){
        StoryStatistic storyStatistic = storyStatisticDAO.findStatisitcByResId(resourceId);
        if(storyStatistic.getFlag()== ResultUtils.DATAISNULL){//当为空的时候
            storyStatisticDAO.insertStatisticByResourceId(resourceId,channel,type);
        }
        storyStatisticDAO.addPaytotalByResourceId(resourceId, price);
        //addClickRateByResourceId(resourceId);
    }

    /**
     * 增加加入书架的总数
     * @param resourceId
     */
    public void addOnshelfTotalByResourceId(long resourceId,String channel,String type){
        StoryStatistic storyStatistic = storyStatisticDAO.findStatisitcByResId(resourceId);
        if(storyStatistic.getFlag()== ResultUtils.DATAISNULL){//当为空的时候
            storyStatisticDAO.insertStatisticByResourceId(resourceId,channel,type);
        }
        storyStatisticDAO.addOnshelfTotalByResourceId(resourceId);
        //addClickRateByResourceId(resourceId);
    }

    /**
     * 增加小说评论的总数
     * @param resourceId
     * @param channel
     * @param type
     */
    public void addCommentTotalByResourceId(long resourceId,String channel,String type){
        StoryStatistic storyStatistic = storyStatisticDAO.findStatisitcByResId(resourceId);
        if(storyStatistic.getFlag()== ResultUtils.DATAISNULL){//当为空的时候
            storyStatisticDAO.insertStatisticByResourceId(resourceId,channel,type);
        }
        storyStatisticDAO.addCommentTotalByResourceId(resourceId);
        //addClickRateByResourceId(resourceId);
    }

    /**
     * 根据频道查询枚举详情
     * @param channel
     * @return
     */
    public List<StoryEnumerate> findStoryEnumerateByChanel(String channel){
        return storyStatisticDAO.findStoryEnumerateByChanel(channel);
    }

    /**
     * 查询付费排行榜
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public List<StoryStatistic> findStatisticByPayTotal(int pageIndex,int pageSize,String channel){
        return storyStatisticDAO.findStatisticByPayTotal(pageIndex, pageSize,channel);
    }

    /**
     * 查询收藏排行榜
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public List<StoryStatistic> findStatisticByOnshelfTotal(int pageIndex,int pageSize,String channel){
        return storyStatisticDAO.findStatisticByOnshelfTotal(pageIndex, pageSize,channel);
    }
}
