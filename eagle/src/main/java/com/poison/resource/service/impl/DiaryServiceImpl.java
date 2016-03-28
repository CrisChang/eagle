package com.poison.resource.service.impl;

import java.util.List;
import java.util.Map;

import com.poison.resource.domain.repository.DiaryDomainRepository;
import com.poison.resource.model.Diary;
import com.poison.resource.service.DiaryService;

public class DiaryServiceImpl implements DiaryService{
	private DiaryDomainRepository diaryDomainRepository;
	
	public void setDiaryDomainRepository(DiaryDomainRepository diaryDomainRepository) {
		this.diaryDomainRepository = diaryDomainRepository;
	}
	@Override
	public int addDiary(Diary diary) {
		return diaryDomainRepository.addDiary(diary);
	}
	@Override
	public int updateDiary(Diary diary) {
		return diaryDomainRepository.updateDiary(diary);
	}
	@Override
	public Diary deleteDiaryById(long id) {
		return diaryDomainRepository.deleteDiaryById(id);
	}
	@Override
	public Diary queryByIdDiary(long id) {
		return diaryDomainRepository.queryByIdDiary(id);
	}
	@Override
	public Diary queryByIdDiaryWithoutDel(long id) {
		return diaryDomainRepository.queryByIdDiary(id);
	}
	@Override
	public List<Diary> queryDiaryByUid(Diary diary) {
		return diaryDomainRepository.queryDiaryByUid(diary);
	}
	@Override
	public List<Diary> queryType(long uid, String type) {
		return diaryDomainRepository.queryType(uid, type);
	}
	
	/**
	 * 根据ID查询日记详情
	 */
	@Override
	public List<Diary> findDiaryListById(long id) {
		return diaryDomainRepository.findDiaryListById(id);
	}
	
	/**
	 * 根据用户ID查询日记详情
	 */
	@Override
	public List<Diary> findDiaryListByUsersId(List<Long> userIdList, Long resId) {
		return diaryDomainRepository.findDiaryListByUsersId(userIdList, resId);
	}
	
	/**
	 * 查询所有的type
	 */
	@Override
	public List<Diary> queryAllType(String type) {
		return diaryDomainRepository.queryAllType(type);
	}
	
	/**
	 * 查询日记列表
	 */
	@Override
	public List<Diary> findDiaryList(Long id) {
		return diaryDomainRepository.findDiaryList(id);
	}
	
	/**
	 * 根据uid查询用户发布的日记总数
	 */
	@Override
	public Map<String, Object> findDiaryCount(long userId) {
		return diaryDomainRepository.findDiaryCount(userId);
	}
	
	/**
	 * 根据用户id查询日志列表
	 */
	@Override
	public List<Diary> findDiaryListByUserId(Long userId, Long resId) {
		return diaryDomainRepository.findDiaryListByUserId(userId, resId);
	}
	/**
	 * 根据用户id查询一个时间段的文字时间信息 
	 */
	@Override
	public List<Diary> findUserDiaryTime(Long userId, Long starttime,Long endtime){
		return diaryDomainRepository.findUserDiaryTime(userId, starttime, endtime);
	}
	/**
	 * 根据用户id查询一个时间段的文字信息列表
	 */
	@Override
	public List<Diary> findUserDiarysByTime(Long userId, Long starttime,Long endtime){
		return diaryDomainRepository.findUserDiarysByTime(userId, starttime, endtime);
	}
	/**
	 * 根据用户id查询一个时间段的文字信息列表
	 */
	@Override
	public List<Diary> searchDiaryByLike(Long userId,String keyword, Long starttime,Long endtime,long start,int pageSize){
		return diaryDomainRepository.searchDiaryByLike(userId, keyword, starttime, endtime, start, pageSize);
	}
	/**
	 * 根据模糊查询的条件查询数量
	 */
	@Override
	public Map<String, Object> findDiaryCountByLike(long userId,String keyword,Long starttime,Long endtime){
		return diaryDomainRepository.findDiaryCountByLike(userId, keyword, starttime, endtime);
	}
}
