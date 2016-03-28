package com.poison.story.dao;

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
 * Time: 17:56
 */
public interface StoryDAO {

    /**
     * 根据id查询小说详情
     * @param id
     * @return
     */
    public Story findStoryById(long id);

    /**
     * 根据小说id查询章节列表
     * @param storyId
     * @param range
     * @return
     */
    public List<StoryChapter> findChapterListByStoryId(long storyId,int range);

    /**
     * 根据id查询章节详情
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
     *
     * @param type
     * @return
     */
    public List<StoryShelf> findMyShelfByType(String type,int pageIndex,int pageSize);

    /**
     * 查询小说章节总数
     * @param storyId
     * @return
     */
    public Map<String,Object> findStoryChapterCount(long storyId);

    /**
     * 插入书架信息
     * @param storyShelf
     * @return
     */
    public int insertStoryShelf(StoryShelf storyShelf);

    /**
     * 删除书架的书
     * @param id
     * @return
     */
    public int deleteStoryShelf(long id,long userId);

    /**
     * 查询一个书架详情
     * @param id
     * @return
     */
    public StoryShelf findStoryShelfById(long id);

    /**
     * 查询用户是否加入过书架
     * @param userId
     * @return
     */
    public List<StoryShelf> findStoryShelfByUidAndStoryId(long userId,long storyId);

    /**
     * 查询小说在书架的用户id
     * @param storyId
     * @return
     */
    public List<Long> findStoryShelfByStoryId(long storyId);
    
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
}
