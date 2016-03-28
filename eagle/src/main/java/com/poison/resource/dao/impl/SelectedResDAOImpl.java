package com.poison.resource.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.poison.eagle.utils.ResultUtils;
import com.poison.resource.dao.SelectedResDAO;
import com.poison.resource.model.SelectedRes;

@SuppressWarnings("deprecation")
public class SelectedResDAOImpl extends SqlMapClientDaoSupport implements SelectedResDAO {

	private static final  Log LOG = LogFactory.getLog(SelectedResDAOImpl.class);
	
	/**
	 * 根据精选值排序 查询精选资源列表
	 * @param type
	 * @param score
	 * @param pageSize
	 * @return
	 */
	@Override
	public List<SelectedRes> findSelectedResByScore(String type,String restype,Long score,Integer pageSize){
		List<SelectedRes> selectedress=new ArrayList<SelectedRes>();
		SelectedRes selectedres=new SelectedRes();
		Map<Object,Object> map=new HashMap<Object, Object>();
		map.put("type", type);
		map.put("restype", restype);
		map.put("score", score);
		map.put("pageSize", pageSize);
		try {
			selectedress=getSqlMapClientTemplate().queryForList("findSelectedResByScore",map);
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(),e.fillInStackTrace());
			selectedress=new ArrayList<SelectedRes>();
			selectedres=new SelectedRes();
			selectedres.setFlag(ResultUtils.QUERY_ERROR);
			selectedress.add(selectedres);
		}
		return selectedress;
	}
	/**
	 * 根据精选值排序 查询精选列表 不包含置顶的
	 * @param type
	 * @param score
	 * @param pageSize
	 * @return
	 */
	@Override
	public List<SelectedRes> findSelectedResByScoreWithoutTopshow(String type,String restype,Long score,Integer pageSize){
		List<SelectedRes> selectedress=new ArrayList<SelectedRes>();
		SelectedRes selectedres=new SelectedRes();
		Map<Object,Object> map=new HashMap<Object, Object>();
		map.put("type", type);
		map.put("restype", restype);
		map.put("score", score);
		map.put("pageSize", pageSize);
		try {
			selectedress=getSqlMapClientTemplate().queryForList("findSelectedResByScoreWithoutTopshow",map);
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(),e.fillInStackTrace());
			selectedress=new ArrayList<SelectedRes>();
			selectedres=new SelectedRes();
			selectedres.setFlag(ResultUtils.QUERY_ERROR);
			selectedress.add(selectedres);
		}
		return selectedress;
	}
	
	/**
	 * 根据精选id排序 查询精选资源列表
	 * @param type
	 * @param id
	 * @param pageSize
	 * @return
	 */
	@Override
	public List<SelectedRes> findSelectedResById(String type,String restype,Long id,Integer pageSize){
		List<SelectedRes> selectedress=new ArrayList<SelectedRes>();
		SelectedRes selectedres=new SelectedRes();
		Map<Object,Object> map=new HashMap<Object, Object>();
		map.put("pageSize", pageSize);
		map.put("type", type);
		map.put("restype", restype);
		map.put("id", id);
		try {
			selectedress=getSqlMapClientTemplate().queryForList("findSelectedResById",map);
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(),e.fillInStackTrace());
			selectedress=new ArrayList<SelectedRes>();
			selectedres=new SelectedRes();
			selectedres.setFlag(ResultUtils.QUERY_ERROR);
			selectedress.add(selectedres);
		}
		return selectedress;
	}
	
	/**
	 * 根据精选id排序 查询精选列表 不包含置顶的
	 * @param type
	 * @param id
	 * @param pageSize
	 * @return
	 */
	@Override
	public List<SelectedRes> findSelectedResByIdWithoutTopshow(String type,String restype,Long id,Integer pageSize){
		List<SelectedRes> selectedress=null;
		SelectedRes selectedres=new SelectedRes();
		Map<Object,Object> map=new HashMap<Object, Object>();
		map.put("pageSize", pageSize);
		map.put("type", type);
		map.put("restype", restype);
		map.put("id", id);
		try {
			selectedress=getSqlMapClientTemplate().queryForList("findSelectedResByIdWithoutTopshow",map);
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(),e.fillInStackTrace());
			selectedress=new ArrayList<SelectedRes>(1);
			selectedres=new SelectedRes();
			selectedres.setFlag(ResultUtils.QUERY_ERROR);
			selectedress.add(selectedres);
		}
		return selectedress;
	}
	
	/**
	 * 根据置顶值排序 查询精选资源列表 (只是置顶的资源)
	 */
	@Override
	public List<SelectedRes> findSelectedResByTopshow(String type,String restype){
		List<SelectedRes> selectedress=new ArrayList<SelectedRes>();
		SelectedRes selectedres=new SelectedRes();
		Map<Object,Object> map=new HashMap<Object, Object>();
		map.put("type", type);
		map.put("restype", restype);
		try {
			selectedress=getSqlMapClientTemplate().queryForList("findSelectedResByTopshow",map);
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(),e.fillInStackTrace());
			selectedress=new ArrayList<SelectedRes>();
			selectedres=new SelectedRes();
			selectedres.setFlag(ResultUtils.QUERY_ERROR);
			selectedress.add(selectedres);
		}
		return selectedress;
	}
	
	/**
	 * 查询精选资源的数量根据资源类型
	 * 
	 */
	@Override
	public long getSelectedResCountByType(String type,String restype){
		long i = 0;
		Map<Object,Object> map=new HashMap<Object, Object>();
		map.put("type", type);
		map.put("restype", restype);
		try{
			Long total = (Long) getSqlMapClientTemplate().queryForObject("getSelectedResCountByType",map);
			if(total!=null){
				i = total;
			}
		}catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(),e.fillInStackTrace());
			i = -1;
		}
		return i;
	}
	
	/**
	 * 根据资源id和资源类型查询
	 */
	@Override
	public SelectedRes findSelectedResByResidAndType(long resid,String restype,String type){
		SelectedRes selectedres=null;
		Map<Object,Object> map=new HashMap<Object, Object>();
		map.put("resid", resid);
		map.put("type", type);
		map.put("restype", restype);
		try {
			selectedres=(SelectedRes) getSqlMapClientTemplate().queryForObject("findSelectedResByResidAndType",map);
			if(selectedres!=null){
				selectedres.setFlag(ResultUtils.SUCCESS);
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(),e.fillInStackTrace());
			selectedres=new SelectedRes();
			selectedres.setFlag(ResultUtils.QUERY_ERROR);
		}
		return selectedres;
	}
}
