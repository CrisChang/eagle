package com.poison.store.model;

import com.keel.common.lang.BaseDO;

public class ChartsDb extends BaseDO{

	/**
	 * ChartsDb序列号
	 */
	private static final long serialVersionUID = -7906364320759249570L;
	
	private long id;					//主键
	private String chartName;		//排行榜名字
	private String chartGroup;	//排行榜分组
	private String type;				//类型
	private int flag;					//标识符
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getChartName() {
		return chartName;
	}
	public void setChartName(String chartName) {
		this.chartName = chartName;
	}
	public String getChartGroup() {
		return chartGroup;
	}
	public void setChartGroup(String chartGroup) {
		this.chartGroup = chartGroup;
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
	
	
}
