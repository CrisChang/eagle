package com.poison.story.service.impl;

import com.poison.story.model.Story;
import com.poison.story.model.StoryChapter;
import com.poison.story.model.StoryPay;
import com.poison.story.model.StoryShelf;
import com.poison.story.repository.StoryDomainRepository;
import com.poison.story.service.StoryService;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: changjiang
 * Date: 15/11/26
 * Time: 17:57
 */
public class StoryServiceImpl implements StoryService{

    private StoryDomainRepository storyDomainRepository;

    public void setStoryDomainRepository(StoryDomainRepository storyDomainRepository) {
        this.storyDomainRepository = storyDomainRepository;
    }

    /**
     * 根据id查询小说详情
     * @param id
     * @return
     */
    @Override
    public Story findStoryById(long id) {
        return storyDomainRepository.findStoryById(id);
    }

    /**
     * 查询章节列表
     * @param storyId
     * @param lastId
     * @return
     */
    @Override
    public List<StoryChapter> findChapterListByStoryId(long storyId, Long lastId) {
        return storyDomainRepository.findChapterListByStoryId(storyId,lastId);
    }

    /**
     * 查询章节详情
     * @param id
     * @return
     */
    @Override
    public String findChapterContent(long id) {
        return storyDomainRepository.findChapterContent(id);
    }
    /**
     * 根据id查询章节信息
     * @param id
     * @return
     */
    @Override
    public StoryChapter findStoryChapterById(long id){
    	return storyDomainRepository.findStoryChapterById(id);
    }
    /**
     * 查询书架
     * @param id
     * @return
     */
    @Override
    public List<StoryShelf> findMyShelfByUserId(long id) {
        return storyDomainRepository.findMyShelfByUserId(id);
    }

    /**
     * 根据类型查询书架
     * @param type
     * @return
     */
    @Override
    public List<StoryShelf> findMyShelfByType(String type,int pageIndex,int pageSize) {
        return storyDomainRepository.findMyShelfByType(type,pageIndex,pageSize);
    }

    /**
     * 查询章节总数
     * @param storyId
     * @return
     */
    @Override
    public Map<String, Object> findStoryChapterCount(long storyId) {
        return storyDomainRepository.findStoryChapterCount(storyId);
    }

    /**
     * 加入书架
     * @param storyShelf
     * @return
     */
    @Override
    public StoryShelf insertStoryShelf(StoryShelf storyShelf) {
        return storyDomainRepository.insertStoryShelf(storyShelf);
    }

    /**
     * 删除书架
     * @param id
     * @return
     */
    @Override
    public int deleteStoryShelf(long id,long userId) {
        return storyDomainRepository.deleteStoryShelf(id,userId);
    }

    /**
     * 是否在书架中
     * @param userId
     * @param storyId
     * @return
     */
    @Override
    public boolean isOnShelf(long userId, long storyId) {
        return storyDomainRepository.isOnShelf(userId, storyId);
    }
    /**
     * 查询支付信息根据章节id
     * @param chapterId
     * @return
     */
    @Override
    public StoryPay findStoryPayByChapterId(long chapterId,long userId){
    	return storyDomainRepository.findStoryPayByChapterId(chapterId,userId);
    }
    /**
     * 根据章节id集合查询章节付费信息集合
     * @param userId
     * @return
     */
    @Override
    public List<StoryPay> findStoryPaysByChapterIds(List<Long> chapterIds,long userId){
    	return storyDomainRepository.findStoryPaysByChapterIds(chapterIds, userId);
    }
    /**
     * 保存章节支付信息
     */
    @Override
    public int saveStoryPay(StoryPay storyPay){
    	return storyDomainRepository.saveStoryPay(storyPay);
    }

    /**
     * 查询书架中订阅这本书的用户列表
     * @param storyId
     * @return
     */
    @Override
    public List<Long> findStoryShelfByStoryId(long storyId) {
        return storyDomainRepository.findStoryShelfByStoryId(storyId);
    }

}
