package com.poison.store.client.impl;

import java.util.List;
import java.util.concurrent.TimeoutException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.exception.MemcachedException;

import com.poison.eagle.utils.CheckParams;
import com.poison.eagle.utils.ResultUtils;
import com.poison.store.client.MvFacade;
import com.poison.store.ext.constant.MemcacheStoreConstant;
import com.poison.store.model.MvInfo;
import com.poison.store.service.MvService;

public class MvFacadeImpl implements MvFacade {
	
	private static final  Log LOG = LogFactory.getLog(MvFacadeImpl.class);
	
	private MvService mvService;
	//private MemcachedClient storeMemcachedClient;
	
	/*public void setStoreMemcachedClient(MemcachedClient storeMemcachedClient) {
		this.storeMemcachedClient = storeMemcachedClient;
	}*/

	public void setMvService(MvService mvService) {
		this.mvService = mvService;
	}

	@Override
	public MvInfo queryById(long id) {
		MvInfo mvInfo = new MvInfo();
		//try {
			//mvInfo = storeMemcachedClient.get(MemcacheStoreConstant.STORE_MOVIE_ID+id);
			if(null==mvInfo||mvInfo.getId()==0){
				mvInfo = mvService.queryById(id);
				//storeMemcachedClient.set(MemcacheStoreConstant.STORE_MOVIE_ID+id, MemcacheStoreConstant.TIME_INTERVALS, mvInfo);		
			}
		/*} catch (TimeoutException e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
		} catch (InterruptedException e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
		} catch (MemcachedException e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
		}*/
		return mvInfo;
	}
	/**
	 * 此方法的作用是精准、模糊查询
	 */
	@Override
	public MvInfo findMvInfoByName(String name,String releaseDate) {
		return mvService.findMvInfoByName(name,releaseDate);
	}

	/**
	 * 插入电影信息
	 */
	@Override
	public MvInfo insertMvInfo(MvInfo mvInfo) {
		String name = mvInfo.getName();
		String releaseDate = mvInfo.getReleaseDate();
		String mvUrl = mvInfo.getMovieUrl();
		Pattern releasePattern = Pattern.compile("[0-9]{4}");
		Matcher releaseMatcher = releasePattern.matcher(releaseDate);
		if(releaseMatcher.find()){
			releaseDate = releaseMatcher.group();
		}
		//查询库中是否已经有了这本书
		MvInfo info = new MvInfo();
		info = mvService.queryByMvURL(mvUrl);
		long id = info.getId();
		String describe = info.getDescription();
		String tempDescribe = mvInfo.getDescription();
		if(null==describe||"".equals(describe)){//当简介为空的时候
			if(null!=tempDescribe&&!"".equals(tempDescribe)){
				mvService.updateMvInfoDescribe(id, tempDescribe);
			}
		}
		String actor = info.getActor();
		String tempActor = mvInfo.getActor();
		if(null==actor||"".equals(actor)){//当演员为空的时候
			if(null!=tempActor&&!"".equals(tempActor)){
				mvService.updateMvInfoActor(id, tempActor);
			}
		}
				//findMvInfoByName(name,releaseDate);
		//如果没有这部电影则添加这本书
		if(ResultUtils.DATAISNULL==info.getFlag()){
			info = new MvInfo();
			updateReleaseDateSort(mvInfo);
			long flag = mvService.insertMvInfo(mvInfo);
			if(ResultUtils.ERROR == flag){
				info.setFlag(ResultUtils.ERROR);
				return info;
			}else if(ResultUtils.QUERY_ERROR==flag){
				info.setFlag(ResultUtils.QUERY_ERROR);
				return info;
			}
			info = mvService.queryById(flag);
		}
		return info;
	}

	private void updateReleaseDateSort(MvInfo mvInfo){
		try{
			Double releaseDateD = CheckParams.getRealeaseDate(mvInfo.getReleaseDate());
			long releaseDateSort = releaseDateD.longValue();
			mvInfo.setReleaseDateSort(releaseDateSort);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 根据movieUrl查询电影
	 */
	@Override
	public MvInfo queryByMvURL(String movieUrl) {
		return mvService.queryByMvURL(movieUrl);
	}

	/**
	 * 更新电影的简介
	 */
	@Override
	public void updateMvInfoDescribe(long id, String describe) {
		mvService.updateMvInfoDescribe(id, describe);
	}

	/**
	 * 更新电影的简介
	 */
	@Override
	public void updateMvInfoReleaseDateSort(long id, long releaseDateSort) {
		mvService.updateMvInfoReleaseDateSort(id, releaseDateSort);
	}

	/**
	 * 更新电影演员信息
	 */
	@Override
	public void updateMvInfoActor(long id, String actor) {
		mvService.updateMvInfoActor(id, actor);
	}

	@Override
	public List<MvInfo> findMvInfosByIds(long[] ids) {
		return mvService.findMvInfosByIds(ids);
	}
}
