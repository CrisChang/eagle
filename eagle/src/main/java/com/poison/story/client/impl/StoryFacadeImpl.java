package com.poison.story.client.impl;

import com.keel.utils.UKeyWorker;
import com.poison.story.client.StoryFacade;
import com.poison.story.model.Story;
import com.poison.story.model.StoryChapter;
import com.poison.story.model.StoryPay;
import com.poison.story.model.StoryPromote;
import com.poison.story.model.StoryShelf;
import com.poison.story.service.IStoryPromoteService;
import com.poison.story.service.StoryService;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: changjiang
 * Date: 15/11/26
 * Time: 17:58
 */
public class StoryFacadeImpl implements StoryFacade{

    private StoryService storyService;
    private UKeyWorker reskeyWork;
    
    private IStoryPromoteService promoteService;

    public void setReskeyWork(UKeyWorker reskeyWork) {
        this.reskeyWork = reskeyWork;
    }

    public void setStoryService(StoryService storyService) {
        this.storyService = storyService;
    }
    
    

    public void setStoryPromoteService(IStoryPromoteService promoteService) {
		this.promoteService = promoteService;
	}

	/**
     * 根据id查询小说详情
     * @param id
     * @return
     */
    @Override
    public Story findStoryById(long id) {
        return storyService.findStoryById(id);
    }

    /**
     * 查询章节列表
     * @param storyId
     * @param lastId
     * @return
     */
    @Override
    public List<StoryChapter> findChapterListByStoryId(long storyId, Long lastId) {
        return storyService.findChapterListByStoryId(storyId, lastId);
    }

    /**
     * 查询章节详情
     * @param id
     * @return
     */
    @Override
    public String findChapterContent(long id) {
        return storyService.findChapterContent(id);
    }
    /**
     * 根据id查询章节信息
     * @param id
     * @return
     */
    @Override
    public StoryChapter findStoryChapterById(long id){
    	return storyService.findStoryChapterById(id);
    }
    /**
     * 查询书架的id
     * @param id
     * @return
     */
    @Override
    public List<StoryShelf> findMyShelfByUserId(long id) {
        return storyService.findMyShelfByUserId(id);
    }

    /**
     * 根据类型查询书架
     * @param type
     * @return
     */
    @Override
    public List<StoryShelf> findMyShelfByType(String type,int pageIndex,int pageSize) {
        return storyService.findMyShelfByType(type,pageIndex,pageSize);
    }

    /**
     * 查询章节总数
     * @param storyId
     * @return
     */
    @Override
    public Map<String, Object> findStoryChapterCount(long storyId) {
        return storyService.findStoryChapterCount(storyId);
    }

    /**
     * 加入书架
     * @param userId
     * @param storyId
     * @return
     */
    @Override
    public StoryShelf insertStoryShelf(long userId, long storyId) {
        StoryShelf storyShelf = new StoryShelf();
        storyShelf.setId(reskeyWork.getId());
        storyShelf.setUserId(userId);
        storyShelf.setStoryId(storyId);
        storyShelf.setType("0");//加入书架默认type为0
        storyShelf.setName("");
        storyShelf.setIntroduce("");
        storyShelf.setCover("");
        storyShelf.setOtherUrl("");
        long systime = System.currentTimeMillis();
        storyShelf.setCreateDate(systime);
        storyShelf.setLatestRevisionDate(systime);
        return storyService.insertStoryShelf(storyShelf);
    }

    /**
     * 删除书架
     * @param id
     * @return
     */
    @Override
    public int deleteStoryShelf(long id,long userId) {
        return storyService.deleteStoryShelf(id,userId);
    }

    /**
     * 是否在书架上
     * @param userId
     * @param storyId
     * @return
     */
    @Override
    public boolean isOnShelf(long userId, long storyId) {
        return storyService.isOnShelf(userId, storyId);
    }
    /**
     * 查询支付信息根据章节id
     * @param chapterId
     * @return
     */
    public StoryPay findStoryPayByChapterId(long chapterId,long userId){
    	return storyService.findStoryPayByChapterId(chapterId,userId);
    }
    /**
     * 根据章节id集合查询章节付费信息集合
     * @param userId
     * @return
     */
    @Override
    public List<StoryPay> findStoryPaysByChapterIds(List<Long> chapterIds,long userId){
    	return storyService.findStoryPaysByChapterIds(chapterIds, userId);
    }
    /**
     * 保存章节支付信息--默认支付状态
     */
    public int saveStoryPay(long chapterId,long userId,int payAmount){
    	StoryPay storyPay = new StoryPay();
    	storyPay.setChapterId(chapterId);
    	storyPay.setUserId(userId);
    	storyPay.setPayAmount(payAmount);
    	storyPay.setPayed(1);
    	return storyService.saveStoryPay(storyPay);
    }

	@Override
	public List<StoryPromote> findStoryPromoteByCondition(StoryPromote promote, long pageIndex, int pageSize) {
		return promoteService.findStoryPromoteByCondition(promote, pageIndex, pageSize);
	}

    /**
     * 查询书架中订阅这本小说的用户列表
     * @param storyId
     * @return
     */
    @Override
    public List<Long> findStoryShelfByStoryId(long storyId) {
        return storyService.findStoryShelfByStoryId(storyId);
    }
}
