package com.poison.resource.client;

import java.util.List;
import java.util.Map;

import com.poison.resource.model.Post;

/**
 * 
 * 类的作用:此类的作用是进一步对service层的方法细化
 * 作者:闫前刚
 * 创建时间:2014-8-1上午11:03:01
 * email :1486488968@qq.com
 * version: 1.0
 */
public interface PostFacade {
	/**
	 * 
	 * 方法的描述 :此方法的作用是新建帖子
	 * @param name 标题名
	 * @param content 内容
	 * @param uid 用户名
	 * @return
	 */
	public Post addPost(String type,String name,String content,long uid,String summary);
	/**
	 * 
	 * 方法的描述 :此方法的作用是修改帖子信息
	 * @param id 根据主键id
	 * @param name 标题名称
	 * @param content 内容
	 * @param uid 用户名
	 * @return
	 */
	public Post updateByIdPost(long id,String name,long uid,String content,String summary);
	/**
	 * 
	 * 方法的描述 :此方法的作用是删除帖子信息
	 * @param id 根据主键id
	 * @return
	 */
	public Post deleteByIdPost(long id);
	/**
	 * 
	 * 方法的描述 :此方法的作用是根据用户名查询当前的帖子
	 * @param uid
	 * @return
	 */
	public List<Post> queryUidPost(long uid);
	/**
	 * 
	 * 方法的描述 :此方法的作用是修改帖子内容
	 * @param uid 用户名
	 * @param content
	 * @return
	 */
	public Post updateByIdContent(long uid,String content);
	/**
	 * 
	 * 方法的描述 :此方法的作用是查询所需要的帖子信息
	 * @param id 主键id
	 * @return
	 */
	public Post queryByIdName(long id);
	/**
	 * 
	 * 方法的描述 :该方法的作用是根据类别查询
	 * @param type 类别
	 * @return
	 */
	public List<Post> queryByTypePost(String type,Long id);
	/**
	 * 
	 * 方法的描述 :此方法的作用查询当前用户类别
	 * @param type 类别
	 * @param uid 用户id
	 * @return
	 */
	public List<Post> queryByTypeUid(String type, long uid);
	
	/**
	 * 
	 * <p>Title: findPostListById</p> 
	 * <p>Description: 根据ID查询帖子详情</p> 
	 * @author :changjiang
	 * date 2014-8-2 上午2:28:50
	 * @param id
	 * @return
	 */
	public List<Post> findPostListById(long id);
	
	/**
	 * 
	 * <p>Title: findPostListByUsersId</p> 
	 * <p>Description: 根据ID查询帖子列表</p> 
	 * @author :changjiang
	 * date 2014-8-2 下午5:01:33
	 * @param userIdList
	 * @param resId
	 * @return
	 */
	public List<Post> findPostListByUsersId(List<Long> userIdList, Long resId,String type);
	
	/**
	 * 
	 * <p>Title: updatePostReadingCount</p> 
	 * <p>Description: 更新阅读量</p> 
	 * @author :changjiang
	 * date 2014-11-12 下午7:24:49
	 * @param id
	 * @return
	 */
	public int updatePostReadingCount(long id);
	
	/**
	 * 
	 * <p>Title: findPostCount</p> 
	 * <p>Description: 查询帖子总数</p> 
	 * @author :changjiang
	 * date 2014-12-9 下午8:28:54
	 * @param userId
	 * @return
	 */
	public Map<String, Object> findPostCount(long userId);
	
	/**
	 * 
	 * <p>Title: findPostListByUserId</p> 
	 * <p>Description: 根据用户id查询长文章详情</p> 
	 * @author :changjiang
	 * date 2015-6-5 下午4:12:03
	 * @param userId
	 * @param resId
	 * @param type
	 * @return
	 */
	public List<Post> findPostListByUserId(Long userId, Long resId, String type);
}
