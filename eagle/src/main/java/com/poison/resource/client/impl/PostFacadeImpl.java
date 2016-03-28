package com.poison.resource.client.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import redis.clients.jedis.Jedis;

import com.keel.common.cache.redis.JedisSimpleClient;
import com.keel.common.cache.redis.JedisWorker;
import com.keel.utils.UKeyWorker;
import com.poison.eagle.manager.SensitiveManager;
import com.poison.eagle.utils.CommentUtils;
import com.poison.resource.client.PostFacade;
import com.poison.resource.ext.constant.ResStatisticConstant;
import com.poison.resource.ext.utils.ResRandomUtils;
import com.poison.resource.model.Post;
import com.poison.resource.model.ResStatistic;
import com.poison.resource.service.PostService;
import com.poison.resource.service.ResStatisticService;

public class PostFacadeImpl implements PostFacade {
	private UKeyWorker reskeyWork;
	private PostService postService;
	private JedisSimpleClient resourceVisitClient;
	private ResStatisticService resStatisticService;
	private SensitiveManager sensitiveManager;
	
	
	public void setSensitiveManager(SensitiveManager sensitiveManager) {
		this.sensitiveManager = sensitiveManager;
	}
	public void setResStatisticService(ResStatisticService resStatisticService) {
		this.resStatisticService = resStatisticService;
	}
	public void setResourceVisitClient(JedisSimpleClient resourceVisitClient) {
		this.resourceVisitClient = resourceVisitClient;
	}
	public void setReskeyWork(UKeyWorker reskeyWork) {
		this.reskeyWork = reskeyWork;
	}
	public void setPostService(PostService postService) {
		this.postService = postService;
	}
	/**
	 * 此方法的作用是新建帖子
	 */
	@Override
	public Post addPost(String type,String name, String content, long uid,String summary) {
		Post post=new Post();
		long postId = reskeyWork.getId();
		post.setId(postId);
		post.setBeginDate(new Date().getTime());
		post.setEndDate(new Date().getTime());
		//content = sensitiveManager.checkSensitive(content, uid, postId, CommentUtils.TYPE_POST);
		post.setContent(content);
		post.setName(name);
		post.setIsDel(0);
		post.setUid(uid);
		post.setType(type);
		post.setReadingCount(0);
		post.setSummary(summary);
		return postService.addPost(post);
	}
	/**
	 * 此方法的作用是修改标题
	 */
	@Override
	public Post updateByIdPost(long id, String name, long uid,String content,String summary) {
		Post post=new Post();
		post.setId(id);
		post.setName(name);
		post.setUid(uid);
		post.setContent(content);
		post.setEndDate(new Date().getTime());
		post.setSummary(summary);
		return postService.updateByIdPost(post);
	}
	/**
	 * 删除帖子操作
	 */
	@Override
	public Post deleteByIdPost(long id) {
		return postService.deleteByIdPost(id);
	}
	/**
	 * 根据用户id查询帖子
	 */
	@Override
	public List<Post> queryUidPost(long uid) {
		Post post=new Post();
		post.setUid(uid);
		return postService.queryUidPost(post);
	}
	/**
	 * 修改帖子内容
	 */
	@Override
	public Post  updateByIdContent(long uid, String content) {
		Post post=new Post();
		post.setEndDate(new Date().getTime());
		post.setUid(uid);
		post.setContent(content);
		return postService.updateByIdContent(post);
	}
	/**
	 * 查询帖子信息 返回实体
	 */
	@Override
	public Post queryByIdName(final long id) {
		/*resourceVisitClient.execute(new JedisWorker<Object>(){
			@Override
			public Object work(Jedis jedis) {
				String key = ResStatisticConstant.POST_STATISTIC_MARK+id+ResStatisticConstant.POST_STATISTIC_TYPE;
				String beforeDate = jedis.hget(key, ResStatisticConstant.RESOURCE_STATISTIC_DATE);
				//当没有数据时，置0
				if(null==beforeDate||"".equals(beforeDate)){
					beforeDate = "0";
				}
				long sysdate = System.currentTimeMillis();
				jedis.hset(key, ResStatisticConstant.RESOURCE_STATISTIC_DATE, sysdate+"");
				long falseVisit = jedis.hincrBy(key, ResStatisticConstant.STATISTIC_FALSE_VISIT, ResRandomUtils.RandomInt());
				long visitNumber = jedis.hincrBy(key, ResStatisticConstant.RESOURCE_STATISTIC_VISIT, 1);
				//当大于等于十分钟时，更新数据库
				if(sysdate-Long.valueOf(beforeDate)>=ResStatisticConstant.STATISTIC_TIME_INTERVALS){
					ResStatistic resStatistic = new ResStatistic();
					resStatistic.setResId(id);
					resStatistic.setType(CommentUtils.TYPE_POST);
					resStatistic.setFalseVisit(falseVisit);
					resStatistic.setVisitNumber(visitNumber);
					resStatistic.setLatestRevisionDate(sysdate);
					resStatisticService.insertResStatistic(resStatistic);
				}
				return jedis.hgetAll(key);
			}
		});*/
		Post post=new Post();
		post.setId(id);
		return postService.queryByIdName(post);
	}
	/**
	 * 此方法的作用是根据类别查询
	 */
	@Override
	public List<Post> queryByTypePost(String type,Long id) {
		return postService.queryByTypePost(type,id);
	}
	/**
	 * 此方法的作用是查询当前用户的类别
	 */
	@Override
	public List<Post> queryByTypeUid(String type, long uid) {
		Post post=new Post();
		post.setUid(uid);
		post.setType(type);
		return postService.queryByTypeUid(type, uid);
	}
	
	/**
	 * 根据ID查询帖子详情
	 */
	@Override
	public List<Post> findPostListById(long id) {
		return postService.findPostListById(id);
	}
	
	/**
	 * 根据ID查询帖子详情
	 */
	@Override
	public List<Post> findPostListByUsersId(List<Long> userIdList, Long resId,String type) {
		return postService.findPostListByUsersId(userIdList, resId,type);
	}
	
	/**
	 * 更新阅读量
	 */
	@Override
	public int updatePostReadingCount(long id) {
		return postService.updatePostReadingCount(id);
	}
	
	/**
	 * 查询帖子的总数
	 */
	@Override
	public Map<String, Object> findPostCount(long userId) {
		return postService.findPostCount(userId);
	}
	
	/**
	 * 根据用户id查询长文章详情
	 */
	@Override
	public List<Post> findPostListByUserId(Long userId, Long resId, String type) {
		return postService.findPostListByUserId(userId, resId, type);
	}

}
