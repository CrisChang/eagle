package com.poison.store.model;

import com.keel.common.lang.BaseDO;
import com.poison.resource.model.TagCategory;

public class PopCharts extends BaseDO implements Comparable<PopCharts> {

	/**
	 * PopCharts序列号
	 */
	private static final long serialVersionUID = 1473665427416881045L;
	private long id;					//主键
	private String chartName;		//排行榜名字
	private String coverPage;		//封面
	private int chartLevel;			//排行榜等级
	private String type;				//类型
	private int flag;					//标识符
	
	
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
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
	public String getCoverPage() {
		return coverPage;
	}
	public void setCoverPage(String coverPage) {
		this.coverPage = coverPage;
	}
	public int getChartLevel() {
		return chartLevel;
	}
	public void setChartLevel(int chartLevel) {
		this.chartLevel = chartLevel;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	@Override
	public int compareTo(PopCharts o) {
		if(o.chartLevel>=this.chartLevel){
			return 1;
		}
		return -1;
	}
	
}
