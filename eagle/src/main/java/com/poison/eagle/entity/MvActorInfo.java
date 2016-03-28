package com.poison.eagle.entity;

import com.keel.common.lang.BaseDO;

/**
 * 电影角色 Bo 供返回给客户端使用
 * 
 * @author :zhangqi
 * @time:2015-6-10下午12:05:26
 * @return
 */
public class MvActorInfo extends BaseDO {

	private static final long serialVersionUID = 1L;

	private long mvId; // 电影ID
	private String moivePic; // 电影封面
	private String movieName;// 电影名字
	private String actorName;// 角色名字
	private String releaseDate;// 上映日期
	private String directors;// 导演

	public long getMvId() {
		return mvId;
	}

	public void setMvId(long mvId) {
		this.mvId = mvId;
	}

	public String getMoivePic() {
		return moivePic;
	}

	public void setMoivePic(String moivePic) {
		this.moivePic = moivePic;
	}

	public String getMovieName() {
		return movieName;
	}

	public void setMovieName(String movieName) {
		this.movieName = movieName;
	}

	public String getActorName() {
		return actorName;
	}

	public void setActorName(String actorName) {
		this.actorName = actorName;
	}

	public String getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(String releaseDate) {
		this.releaseDate = releaseDate;
	}

	public String getDirectors() {
		return directors;
	}

	public void setDirectors(String directors) {
		this.directors = directors;
	}

}
