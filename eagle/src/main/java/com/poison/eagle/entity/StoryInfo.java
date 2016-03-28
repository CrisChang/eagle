package com.poison.eagle.entity;

import com.keel.common.lang.BaseDO;
import com.poison.eagle.utils.CheckParams;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: changjiang
 * Date: 15/12/1
 * Time: 12:31
 */
public class StoryInfo extends BaseDO{

    private static final String DATEFORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final long serialVersionUID = 1090861823026754428L;
    private SimpleDateFormat sf;

    private long id;//主键id
    private long userId;//用户id
    private String name;//小说名
    private String author;//作者名
    private String cover;//小说封面
    private String introduce;//简介
    private String recommendedIntroduce;//推荐语
    private String advert;//广告语
    private List<String> tag;//标签
    private String type;//类型
    private int wordNumber;//总字数
    private String state;//状态 未发布，更新中，完结
    private int isPay;//是否付费 0免费  1付费
    private String latestChapter;//最新章节
    private int monthTicket;//月票
    private int recommendTicket;//推荐票
    private int isOnShelf;//是否加入书架
    private int chapterNumber;//章节数
    private int clickRate;//点击量
    private String createDate;//创建时间
    private String latestRevisionDate;//最后更新时间

    public String getAdvert() {
        return advert;
    }

    public void setAdvert(String advert) {
        this.advert = advert;
    }

    public String getRecommendedIntroduce() {
        return recommendedIntroduce;
    }

    public void setRecommendedIntroduce(String recommendedIntroduce) {
        this.recommendedIntroduce = recommendedIntroduce;
    }

    public int getIsPay() {
        return isPay;
    }

    public void setIsPay(int isPay) {
        this.isPay = isPay;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getClickRate() {
        return clickRate;
    }

    public void setClickRate(int clickRate) {
        this.clickRate = clickRate;
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

    public List<String> getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = CheckParams.putStringToList(tag);
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

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        if(sf == null){
            sf = new SimpleDateFormat(DATEFORMAT);
        }
        Date date = new Date(createDate);
        this.createDate = sf.format(date);
    }

    public String getLatestRevisionDate() {
        return latestRevisionDate;
    }

    public void setLatestRevisionDate(long latestRevisionDate) {
        if(sf == null){
            sf = new SimpleDateFormat(DATEFORMAT);
        }
        Date date = new Date(latestRevisionDate);
        this.latestRevisionDate = sf.format(date);
    }

    public int getMonthTicket() {
        return monthTicket;
    }

    public void setMonthTicket(int monthTicket) {
        this.monthTicket = monthTicket;
    }

    public int getRecommendTicket() {
        return recommendTicket;
    }

    public void setRecommendTicket(int recommendTicket) {
        this.recommendTicket = recommendTicket;
    }

    public int getIsOnShelf() {
        return isOnShelf;
    }

    public void setIsOnShelf(int isOnShelf) {
        this.isOnShelf = isOnShelf;
    }

    public int getChapterNumber() {
        return chapterNumber;
    }

    public void setChapterNumber(int chapterNumber) {
        this.chapterNumber = chapterNumber;
    }
}
