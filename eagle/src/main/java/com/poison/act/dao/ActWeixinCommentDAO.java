package com.poison.act.dao;

import java.util.List;

import com.poison.act.model.ActWeixinComment;

public interface ActWeixinCommentDAO {
	
	/**
	 * 
	 * <p>Title: insertComment</p> 
	 * <p>Description: 插入评论信息</p> 
	 * @author :weizhensong
	 * date 2014-7-27 上午12:20:19
	 * @return
	 */
	public int insertWeixinComment(ActWeixinComment actWeixinComment);
	
	/**
	 * 
	 * <p>Title: findComment</p> 
	 * <p>Description: 查找评论列表</p> 
	 * @author :weizhensong
	 * date 2014-7-27 上午12:29:00
	 * @param userId
	 * @return
	 */
	public List<ActWeixinComment> findWeixinComment(String sopendid);
	/**
	 * 
	 * @Title: existUserComment 
	 * @Description: 查询是否存在一个微信用户的某个评价信息
	 * @author weizhensong
	 * @date 2015-3-23 下午6:03:48
	 * @param @param openid
	 * @param @param comment
	 * @param @return
	 * @return int
	 * @throws
	 */
	public int existUserComment(String openid, String sopenid,String comment);
}
