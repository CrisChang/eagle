package com.poison.eagle.entity;

import java.util.List;

import com.keel.common.lang.BaseDO;

/**
 * 返回给前端的角色信息
 * 
 * @author :zhangqi
 * @time:2015-6-10上午10:23:53
 * @return
 */
public class ActorInfo extends BaseDO {

	private static final long serialVersionUID = 1L;

	private long id; // id
	private String pic;// 角色封面
	private String name;// 演员真名
	private String actorName;// 角色名称
	private String actorType; // 角色类别 1：导演 2：编剧 3：主演

	private String sex;
	private String constellation;
	private String birthday;
	private String birthplace;
	private String career;
	private String foreignName;
	private String aboutLarge;
	private List<String> actorStills;

	public ActorInfo() {
		super();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getActorType() {
		return actorType;
	}

	public void setActorType(String actorType) {
		this.actorType = actorType;
	}

	public String getActorName() {
		return actorName;
	}

	public void setActorName(String actorName) {
		this.actorName = actorName;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getConstellation() {
		return constellation;
	}

	public void setConstellation(String constellation) {
		this.constellation = constellation;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getBirthplace() {
		return birthplace;
	}

	public void setBirthplace(String birthplace) {
		this.birthplace = birthplace;
	}

	public String getCareer() {
		return career;
	}

	public void setCareer(String career) {
		this.career = career;
	}

	public String getAboutLarge() {
		return aboutLarge;
	}

	public void setAboutLarge(String aboutLarge) {
		this.aboutLarge = aboutLarge;
	}

	public String getForeignName() {
		return foreignName;
	}

	public void setForeignName(String foreignName) {
		this.foreignName = foreignName;
	}

	public List<String> getActorStills() {
		return actorStills;
	}

	public void setActorStills(List<String> actorStills) {
		this.actorStills = actorStills;
	}

}
