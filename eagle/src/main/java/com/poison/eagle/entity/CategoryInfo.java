package com.poison.eagle.entity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.keel.common.lang.BaseDO;
import com.poison.eagle.utils.UserUtils;
import com.poison.eagle.utils.WebUtils;
import com.poison.resource.model.MvComment;
import com.poison.ucenter.model.UserAllInfo;

public class CategoryInfo extends BaseDO implements Comparable<CategoryInfo>,Serializable{
	
	/**
	 * serialVersionUID = -5164394545747528531L;
	 */
	private static final long serialVersionUID = -5164394545747528531L;
	private long id;
	private String name;
	private String imageUrl;


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
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}


	@Override
	public int compareTo(CategoryInfo o) {
		if(o.id>=this.id){
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
