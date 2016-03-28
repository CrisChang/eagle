package com.poison.resource.dao;

import java.util.List;
import java.util.Map;

import com.poison.resource.model.Post;

/**
 * 
 * 类的作用:此类的作用是作用与数据库进行持久化操作
 * 作者:闫前刚
 * 创建时间:2014-8-1上午10:35:38
 * email :1486488968@qq.com
 * version: 1.0
 */
public interface PostDAO  {
	/**
	 * 
	 * 方法的描述 :此方法的作用和是添加帖子
	 * @param psot
	 * @return
	 */
	public int addPost(Post psot);
	/**
	 * 
	 * 方法的描述 :此方法的作用是修改帖子
	 * @param post
	 * @return
	 */
	public int updateByIdPost(Post post);
	/**
	 * 
	 * 方法的描述 :此方法的作用是根据id删除帖子
	 * @param id
	 * @return
	 */
	public int deleteByIdPost(long id);
	/**
	 * 
	 * 方法的描述 :此方法的作用是查询帖子信息
	 * @param post
	 * @return
	 */
	public List<Post> queryUidPost(Post post);
	/**
	 * 
	 * 方法的描述 :此方法的作用是修改内容
	 * @param post
	 * @return
	 */
	public int updateByIdContent(Post post);
	/**
	 * 
	 * 方法的描述 :此方法的作用是查询所用的帖子
	 * @param post
	 * @return
	 */
	public Post queryByIdName(Post post);
	/**
	 * 
	 * 方法的描述 :此方法的作用是查询类别
	 * @param post
	 * @return
	 */
	public List<Post> queryByTypePost(String type,Long id);
	/**
	 * 
	 * 方法的描述 :此方法的作用是查询帖子目录
	 * @param type 类别
	 * @param uid 用户
	 * @return
	 */
	public List<Post> queryByTypeUid(String type,long uid);
	
	/**
	 * 
	 * <p>Title: findPostListById</p> 
	 * <p>Description: 根据ID查询帖子列表</p> 
	 * @author :changjiang
	 * date 2014-8-2 上午2:20:27
	 * @param id
	 * @return
	 */
	public List<Post> findPostListById(long id);
	
	/**
	 * 
	 * <p>Title: findPostListByUsersId</p> 
	 * <p>Description: 根据ID查询帖子信息</p> 
	 * @author :changjiang
	 * date 2014-8-2 下午4:27:05
	 * @param userIdList
	 * @param resId
	 * @return
	 */
	public List<Post> findPostListByUsersId(List<Long> userIdList,Long resId,String type);
	
	/**
	 * 
	 * <p>Title: findPostListByUserId</p> 
	 * <p>Description: 根据用户id查询帖子详情</p> 
	 * @author :changjiang
	 * date 2015-6-5 下午4:04:24
	 * @param userId
	 * @param resId
	 * @param type
	 * @return
	 */
	public List<Post> findPostListByUserId(Long userId,Long resId,String type);
	
	/**
	 * 
	 * <p>Title: updatePostReadingCount</p> 
	 * <p>Description: 更新阅读量</p> 
	 * @author :changjiang
	 * date 2014-11-12 下午7:18:12
	 * @return
	 */
	public int updatePostReadingCount(long id);
	
	/**
	 * 
	 * <p>Title: findPostCount</p> 
	 * <p>Description: 查询帖子总数</p> 
	 * @author :changjiang
	 * date 2014-12-9 下午7:35:46
	 * @param userId
	 * @return
	 */
	public Map<String, Object> findPostCount(long userId);
}
