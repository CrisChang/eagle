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
 * 
 * @author 温晓宁
 *
 */
public class TipEntity extends BaseDO implements Comparable<TipEntity>,Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6826462147582800102L;
	private static final String DATEFORMAT = "yyyy-MM-dd HH:mm:ss";
	private SimpleDateFormat sf;
	private long id;
	private String ridStr;
	private String amount;//金额
	private String createDate;//时间
	private String content;//留言
	private int count;//打赏次数
	private UserEntity userEntity;//关联用户
	private ResourceInfo resourceInfo;//关联资源

	
	
	public String getRidStr() {
		return ridStr;
	}
	public void setRidStr(String ridStr) {
		this.ridStr = ridStr;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		float m = (float)amount / 100;
		this.amount = m+"";
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(long createDate) {
		Date data = new Date(createDate);
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		this.createDate = sf.format(data);
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public UserEntity getUserEntity() {
		return userEntity;
	}



	public void setUserEntity(UserEntity userEntity) {
		this.userEntity = userEntity;
	}



	public ResourceInfo getResourceInfo() {
		return resourceInfo;
	}
	public void setResourceInfo(ResourceInfo resourceInfo) {
		this.resourceInfo = resourceInfo;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
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
	public int compareTo(TipEntity o) {
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
