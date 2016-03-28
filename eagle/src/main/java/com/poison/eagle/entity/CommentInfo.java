package com.poison.eagle.entity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.keel.common.lang.BaseDO;
import com.poison.ucenter.model.UserAllInfo;
/**
 * 
 * @author 温晓宁
 *
 */
public class CommentInfo extends BaseDO implements Comparable<CommentInfo>,Serializable{
	/**
	 * serialVersionUID = -4156779521871130713L;
	 */
	private static final long serialVersionUID = -4156779521871130713L;
	private static final String DATEFORMAT = "yyyy-MM-dd HH:mm:ss";
	private SimpleDateFormat sf;
	private long id;//主键id
	private String idStr;
	private long commentId;//当作为评论存在时，该字段为主键id，当作为回复时，该字段为回复评论的id
	private String commentIdStr;
	private String content;//评论或回复内容
	private int zNum;//赞的数量
	private int cNum;//评论的数量
	private int isPraise = 1;//0:赞过,1:没赞过
	private String btime;//创建时间
	private UserEntity userEntity;//评论人用户实体类
	private UserEntity toUserEntity;//被评论人实体类
	private List<Map> contList;//内容数组
	
	public CommentInfo() {
		super();
	}
	public CommentInfo(long id, String content, long btime,
			UserEntity userEntity) {
		super();
		if(sf == null){
			sf = new SimpleDateFormat(DATEFORMAT);
		}
		Date date = new Date(btime);
		this.btime = sf.format(date);
		this.id = id;
		this.idStr = id+"";
		this.content = content;
		this.userEntity = userEntity;
	}

	
	public List<Map> getContList() {
		return contList;
	}
	public void setContList(List<Map> contList) {
		this.contList = contList;
	}
	public String getIdStr() {
		return idStr;
	}
	public void setIdStr(String idStr) {
		this.idStr = id+"";
	}
	public String getCommentIdStr() {
		return commentIdStr;
	}
	public void setCommentIdStr(String commentIdStr) {
		this.commentIdStr = commentId+"";
	}
	public long getId() {
		return id;
	}
	public int getcNum() {
		return cNum;
	}
	public void setcNum(int cNum) {
		this.cNum = cNum;
	}
	public int getIsPraise() {
		return isPraise;
	}
	public void setIsPraise(int isPraise) {
		this.isPraise = isPraise;
	}
	public int getzNum() {
		return zNum;
	}
	public long getCommentId() {
		return commentId;
	}
	public void setCommentId(long commentId) {
		this.commentId = commentId;
	}
	public void setzNum(int zNum) {
		this.zNum = zNum;
	}
	public void setId(long id) {
		this.id = id;
	}
	public UserEntity getToUserEntity() {
		return toUserEntity;
	}
	public void setToUserEntity(UserEntity toUserEntity) {
		this.toUserEntity = toUserEntity;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
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
	public UserEntity getUserEntity() {
		return userEntity;
	}
	public void setUserEntity(UserEntity userEntity) {
		this.userEntity = userEntity;
	}
	public static void main(String[] args) {
		long d = 1406443693894l;
		
		Date data = new Date(d);
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		sf.format(data);
		if(sf == null){
			sf = new SimpleDateFormat(DATEFORMAT);
		}
		Date date = new Date(d);
		System.out.println(sf.format(data));
		
		System.out.println(UserAllInfo.class.getName());
	}
	
	
	@Override
	public int compareTo(CommentInfo o) {
		if(o.id>=this.id){
			return 1;
		}
		return -1;
	}
	
	
}
