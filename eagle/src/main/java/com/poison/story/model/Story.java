package com.poison.story.model;

import com.keel.common.lang.BaseDO;
import com.poison.eagle.utils.CheckParams;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: changjiang
 * Date: 15/11/26
 * Time: 17:55
 */
public class Story extends BaseDO{

    private static final long serialVersionUID = 4417331346968604829L;

    private long id;//主键id
    private long userId;//用户id
    private String channel;//频道1为男频 2为女频
    private String name;//小说名
    private String author;//作者名
    private String cover;//小说封面
    private String introduce;//简介
    private String tag;//标签
    private String type;//类型
    private int isPay;//是否付费
    private int price;//价格
    private int wordNumber;//总字数
    private String state;//状态 未发布，更新中，完结
    private String latestChapter;//最新章节
    private long createDate;//创建时间
    private long latestChapterTime;//最后更新时间
    private long latestRevisionDate;//最后更新时间
    private String flag;//标识符
    private String recommendIntroduce;//推荐语


    public long getLatestChapterTime() {
        return latestChapterTime;
    }

    public void setLatestChapterTime(long latestChapterTime) {
        this.latestChapterTime = latestChapterTime;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getRecommendIntroduce() {
        return recommendIntroduce;
    }

    public void setRecommendIntroduce(String recommendIntroduce) {
        this.recommendIntroduce = recommendIntroduce;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getIsPay() {
        return isPay;
    }

    public void setIsPay(int isPay) {
        this.isPay = isPay;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getWordNumber() {
        return wordNumber;
    }

    public void setWordNumber(int wordNumber) {
        this.wordNumber = wordNumber;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getLatestChapter() {
        return latestChapter;
    }

    public void setLatestChapter(String latestChapter) {
        this.latestChapter = latestChapter;
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    public long getLatestRevisionDate() {
        return latestRevisionDate;
    }

    public void setLatestRevisionDate(long latestRevisionDate) {
        this.latestRevisionDate = latestRevisionDate;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }
}
