package com.poison.ucenter.dao;

import java.util.List;

import com.keel.framework.runtime.ProductContext;
import com.poison.ucenter.model.UserAttention;

public interface UserAttentionDAO {

	/**
	 * 
	 * <p>Title: findUserAttention</p> 
	 * <p>Description: 查询用户关注人列表</p> 
	 * @author somebody
	 * date 2014-7-19 下午12:52:39
	 * @return
	 */
	public List<UserAttention> findUserAttention(ProductContext productContext,long userId,int count,int pageIndex,int pageSize);
	
	/**
	 * 
	 * <p>Title: findUserAttention</p> 
	 * <p>Description: 查询</p> 
	 * @author :changjiang
	 * date 2014-9-28 上午11:53:06
	 * @param userId
	 * @return
	 */
	public List<UserAttention> findUserAttentionTalentedPersons(long userId,String type);
	
	/**
	 * 
	 * <p>Title: findUserFens</p> 
	 * <p>Description: 我的粉丝</p> 
	 * @author changjiagn
	 * date 2014-7-19 下午3:26:37
	 * @param userAttentionId
	 * @return
	 */
	public List<UserAttention> findUserFens(ProductContext productContext,long userAttentionId,int count,int pageIndex,int pageSize);
	

	/**
	 * 
	 * <p>Title: findUserAttentionEachOther</p> 
	 * <p>Description: 查找相互关注的用户列表</p> 
	 * @author :changjiang
	 * date 2014-7-29 上午11:59:36
	 * @return
	 */
	public List<UserAttention> findUserAttentionEachOther(long userId);
	/**
	 * 
	 * <p>Title: doAttention</p> 
	 * <p>Description: 添加关注</p> 
	 * @author :changjiang
	 * date 2014-7-21 下午5:18:01
	 * @return
	 */
	public int insertAttention(ProductContext productContext,UserAttention userAttention);
	
	/**
	 * 
	 * <p>Title: cancelAttention</p> 
	 * <p>Description: 取消关注</p> 
	 * @author :changjiang
	 * date 2014-7-21 下午7:03:20
	 * @return
	 */
	public int updateAttention(ProductContext productContext,UserAttention userAttention);
	
	/**
	 * 
	 * <p>Title: findUserAttentionIsExist</p> 
	 * <p>Description: 用户关注是否存在</p> 
	 * @author :changjiang
	 * date 2014-7-22 下午7:54:53
	 * @param productContext
	 * @param userId	用户ID
	 * @param userAttentionId  用户关注人的ID
	 * @return
	 */
	public UserAttention findUserAttentionIsExist(ProductContext productContext, UserAttention userAttention);
	
	/**
	 * 
	 * <p>Title: findUserAttentionList</p> 
	 * <p>Description: 查询朋友关注人列表</p> 
	 * @author :changjiang
	 * date 2014-7-27 下午2:07:19
	 * @return
	 */
	public List<Long> findUserAttentionList(List<Long> userIdList);
	
	/**
	 * 
	 * <p>Title: findUserAttention</p> 
	 * <p>Description: 查询用户关注人的USERID</p> 
	 * @author :changjiang
	 * date 2014-7-31 下午10:22:27
	 * @param userId
	 * @return
	 */
	public List<Long> findUserAttention(long userId,int pageIndex,int pageSize);
	
	/**
	 * 
	 * <p>Title: findUserAttentionCount</p> 
	 * <p>Description: 查询用户关注的总人数</p> 
	 * @author :changjiang
	 * date 2014-7-28 下午3:18:22
	 * @return
	 */
	public int findUserAttentionCount(long userId);
	
	/**
	 * 
	 * <p>Title: findUserFensCount</p> 
	 * <p>Description: 查询用户的粉丝数</p> 
	 * @author :changjiang
	 * date 2014-7-28 下午3:34:42
	 * @return
	 */
	public int findUserFensCount(long userId);
}
