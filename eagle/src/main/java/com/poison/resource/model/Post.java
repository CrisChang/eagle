package com.poison.resource.model;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.keel.common.lang.BaseDO;
import com.poison.eagle.manager.FansListManager;
import com.poison.eagle.utils.CommentUtils;

/**
 * 
 * 类的作用:此方法的作用是封装数据库字段
 * 作者:闫前刚
 * 创建时间:2014-7-30下午5:31:48
 * email :1486488968@qq.com
 * version: 1.0
 */
public class Post extends BaseDO implements Comparable<Post>{
	/**
	 * Post序列号
	 */
	private static final long serialVersionUID = -6974471555541228063L;
	private static final Log LOG = LogFactory.getLog(Post.class);
	private long id;//主键id
	private long uid;//用户名
	private long isDel=0;//0表示被删除1表示没删除
	private long beginDate;//发帖日期
	private long endDate;//更新日期
	private String content;//内容
	private String name;//贴名称
	private String type;//选择类型
	private int readingCount;//阅读量
	private String summary;	//摘要
	private int flag=0;//标识符
	
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
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	@Override
	public int compareTo(Post o) {
		if(o.id==this.id){
			return 1;
		}else{
			return -1;
		}
		
	}
	
}
