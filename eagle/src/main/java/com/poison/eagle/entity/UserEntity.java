package com.poison.eagle.entity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.keel.common.lang.BaseDO;
import com.poison.resource.model.MvComment;
import com.poison.ucenter.model.UserAllInfo;

public class UserEntity extends BaseDO implements Comparable<UserEntity>,Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8877173508245816708L;
	private static final String DATEFORMAT = "yyyy-MM-dd HH:mm:ss";
	private SimpleDateFormat sf;
	private long id;
	private String nickName;//昵称
	private String face_url;//头像地址
	private String sex;//性别
	private String sign;//个性签名
	private String interest;//兴趣（已废弃）
	private String introduction;//个人说明
	private String affective;//个人感情状态
	private String residence;//居住地
	private String profession;//职业
	private String pushToken;//手机设备号
	private String age;//个人年龄
	private String constellation;//个人星座
	private int fansNum;//粉丝数，已废弃
	private int plusNum;//关注数，已废弃
	private int big;//逼格值
	private int level;//等级
	private int nextBig;//距离下级需要多少big
	private int isInterest = 2;//1:已关注、2：未关注、：0相互关注
	private int type = 0;//0:正常用户、1：达人
	private Long sort;//排行字段
	private int isBinding;//是否绑定
	private String identification;//身份认证
	private String isOperation;//是否运营
	private String goldAmount="0";//金币数量,金币余额
	
	public UserEntity() {
		super();
	}
	
	public UserEntity(long id, String nickName, String face_url, String sex,
			String sign, String interest, String introduction,String identification) {
		super();
		this.id = id;
		this.nickName = nickName;
		this.face_url = face_url;
		this.sex = sex;
		this.sign = sign;
		this.interest = interest;
		this.introduction = introduction;
		this.identification = identification;
	}

	public static void main(String[] args) {
		long d = 1406443693894l;

		Date data = new Date(d);
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		sf.format(data);
		if(sf == null){
			sf = new SimpleDateFormat(DATEFORMAT);
		}
		Date date = new Date(d);
		System.out.println(sf.format(data));

		System.out.println(UserAllInfo.class.getName());
	}

	public int getIsBinding() {
		return isBinding;
	}

	public void setIsBinding(int isBinding) {
		this.isBinding = isBinding;
	}

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

	public String getIdentification() {
		return identification;
	}

	public void setIdentification(String identification) {
		this.identification = identification;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getAffective() {
		return affective;
	}

	public void setAffective(String affective) {
		this.affective = affective;
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

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public Long getSort() {
		return sort;
	}

	public void setSort(Long sort) {
		this.sort = sort;
	}

	public int getFansNum() {
		return fansNum;
	}

	public void setFansNum(int fansNum) {
		this.fansNum = fansNum;
	}

	public int getBig() {
		return big;
	}

	public void setBig(int big) {
		this.big = big;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getNextBig() {
		return nextBig;
	}

	public void setNextBig(int nextBig) {
		this.nextBig = nextBig;
	}

	public int getPlusNum() {
		return plusNum;
	}

	public void setPlusNum(int plusNum) {
		this.plusNum = plusNum;
	}

	public String getFace_url() {
		return face_url;
	}

	public void setFace_url(String face_url) {
		this.face_url = face_url;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
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
	
	public String getPushToken() {
		return pushToken;
	}

	public void setPushToken(String pushToken) {
		this.pushToken = pushToken;
	}

	public int getIsInterest() {
		return isInterest;
	}

	public void setIsInterest(int isInterest) {
		this.isInterest = isInterest;
	}
	
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
	public String getGoldAmount() {
		if(goldAmount == null){
			goldAmount = "0";
		}
		return goldAmount;
	}

	public void setGoldAmount(String goldAmount) {
		this.goldAmount = goldAmount;
	}

	@Override
	public int compareTo(UserEntity o) {
		if(o.id>=this.id){
			return 1;
		}
		return -1;
	}
	public boolean equals(Object destination)
    {
        boolean retVal = false;
        if(destination != null && destination.getClass().equals(this.getClass()))
        {
        	UserEntity bean = (UserEntity)destination;
            if(bean.getId() == 0 && this.getId()==0)
            {
                retVal = true;
            }
            else
            {
                if(bean.getId()!=0 && bean.getId()==(this.getId()))
                {
                    retVal = true;
                }
            }
        }
        return retVal;
    }
	
	
}
