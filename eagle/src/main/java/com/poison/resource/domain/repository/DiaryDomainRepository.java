package com.poison.resource.domain.repository;

import java.util.List;
import java.util.Map;

import com.poison.eagle.utils.ResultUtils;
import com.poison.resource.dao.DiaryDAO;
import com.poison.resource.model.Diary;

/**
 * 
 * 类的作用:此类的作用是封装dao层中的方法
 * 作者:闫前刚
 * 创建时间:2014-7-31上午10:45:47
 * email :1486488968@qq.com
 * version: 1.0
 */
public class DiaryDomainRepository {
	private DiaryDAO diaryDAO;
	
	public void setDiaryDAO(DiaryDAO diaryDAO) {
		this.diaryDAO = diaryDAO;
	}
	/**
	 * 
	 * 方法的描述 :此方法的作用是添加日记
	 * @param diary
	 * @return
	 */
	public int addDiary(Diary diary){
		return diaryDAO.addDiary(diary);
	}
	/**
	 * 
	 * 方法的描述 :此方法的作用是修改日记内容
	 * @param diary
	 * @return
	 */
	public int updateDiary(Diary diary){
		return diaryDAO.updateDiary(diary);
	}
	/**
	 * 
	 * 方法的描述 :此方法的用是删除当前用户日记
	 * @param id 根据主键
	 * @return
	 */
	public Diary deleteDiaryById(long id){
		Diary diary = new Diary();
		diary = diaryDAO.queryByIdDiary(id);
		int flag = ResultUtils.ERROR;
		flag = diary.getFlag();
		if(ResultUtils.DATAISNULL==flag){
			return diary;
		}
		flag = diaryDAO.deleteDiary(id);
		diary.setFlag(flag);
		return diary;
	}
	/**
	 * 
	 * 方法的描述 :此方法的作用是查询日记内容
	 * @param id 根据id
	 * @return
	 */
	public Diary queryByIdDiary(long id){
		return diaryDAO.queryByIdDiary(id);
	}
	/**
	 * 
	 * 方法的描述 :此方法的作用是查询日记内容
	 * @param id 根据id
	 * @return
	 */
	public Diary queryByIdDiaryWithoutDel(long id){
		return diaryDAO.queryByIdDiaryWithoutDel(id);
	}
	/**
	 * 
	 * 方法的描述 :此方法的作用是查询当前用户的日记内容
	 * @param diary
	 * @return
	 */
	public List<Diary> queryDiaryByUid(Diary diary){
		return diaryDAO.queryDiaryByUid(diary);
	}
	/**
	 * 
	 * 方法的描述 :此方法的作用是查询类别是否属于当前用户
	 * @param map 存放 uid 存放type
	 * @param uid
	 * @param type
	 * @return
	 */
	public List<Diary> queryType(long uid,String type){
		return diaryDAO.queryType(uid,type);
	}
	
	/**
	 * 
	 * <p>Title: findDiaryListById</p> 
	 * <p>Description: 根据ID查询日记列表</p> 
	 * @author :changjiang
	 * date 2014-8-2 上午1:45:27
	 * @param id
	 * @return
	 */
	public List<Diary> findDiaryListById(long id){
		return diaryDAO.findDiaryListById(id);
	}
	
	/**
	 * 
	 * <p>Title: findDiaryListByUsersId</p> 
	 * <p>Description: 根据ID查询日记详情</p> 
	 * @author :changjiang
	 * date 2014-8-2 下午3:57:31
	 * @return
	 */
	public List<Diary> findDiaryListByUsersId(List<Long> userIdList,Long resId){
		return diaryDAO.findDiaryListByUsersId(userIdList, resId);
	}
	
	/**
	 * 
	 * <p>Title: queryAllType</p> 
	 * <p>Description: 查询所有的type类型</p> 
	 * @author :changjiang
	 * date 2014-10-13 下午9:36:19
	 * @param type
	 * @return
	 */
	public List<Diary> queryAllType(String type){
		return diaryDAO.queryAllType(type);
	}
	
	/**
	 * 
	 * <p>Title: findDiaryList</p> 
	 * <p>Description: 查询日记列表</p> 
	 * @author :changjiang
	 * date 2014-10-27 上午10:36:52
	 * @param id
	 * @return
	 */
	public List<Diary> findDiaryList(Long id){
		return diaryDAO.findDiaryList(id);
	}
	
	/**
	 * 
	 * <p>Title: findDiaryCount</p> 
	 * <p>Description: 根据uid查询用户对日志的总数</p> 
	 * @author :changjiang
	 * date 2014-12-9 下午7:06:20
	 * @param userId
	 * @return
	 */
	public Map<String, Object> findDiaryCount(long userId){
		return diaryDAO.findDiaryCount(userId);
	}
	
	/**
	 * 
	 * <p>Title: findDiaryListByUserId</p> 
	 * <p>Description: 根据用户id查询日志详情</p> 
	 * @author :changjiang
	 * date 2015-6-5 下午3:29:18
	 * @param userId
	 * @param resId
	 * @return
	 */
	public List<Diary> findDiaryListByUserId(Long userId, Long resId){
		return diaryDAO.findDiaryListByUserId(userId, resId);
	}
	/**
	 * 根据用户id查询一个时间段的文字时间信息 
	 */
	public List<Diary> findUserDiaryTime(Long userId, Long starttime,Long endtime){
		return diaryDAO.findUserDiaryTime(userId, starttime, endtime);
	}
	/**
	 * 根据用户id查询一个时间段的文字信息列表
	 */
	public List<Diary> findUserDiarysByTime(Long userId, Long starttime,Long endtime){
		return diaryDAO.findUserDiarysByTime(userId, starttime, endtime);
	}
	/**
	 * 根据用户id查询一个时间段的文字信息列表
	 */
	public List<Diary> searchDiaryByLike(Long userId,String keyword, Long starttime,Long endtime,long start,int pageSize){
		return diaryDAO.searchDiaryByLike(userId, keyword, starttime, endtime, start, pageSize);
	}
	/**
	 * 根据模糊查询的条件查询数量
	 */
	public Map<String, Object> findDiaryCountByLike(long userId,String keyword,Long starttime,Long endtime){
		return diaryDAO.findDiaryCountByLike(userId, keyword, starttime, endtime);
	}
}
