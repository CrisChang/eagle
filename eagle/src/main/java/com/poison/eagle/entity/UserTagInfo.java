package com.poison.eagle.entity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.keel.common.lang.BaseDO;
import com.poison.eagle.utils.UserUtils;
import com.poison.eagle.utils.WebUtils;
import com.poison.resource.model.MvComment;
import com.poison.ucenter.model.UserAllInfo;

public class UserTagInfo extends BaseDO implements Comparable<UserTagInfo>,Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7006251981350985553L;
	public static String ID = "id";
	public static String TAG = "tag";
	private static final String DATEFORMAT = "yyyy-MM-dd HH:mm:ss";
	private SimpleDateFormat sf;
	
	private long id;
	private long uid;
	private long tagId;
	private String tagName;
	private int count;
	private String type;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getUid() {
		return uid;
	}
	public void setUid(long uid) {
		this.uid = uid;
	}
	public long getTagId() {
		return tagId;
	}
	public void setTagId(long tagId) {
		this.tagId = tagId;
	}
	public String getTagName() {
		return tagName;
	}
	public void setTagName(String tagName) {
		this.tagName = tagName;
	}


	public int getCount() {
		return count;
	}


	public void setCount(int count) {
		this.count = count;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
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
	}
	

	@Override
	public int compareTo(UserTagInfo o) {
		if(o.count>=this.count){
			return 1;
		}
		return -1;
	}
//	public boolean equals(Object destination)
//    {
//        boolean retVal = false;
//        if(destination != null && destination.getClass().equals(this.getClass()))
//        {
//        	TagInfo bean = (TagInfo)destination;
//            if(bean.getId() == 0 && this.getId()==0)
//            {
//                retVal = true;
//            }
//            else
//            {
//                if(bean.getId()!=0 && bean.getId()==(this.getId()))
//                {
//                    retVal = true;
//                }
//            }
//        }
//        return retVal;
//    }
	
	
}
