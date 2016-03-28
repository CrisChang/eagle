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

public class UserAlbumInfo extends BaseDO implements Comparable<UserAlbumInfo>,Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8716758126547820043L;

	private UserUtils userUtils = UserUtils.getInstance();
	
	private static final String DATEFORMAT = "yyyy-MM-dd HH:mm:ss";
	private SimpleDateFormat sf;
	private long id;
	private long uid;
	private String title;//相册名称
	private List<Map<String, Object>> urlList;//map中id为相片id，name：相片名称
	private String type;
	public UserAlbumInfo() {
		super();
	}
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}

	public long getUid() {
		return uid;
	}
	public void setUid(long uid) {
		this.uid = uid;
	}
	
	public List<Map<String, Object>> getUrlList() {
		return urlList;
	}
	public void setUrlList(String json) {
		urlList = userUtils.putUserAlbumJsonToList(json);
		this.urlList = urlList;
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
		System.out.println(sf.format(data));
		
		System.out.println(UserAllInfo.class.getName());
	}
	

	@Override
	public int compareTo(UserAlbumInfo o) {
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
        	UserAlbumInfo bean = (UserAlbumInfo)destination;
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
