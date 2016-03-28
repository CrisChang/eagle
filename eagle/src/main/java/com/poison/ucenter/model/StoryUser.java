package com.poison.ucenter.model;

import com.keel.common.lang.BaseDO;

/**
 * Created with IntelliJ IDEA.
 * User: changjiang
 * Date: 16/2/25
 * Time: 14:37
 */
public class StoryUser extends BaseDO{

    private static final long serialVersionUID = -8264293853331844050L;

    private long userId;							//基本主键
    private String loginName;			    	//登录用户名
    private String password;				    //登录密码
    private String passwordMd5;			//登录密码二次加密
    private String mobilePhone;				//用户手机号
    private String name;							//用户昵称
    private String faceAddress;				//用户头像地址
    private long birthday;						//用户生日
    private String sex;							//用户性别
    private String ip;						        //创建Ip
    private long createDate;                  	//创建时间
    private long lastestLoginDate;			//最后一次登录时间
    private long lastestRevisionDate;     //最后一次修改时间
    private int flag;								//判断标示
    private String state;							//个人状态码
    private int isBinding;  //是否绑定

    public int getIsBinding() {
        return isBinding;
    }

    public void setIsBinding(int isBinding) {
        this.isBinding = isBinding;
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
