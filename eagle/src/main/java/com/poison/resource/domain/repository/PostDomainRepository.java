package com.poison.resource.domain.repository;

import java.util.List;
import java.util.Map;

import com.poison.eagle.utils.ResultUtils;
import com.poison.resource.dao.PostDAO;
import com.poison.resource.model.Post;

/**
 * 
 * 类的作用:此类的作用是封装dao层中的方法
 * 作者:闫前刚
 * 创建时间:2014-8-1上午10:51:25
 * email :1486488968@qq.com
 * version: 1.0
 */
public class PostDomainRepository {
	private PostDAO postDAO;
	
	public void setPostDAO(PostDAO postDAO) {
		this.postDAO = postDAO;
	}
	/**
	 * 
	 * 方法的描述 :此方法的作用是新建帖子
	 * @param post
	 * @return
	 */
	public Post addPost(Post post){
		Post pt = new Post();
		int flag = postDAO.addPost(post);
		pt.setFlag(flag);
		if(ResultUtils.SUCCESS==flag){
			pt = postDAO.queryByIdName(post);
		}
		return pt;
	}
	/**
	 * 
	 * 方法的描述 :此方法的作用是修改帖子
	 * @param post
	 * @return
	 */
	public Post updateByIdPost(Post post){
		Post pt = new Post();
		int flag = postDAO.updateByIdPost(post);
		pt.setFlag(flag);
		if(ResultUtils.SUCCESS==flag){
			pt = postDAO.queryByIdName(post);
		}
		return pt;
	}
	/**
	 * 
	 * 方法的描述 :此方法的作用是删除帖子
	 * @param id
	 * @return
	 */
	public Post deleteByIdPost(long id){
		Post post = new Post();
		post.setId(id);
		Post pt = postDAO.queryByIdName(post);
		int flag = pt.getFlag();
		if(ResultUtils.SUCCESS==flag||0 == flag){
			flag = postDAO.deleteByIdPost(id);
			pt.setFlag(flag);
		}
		return pt;
	}
	/**
	 * 
	 * 方法的描述 :此方法的作用是查询帖子相关信息
	 * @param post
	 * @return
	 */
	public List<Post> queryUidPost(Post post){
		return postDAO.queryUidPost(post);
	}
	/**
	 * 
	 * 方法的描述 :此方法的作用是修改帖子内容
	 * @param post
	 * @return
	 */
	public Post updateByIdContent(Post post){
		int flag = postDAO.updateByIdContent(post);
		Post pt = new Post();
		if(ResultUtils.SUCCESS==flag){
			pt = postDAO.queryByIdName(post);
			pt.setFlag(flag);
		}
		return pt;
	}
	/**
	 * 
	 * 方法的描述 :此方法的作用是查询所需要的帖子
	 * @param post
	 * @return
	 */
	public Post queryByIdName(Post post){
		return postDAO.queryByIdName(post);
	}
	/**
	 * 
	 * 方法的描述 :此方法的作用是根据类别查询
	 * @param post
	 * @return
	 */
	public List<Post> queryByTypePost(String type,Long id){
		return postDAO.queryByTypePost(type,id);
	}
	/**
	 * 
	 * 方法的描述 :此方法的作用是查询当前用户的类别列表
	 * @param type 类别
	 * @param uid  用户id
	 * @return
	 */
	public List<Post> queryByTypeUid(String type, long uid){
		return postDAO.queryByTypeUid(type, uid);
	}
	
	/**
	 * 
	 * <p>Title: findPostListById</p> 
	 * <p>Description: 根据ID查询</p> 
	 * @author :changjiang
	 * date 2014-8-2 上午2:24:37
	 * @param id
	 * @return
	 */
	public List<Post> findPostListById(long id){
		return postDAO.findPostListById(id);
	}
	
	/**
	 * 
	 * <p>Title: findPostListByUsersId</p> 
	 * <p>Description: 根据ID查询帖子列表</p> 
	 * @author :changjiang
	 * date 2014-8-2 下午4:58:56
	 * @param userIdList
	 * @param resId
	 * @return
	 */
	public List<Post> findPostListByUsersId(List<Long> userIdList,Long resId,String type){
		return postDAO.findPostListByUsersId(userIdList, resId,type);
	}
	
	/**
	 * 
	 * <p>Title: updatePostReadingCount</p> 
	 * <p>Description: 更新阅读量</p> 
	 * @author :changjiang
	 * date 2014-11-12 下午7:22:24
	 * @param id
	 * @return
	 */
	public int updatePostReadingCount(long id){
		return postDAO.updatePostReadingCount(id);
	}
	
	/**
	 * 
	 * <p>Title: findPostCount</p> 
	 * <p>Description: 查找帖子总数</p> 
	 * @author :changjiang
	 * date 2014-12-9 下午8:24:16
	 * @param userId
	 * @return
	 */
	public Map<String, Object> findPostCount(long userId){
		return postDAO.findPostCount(userId);
	}
	
	/**
	 * 
	 * <p>Title: findPostListByUserId</p> 
	 * <p>Description: 根据用户id查询长文章列表</p> 
	 * @author :changjiang
	 * date 2015-6-5 下午4:09:13
	 * @param userId
	 * @param resId
	 * @param type
	 * @return
	 */
	public List<Post> findPostListByUserId(Long userId, Long resId, String type){
		return postDAO.findPostListByUserId(userId, resId, type);
	}
}
