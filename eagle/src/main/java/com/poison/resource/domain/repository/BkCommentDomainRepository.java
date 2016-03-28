package com.poison.resource.domain.repository;

import java.util.List;
import java.util.Map;

import com.poison.eagle.utils.ResultUtils;
import com.poison.resource.dao.BkAvgMarkDAO;
import com.poison.resource.dao.BkCommentDAO;
import com.poison.resource.model.BkAvgMark;
import com.poison.resource.model.BkComment;
import com.poison.store.dao.BkInfoDAO;

public class BkCommentDomainRepository {

	private BkCommentDAO bkCommentDAO;

	private BkAvgMarkDAO bkAvgMarkDAO;
	
	private BkInfoDAO bkInfoDAO;
	
	public void setBkInfoDAO(BkInfoDAO bkInfoDAO) {
		this.bkInfoDAO = bkInfoDAO;
	}

	public void setBkAvgMarkDAO(BkAvgMarkDAO bkAvgMarkDAO) {
		this.bkAvgMarkDAO = bkAvgMarkDAO;
	}

	public void setBkCommentDAO(BkCommentDAO bkCommentDAO) {
		this.bkCommentDAO = bkCommentDAO;
	}

	/**
	 * 
	 * <p>Title: addOneBkComment</p> 
	 * <p>Description: 插入一条书评</p> 
	 * @author :changjiang
	 * date 2014-8-6 下午4:58:53
	 * @param bkComment
	 * @return
	 */
	public BkComment addOneBkComment(BkComment bkComment){
		//判断是否评论过了
		long userId = bkComment.getUserId();
		int bookId = bkComment.getBookId();
		long id = bkComment.getId();
		String resType = bkComment.getResType();
		List<BkComment> bkCommentList = null;//bkCommentDAO.findMyBkCommentList(userId,bookId,null,resType);
		int flag = ResultUtils.ERROR;
		//尚未评论过，添加评论
		BkComment comment = new BkComment();
		if(null==bkCommentList||bkCommentList.size()==0){
			flag =  bkCommentDAO.insertBkComment(bkComment);
			comment  = bkCommentDAO.findCommentIsExistById(id);
			flag = ResultUtils.SUCCESS;
			if(null==comment){
				comment = new BkComment();
				flag = ResultUtils.DATAISNULL;
			}
		}
		comment.setFlag(flag);
		/*comment.setFlag(flag);
		if(ResultUtils.SUCCESS==flag){
			//long id = bkComment.getId();
			comment= bkCommentDAO.findCommentIsExistById(id);
			comment.setFlag(ResultUtils.SUCCESS);
		}*/
		return comment;
	}
	
	/**
	 * 
	 * <p>Title: findAllBkComment</p> 
	 * <p>Description: 查询一本书的所有评论</p> 
	 * @author :changjiang
	 * date 2014-8-7 下午8:53:16
	 * @return
	 */
	public List<BkComment> findAllBkComment(int bookId,Long resId,String resType,String resourceType){
		return bkCommentDAO.findAllBkComment(bookId, resId,resType,resourceType);
	}
	
	/**
	 * 查询所有书的评论
	 */
	public List<BkComment> findBkCommentListForOld(int bookId,Long resId,String resType,String resourceType){
		return bkCommentDAO.findBkCommentListForOld(bookId, resId, resType, resourceType);
	}
	/**
	 *  根据id集合查询
	 */
	public List<BkComment> findBkCommentListByIds(List<Long> ids){
		return bkCommentDAO.findBkCommentListByIds(ids);
	}
	/**
	 * 
	 * <p>Title: findAllBkCommentListByType</p> 
	 * <p>Description: 示例类</p> 
	 * @author :changjiang
	 * date 2014-9-12 下午3:31:33
	 * @param userId
	 * @param type
	 * @param resId
	 * @return
	 */
	public List<BkComment> findAllBkCommentListByType(Integer userId, String type,
			Long resId){
		return bkCommentDAO.findAllBkCommentListByType(userId, type, resId);
	}
	/**
	 * 
	 * <p>Title: findAllComment</p> 
	 * <p>Description: 查询所有评论</p> 
	 * @author :changjiang
	 * date 2014-8-8 下午2:59:36
	 * @param resId
	 * @return
	 */
	public List<BkComment> findAllComment(Long resId){
		return bkCommentDAO.findAllComment(resId);
	}
	
	/**
	 * 
	 * <p>Title: findMyBkCommentList</p> 
	 * <p>Description: 查询自己对一本书的所有评论</p> 
	 * @author :changjiang
	 * date 2014-8-9 下午9:11:40
	 * @param userId
	 * @param bookId
	 * @param resId
	 * @return
	 */
	public List<BkComment> findMyBkCommentList(long userId, int bookId,
			Long resId,String resType){
		return bkCommentDAO.findMyBkCommentList(userId, bookId, resId,resType);
	}
	
	/**
	 * 
	 * <p>Title: findCommentCount</p> 
	 * <p>Description: 查找一本书的评论总数</p> 
	 * @author :changjiang
	 * date 2014-8-17 下午2:40:55
	 * @param bookId
	 * @return
	 */
	public int findCommentCount(int bookId,String resType){
		return bkCommentDAO.findCommentCount(bookId,resType);
	}
	
	/**
	 * 
	 * <p>Title: updateMyCommentByBook</p> 
	 * <p>Description: 修改用户对一本书的评论</p> 
	 * @author :changjiang
	 * date 2014-8-17 下午3:22:46
	 * @param bkComment
	 * @return
	 */
	public BkComment updateMyCommentByBook(BkComment bkComment){
		int flag = ResultUtils.ERROR;
		long id = bkComment.getId();
		BkComment comment = bkCommentDAO.findCommentIsExistById(id);
		if(null == comment){
			comment = new BkComment();
			flag = ResultUtils.DATAISNULL;
			comment.setFlag(flag);
			return comment;
		}
		flag = bkCommentDAO.updateMyCommentByBook(bkComment);
		comment = bkCommentDAO.findCommentIsExistById(id);
		comment.setFlag(ResultUtils.SUCCESS);
		return comment;
	}
	
	/**
	 * 
	 * <p>Title: findCommentIsExistById</p> 
	 * <p>Description:查询一条书评</p> 
	 * @author :changjiang
	 * date 2014-8-18 下午4:31:53
	 * @param id
	 * @return
	 */
	public BkComment findCommentIsExistById(long id){
		return bkCommentDAO.findCommentIsExistById(id);
	}
	
	/**
	 * 
	 * <p>Title: deleteMyCommentById</p> 
	 * <p>Description: 删除一条的评论</p> 
	 * @author :changjiang
	 * date 2014-9-11 下午3:33:17
	 * @param id
	 * @return
	 */
	public BkComment deleteMyCommentById(long id){
		int flag = ResultUtils.ERROR;
		BkComment bkComment = bkCommentDAO.findCommentIsExistById(id);
		if(null==bkComment){
			bkComment = new BkComment();
			flag = ResultUtils.DATAISNULL;
			bkComment.setFlag(flag);
			return bkComment;
		}
		flag = bkCommentDAO.deleteMyCommentById(id);
		bkComment.setFlag(flag);
		return bkComment;
	}
	/**
	 * 
	 * <p>Title: findCommentListByUserId</p> 
	 * <p>Description: 根据用户ID查询该用户的评论列表</p> 
	 * @author :changjiang
	 * date 2014-8-19 下午7:55:38
	 * @param userId
	 * @param resId
	 * @return
	 */
	public List<BkComment> findCommentListByUserId(long userId,Long resId){
		return bkCommentDAO.findCommentListByUserId(userId, resId);
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
	public BkAvgMark addBkAvgMark(BkAvgMark bkAvgMark){
		int bkId = bkAvgMark.getBkId();
		BkAvgMark avgMark = bkAvgMarkDAO.findBkAvgMarkByBkId(bkId);
		int flag = avgMark.getFlag();
		//获取用户的评分和人数
		float userScore = bkAvgMark.getBkAvgMark();
		int userTotalNum = bkAvgMark.getBkTotalNum();
		//当这本书的评分为空时插入一条评分信息
		if(ResultUtils.DATAISNULL==flag){
			flag = bkAvgMarkDAO.insertBkAvgMark(bkAvgMark);
			avgMark = bkAvgMarkDAO.findBkAvgMarkByBkId(bkId);
		}//当这本书的评分不为空时，更改一个信息
		else if(ResultUtils.SUCCESS==flag){
			float avgScore = avgMark.getBkAvgMark();
			int totalNum = avgMark.getBkTotalNum();
			totalNum = totalNum+userTotalNum;
			avgScore = avgScore + (userScore-avgScore)/totalNum;
			//avgScore = Math.round(avgScore*100)/100;
			bkAvgMark.setBkAvgMark(avgScore);
			bkAvgMark.setBkTotalNum(totalNum);
			bkAvgMarkDAO.updateBkAvgMark(bkAvgMark);
			avgMark = bkAvgMarkDAO.findBkAvgMarkByBkId(bkId);
		}
		return avgMark;
	}
	
	/**
	 * 
	 * <p>Title: addExpertsBkAvgMark</p> 
	 * <p>Description: 增加神人的平均分</p> 
	 * @author :changjiang
	 * date 2015-5-15 下午3:54:58
	 * @param bkAvgMark
	 * @return
	 */
	public BkAvgMark addExpertsBkAvgMark(BkAvgMark bkAvgMark){
		int bkId = bkAvgMark.getBkId();
		BkAvgMark avgMark = bkAvgMarkDAO.findBkAvgMarkByBkId(bkId);
		int flag = avgMark.getFlag();
		//获取用户的评分和人数
		float userScore = bkAvgMark.getExpertsAvgMark();
		int userTotalNum = bkAvgMark.getExpertsTotalNum();
		//当这本书的评分为空时插入一条评分信息
		if(ResultUtils.DATAISNULL==flag){
			flag = bkAvgMarkDAO.insertBkAvgMark(bkAvgMark);
			avgMark = bkAvgMarkDAO.findBkAvgMarkByBkId(bkId);
		}//当这本书的评分不为空时，更改一个信息
		else if(ResultUtils.SUCCESS==flag){
			float avgScore = avgMark.getExpertsAvgMark();
			int totalNum = avgMark.getExpertsTotalNum();
			totalNum = totalNum+userTotalNum;
			avgScore = avgScore + (userScore-avgScore)/totalNum;
			//avgScore = Math.round(avgScore*100)/100;
			bkAvgMark.setExpertsAvgMark(avgScore);
			bkAvgMark.setExpertsTotalNum(totalNum);
			bkAvgMarkDAO.updateExpertsAvgMark(bkAvgMark);
			avgMark = bkAvgMarkDAO.findBkAvgMarkByBkId(bkId);
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
	public BkAvgMark updateBkAvgMark(BkAvgMark bkAvgMark,float beforeScore){
		int bkId = bkAvgMark.getBkId();
		int userTotalNum = bkAvgMark.getBkTotalNum();
		float userAvgMark = bkAvgMark.getBkAvgMark();
		BkAvgMark avgMark = bkAvgMarkDAO.findBkAvgMarkByBkId(bkId);
		int totalNum = avgMark.getBkTotalNum();
		float avgScore = avgMark.getBkAvgMark();
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
		bkAvgMark.setBkAvgMark(avgScore);
		bkAvgMark.setBkTotalNum(totalNum+userTotalNum);
		bkAvgMarkDAO.updateBkAvgMark(bkAvgMark);
		avgMark = bkAvgMarkDAO.findBkAvgMarkByBkId(bkId);
		return avgMark;
	}
	
	/**
	 * 
	 * <p>Title: deleteBkAvgMark</p> 
	 * <p>Description: 删除一本书的评分</p> 
	 * @author :changjiang
	 * date 2014-9-18 上午11:51:11
	 * @param bkId
	 * @param bkAvgMark
	 * @param beforeScore
	 * @return
	 */
	public BkAvgMark deleteBkAvgMark(BkAvgMark bkAvgMark,float beforeScore){
		int bkId = bkAvgMark.getBkId();
		int userTotalNum = bkAvgMark.getBkTotalNum();
		float userAvgMark = bkAvgMark.getBkAvgMark();
		BkAvgMark avgMark = bkAvgMarkDAO.findBkAvgMarkByBkId(bkId);
		int totalNum = avgMark.getBkTotalNum();
		float avgScore = avgMark.getBkAvgMark();
		float totalScore = avgScore*totalNum-beforeScore;
		totalNum = totalNum-userTotalNum;
		if(totalNum==0){
			avgScore = 0;
		}else{
			avgScore =totalScore/totalNum;
		}
		bkAvgMark.setBkAvgMark(avgScore);
		bkAvgMark.setBkTotalNum(totalNum);
		bkAvgMarkDAO.updateBkAvgMark(bkAvgMark);
		avgMark = bkAvgMarkDAO.findBkAvgMarkByBkId(bkId);
		return avgMark;
	}
	
	/**
	 * 
	 * <p>Title: findBkAvgMarkByBkId</p> 
	 * <p>Description: 查询一本书的评分</p> 
	 * @author :changjiang
	 * date 2014-9-11 下午2:44:33
	 * @param bkId
	 * @return
	 */
	public BkAvgMark findBkAvgMarkByBkId(int bkId){
		return bkAvgMarkDAO.findBkAvgMarkByBkId(bkId);
	}
	/**
	 * 根据书的id集合查询和书的类型查询评分信息
	 */
	public List<BkAvgMark> findBkAvgMarkByBkIds(List<Long> bkids,String type){
		return bkAvgMarkDAO.findBkAvgMarkByBkIds(bkids, type);
	}
	/**
	 * 
	 * <p>Title: updateBkCommentBigValue</p> 
	 * <p>Description: 更新书评的逼格值</p> 
	 * @author :changjiang
	 * date 2014-9-29 上午11:26:41
	 * @param id
	 * @param bigValue
	 * @return
	 */
	public BkComment updateBkCommentBigValue(long id, float bigValue){
		int flag = bkCommentDAO.updateBkCommentBigValue(id, bigValue);
		BkComment bkComment = new BkComment();
		bkComment.setFlag(ResultUtils.ERROR);
		if(ResultUtils.SUCCESS==flag){
			bkComment = bkCommentDAO.findCommentIsExistById(id);
			bkComment.setFlag(ResultUtils.SUCCESS);
		}
		return bkComment;
	}
	
	/**
	 * 
	 * <p>Title: findBkCommentCount</p> 
	 * <p>Description: 查询书评的总数</p> 
	 * @author :changjiang
	 * date 2014-12-9 下午6:49:36
	 * @param userId
	 * @return
	 */
	public Map<String, Object> findBkCommentCount(long userId){
		return bkCommentDAO.findBkCommentCount(userId);
	}
	
	/**
	 * 
	 * <p>Title: findMostCommentBK</p> 
	 * <p>Description: 查找最多</p> 
	 * @author :changjiang
	 * date 2015-1-27 下午10:15:43
	 * @param bkTotalNum
	 * @param type
	 * @return
	 */
	public List<BkAvgMark> findMostCommentBK(Integer bkTotalNum, String type){
		return null;
	}
	
	/**
	 * 
	 * <p>Title: updateBkExpertsAvgMark</p> 
	 * <p>Description: 修改神人的书评分</p> 
	 * @author :changjiang
	 * date 2015-5-15 下午5:09:26
	 * @param bkAvgMark
	 * @param beforeScore
	 * @return
	 */
	public BkAvgMark updateBkExpertsAvgMark(BkAvgMark bkAvgMark,
			float beforeScore){
		int bkId = bkAvgMark.getBkId();
		int userTotalNum = bkAvgMark.getExpertsTotalNum();
		float userAvgMark = bkAvgMark.getExpertsAvgMark();
		BkAvgMark avgMark = bkAvgMarkDAO.findBkAvgMarkByBkId(bkId);
		int totalNum = avgMark.getBkTotalNum();
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
		bkAvgMark.setExpertsAvgMark(avgScore);
		bkAvgMark.setExpertsTotalNum(totalNum+userTotalNum);
		bkAvgMarkDAO.updateBkAvgMark(bkAvgMark);
		avgMark = bkAvgMarkDAO.findBkAvgMarkByBkId(bkId);
		return avgMark;
	}
	
	/**
	 * 
	 * <p>Title: findUserLongBkCommentListByUserId</p> 
	 * <p>Description: </p> 
	 * @author :changjiang
	 * date 2015-6-26 下午4:16:29
	 * @param userId
	 * @param resId
	 * @return
	 */
	public List<BkComment> findUserLongBkCommentListByUserId(long userId,
			Long resId){
		return bkCommentDAO.findUserLongBkCommentListByUserId(userId, resId);
	}
	/**
	 * 根据时间段查询用户的书评信息
	 */
	public List<BkComment> findMyBkCommentListByTime(long userId,Long starttime,Long endtime){
		return bkCommentDAO.findMyBkCommentListByTime(userId, starttime, endtime);
	}
	/**
	 * 查询是否存在某个用户的书评记录
	 */
	public long findBkCommentRecord(long userid){
		return bkCommentDAO.findBkCommentRecord(userid);
	}
}
