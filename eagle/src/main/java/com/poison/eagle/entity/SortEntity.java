package com.poison.eagle.entity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.keel.common.lang.BaseDO;
import com.poison.resource.model.MvComment;
import com.poison.ucenter.model.UserAllInfo;
/**
 * 该类为排序比较类，在首页朋友圈显示时，通过该类进行转换比较
 * @author 温晓宁
 *
 */
public class SortEntity extends BaseDO implements Comparable<SortEntity>,Serializable{
	private static final long serialVersionUID = 6826462147582800102L;
	private long id;
	private Object object;

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Object getObject() {
		return object;
	}
	public void setObject(Object object) {
		this.object = object;
	}
	@Override
	public int compareTo(SortEntity o) {
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
//        	TipEntity bean = (TipEntity)destination;
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
