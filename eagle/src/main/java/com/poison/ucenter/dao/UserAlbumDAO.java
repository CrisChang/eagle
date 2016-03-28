package com.poison.ucenter.dao;

import java.util.List;

import com.poison.ucenter.model.UserAlbum;

public interface UserAlbumDAO {

	/**
	 * 
	 * <p>Title: insertintoUserAlbum</p> 
	 * <p>Description: 插入用户</p> 
	 * @author :changjiang
	 * date 2014-10-17 上午11:52:40
	 * @param userAlbum
	 * @return
	 */
	public int insertintoUserAlbum(UserAlbum userAlbum);
	
	/**
	 * 
	 * <p>Title: findUserAlbumById</p> 
	 * <p>Description: 根据ID查询用户相册</p> 
	 * @author :changjiang
	 * date 2014-10-17 下午12:02:20
	 * @param id
	 * @return
	 */
	public UserAlbum findUserAlbumById(long id);
	
	/**
	 * 
	 * <p>Title: findUserAlbumByTitle</p> 
	 * <p>Description: 根据title查询用户的相册</p> 
	 * @author :changjiang
	 * date 2014-10-21 下午4:15:23
	 * @param userAlbum
	 * @return
	 */
	public UserAlbum findUserAlbumByTitle(UserAlbum userAlbum);
	
	/**
	 * 
	 * <p>Title: updateUserAlbum</p> 
	 * <p>Description: 修改个人的相册</p> 
	 * @author :changjiang
	 * date 2014-10-21 下午6:32:34
	 * @param userAlbum
	 * @return
	 */
	public int updateUserAlbum(UserAlbum userAlbum);
	
	/**
	 * 
	 * <p>Title: findUserAlbumByUid</p> 
	 * <p>Description: 根据用户id查询用户相册</p> 
	 * @author :changjiang
	 * date 2014-10-22 下午5:05:59
	 * @param userId
	 * @return
	 */
	public List<UserAlbum> findUserAlbumByUid(long userId);
	
}
