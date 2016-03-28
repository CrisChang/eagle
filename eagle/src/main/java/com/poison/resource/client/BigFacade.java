package com.poison.resource.client;

import java.util.List;

import com.poison.resource.model.Big;
import com.poison.resource.model.BigLevelValue;

public interface BigFacade {

	/**
	 * 
	 * <p>Title: findBig</p> 
	 * <p>Description: 查询big详情</p> 
	 * @author :changjiang
	 * date 2014-9-26 下午3:19:49
	 * @param attribute
	 * @param branch
	 * @param value
	 * @return
	 */
	//public Big findBig(String attribute, String branch, String value);
	
	/**
	 * 
	 * <p>Title: getPersonalBigValue</p> 
	 * <p>Description: 获取个人逼格值</p> 
	 * @author :changjiang
	 * date 2014-9-28 下午4:08:58
	 * @param constellation
	 * @param city
	 * @param interest
	 * @param nickname
	 * @return
	 */
	public float getPersonalBigValue(String constellation,String city,String interest,String nickname);
	
	/**
	 * 
	 * <p>Title: getMovieBigValue</p> 
	 * <p>Description: 获取电影逼格值</p> 
	 * @author :changjiang
	 * date 2014-9-28 下午4:09:49
	 * @param releaseddate
	 * @param place
	 * @param type
	 * @param director
	 * @return
	 */
	public float getMovieBigValue(String releaseddate,String place,String type,String director);
	
	/**
	 * 
	 * <p>Title: getBookBigValue</p> 
	 * <p>Description: 获取书籍的逼格值</p> 
	 * @author :changjiang
	 * date 2014-9-28 下午4:10:23
	 * @param author
	 * @param tags
	 * @return
	 */
	public float getBookBigValue(String author,String tags);
	
	/**
	 * 通过等级查询所需总分
	 * @param level
	 * @return
	 */
	public BigLevelValue getLevelValue(int level);
}
