package com.poison.story.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.poison.eagle.utils.ResultUtils;
import com.poison.story.dao.StoryDAO;
import com.poison.story.model.Story;
import com.poison.story.model.StoryChapter;
import com.poison.story.model.StoryPay;
import com.poison.story.model.StoryShelf;

/**
 * Created with IntelliJ IDEA.
 * User: changjiang
 * Date: 15/11/26
 * Time: 17:56
 */
public class StroyDAOImpl extends SqlMapClientDaoSupport implements StoryDAO {

    private static final Log LOG = LogFactory.getLog(StroyDAOImpl.class);

    /**
     * 根据id查询小说详情
     * @param id
     * @return
     */
    @Override
    public Story findStoryById(long id) {

        Story story = new Story();
        try{
            story = (Story)getSqlMapClientTemplate().queryForObject("findStoryById",id);
            if(null==story){
                story = new Story();
                story.setFlag(ResultUtils.DATAISNULL+"");
                return story;
            }
            story.setFlag(ResultUtils.SUCCESS+"");
        }catch (Exception e) {
            e.printStackTrace();
            LOG.error("根据id查询小说出错"+e.getMessage(),e.fillInStackTrace());
            story = new Story();
            story.setFlag(ResultUtils.ERROR+"");
        }
        return story;
    }

    /**
     * 查询章节列表
     * @param storyId
     * @param range
     * @return
     */
    @Override
    public List<StoryChapter> findChapterListByStoryId(long storyId, int range) {
        List<StoryChapter> chapterList = new ArrayList<StoryChapter>();
        Map<String,Object> map = new HashMap();
        try{
            map.put("storyId",storyId);
            map.put("range",null);
            chapterList = getSqlMapClientTemplate().queryForList("findChapterListByStoryId",map);
        }catch (Exception e) {
            e.printStackTrace();
            LOG.error("根据id查询章节列表出错"+e.getMessage(),e.fillInStackTrace());
            chapterList = null;
        }
        return chapterList;
    }

    /**
     * 查询章节详情
     * @param id
     * @return
     */
    @Override
    public String findChapterContent(long id) {
        String resultStr = "";
        try{
            resultStr = (String)getSqlMapClientTemplate().queryForObject("findChapterContentByChapterId",id);
        }catch (Exception e) {
            e.printStackTrace();
            LOG.error("根据id查询章节列表出错"+e.getMessage(),e.fillInStackTrace());
            resultStr = "";
        }
        return resultStr;
    }
    
    /**
     * 根据id查询章节信息
     * @param id
     * @return
     */
    @Override
    public StoryChapter findStoryChapterById(long id) {

    	StoryChapter storyChapter = new StoryChapter();
        try{
        	storyChapter = (StoryChapter)getSqlMapClientTemplate().queryForObject("findStoryChapterById",id);
            if(null==storyChapter){
            	storyChapter = new StoryChapter();
            	storyChapter.setFlag(ResultUtils.DATAISNULL+"");
                return storyChapter;
            }
            storyChapter.setFlag(ResultUtils.SUCCESS+"");
        }catch (Exception e) {
            e.printStackTrace();
            LOG.error("根据id查询小说出错"+e.getMessage(),e.fillInStackTrace());
            storyChapter = new StoryChapter();
            storyChapter.setFlag(ResultUtils.ERROR+"");
        }
        return storyChapter;
    }

    /**
     * 查询我的书架
     * @param id
     * @return
     */
    @Override
    public List<StoryShelf> findMyShelfByUserId(long id) {
        List<StoryShelf> shelfList = new ArrayList<StoryShelf>();
        try{
            shelfList = getSqlMapClientTemplate().queryForList("findShelfByUserId", id);
        }catch (Exception e) {
            e.printStackTrace();
            LOG.error("根据userid查询书架出错"+e.getMessage(),e.fillInStackTrace());
            shelfList = null;
        }
        return shelfList;
    }

    /**
     * 查询我的书架根据类型
     * @param type
     * @return
     */
    @Override
    public List<StoryShelf> findMyShelfByType(String type,int pageIndex,int pageSize) {
        List<StoryShelf> shelfList = new ArrayList<StoryShelf>();
        try{
            Map<String,Object> map = new HashMap<String, Object>();
            map.put("type",type);
            map.put("pageIndex",pageIndex);
            map.put("pageSize",pageSize);
            shelfList = getSqlMapClientTemplate().queryForList("findShelfByType", map);
        }catch (Exception e) {
            e.printStackTrace();
            LOG.error("根据userid查询书架出错"+e.getMessage(),e.fillInStackTrace());
            shelfList = null;
        }
        return shelfList;
    }

    /**
     * 查询章节总数
     * @param storyId
     * @return
     */
    @Override
    public Map<String, Object> findStoryChapterCount(long storyId) {
        Map<String,Object> resultMap = new HashMap<String, Object>();
        int flag = ResultUtils.ERROR;
        int count = 0;
        try{
            count = (Integer)getSqlMapClientTemplate().queryForObject("findChapterCountByStoryId", storyId);
            flag = ResultUtils.SUCCESS;
        }catch (Exception e) {
            e.printStackTrace();
            flag = ResultUtils.ERROR;
            LOG.error("根据userid查询书架出错"+e.getMessage(),e.fillInStackTrace());
        }
        resultMap.put("flag",flag);
        resultMap.put("count",count);
        return resultMap;
    }

    /**
     *
     * @param storyShelf
     * @return
     */
    @Override
    public int insertStoryShelf(StoryShelf storyShelf) {
        int flag = ResultUtils.ERROR;
        try{
            getSqlMapClientTemplate().insert("insertStoryShelf", storyShelf);
            flag = ResultUtils.SUCCESS;
        }catch (Exception e) {
            e.printStackTrace();
            flag = ResultUtils.ERROR;
            LOG.error("加入书架出错"+e.getMessage(),e.fillInStackTrace());
        }
        return flag;
    }

    /**
     * 删除书架
     * @param id
     * @return
     */
    @Override
    public int deleteStoryShelf(long id,long userId) {
        int flag = ResultUtils.ERROR;
        long systime = System.currentTimeMillis();
        try{
            Map<String,Object> map = new HashMap<String, Object>();
            map.put("storyId",id);
            map.put("userId",userId);
            map.put("latestRevisionDate",systime);
            getSqlMapClientTemplate().update("deleteStoryShelf", map);
            flag = ResultUtils.SUCCESS;
        }catch (Exception e) {
            e.printStackTrace();
            flag = ResultUtils.ERROR;
            LOG.error("删除书架出错"+e.getMessage(),e.fillInStackTrace());
        }
        return flag;
    }

    /**
     * 查询书架详情
     * @param id
     * @return
     */
    @Override
    public StoryShelf findStoryShelfById(long id) {
        StoryShelf storyShelf = new StoryShelf();
        int flag = ResultUtils.ERROR;
        try{
            storyShelf = (StoryShelf)getSqlMapClientTemplate().queryForObject("findStoryShelfById", id);
            if(null==storyShelf){
                storyShelf = new StoryShelf();
                storyShelf.setFlag(ResultUtils.DATAISNULL);
                return storyShelf;
            }
            flag = ResultUtils.SUCCESS;
        }catch (Exception e) {
            e.printStackTrace();
            storyShelf = new StoryShelf();
            flag = ResultUtils.ERROR;
            LOG.error("加入书架出错"+e.getMessage(),e.fillInStackTrace());
        }
        storyShelf.setFlag(flag);
        return storyShelf;
    }

    /**
     * 查询用户是否加入过这个书架
     * @param userId
     * @return
     */
    @Override
    public List<StoryShelf> findStoryShelfByUidAndStoryId(long userId, long storyId) {
        List<StoryShelf> storyShelfList = new ArrayList<StoryShelf>();
        int flag = ResultUtils.ERROR;
        try{
            Map<String,Object> map = new HashMap<String, Object>();
            map.put("userId",userId);
            map.put("storyId",storyId);
            storyShelfList = getSqlMapClientTemplate().queryForList("findStoryShelfByUidAndStoryId", map);
        }catch (Exception e) {
            e.printStackTrace();
            storyShelfList = null;
            LOG.error("加入书架出错"+e.getMessage(),e.fillInStackTrace());
        }
        return storyShelfList;
    }

    /**
     * 查询小说在书架中的详情
     * @param storyId
     * @return
     */
    @Override
    public List<Long> findStoryShelfByStoryId(long storyId) {
        List<Long> storyShelfUidList = new ArrayList<Long>();
        try{
            storyShelfUidList = getSqlMapClientTemplate().queryForList("findStoryShelfByStoryId", storyId);
        }catch (Exception e) {
            e.printStackTrace();
            storyShelfUidList = null;
            LOG.error("查询书架中的书出错"+e.getMessage(),e.fillInStackTrace());
        }
        return storyShelfUidList;
    }

    /**
     * 查询支付信息根据章节id和用户id
     * @param chapterId
     * @return
     */
    @Override
    public StoryPay findStoryPayByChapterId(long chapterId,long userId){
    	StoryPay storyPay = new StoryPay();
         try{
        	 Map<String,Object> map = new HashMap<String, Object>();
             map.put("chapterId",chapterId);
             map.put("userId", userId);
        	 storyPay = (StoryPay)getSqlMapClientTemplate().queryForObject("findStoryPayByChapterId",map);
             if(null==storyPay){
            	 storyPay = new StoryPay();
            	 storyPay.setFlag(ResultUtils.DATAISNULL+"");
                 return storyPay;
             }
             storyPay.setFlag(ResultUtils.SUCCESS+"");
         }catch (Exception e) {
             e.printStackTrace();
             LOG.error("根据章节id查询付费状态出现异常"+e.getMessage(),e.fillInStackTrace());
             storyPay = new StoryPay();
             storyPay.setFlag(ResultUtils.ERROR+"");
         }
         return storyPay;
    }
    
    
    /**
     * 根据章节id集合查询章节付费信息集合
     * @param chapterId
     * @param userId
     * @return
     */
    @Override
    public List<StoryPay> findStoryPaysByChapterIds(List<Long> chapterIds,long userId){
    	List<StoryPay> storyPays = null;
         try{
        	 if(chapterIds!=null && chapterIds.size()>0){
        		 Map<String,Object> map = new HashMap<String, Object>();
                 map.put("chapterIdList",chapterIds);
                 map.put("userId", userId);
            	 storyPays = getSqlMapClientTemplate().queryForList("findStoryPaysByChapterIds",map);
        	 }
         }catch (Exception e) {
             e.printStackTrace();
             LOG.error("根据章节id集合查询付费信息集合出现异常"+e.getMessage(),e.fillInStackTrace());
             storyPays = new ArrayList<StoryPay>(1);
             StoryPay storyPay = new StoryPay();
             storyPay.setFlag(ResultUtils.QUERY_ERROR+"");
             storyPays.add(storyPay);
         }
         return storyPays;
    }
    /**
     * 保存章节支付信息
     */
    @Override
    public int saveStoryPay(StoryPay storyPay){
    	int i = ResultUtils.ERROR;
		try{
			getSqlMapClientTemplate().insert("saveStoryPay", storyPay);
			i = ResultUtils.SUCCESS;
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			i = ResultUtils.ERROR;
		}
		return i;
    }
}
