package com.poison.ucenter.model;

import com.keel.common.lang.BaseDO;

public class Advert extends BaseDO{

	/**
	 * Advert序列号
	 */
	private static final long serialVersionUID = 9032857901311987296L;
	private long id;			//主键id
	private String image;		//图片地址
	private String url;	//链接地址
	private int isdel;			//删除标志
	private long creattime;		//创建时间
	private long updatetime;	//最后修改时间
	
	private int show;			//是否展示广告
	private int flag;			//判断标示
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getIsdel() {
		return isdel;
	}
	public void setIsdel(int isdel) {
		this.isdel = isdel;
	}
	public long getCreattime() {
		return creattime;
	}
	public void setCreattime(long creattime) {
		this.creattime = creattime;
	}
	public long getUpdatetime() {
		return updatetime;
	}
	public void setUpdatetime(long updatetime) {
		this.updatetime = updatetime;
	}
	public int getShow() {
		return show;
	}
	public void setShow(int show) {
		this.show = show;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
}
