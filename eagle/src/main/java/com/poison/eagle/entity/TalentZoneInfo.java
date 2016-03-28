package com.poison.eagle.entity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.keel.common.lang.BaseDO;
import com.poison.resource.model.MvComment;
import com.poison.ucenter.model.UserAllInfo;

public class TalentZoneInfo extends BaseDO implements Comparable<TalentZoneInfo>,Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7170014831334350149L;
	private static final String DATEFORMAT = "yyyy-MM-dd HH:mm:ss";
	private SimpleDateFormat sf;
	private long id;
	private String name;
	private String description;
	private String logo;
	private String sign;
	private String type;
	private int size;
	private UserEntity leader;
	private List<UserEntity> userList;
	public TalentZoneInfo() {
		super();
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public UserEntity getLeader() {
		return leader;
	}
	public void setLeader(UserEntity leader) {
		this.leader = leader;
	}
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
	public String getSign() {
		return sign;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public List<UserEntity> getUserList() {
		return userList;
	}
	public void setUserList(List<UserEntity> userList) {
		this.userList = userList;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	@Override
	public int compareTo(TalentZoneInfo o) {
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
        	TalentZoneInfo bean = (TalentZoneInfo)destination;
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
