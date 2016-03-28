package com.poison.store.model;

import com.keel.common.lang.BaseDO;

/**
 * 角色 DO
 * 
 * @author :zhangqi
 * @time:2015-4-28下午5:57:21
 * @return
 */
public class Actor extends BaseDO {

	private static final long serialVersionUID = 1L;

	private long id;
	private String actorUrl;
	private String pic;
	private String name;
	private int sex;
	private String constellation;
	private String birthday;
	private String birthplace;
	private String career;
	private String moreForeignName;
	private String moreChineseName;
	private String imdb;
	private String about;
	private String aboutLarge;
	private String familyMember;
	private String extendField1;
	private String extendField2;
	private String extendField3;
	private String foreignName;
	private String chineseName;

	private long createTime;
	private long updateTime;

	private ActorStills actorStills;

	private long flag;

	public Actor() {
		super();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getActorUrl() {
		return actorUrl;
	}

	public void setActorUrl(String actorUrl) {
		this.actorUrl = actorUrl;
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

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
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

	public String getMoreForeignName() {
		return moreForeignName;
	}

	public void setMoreForeignName(String moreForeignName) {
		this.moreForeignName = moreForeignName;
	}

	public String getMoreChineseName() {
		return moreChineseName;
	}

	public void setMoreChineseName(String moreChineseName) {
		this.moreChineseName = moreChineseName;
	}

	public String getImdb() {
		return imdb;
	}

	public void setImdb(String imdb) {
		this.imdb = imdb;
	}

	public String getAbout() {
		return about;
	}

	public void setAbout(String about) {
		this.about = about;
	}

	public String getAboutLarge() {
		return aboutLarge;
	}

	public void setAboutLarge(String aboutLarge) {
		this.aboutLarge = aboutLarge;
	}

	public String getFamilyMember() {
		return familyMember;
	}

	public void setFamilyMember(String familyMember) {
		this.familyMember = familyMember;
	}

	public String getExtendField1() {
		return extendField1;
	}

	public void setExtendField1(String extendField1) {
		this.extendField1 = extendField1;
	}

	public String getExtendField2() {
		return extendField2;
	}

	public void setExtendField2(String extendField2) {
		this.extendField2 = extendField2;
	}

	public String getExtendField3() {
		return extendField3;
	}

	public void setExtendField3(String extendField3) {
		this.extendField3 = extendField3;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}

	public long getFlag() {
		return flag;
	}

	public void setFlag(long flag) {
		this.flag = flag;
	}

	public String getForeignName() {
		return foreignName;
	}

	public void setForeignName(String foreignName) {
		this.foreignName = foreignName;
	}

	public String getChineseName() {
		return chineseName;
	}

	public void setChineseName(String chineseName) {
		this.chineseName = chineseName;
	}

	public ActorStills getActorStills() {
		return actorStills;
	}

	public void setActorStills(ActorStills actorStills) {
		this.actorStills = actorStills;
	}

}
