package com.poison.eagle.entity;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.keel.common.lang.BaseDO;
import com.poison.eagle.utils.CheckParams;
/**
 * 电影 实例类是毒药APP中承载电影信息的java文件，使用详细规则如下：
 * 1.当返回电影为列表时，具体字段为：id,name,moviePic,score,director,totalTicket,weeklyTicket
 * 	   当返回电影为实体类时，返回全部字段
 * @author 温晓宁
 *
 */
public class MovieInfo extends BaseDO implements Comparable<MovieInfo>,Serializable {
	/**
	 * serialVersionUID = -8465809405065197359L;
	 */
	private static final long serialVersionUID = -8465809405065197359L;
	private long id;
	private String idStr;
	private String name;//电影名
	private String url;//豆瓣电影地址
	private String moviePic;//封面地址
	private String movieHPic;//横向封面地址
	private String score;//评分
	private String talentScore;//专家评分
	private List<String> director;//导演集合
	private List<String> writer;//编剧
	private List<String> actor;//演员集合
	private List<String> tags;//标签
	private List<String> language;//语言
	private List<String> countries;//制作国家
	private List<String> year;//上映时间国家
	private List<String> filmLength;//时长
	private String alias;//别名
	private String imdb;//imdb号
	private String describe;//简介
	private ResourceInfo resourceInfo;//我的评论资源
	private List photos;//剧照
	private List trailers;//预告片
	private String totalTicket;//总票房
	private String weeklyTicket ;//周票房
	private String rank;//同期排名
	private int cNum;//评论数量
	private int talentCommentNum;//专家评论数量
//	private int isDB;//
	private int inList = 1;//0:在我的影单中，1：没有
	private String searchNum;//搜索量
	private String hotNum;//热度
	private String chartMovie;
	
	public String getIdStr() {
		return idStr;
	}
	public void setIdStr(String idStr) {
		this.idStr = id+"";
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
		this.idStr = id+"";
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
//	public int getIsDB() {
//		return isDB;
//	}
//	public void setIsDB(int isDB) {
//		this.isDB = isDB;
//	}
	public String getMoviePic() {
		return moviePic;
	}
	public int getInList() {
		return inList;
	}
	public void setInList(int inList) {
		this.inList = inList;
	}
	public String getTalentScore() {
		return talentScore;
	}
	public void setTalentScore(String talentScore) {
		this.talentScore = talentScore;
	}
	public String getRank() {
		return rank;
	}
	public void setRank(String rank) {
		this.rank = rank;
	}
	public int getTalentCommentNum() {
		return talentCommentNum;
	}
	public void setTalentCommentNum(int talentCommentNum) {
		this.talentCommentNum = talentCommentNum;
	}
	public void setMoviePic(String moviePic) {
		moviePic = CheckParams.matcherMoviePic(moviePic);
		this.moviePic = moviePic;
	}
	public String getMovieHPic() {
		return movieHPic;
	}
	public void setMovieHPic(String movieHPic) {
		this.movieHPic = movieHPic;
	}
	public String getScore() {
		return score;
	}
	public void setScore(String score) {
		this.score = score;
	}
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public String getImdb() {
		return imdb;
	}
	public String getTotalTicket() {
		return totalTicket;
	}
	public void setTotalTicket(String totalTicket) {
		if("".equals(totalTicket)){
			this.totalTicket = this.totalTicket;
		}
		this.totalTicket = totalTicket;
	}
	public String getWeeklyTicket() {
		return weeklyTicket;
	}
	public void setWeeklyTicket(String weeklyTicket) {
		if("".equals(weeklyTicket)){
			this.weeklyTicket = this.weeklyTicket;
		}
		this.weeklyTicket = weeklyTicket;
	}
	public void setImdb(String imdb) {
		imdb = CheckParams.matcherIMDB(imdb);
		this.imdb = imdb;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		url = CheckParams.matcherMovieUrl(url);
		this.url = url;
	}
	public String getDescribe() {
		return describe;
	}
	public void setDescribe(String describe) {
		describe = CheckParams.putDescribeToHTML(describe);
		this.describe = describe;
	}
	public List<String> getDirector() {
		return director;
	}
	public List getPhotos() {
		return photos;
	}
	public void setPhotos(List photos) {
		this.photos = photos;
	}
	public List getTrailers() {
		return trailers;
	}
	public void setTrailers(List trailers) {
		this.trailers = trailers;
	}
	public void setDirector(String director) {
		this.director = CheckParams.putStringToList(director);
	}
	public List<String> getActor() {
		return actor;
	}
	public void setActor(String actor) {
		this.actor = CheckParams.putStringToList(actor);
	}
	public List<String> getTags() {
		return tags;
	}
	public void setTags(String tags) {
		this.tags = CheckParams.putStringToList(tags);
	}
	public List<String> getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = CheckParams.putStringToList(language);
	}
	public List<String> getCountries() {
		return countries;
	}
	public void setCountries(String countries) {
		this.countries = CheckParams.putStringToList(countries);
	}
	public List<String> getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = CheckParams.putStringToList(year);
	}
	public List<String> getFilmLength() {
		return filmLength;
	}
	public void setFilmLength(String filmLength) {
		this.filmLength = CheckParams.putStringToList(filmLength);
	}
	public List<String> getWriter() {
		return writer;
	}
	public void setWriter(String writer) {
		this.writer = CheckParams.putStringToList(writer);
	}
	public ResourceInfo getResourceInfo() {
		return resourceInfo;
	}
	public void setResourceInfo(ResourceInfo resourceInfo) {
		this.resourceInfo = resourceInfo;
	}
	public int getcNum() {
		return cNum;
	}
	public void setcNum(int cNum) {
		this.cNum = cNum;
	}
	public String getSearchNum() {
		return searchNum;
	}
	public void setSearchNum(String searchNum) {
		this.searchNum = searchNum;
	}
	public String getHotNum() {
		return hotNum;
	}
	public void setHotNum(String hotNum) {
		this.hotNum = hotNum;
	}
	public String getChartMovie() {
		return chartMovie;
	}
	public void setChartMovie(String chartMovie) {
		this.chartMovie = chartMovie;
	}
	@Override
	public int compareTo(MovieInfo o) {
		if(o.id>=this.id){
			return 1;
		}
		return -1;
	}
}
