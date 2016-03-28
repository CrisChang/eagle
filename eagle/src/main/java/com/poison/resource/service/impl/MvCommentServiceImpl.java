package com.poison.resource.service.impl;

import java.util.List;
import java.util.Map;

import com.poison.resource.domain.repository.MvCommentDomainrepository;
import com.poison.resource.model.MvAvgMark;
import com.poison.resource.model.MvComment;
import com.poison.resource.service.MvCommentService;

public class MvCommentServiceImpl implements MvCommentService {
	private MvCommentDomainrepository mvCommentDomainrepository;
	
	public void setMvCommentDomainrepository(
			MvCommentDomainrepository mvCommentDomainrepository) {
		this.mvCommentDomainrepository = mvCommentDomainrepository;
	}

	@Override
	public MvComment addMvComment(MvComment comment) {
		return mvCommentDomainrepository.addMvComment(comment);
	}

	@Override
	public List<MvComment> findAllComment(Long id) {
		return mvCommentDomainrepository.findAllComment(id);
	}

	@Override
	public List<MvComment> findAllMvComment(long movieId, Long id,String type,String resourceType) {
		return mvCommentDomainrepository.findAllMvComment(movieId, id,type,resourceType);
	}
	/**
	 * for旧版
	 * @param movieId
	 * @param id
	 * @param type
	 * @param resourceType
	 * @return
	 */
	@Override
	public List<MvComment> findAllMvCommentForOld(long movieId, Long id,String type,String resourceType,Integer pageIndex, Integer pageSize){
		return mvCommentDomainrepository.findAllMvCommentForOld(movieId, id, type, resourceType,pageIndex,pageSize);
	}

	/**
	 * 查询用户对一部电影的评论信息
	 */
	@Override
	public List<MvComment> findUserMvComment(MvComment mvComment) {
		return mvCommentDomainrepository.findUserMvComment(mvComment);
	}

	/**
	 * 更新评论信息
	 */
	@Override
	public MvComment updateMyMvComment(long id, String content, String score,String title,String cover) {
		return mvCommentDomainrepository.updateMyMvComment(id, content, score,title,cover);
	}

	/**
	 * 删除一条评论
	 */
	@Override
	public MvComment deleteMvComment(long id) {
		return mvCommentDomainrepository.deleteMvComment(id);
	}

	/**
	 * 查询一部电影评论总数
	 */
	@Override
	public int findMvCommentCount(long movieId) {
		return mvCommentDomainrepository.findMvCommentCount(movieId);
	}

	/**
	 * 查询影评
	 */
	@Override
	public MvComment findMvCommentIsExist(long id) {
		return mvCommentDomainrepository.findMvCommentIsExist(id);
	}

	/**
	 * 增加一个电影评分
	 */
	@Override
	public MvAvgMark addMvAvgMark(MvAvgMark mvAvgMark) {
		return mvCommentDomainrepository.addMvAvgMark(mvAvgMark);
	}

	/**
	 * 更改一个电影的评分
	 */
	@Override
	public MvAvgMark updateMvAvgMark(MvAvgMark mvAvgMark, float beforeScore) {
		return mvCommentDomainrepository.updateMvAvgMark(mvAvgMark, beforeScore);
	}

	/**
	 * 查询一个电影的评分
	 */
	@Override
	public MvAvgMark findMvAvgMarkByMvId(long mvId) {
		return mvCommentDomainrepository.findMvAvgMarkByMvId(mvId);
	}
	/**
	 * 根据电影的id集合查询评分信息
	 */
	@Override
	public List<MvAvgMark> findMvAvgMarkByMvIds(List<Long> mvids){
		return mvCommentDomainrepository.findMvAvgMarkByMvIds(mvids);
	}
	/**
	 * 删除电影评分
	 */
	@Override
	public MvAvgMark deleteMvAvgMark(MvAvgMark mvAvgMark, float beforeScore) {
		return mvCommentDomainrepository.deleteMvAvgMark(mvAvgMark, beforeScore);
	}

	/**
	 * 根据type查询电影评论
	 */
	@Override
	public List<MvComment> findAllMvCommentListByType(Long userId, String type,
			Long resId) {
		return mvCommentDomainrepository.findAllMvCommentListByType(userId, type, resId);
	}

	/**
	 * 更新影评的逼格值
	 */
	@Override
	public MvComment updateMvCommentBigValue(long id, float bigValue) {
		return mvCommentDomainrepository.updateMvCommentBigValue(id, bigValue);
	}

	/**
	 * 根据用户id查询电影评论
	 */
	@Override
	public List<MvComment> findAllMvCommentListByUsersId(List<Long> usersId,
			String type, Long resId) {
		return mvCommentDomainrepository.findAllMvCommentListByUsersId(usersId, type, resId);
	}

	/**
	 * 根据uid查询电影评论
	 */
	@Override
	public Map<String, Object> findMvCommentCountByUid(long userId) {
		return mvCommentDomainrepository.findMvCommentCountByUid(userId);
	}

	/**
	 * 添加专家评分
	 */
	@Override
	public MvAvgMark addExpertsAvgMark(MvAvgMark mvAvgMark) {
		return mvCommentDomainrepository.addExpertsAvgMark(mvAvgMark);
	}

	/**
	 * 修改专家评分
	 */
	@Override
	public MvAvgMark updateExpertsAvgMark(MvAvgMark mvAvgMark, float beforeScore) {
		return mvCommentDomainrepository.updateExpertsAvgMark(mvAvgMark, beforeScore);
	}

	/**
	 * 删除专家评分
	 */
	@Override
	public MvAvgMark deleteExpertsAvgMark(MvAvgMark mvAvgMark, float beforeScore) {
		return mvCommentDomainrepository.deleteExpertsAvgMark(mvAvgMark, beforeScore);
	}

	/**
	 * 根据用户id查询用户的长影评
	 */
	@Override
	public List<MvComment> findUserLongMvCommentListByUserId(long userId,
			Long resId) {
		return mvCommentDomainrepository.findUserLongMvCommentListByUserId(userId, resId);
	}
	/**
	 * 查询某个用户某个时间段的影评
	 */
	@Override
	public List<MvComment> findUserMvCommentsByTime(long userId,Long starttime,Long endtime){
		return mvCommentDomainrepository.findUserMvCommentsByTime(userId, starttime, endtime);
	}
	/**
	 * 根据标题和内容模糊查询，查询某个时间段的
	 */
	@Override
	public List<MvComment> searchMvCommentByLike(long userId,String keyword,Long starttime,Long endtime,long start,int pageSize){
		return mvCommentDomainrepository.searchMvCommentByLike(userId, keyword, starttime, endtime,start,pageSize);
	}
	/**
	 * 根据模糊查询条件查询帖子的数量
	 */
	@Override
	public Map<String, Object> findMvCommentCountByLike(long userId,String keyword,Long starttime,Long endtime){
		return mvCommentDomainrepository.findMvCommentCountByLike(userId, keyword, starttime, endtime);
	}
	
	/**
	 * 根据阶段id查询影评列表根据评委评分排序 
	 * @param stageid
	 * @param start
	 * @param pagesize
	 * @return
	 */
	@Override
	public List<MvComment> findMvCommentsByStageidOrderbyPoint(long stageid,long start,int pagesize){
		return mvCommentDomainrepository.findMvCommentsByStageidOrderbyPoint(stageid, start, pagesize);
	}
	
	/**
	 * 根据阶段id查询影评列表根据评委评分排序 
	 * @param stageid
	 * @param start
	 * @param pagesize
	 * @return
	 */
	@Override
	public List<MvComment> findMvCommentsByStageidOrderbyId(long stageid,long start,int pagesize){
		return mvCommentDomainrepository.findMvCommentsByStageidOrderbyId(stageid, start, pagesize);
	}
	/**
	 * 根据阶段id查询用户影评得分排行榜按评委评分排序 
	 * @param stageid
	 * @param start
	 * @param pagesize
	 * @return
	 */
	@Override
	public List<MvComment> findMvCommentUserRankByStageid(long stageid,long start,int pagesize){
		return mvCommentDomainrepository.findMvCommentUserRankByStageid(stageid, start, pagesize);
	}
	/**
	 * 根据阶段id查询某个人的影评列表根据评委评分排序
	 * @param stageid
	 * @param start
	 * @param pagesize
	 * @return
	 */
	@Override
	public List<MvComment> findMvCommentsByUseridAndStageid(long userId,long stageid,long start,int pagesize){
		return mvCommentDomainrepository.findMvCommentsByUseridAndStageid(userId, stageid, start, pagesize);
	}
	/**
	 * 根据阶段id查询某个电影的影评列表
	 * @param stageid
	 * @param start
	 * @param pagesize
	 * @return
	 */
	@Override
	public List<MvComment> findMvCommentsByMovieidAndStageid(long movieId,long stageid,Long resId,int pagesize){
		return mvCommentDomainrepository.findMvCommentsByMovieidAndStageid(movieId, stageid, resId, pagesize);
	}
	/**
	 * 根据影评id集合查询影评列表 
	 * @param stageid
	 * @return
	 */
	@Override
	public List<MvComment> findMvCommentsByIdsAndStageid(long stageid,List<Long> commentids){
		return mvCommentDomainrepository.findMvCommentsByIdsAndStageid(stageid, commentids);
	}
	/**
	 * 根据影评id集合查询影评列表 
	 * @param stageid
	 * @return
	 */
	@Override
	public List<MvComment> findMvCommentsByIds(List<Long> commentids){
		return mvCommentDomainrepository.findMvCommentsByIds(commentids);
	}
	/**
	 * 查询是否存在某个用户的影评记录
	 */
	@Override
	public long findMvCommentRecord(long userid){
		return mvCommentDomainrepository.findMvCommentRecord(userid);
	}

	/**
	 * 根据resType查询影评列表
	 * @param movieId
	 * @param resourceType
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	@Override
	public List<MvComment> findOneMvCommentListByResTypeAndPage(long movieId, String resourceType, int pageIndex, int pageSize) {
		return mvCommentDomainrepository.findOneMvCommentListByResTypeAndPage(movieId, resourceType, pageIndex, pageSize);
	}
}
