package com.poison.story.repository;

import com.poison.eagle.utils.ResultUtils;
import com.poison.story.dao.StoryDAO;
import com.poison.story.model.*;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: changjiang
 * Date: 15/11/26
 * Time: 17:57
 */
public class StoryDomainRepository {

    private StoryDAO storyDAO;

    public void setStoryDAO(StoryDAO storyDAO) {
        this.storyDAO = storyDAO;
    }

    public Story findStoryById(long id){
        return  storyDAO.findStoryById(id);
    }

    /**
     * 查询章节列表
     * @param storyId
     * @param range
     * @return
     */
    public List<StoryChapter> findChapterListByStoryId(long storyId, Long lastId){
        return storyDAO.findChapterListByStoryId(storyId,0);
    }

    /**
     * 查询章节详情
     * @param id
     * @return
     */
    public String findChapterContent(long id){
        return storyDAO.findChapterContent(id);
    }
    /**
     * 根据id查询章节信息
     * @param id
     * @return
     */
    public StoryChapter findStoryChapterById(long id){
    	return storyDAO.findStoryChapterById(id);
    }
    /**
     * 查询书架
     * @param id
     * @return
     */
    public List<StoryShelf> findMyShelfByUserId(long id){
        return storyDAO.findMyShelfByUserId(id);
    }

    /**
     * 根据类型查询书架
     * @param type
     * @return
     */
    public List<StoryShelf> findMyShelfByType(String type,int pageIndex,int pageSize) {
        return storyDAO.findMyShelfByType(type,pageIndex,pageSize);
    }

    /**
     * 查询章节总数
     * @param storyId
     * @return
     */
    public Map<String, Object> findStoryChapterCount(long storyId) {
        return storyDAO.findStoryChapterCount(storyId);
    }

    /**
     * 插入书架
     * @param storyShelf
     * @return
     */
    public StoryShelf insertStoryShelf(StoryShelf storyShelf){
        StoryShelf storyShelfTemp = new StoryShelf();

        List<StoryShelf> storyShelfTempList = storyDAO.findStoryShelfByUidAndStoryId(storyShelf.getUserId(),storyShelf.getStoryId());

        int flag = ResultUtils.ERROR;
        if(null == storyShelfTempList||storyShelfTempList.size()==0){//当没有时
            flag = storyDAO.insertStoryShelf(storyShelf);
            storyShelfTemp = storyDAO.findStoryShelfById(storyShelf.getId());
        }else{//当有时
            flag= ResultUtils.IS_ALREADY_NOVELLIST;
        }

        storyShelfTemp.setFlag(flag);

        return storyShelfTemp;
    }

    /**
     * 查看是否在书架里
     * @param userId
     * @param storyId
     * @return
     */
    public boolean isOnShelf(long userId,long storyId){
        boolean flag = false;
        List<StoryShelf> storyShelfTempList = storyDAO.findStoryShelfByUidAndStoryId(userId,storyId);
        if(null == storyShelfTempList||storyShelfTempList.size()==0){//没有在书架
            flag = true;
        }
        return flag;
    }

    /**
     * 删除书架
     * @param id
     * @return
     */
    public int deleteStoryShelf(long id,long userId){
        return storyDAO.deleteStoryShelf(id,userId);
    }
    
    /**
     * 查询支付信息根据章节id
     * @param chapterId
     * @return
     */
    public StoryPay findStoryPayByChapterId(long chapterId,long userId){
    	return storyDAO.findStoryPayByChapterId(chapterId,userId);
    }
    
    /**
     * 保存章节支付信息
     */
    public int saveStoryPay(StoryPay storyPay){
    	return storyDAO.saveStoryPay(storyPay);
    }
    /**
     * 根据章节id集合查询章节付费信息集合
     * @param userId
     * @return
     */
    public List<StoryPay> findStoryPaysByChapterIds(List<Long> chapterIds,long userId){
    	return storyDAO.findStoryPaysByChapterIds(chapterIds, userId);
    }

    /**
     * 查询书架中的用户列表
     * @param storyId
     * @return
     */
    public List<Long> findStoryShelfByStoryId(long storyId){
        return storyDAO.findStoryShelfByStoryId(storyId);
    }
}
