package com.poison.resource.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.poison.eagle.utils.ResultUtils;
import com.poison.resource.dao.PostDAO;
import com.poison.resource.model.Diary;
import com.poison.resource.model.Post;

@SuppressWarnings("deprecation")
public class PostDAOImpl extends SqlMapClientDaoSupport implements PostDAO {

	private static final  Log LOG = LogFactory.getLog(PostDAOImpl.class);
	
	@Override
	public int addPost(Post post) {
		try {
			getSqlMapClientTemplate().insert("addPost", post);
			return ResultUtils.SUCCESS;
		} catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			return ResultUtils.INSERT_ERROR;
		}
	}

	@Override
	public int updateByIdPost(Post post) {
		try {
			getSqlMapClientTemplate().update("updateByIdPost", post);
			return ResultUtils.SUCCESS;
		} catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			return ResultUtils.UPDATE_ERROR;
		}
	}

	@Override
	public int deleteByIdPost(long id) {
		try {
			getSqlMapClientTemplate().update("deleteByIdPost", id);
			return ResultUtils.SUCCESS;
		} catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			return ResultUtils.DELETE_ERROR;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Post> queryUidPost(Post post) {
		List<Post> list = new ArrayList<Post>();
		try {
			list = getSqlMapClientTemplate().queryForList("queryByUidPost",
					post);
			if (null == list) {
				list = new ArrayList<Post>();
				post.setFlag(ResultUtils.SUCCESS);
				list.add(post);
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			list = new ArrayList<Post>();
			post.setFlag(ResultUtils.QUERY_ERROR);
			list.add(post);
			return list;
		}
		return list;
	}

	@Override
	public int updateByIdContent(Post post) {
		try {
			getSqlMapClientTemplate().update("updateByIdContent", post);
			return ResultUtils.SUCCESS;
		} catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			return ResultUtils.UPDATE_ERROR;
		}
	}

	@Override
	public Post queryByIdName(Post post) {
		try {
			post = (Post) getSqlMapClientTemplate().queryForObject(
					"queryByIdName", post);
			if (null == post) {
				post = new Post();
				post.setFlag(ResultUtils.DATAISNULL);
				return post;
			}
			post.setFlag(ResultUtils.SUCCESS);
		} catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			post = new Post();
			post.setFlag(ResultUtils.QUERY_ERROR);
			return post;
		}
		return post;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Post> queryByTypePost(String type,Long id) {
		List<Post> list = new ArrayList<Post>();
		Map<String, Object> map = new HashMap<String, Object>();
		Post post = new Post();
		try {
			map.put("type", type);
			map.put("id", id);
			list = getSqlMapClientTemplate().queryForList("queryByTypePost",
					map);
			if (null == list) {
				list = new ArrayList<Post>();
				post.setFlag(ResultUtils.SUCCESS);
				list.add(post);
			}
		} catch (Exception e) {
			list = new ArrayList<Post>();
			post.setFlag(ResultUtils.QUERY_ERROR);
			list.add(post);
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Post> queryByTypeUid(String type, long uid) {
		Map<Object,Object> map=new HashMap<Object, Object>();
		List<Post> list=new ArrayList<Post>();
		map.put("type", type);
		map.put("uid", uid);
		try {
			list=getSqlMapClientTemplate().queryForList("queryByTypeUid",map);
			if(null==list){
				list=new ArrayList<Post>();
				Post post=new Post();
				post.setFlag(ResultUtils.SUCCESS);
				list.add(post);
			}
		} catch (Exception e) {
			list=new ArrayList<Post>();
			Post post=new Post();
			post.setFlag(ResultUtils.QUERY_ERROR);
			list.add(post);
			return list;
		}
		return list;
	}

	/**
	 * 根据ID查询帖子信息
	 */
	@Override
	public List<Post> findPostListById(long id) {
		List<Post> postList = new ArrayList<Post>();
		Post post = new Post();
		try{
			if(id==0){
				postList = getSqlMapClientTemplate().queryForList("findPostListByDate");
			}else{
				postList = getSqlMapClientTemplate().queryForList("findPostListById",id);
			}
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			post.setFlag(ResultUtils.QUERY_ERROR);
			postList.add(post);
		}
		return postList;
	}

	/**
	 * 根据ID查询帖子信息
	 */
	@Override
	public List<Post> findPostListByUsersId(List<Long> userIdList, Long resId,String type) {
		List<Post> postList = new ArrayList<Post>();
		//List<Long> resIdList = new ArrayList<Long>();
		//resIdList.add(userId);
		Post post = new Post();
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("usersIdList", userIdList);
		map.put("resId", resId);
		map.put("type", type);
		try {
			if (null == userIdList || userIdList.size() == 0) {
				return postList;
			}
			postList = getSqlMapClientTemplate().queryForList("findPostListByUsersId", map);
		} catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			postList = new ArrayList<Post>();
			post.setFlag(ResultUtils.ERROR);
			postList.add(post);
		}
		return postList;
	}

	/**
	 * 更新阅读量
	 */
	@Override
	public int updatePostReadingCount(long id) {
		int flag = ResultUtils.ERROR;
		try{
			getSqlMapClientTemplate().update("updatePostReadingCount",id);
			flag = ResultUtils.SUCCESS;
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
		}
		return flag;
	}

	/**
	 * 根据uid查询帖子的总数
	 */
	@Override
	public Map<String, Object> findPostCount(long userId) {
		Map<String, Object> map = new HashMap<String, Object>();
		int count = 0;
		int flag = ResultUtils.ERROR;
		try{
			count = (Integer) getSqlMapClientTemplate().queryForObject("findPostCount",userId);
			flag = ResultUtils.SUCCESS;
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			count = 0;
			flag = ResultUtils.ERROR;
		}
		map.put("flag", flag);
		map.put("count", count);
		return map;
	}

	/**
	 * 根据用户id查询长文章详情
	 */
	@Override
	public List<Post> findPostListByUserId(Long userId, Long resId, String type) {
		List<Post> postList = new ArrayList<Post>();
		//List<Long> resIdList = new ArrayList<Long>();
		//resIdList.add(userId);
		Post post = new Post();
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("userId", userId);
		map.put("resId", resId);
		map.put("type", type);
		try {
			postList = getSqlMapClientTemplate().queryForList("findPostListByUserId", map);
			if(null==postList){
				postList = new ArrayList<Post>();
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			postList = new ArrayList<Post>();
		}
		return postList;
	}

}
