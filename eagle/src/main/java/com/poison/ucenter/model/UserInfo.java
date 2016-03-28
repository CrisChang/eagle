package com.poison.ucenter.model;

import com.keel.common.lang.BaseDO;
import com.poison.eagle.entity.ResourceInfo;

public class UserInfo  extends BaseDO implements Comparable<UserInfo>{
	/**
	 * UserInfo序列号
	 */
	private static final long serialVersionUID = -4661224663568481214L;
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
	private String pushToken;					//设备号
	private String affectiveStates;			//个人的感情状态
	private String residence;					//居住地
	private String profession;					//个人职业
	private long attentionDate;				//用户的关注时间
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

	public long getAttentionDate() {
		return attentionDate;
	}

	public void setAttentionDate(long attentionDate) {
		this.attentionDate = attentionDate;
	}

	public UserInfo(){
		
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
	public String getMobilePhone() {
		return mobilePhone;
	}
	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
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
	public long getLastestRevisionDate() {
		return lastestRevisionDate;
	}
	public void setLastestRevisionDate(long lastestRevisionDate) {
		this.lastestRevisionDate = lastestRevisionDate;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
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
	public long getLastestLoginDate() {
		return lastestLoginDate;
	}
	public void setLastestLoginDate(long lastestLoginDate) {
		this.lastestLoginDate = lastestLoginDate;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getPasswordMd5() {
		return passwordMd5;
	}

	public void setPasswordMd5(String passwordMd5) {
		this.passwordMd5 = passwordMd5;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}
	
	
	 public boolean equals(Object destination)
	    {
	        boolean retVal = false;
	        if(destination != null && destination.getClass().equals(this.getClass()))
	        {
	        	UserInfo bean = (UserInfo)destination;
	            if(bean.getUserId()==0 && this.getUserId() == 0)
	            {
	                retVal = true;
	            }
	            else
	            {
	                if(bean.getUserId() >0 && bean.getUserId() == this.getUserId())
	                {
	                    retVal = true;
	                }
	            }
	        }
	        return retVal;
	    }

	@Override
	public int compareTo(UserInfo o) {
		if(o.attentionDate>=this.attentionDate){
			return 1;
		}
		return -1;
	}
}
