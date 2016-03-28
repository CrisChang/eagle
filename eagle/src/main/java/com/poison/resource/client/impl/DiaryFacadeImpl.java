package com.poison.resource.client.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import redis.clients.jedis.Jedis;

import com.keel.common.cache.redis.JedisSimpleClient;
import com.keel.common.cache.redis.JedisWorker;
import com.keel.utils.UKeyWorker;
import com.poison.eagle.manager.SensitiveManager;
import com.poison.eagle.utils.CommentUtils;
import com.poison.eagle.utils.ResultUtils;
import com.poison.resource.client.DiaryFacade;
import com.poison.resource.ext.constant.ResStatisticConstant;
import com.poison.resource.ext.utils.ResRandomUtils;
import com.poison.resource.model.Diary;
import com.poison.resource.model.ResStatistic;
import com.poison.resource.service.DiaryService;
import com.poison.resource.service.ResStatisticService;

public class DiaryFacadeImpl implements DiaryFacade{
	private DiaryService diaryService; 
	//private Diary diary=new Diary();
	private UKeyWorker reskeyWork;
	private JedisSimpleClient resourceVisitClient;
	private JedisSimpleClient userTagClient;
	private ResStatisticService resStatisticService;
	private SensitiveManager sensitiveManager;
	
	
	public void setSensitiveManager(SensitiveManager sensitiveManager) {
		this.sensitiveManager = sensitiveManager;
	}
	public void setResStatisticService(ResStatisticService resStatisticService) {
		this.resStatisticService = resStatisticService;
	}
	public void setUserTagClient(JedisSimpleClient userTagClient) {
		this.userTagClient = userTagClient;
	}
	public void setResourceVisitClient(JedisSimpleClient resourceVisitClient) {
		this.resourceVisitClient = resourceVisitClient;
	}
	public void setReskeyWork(UKeyWorker reskeyWork) {
		this.reskeyWork = reskeyWork;
	}
	public void setDiaryService(DiaryService diaryService) {
		this.diaryService = diaryService;
	}
	/**
	 * 此方法的作用是添加日记
	 */
	@Override
	public Diary addDiary(String type, String content, long uid,String lon,String lat,String locationName,String locationCity,String locationArea,String title,String cover) {
		Diary diary=new Diary();
		long id = reskeyWork.getId();
		diary.setBeginDate(new Date().getTime());
		diary.setEndDate(new Date().getTime());
		//去除敏感词汇
		//content = sensitiveManager.checkSensitive(content, uid, id, CommentUtils.TYPE_DIARY);
		diary.setContent(content);
		diary.setType(type);
		diary.setUid(uid);
		diary.setId(id);
		diary.setIsDel(0);
		diary.setLon(lon);
		diary.setLat(lat);
		diary.setLocationName(locationName);
		diary.setLocationCity(locationCity);
		diary.setLocationArea(locationArea);
		diary.setTitle(title);
		diary.setCover(cover);
		diaryService.addDiary(diary);
		diary = diaryService.queryByIdDiary(id);
		return diary;
	}
	/**
	 * 此方法的作用是修改日记内容
	 */
	@Override
	public Diary updateDiary(long id, String content, long uid) {
		Diary diary=new Diary();
		diary.setEndDate(new Date().getTime());
		diary.setId(id);
		diary.setContent(content);
		diary.setUid(uid);
		diaryService.updateDiary(diary);
		diary = diaryService.queryByIdDiary(id);
		return diary;
	}
	/**
	 * 删除日记
	 */
	@Override
	public Diary deleteById(long id) {
		Diary diary=new Diary();
		diary.setId(id);
		return diaryService.deleteDiaryById(id);
	}
	/**
	 * 根据id查询日记
	 */
	@Override
	public Diary queryByIdDiary(final long id) {
		/*resourceVisitClient.execute(new JedisWorker<Object>(){
			@Override
			public Object work(Jedis jedis) {
				String key = ResStatisticConstant.DIARY_STATISTIC_MARK+id+ResStatisticConstant.DIARY_STATISTIC_TYPE;
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
					resStatistic.setType(CommentUtils.TYPE_DIARY);
					resStatistic.setFalseVisit(falseVisit);
					resStatistic.setVisitNumber(visitNumber);
					resStatistic.setLatestRevisionDate(sysdate);
					resStatisticService.insertResStatistic(resStatistic);
				}
				return jedis.hgetAll(key);
			}
		});*/
		return diaryService.queryByIdDiary(id);
	}
	
	public Diary queryByIdDiaryWithoutDel(final long id) {
		return diaryService.queryByIdDiaryWithoutDel(id);
	}
	
	/**
	 * 查询当前用户日记目录
	 */
	@Override
	public List<Diary> queryByUid(long uid) {
		Diary diary=new Diary();
		diary.setUid(uid);
		return diaryService.queryDiaryByUid(diary);
	}
	/**
	 * 查询当前用户类别
	 */
	@Override
	public List<Diary> queryType(long uid, String type) {
		return diaryService.queryType(uid, type);
	}
	

	/**
	 * 根据ID查询日记详情
	 */
	@Override
	public List<Diary> findDiaryListById(long id) {
		return diaryService.findDiaryListById(id);
	}
	
	/**
	 * 根据IDList查询日记想抢
	 */
	@Override
	public List<Diary> findDiaryListByUsersId(List<Long> userIdList, Long resId) {
		return diaryService.findDiaryListByUsersId(userIdList, resId);
	}
	
	/**
	 * 查询所有的type
	 */
	@Override
	public List<Diary> queryAllType(String type) {
		return diaryService.queryAllType(type);
	}
	
	/**
	 * 查询日记列表
	 */
	@Override
	public List<Diary> findDiaryList(Long id) {
		return diaryService.findDiaryList(id);
	}
	
	/**
	 * 查询日记总数
	 */
	@Override
	public Map<String, Object> findDiaryCount(long userId) {
		return diaryService.findDiaryCount(userId);
	}
	
	/**
	 * 根据用户id查询用户的日志列表
	 */
	@Override
	public List<Diary> findDiaryListByUserId(Long userId, Long resId) {
		return diaryService.findDiaryListByUserId(userId, resId);
	}
	/**
	 * 根据用户id查询一个时间段的文字时间信息 
	 */
	@Override
	public List<Diary> findUserDiaryTime(Long userId, Long starttime,Long endtime){
		return diaryService.findUserDiaryTime(userId, starttime, endtime);
	}
	/**
	 * 根据用户id查询一个时间段的文字信息列表
	 */
	@Override
	public List<Diary> findUserDiarysByTime(Long userId, Long starttime,Long endtime){
		return diaryService.findUserDiarysByTime(userId, starttime, endtime);
	}
	/**
	 * 根据用户id查询一个时间段的文字信息列表
	 */
	@Override
	public List<Diary> searchDiaryByLike(Long userId,String keyword, Long starttime,Long endtime,long start,int pageSize){
		return diaryService.searchDiaryByLike(userId, keyword, starttime, endtime, start, pageSize);
	}
	/**
	 * 根据模糊查询的条件查询数量
	 */
	@Override
	public Map<String, Object> findDiaryCountByLike(long userId,String keyword,Long starttime,Long endtime){
		return diaryService.findDiaryCountByLike(userId, keyword, starttime, endtime);
	}
}
