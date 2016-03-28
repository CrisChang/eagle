package com.poison.resource.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.poison.eagle.utils.ResultUtils;
import com.poison.resource.dao.SerializeListLinkDAO;
import com.poison.resource.model.SerializeListLink;

public class SerializeListLinkDAOImpl extends SqlMapClientDaoSupport implements SerializeListLinkDAO{

	private static final  Log LOG = LogFactory.getLog(SerializeListLinkDAOImpl.class);
	/**
	 * 插入连载清单关系
	 */
	@Override
	public int insertSerializeListLink(SerializeListLink serializeListLink) {
		int flag = ResultUtils.INSERT_ERROR;
		try{
			getSqlMapClientTemplate().insert("insertserializelistlink",serializeListLink);
			flag = ResultUtils.SUCCESS;
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			flag = ResultUtils.INSERT_ERROR;
		}
		return flag;
	}

	/**
	 * 根据id查询连载关系
	 */
	@Override
	public SerializeListLink findSerializeListLinkById(
			long id) {
		SerializeListLink listLink = new SerializeListLink();
		try{
			listLink = (SerializeListLink) getSqlMapClientTemplate().queryForObject("findSerializeListLinkById",id);
			if(null==listLink){
				listLink = new SerializeListLink();
				listLink.setFlag(ResultUtils.DATAISNULL);
				return listLink;
			}
			listLink.setFlag(ResultUtils.SUCCESS);
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
		}
		return listLink;
	}

	/**
	 * 根据连载清单id查询连载关系清单
	 */
	@Override
	public List<SerializeListLink> findSerializeListLinkByListId(long id) {
		List<SerializeListLink> linkList = new ArrayList<SerializeListLink>();
		try{
			linkList = getSqlMapClientTemplate().queryForList("findSerializeListLinkByListId",id);
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			linkList = new ArrayList<SerializeListLink>();
			SerializeListLink serializeListLink = new SerializeListLink();
			serializeListLink.setFlag(ResultUtils.QUERY_ERROR);
			linkList.add(serializeListLink);
		}
		return linkList;
	}


	/**
	 * 更新最后修改时间
	 * @param id
	 * @return
	 */
	@Override
	public int updateLastViewSerlizeLinkEndDate(long id) {
		int flag = ResultUtils.ERROR;
		Map<String,Object> map = new HashMap<String, Object>();
		try{
			map.put("id",id);
			map.put("latestRevisionDate",System.currentTimeMillis());
			getSqlMapClientTemplate().update("updateLastViewSerializeLinkEndDate",map);
			flag = ResultUtils.SUCCESS;
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
		}

		return flag;
	}


	/**
	 * 查询
	 * @param userId
	 * @param chpaterId
	 * @return
	 */
	@Override
	public SerializeListLink findSerializeListLinkByUidAndChapterId(long userId, long chpaterId) {
		SerializeListLink serializeListLink = new SerializeListLink();
		Map<String,Object> map = new HashMap<String, Object>();
		try{
			map.put("userId",userId);
			map.put("chpaterId",chpaterId);
			serializeListLink = (SerializeListLink)getSqlMapClientTemplate().queryForObject("findSerializeListLinkByUidAndChapterId",map);
			if(null == serializeListLink){
				serializeListLink = new SerializeListLink();
				serializeListLink.setFlag(ResultUtils.DATAISNULL);
				return serializeListLink;
			}
			serializeListLink.setFlag(ResultUtils.SUCCESS);
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			serializeListLink = new SerializeListLink();
			serializeListLink.setFlag(ResultUtils.ERROR);
		}
		return serializeListLink;
	}

	/**
	 * 根据用户id和小说id查询
	 * @param userId
	 * @param serializeId
	 * @return
	 */
	@Override
	public SerializeListLink findSerializeListLinkByUidAndSerializeId(long userId, long serializeId) {
		SerializeListLink serializeListLink = new SerializeListLink();
		Map<String,Object> map = new HashMap<String, Object>();
		try{
			map.put("userId",userId);
			map.put("serializeId",serializeId);
			serializeListLink = (SerializeListLink)getSqlMapClientTemplate().queryForObject("findSerializeListLinkByUidAndSerializeId",map);
			if(null == serializeListLink){
				serializeListLink = new SerializeListLink();
				serializeListLink.setFlag(ResultUtils.DATAISNULL);
				return serializeListLink;
			}
			serializeListLink.setFlag(ResultUtils.SUCCESS);
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			serializeListLink = new SerializeListLink();
			serializeListLink.setFlag(ResultUtils.ERROR);
		}
		return serializeListLink;
	}

	/**
	 * 查询看过的小说列表
	 * @param userId
	 * @param endDate
	 * @return
	 */
	@Override
	public List<SerializeListLink> findViewedSerializeLinkList(long userId, Long endDate) {
		List<SerializeListLink> list = new ArrayList<SerializeListLink>();
		Map<String,Object> map = new HashMap<String, Object>();
		try{
			map.put("userId",userId);
			map.put("endDate",endDate);
			list = getSqlMapClientTemplate().queryForList("findViewedSerializeLinkList",map);
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			list = new ArrayList<SerializeListLink>();
		}

		return list;
	}

	/**
	 * 删除看过的小说
	 * @param userId
	 * @param serializeId
	 * @return
	 */
	@Override
	public int delSerializeListLinkByUidAndSerializeId(long userId, long serializeId) {

		int flag = ResultUtils.ERROR;
		Map<String,Object> map = new HashMap<String, Object>();
		try{
			map.put("userId",userId);
			map.put("serializeId",serializeId);
			map.put("endDate",System.currentTimeMillis());
			getSqlMapClientTemplate().update("delSerializeListLinkByUidAndSerializeId", map);
			flag = ResultUtils.SUCCESS;
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			e.printStackTrace();
			flag = ResultUtils.ERROR;
		}

		return flag;
	}
}
