package com.poison.resource.domain.repository;

import java.util.List;

import com.poison.eagle.utils.ResultUtils;
import com.poison.resource.dao.SerializeListDAO;
import com.poison.resource.dao.SerializeListLinkDAO;
import com.poison.resource.model.SerializeList;
import com.poison.resource.model.SerializeListLink;

public class SerializeListDomainRepository {

	private SerializeListDAO serializeListDAO;
	private SerializeListLinkDAO serializeListLinkDAO;

	public void setSerializeListLinkDAO(SerializeListLinkDAO serializeListLinkDAO) {
		this.serializeListLinkDAO = serializeListLinkDAO;
	}

	public void setSerializeListDAO(SerializeListDAO serializeListDAO) {
		this.serializeListDAO = serializeListDAO;
	}
	
	/**
	 * 
	 * <p>Title: addSerializeList</p> 
	 * <p>Description: 插入连载清单</p> 
	 * @author :changjiang
	 * date 2014-9-13 下午3:41:21
	 * @param SerializeList
	 * @return
	 */
	public SerializeList addSerializeList(SerializeList serializeList){
		int flag = serializeListDAO.addSerializeList(serializeList);
		SerializeList list = new SerializeList();
		if(ResultUtils.SUCCESS == flag){
			long id = serializeList.getId();
			list = serializeListDAO.findSerializeListById(id);
			return list;
		}
		list.setFlag(flag);
		return list;
	}
	
	/**
	 * 
	 * <p>Title: updateSerializeList</p> 
	 * <p>Description: 修改连载清单</p> 
	 * @author :changjiang
	 * date 2014-9-19 下午2:55:30
	 * @param serializeList
	 * @return
	 */
	public SerializeList updateSerializeList(long id){
		SerializeList list = serializeListDAO.findSerializeListById(id);
		int flag = list.getFlag();
		if(ResultUtils.SUCCESS==flag){
			list.setIsPublishing(1);
			flag = serializeListDAO.updateSerializeList(list);
			list = serializeListDAO.findSerializeListById(id);
			return list;
		}
		return list;
	}
	
	/**
	 * 
	 * <p>Title: addSerializeListLink</p> 
	 * <p>Description: 增加一条Link信息</p> 
	 * @author :changjiang
	 * date 2014-9-14 上午11:31:13
	 * @param serializeList
	 * @return
	 */
	public SerializeListLink addSerializeListLink(SerializeListLink serializeListLink){
		SerializeListLink list = new SerializeListLink();
		
		int flag = serializeListLinkDAO.insertSerializeListLink(serializeListLink);
		if(ResultUtils.SUCCESS == flag){
			long id = serializeListLink.getId();
			list = serializeListLinkDAO.findSerializeListLinkById(id);
			return list;
		}
		list.setFlag(flag);
		return list;
	}

	/**
	 * 更新最后的看过时间
	 * @param serializeListLink
	 * @return
	 */
	public SerializeListLink updateLastViewSerlizeLinkEndDate(SerializeListLink serializeListLink){
		long userId = serializeListLink.getUserId();
		long chapterId = serializeListLink.getChapterId();
		long serializeId = serializeListLink.getSerializeId();
		SerializeListLink list = serializeListLinkDAO.findSerializeListLinkByUidAndSerializeId(userId,serializeId);
				//findSerializeListLinkByUidAndChapterId(userId,chapterId);
		int flag = list.getFlag();
		if(ResultUtils.DATAISNULL==flag){
			//加入一条新的记录
			flag = serializeListLinkDAO.insertSerializeListLink(serializeListLink);
		}else if(ResultUtils.SUCCESS == flag){
			//如果有这条记录的话更新最后时间
			long id = list.getId();
			serializeListLinkDAO.updateLastViewSerlizeLinkEndDate(id);
			flag = ResultUtils.IS_ALREADY_NOVELLIST;
		}
		list = serializeListLinkDAO.findSerializeListLinkByUidAndSerializeId(userId, serializeId);
		list.setFlag(flag);
		return list;
	}
	
	/**
	 * 
	 * <p>Title: findSerializeListLinkByListId</p> 
	 * <p>Description: 根据连载清单id查询连载关系表</p> 
	 * @author :changjiang
	 * date 2014-9-19 上午11:26:37
	 * @param id
	 * @return
	 */
	public List<SerializeListLink> findSerializeListLinkByListId(long id){
		return serializeListLinkDAO.findSerializeListLinkByListId(id);
	}

	/**
	 * 查询看过的小说列表
	 * @param userId
	 * @param endDate
	 * @return
	 */
	public List<SerializeListLink> findViewedSerializeLinkList(long userId, Long endDate){
		return serializeListLinkDAO.findViewedSerializeLinkList(userId,endDate);
	}

	/**
	 *
	 * @param userId
	 * @param serializeId
	 * @return
	 */
	public SerializeListLink findSerializeListLinkByUidAndSerializeId(long userId, long serializeId) {
		return serializeListLinkDAO.findSerializeListLinkByUidAndSerializeId(userId,serializeId);
	}

	/**
	 * 删除已经看过的小说
	 * @param userId
	 * @param serializeId
	 * @return
	 */
	public int delSerializeListLink(long userId,long serializeId){
		return  serializeListLinkDAO.delSerializeListLinkByUidAndSerializeId(userId, serializeId);
	}
}
