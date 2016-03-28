package com.poison.story.dao.impl;

import com.poison.eagle.utils.ResultUtils;
import com.poison.story.dao.StoryStatisticDAO;
import com.poison.story.model.StoryEnumerate;
import com.poison.story.model.StoryShelf;
import com.poison.story.model.StoryStatistic;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: changjiang
 * Date: 16/1/6
 * Time: 16:46
 */
public class StoryStatisticDAOImpl extends SqlMapClientDaoSupport implements StoryStatisticDAO{

    private static final Log LOG = LogFactory.getLog(StoryStatisticDAOImpl.class);

    /**
     * 查询小说的点击量
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @Override
    public List<StoryStatistic> findStatisticByClickRate(int pageIndex, int pageSize,String channel) {

        List<StoryStatistic> statisticList = new ArrayList<StoryStatistic>();
        try{
            Map<String,Object> map = new HashMap<String, Object>();
            map.put("pageIndex",pageIndex);
            map.put("pageSize",pageSize);
            map.put("channel",channel);
            statisticList = getSqlMapClientTemplate().queryForList("findStatisticByClickRate", map);
        }catch (Exception e) {
            e.printStackTrace();
            LOG.error("根据userid查询书架出错"+e.getMessage(),e.fillInStackTrace());
            statisticList = null;
        }
        return statisticList;
    }

    /**
     * 查询推荐榜
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @Override
    public List<StoryStatistic> findStatisticByReommentVote(int pageIndex, int pageSize,String channel) {
        List<StoryStatistic> statisticList = new ArrayList<StoryStatistic>();
        try{
            Map<String,Object> map = new HashMap<String, Object>();
            map.put("pageIndex",pageIndex);
            map.put("pageSize",pageSize);
            map.put("channel",channel);
            statisticList = getSqlMapClientTemplate().queryForList("findStatisticByRecommentVote", map);
        }catch (Exception e) {
            e.printStackTrace();
            LOG.error("根据userid查询书架出错"+e.getMessage(),e.fillInStackTrace());
            statisticList = null;
        }
        return statisticList;
    }

    /**
     * 查询付费排行榜
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @Override
    public List<StoryStatistic> findStatisticByPayTotal(int pageIndex, int pageSize,String channel) {
        List<StoryStatistic> statisticList = new ArrayList<StoryStatistic>();
        try{
            Map<String,Object> map = new HashMap<String, Object>();
            map.put("pageIndex",pageIndex);
            map.put("pageSize",pageSize);
            map.put("channel",channel);
            statisticList = getSqlMapClientTemplate().queryForList("findStatisticByPayTotal", map);
        }catch (Exception e) {
            e.printStackTrace();
            LOG.error("查询付费排行榜出错"+e.getMessage(),e.fillInStackTrace());
            statisticList = null;
        }
        return statisticList;
    }

    /**
     * 查询收藏排行榜
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @Override
    public List<StoryStatistic> findStatisticByOnshelfTotal(int pageIndex, int pageSize,String channel) {
        List<StoryStatistic> statisticList = new ArrayList<StoryStatistic>();
        try{
            Map<String,Object> map = new HashMap<String, Object>();
            map.put("pageIndex",pageIndex);
            map.put("pageSize",pageSize);
            map.put("channel",channel);
            statisticList = getSqlMapClientTemplate().queryForList("findStatisticByOnshelfTotal", map);
        }catch (Exception e) {
            e.printStackTrace();
            LOG.error("查询收藏书架排行榜出错"+e.getMessage(),e.fillInStackTrace());
            statisticList = null;
        }
        return statisticList;
    }

    /**
     * 查询统计详情
     * @param resourceId
     * @return
     */
    @Override
    public StoryStatistic findStatisitcByResId(long resourceId) {
        StoryStatistic storyStatistic = new StoryStatistic();
        int flag = ResultUtils.ERROR;
        try{
            storyStatistic = (StoryStatistic)getSqlMapClientTemplate().queryForObject("findStatisticByResourceId", resourceId);
            if(null==storyStatistic){
                storyStatistic = new StoryStatistic();
                storyStatistic.setFlag(ResultUtils.DATAISNULL);
                return storyStatistic;
            }
        }catch (Exception e) {
            e.printStackTrace();
            LOG.error("根据resourceid查询统计详情出错"+e.getMessage(),e.fillInStackTrace());
            storyStatistic = new StoryStatistic();
            storyStatistic.setFlag(ResultUtils.ERROR);
        }
        return storyStatistic;
    }

    /**
     * 增加点击量
     * @param resourceId
     */
    @Override
    public void addClickRateByResourceId(long resourceId) {
        try{
            getSqlMapClientTemplate().update("addClickRateByResourceId",resourceId);
        }catch (Exception e){
            LOG.error("根据resourceid增加点击量"+e.getMessage(),e.fillInStackTrace());
        }
    }

    /**
     * 增加推荐票的量
     * @param resourceId
     */
    @Override
    public void addRecommendVoteByResourceId(long resourceId) {
        try{
            getSqlMapClientTemplate().update("addRecommendVoteByResourceId",resourceId);
        }catch (Exception e){
            LOG.error("根据resourceid增加推荐票"+e.getMessage(),e.fillInStackTrace());
        }
    }

    /**
     * 增加付费总量
     * @param resourceId
     */
    @Override
    public void addPaytotalByResourceId(long resourceId,int price) {
        try{
            Map<String,Object> map = new HashMap<String, Object>();
            map.put("resourceId",resourceId);
            map.put("price",price);
            getSqlMapClientTemplate().update("addPayByResourceId",map);
        }catch (Exception e){
            LOG.error("根据resourceid增加付费总数"+e.getMessage(),e.fillInStackTrace());
        }
    }

    /**
     * 增加收藏总数
     * @param resourceId
     */
    @Override
    public void addOnshelfTotalByResourceId(long resourceId) {
        try{
            getSqlMapClientTemplate().update("addOnShelfByResourceId",resourceId);
        }catch (Exception e){
            LOG.error("根据resourceid增加加入书架总数"+e.getMessage(),e.fillInStackTrace());
        }
    }

    /**
     * 小说评论增加
     * @param resourceId
     */
    @Override
    public void addCommentTotalByResourceId(long resourceId) {
        try{
            getSqlMapClientTemplate().update("addCommentTotalByResourceId",resourceId);
        }catch (Exception e){
            LOG.error("根据resourceid增加小说评论的总数"+e.getMessage(),e.fillInStackTrace());
        }
    }

    /**
     * 插入一条统计信息
     * @param resourceId
     * @return
     */
    @Override
    public int insertStatisticByResourceId(long resourceId,String channel,String type) {
        int flag = ResultUtils.ERROR;
        try{
            Map<String,Object> map = new HashMap<String, Object>();
            long sysTime = System.currentTimeMillis();
            map.put("resourceId",resourceId);
            map.put("channel",channel);
            map.put("type",type);
            map.put("createDate",sysTime);
            map.put("latestRevisionDate",sysTime);
            getSqlMapClientTemplate().insert("insertStatisticByResId",map);
            flag = ResultUtils.SUCCESS;
        }catch (Exception e){
            LOG.error("根据resourceid增加点击量"+e.getMessage(),e.fillInStackTrace());
        }
        return flag;
    }

    /**
     * 根据频道查询枚举接口
     * @param channel
     * @return
     */
    @Override
    public List<StoryEnumerate> findStoryEnumerateByChanel(String channel) {
        List<StoryEnumerate> storyEnumerateList = new ArrayList<StoryEnumerate>();
        try{
            storyEnumerateList = getSqlMapClientTemplate().queryForList("findStoryEnumerateByChanel", channel);
        }catch (Exception e) {
            e.printStackTrace();
            LOG.error("根据channel查询枚举类型出错"+e.getMessage(),e.fillInStackTrace());
            storyEnumerateList = null;
        }
        return storyEnumerateList;
    }
}
