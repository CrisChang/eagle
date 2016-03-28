package com.poison.store.domain.repository;

import java.util.ArrayList;
import java.util.List;

import com.poison.store.dao.MvInfoDAO;
import com.poison.store.ext.utils.MovieRelationUtil;
import com.poison.store.model.MvInfo;

/**
 * 
 * 类的作用:此类的作用是再一次的封装dao层中的数据
 * 作者:闫前刚
 * 创建时间:2014-8-7下午4:46:44
 * email :1486488968@qq.com
 * version: 1.0
 */
public class MvDomainRepository {

	private MvInfoDAO mvInfoDAO;
	
	public void setMvInfoDAO(MvInfoDAO mvInfoDAO) {
		this.mvInfoDAO = mvInfoDAO;
	}
	/**
	 * 
	 * 方法的描述 :查询电影信息
	 * @param id 根据id
	 * @return
	 */
	public MvInfo queryById(long id){
		MvInfo mvInfo = mvInfoDAO.queryById(id);
		String name = mvInfo.getName();
		String alias = mvInfo.getAlias();
		String boxOffice = mvInfo.getBoxOffice();
		//添加别名
		name = MovieRelationUtil.addAlias(name, alias);
		//添加票房
		boxOffice = MovieRelationUtil.addBoxOffice(boxOffice);
		mvInfo.setName(name);
		mvInfo.setBoxOffice(boxOffice);
		return mvInfo;
	}
	/**
	 * 
	 * 方法的描述 :查询电影信息
	 * @param movieUrl 
	 * @return
	 */
	public MvInfo queryByMvURL(String movieUrl) {
		MvInfo mvInfo = mvInfoDAO.queryByMvURL(movieUrl);
		String name = mvInfo.getName();
		String alias = mvInfo.getAlias();
		String boxOffice = mvInfo.getBoxOffice();
		//添加别名
		name = MovieRelationUtil.addAlias(name, alias);
		//添加票房
		boxOffice = MovieRelationUtil.addBoxOffice(boxOffice);
		mvInfo.setName(name);
		mvInfo.setBoxOffice(boxOffice);
		return mvInfo;
	}
	/**
	 * 
	 * 方法的描述 :此方法的作用是精准、模糊查询
	 * @param name
	 * @return
	 */
	public MvInfo findMvInfoByName(MvInfo info){
		MvInfo mvInfo = new MvInfo();
		List<MvInfo> list = mvInfoDAO.findMvInfoByName(info);
		mvInfo = list.get(0);
		return mvInfo;
	}
	
	/**
	 * 
	 * <p>Title: insertMvInfo</p> 
	 * <p>Description: 插入电影信息</p> 
	 * @author :changjiang
	 * date 2014-8-23 下午5:43:47
	 * @return
	 */
	public long insertMvInfo(MvInfo mvInfo){
		return mvInfoDAO.insertMvInfo(mvInfo);
	}
	
	/**
	 * 
	 * <p>Title: updateMvInfoDescribe</p> 
	 * <p>Description: </p> 
	 * @author :changjiang
	 * date 2015-3-5 上午12:26:06
	 * @param id
	 * @param describe
	 */
	public void updateMvInfoDescribe(long id, String describe){
		mvInfoDAO.updateMvInfoDescribe(id, describe);
	}
	
	public void updateMvInfoReleaseDateSort(long id, long releaseDateSort){
		mvInfoDAO.updateMvInfoReleaseDateSort(id, releaseDateSort);
	}
	
	/**
	 * 
	 * <p>Title: updateMvInfoActor</p> 
	 * <p>Description: 更新电影演员信息</p> 
	 * @author :changjiang
	 * date 2015-3-5 上午1:30:48
	 * @param id
	 * @param actor
	 */
	public void updateMvInfoActor(long id, String actor){
		mvInfoDAO.updateMvInfoActor(id, actor);
	}
	/**
	 * 根据id集合查询电影信息集合
	 */
	public List<MvInfo> findMvInfosByIds(long ids[]){
		List<MvInfo> mvInfos = mvInfoDAO.findMvInfosByIds(ids);
		if(mvInfos!=null && mvInfos.size()>0){
			for(int i=0;i<mvInfos.size();i++){
				MvInfo mvInfo = mvInfos.get(i);
				handleMvInfo(mvInfo);
			}
		}
		return mvInfos;
	}
	
	//处理电影名称、票房的公共方法
	private void handleMvInfo(MvInfo mvInfo){
		if(mvInfo!=null && mvInfo.getId()>0){
			try{
				String name = mvInfo.getName();
				String alias = mvInfo.getAlias();
				String boxOffice = mvInfo.getBoxOffice();
				//添加别名
				name = MovieRelationUtil.addAlias(name, alias);
				//添加票房
				boxOffice = MovieRelationUtil.addBoxOffice(boxOffice);
				mvInfo.setName(name);
				mvInfo.setBoxOffice(boxOffice);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
}
