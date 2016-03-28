package com.poison.resource.service;

import java.util.List;
import java.util.Map;

import com.poison.resource.model.Post;

/**
 * 
 * 类的作用:此类作用是处理业务相关的内容
 * 作者:闫前刚
 * 创建时间:2014-8-1上午10:57:00
 * email :1486488968@qq.com
 * version: 1.0
 */
public interface PostService {
	/**
	 * 
	 * 方法的描述 :此方法的作用是新建帖子
	 * @param post
	 * @return
	 */
	public Post addPost(Post post);
	/**
	 * 
	 * 方法的描述 :此方法的作用是修改帖子信息
	 * @param post
	 * @return
	 */
	public Post updateByIdPost(Post post);
	/**
	 * 
	 * 方法的描述 :删除帖子信息
	 * @param id
	 * @return
	 */
	public Post deleteByIdPost(long id);
	/**
	 * 
	 * 方法的描述 :此方法的作用是查询帖子相关的内容
	 * @param post
	 * @return
	 */
	public List<Post> queryUidPost(Post post);
	/**
	 * 
	 * 方法的描述 :此方法的作用是修改帖子内容
	 * @param post
	 * @return
	 */
	public Post updateByIdContent(Post post);
	/**
	 * 
	 * 方法的描述 :此方法的作用是查询所需帖子内容
	 * @param post
	 * @return
	 */
	public Post queryByIdName(Post post);
	/**
	 * 
	 * 方法的描述 :根据用户名查询
	 * @param post
	 * @return
	 */
	public List<Post> queryByTypePost(String type,Long id);
	/**
	 * 
	 * 方法的描述 :此方法的作用是查询当前用户类别
	 * @param type 类别
	 * @param uid 用户id
	 * @return
	 */
	public List<Post> queryByTypeUid(String type, long uid);
	
	/**
	 * 
	 * <p>Title: findDiaryListById</p> 
	 * <p>Description: 根据ID查询日记</p> 
	 * @author :changjiang
	 * date 2014-8-2 上午2:26:06
	 * @return
	 */
	public List<Post> findPostListById(long id);
	
	/**
	 * 
	 * <p>Title: findPostListByUsersId</p> 
	 * <p>Description: 根据ID查询帖子列表</p> 
	 * @author :changjiang
	 * date 2014-8-2 下午4:59:56
	 * @param userIdList
	 * @param resId
	 * @return
	 */
	public List<Post> findPostListByUsersId(List<Long> userIdList,Long resId,String type);
	
	/**
	 * 
	 * <p>Title: updatePostReadingCount</p> 
	 * <p>Description: 更新阅读量</p> 
	 * @author :changjiang
	 * date 2014-11-12 下午7:23:26
	 * @param id
	 * @return
	 */
	public int updatePostReadingCount(long id);
	
	/**
	 * 
	 * <p>Title: findPostCount</p> 
	 * <p>Description: 查询帖子总数</p> 
	 * @author :changjiang
	 * date 2014-12-9 下午8:26:32
	 * @param userId
	 * @return
	 */
	public Map<String, Object> findPostCount(long userId);
	
	/**
	 * 
	 * <p>Title: findPostListByUserId</p> 
	 * <p>Description: 根据用户id查询长文章详情</p> 
	 * @author :changjiang
	 * date 2015-6-5 下午4:09:54
	 * @param userId
	 * @param resId
	 * @param type
	 * @return
	 */
	public List<Post> findPostListByUserId(Long userId, Long resId, String type);
}
