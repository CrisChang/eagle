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
/**
 * 
 * @author 温晓宁
 *
 */
public class TagInfo extends BaseDO implements Comparable<TagInfo>,Serializable{
	/**
	 * serialVersionUID = 6947075815507733475L;
	 */
	private static final long serialVersionUID = 6947075815507733475L;
	public static String ID = "id";
	public static String TAG = "tag";
	private static final String DATEFORMAT = "yyyy-MM-dd HH:mm:ss";
	private SimpleDateFormat sf;
	
	
	private String tagGroup="";
	private List<Map<String, String>> tags = new ArrayList<Map<String, String>>();
	private String type;


	public String getTagGroup() {
		return tagGroup;
	}


	public void setTagGroup(String tagGroup) {
		this.tagGroup = tagGroup;
	}



	public List<Map<String, String>> getTags() {
		return tags;
	}


	public void setTags(List<Map<String, String>> tags) {
		this.tags = tags;
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
	public int compareTo(TagInfo o) {
		if(o.tagGroup.getBytes().length>=this.tagGroup.getBytes().length){
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
