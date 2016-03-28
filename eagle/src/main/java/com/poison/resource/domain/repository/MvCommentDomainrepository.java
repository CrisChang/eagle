package com.poison.resource.domain.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.poison.eagle.utils.ResultUtils;
import com.poison.resource.dao.MvAvgMarkDAO;
import com.poison.resource.dao.MvCommentDAO;
import com.poison.resource.model.BkAvgMark;
import com.poison.resource.model.MvAvgMark;
import com.poison.resource.model.MvComment;

public class MvCommentDomainrepository {
	private MvCommentDAO mvCommentDAO;
	
	private MvAvgMarkDAO mvAvgMarkDAO;

	
	
	public void setMvAvgMarkDAO(MvAvgMarkDAO mvAvgMarkDAO) {
		this.mvAvgMarkDAO = mvAvgMarkDAO;
	}
	public void setMvCommentDAO(MvCommentDAO mvCommentDAO) {
		this.mvCommentDAO = mvCommentDAO;
	}
	/**
	 * 
	 * 方法的描述 :此方法的作用是插入一条评论
	 * @param comment
	 * @return
	 */
	public MvComment addMvComment(MvComment comment){
		//判断是否已经评论过了
		long id = comment.getId();
		List<MvComment> mvCommentList = new ArrayList<MvComment>();//mvCommentDAO.findUserMvComment(comment);
		//用户没有评论的时候，插入评论
		int flag = ResultUtils.ERROR;
		MvComment mvComment = new MvComment();
		if(null==mvCommentList||mvCommentList.size()==0){
			flag = mvCommentDAO.addMvComment(comment);
			mvComment = mvCommentDAO.findMvCommentIsExist(id);
			flag = ResultUtils.SUCCESS;
			if(null==mvComment){
				mvComment = new MvComment();
				flag = ResultUtils.DATAISNULL;
			}
		}else if(comment.getStageid()>0){
			//存在大赛的影评，则抛错为：大赛影评内容不允许修改
			flag = ResultUtils.MATCH_MV_COMMENT_NOT_ALLOW_UPDATE;
		}
		mvComment.setFlag(flag);
		return mvComment;
	}
	/**
	 * 
	 * 方法的描述 :查询这部电影的评论内容
	 * @param movieId
	 * @param id
	 * @return
	 */
	public List<MvComment> findAllMvComment(long movieId, Long id,String type,String resourceType){
		return mvCommentDAO.findAllMvComment(movieId, id,type,resourceType);
	}
	/**
	 * for旧版
	 * @param movieId
	 * @param id
	 * @param type
	 * @param resourceType
	 * @return
	 */
	public List<MvComment> findAllMvCommentForOld(long movieId, Long id,String type,String resourceType,Integer pageIndex, Integer pageSize){
		return mvCommentDAO.findAllMvCommentForOld(movieId, id, type, resourceType,pageIndex,pageSize);
	}
	/**
	 * 
	 * 方法的描述 :查询所有评论
	 * @param id
	 * @return
	 */
	public List<MvComment> findAllComment(Long id){
		return mvCommentDAO.findAllComment(id);
	}
	
	/**
	 * 
	 * <p>Title: findUserMvComment</p> 
	 * <p>Description: 查询用户的评论信息</p> 
	 * @author :changjiang
	 * date 2014-8-26 下午12:26:49
	 * @param mvComment
	 * @return
	 */
	public List<MvComment> findUserMvComment(MvComment mvComment){
		return mvCommentDAO.findUserMvComment(mvComment);
	}
	
	/**
	 * 
	 * <p>Title: updateMyMvComment</p> 
	 * <p>Description: 更新我的影评</p> 
	 * @author :changjiang
	 * date 2014-8-26 下午3:47:38
	 * @param mvComment
	 * @return
	 */
	public MvComment updateMyMvComment(long id,String content,String score,String title,String cover){
		MvComment comment = mvCommentDAO.findMvCommentIsExist(id);
		int flag = comment.getFlag();
		if(ResultUtils.SUCCESS==flag){
			long sysdate = System.currentTimeMillis();
			comment.setContent(content);
			comment.setScore(score);
			comment.setLatestRevisionDate(sysdate);
			comment.setTitle(title);
			comment.setCover(cover);
			flag = mvCommentDAO.updateMyMvComment(comment);
			comment = mvCommentDAO.findMvCommentIsExist(id);
			return comment;
		}
		comment.setFlag(ResultUtils.ERROR);
		return comment;
	}
	
	/**
	 * 
	 * <p>Title: deleteMvComment</p> 
	 * <p>Description: 删除一条评论</p> 
	 * @author :changjiang
	 * date 2014-8-26 下午4:42:41
	 * @param id
	 * @return
	 */
	public MvComment deleteMvComment(long id){
		MvComment comment = mvCommentDAO.findMvCommentIsExist(id);
		int flag = comment.getFlag();
		if(ResultUtils.SUCCESS==flag){
			long sysdate = System.currentTimeMillis();
			comment.setIsDel(1);
			comment.setLatestRevisionDate(sysdate);
			flag = mvCommentDAO.updateMyMvComment(comment);
			comment.setFlag(flag);
		}
		return comment;
	}
	/**
	 * 
	 * <p>Title: findMvCommentCount</p> 
	 * <p>Description: 查找电影的评论总数</p> 
	 * @author :changjiang
	 * date 2014-8-26 下午4:04:01
	 * @param movieId
	 * @return
	 */
	public int findMvCommentCount(long movieId){
		return mvCommentDAO.findMvCommentCount(movieId);
	}
	
	
	
	/**
	 * 
	 * <p>Title: findMvCommentIsExist</p> 
	 * <p>Description: 查询一个影评</p> 
	 * @author :changjiang
	 * date 2014-8-26 下午9:54:10
	 * @param id
	 * @return
	 */
	public MvComment findMvCommentIsExist(long id){
		return mvCommentDAO.findMvCommentIsExist(id);
	}
	
	/**
	 * 
	 * <p>Title: findAllMvCommentListByType</p> 
	 * <p>Description: 根据type查询电影评论</p> 
	 * @author :changjiang
	 * date 2014-9-18 下午1:15:36
	 * @param userId
	 * @param type
	 * @param resId
	 * @return
	 */
	public List<MvComment> findAllMvCommentListByType(Long userId, String type,
			Long resId) {
		return mvCommentDAO.findAllMvCommentListByType(userId, type, resId);
	}
	
	/**
	 * 
	 * <p>Title: insertBkAvgMark</p> 
	 * <p>Description: 插入一本书的打分</p> 
	 * @author :changjiang
	 * date 2014-9-9 下午3:50:16
	 * @param bkAvgMark
	 * @return
	 */
	public MvAvgMark addMvAvgMark(MvAvgMark mvAvgMark){
		long mvId = mvAvgMark.getMvId();
		MvAvgMark avgMark = mvAvgMarkDAO.findMvAvgMarkByMvId(mvId);
		int flag = avgMark.getFlag();
		//获取用户的评分和人数
		float userScore = mvAvgMark.getMvAvgMark();
		int userTotalNum = mvAvgMark.getMvTotalNum();
		//当这本书的评分为空时插入一条评分信息
		if(ResultUtils.DATAISNULL==flag){
			flag = mvAvgMarkDAO.insertMvAvgMark(mvAvgMark);
			avgMark = mvAvgMarkDAO.findMvAvgMarkByMvId(mvId);
		}//当这本书的评分不为空时，更改一个信息
		else if(ResultUtils.SUCCESS==flag){
			float avgScore = avgMark.getMvAvgMark();
			int totalNum = avgMark.getMvTotalNum();
			totalNum = totalNum+userTotalNum;
			avgScore = avgScore + (userScore-avgScore)/totalNum;
			//avgScore = Math.round(avgScore*100)/100;
			mvAvgMark.setMvAvgMark(avgScore);
			mvAvgMark.setMvTotalNum(totalNum);
			mvAvgMarkDAO.updateMvAvgMark(mvAvgMark);
			avgMark = mvAvgMarkDAO.findMvAvgMarkByMvId(mvId);
		}
		return avgMark;
	}
	
	/**
	 * 
	 * <p>Title: addExpertsAvgMark</p> 
	 * <p>Description: 添加专家评分</p> 
	 * @author :changjiang
	 * date 2014-12-12 上午11:26:56
	 * @param mvAvgMark
	 * @return
	 */
	public MvAvgMark addExpertsAvgMark(MvAvgMark mvAvgMark){
		long mvId = mvAvgMark.getMvId();
		MvAvgMark avgMark = mvAvgMarkDAO.findMvAvgMarkByMvId(mvId);
		int flag = avgMark.getFlag();
		//获取专家的评分和人数
		float userScore = mvAvgMark.getExpertsAvgMark();
		int userTotalNum = mvAvgMark.getExpertsTotalNum();
		//当这本书的评分为空时插入一条评分信息
		if(ResultUtils.DATAISNULL==flag){
			flag = mvAvgMarkDAO.insertMvAvgMark(mvAvgMark);
			avgMark = mvAvgMarkDAO.findMvAvgMarkByMvId(mvId);
		}//当这本书的评分不为空时，更改一个信息
		else if(ResultUtils.SUCCESS==flag){
			float avgScore = avgMark.getExpertsAvgMark();
			int totalNum = avgMark.getExpertsTotalNum();
			totalNum = totalNum+userTotalNum;
			avgScore = avgScore + (userScore-avgScore)/totalNum;
			//avgScore = Math.round(avgScore*100)/100;
			mvAvgMark.setExpertsAvgMark(avgScore);
			mvAvgMark.setExpertsTotalNum(totalNum);
			mvAvgMarkDAO.updateExpertsAvgMark(mvAvgMark);
			avgMark = mvAvgMarkDAO.findMvAvgMarkByMvId(mvId);
		}
		return avgMark;
	}
	
	/**
	 * 
	 * <p>Title: updateBkAvgMark</p> 
	 * <p>Description: 更新一个人的评论</p> 
	 * @author :changjiang
	 * date 2014-9-9 下午5:19:13
	 * @param bkAvgMark
	 * @param beforeScore
	 * @return
	 */
	public MvAvgMark updateMvAvgMark(MvAvgMark mvAvgMark,float beforeScore){
		long mvId = mvAvgMark.getMvId();
		int userTotalNum = mvAvgMark.getMvTotalNum();
		float userAvgMark = mvAvgMark.getMvAvgMark();
		MvAvgMark avgMark = mvAvgMarkDAO.findMvAvgMarkByMvId(mvId);
		int totalNum = avgMark.getMvTotalNum();
		float avgScore = avgMark.getMvAvgMark();
		float totalScore = avgScore*totalNum-beforeScore;
		totalNum = totalNum-userTotalNum;
		if(totalNum==0){
			avgScore = userAvgMark/(totalNum+userTotalNum);
		}else{
			 //totalScore/totalNum+
			avgScore =totalScore/totalNum+(userAvgMark-totalScore/totalNum)/(totalNum+userTotalNum);
		}
		if(avgScore<0){
			avgScore = 0;
		}
		mvAvgMark.setMvAvgMark(avgScore);
		mvAvgMark.setMvTotalNum(totalNum+userTotalNum);
		mvAvgMarkDAO.updateMvAvgMark(mvAvgMark);
		avgMark = mvAvgMarkDAO.findMvAvgMarkByMvId(mvId);
		return avgMark;
	}
	
	/**
	 * 
	 * <p>Title: updateExpertsAvgMark</p> 
	 * <p>Description: 更新专家的平均分</p> 
	 * @author :changjiang
	 * date 2014-12-12 上午11:33:55
	 * @param mvAvgMark
	 * @param beforeScore
	 * @return
	 */
	public MvAvgMark updateExpertsAvgMark(MvAvgMark mvAvgMark,float beforeScore){
		long mvId = mvAvgMark.getMvId();
		int userTotalNum = mvAvgMark.getExpertsTotalNum();
		float userAvgMark = mvAvgMark.getExpertsAvgMark();
		MvAvgMark avgMark = mvAvgMarkDAO.findMvAvgMarkByMvId(mvId);
		int totalNum = avgMark.getExpertsTotalNum();
		float avgScore = avgMark.getExpertsAvgMark();
		float totalScore = avgScore*totalNum-beforeScore;
		totalNum = totalNum-userTotalNum;
		if(totalNum==0){
			avgScore = userAvgMark/(totalNum+userTotalNum);
		}else{
			 //totalScore/totalNum+
			avgScore =totalScore/totalNum+(userAvgMark-totalScore/totalNum)/(totalNum+userTotalNum);
		}
		if(avgScore<0){
			avgScore = 0;
		}
		mvAvgMark.setExpertsAvgMark(avgScore);
		mvAvgMark.setExpertsTotalNum(totalNum+userTotalNum);
		mvAvgMarkDAO.updateExpertsAvgMark(mvAvgMark);
		avgMark = mvAvgMarkDAO.findMvAvgMarkByMvId(mvId);
		return avgMark;
	}
	
	/**
	 * 
	 * <p>Title: deleteMvAvgMark</p> 
	 * <p>Description: 删除电影的评分</p> 
	 * @author :changjiang
	 * date 2014-9-18 下午12:15:32
	 * @param mvAvgMark
	 * @param beforeScore
	 * @return
	 */
	public MvAvgMark deleteMvAvgMark(MvAvgMark mvAvgMark,float beforeScore){
		long mvId = mvAvgMark.getMvId();
		int userTotalNum = mvAvgMark.getMvTotalNum();
		float userAvgMark = mvAvgMark.getMvAvgMark();
		MvAvgMark avgMark = mvAvgMarkDAO.findMvAvgMarkByMvId(mvId);
		int totalNum = avgMark.getMvTotalNum();
		float avgScore = avgMark.getMvAvgMark();
		float totalScore = avgScore*totalNum-beforeScore;
		totalNum = totalNum-userTotalNum;
		if(totalNum==0){
			avgScore = 0;
		}else{
			avgScore =totalScore/totalNum;
		}
		mvAvgMark.setMvAvgMark(avgScore);
		mvAvgMark.setMvTotalNum(totalNum);
		mvAvgMarkDAO.updateMvAvgMark(mvAvgMark);
		avgMark = mvAvgMarkDAO.findMvAvgMarkByMvId(mvId);
		return avgMark;
	}
	
	/**
	 * 
	 * <p>Title: deleteExpertsAvgMark</p> 
	 * <p>Description: 删除专家评分</p> 
	 * @author :changjiang
	 * date 2014-12-12 上午11:37:16
	 * @param mvAvgMark
	 * @param beforeScore
	 * @return
	 */
	public MvAvgMark deleteExpertsAvgMark(MvAvgMark mvAvgMark,float beforeScore){
		long mvId = mvAvgMark.getMvId();
		int userTotalNum = mvAvgMark.getExpertsTotalNum();
		float userAvgMark = mvAvgMark.getExpertsAvgMark();
		MvAvgMark avgMark = mvAvgMarkDAO.findMvAvgMarkByMvId(mvId);
		int totalNum = avgMark.getExpertsTotalNum();
		float avgScore = avgMark.getExpertsAvgMark();
		float totalScore = avgScore*totalNum-beforeScore;
		totalNum = totalNum-userTotalNum;
		if(totalNum==0){
			avgScore = 0;
		}else{
			avgScore =totalScore/totalNum;
		}
		mvAvgMark.setExpertsAvgMark(avgScore);
		mvAvgMark.setExpertsTotalNum(totalNum);
		mvAvgMarkDAO.updateExpertsAvgMark(mvAvgMark);
		avgMark = mvAvgMarkDAO.findMvAvgMarkByMvId(mvId);
		return avgMark;
	}
	
	/**
	 * 
	 * <p>Title: findMvAvgMarkByMvId</p> 
	 * <p>Description: 查询电影的评分</p> 
	 * @author :changjiang
	 * date 2014-9-11 下午2:55:29
	 * @param mvId
	 * @return
	 */
	public MvAvgMark findMvAvgMarkByMvId(long mvId){
		return mvAvgMarkDAO.findMvAvgMarkByMvId(mvId);
	}
	/**
	 * 根据电影的id集合查询评分信息
	 */
	public List<MvAvgMark> findMvAvgMarkByMvIds(List<Long> mvids){
		return mvAvgMarkDAO.findMvAvgMarkByMvIds(mvids);
	}
	/**
	 * 
	 * <p>Title: updateMvCommentBigValue</p> 
	 * <p>Description: 更新影评的逼格值</p> 
	 * @author :changjiang
	 * date 2014-9-29 下午2:21:59
	 * @param id
	 * @param bigValue
	 * @return
	 */
	public MvComment updateMvCommentBigValue(long id, float bigValue){
		int flag = mvCommentDAO.updateMvCommentBigValue(id, bigValue);
		MvComment mvComment = new MvComment();
		mvComment.setFlag(ResultUtils.ERROR);
		if(ResultUtils.SUCCESS==flag){
			mvComment = mvCommentDAO.findMvCommentIsExist(id);
		}
		return mvComment;
	}
	
	/**
	 * 
	 * <p>Title: findAllMvCommentListByUsersId</p> 
	 * <p>Description: 根据用户id查询电影评论</p> 
	 * @author :changjiang
	 * date 2014-10-27 下午4:30:12
	 * @param usersId
	 * @param type
	 * @param resId
	 * @return
	 */
	public List<MvComment> findAllMvCommentListByUsersId(List<Long> usersId,
			String type, Long resId){
		return mvCommentDAO.findAllMvCommentListByUsersId(usersId, type, resId);
	}
	
	/**
	 * 
	 * <p>Title: findMvCommentCountByUid</p> 
	 * <p>Description: 根据uid查询电影评论总数</p> 
	 * @author :changjiang
	 * date 2014-12-9 下午7:23:14
	 * @param userId
	 * @return
	 */
	public Map<String, Object> findMvCommentCountByUid(long userId){
		return mvCommentDAO.findMvCommentCountByUid(userId);
	}
	
	/**
	 * 
	 * <p>Title: findUserLongMvCommentListByUserId</p> 
	 * <p>Description: 示例类</p> 
	 * @author :changjiang
	 * date 2015-6-24 下午6:26:43
	 * @param userId
	 * @param resId
	 * @return
	 */
	public List<MvComment> findUserLongMvCommentListByUserId(long userId,
			Long resId){
		return mvCommentDAO.findUserLongMvCommentListByUserId(userId, resId);
	}
	/**
	 * 查询某个用户某个时间段的影评
	 */
	public List<MvComment> findUserMvCommentsByTime(long userId,Long starttime,Long endtime){
		return mvCommentDAO.findUserMvCommentsByTime(userId, starttime, endtime);
	}
	/**
	 * 根据标题和内容模糊查询，查询某个时间段的
	 */
	public List<MvComment> searchMvCommentByLike(long userId,String keyword,Long starttime,Long endtime,long start,int pageSize){
		return mvCommentDAO.searchMvCommentByLike(userId, keyword, starttime, endtime,start,pageSize);
	}
	/**
	 * 根据模糊查询条件查询帖子的数量
	 */
	public Map<String, Object> findMvCommentCountByLike(long userId,String keyword,Long starttime,Long endtime){
		return mvCommentDAO.findMvCommentCountByLike(userId, keyword, starttime, endtime);
	}
	
	/**
	 * 根据阶段id查询影评列表根据评委评分排序 
	 * @param stageid
	 * @param start
	 * @param pagesize
	 * @return
	 */
	public List<MvComment> findMvCommentsByStageidOrderbyPoint(long stageid,long start,int pagesize){
		return mvCommentDAO.findMvCommentsByStageidOrderbyPoint(stageid, start, pagesize);
	}
	
	/**
	 * 根据阶段id查询影评列表根据评委评分排序 
	 * @param stageid
	 * @param start
	 * @param pagesize
	 * @return
	 */
	public List<MvComment> findMvCommentsByStageidOrderbyId(long stageid,long start,int pagesize){
		return mvCommentDAO.findMvCommentsByStageidOrderbyId(stageid, start, pagesize);
	}
	/**
	 * 根据阶段id查询用户影评得分排行榜按评委评分排序 
	 * @param stageid
	 * @param start
	 * @param pagesize
	 * @return
	 */
	public List<MvComment> findMvCommentUserRankByStageid(long stageid,long start,int pagesize){
		return mvCommentDAO.findMvCommentUserRankByStageid(stageid, start, pagesize);
	}
	/**
	 * 根据阶段id查询某个人的影评列表根据评委评分排序
	 * @param stageid
	 * @param start
	 * @param pagesize
	 * @return
	 */
	public List<MvComment> findMvCommentsByUseridAndStageid(long userId,long stageid,long start,int pagesize){
		return mvCommentDAO.findMvCommentsByUseridAndStageid(userId, stageid, start, pagesize);
	}
	/**
	 * 根据阶段id查询某个电影的影评列表
	 * @param stageid
	 * @param start
	 * @param pagesize
	 * @return
	 */
	public List<MvComment> findMvCommentsByMovieidAndStageid(long movieId,long stageid,Long resId,int pagesize){
		return mvCommentDAO.findMvCommentsByMovieidAndStageid(movieId, stageid, resId, pagesize);
	}
	/**
	 * 根据影评id集合查询影评列表 
	 * @param stageid
	 * @param start
	 * @param pagesize
	 * @return
	 */
	public List<MvComment> findMvCommentsByIdsAndStageid(long stageid,List<Long> commentids){
		return mvCommentDAO.findMvCommentsByIdsAndStageid(stageid, commentids);
	}
	public List<MvComment> findMvCommentsByIds(List<Long> commentids){
		return mvCommentDAO.findMvCommentsByIds(commentids);
	}
	/**
	 * 查询是否存在某个用户的影评记录
	 */
	public long findMvCommentRecord(long userid){
		return mvCommentDAO.findMvCommentRecord(userid);
	}

	/**
	 * 查询影评
	 * @param movieId
	 * @param resourceType
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	public List<MvComment> findOneMvCommentListByResTypeAndPage(long movieId, String resourceType, int pageIndex, int pageSize){
		return mvCommentDAO.findOneMvCommentListByResTypeAndPage(movieId, resourceType, pageIndex, pageSize);
	}
}
