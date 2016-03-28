package com.poison.resource.model;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.keel.common.lang.BaseDO;
import com.poison.eagle.utils.CommentUtils;

public class Article extends BaseDO implements Comparable<Article>{
	/**
	 * Post序列号
	 */
	private static final long serialVersionUID = -6974471555561228063L;
	private static final Log LOG = LogFactory.getLog(Article.class);
	private long id;//主键id
	private long uid;//用户名
	private long isDel=0;//0表示被删除1表示没删除
	private long beginDate;//发帖日期
	private long endDate;//更新日期
	private String content;//内容
	private String name;//贴名称
	private String cover;//封面图片
	private String type;//选择类型
	private int readingCount;//阅读量
	private int falsereading;//虚假阅读量
	private String summary;	//摘要
	private int atype;//文章类型，0:原创,1:转载
	private int flag=0;//标识符
	private String tag;//标签

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getSummary() {
		if(summary == null){
			return "";
		}
		try {
			if(summary.length()>CommentUtils.RESOURCE_CONTENT_SIZE_INDEX){
				summary = summary.substring(0, CommentUtils.RESOURCE_CONTENT_SIZE_INDEX);
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
			summary = "";
		}
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public int getReadingCount() {
		return readingCount;
	}
	public void setReadingCount(int readingCount) {
		this.readingCount = readingCount;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getUid() {
		return uid;
	}
	public void setUid(long uid) {
		this.uid = uid;
	}
	public long getIsDel() {
		return isDel;
	}
	public void setIsDel(long isDel) {
		this.isDel = isDel;
	}
	public long getBeginDate() {
		return beginDate;
	}
	public void setBeginDate(long beginDate) {
		this.beginDate = beginDate;
	}
	public long getEndDate() {
		return endDate;
	}
	public void setEndDate(long endDate) {
		this.endDate = endDate;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCover() {
		return cover;
	}
	public void setCover(String cover) {
		this.cover = cover;
	}
	public int getAtype() {
		return atype;
	}
	public void setAtype(int atype) {
		this.atype = atype;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	public int getFalsereading() {
		return falsereading;
	}
	public void setFalsereading(int falsereading) {
		this.falsereading = falsereading;
	}
	@Override
	public int compareTo(Article o) {
		if(o.id==this.id){
			return 1;
		}else{
			return -1;
		}
		
	}
	
}
