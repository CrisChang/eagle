package com.poison.eagle.utils;

import java.io.Serializable;


public class DBMovieDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private long id;
	
	private String movieUrl;	//豆瓣电影的连接
	
	private String moviePic;	//豆瓣电影图片保存路径
	
	private String score;		//综合评分
	
	private String name;		//名称
	
	private String director;	//导演
	
	private String screenwriter;//编剧
	
	private String actor;		//主要演员
	
	private String tags;		//电影类型或者标签
	
	private String userTags;	//用户给打的标签

	private String proCountries;//制作国家或地区
	
	private String language;	//语种
	
	private String releaseDate; //上映日期
	
	private String about;		//关联的连续剧,二季 三季啥的
	
	private int number;			//连续剧的集数
	
	private String filmTime;	//电影时长或者单集时长
	
	private String alias;		//别名
	
	private String imdbLink;	//影视库连接
	
	private String describe;	//电影简介
	
	private long collTime;		//采集时间
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getMovieUrl() {
		return movieUrl;
	}

	public void setMovieUrl(String movieUrl) {
		this.movieUrl = movieUrl;
	}

	public String getMoviePic() {
		return moviePic;
	}

	public void setMoviePic(String moviePic) {
		this.moviePic = moviePic;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDirector() {
		return director;
	}

	public void setDirector(String director) {
		this.director = director;
	}

	public String getScreenwriter() {
		return screenwriter;
	}

	public void setScreenwriter(String screenwriter) {
		this.screenwriter = screenwriter;
	}

	public String getActor() {
		return actor;
	}

	public void setActor(String actor) {
		this.actor = actor;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public String getUserTags() {
		return userTags;
	}

	public void setUserTags(String userTags) {
		this.userTags = userTags;
	}

	public String getProCountries() {
		return proCountries;
	}

	public void setProCountries(String proCountries) {
		this.proCountries = proCountries;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(String releaseDate) {
		this.releaseDate = releaseDate;
	}

	public String getAbout() {
		return about;
	}

	public void setAbout(String about) {
		this.about = about;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public String getFilmTime() {
		return filmTime;
	}

	public void setFilmTime(String filmTime) {
		this.filmTime = filmTime;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getImdbLink() {
		return imdbLink;
	}

	public void setImdbLink(String imdbLink) {
		this.imdbLink = imdbLink;
	}

	public String getDescribe() {
		return describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}

	public long getCollTime() {
		return collTime;
	}

	public void setCollTime(long collTime) {
		this.collTime = collTime;
	}	
}
