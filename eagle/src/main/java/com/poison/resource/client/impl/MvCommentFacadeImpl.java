package com.poison.resource.client.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import redis.clients.jedis.Jedis;

import com.keel.common.cache.redis.JedisSimpleClient;
import com.keel.common.cache.redis.JedisWorker;
import com.keel.utils.UKeyWorker;
import com.poison.eagle.manager.MovieManager;
import com.poison.eagle.manager.SensitiveManager;
import com.poison.eagle.utils.CommentUtils;
import com.poison.resource.client.MvCommentFacade;
import com.poison.resource.client.MyMovieFacade;
import com.poison.resource.ext.constant.ResStatisticConstant;
import com.poison.resource.ext.utils.ResRandomUtils;
import com.poison.resource.model.BkAvgMark;
import com.poison.resource.model.MovieList;
import com.poison.resource.model.MvAvgMark;
import com.poison.resource.model.MvComment;
import com.poison.resource.model.MvListLink;
import com.poison.resource.model.ResStatistic;
import com.poison.resource.service.MovieListService;
import com.poison.resource.service.MvCommentService;
import com.poison.resource.service.ResStatisticService;
import com.poison.store.client.MvFacade;
import com.poison.store.model.MvInfo;
import com.poison.ucenter.model.UserInfo;
import com.poison.ucenter.service.UcenterService;

public class MvCommentFacadeImpl implements MvCommentFacade{
	private static final  Log LOG = LogFactory.getLog(MvCommentFacadeImpl.class);
	private MvCommentService mvCommentService;
	private MovieListService movieListService;
	private UcenterService ucenterService;
	private UKeyWorker reskeyWork;
	private JedisSimpleClient resourceVisitClient;
	private ResStatisticService resStatisticService;
	private SensitiveManager sensitiveManager;
	private MvFacade mvFacade;
	private MyMovieFacade myMovieFacade;
	
	
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
	public void setMovieListService(MovieListService movieListService) {
		this.movieListService = movieListService;
	}
	public void setMvCommentService(MvCommentService mvCommentService) {
		this.mvCommentService = mvCommentService;
	}
	public void setUcenterService(UcenterService ucenterService) {
		this.ucenterService = ucenterService;
	}
	/**
	 * 此方法的作用是插入一条评论
	 */
	@Override
	public MvComment addMvComment(long userId, long movieId, String content,
			String score, int isOpposition, int isDb,String type,Long resId,String lon,String lat,String locationDescription,String locationCity,String locationArea,String title,String cover,String resType) {
		List<MovieList> flist = new ArrayList<MovieList>();
		MovieList mvList = new MovieList();
		long sysdate = System.currentTimeMillis();
			//获取默认影单
		if(null==resId){
			flist=movieListService.queryDefaultFilmList(userId);
			mvList.setFilmListName("我看过的电影");
			mvList.setUid(userId);
			//long filmListId=flist.getId();
			//当影单为空时插入新影单
			if(null==flist||flist.size()==0){
				//MovieList filmList = new MovieList();
				mvList.setId(reskeyWork.getId());
				mvList.setReason("");
				mvList.setIsDel(0);
				mvList.setType(0);
				mvList.setTag("");
				mvList.setIsPublishing(1);
				mvList.setCreateDate(sysdate);
				mvList.setLatestRevisionDate(sysdate);
				movieListService.addNewFilmList(null, mvList);
				flist=movieListService.queryDefaultFilmList(userId);
			}else{
				mvList = flist.get(0);
			}
		}else{
			mvList = movieListService.findMovieListById(resId);
		}
			MovieList mvList111 = mvList;//movieListService.queryUserMvListByName(mvList);
			long filmListId=mvList111.getId();
			//向默认影单中添加一部电影
			MvListLink listLink=new MvListLink();
			listLink.setCreateDate(new Date().getTime());
			listLink.setLatestRevisionDate(new Date().getTime());
			listLink.setId(reskeyWork.getId());
			listLink.setFilmListId(filmListId);
			listLink.setMovieId(movieId);
			listLink.setIsDel(0);
			listLink.setIsDB(0);
			//查询影单中是否存在这部电影
			 MvListLink mvListLink=new MvListLink();//movieListService.findMovieIsExist(listLink);
			//当影单中不存在这本书时插入影单
			 if(0==mvListLink.getId()){
				 mvListLink = new MvListLink();
				 mvListLink.setId(reskeyWork.getId());
				 mvListLink.setFilmListId(filmListId);
				 mvListLink.setMovieId(movieId);
				 mvListLink.setIsDB(isDb);
				 mvListLink.setIsDel(0);
				 mvListLink.setCreateDate(sysdate);
				 mvListLink.setLatestRevisionDate(sysdate);
				 movieListService.addMovieListLink(mvListLink);
				 
				//修改影单的封面
				try {
					MvInfo mvInfo = mvFacade.queryById(movieId);
					String movieListPic = mvList111.getCover();
					String mvInfoPic = mvInfo.getMoviePic();
					if(mvInfoPic == null){
						mvInfoPic = "";
					}
					if("".equals(movieListPic)){
						//修改影单封面
						myMovieFacade.updateMovieListPic(mvList111.getId(), mvInfoPic);
					}
				} catch (Exception e) {
					LOG.error(e.getMessage(), e.fillInStackTrace());
				}
			 }
			 //插入一条评论
			 MvComment cot=new MvComment();
			 long mvCommentId = reskeyWork.getId();
			 cot.setId(mvCommentId);
			 //去除敏感词汇
			 //content = sensitiveManager.checkSensitive(content, userId, mvCommentId, CommentUtils.TYPE_MOVIE_COMMENT);
			 cot.setContent(content);
			 cot.setCreateDate(sysdate);
			 cot.setIsOpposition(isOpposition);
			 cot.setIsDB(isDb);
			 cot.setIsDel(0);
			 cot.setType(type);
			 cot.setLatestRevisionDate(sysdate);
			 cot.setMovieId(movieId);
			 cot.setScore(score);
			 cot.setUserId(userId);
			 cot.setLon(lon);
			 cot.setLat(lat);
			 cot.setLocationName(locationDescription);
			 cot.setLocationCity(locationCity);
			 cot.setLocationArea(locationArea);
			 cot.setCover(cover);
			 cot.setTitle(title);
			 cot.setResourceType(resType);
		return mvCommentService.addMvComment(cot);
	}
	/**
	 * 此方法的作用是插入一条影评大赛的评论
	 */
	@Override
	public MvComment addActivityMvComment(long userId, long movieId, String content,
			String score, int isOpposition, int isDb,String type,Long resId,String lon,String lat,String locationDescription,String locationCity,String locationArea,String title,String cover,String resType,long stageid) {
		long sysdate = System.currentTimeMillis();
		 //插入一条评论
		 MvComment cot=new MvComment();
		 long mvCommentId = reskeyWork.getId();
		 cot.setId(mvCommentId);
		 //去除敏感词汇
		 //content = sensitiveManager.checkSensitive(content, userId, mvCommentId, CommentUtils.TYPE_MOVIE_COMMENT);
		 cot.setContent(content);
		 cot.setCreateDate(sysdate);
		 cot.setIsOpposition(isOpposition);
		 cot.setIsDB(isDb);
		 cot.setIsDel(0);
		 cot.setType(type);
		 cot.setLatestRevisionDate(sysdate);
		 cot.setMovieId(movieId);
		 cot.setScore(score);
		 cot.setUserId(userId);
		 cot.setLon(lon);
		 cot.setLat(lat);
		 cot.setLocationName(locationDescription);
		 cot.setLocationCity(locationCity);
		 cot.setLocationArea(locationArea);
		 cot.setCover(cover);
		 cot.setTitle(title);
		 cot.setResourceType(resType);
		 cot.setStageid(stageid);
		 cot.setPoint(0);
		return mvCommentService.addMvComment(cot);
	}
	/**
	 *查询所有评论信息
	 */
	@Override
	public List<MvComment> findAllComment(Long id) {
		return mvCommentService.findAllComment(id);
	}
	/**
	 * 查询这部电影的评论信息
	 */
	@Override
	public List<MvComment> findAllMvComment(long movieId, Long id,String type,String resourceType) {
		return mvCommentService.findAllMvComment(movieId, id,type,resourceType);
	}
	@Override
	public List<MvComment> findAllMvCommentForOld(long movieId, Long id,String type,String resourceType,Integer pageIndex, Integer pageSize){
		return mvCommentService.findAllMvCommentForOld(movieId, id, type, resourceType,pageIndex,pageSize);
	}
	
	/**
	 * 查询用户对一部电影的评论信息
	 */
	@Override
	public List<MvComment> findUserMvComment(long userId,int movieId) {
		MvComment mvComment = new MvComment();
		mvComment.setUserId(userId);
		mvComment.setMovieId(movieId);
		return mvCommentService.findUserMvComment(mvComment);
	}
	/**
	 * 查询用户对一部电影的评论信息
	 */
	@Override
	public List<MvComment> findUserMvComment(long userId,int movieId,long stageid) {
		MvComment mvComment = new MvComment();
		mvComment.setUserId(userId);
		mvComment.setMovieId(movieId);
		mvComment.setStageid(stageid);
		return mvCommentService.findUserMvComment(mvComment);
	}
	
	@Override
	public List<MvComment> findUserFriendMvComment(long userId, long movieId) {
		List<UserInfo> userInfos = ucenterService.findUserAttention(null, userId, 0, 0, CommentUtils.PAGE_SIZE);
		List<UserInfo> eachs = ucenterService.userAttentionEachList(userId);
		userInfos.addAll(eachs);
		List<MvComment> mvComments = new ArrayList<MvComment>();
		for (UserInfo userInfo : userInfos) {
			MvComment mvComment = new MvComment();
			mvComment.setUserId(userInfo.getUserId());
			mvComment.setMovieId(movieId);
			List<MvComment> list = mvCommentService.findUserMvComment(mvComment);
			if(list.size()>0){
				mvComments.add(list.get(0));
			}
		}
		
		return mvComments;
	}
	
	/** 
	 * 更新电影评论
	 */
	@Override
	public MvComment updateMyMvComment(long id, String content, String score,String title,String cover) {
		return mvCommentService.updateMyMvComment(id, content, score,title,cover);
	}
	
	/**
	 * 删除电影评论
	 */
	@Override
	public MvComment deleteMvComment(long id) {
		return mvCommentService.deleteMvComment(id);
	}
	
	/**
	 * 查询评论总数
	 */
	@Override
	public int findMvCommentCount(long movieId) {
		return mvCommentService.findMvCommentCount(movieId);
	}
	
	/**
	 * 查询影评
	 */
	@Override
	public MvComment findMvCommentIsExist(final long id) {
		/*resourceVisitClient.execute(new JedisWorker<Object>(){
			@Override
			public Object work(Jedis jedis) {
				String key = ResStatisticConstant.MVCOMMENT_STATISTIC_MARK+id+ResStatisticConstant.MVCOMMENT_STATISTIC_TYPE;
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
					resStatistic.setType(CommentUtils.TYPE_MOVIE_COMMENT);
					resStatistic.setFalseVisit(falseVisit);
					resStatistic.setVisitNumber(visitNumber);
					resStatistic.setLatestRevisionDate(sysdate);
					resStatisticService.insertResStatistic(resStatistic);
				}
				return jedis.hgetAll(key);
			}
		});*/
		return mvCommentService.findMvCommentIsExist(id);
	}
	
	/**
	 * 查询某个用户某个时间段的影评
	 */
	@Override
	public List<MvComment> findUserMvCommentsByTime(long userId,Long starttime,Long endtime){
		return mvCommentService.findUserMvCommentsByTime(userId, starttime, endtime);
	}
	
	/**
	 * 根据标题和内容模糊查询，查询某个时间段的
	 */
	@Override
	public List<MvComment> searchMvCommentByLike(long userId,String keyword,Long starttime,Long endtime,long start,int pageSize){
		return mvCommentService.searchMvCommentByLike(userId, keyword, starttime, endtime,start,pageSize);
	}
	/**
	 * 根据模糊查询条件查询帖子的数量
	 */
	@Override
	public Map<String, Object> findMvCommentCountByLike(long userId,String keyword,Long starttime,Long endtime){
		return mvCommentService.findMvCommentCountByLike(userId, keyword, starttime, endtime);
	}
	/**
	 * 增加一个评分
	 */
	@Override
	public MvAvgMark addMvAvgMark(long mvId, float mvAvgMark) {
		MvAvgMark avgMark = new MvAvgMark();
		long sysdate = System.currentTimeMillis();
		avgMark.setId(reskeyWork.getId());
		avgMark.setMvId(mvId);
		avgMark.setMvAvgMark(mvAvgMark);
		avgMark.setMvTotalNum(1);
		avgMark.setIsDelete(0);
		avgMark.setLatestRevisionDate(sysdate);
		return mvCommentService.addMvAvgMark(avgMark);
	}
	
	/**
	 * 修改一个评分
	 */
	@Override
	public MvAvgMark updateMvAvgMark(long mvId, float mvAvgMark,
			float beforeScore) {
		MvAvgMark avgMark = new MvAvgMark();
		long sysdate = System.currentTimeMillis();
		avgMark.setMvId(mvId);
		avgMark.setMvAvgMark(mvAvgMark);
		avgMark.setMvTotalNum(1);
		avgMark.setIsDelete(0);
		avgMark.setLatestRevisionDate(sysdate);
		return mvCommentService.updateMvAvgMark(avgMark, beforeScore);
	}
	
	/**
	 * 查找一个电影的评分
	 */
	@Override
	public MvAvgMark findMvAvgMarkByMvId(long mvId) {
		return mvCommentService.findMvAvgMarkByMvId(mvId);
	}
	/**
	 * 根据电影的id集合查询评分信息
	 */
	@Override
	public List<MvAvgMark> findMvAvgMarkByMvIds(List<Long> mvids){
		return mvCommentService.findMvAvgMarkByMvIds(mvids);
	}
	/**
	 * 删除一部电影的评论
	 */
	@Override
	public MvComment deleteMyCommentByMvId(long id) {
		return mvCommentService.deleteMvComment(id);
	}
	
	/**
	 * 删除电影的评分
	 */
	@Override
	public MvAvgMark deleteMvAvgMark(int mvId, float mvAvgMark,
			float beforeScore) {
		MvAvgMark avgMark = new MvAvgMark();
		long sysdate = System.currentTimeMillis();
		avgMark.setMvId(mvId);
		avgMark.setMvAvgMark(mvAvgMark);
		avgMark.setMvTotalNum(1);
		avgMark.setIsDelete(0);
		avgMark.setLatestRevisionDate(sysdate);
		return mvCommentService.deleteMvAvgMark(avgMark, beforeScore);
	}
	
	/**
	 * 根据type查询电影评论
	 */
	@Override
	public List<MvComment> findAllMvCommentListByType(Long userId, String type,
			Long resId) {
		return mvCommentService.findAllMvCommentListByType(userId, type, resId);
	}
	
	/**
	 * 增加影评的逼格值
	 */
	@Override
	public MvComment addMvCommentBigValue(long id, float bigValue) {
		return mvCommentService.updateMvCommentBigValue(id, bigValue);
	}
	
	/**
	 * 根据用户id查询电影评论
	 */
	@Override
	public List<MvComment> findAllMvCommentListByUsersId(List<Long> usersId,
			String type, Long resId) {
		return mvCommentService.findAllMvCommentListByUsersId(usersId, type, resId);
	}
	
	/**
	 * 根据uid查询电影评论
	 */
	@Override
	public Map<String, Object> findMvCommentCountByUid(long userId) {
		return mvCommentService.findMvCommentCountByUid(userId);
	}
	
	/**
	 * 增加专家评分
	 */
	@Override
	public MvAvgMark addExpertsAvgMark(long mvId, float expertsAvgMark) {
		MvAvgMark avgMark = new MvAvgMark();
		long sysdate = System.currentTimeMillis();
		avgMark.setId(reskeyWork.getId());
		avgMark.setMvId(mvId);
		avgMark.setExpertsAvgMark(expertsAvgMark);
		avgMark.setExpertsTotalNum(1);
		avgMark.setIsDelete(0);
		avgMark.setLatestRevisionDate(sysdate);
		return mvCommentService.addExpertsAvgMark(avgMark);
	}
	
	/**
	 * 更新专家评分
	 */
	@Override
	public MvAvgMark updateExpertsAvgMark(long mvId, float expertsAvgMark,
			float beforeScore) {
		MvAvgMark avgMark = new MvAvgMark();
		long sysdate = System.currentTimeMillis();
		avgMark.setMvId(mvId);
		avgMark.setExpertsAvgMark(expertsAvgMark);
		avgMark.setExpertsTotalNum(1);
		avgMark.setIsDelete(0);
		avgMark.setLatestRevisionDate(sysdate);
		return mvCommentService.updateExpertsAvgMark(avgMark,beforeScore);
	}
	
	/**
	 * 根据用户id查询长影评
	 */
	@Override
	public List<MvComment> findUserLongMvCommentListByUserId(long userId,
			Long resId) {
		return mvCommentService.findUserLongMvCommentListByUserId(userId, resId);
	}
	
	/**
	 * 删除专家评分
	 */
	@Override
	public MvAvgMark deleteExpertsAvgMark(int mvId, float expertsAvgMark,
			float beforeScore) {
		MvAvgMark avgMark = new MvAvgMark();
		long sysdate = System.currentTimeMillis();
		avgMark.setMvId(mvId);
		avgMark.setExpertsAvgMark(expertsAvgMark);
		avgMark.setExpertsTotalNum(1);
		avgMark.setIsDelete(0);
		avgMark.setLatestRevisionDate(sysdate);
		return mvCommentService.deleteExpertsAvgMark(avgMark, beforeScore);
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
		return mvCommentService.findMvCommentsByStageidOrderbyPoint(stageid, start, pagesize);
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
		return mvCommentService.findMvCommentsByStageidOrderbyId(stageid, start, pagesize);
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
		return mvCommentService.findMvCommentUserRankByStageid(stageid, start, pagesize);
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
		return mvCommentService.findMvCommentsByUseridAndStageid(userId, stageid, start, pagesize);
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
		return mvCommentService.findMvCommentsByMovieidAndStageid(movieId, stageid, resId, pagesize);
	}
	/**
	 * 根据影评id集合查询影评列表 
	 * @param stageid
	 * @return
	 */
	@Override
	public List<MvComment> findMvCommentsByIdsAndStageid(long stageid,List<Long> commentids){
		return mvCommentService.findMvCommentsByIdsAndStageid(stageid, commentids);
	}
	/**
	 * 根据影评id集合查询影评列表 
	 * @param stageid
	 * @return
	 */
	@Override
	public List<MvComment> findMvCommentsByIds(List<Long> commentids){
		return mvCommentService.findMvCommentsByIds(commentids);
	}
	/**
	 * 查询是否存在某个用户的影评记录
	 */
	@Override
	public long findMvCommentRecord(long userid){
		return mvCommentService.findMvCommentRecord(userid);
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
		return mvCommentService.findOneMvCommentListByResTypeAndPage(movieId, resourceType, pageIndex, pageSize);
	}


	public void setMvFacade(MvFacade mvFacade) {
		this.mvFacade = mvFacade;
	}
	public void setMyMovieFacade(MyMovieFacade myMovieFacade) {
		this.myMovieFacade = myMovieFacade;
	}
	
	
}
