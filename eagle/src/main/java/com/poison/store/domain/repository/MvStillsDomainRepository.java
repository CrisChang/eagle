package com.poison.store.domain.repository;

import com.poison.eagle.utils.ResultUtils;
import com.poison.store.dao.MvStillsDAO;
import com.poison.store.model.MovieStills;
import com.poison.store.model.OnlineRead;

public class MvStillsDomainRepository {

	private MvStillsDAO mvStillsDAO;

	public void setMvStillsDAO(MvStillsDAO mvStillsDAO) {
		this.mvStillsDAO = mvStillsDAO;
	}
	
	/**
	 * 
	 * <p>Title: insertintoMvOlineStills</p> 
	 * <p>Description: 插入影片剧照</p> 
	 * @author :changjiang
	 * date 2014-8-29 上午2:24:28
	 * @param movieStills
	 * @return
	 */
	public int insertintoMvOlineStills(MovieStills movieStills) {
		return mvStillsDAO.insertintoMvOlineStills(movieStills);
	}
	
	/**
	 * 
	 * <p>Title: findMvOlineStillsByBkId</p> 
	 * <p>Description: 根据电影id查询剧照</p> 
	 * @author :changjiang
	 * date 2014-8-29 上午2:26:33
	 * @param mvId
	 * @return
	 */
	public MovieStills findMvOlineStillsByBkId(long mvId){
		return mvStillsDAO.findMvOlineStillsByBkId(mvId);
	}
	
	/**
	 * 
	 * <p>Title: updateMvStills</p> 
	 * <p>Description: 更新剧照</p> 
	 * @author :changjiang
	 * date 2014-8-29 下午3:20:53
	 * @param movieStills
	 * @return
	 */
	public int updateMvStills(MovieStills movieStills){
		return mvStillsDAO.updateMvStills(movieStills);
	}
	
	/**
	 * 
	 * <p>Title: updateMvOther</p> 
	 * <p>Description: 更新片花</p> 
	 * @author :changjiang
	 * date 2014-8-29 下午3:21:21
	 * @param movieStills
	 * @return
	 */
	public int updateMvOther(MovieStills movieStills){
		return mvStillsDAO.updateMvOther(movieStills);
	}
	
	/**
	 * 
	 * <p>Title: updateMvTwoDimensionCode</p> 
	 * <p>Description: 更新电影的二维码</p> 
	 * @author :changjiang
	 * date 2014-10-30 下午5:26:23
	 * @param movieStills
	 * @return
	 */
	public MovieStills updateMvTwoDimensionCode(MovieStills movieStills){
		long mvId = movieStills.getMvId();
		MovieStills stills = mvStillsDAO.findMvOlineStillsByBkId(mvId);
 		int flag = stills.getFlag();
		//当有这部电影时更新
		if(ResultUtils.SUCCESS==flag){
			flag = mvStillsDAO.updateMvTwoDimensionCode(movieStills);
		}//当数据不存在时插入一条信息
		else if(ResultUtils.DATAISNULL==flag){
			flag = mvStillsDAO.insertintoMvOlineStills(movieStills);
		}
		stills = mvStillsDAO.findMvOlineStillsByBkId(mvId);
		stills.setFlag(flag);
		return stills;
	}
}
