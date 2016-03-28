package com.poison.eagle.entity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.keel.common.lang.BaseDO;

public class ChaperInfo extends BaseDO implements Comparable<ChaperInfo>,Serializable{
	/**
	 * serialVersionUID = -1537539474850214207L;
	 */
	private static final long serialVersionUID = -1537539474850214207L;
	private static final String DATEFORMAT = "yyyy-MM-dd HH:mm:ss";
	private SimpleDateFormat sf;
	private long id;
	private long parentId;
	private String title;
	private String content;//内容
	private String btime;//创建时间
	private String utime;//修改时间
	private long nextId;//下一张id
	private String nextTitle;//下一章标题
	private long previousId;//上一章id
	private String previousTitle;//上一章标题
	
	public ChaperInfo() {
		super();
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getParentId() {
		return parentId;
	}
	public void setParentId(long parentId) {
		this.parentId = parentId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
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
		Date udate = new Date(btime);
		this.utime = sf.format(udate);
	}
	public String getUtime() {
		return utime;
	}
	public void setUtime(long utime) {
		if(sf == null){
			sf = new SimpleDateFormat(DATEFORMAT);
		}
		Date udate = new Date(utime);
		this.utime = sf.format(udate);
	}
	public long getNextId() {
		return nextId;
	}
	public void setNextId(long nextId) {
		this.nextId = nextId;
	}
	public String getNextTitle() {
		return nextTitle;
	}
	public void setNextTitle(String nextTitle) {
		this.nextTitle = nextTitle;
	}
	public long getPreviousId() {
		return previousId;
	}
	public void setPreviousId(long previousId) {
		this.previousId = previousId;
	}
	public String getPreviousTitle() {
		return previousTitle;
	}
	public void setPreviousTitle(String previousTitle) {
		this.previousTitle = previousTitle;
	}
	@Override
	public int compareTo(ChaperInfo o) {
		if(o.id>=this.id){
			return 1;
		}
		return -1;
	}
	public static void main(String[] args) {
	}
	
}
