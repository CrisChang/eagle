package com.poison.act.dao;

import java.util.List;

import com.poison.act.model.ActAt;

public interface ActAtDAO {

	/**
	 * 
	 * <p>Title: insertintoActAt</p> 
	 * <p>Description: 插入at信息</p> 
	 * @author :weizhensong
	 * date 2014-7-27 上午12:20:19
	 * @return
	 */
	public int insertintoActAt(ActAt actAt);
	
	/**
	 * 
	 * <p>Title: findResAt</p> 
	 * <p>Description: 查找某个资源相关的at列表</p> 
	 * @author :weizhensong
	 * date 2014-7-27 上午12:29:00
	 * @param resourceid
	 * @return
	 */
	public List<ActAt> findResAt(long resourceid,Long id);
	/**
	 * 
	 * <p>Title: findResAt</p> 
	 * <p>Description: 查找某个人at别人的列表</p> 
	 * @author :weizhensong
	 * date 2014-7-27 上午12:29:00
	 * @param userid
	 * @return
	 */
	public List<ActAt> findUserAt(long userid,Long id);
	/**
	 * 
	 * <p>Title: findResAt</p> 
	 * <p>Description: 查找某个人被at的列表</p> 
	 * @author :weizhensong
	 * date 2014-7-27 上午12:29:00
	 * @param atUserid
	 * @return
	 */
	public List<ActAt> findAtUser(long atUserid,Long id);
	/**
	 * 
	 * <p>Title: findResAtCount</p> 
	 * <p>Description: 查询某个资源相关的at的总数</p> 
	 * @author :weizhensong
	 * date 2014-7-28 下午4:50:07
	 * @param resourceid
	 * @return
	 */
	public int findResAtCount(long resourceid);
	/**
	 * 
	 * <p>Title: findUserAtCount</p> 
	 * <p>Description: 查询某个人at别人的总数</p> 
	 * @author :weizhensong
	 * date 2014-7-28 下午4:50:07
	 * @param userid
	 * @return
	 */
	public int findUserAtCount(long userid);
	/**
	 * 
	 * <p>Title: findAtUserCount</p> 
	 * <p>Description: 查询某个人被at的总数</p> 
	 * @author :weizhensong
	 * date 2014-7-28 下午4:50:07
	 * @param atUserid
	 * @return
	 */
	public int findAtUserCount(long atUserid);
	
	/**
	 * 
	 * <p>Title: findAtById</p> 
	 * <p>Description: 查询at信息根据id</p> 
	 * @author :weizhensong
	 * date 2014-7-28 下午8:30:16
	 * @return
	 */
	public ActAt findAtById(long id);
	
	/**
	 * 
	 * <p>Title: deleteActAt</p> 
	 * <p>Description: 示例类</p> 
	 * @author :weizhensong
	 * date 2014-7-30 上午9:53:26
	 * @return
	 */
	public int deleteActAt(ActAt actAt);
}