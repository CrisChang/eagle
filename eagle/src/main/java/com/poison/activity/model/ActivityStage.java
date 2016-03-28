package com.poison.activity.model;

import java.io.Serializable;

import com.keel.common.lang.BaseDO;
/**
 * 活动阶段表
 * @Description:
 * @author Weizhensong
 *
 */
public class ActivityStage extends BaseDO implements Comparable<ActivityStage>,Serializable{
	
	public static final int state_init=0;//初始状态
	public static final int state_start1=1;//开始状态（不允许报名）
	public static final int state_start2=2;//开始状态（允许报名）
	public static final int state_stop=3;//停止状态
	
	public static final String type_mvcomment="1";//影评大赛类型阶段
	/**
	 * ActivityStage序列号
	 */
	private static final long serialVersionUID = 3685207267875351883L;
	private long id;				//主键
	private String title;			//阶段名称
	private int state;				//阶段状态
	private long createtime;				//保存时间
	private long updatetime;			//更新时间
	private int isdel;				//是否删除
	private int score;					//排序值，值越高代表阶段越新，最高的代表最新的阶段
	private String type;			//区分活动类型
	private int flag;//
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public int getState() {
		return state;
	}


	public void setState(int state) {
		this.state = state;
	}


	public long getCreatetime() {
		return createtime;
	}


	public void setCreatetime(long createtime) {
		this.createtime = createtime;
	}


	public long getUpdatetime() {
		return updatetime;
	}


	public void setUpdatetime(long updatetime) {
		this.updatetime = updatetime;
	}


	public int getIsdel() {
		return isdel;
	}


	public void setIsdel(int isdel) {
		this.isdel = isdel;
	}


	public int getScore() {
		return score;
	}


	public void setScore(int score) {
		this.score = score;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getFlag() {
		return flag;
	}


	public void setFlag(int flag) {
		this.flag = flag;
	}


	@Override
	public int compareTo(ActivityStage o) {
		if(o.score>=this.score){
			return 1;
		}
		return -1;
	}
}
