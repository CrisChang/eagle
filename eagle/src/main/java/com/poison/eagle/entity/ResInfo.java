package com.poison.eagle.entity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.keel.common.lang.BaseDO;
import com.poison.eagle.utils.CheckParams;
import com.poison.eagle.utils.CommentUtils;
import com.poison.resource.model.Article;
import com.poison.resource.model.BkComment;
import com.poison.resource.model.MvComment;
import com.poison.resource.model.Topic;
import com.poison.store.model.BkInfo;
import com.poison.store.model.MvInfo;
import com.poison.ucenter.model.UserAllInfo;
import com.poison.ucenter.model.UserInfo;
/**
 *新的资源类
 * 
 */
public class ResInfo extends BaseDO implements Comparable<ResInfo> ,Serializable{
	/**
	 * serialVersionUID = -8236557556314302627L;
	 */
	private static final long serialVersionUID = -8236557556316302627L;
	private static final String DATEFORMAT = "yyyy-MM-dd HH:mm:ss";
	private SimpleDateFormat sf;
	private long rid;//资源id
	private String ridStr;	//资源id字符串
	private String type;//资源类型
	private long userid;//用户id
	private String useridStr;//用户id字符窜
	private String title;//标题
	private String imageUrl;//图片地址
	private String content;//内容地址
	private String summary;//摘要、简介
	private String authorName;//作者
	private List<String> director;//导演集合
	private String btime;//创建时间 格式yyyy-MM-dd HH:mm:ss
	private String rNum;//转发数量
	private String cNum;//评论数量
	private String zNum;//赞数量
	private String fNum;//收藏数量
	private String lNum;//low数量
	private String readingCount;//阅读量
	private String searchNum;//搜索量
	private String talkcount;//讨论数量
	private String score;//评分
	private String talentScore;//专家评分
	private String hotNum;//热度
	private String voteNum="0";//投票数
	private int isDel = 0;//0：未删除、1：已删除
	private String usefulCount = "0"; //有用的数量
	private int atype;//文章类型，0:原创,1:转载
	
	private String sex;//性别
	private String sign;//个性签名
	private String age;//个人年龄
	private String identification;//身份认证
	private int isInterest = 2;//1:已关注、2：未关注、：0相互关注
	private int userType = 0;//0:正常用户、1：达人
	private String headImage = "";//用户头像地址
	private String tag;
	private UserEntity userEntity;
	
	public String getReadingCount() {
		return readingCount;
	}
	public void setReadingCount(String readingCount) {
		this.readingCount = readingCount;
	}
	public ResInfo() {
		super();
	}
	public long getRid() {
		return rid;
	}
	public void setRid(long rid) {
		this.rid = rid;
	}
	public String getRidStr() {
		return ridStr;
	}
	public void setRidStr(String ridStr) {
		this.ridStr = ridStr;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public String getBtime() {
		return btime;
	}
	public void setBtime(long btime) {
		if(sf == null){
			sf = new SimpleDateFormat(DATEFORMAT);
		}
		Date date = new Date(btime);
		this.btime = sf.format(date);
	}
	public void setBtime(String btime) {
		this.btime = btime;
	}
	public String getrNum() {
		return rNum;
	}
	public void setrNum(String rNum) {
		this.rNum = rNum;
	}
	public String getcNum() {
		return cNum;
	}
	public void setcNum(String cNum) {
		this.cNum = cNum;
	}
	public String getzNum() {
		return zNum;
	}
	public void setzNum(String zNum) {
		this.zNum = zNum;
	}
	public String getfNum() {
		return fNum;
	}
	public void setfNum(String fNum) {
		this.fNum = fNum;
	}
	public String getlNum() {
		return lNum;
	}
	public void setlNum(String lNum) {
		this.lNum = lNum;
	}
	public String getSearchNum() {
		return searchNum;
	}
	public void setSearchNum(String searchNum) {
		this.searchNum = searchNum;
	}
	public String getScore() {
		return score;
	}
	public void setScore(String score) {
		this.score = score;
	}
	public int getIsDel() {
		return isDel;
	}
	public void setIsDel(int isDel) {
		this.isDel = isDel;
	}
	public String getAuthorName() {
		return authorName;
	}
	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}
	public String getTalentScore() {
		return talentScore;
	}
	public void setTalentScore(String talentScore) {
		this.talentScore = talentScore;
	}
	public String getHotNum() {
		return hotNum;
	}
	public void setHotNum(String hotNum) {
		this.hotNum = hotNum;
	}
	public List<String> getDirector() {
		return director;
	}
	public void setDirector(List<String> director) {
		this.director = director;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public String getIdentification() {
		return identification;
	}
	public void setIdentification(String identification) {
		this.identification = identification;
	}
	public int getIsInterest() {
		return isInterest;
	}
	public void setIsInterest(int isInterest) {
		this.isInterest = isInterest;
	}
	public int getUserType() {
		return userType;
	}
	public void setUserType(int userType) {
		this.userType = userType;
	}
	public long getUserid() {
		return userid;
	}
	public void setUserid(long userid) {
		this.userid = userid;
		this.useridStr = userid+"";
	}
	public String getUseridStr() {
		return useridStr;
	}
	public void setUseridStr(String useridStr) {
		this.useridStr = useridStr;
	}
	public String getTalkcount() {
		return talkcount;
	}
	public void setTalkcount(String talkcount) {
		this.talkcount = talkcount;
	}
	public String getUsefulCount() {
		return usefulCount;
	}
	public void setUsefulCount(String usefulCount) {
		this.usefulCount = usefulCount;
	}
	public String getVoteNum() {
		return voteNum;
	}
	public void setVoteNum(String voteNum) {
		this.voteNum = voteNum;
	}
	public int getAtype() {
		return atype;
	}
	public void setAtype(int atype) {
		this.atype = atype;
	}
	public String getHeadImage() {
		return headImage;
	}
	public void setHeadImage(String headImage) {
		this.headImage = headImage;
	}
	public UserEntity getUserEntity() {
		return userEntity;
	}
	public void setUserEntity(UserEntity userEntity) {
		this.userEntity = userEntity;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public static ResInfo convert(Object obj){
		ResInfo info = null;
		if(obj!=null){
			info = new ResInfo();
			String objName = obj.getClass().getName();
			if(BookInfo.class.getName().equals(objName)){
				BookInfo bookInfo = (BookInfo)obj;
				info.setRid(bookInfo.getId());
				info.setRidStr(bookInfo.getId()+"");
				info.setTitle(bookInfo.getName());
				info.setImageUrl(bookInfo.getPagePic());
				info.setType(bookInfo.getType());
				info.setSummary(bookInfo.getIntroduction());
				info.setAuthorName(bookInfo.getAuthorName());
				info.setcNum(bookInfo.getcNum()+"");
				info.setSearchNum(bookInfo.getSearchNum());
				info.setHotNum(bookInfo.getHotNum());
				info.setScore(bookInfo.getScore());
				info.setTalentScore(bookInfo.getTalentScore());
				info.setBtime(bookInfo.getPublishTime());
			}else if(BkInfo.class.getName().equals(objName)){
				BkInfo bkInfo = (BkInfo)obj;
				info.setRid(bkInfo.getId());
				info.setRidStr(bkInfo.getId()+"");
				info.setTitle(bkInfo.getName());
				info.setImageUrl(bkInfo.getBookPic());
				info.setType(bkInfo.getResType());
				info.setSummary(bkInfo.getContent());
				info.setAuthorName(bkInfo.getAuthorName());
			}else if(MvInfo.class.getName().equals(objName)){
				MvInfo mvInfo = (MvInfo)obj;
				info.setRid(mvInfo.getId());
				info.setRidStr(mvInfo.getId()+"");
				info.setTitle(mvInfo.getName());
				info.setImageUrl(CheckParams.matcherMoviePic(mvInfo.getMoviePic()));
				info.setType(CommentUtils.TYPE_MOVIE);
				info.setSummary(mvInfo.getDescription());
				info.setDirector(CheckParams.putStringToList(mvInfo.getDirector()));
				info.setScore(mvInfo.getScore());
			}else if(MovieInfo.class.getName().equals(objName)){
				MovieInfo movieInfo = (MovieInfo)obj;
				info.setRid(movieInfo.getId());
				info.setRidStr(movieInfo.getId()+"");
				info.setTitle(movieInfo.getName());
				info.setImageUrl(movieInfo.getMoviePic());
				info.setType(CommentUtils.TYPE_MOVIE);
				info.setSummary(movieInfo.getDescribe());
				info.setDirector(movieInfo.getDirector());
				info.setScore(movieInfo.getScore());
				info.setcNum(movieInfo.getcNum()+"");
				info.setSearchNum(movieInfo.getSearchNum());
				info.setHotNum(movieInfo.getHotNum());
				info.setTalentScore(movieInfo.getTalentScore());
			}else if(UserEntity.class.getName().equals(objName)){
				UserEntity userEntity = (UserEntity)obj;
				info.setRid(userEntity.getId());
				info.setRidStr(userEntity.getId()+"");
				info.setTitle(userEntity.getNickName());
				info.setImageUrl(userEntity.getFace_url());
				info.setType(CommentUtils.TYPE_USER);
				info.setSummary(userEntity.getIntroduction());
				info.setAge(userEntity.getAge());
				info.setIdentification(userEntity.getIdentification());
				info.setIsInterest(userEntity.getIsInterest());
				info.setSex(userEntity.getSex());
				info.setSign(userEntity.getSign());
				info.setUserType(userEntity.getType());
			}else if(UserAllInfo.class.getName().equals(objName)){
				UserAllInfo ui = (UserAllInfo) obj;
				info.setRid(ui.getUserId());
				info.setRidStr(ui.getUserId()+"");
				info.setTitle(ui.getName());
				info.setImageUrl(ui.getFaceAddress());
				info.setSign(ui.getSign());
				info.setSex(ui.getSex());
				info.setSummary(ui.getIntroduction());
				info.setIdentification(ui.getIdentification());
				info.setUserType(ui.getLevel());
				info.setBtime(ui.getCreateDate());
				info.setType(CommentUtils.TYPE_USER);
			}else if(UserInfo.class.getName().equals(objName)){
				UserInfo ui = (UserInfo) obj;
				info.setRid(ui.getUserId());
				info.setRidStr(ui.getUserId()+"");
				info.setTitle(ui.getName());
				info.setImageUrl(ui.getFaceAddress());
				info.setSex(ui.getSex());
				info.setUserType(ui.getLevel());
				info.setBtime(ui.getCreateDate());
				info.setType(CommentUtils.TYPE_USER);
			}else if(TopicInfo.class.getName().equals(objName)){
				TopicInfo topicInfo = (TopicInfo)obj;
				info.setRid(topicInfo.getId());
				info.setRidStr(topicInfo.getIdstr());
				info.setTitle(topicInfo.getTitle());
				info.setImageUrl(topicInfo.getCover());
				info.setUserid(topicInfo.getUserid());
				info.setUseridStr(topicInfo.getUserid()+"");
				info.setSummary(topicInfo.getDescription());
				info.setReadingCount(topicInfo.getReadcount()+"");
				info.setTalkcount(topicInfo.getTalkcount()+"");
				info.setBtime(topicInfo.getCreateDate());
				info.setType(CommentUtils.TYPE_TOPIC);
			}else if(Topic.class.getName().equals(objName)){
				Topic topic = (Topic)obj;
				info.setRid(topic.getId());
				info.setRidStr(topic.getId()+"");
				info.setTitle(topic.getTitle());
				info.setImageUrl(topic.getCover());
				info.setUserid(topic.getUserid());
				info.setUseridStr(topic.getUserid()+"");
				info.setSummary(topic.getDescription());
				info.setReadingCount((topic.getReadcount()+topic.getFalsereading())+"");
				info.setTalkcount(topic.getTalkcount()+"");
				info.setBtime(topic.getCreateDate());
				info.setType(CommentUtils.TYPE_TOPIC);
			}else if(Article.class.getName().equals(objName)){
				Article article = (Article)obj;
				info.setRid(article.getId());
				info.setRidStr(article.getId()+"");
				info.setTitle(article.getName());
				info.setContent(article.getContent());
				info.setImageUrl(article.getCover());
				info.setType(CommentUtils.TYPE_NEWARTICLE);
				info.setUserid(article.getUid());
				info.setUseridStr(article.getUid()+"");
				info.setBtime(article.getBeginDate());
				info.setReadingCount((article.getReadingCount()+article.getFalsereading())+"");
				info.setSummary(article.getSummary());
				info.setAtype(article.getAtype());
			}else if(MvComment.class.getName().equals(objName)){
				MvComment mvComment = (MvComment)obj;
				info.setRid(mvComment.getId());
				info.setRidStr(mvComment.getId()+"");
				info.setTitle(mvComment.getTitle());
				info.setContent(mvComment.getContent());
				info.setImageUrl(mvComment.getCover());
				info.setType(mvComment.getResourceType());
				info.setUserid(mvComment.getUserId());
				info.setUseridStr(mvComment.getUserId()+"");
				info.setBtime(mvComment.getCreateDate());
				info.setReadingCount(0+"");
				info.setSummary(mvComment.getDescription());
				info.setScore(mvComment.getScore());
				info.setTag(mvComment.getTag());
			}else if(BkComment.class.getName().equals(objName)){
				BkComment bkComment = (BkComment)obj;
				info.setRid(bkComment.getId());
				info.setRidStr(bkComment.getId()+"");
				info.setTitle(bkComment.getTitle());
				info.setContent(bkComment.getComment());
				info.setImageUrl(bkComment.getCover());
				info.setType(bkComment.getResourceType());
				info.setUserid(bkComment.getUserId());
				info.setUseridStr(bkComment.getUserId()+"");
				info.setBtime(bkComment.getCreateDate());
				info.setReadingCount(0+"");
				info.setSummary(bkComment.getDescription());
				info.setScore(bkComment.getScore());
				info.setTag(bkComment.getTag());
			}else if(ResourceInfo.class.getName().equals(objName)){
				ResourceInfo resourceInfo = (ResourceInfo)obj;
				info.setRid(resourceInfo.getRid());
				info.setRidStr(resourceInfo.getRidStr());
				info.setTitle(resourceInfo.getTitle());
				info.setContent(resourceInfo.getContUrl());
				info.setImageUrl(resourceInfo.getImageUrl());
				info.setType(resourceInfo.getType());
				if(resourceInfo.getUserEntity()!=null){
					info.setUserid(resourceInfo.getUserEntity().getId());
				}
				info.setBtime(resourceInfo.getBtime());
				info.setReadingCount(resourceInfo.getReadingCount());
				info.setSummary(resourceInfo.getSummary());
				info.setAtype(resourceInfo.getAtype());
			}
		}
		return info;
	}
	
	@Override
	public int compareTo(ResInfo o) {
		if(o.rid>=this.rid){
			return 1;
		}
		return -1;
	}
}
