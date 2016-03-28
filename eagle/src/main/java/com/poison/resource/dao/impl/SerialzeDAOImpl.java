package com.poison.resource.dao.impl;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.poison.resource.model.ChapterSummary;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.poison.eagle.utils.ResultUtils;
import com.poison.resource.dao.SerializeDAO;
import com.poison.resource.model.Chapter;
import com.poison.resource.model.Serialize;

@SuppressWarnings("deprecation")
public class SerialzeDAOImpl extends SqlMapClientDaoSupport implements
		SerializeDAO {
	
	private static final  Log LOG = LogFactory.getLog(SerialzeDAOImpl.class);
	/**
	 * 添加连载
	 */
	@Override
	public int addSerialize(Serialize ser) {
		try {
			getSqlMapClientTemplate().insert("addSerialize",ser);
			return ResultUtils.SUCCESS;
		} catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			return ResultUtils.ERROR;
		}
	}
	/**
	 * 添加章节
	 */
	@Override
	public int addChapter(Chapter ch) {
		try {
			getSqlMapClientTemplate().insert("addChapter",ch);
			return ResultUtils.SUCCESS;
		} catch (Exception e) {
			return ResultUtils.ERROR;
		}
	}
	/**
	 * 续写
	 */
	@Override
	public int addOldChapter(Chapter ch) {
		try {
			getSqlMapClientTemplate().insert("addChapter",ch);
			return ResultUtils.SUCCESS;
		} catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			return ResultUtils.ERROR;
	}

}
	/**
	 * 修改连载名称
	 */
	@Override
	public int updateSerialize(Serialize ser) {
		try {
			getSqlMapClientTemplate().update("updateSerialize",ser);
			return ResultUtils.SUCCESS;
		} catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			return ResultUtils.UPDATE_ERROR;
		}
	}
	
	
	/**
	 * 修改连载类型
	 */
	@Override
	public int updateSerializeType(Serialize ser) {
		try {
			getSqlMapClientTemplate().update("updateSerializeType",ser);
			return ResultUtils.SUCCESS;
		} catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			return ResultUtils.UPDATE_ERROR;
		}
	}
	

	@Override
	public int findById(long id) {
		try {
			getSqlMapClientTemplate().queryForObject("findByIds",id);
			return ResultUtils.SUCCESS;
		} catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			return ResultUtils.DATAISNULL;
		}
	}
	/**
	 * 修改章节名称
	 */
	@Override
	public int updateChapter(Chapter ch) {
		try {
			getSqlMapClientTemplate().update("updateChapter",ch);
			return ResultUtils.SUCCESS;
		} catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			return ResultUtils.UPDATE_ERROR;
		}
	}

	@Override
	public int queryById(long id) {
		try {
			getSqlMapClientTemplate().queryForObject("queryByIds",id);
			return ResultUtils.SUCCESS;
		} catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			return ResultUtils.DATAISNULL;
		}
	}
	/**
	 * 修改章节内容
	 */
	@Override
	public int updateChapterContent(Chapter ch) {
		try {
			getSqlMapClientTemplate().update("updateChapterContent",ch);
			return ResultUtils.SUCCESS;
		} catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			return ResultUtils.UPDATE_ERROR;
		}
	}
	/**
	 * 修改连载详情内容
	 */
	@Override
	public int updateSerializeIntroduce(Serialize ser) {
		try {
			getSqlMapClientTemplate().update("updateSerializeIntroduce",ser);
			return ResultUtils.SUCCESS;
		} catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			return ResultUtils.UPDATE_ERROR;
		}
	}
	/**
	 * 根据id删除章节信息
	 */
	@Override
	public int deleteChapter(Chapter ch) {
		try {
			getSqlMapClientTemplate().update("deleteChapter",ch);
			return ResultUtils.SUCCESS;
		} catch (Exception e) {
			return ResultUtils.DELETE_ERROR;
		}
	}
	/**
	 * 根据id删除连载信息 
	 */
	@Override
	public int deleteSerialize(Serialize ser) {
		try {
			getSqlMapClientTemplate().update("deleteSerialize",ser);
			return ResultUtils.SUCCESS;
		} catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			return ResultUtils.DELETE_ERROR;
		}
	}
	/**
	 * 根据id查询连载内容
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Serialize> findAllSerialize(long id) {
		List<Serialize> list=new ArrayList<Serialize>();
		try {
			list=getSqlMapClientTemplate().queryForList("findByIds",id);
		} catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			list=new ArrayList<Serialize>();
			Serialize ser=new Serialize();
			ser.setFlag(ResultUtils.QUERY_ERROR);
			list.add(ser);
			return list;
		}
		return list;
	}
	/**
	 * 根据类别查询连载信息
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Serialize> findSerialize(String type) {
		List<Serialize> list=new ArrayList<Serialize>();
		try {
			list=getSqlMapClientTemplate().queryForList("findByType",type);
			Serialize ser=new Serialize();
			list=new ArrayList<Serialize>();
			ser.setFlag(ResultUtils.DATAISNULL);
			list.add(ser);
		} catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			Serialize ser=new Serialize();
			list=new ArrayList<Serialize>();
			ser.setFlag(ResultUtils.QUERY_ERROR);
			list.add(ser);
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Chapter> findChapter(long parentId) {
		List<Chapter> list=new ArrayList<Chapter>();
		try {
			list=getSqlMapClientTemplate().queryForList("findChapter",parentId);
		} catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			list=new ArrayList<Chapter>();
		}
		return list;
	}

	@Override
	public List<ChapterSummary> findChapterSummary(long parentId) {
		List<ChapterSummary> list=new ArrayList<ChapterSummary>();
		try {
			list=getSqlMapClientTemplate().queryForList("findChapterSummary",parentId);
		} catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			list=new ArrayList<ChapterSummary>();
		}
		return list;
	}

	/**
	 * 此方法的作用是根据查询章节内容
	 */
	@Override
	public Chapter queryChapterContent(long id) {
		 Chapter ch=new Chapter();
		try {
			ch=(Chapter) getSqlMapClientTemplate().queryForObject("findChapterContent",id);
		} catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			ch.setFlag(ResultUtils.QUERY_ERROR);
			return ch;
		}
		return ch;
	}
	/**
	 * 查询所有连载信息
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Serialize> findAllSerialize() {
		List<Serialize> serlist=new ArrayList<Serialize>();
		try {
			serlist=getSqlMapClientTemplate().queryForList("findAllSerialize");
			if(null==serlist){
				Serialize ser=new Serialize();
				serlist=new ArrayList<Serialize>();
				ser.setFlag(ResultUtils.DATAISNULL);
				serlist.add(ser);
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			Serialize ser=new Serialize();
			serlist=new ArrayList<Serialize>();
			ser.setFlag(ResultUtils.QUERY_ERROR);
			serlist.add(ser);
			return serlist;
		}
		return serlist;
	}
	/**
	 * 查询所有章节信息
	 *//*
	@SuppressWarnings("unchecked")
	@Override
	public List<Chapter> findAllChapter() {
		List<Chapter> chlist=new ArrayList<Chapter>();
		try {
			chlist=getSqlMapClientTemplate().queryForList("");
			if(null==chlist){
				Chapter ch=new Chapter();
				chlist=new ArrayList<Chapter>();
				ch.setFlag(ResultUtils.DATAISNULL);
				chlist.add(ch);
			}
		} catch (Exception e) {
			Chapter ch=new Chapter();
			chlist=new ArrayList<Chapter>();
			ch.setFlag(ResultUtils.QUERY_ERROR);
			chlist.add(ch);
		}
		return chlist;
	}*/
	/**
	 * 此方法的作用是根据用户名查询连载信息
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Serialize> findSerializeByUser(long uid) {
		List<Serialize> serlist;
		try {
			serlist=getSqlMapClientTemplate().queryForList("findSerializeByUid",uid);
			if(null==serlist){
				serlist=new ArrayList<Serialize>();
				Serialize ser=new Serialize();
				ser.setFlag(ResultUtils.DATAISNULL);
				serlist.add(ser);
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			serlist=new ArrayList<Serialize>();
			Serialize ser=new Serialize();
			ser.setFlag(ResultUtils.QUERY_ERROR);
			serlist.add(ser);
		}
		return serlist;
	}
	
	/**
	 * 根据用户ID查询连载分页列表
	 */
	@SuppressWarnings("unchecked")
	public List<Serialize> findSerializePageByUserId(Long uid, int beginNo,	int pageSize) {
		List<Serialize> serlist;
		Map<Object,Object> map=new HashMap<Object, Object>();
		map.put("uid", uid);
		map.put("beginNo", beginNo);
		map.put("pageSize", pageSize);
		try {
			serlist=getSqlMapClientTemplate().queryForList("findSerializePageByUserId",map);
			if(null==serlist){
				serlist=new ArrayList<Serialize>();
				Serialize ser=new Serialize();
				ser.setFlag(ResultUtils.DATAISNULL);
				serlist.add(ser);
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			serlist=new ArrayList<Serialize>();
			Serialize ser=new Serialize();
			ser.setFlag(ResultUtils.QUERY_ERROR);
			serlist.add(ser);
		}
		return serlist;
	}
	
	/**
	 * 根据用户id查询列表
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Serialize> findSerializeUserId(List<Long> userList, Long uid) {
		List<Serialize> serlist=new ArrayList<Serialize>();
		Serialize ser=new Serialize();
		Map<Object,Object> map=new HashMap<Object, Object>();
		map.put("userList", userList);
		map.put("uid", uid);
		try {
			if(null==userList||userList.size()==0){
				return serlist;
			}
			serlist=getSqlMapClientTemplate().queryForList("findSerializeUserId",map);
		} catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			serlist=new ArrayList<Serialize>();
			ser.setFlag(ResultUtils.QUERY_ERROR);
			serlist.add(ser);
			return serlist;
		}
		return serlist;
	}
	
	/**
	 * 根据ID查询连载信息
	 */
	@Override
	public Serialize findSerializeById(long id) {
		Serialize ser = new Serialize();
		try{
			ser = (Serialize) getSqlMapClientTemplate().queryForObject("findByIds",id);
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			ser.setFlag(ResultUtils.QUERY_ERROR);
		}
		return ser;
	}
	
	/**
	 * 查询这个章节是否存在
	 */
	@Override
	public Serialize findSerializeIsExisted(long userId, String name) {
		Serialize serialize = new Serialize();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", userId);
		map.put("name", name);
		try{
			serialize =  (Serialize) getSqlMapClientTemplate().queryForObject("findSerializeIsExisted",map);
			if(null==serialize){
				serialize = new Serialize();
				serialize.setFlag(ResultUtils.DATAISNULL);
				return serialize;
			}
			serialize.setFlag(ResultUtils.SUCCESS);
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			serialize = new Serialize();
			serialize.setFlag(ResultUtils.QUERY_ERROR);
		}
		return serialize;
	}

	// 根据用户id查询该用户的连载数量
	public int findSerializeTotalCountByUserId(Long uid) {
		Integer totalCount;
		try{
			totalCount = (Integer) getSqlMapClientTemplate().queryForObject("findSerializeTotalCountByUserId", uid);
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			totalCount = 0;
		}
		return totalCount;
	}
	
	//按章节查询章节
	public Chapter findChapterToPage(Long uid, Long serId, int beginNo) {
		Chapter chapter = new Chapter();
		Map<Object,Object> map=new HashMap<Object, Object>();
		map.put("uid", uid);
		map.put("beginNo", beginNo);
		map.put("serId", serId);
		try {
			chapter=(Chapter) getSqlMapClientTemplate().queryForObject("findChapterToPage",map);
			if(null==chapter){
				//chapter=new Chapter();
				chapter.setFlag(ResultUtils.DATAISNULL);
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			//chapter=new Chapter();
			chapter.setFlag(ResultUtils.QUERY_ERROR);
		}
		return chapter;
	}

	//根据用户id查询该用户的章节数量
	public int findChapterTotalCountByUserId(Long serId) {
		Integer totalCount;
		try{
			totalCount = (Integer) getSqlMapClientTemplate().queryForObject("findChapterTotalCountByUserId", serId);
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			totalCount = 0;
		}
		return totalCount;
	}
	
	/**
	 * 更新连载的封面地址
	 */
	@Override
	public int updateSerializeUrl(Serialize ser) {
		int flag = ResultUtils.ERROR;
		try{
			getSqlMapClientTemplate().update("updateSerializeUrl",ser);
			flag = ResultUtils.SUCCESS;
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			flag = ResultUtils.ERROR;
		}
		return flag;
	}
}
