package com.poison.story.service;

import com.poison.story.model.Story;
import com.poison.story.model.StoryChapter;
import com.poison.story.model.StoryPay;
import com.poison.story.model.StoryShelf;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: changjiang
 * Date: 15/11/26
 * Time: 17:57
 */
public interface StoryService {

    /**
     * 根据id查询小说详情
     * @param id
     * @return
     */
    public Story findStoryById(long id);

    /**
     * 查询章节列表
     * @param storyId
     * @param lastId
     * @return
     */
    public List<StoryChapter> findChapterListByStoryId(long storyId, Long lastId);

    /**
     * 查询章节详情
     * @param id
     * @return
     */
    public String findChapterContent(long id);
    /**
     * 根据id查询章节信息
     * @param id
     * @return
     */
    public StoryChapter findStoryChapterById(long id);

    /**
     * 查询我的书架
     * @param id
     * @return
     */
    public List<StoryShelf> findMyShelfByUserId(long id);

    /**
     * 根据类型查询书架
     * @param type
     * @return
     */
    public List<StoryShelf> findMyShelfByType(String type,int pageIndex,int pageSize);

    /**
     * 查询章节总数
     * @param storyId
     * @return
     */
    public Map<String, Object> findStoryChapterCount(long storyId);

    /**
     * 加入书架
     * @param storyShelf
     * @return
     */
    public StoryShelf insertStoryShelf(StoryShelf storyShelf);

    /**
     * 删除书架
     * @param id
     * @return
     */
    public int deleteStoryShelf(long id,long userId);

    /**
     * 查看是否在书架
     * @param userId
     * @param storyId
     * @return
     */
    public boolean isOnShelf(long userId,long storyId);
    /**
     * 查询支付信息根据章节id
     * @param chapterId
     * @return
     */
    public StoryPay findStoryPayByChapterId(long chapterId,long userId);
    /**
     * 根据章节id集合查询章节付费信息集合
     * @param userId
     * @return
     */
    public List<StoryPay> findStoryPaysByChapterIds(List<Long> chapterIds,long userId);
    
    /**
     * 保存章节支付信息
     */
    public int saveStoryPay(StoryPay storyPay);

    /**
     * 查询书架中订阅一本小说的用户id
     * @param storyId
     * @return
     */
    public List<Long> findStoryShelfByStoryId(long storyId);
}
