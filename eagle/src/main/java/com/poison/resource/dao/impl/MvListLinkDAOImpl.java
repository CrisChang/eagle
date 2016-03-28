package com.poison.resource.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.poison.eagle.utils.ResultUtils;
import com.poison.resource.dao.MvListLinkDAO;
import com.poison.resource.model.MvListLink;

@SuppressWarnings("deprecation")
public class MvListLinkDAOImpl extends SqlMapClientDaoSupport implements MvListLinkDAO {

	private static final  Log LOG = LogFactory.getLog(MvListLinkDAOImpl.class);
	
	@Override
	public int addMovieListLink(MvListLink mvLink) {
		int flag = ResultUtils.ERROR;
		try {
			getSqlMapClientTemplate().insert("addMovieListLink",mvLink);
			flag = ResultUtils.SUCCESS;
		} catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			flag = ResultUtils.INSERT_ERROR;
		}
		return flag;
	}

	@Override
	public int updateMovieListLink(MvListLink mvLink) {
		try {
			getSqlMapClientTemplate().update("updateMovieListLink",mvLink);
			return ResultUtils.SUCCESS;
		} catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			return ResultUtils.UPDATE_ERROR;
		}
	}

	@Override
	public List<MvListLink> findMovieListInfo(long filmListId, Long resId,Integer pageSize) {
		List<MvListLink> mlist=new ArrayList<MvListLink>();
		MvListLink mk=new MvListLink();
		Map<Object,Object> map=new HashMap<Object, Object>();
		map.put("filmListId", filmListId);
		map.put("resId", resId);
		map.put("pageSize", pageSize);
		try {
			mlist=getSqlMapClientTemplate().queryForList("findMovieListInfo",map);
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(),e.fillInStackTrace());
			mlist=new ArrayList<MvListLink>();
			mk=new MvListLink();
			mk.setFlag(ResultUtils.QUERY_ERROR);
			mlist.add(mk);
		}
		return mlist;
	}

	@Override
	public MvListLink findMovieListById(long filmListId) {
		MvListLink mk=new MvListLink();
		try {
			mk=(MvListLink)getSqlMapClientTemplate().queryForObject("findMovieListById",filmListId);
			if(mk==null){
				mk=new MvListLink();
				mk.setFlag(ResultUtils.DATAISNULL);
				return mk;
			}else{
				mk=new MvListLink();
				mk.setFlag(ResultUtils.SUCCESS);
				return mk;
			}
		
		} catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			mk=new MvListLink();
			mk.setFlag(ResultUtils.QUERY_ERROR);
			return mk;
		}
	}

	@Override
	public MvListLink findMovieIsExist(MvListLink mvLink) {
		MvListLink mk=new MvListLink();
		try {
			mk=(MvListLink)getSqlMapClientTemplate().queryForObject("findMovieIsExist",mvLink);
			if(null==mk){
				mk=new MvListLink();
				mk.setFlag(ResultUtils.DATAISNULL);
				return mk;
			}else{
				mk.setFlag(ResultUtils.SUCCESS);
				return mk;
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			mk=new MvListLink();
			mk.setFlag(ResultUtils.QUERY_ERROR);
			return mk;
		}
	}

	/**
	 * 更新影单备注
	 */
	@Override
	public int updateMovieLinkRemark(String friendinfo, String address,
			String tags, long id,String description) {
		int flag = ResultUtils.ERROR;
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			long sysdate = System.currentTimeMillis();
			map.put("id", id);
			map.put("friendinfo", friendinfo);
			map.put("address", address);
			map.put("tags", tags);
			map.put("description", description);
			map.put("latestRevisionDate", sysdate);
			getSqlMapClientTemplate().update("updateMovieListLinkRemark",map);
			flag = ResultUtils.SUCCESS;
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			flag = ResultUtils.ERROR;
		}
		return flag;
	}

	/**
	 * 查询影单中的一本电影是否存在
	 */
	@Override
	public MvListLink findMovieLinkIsExist(long id) {
		MvListLink mvListLink = new MvListLink();
		try{
			mvListLink = (MvListLink) getSqlMapClientTemplate().queryForObject("findMovieLinkIsExistById",id);
			if(null==mvListLink){
				mvListLink = new MvListLink();
				mvListLink.setFlag(ResultUtils.DATAISNULL);
				return mvListLink;
			}
			mvListLink.setFlag(ResultUtils.SUCCESS);
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			mvListLink = new MvListLink();
			mvListLink.setFlag(ResultUtils.ERROR);
		}
		return mvListLink;
	}
	
	/**
	 * 查询一个影单的电影数量
	 */
	@Override
	public Map<String, Object> findMovieLinkCount(long movieListId) {
		int flag = ResultUtils.ERROR;
		int count = 0 ;
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			count = (Integer) getSqlMapClientTemplate().queryForObject("findMovieLinkCount",movieListId);
			flag = ResultUtils.SUCCESS;
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
		}
		map.put("flag", flag);
		map.put("count", count);
		return map;
	}

	/**
	 * 查询影单中的电影数量
	 */
	@Override
	public long getMovieCountByListId(long listid) {
		long i = 0;
		try{
			Long total = (Long) getSqlMapClientTemplate().queryForObject("getMovieCountByListId",listid);
			if(total!=null){
				i = total;
			}
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			i = -1;
		}
		return i;
	}
	
	/**
	 * 根据索引查询某个影单中的电影信息列表
	 */
	@Override
	public List<MvListLink> getMovieListLinkByStartIndex(long listid, long start,int pageSize) {
		List<MvListLink> mlist=new ArrayList<MvListLink>();
		MvListLink mk=new MvListLink();
		Map<Object,Object> map=new HashMap<Object, Object>();
		map.put("listid", listid);
		map.put("start", start);
		map.put("pageSize", pageSize);
		try {
			mlist=getSqlMapClientTemplate().queryForList("getMovieListLinkByStartIndex",map);
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(),e.fillInStackTrace());
			mlist=new ArrayList<MvListLink>();
			mk=new MvListLink();
			mk.setFlag(ResultUtils.QUERY_ERROR);
			mlist.add(mk);
		}
		return mlist;
	}
	
}
