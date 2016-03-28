package com.poison.resource.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.poison.eagle.utils.ResultUtils;
import com.poison.resource.dao.SelectedDAO;
import com.poison.resource.model.Selected;

@SuppressWarnings("deprecation")
public class SelectedDAOImpl extends SqlMapClientDaoSupport implements SelectedDAO {

	private static final  Log LOG = LogFactory.getLog(SelectedDAOImpl.class);
	
	@Override
	public List<Selected> findSelectedByScore(Long score,Integer pageSize){
		List<Selected> selecteds=new ArrayList<Selected>();
		Selected selected=new Selected();
		Map<Object,Object> map=new HashMap<Object, Object>();
		map.put("score", score);
		map.put("pageSize", pageSize);
		try {
			selecteds=getSqlMapClientTemplate().queryForList("findSelectedByScore",map);
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(),e.fillInStackTrace());
			selecteds=new ArrayList<Selected>();
			selected=new Selected();
			selected.setFlag(ResultUtils.QUERY_ERROR);
			selecteds.add(selected);
		}
		return selecteds;
	}
	/**
	 * 根据精选值排序 查询精选列表 (不包含用户和置顶的资源)
	 * @Title: findSelectedByScoreWithoutTopshow 
	 * @Description: TODO
	 * @author Administrator
	 * @date 2015-7-8 下午3:41:07
	 * @param @param score
	 * @param @param pageSize
	 * @param @return
	 * @return List<Selected>
	 * @throws
	 */
	@Override
	public List<Selected> findSelectedByScoreWithoutTopshow(Long score,Integer pageSize,Long starttime,Long endtime){
		List<Selected> selecteds=new ArrayList<Selected>();
		Selected selected=new Selected();
		Map<Object,Object> map=new HashMap<Object, Object>();
		map.put("score", score);
		map.put("pageSize", pageSize);
		map.put("starttime", starttime);
		map.put("endtime", endtime);
		try {
			selecteds=getSqlMapClientTemplate().queryForList("findSelectedByScoreWithoutTopshow",map);
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(),e.fillInStackTrace());
			selecteds=new ArrayList<Selected>();
			selected=new Selected();
			selected.setFlag(ResultUtils.QUERY_ERROR);
			selecteds.add(selected);
		}
		return selecteds;
	}
	
	/**
	 * 
	 * @Title: findSelectedRandomWithoutTopshow 
	 * @Description: TODO
	 * @author Administrator
	 * @date 2015-7-17 上午11:17:37
	 * @param @param pageSize
	 * @param @return
	 * @return List<Selected>
	 * @throws
	 */
	@Override
	public List<Selected> findSelectedRandomWithoutTopshow(Long starttime,Long endtime,Integer pageSize){
		List<Selected> selecteds=new ArrayList<Selected>();
		Selected selected=new Selected();
		Map<Object,Object> map=new HashMap<Object, Object>();
		map.put("pageSize", pageSize);
		map.put("starttime", starttime);
		map.put("endtime", endtime);
		try {
			selecteds=getSqlMapClientTemplate().queryForList("findSelectedRandomWithoutTopshow",map);
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(),e.fillInStackTrace());
			selecteds=new ArrayList<Selected>();
			selected=new Selected();
			selected.setFlag(ResultUtils.QUERY_ERROR);
			selecteds.add(selected);
		}
		return selecteds;
	}
	
	/**
	 * 根据置顶值排序 查询精选列表 (只是置顶的资源)
	 * @Title: findSelectedByScoreWithoutTopshow 
	 * @Description: TODO
	 * @author Administrator
	 * @date 2015-7-8 下午3:41:07
	 * @param @param score
	 * @param @param pageSize
	 * @param @return
	 * @return List<Selected>
	 * @throws
	 */
	@Override
	public List<Selected> findSelectedByTopshow(){
		List<Selected> selecteds=new ArrayList<Selected>();
		Selected selected=new Selected();
		try {
			selecteds=getSqlMapClientTemplate().queryForList("findSelectedByTopshow");
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(),e.fillInStackTrace());
			selecteds=new ArrayList<Selected>();
			selected=new Selected();
			selected.setFlag(ResultUtils.QUERY_ERROR);
			selecteds.add(selected);
		}
		return selecteds;
	}
	
	/**
	 * 查询精选数量-不包含用户
	 */
	@Override
	public long getSelectedCount(){
		long i = 0;
		try{
			Long total = (Long) getSqlMapClientTemplate().queryForObject("getSelectedCount");
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
	 * 查询推荐用户
	 */
	@Override
	public List<Selected> findSelectedUserByScore(Long score,Integer pageSize){
		List<Selected> selecteds=new ArrayList<Selected>();
		Selected selected=new Selected();
		Map<Object,Object> map=new HashMap<Object, Object>();
		map.put("score", score);
		map.put("pageSize", pageSize);
		try {
			selecteds=getSqlMapClientTemplate().queryForList("findSelectedUserByScore",map);
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(),e.fillInStackTrace());
			selecteds=new ArrayList<Selected>();
			selected=new Selected();
			selected.setFlag(ResultUtils.QUERY_ERROR);
			selecteds.add(selected);
		}
		return selecteds;
	}
	/**
	 * 根据推荐用户id查询所关联的资源
	 */
	@Override
	public List<Selected> findSelectedUserResource(List<Long> userids){
		List<Selected> selecteds=new ArrayList<Selected>();
		Selected selected=new Selected();
		Map<Object,Object> map=new HashMap<Object, Object>();
		map.put("useridList", userids);
		try {
			selecteds=getSqlMapClientTemplate().queryForList("findSelectedUserResource",map);
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(),e.fillInStackTrace());
			selecteds=new ArrayList<Selected>();
			selected=new Selected();
			selected.setFlag(ResultUtils.QUERY_ERROR);
			selecteds.add(selected);
		}
		return selecteds;
	}
	
	/**
	 * 根据资源id和资源类型查询
	 */
	@Override
	public Selected findSelectedByResidAndType(long resid,String type){
		Selected selected=null;
		Map<Object,Object> map=new HashMap<Object, Object>();
		map.put("resid", resid);
		map.put("type", type);
		try {
			selected=(Selected) getSqlMapClientTemplate().queryForObject("findSelectedByResidAndType",map);
			if(selected!=null){
				selected.setFlag(ResultUtils.SUCCESS);
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(),e.fillInStackTrace());
			selected=new Selected();
			selected.setFlag(ResultUtils.QUERY_ERROR);
		}
		return selected;
	}
	
	/**
	 * 查询最新的十条信息
	 */
	@Override
	public List<Selected> findSelectedByIdOrderDesc() {
		List<Selected> selectedList = new ArrayList<Selected>();
		try{
			selectedList = getSqlMapClientTemplate().queryForList("findSelectedByIdOrderDesc");
			if(null==selectedList){
				selectedList = new ArrayList<Selected>();
			}
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			selectedList = new ArrayList<Selected>();
		}
		return selectedList;
	}
	
	/**
	 * 查询两个id之间的精选内容
	 */
	@Override
	public List<Selected> findSelectedByMiddle(int firstIndex, int secondIndex,int pageSize,long timeSeparation) {
		List<Selected> selectList = new ArrayList<Selected>();
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			map.put("firstIndex", firstIndex);
			map.put("secondIndex", secondIndex);
			map.put("pageSize", pageSize);
			map.put("timeSeparation",timeSeparation);
			selectList = getSqlMapClientTemplate().queryForList("findSelectedByMiddle",map);
			if(null==selectList){
				selectList = new ArrayList<Selected>();
			}
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			selectList = new ArrayList<Selected>();
		}
		return selectList;
	}
	
	/**
	 * 查询大于一个id的精选内容
	 */
	@Override
	public List<Selected> findSelectedOrderId(int bigIndex) {
		List<Selected> selectList = new ArrayList<Selected>();
		try{
			selectList = getSqlMapClientTemplate().queryForList("findSelectedOrderId",bigIndex);
			if(null==selectList){
				selectList = new ArrayList<Selected>();
			}
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			selectList = new ArrayList<Selected>();
		}
		return selectList;
	}
}
