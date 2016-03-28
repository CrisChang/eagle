package com.poison.act.dao;

import java.util.List;
import java.util.Map;

import com.poison.act.model.ActPraise;
import com.poison.act.model.ActUseful;

public interface ActUsefulDAO {

	/**
	 * 
	 * <p>Title: insertUseful</p> 
	 * <p>Description: 插入是否有用的信息</p> 
	 * @author :changjiang
	 * date 2015-6-8 下午8:37:58
	 * @param actUseful
	 * @return
	 */
	public int insertUseful(ActUseful actUseful);
	
	/**
	 * 
	 * <p>Title: updateUseful</p> 
	 * <p>Description: 更新是否有用</p> 
	 * @author :changjiang
	 * date 2015-6-8 下午8:38:41
	 * @param actUseful
	 * @return
	 */
	public int updateUseful(long id,int isUseful,long latestRevisionDate);
	
	/**
	 * 
	 * <p>Title: findUsefulByResid</p> 
	 * <p>Description: 根据资源id查询是否有用</p> 
	 * @author :changjiang
	 * date 2015-6-9 下午5:09:29
	 * @param id
	 * @return
	 */
	public ActUseful findUsefulByResidAndUserid(long resId,long userId);
	
	/**
	 * 
	 * <p>Title: findUserfulById</p> 
	 * <p>Description: 根据id查询是否有用</p> 
	 * @author :changjiang
	 * date 2015-6-9 下午6:07:48
	 * @param id
	 * @return
	 */
	public ActUseful findUserfulById(long id);
	
	/**
	 * 
	 * <p>Title: findUsefulCount</p> 
	 * <p>Description: 查询有用的总数</p> 
	 * @author :changjiang
	 * date 2015-6-10 下午4:34:55
	 * @param id
	 * @return
	 */
	public Map<String, Object> findUsefulCount(long resId);
	
	/**
	 * 
	 * <p>Title: findUselessCount</p> 
	 * <p>Description: 查询没用的总数</p> 
	 * @author :changjiang
	 * date 2015-6-10 下午4:35:42
	 * @param id
	 * @return
	 */
	public Map<String, Object> findUselessCount(long resId);
	
	/**
	 * 
	 * <p>Title: findUsefulListByResUid</p> 
	 * <p>Description: 根据用户的id和最后resid查询用户有用列表</p> 
	 * @author :changjiang
	 * date 2015-6-15 下午5:42:56
	 * @param userid
	 * @param lastId
	 * @return
	 */
	public List<ActUseful> findUsefulListByResUid(long userid, Long lastId);
	
	/**
	 * 
	 * <p>Title: findUsefulListByResIdAndType</p> 
	 * <p>Description: 查找有用列表</p> 
	 * @author :changjiang
	 * date 2015-6-15 下午7:26:37
	 * @param resId
	 * @param id
	 * @return
	 */
	public List<ActUseful> findUsefulListByResIdAndType(long resId,
			 Long id);
}
