package com.poison.story.client;

import com.poison.story.model.Story;
import com.poison.story.model.StoryChapter;
import com.poison.story.model.StoryPay;
import com.poison.story.model.StoryPromote;
import com.poison.story.model.StoryShelf;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: changjiang
 * Date: 15/11/26
 * Time: 17:58
 */
public interface StoryFacade {

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
     * 查询书架的id
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
     * @return
     */
    public StoryShelf insertStoryShelf(long userId,long storyId);

    /**
     * 删除书架
     * @param id
     * @return
     */
    public int deleteStoryShelf(long id,long userId);

    /**
     * 是否在书架上
     * @param userId
     * @param storyId
     * @return
     */
    public boolean isOnShelf(long userId, long storyId);
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
    public int saveStoryPay(long chapterId,long userId,int payAmount);
    /**
     * 根据条件查询小说运营信息
     * @param promote 封装查询条件的实体
     * @param l 起始页标
     * @param pageSize  页面大小
     * @return 查到的小说运营信息
     */
    public List<StoryPromote> findStoryPromoteByCondition(StoryPromote promote,long l,int pageSize);

    /**
     * 查询书架中订阅这本小说的用户列表
     * @param storyId
     * @return
     */
    public List<Long> findStoryShelfByStoryId(long storyId);
}
