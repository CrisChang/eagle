package com.poison.resource.model;


import com.keel.common.lang.BaseDO;

/**
 * 
 * 类的作用:此类的作用是封装数据库字段
 * 作者:闫前刚
 * 创建时间:2014-7-27下午4:14:33
 * email :1486488968@qq.com
 * version: 1.0
 */
public class Serialize extends BaseDO implements Comparable<Serialize>{
	/**
	 * Serialize序列号
	 */
	private static final long serialVersionUID = -1427758475334057575L;
	private long id; //主键id
	private long beginDate;//创建时间
	private long endDate;//结束时间
	private String introduce;//图书核心内容介绍
	private String author;//作者简介
	private String name;//创建连载名称
	private String url;//图片上传路径
	private String type;//选择类型
	private String tags;//标签
	private long isDel=0;//标识符0表示删除1表示没有删除
	private long uid;//用户名
	private int flag=0;//设置返回标识符
	
	
	public long getUid() {
		return uid;
	}
	public void setUid(long uid) {
		this.uid = uid;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public long getIsDel() {
		return isDel;
	}
	public void setIsDel(long isDel) {
		this.isDel = isDel;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
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
	public String getIntroduce() {
		return introduce;
	}
	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	@Override
	public int compareTo(Serialize o) {
		if(o.id==this.id){
			return 1;
		}else{
			return 0;
		}
	}
	
}
