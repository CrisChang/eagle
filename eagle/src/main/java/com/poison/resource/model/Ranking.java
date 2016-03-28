package com.poison.resource.model;

import com.keel.common.lang.BaseDO;

public class Ranking extends BaseDO implements Comparable<Ranking>{
	/**
	 * Ranking序列号
	 */
	private static final long serialVersionUID = 2011686277869123967L;
	
	public static final String type_latestmovierec = "1";//最新电影推荐榜
	public static final String type_latestdramarec = "2";//最新剧集推荐榜
	public static final String type_weekmoviesearch = "3";//每周电影热搜榜
	public static final String type_weekdramasearch = "4";//每周剧集热搜榜
	public static final String type_weekmovielist = "5";//每周热门影单榜
	public static final String type_weekbooksearch = "6";//每周图书热搜榜
	public static final String type_weekbooklist = "7";//每周热门书单榜
	public static final String type_latestbookrec = "8";//最新图书推荐榜
	
	
	private long id;//主鍵id
	private String ranktype;//排行类型
	private int isdel;//0表示删除，1表示未删除
	private long resid;//资源id
	private String restype;//资源类型
	private long score;//排序分值
	private long createtime;//创建时间
	private long updatetime;//更新时间
	
	
	private int flag;//标示
	
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getResid() {
		return resid;
	}

	public void setResid(long resid) {
		this.resid = resid;
	}

	public long getScore() {
		return score;
	}

	public void setScore(long score) {
		this.score = score;
	}

	public int getIsdel() {
		return isdel;
	}

	public void setIsdel(int isdel) {
		this.isdel = isdel;
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

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public String getRanktype() {
		return ranktype;
	}

	public void setRanktype(String ranktype) {
		this.ranktype = ranktype;
	}

	public String getRestype() {
		return restype;
	}

	public void setRestype(String restype) {
		this.restype = restype;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public int compareTo(Ranking s) {
		if(s.score==this.score){
			return 1;
		}
		return -1;
	}
}
