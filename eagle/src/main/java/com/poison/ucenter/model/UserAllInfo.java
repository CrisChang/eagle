package com.poison.ucenter.model;

import com.keel.common.lang.BaseDO;

public class UserAllInfo extends BaseDO{


	/**
	 * UserAllInfo序列号
	 */
	private static final long serialVersionUID = 5986682302525786177L;
	private long userId;							//基本主键
	private String loginName;			    	//登录用户名
	private String password;				    //登录密码
	private String passwordMd5;			//登录密码二次加密
	private String mobilePhone;				//用户手机号
	private String name;							//用户昵称
	private String faceAddress;				//用户头像地址
	private long birthday;						//用户生日
	private String sex;							//用户性别
	private int level;								//用户等级
	private String twoDimensionCode;	//用户二维码
	private String ip;						        //创建Ip
	private long createDate;                  	//创建时间
	private long lastestLoginDate;			//最后一次登录时间
	private long lastestRevisionDate;     //最后一次修改时间
	private int flag;								//判断标示
	private String sign;			    				//用户个性签名
	private String interest;				    		//用户兴趣
	private String introduction;					//用户个人说明
	private String pushToken;
	private String affectiveStates;			//个人的感情状态
	private String residence;					//居住地
	private String profession;					//个人职业
	private String identification;				//用户个人认证
	private String state;							//个人状态码
	private String age;							//个人年龄、
	private String constellation;				//个人星座
	private String isOperation;				//是否运营


	public String getIsOperation() {
		return isOperation;
	}

	public void setIsOperation(String isOperation) {
		this.isOperation = isOperation;
	}

	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public String getConstellation() {
		return constellation;
	}
	public void setConstellation(String constellation) {
		this.constellation = constellation;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getIdentification() {
		return identification;
	}
	public void setIdentification(String identification) {
		this.identification = identification;
	}
	public String getAffectiveStates() {
		return affectiveStates;
	}
	public void setAffectiveStates(String affectiveStates) {
		this.affectiveStates = affectiveStates;
	}
	public String getResidence() {
		return residence;
	}
	public void setResidence(String residence) {
		this.residence = residence;
	}
	public String getProfession() {
		return profession;
	}
	public void setProfession(String profession) {
		this.profession = profession;
	}
	public String getTwoDimensionCode() {
		return twoDimensionCode;
	}
	public void setTwoDimensionCode(String twoDimensionCode) {
		this.twoDimensionCode = twoDimensionCode;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public String getPushToken() {
		return pushToken;
	}
	public void setPushToken(String pushToken) {
		this.pushToken = pushToken;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPasswordMd5() {
		return passwordMd5;
	}
	public void setPasswordMd5(String passwordMd5) {
		this.passwordMd5 = passwordMd5;
	}
	public String getMobilePhone() {
		return mobilePhone;
	}
	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getFaceAddress() {
		return faceAddress;
	}
	public void setFaceAddress(String faceAddress) {
		this.faceAddress = faceAddress;
	}
	public long getBirthday() {
		return birthday;
	}
	public void setBirthday(long birthday) {
		this.birthday = birthday;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public long getCreateDate() {
		return createDate;
	}
	public void setCreateDate(long createDate) {
		this.createDate = createDate;
	}
	public long getLastestLoginDate() {
		return lastestLoginDate;
	}
	public void setLastestLoginDate(long lastestLoginDate) {
		this.lastestLoginDate = lastestLoginDate;
	}
	public long getLastestRevisionDate() {
		return lastestRevisionDate;
	}
	public void setLastestRevisionDate(long lastestRevisionDate) {
		this.lastestRevisionDate = lastestRevisionDate;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getInterest() {
		return interest;
	}
	public void setInterest(String interest) {
		this.interest = interest;
	}
	public String getIntroduction() {
		return introduction;
	}
	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}
	
	
}
