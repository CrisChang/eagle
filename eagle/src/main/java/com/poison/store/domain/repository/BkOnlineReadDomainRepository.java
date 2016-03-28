package com.poison.store.domain.repository;

import com.poison.eagle.utils.ResultUtils;
import com.poison.store.dao.BkOnlineReadDAO;
import com.poison.store.model.OnlineRead;

public class BkOnlineReadDomainRepository {

	private BkOnlineReadDAO bkOnlineReadDAO;

	public void setBkOnlineReadDAO(BkOnlineReadDAO bkOnlineReadDAO) {
		this.bkOnlineReadDAO = bkOnlineReadDAO;
	}
	
	/**
	 * 
	 * <p>Title: insertBkOnlineRead</p> 
	 * <p>Description: 插入试读</p> 
	 * @author :changjiang
	 * date 2014-8-27 下午4:35:16
	 * @param read
	 * @return
	 */
	public int insertBkOnlineRead(OnlineRead read){
		return bkOnlineReadDAO.insertBkOnlineRead(read);
	}
	
	/**
	 * 
	 * <p>Title: findOnlineReadByBkId</p> 
	 * <p>Description: 根据书的id查看试读</p> 
	 * @author :changjiang
	 * date 2014-8-27 下午5:02:31
	 * @param bkId
	 * @return
	 */
	public OnlineRead findOnlineReadByBkId(int bkId,String resType){
		return bkOnlineReadDAO.findOnlineReadByBkId(bkId,resType);
	}
	
	/**
	 * 
	 * <p>Title: updateBkOnLineRead</p> 
	 * <p>Description: 更新试读</p> 
	 * @author :changjiang
	 * date 2014-10-30 下午3:19:45
	 * @param read
	 * @return
	 */
	public OnlineRead updateBkOnLineRead(OnlineRead read){
		long bkId = read.getBkId();
		String resType = read.getResType();
		OnlineRead onlineRead = bkOnlineReadDAO.findOnlineReadByBkId((int)bkId,resType);
 		int flag = onlineRead.getFlag();
		//当有这本书时更新
		if(ResultUtils.SUCCESS==flag){
			flag = bkOnlineReadDAO.updateBkOnLineRead(read);
		}//当数据不存在时插入一条信息
		else if(ResultUtils.DATAISNULL==flag){
			flag = bkOnlineReadDAO.insertBkOnlineRead(read);
		}
		onlineRead = bkOnlineReadDAO.findOnlineReadByBkId((int)bkId,resType);
		onlineRead.setFlag(flag);
		return onlineRead;
	}
}
