package com.poison.resource.service.impl;

import java.util.List;
import java.util.Map;

import com.poison.resource.domain.repository.PostDomainRepository;
import com.poison.resource.model.Post;
import com.poison.resource.service.PostService;

public class PostServiceImpl implements PostService {
	private PostDomainRepository postDomainRepository;

	public void setPostDomainRepository(
			PostDomainRepository postDomainRepository) {
		this.postDomainRepository = postDomainRepository;
	}

	@Override
	public Post addPost(Post post) {
		return postDomainRepository.addPost(post);
	}

	@Override
	public Post updateByIdPost(Post post) {
		return postDomainRepository.updateByIdPost(post);
	}

	@Override
	public Post deleteByIdPost(long id) {
		return postDomainRepository.deleteByIdPost(id);
	}

	@Override
	public List<Post> queryUidPost(Post post) {
		return postDomainRepository.queryUidPost(post);
	}

	@Override
	public Post updateByIdContent(Post post) {
		return postDomainRepository.updateByIdContent(post);
	}

	@Override
	public Post queryByIdName(Post post) {
		return postDomainRepository.queryByIdName(post);
	}

	@Override
	public List<Post> queryByTypePost(String type,Long id) {
		return postDomainRepository.queryByTypePost(type,id);
	}

	@Override
	public List<Post> queryByTypeUid(String type, long uid) {
		return postDomainRepository.queryByTypeUid(type, uid);
	}

	/**
	 * 根据ID查询帖子详情
	 */
	@Override
	public List<Post> findPostListById(long id) {
		return postDomainRepository.findPostListById(id);
	}

	/**
	 * 根据ID查询帖子详情
	 */
	@Override
	public List<Post> findPostListByUsersId(List<Long> userIdList, Long resId,String type) {
		return postDomainRepository.findPostListByUsersId(userIdList, resId,type);
	}

	/**
	 * 更新阅读量
	 */
	@Override
	public int updatePostReadingCount(long id) {
		return postDomainRepository.updatePostReadingCount(id);
	}

	/**
	 * 查询帖子总数
	 */
	@Override
	public Map<String, Object> findPostCount(long userId) {
		return postDomainRepository.findPostCount(userId);
	}

	/**
	 * 根据用户id查询长文章列表
	 */
	@Override
	public List<Post> findPostListByUserId(Long userId, Long resId, String type) {
		return postDomainRepository.findPostListByUserId(userId, resId, type);
	}

}
