package com.poison.store.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.poison.eagle.utils.ResultUtils;
import com.poison.store.dao.NetBookDAO;
import com.poison.store.model.NetBook;

public class NetBookDAOImpl extends SqlMapClientDaoSupport implements NetBookDAO{

	private static final  Log LOG = LogFactory.getLog(NetBookDAOImpl.class);
	
	/**
	 * 根据id查询网络小说
	 */
	@Override
	public NetBook findNetBookInfoById(long id) {
		NetBook netBook = new NetBook();
		try{
			netBook = (NetBook) getSqlMapClientTemplate().queryForObject("findNetBkInfo",id);
			if(null==netBook){
				netBook = new NetBook();
				netBook.setFlag(ResultUtils.DATAISNULL);
				return netBook;
			}
			netBook.setFlag(ResultUtils.SUCCESS);
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			netBook = new NetBook();
			netBook.setFlag(ResultUtils.ERROR);
		}
		return netBook;
	}
	/**
	 * 根据id集合查询网络小说信息集合
	 */
	@Override
	public List<NetBook> findNetBkInfosByIds(long ids[]){
		List<NetBook> bklist=new ArrayList<NetBook>();
		NetBook bkInfo=new NetBook();
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			String idstr = "0";
			if(ids!=null && ids.length>0){
				for(int i=0;i<ids.length;i++){
					idstr = idstr+","+ids[i];
				}
			}
			map.put("ids", idstr);
			bklist=getSqlMapClientTemplate().queryForList("findNetBkInfosByIds",map);
			if(bklist==null||bklist.size()==0){
				bklist=new ArrayList<NetBook>();
				//bkInfo.setFlag(ResultUtils.DATAISNULL);
				//bklist.add(bkInfo);
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			bklist=new ArrayList<NetBook>();
			bkInfo.setFlag(ResultUtils.QUERY_ERROR);
			bklist.add(bkInfo);
			return bklist;
		}
		return bklist;
	}
}
