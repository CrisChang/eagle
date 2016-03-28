package com.poison.eagle.entity;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.codehaus.jackson.type.TypeReference;

import com.keel.common.lang.BaseDO;
import com.poison.eagle.manager.TalentZoneManager;
import com.poison.eagle.utils.CheckParams;
import com.poison.eagle.utils.CommentUtils;
/**
 * ResourceInfo（资源类）是毒药APP中最常用到的实体类，因为软件中设计到的资源众多，为方便开发将所有的资源类统一归总成为资源类，下面是一些常见规则和注意事项：
 * 1.rid为资源id可以使任何资源的id，以及其他字段的具体意义需要根据type类型 来定义。
 * 2.type分类：0：书单、1：影单、2：连载、3：日志、6：书评、7：影评、8：连载章节、9：电影讨论、10：书讨论、12：转发、17：收藏、21：相册、22：长微博、26：发现页、27：书、28：电影
 * 3.根据type不同首页会使用不同的字
 * 		都会有的字段为：rid,type,title,contList,rNum,cNum,zNum,fNum,lNum,userEntity
 * 		0：bookListId,reason;
 * 		1:movieListId,reason;
 * 		6:bkCommentId,score
 * 		7:mvCommentId,score;
 * 		12:ResourceInfo(转发为资源套资源外层资源为上面常用字段，内层为各个资源结构)
 * 		17:rid为收藏id，具体收藏结构根据type与其他类型相同
 * 		22:imageUrl,contUrl,readingCount;
 * @author 温晓宁
 * 
 */
public class ResourceInfo extends BaseDO implements Comparable<ResourceInfo> ,Serializable{
	/**
	 * serialVersionUID = -8236557556314302627L;
	 */
	private static final long serialVersionUID = -8236557556314302627L;
	private static final  Log LOG = LogFactory.getLog(ResourceInfo.class);
	private static final String DATEFORMAT = "yyyy-MM-dd HH:mm:ss";
	private SimpleDateFormat sf;
	private long rid;//资源id
	private String ridStr;	//资源id字符串
	private long bookListId;//书单id
	private String bookListIdStr;//书单id字符串
	private long movieListId;//影单id
	private String movieListIdStr;//影单id字符串
	private long bkCommentId;//书评id
	private String bkCommentIdStr;//书评id字符串
	private long mvCommentId;//影评id
	private String mvCommentIdStr;//影评id字符串
	private long diaryId;//日志id
	private String diaryIdStr;//日志id字符串
	
	private long linkId;//link表的主键id，在相关功能会使用，具体见接口说明
	private String linkIdStr;//link表的主键id字符串，在相关功能会使用，具体见接口说明
	private String type;//资源类型
	private String title;//标题
	private String imageUrl;//图片地址
	private String contUrl;//内容地址
	private int atype;//0:原创，1：转载
	private String score;//分数
	private List<Map<String, String>> images;//组图字段  title文字说明，url图片地址
	/**
	 * conList是资源中最常用到的字段，将用户产生的本文图片等信息都汇总到该字段中：
	 * 1.结构：[{"type":"0","content":"内容"},{"type":"1","content":"图片地址"}]
	 * 2.type分类：0:文本内容、1:图片地址、2:音频地址、3:视频地址
	 */
	private List<Map> contList;//内容数组
	private String reason;//推荐理由
	private String summary;//摘要
	private String btime;//创建时间 格式yyyy-MM-dd HH:mm:ss
	private int size;//各种可能的数量
	private int rNum;//转发数量
	private int cNum;//评论数量
	private int zNum;//赞数量
	private int fNum;//收藏数量
	private int lNum;//low数量
	private String readingCount;//阅读量
	private int isCollect = 1;//0：收藏、1：没有收藏
	private int isPraise = 1;//0:赞过,1:没赞过
	private int isLow = 1;//0：low，1：没low
	private int isDel = 0;//0：未删除、1：已删除
	private int isUseful = 0;//是否有用 0没选择、1有用、2没用
	private String usefulCount = "0"; //有用的数量
	private String uselessCount = "0";//没用的数量
	private String voteNum;//投票数
	private String friendName;//朋友的朋友名称、在书单中代表 书单中书的属性之一
	private String address;//地理位置
	private List<String> tags;//标签列表
	private String remark;//备注
	private int money;//打赏总金额，单位：分
	private UserEntity userEntity;//用户实例类
	private SerializeInfo serializeInfo;//连载实例类
	private BookInfo bookInfo;//书的实例类
	private MovieInfo movieInfo;//影的实例类
	private ResourceInfo resourceInfo;//转发或@的时候的内嵌资源类
	private int isRead;//0：读过、1：未读过
	private int inList = 1;//0:在书单中，1：没有
	private int flag;//数据类型：1：用户产生，2：运营数据，3；推广数据
	private String lon;//经度
	private String lat;//维度
	private String locationName;//地点名字
	private String locationCity;//地点城市
	private String locationArea;//地点地区
	private String distance = "0";//距离多远
	private String readTotal = "0";//看过多少部书或电影
	private String point;//评分
	private long stageid;//阶段id
	
	private List<MovieInfo> movieInfos;//影单中电影的集合
	private List<BookInfo> bookInfos;//书单中的书的集合
	
	
	public String getReadTotal() {
		return readTotal;
	}
	public void setReadTotal(String readTotal) {
		this.readTotal = readTotal;
	}
	public String getReadingCount() {
		return readingCount;
	}
	public void setReadingCount(String readingCount) {
		this.readingCount = readingCount;
	}
	public ResourceInfo() {
		super();
	}
	public ResourceInfo(UserEntity userEntity, long rid, String type,
			String title, List contList, long btime, int relayNum,
			int commentNum, int praizeNum) {
		super();
		if(sf == null){
			sf = new SimpleDateFormat(DATEFORMAT);
		}
		Date date = new Date(btime);
		this.rid = rid;
		this.ridStr = rid+"";
		this.type = type;
		this.title = title;
		this.contList = contList;
		this.btime = sf.format(date);
		this.rNum = relayNum;
		this.cNum = commentNum;
		this.zNum = praizeNum;
		this.userEntity = userEntity;
	}
	
	
	public int getIsUseful() {
		return isUseful;
	}
	public void setIsUseful(int isUseful) {
		this.isUseful = isUseful;
	}
	public String getUsefulCount() {
		return usefulCount;
	}
	public void setUsefulCount(String usefulCount) {
		this.usefulCount = usefulCount;
	}
	public String getUselessCount() {
		return uselessCount;
	}
	public void setUselessCount(String uselessCount) {
		this.uselessCount = uselessCount;
	}
	public String getLocationCity() {
		return locationCity;
	}
	public void setLocationCity(String locationCity) {
		this.locationCity = locationCity;
	}
	public String getLocationArea() {
		return locationArea;
	}
	public void setLocationArea(String locationArea) {
		this.locationArea = locationArea;
	}
	public String getLon() {
		return lon;
	}
	public void setLon(String lon) {
		this.lon = lon;
	}
	public String getLat() {
		return lat;
	}
	public void setLat(String lat) {
		this.lat = lat;
	}
	public String getLocationName() {
		return locationName;
	}
	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}
	public String getDistance() {
		return distance;
	}
	public void setDistance(String distance) {
		this.distance = distance;
	}
	public String getRidStr() {
		return ridStr;
	}
	public void setRidStr(String ridStr) {
		this.ridStr = rid+"";
	}
	
	public String getBookListIdStr() {
		return bookListIdStr;
	}
	public void setBookListIdStr(String bookListIdStr) {
		this.bookListIdStr = bookListId+"";
	}
	public String getMovieListIdStr() {
		return movieListIdStr;
	}
	public void setMovieListIdStr(String movieListIdStr) {
		this.movieListIdStr = movieListId+"";
	}
	public String getBkCommentIdStr() {
		return bkCommentIdStr;
	}
	public void setBkCommentIdStr(String bkCommentIdStr) {
		this.bkCommentIdStr = bkCommentId+"";
	}
	public String getMvCommentIdStr() {
		return mvCommentIdStr;
	}
	public void setMvCommentIdStr(String mvCommentIdStr) {
		this.mvCommentIdStr = mvCommentId+"";
	}
	public String getDiaryIdStr() {
		return diaryIdStr;
	}
	public void setDiaryIdStr(String diaryIdStr) {
		this.diaryIdStr = diaryId+"";
	}
	public String getLinkIdStr() {
		return linkIdStr;
	}
	public void setLinkIdStr(String linkIdStr) {
		this.linkIdStr = linkId+"";
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public List<String> getTags() {
		return tags;
	}
	public void setTags(String tags) {
		this.tags = CheckParams.getListFromString(tags);
	}
	public int getInList() {
		return inList;
	}
	public void setInList(int inList) {
		this.inList = inList;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public void setBtime(String btime) {
		this.btime = btime;
	}
	public long getRid() {
		return rid;
	}
	/*public int getReadingCount() {
		return readingCount;
	}*/
	public List<Map<String, String>> getImages() {
		return images;
	}
	public void setImages(String images) {
		ObjectMapper objectMapper  = new ObjectMapper();
		
		objectMapper.setSerializationInclusion(Inclusion.NON_NULL);
		objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
		objectMapper.configure(DeserializationConfig.Feature.WRAP_EXCEPTIONS, false) ;
		
		List<Map<String, String>> imgs = new ArrayList<Map<String,String>>();
		try {
			imgs = objectMapper.readValue(images,  new TypeReference<List<Map<String, String>>>(){});
		} catch (JsonParseException e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
		} catch (JsonMappingException e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
		} catch (IOException e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
		}
		this.images = imgs;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		
		try {
			if(summary == null || "".equals(summary)){
				summary = "";
			}
			if(summary.length() > CommentUtils.RESOURCE_CONTENT_SIZE_INDEX){
				summary = summary.substring(0, CommentUtils.RESOURCE_CONTENT_SIZE_INDEX);
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
			summary = "";
		}
		this.summary = summary;
	}
	/*public void setReadingCount(int readingCount) {
		this.readingCount = readingCount;
	}*/
	public long getLinkId() {
		return linkId;
	}
	public void setLinkId(long linkId) {
		this.linkId = linkId;
		this.linkIdStr = linkId+"";
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public int getIsPraise() {
		return isPraise;
	}
	public void setIsPraise(int isPraise) {
		this.isPraise = isPraise;
	}
	public int getlNum() {
		return lNum;
	}
	public void setlNum(int lNum) {
		this.lNum = lNum;
	}
	public int getMoney() {
		return money;
	}
	public void setMoney(int money) {
		this.money = money;
	}
	public void setRid(long rid) {
		this.rid = rid;
		this.ridStr = rid+"";
	}
	public int getIsDel() {
		return isDel;
	}
	public void setIsDel(int isDel) {
		this.isDel = isDel;
	}
	public String getContUrl() {
		return contUrl;
	}
	public void setContUrl(String contUrl) {
		this.contUrl = contUrl;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public long getBookListId() {
		return bookListId;
	}
	public long getBkCommentId() {
		return bkCommentId;
	}
	public void setBkCommentId(long bkCommentId) {
		this.bkCommentId = bkCommentId;
		this.bkCommentIdStr = bkCommentId+"";
	}
	public long getMvCommentId() {
		return mvCommentId;
	}
	public void setMvCommentId(long mvCommentId) {
		this.mvCommentId = mvCommentId;
		this.mvCommentIdStr = mvCommentId + "";
	}
	public int getfNum() {
		return fNum;
	}
	public void setfNum(int fNum) {
		this.fNum = fNum;
	}
	public int getIsCollect() {
		return isCollect;
	}
	public int getIsLow() {
		return isLow;
	}
	public void setIsLow(int isLow) {
		this.isLow = isLow;
	}
	public void setIsCollect(int isCollect) {
		this.isCollect = isCollect;
	}
	public long getMovieListId() {
		return movieListId;
	}
	public void setMovieListId(long movieListId) {
		this.movieListId = movieListId;
		this.movieListIdStr = movieListId+"";
	}
	public void setBookListId(long bookListId) {
		this.bookListId = bookListId;
		this.bookListIdStr = bookListId + "";
	}
	public String getScore() {
		return score;
	}
	public void setScore(String score) {
		this.score = CheckParams.getScore(score);
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getIsRead() {
		return isRead;
	}
	public void setIsRead(int isRead) {
		this.isRead = isRead;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getAtype() {
		return atype;
	}
	public void setAtype(int atype) {
		this.atype = atype;
	}
	public List getContList() {
		return contList;
	}
	public void setContList(List contList) {
		this.contList = contList;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getBtime() {
		return btime;
	}
	public UserEntity getUserEntity() {
		return userEntity;
	}
	public void setUserEntity(UserEntity userEntity) {
		this.userEntity = userEntity;
	}
	public void setBtime(long btime) {
		if(sf == null){
			sf = new SimpleDateFormat(DATEFORMAT);
		}
		Date date = new Date(btime);
		this.btime = sf.format(date);
	}
	
	public int getrNum() {
		return rNum;
	}
	public SerializeInfo getSerializeInfo() {
		return serializeInfo;
	}
	public void setSerializeInfo(SerializeInfo serializeInfo) {
		this.serializeInfo = serializeInfo;
	}
	public void setrNum(int rNum) {
		this.rNum = rNum;
	}
	public int getcNum() {
		return cNum;
	}
	public void setcNum(int cNum) {
		this.cNum = cNum;
	}
	public int getzNum() {
		return zNum;
	}
	public void setzNum(int zNum) {
		this.zNum = zNum;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	public BookInfo getBookInfo() {
		return bookInfo;
	}
	public void setBookInfo(BookInfo bookInfo) {
		this.bookInfo = bookInfo;
	}
	public MovieInfo getMovieInfo() {
		return movieInfo;
	}
	public void setMovieInfo(MovieInfo movieInfo) {
		this.movieInfo = movieInfo;
	}
	public ResourceInfo getResourceInfo() {
		return resourceInfo;
	}
	public void setResourceInfo(ResourceInfo resourceInfo) {
		this.resourceInfo = resourceInfo;
	}
	public String getFriendName() {
		return friendName;
	}
	public void setFriendName(String friendName) {
		this.friendName = friendName;
	}
	public String getPoint() {
		return point;
	}
	public void setPoint(String point) {
		this.point = point;
	}
	public String getVoteNum() {
		return voteNum;
	}
	public void setVoteNum(String voteNum) {
		this.voteNum = voteNum;
	}
	public long getStageid() {
		return stageid;
	}
	public void setStageid(long stageid) {
		this.stageid = stageid;
		this.stageid=0;
	}
	public List<MovieInfo> getMovieInfos() {
		return movieInfos;
	}
	public void setMovieInfos(List<MovieInfo> movieInfos) {
		this.movieInfos = movieInfos;
	}
	public List<BookInfo> getBookInfos() {
		return bookInfos;
	}
	public void setBookInfos(List<BookInfo> bookInfos) {
		this.bookInfos = bookInfos;
	}
	public static void main(String[] args) {
		long d = 1409122170850l;
		
		Date data = new Date(d);
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		sf.format(data);
		System.out.println(sf.format(data));
	}
	
	
	@Override
	public int compareTo(ResourceInfo o) {
		if(o.rid>=this.rid){
			return 1;
		}
		return -1;
	}
	
	
}
