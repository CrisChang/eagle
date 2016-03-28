package com.poison.resource.model;

import com.keel.common.lang.BaseDO;
import com.poison.eagle.entity.CategoryInfo;

public class TagCategory extends BaseDO implements Comparable<TagCategory> {

	/**
	 * TagCategory序列号
	 */
	private static final long serialVersionUID = -8722702016421063140L;
	private long id;					//主键
	private String tagCategory;	//类别
	private int tagLevel;				//分类顺序
	private String coverPage;		//分类封面
	private String type;				//类型
	private int flag;					//标识符
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getTagCategory() {
		return tagCategory;
	}
	public void setTagCategory(String tagCategory) {
		this.tagCategory = tagCategory;
	}
	public int getTagLevel() {
		return tagLevel;
	}
	public void setTagLevel(int tagLevel) {
		this.tagLevel = tagLevel;
	}
	public String getCoverPage() {
		return coverPage;
	}
	public void setCoverPage(String coverPage) {
		this.coverPage = coverPage;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	@Override
	public int compareTo(TagCategory o) {
		if(o.tagLevel>=this.tagLevel){
			return 1;
		}
		return -1;
	}
	
}
