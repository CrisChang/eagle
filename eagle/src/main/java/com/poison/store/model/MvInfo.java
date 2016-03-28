package com.poison.store.model;

import com.keel.common.lang.BaseDO;
import com.poison.eagle.utils.CheckParams;

public class MvInfo extends BaseDO implements Comparable<MvInfo> {

	
	
	/**
	 * MvInfo的序列号
	 */
	private static final long serialVersionUID = -7378279127968154986L;
	private long id;
	private String movieUrl;// 上传路径
	private String moviePic;// 封面
	private String name;// 影片名称
	private String score;// 分数
	private String director;// 导演
	private String screenWriter;// 编剧
	private String actor;// 主要演员
	private String tags;// 分类或者标签
	private String userTags;// 用户给打的标签
	private String proCountries;// 制作国家或地区
	private String language;// 语种
	private String releaseDate;// 上映日期
	private String about;// 关联的连续剧,二季 三季啥的
	private int number;// 连续剧的集数
	private String filmTime;// 电影时长或者单集时长
	private String alias;// 别名
	private String imdbLink;//
	private String boxOffice;//票房
	private String weekBoxOffice;//周票房
	private String description;// 描述
	private int collTime;// 采集时间
	private String type;//类型
	private String rankingList;//同期排名
 	private long flag;
 	private long releaseDateSort;
 	private String moviePic2;
 	private String extendField1;

	public String getRankingList() {
		return rankingList;
	}

	public void setRankingList(String rankingList) {
		
		this.rankingList = CheckParams.formatStringForInfo(rankingList);
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = CheckParams.formatStringForInfo(type);
	}

	public String getWeekBoxOffice() {
		return weekBoxOffice;
	}

	public void setWeekBoxOffice(String weekBoxOffice) {
		this.weekBoxOffice = CheckParams.formatStringForInfo(weekBoxOffice);
	}

	public String getBoxOffice() {
		return boxOffice;
	}

	public void setBoxOffice(String boxOffice) {
		this.boxOffice = CheckParams.formatStringForInfo(boxOffice);
	}

	public long getFlag() {
		return flag;
	}

	public void setFlag(long flag) {
		this.flag = flag;
	}

	public MvInfo() {
		super();
	}

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
		this.movieUrl = CheckParams.formatStringForInfo(movieUrl);
	}

	public String getMoviePic() {
		return moviePic;
	}

	public void setMoviePic(String moviePic) {
		this.moviePic = CheckParams.formatStringForInfo(moviePic);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = CheckParams.formatStringForInfo(name);
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = CheckParams.formatStringForInfo(score);
	}

	public String getDirector() {
		return director;
	}

	public void setDirector(String director) {
		this.director = CheckParams.formatStringForInfo(director);
	}

	public String getScreenWriter() {
		return screenWriter;
	}

	public void setScreenWriter(String screenWriter) {
		this.screenWriter = CheckParams.formatStringForInfo(screenWriter);
	}

	public String getActor() {
		return actor;
	}

	public void setActor(String actor) {
		this.actor = CheckParams.formatStringForInfo(actor);
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = CheckParams.formatStringForInfo(tags);
	}


	public String getUserTags() {
		return userTags;
	}

	public void setUserTags(String userTags) {
		this.userTags = CheckParams.formatStringForInfo(userTags);
	}

	public String getProCountries() {
		return proCountries;
	}

	public void setProCountries(String proCountries) {
		this.proCountries = CheckParams.formatStringForInfo(proCountries);
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = CheckParams.formatStringForInfo(language);
	}


	public String getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(String releaseDate) {
		this.releaseDate = CheckParams.formatStringForInfo(releaseDate);
	}

	public String getAbout() {
		return about;
	}

	public void setAbout(String about) {
		this.about = CheckParams.formatStringForInfo(about);
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
		this.filmTime = CheckParams.formatStringForInfo(filmTime);
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = CheckParams.formatStringForInfo(alias);
	}

	public String getImdbLink() {
		return imdbLink;
	}

	public void setImdbLink(String imdbLink) {
		this.imdbLink = CheckParams.formatStringForInfo(imdbLink);
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		if(null==description){
			description = "";
		}
		this.description = CheckParams.formatStringForInfo(description.replace("&copy;豆瓣", ""));
	}

	public int getCollTime() {
		return collTime;
	}

	public void setCollTime(int collTime) {
		this.collTime = collTime;
	}

	public long getReleaseDateSort() {
		return releaseDateSort;
	}

	public void setReleaseDateSort(long releaseDateSort) {
		this.releaseDateSort = releaseDateSort;
	}
	
	public String getMoviePic2() {
		return moviePic2;
	}

	public void setMoviePic2(String moviePic2) {
		this.moviePic2 = moviePic2;
	}

	public String getExtendField1() {
		return extendField1;
	}

	public void setExtendField1(String extendField1) {
		this.extendField1 = extendField1;
	}

	@Override
	public int compareTo(MvInfo o) {
		if (o.id == this.id) {
			return 1;
		}
		return 0;
	}
}
