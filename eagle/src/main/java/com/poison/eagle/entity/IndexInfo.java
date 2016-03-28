package com.poison.eagle.entity;

import java.io.Serializable;

import com.keel.common.lang.BaseDO;

public class IndexInfo extends BaseDO implements Comparable<IndexInfo>,Serializable {
	/**
	 * serialVersionUID = 2294449034192250543L;
	 */
	private static final long serialVersionUID = 2294449034192250543L;
	private int id;
	private String name;
	private String pagePic;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPagePic() {
		return pagePic;
	}

	public void setPagePic(String pagePic) {
		this.pagePic = pagePic;
	}

	@Override
	public int compareTo(IndexInfo o) {
		if(o.id>=this.id){
			return 1;
		}
		return -1;
	}
}
