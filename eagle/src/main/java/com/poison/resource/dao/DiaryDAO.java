package com.poison.resource.dao;

import java.util.List;
import java.util.Map;

import com.poison.resource.model.Diary;

/**
 * 
 * 类的作用:此类的作用是持久化操作
 * 作者:闫前刚
 * 创建时间:2014-7-30下午12:20:34
 * email :1486488968@qq.com
 * version: 1.0
 */
public interface DiaryDAO {
	/**
	 * 
	 * 方法的描述 :此方法的作用写日记
	 * @param diary
	 * @return
	 */
	public int addDiary(Diary diary);
	/**
	 * 
	 * 方法的描述 :此方法的作用是删除日记
	 * @param id 根据id删除
	 * @return
	 */
	public int deleteDiary(long id);
	/**
	 * 
	 * 方法的描述 :此方法的作用是查询当前日记
	 * @param diary
	 * @return
	 */
	public List<Diary> queryDiaryByUid(Diary diary);
	/**
	 * 
	 * 方法的描述 :此方法的作用是修改当前日记
	 * @param diary
	 * @return
	 */
	public int updateDiary(Diary diary);
	/**
	 * 
	 * 方法的描述 :此方法的作用是查询日记内容
	 * @param id根据主键id
	 * @return
	 */
	public Diary queryByIdDiary(long id);
	/**
	 * 
	 * 方法的描述 :此方法的作用是查询日记内容
	 * @param id根据主键id
	 * @return
	 */
	public Diary queryByIdDiaryWithoutDel(long id);
	/**
	 * 
	 * 方法的描述 :此方法的作用查询当前日记目录
	 * @param map
	 * @return
	 */
	public List<Diary> queryType(long uid,String type);
	
	/**
	 * 
	 * <p>Title: queryAllType</p> 
	 * <p>Description: 查询所有的TYPE类型</p> 
	 * @author :changjiang
	 * date 2014-10-13 下午8:42:11
	 * @param type
	 * @return
	 */
	public List<Diary> queryAllType(String type);
	
	/**
	 * 
	 * <p>Title: findDiaryListById</p> 
	 * <p>Description: 根据ID查询日记信息</p> 
	 * @author :changjiang
	 * date 2014-8-2 上午1:37:44
	 * @param id
	 * @return
	 */
	public List<Diary> findDiaryListById(long id);
	
	/**
	 * 
	 * <p>Title: findDiaryList</p> 
	 * <p>Description: 查询日记列表</p> 
	 * @author :changjiang
	 * date 2014-10-27 上午10:28:49
	 * @param id
	 * @return
	 */
	public List<Diary> findDiaryList(Long id);
	
	/**
	 * 
	 * <p>Title: findDiaryListByUsersId</p> 
	 * <p>Description: 根据用户ID查询日记详情</p> 
	 * @author :changjiang
	 * date 2014-8-2 下午3:52:59
	 * @param userIdList
	 * @param resId
	 * @return
	 */
	public List<Diary> findDiaryListByUsersId(List<Long> userIdList,Long resId);
	
	/**
	 * 
	 * <p>Title: findDiaryListByUserId</p> 
	 * <p>Description: 根据用户id查询日志详情</p> 
	 * @author :changjiang
	 * date 2015-6-5 下午3:25:10
	 * @param userId
	 * @param resId
	 * @return
	 */
	public List<Diary> findDiaryListByUserId(Long userId,Long resId);
	
	/**
	 * 
	 * <p>Title: findDiaryCount</p> 
	 * <p>Description: 查询用户发布日记的总数</p> 
	 * @author :changjiang
	 * date 2014-12-9 下午7:00:17
	 * @param userId
	 * @return
	 */
	public Map<String, Object> findDiaryCount(long userId);
	/**
	 * 根据用户id查询一个时间段的文字时间信息 
	 */
	public List<Diary> findUserDiaryTime(Long userId, Long starttime,Long endtime);
	/**
	 * 根据用户id查询一个时间段的文字信息列表
	 */
	public List<Diary> findUserDiarysByTime(Long userId, Long starttime,Long endtime);
	/**
	 * 根据用户id查询一个时间段的文字信息列表
	 */
	public List<Diary> searchDiaryByLike(Long userId,String keyword, Long starttime,Long endtime,long start,int pageSize);
	/**
	 * 根据模糊查询的条件查询数量
	 */
	public Map<String, Object> findDiaryCountByLike(long userId,String keyword,Long starttime,Long endtime);
}
