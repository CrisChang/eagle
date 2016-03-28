package com.poison.story.model;

import com.keel.common.lang.BaseDO;
import com.poison.eagle.utils.DateUtil;

/**
 * 
 * @author weizhensong
 *
 */
public class StoryPay extends BaseDO{


    private static final long serialVersionUID = -6623811363788199115L;

    private long id;//主键id
    private long chapterId;//章节id
    private long userId;//用户id
    private int payed;//是否已经付费
    private int payAmount;//付费金额（金币数量）
    private long createtime;//创建时间
    private String createtimestr;//创建时间
    
    private String flag;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
    
	public long getChapterId() {
		return chapterId;
	}

	public void setChapterId(long chapterId) {
		this.chapterId = chapterId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public int getPayed() {
		return payed;
	}

	public void setPayed(int payed) {
		this.payed = payed;
	}

	public int getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(int payAmount) {
		this.payAmount = payAmount;
	}

	public long getCreatetime() {
		return createtime;
	}

	public void setCreatetime(long createtime) {
		this.createtime = createtime;
		this.createtimestr = DateUtil.format(createtime, DateUtil.timeformat);
	}

	public String getCreatetimestr() {
		return createtimestr;
	}

	public void setCreatetimestr(String createtimestr) {
		this.createtimestr = createtimestr;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}
}
