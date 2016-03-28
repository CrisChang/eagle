package com.poison.resource.client;

import java.util.List;
import java.util.Map;

import com.poison.resource.model.Diary;

/**
 * 
 * 类的作用:此类的作用是处理日记相关的信息
 * 作者:闫前刚
 * 创建时间:2014-7-31上午10:52:11
 * email :1486488968@qq.com
 * version: 1.0
 */
public interface DiaryFacade {
	/**
	 * 
	 * 方法的描述 :此方法的作用是添加日记
	 * @param type 添加类型
	 * @param content 内容
	 * @param uid 用户名
	 * @return
	 */
	public Diary addDiary(String type,String content,long uid,String lon,String lat,String locationName,String locationCity,String locationArea,String title,String cover);
	/**
	 * 
	 * 方法的描述 :此方法的作用是修改日记内容
	 * @param id 根据主键id
	 * @param content 内容
	 * @param uid 用户名
	 * @return
	 */
	public Diary updateDiary(long id,String content,long uid);
	/**
	 * 
	 * 方法的描述 :此方法的作用删除当前用户日记内容
	 * @param id根据主键id
	 * @return
	 */
	public Diary deleteById(long id);
	/**
	 * 
	 * 方法的描述 :此方法的作用是查询日记内容
	 * @param id 根据id
	 * @return
	 */
	public Diary queryByIdDiary(long id);
	/**
	 * 
	 * 方法的描述 :此方法的作用是查询日记内容 不包含删除了的
	 * @param id 根据id
	 * @return
	 */
	public Diary queryByIdDiaryWithoutDel(long id);
	/**
	 * 
	 * 方法的描述 :此方法的作用是根据用户名查询当前的目录
	 * @param uid 用户名
	 * @return
	 */
	public List<Diary> queryByUid(long uid);
	
	/**
	 * 
	 * 方法的描述 :此方法的作用是查询当前用户和类别的目录
	 * @param map
	 * @param uid 用户名
	 * @param type 类别
	 * @return
	 */
	public List<Diary> queryType(long uid, String type);
	

	/**
	 * 
	 * <p>Title: findDiaryListById</p> 
	 * <p>Description: 根据ID查询日记详情</p> 
	 * @author :changjiang
	 * date 2014-8-2 上午1:30:08
	 * @param id
	 * @return
	 */
	public List<Diary> findDiaryListById(long id);
	
	/**
	 * 
	 * <p>Title: findDiaryListByUsersId</p> 
	 * <p>Description: 根据ID查询日记详情</p> 
	 * @author :changjiang
	 * date 2014-8-2 下午4:02:09
	 * @param userIdList
	 * @param resId
	 * @return
	 */
	public List<Diary> findDiaryListByUsersId(List<Long> userIdList,Long resId);
	
	/**
	 * 
	 * <p>Title: queryAllType</p> 
	 * <p>Description: 查询所有的type类型</p> 
	 * @author :changjiang
	 * date 2014-10-13 下午9:40:16
	 * @param type
	 * @return
	 */
	public List<Diary> queryAllType(String type);
	
	/**
	 * 
	 * <p>Title: findDiaryList</p> 
	 * <p>Description: 查询日记列表</p> 
	 * @author :changjiang
	 * date 2014-10-27 上午10:39:56
	 * @param id
	 * @return
	 */
	public List<Diary> findDiaryList(Long id);
	
	/**
	 * 
	 * <p>Title: findDiaryCount</p> 
	 * <p>Description: 根据uid查询日记总数</p> 
	 * @author :changjiang
	 * date 2014-12-9 下午7:08:42
	 * @param userId
	 * @return
	 */
	public Map<String, Object> findDiaryCount(long userId);
	
	/**
	 * 
	 * <p>Title: findDiaryListByUserId</p> 
	 * <p>Description: 根据用户id查询用户日志列表</p> 
	 * @author :changjiang
	 * date 2015-6-5 下午3:32:27
	 * @param userId
	 * @param resId
	 * @return
	 */
	public List<Diary> findDiaryListByUserId(Long userId, Long resId);
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
