package com.poison.eagle.entity;

import com.keel.common.lang.BaseDO;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: changjiang
 * Date: 15/12/1
 * Time: 15:40
 */
public class StoryChapterInfo extends BaseDO{


    private static final long serialVersionUID = -6623811363788199105L;

    private static final String DATEFORMAT = "yyyy-MM-dd HH:mm:ss";
    private SimpleDateFormat sf;

    private long id;//主键id
    private long storyId;//小说id
    private long userId;//用户id
    private String name;//章节名称
    private int wordNumber;//章节字数
    private int price;//章节价格
    private int range;//章节顺序
    private int isPay;//是否付费
    private int payed;//是否已经付费
    private String createDate;//创建时间
    private String latestRevisionDate;//最后修改时间
    private String introduce;//作者附加语
    
    private String flag;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getStoryId() {
        return storyId;
    }

    public void setStoryId(long storyId) {
        this.storyId = storyId;
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

    public int getWordNumber() {
        return wordNumber;
    }

    public void setWordNumber(int wordNumber) {
        this.wordNumber = wordNumber;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public int getIsPay() {
        return isPay;
    }

    public void setIsPay(int isPay) {
        this.isPay = isPay;
    }
    public int getPayed() {
		return payed;
	}

	public void setPayed(int payed) {
		this.payed = payed;
	}

	public String getCreateDate() {
        return createDate;
    }

    public String getLatestRevisionDate() {
        return latestRevisionDate;
    }
    
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public void setLatestRevisionDate(String latestRevisionDate) {
		this.latestRevisionDate = latestRevisionDate;
	}
	
	public String getIntroduce() {
		return introduce;
	}

	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}
}
