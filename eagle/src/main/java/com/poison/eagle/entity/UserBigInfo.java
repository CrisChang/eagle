package com.poison.eagle.entity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.keel.common.lang.BaseDO;
import com.poison.resource.model.MvComment;
import com.poison.ucenter.model.UserAllInfo;

public class UserBigInfo extends BaseDO implements Comparable<UserBigInfo>,Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -652205137919105053L;
	private static final String DATEFORMAT = "yyyy-MM-dd HH:mm:ss";
	private SimpleDateFormat sf;
	private long id;
	private int big;
	private int totalBig;
	private int nextBig;
	private int level;
	private String pk;
	public UserBigInfo() {
		super();
	}
	

	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}


	public int getBig() {
		return big;
	}


	public String getPk() {
		return pk;
	}


	public void setPk(String pk) {
		this.pk = pk;
	}


	public void setBig(int big) {
		this.big = big;
	}


	public int getTotalBig() {
		return totalBig;
	}


	public void setTotalBig(int totalBig) {
		this.totalBig = totalBig;
	}


	public int getNextBig() {
		return nextBig;
	}


	public void setNextBig(int nextBig) {
		this.nextBig = nextBig;
	}


	public int getLevel() {
		return level;
	}


	public void setLevel(int level) {
		this.level = level;
	}


	@Override
	public int compareTo(UserBigInfo o) {
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
        	UserBigInfo bean = (UserBigInfo)destination;
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
