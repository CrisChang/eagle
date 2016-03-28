package com.poison.ucenter.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.elasticsearch.common.mvel2.optimizers.impl.refl.nodes.ArrayAccessor;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.poison.eagle.utils.ResultUtils;
import com.poison.ucenter.dao.UserAlbumDAO;
import com.poison.ucenter.model.UserAlbum;

public class UserAlbumDAOImpl extends SqlMapClientDaoSupport implements UserAlbumDAO{

	private static final  Log LOG = LogFactory.getLog(UserAlbumDAOImpl.class);
	/**
	 * 插入用户的相册
	 */
	@Override
	public int insertintoUserAlbum(UserAlbum userAlbum) {
		int flag = ResultUtils.ERROR;
		try{
			getSqlMapClientTemplate().insert("insertintoUserAlbum",userAlbum);
			flag = ResultUtils.SUCCESS;
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			flag = ResultUtils.ERROR;
		}
		return flag;
	}

	/**
	 * 根据ID查询用户相册
	 */
	@SuppressWarnings("deprecation")
	@Override
	public UserAlbum findUserAlbumById(long id) {
		UserAlbum userAlbum = new UserAlbum();
		try{
			userAlbum = (UserAlbum) getSqlMapClientTemplate().queryForObject("findUserAlbumById",id);
			if(null == userAlbum){
				userAlbum = new UserAlbum();
				userAlbum.setFlag(ResultUtils.DATAISNULL);
				return userAlbum;
			}
			userAlbum.setFlag(ResultUtils.SUCCESS);
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			userAlbum = new UserAlbum();
			userAlbum.setFlag(ResultUtils.QUERY_ERROR);
		}
		return userAlbum;
	}

	/**
	 * 根据title查询用户相册
	 */
	@Override
	public UserAlbum findUserAlbumByTitle(UserAlbum userAlbum) {
		UserAlbum uAlbum = new UserAlbum();
		try{
			userAlbum = (UserAlbum) getSqlMapClientTemplate().queryForObject("findUserAlbumByTitle",userAlbum);
			if(null == userAlbum){
				userAlbum = new UserAlbum();
				userAlbum.setFlag(ResultUtils.DATAISNULL);
				return userAlbum;
			}
			userAlbum.setFlag(ResultUtils.SUCCESS);
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			userAlbum = new UserAlbum();
			userAlbum.setFlag(ResultUtils.QUERY_ERROR);
		}
		return userAlbum;
	}

	/**
	 * 更新用户相册
	 */
	@Override
	public int updateUserAlbum(UserAlbum userAlbum) {
		int flag = ResultUtils.UPDATE_ERROR;
		try{
			getSqlMapClientTemplate().update("updateAlbum",userAlbum);
			flag = ResultUtils.SUCCESS;
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			flag = ResultUtils.UPDATE_ERROR;
		}
		return flag;
	}

	/**
	 * 根据用户id查询用户相册
	 */
	@Override
	public List<UserAlbum> findUserAlbumByUid(long userId) {
		List<UserAlbum> list = new ArrayList<UserAlbum>();
		UserAlbum userAlbum = new UserAlbum();
		try{
			list = getSqlMapClientTemplate().queryForList("findUserAlbumByUid",userId);
			if(null==list||list.size()==0){
				list = new ArrayList<UserAlbum>();
			}
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			list = new ArrayList<UserAlbum>();
		}
		return list;
	}

}
