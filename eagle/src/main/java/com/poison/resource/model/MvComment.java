package com.poison.resource.model;

import com.keel.common.lang.BaseDO;
import com.poison.eagle.utils.CheckParams;
import com.poison.ucenter.model.UserInfo;

import java.util.List;

public class MvComment extends BaseDO implements Comparable<MvComment>{
	/**
	 * MvComment序列号
	 */
	private static final long serialVersionUID = -3433874060102511326L;
	private long id;//主键id
	private long userId;//用户id
	private long movieId;//电影id
	private String content;//评论内容
	private int isDel;//0表示被删除1表示未删除
	private float bigValue;//逼格值
	private String type;//表示评论类型
	private int isDB;//是否存在库中
	private int isOpposition;//反对意见
	private String score;//评分
	private long createDate;//创建时间
	private  long latestRevisionDate;//更新日期
	private String description;//影评备注
	private String lon;//经度
	private String lat;//维度
	private String locationName;//地理位置描述
	private int flag;//标示
	private String locationCity;//地理位置城市
	private String locationArea;//地理位置地区
	private String title;//标题
	private String cover;//封面
	private String resourceType;//资源类型  长影评35、短影评7
	private long stageid; //影评大赛活动阶段id
	private float point;	//影评大赛影评得分
	private String tag;//标签

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}


	public String getResourceType() {
		return resourceType;
	}

	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}

	public String getLocationCity() {
		return locationCity;
	}

	public void setLocationCity(String locationCity) {
		this.locationCity = locationCity;
	}

	public String getLocationArea() {
		return locationArea;
	}

	public void setLocationArea(String locationArea) {
		this.locationArea = locationArea;
	}

	public String getLon() {
		return lon;
	}

	public void setLon(String lon) {
		this.lon = lon;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}
	

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		if(null==description){
			description = "";
		}
		this.description = description;
	}

	public float getBigValue() {
		return bigValue;
	}

	public void setBigValue(float bigValue) {
		this.bigValue = bigValue;
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

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getMovieId() {
		return movieId;
	}

	public void setMovieId(long movieId) {
		this.movieId = movieId;
	}

	public int getIsDel() {
		return isDel;
	}

	public void setIsDel(int isDel) {
		this.isDel = isDel;
	}

	public int getIsDB() {
		return isDB;
	}

	public void setIsDB(int isDB) {
		this.isDB = isDB;
	}

	public int getIsOpposition() {
		return isOpposition;
	}

	public void setIsOpposition(int isOpposition) {
		this.isOpposition = isOpposition;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public long getCreateDate() {
		return createDate;
	}

	public void setCreateDate(long createDate) {
		this.createDate = createDate;
	}

	public long getLatestRevisionDate() {
		return latestRevisionDate;
	}

	public void setLatestRevisionDate(long latestRevisionDate) {
		this.latestRevisionDate = latestRevisionDate;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		if(null==content){
			content = "";
		}
		this.content = content;
	}

	public long getStageid() {
		return stageid;
	}

	public void setStageid(long stageid) {
		this.stageid = stageid;
	}

	public float getPoint() {
		return point;
	}

	public void setPoint(float point) {
		this.point = point;
	}

	public MvComment() {
		super();
	}

	@Override
	public int compareTo(MvComment o) {
		if(o.id==this.id){
			return 1;
		}
		return -1;
	}
	public boolean equals(Object destination)
    {
        boolean retVal = false;
        if(destination != null && destination.getClass().equals(this.getClass()))
        {
        	MvComment bean = (MvComment)destination;
            if(bean.getUserId() == 0 && this.getUserId()==0)
            {
                retVal = true;
            }
            else
            {
                if(bean.getUserId()!=0 && bean.getUserId()==(this.getUserId()))
                {
                    retVal = true;
                }
            }
        }
        return retVal;
    }
}
