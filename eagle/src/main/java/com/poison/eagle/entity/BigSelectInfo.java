package com.poison.eagle.entity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.keel.common.lang.BaseDO;

public class BigSelectInfo extends BaseDO implements Comparable<BigSelectInfo> ,Serializable{
	/**
	 * serialVersionUID = 502908854389529565L;
	 */
	private static final long serialVersionUID = 502908854389529565L;
	private static final String DATEFORMAT = "yyyy-MM-dd HH:mm:ss";
	private SimpleDateFormat sf;
	private long id;
	private String title;
	private String type;//0:单选、1:多选
	private String aItem;//a
	private String bItem;//b
	private String cItem;//c
	private String dItem;//d
	private String btime;//创建时间
	
	public BigSelectInfo() {
		super();
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getBtime() {
		return btime;
	}
	public void setBtime(long btime) {
		if(sf == null){
			sf = new SimpleDateFormat(DATEFORMAT);
		}
		Date udate = new Date(btime);
		this.btime = sf.format(udate);
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getaItem() {
		return aItem;
	}
	public void setaItem(String aItem) {
		this.aItem = aItem;
	}
	public String getbItem() {
		return bItem;
	}
	public void setbItem(String bItem) {
		this.bItem = bItem;
	}
	public String getcItem() {
		return cItem;
	}
	public void setcItem(String cItem) {
		this.cItem = cItem;
	}
	public String getdItem() {
		return dItem;
	}
	public void setdItem(String dItem) {
		this.dItem = dItem;
	}
	@Override
	public int compareTo(BigSelectInfo o) {
		if(o.id>=this.id){
			return 1;
		}
		return -1;
	}
	public static void main(String[] args) {
	}
	
}
