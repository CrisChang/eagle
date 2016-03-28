package com.poison.resource.service.impl;

import java.util.List;
import java.util.Map;

import com.poison.resource.domain.repository.BkCommentDomainRepository;
import com.poison.resource.model.BkAvgMark;
import com.poison.resource.model.BkComment;
import com.poison.resource.service.BkCommentService;

public class BkCommentServiceImpl implements BkCommentService{

	private BkCommentDomainRepository bkCommentDomainRepository;
	
	public void setBkCommentDomainRepository(
			BkCommentDomainRepository bkCommentDomainRepository) {
		this.bkCommentDomainRepository = bkCommentDomainRepository;
	}

	/**
	 * 插入一条书评
	 */
	@Override
	public BkComment addOneBkComment(BkComment bkComment) {
		return bkCommentDomainRepository.addOneBkComment(bkComment);
	}

	/**
	 * 查询一本书的所有评论
	 */
	@Override
	public List<BkComment> findAllBkComment(int bookId, Long resId,String resType,String resourceType) {
		return bkCommentDomainRepository.findAllBkComment(bookId, resId,resType,resourceType);
	}
	/**
	 * 查询所有书的评论
	 */
	@Override
	public List<BkComment> findBkCommentListForOld(int bookId,Long resId,String resType,String resourceType){
		return bkCommentDomainRepository.findBkCommentListForOld(bookId, resId, resType, resourceType);
	}
	/**
	 *  根据id集合查询
	 */
	@Override
	public List<BkComment> findBkCommentListByIds(List<Long> ids){
		return bkCommentDomainRepository.findBkCommentListByIds(ids);
	}
	/**
	 * 查询所有评论
	 */
	@Override
	public List<BkComment> findAllComment(Long resId) {
		return bkCommentDomainRepository.findAllComment(resId);
	}

	/**
	 * 查询用户自己对一本书的所有评论
	 */
	@Override
	public List<BkComment> findMyBkCommentList(long userId, int bookId,
			Long resId,String resType) {
		return bkCommentDomainRepository.findMyBkCommentList(userId, bookId, resId,resType);
	}

	/**
	 * 查询一本书的评论总数
	 */
	@Override
	public int findCommentCount(int bookId,String resType) {
		return bkCommentDomainRepository.findCommentCount(bookId,resType);
	}
	
	/**
	 * 更新一条评论
	 */
	@Override
	public BkComment updateMyCommentByBook(BkComment bkComment) {
		return bkCommentDomainRepository.updateMyCommentByBook(bkComment);
	}

	/**
	 * 查询一条评论信息
	 */
	@Override
	public BkComment findCommentIsExistById(long id) {
		return bkCommentDomainRepository.findCommentIsExistById(id);
	}

	/**
	 * 根据用户ID查询该用户的评论列表
	 */
	@Override
	public List<BkComment> findCommentListByUserId(long userId, Long resId) {
		return bkCommentDomainRepository.findCommentListByUserId(userId, resId);
	}

	/**
	 * 增加一个用户对这本书的评分
	 */
	@Override
	public BkAvgMark addBkAvgMark(BkAvgMark bkAvgMark) {
		return bkCommentDomainRepository.addBkAvgMark(bkAvgMark);
	}

	/**
	 * 更新一个用户对这本书的评分
	 */
	@Override
	public BkAvgMark updateBkAvgMark(BkAvgMark bkAvgMark, float beforeScore) {
		return bkCommentDomainRepository.updateBkAvgMark(bkAvgMark, beforeScore);
	}

	/**
	 * 根据书的ID查询一本书的评分
	 */
	@Override
	public BkAvgMark findBkAvgMarkByBkId(int bkId) {
		return bkCommentDomainRepository.findBkAvgMarkByBkId(bkId);
	}
	/**
	 * 根据书的id集合查询和书的类型查询评分信息
	 */
	@Override
	public List<BkAvgMark> findBkAvgMarkByBkIds(List<Long> bkids,String type){
		return bkCommentDomainRepository.findBkAvgMarkByBkIds(bkids, type);
	}
	/**
	 * 删除一条评论
	 */
	@Override
	public BkComment deleteMyCommentById(long id) {
		return bkCommentDomainRepository.deleteMyCommentById(id);
	}

	/**
	 * 根据type查询书评列表
	 */
	@Override
	public List<BkComment> findAllBkCommentListByType(Integer userId, String type,
			Long resId) {
		return bkCommentDomainRepository.findAllBkCommentListByType(userId, type, resId);
	}

	/**
	 * 删除一本书的评分
	 */
	@Override
	public BkAvgMark deleteBkAvgMark(BkAvgMark bkAvgMark, float beforeScore) {
		return bkCommentDomainRepository.deleteBkAvgMark(bkAvgMark, beforeScore);
	}

	/**
	 * 更新书评的逼格值
	 */
	@Override
	public BkComment updateBkCommentBigValue(long id, float bigValue) {
		return bkCommentDomainRepository.updateBkCommentBigValue(id, bigValue);
	}

	/**
	 * 根据userid查询书评总数
	 */
	@Override
	public Map<String, Object> findBkCommentCount(long userId) {
		return bkCommentDomainRepository.findBkCommentCount(userId);
	}

	/**
	 * 增加神人评分
	 */
	@Override
	public BkAvgMark addExpertsBkAvgMark(BkAvgMark bkAvgMark) {
		return bkCommentDomainRepository.addExpertsBkAvgMark(bkAvgMark);
	}

	/**
	 * 更新神人的书评分
	 */
	@Override
	public BkAvgMark updateBkExpertsAvgMark(BkAvgMark bkAvgMark,
			float beforeScore) {
		return bkCommentDomainRepository.updateBkExpertsAvgMark(bkAvgMark, beforeScore);
	}

	/**
	 * 根据用户id查询长书评
	 */
	@Override
	public List<BkComment> findUserLongBkCommentListByUserId(long userId,
			Long resId) {
		return bkCommentDomainRepository.findUserLongBkCommentListByUserId(userId, resId);
	}
	/**
	 * 根据时间段查询用户的书评信息
	 */
	@Override
	public List<BkComment> findMyBkCommentListByTime(long userId,Long starttime,Long endtime){
		return bkCommentDomainRepository.findMyBkCommentListByTime(userId, starttime, endtime);
	}
	/**
	 * 查询是否存在某个用户的书评记录
	 */
	@Override
	public long findBkCommentRecord(long userid){
		return bkCommentDomainRepository.findBkCommentRecord(userid);
	}
}
