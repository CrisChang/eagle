package com.poison.ucenter.model;

import com.keel.common.lang.BaseDO;

/**
 * Created with IntelliJ IDEA.
 * User: changjiang
 * Date: 16/1/26
 * Time: 15:44
 */
public class Author extends BaseDO{


    private static final long serialVersionUID = -4091049407588798579L;

    private long id;//主键id
    private String idStr;//主键id字符串
    private String mobilephone;//电话号码
    private String mail;//邮箱地址
    private String name;//笔名
    private String password;//密码
    private String passwordMd5;//密码
    private String faceUrl;//头像
    private String introduce;//说明
    private int isInit;//是否初始化
    private int state;//是否冻结
    private String contactWay1;//联系方式1
    private String contactWay2;//联系方式1
    private String contactWay3;//联系方式1
    private long createDate;//创建时间
    private long latestRevisionDate;//最后修改时间
    private int flag;//标识符

    public String getIdStr() {
        return idStr;
    }

    public void setIdStr(String idStr) {
        this.idStr = idStr;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getPasswordMd5() {
        return passwordMd5;
    }

    public void setPasswordMd5(String passwordMd5) {
        this.passwordMd5 = passwordMd5;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
        this.idStr = id+"";
    }

    public String getMobilephone() {
        return mobilephone;
    }

    public void setMobilephone(String mobilephone) {
        this.mobilephone = mobilephone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFaceUrl() {
        return faceUrl;
    }

    public void setFaceUrl(String faceUrl) {
        this.faceUrl = faceUrl;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public int getIsInit() {
        return isInit;
    }

    public void setIsInit(int isInit) {
        this.isInit = isInit;
    }

    public String getContactWay1() {
        return contactWay1;
    }

    public void setContactWay1(String contactWay1) {
        this.contactWay1 = contactWay1;
    }

    public String getContactWay2() {
        return contactWay2;
    }

    public void setContactWay2(String contactWay2) {
        this.contactWay2 = contactWay2;
    }

    public String getContactWay3() {
        return contactWay3;
    }

    public void setContactWay3(String contactWay3) {
        this.contactWay3 = contactWay3;
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
}
