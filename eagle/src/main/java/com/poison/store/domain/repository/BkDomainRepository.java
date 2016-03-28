package com.poison.store.domain.repository;

import java.util.ArrayList;
import java.util.List;

import com.poison.eagle.utils.ResultUtils;
import com.poison.store.dao.BkInfoDAO;
import com.poison.store.model.BkInfo;

public class BkDomainRepository {

	private BkInfoDAO bkInfoDAO;

	public void setBkInfoDAO(BkInfoDAO bkInfoDAO) {
		this.bkInfoDAO = bkInfoDAO;
	}

	/**
	 * 
	 * <p>Title: findBkInfo</p> 
	 * <p>Description: 查询一本书的详情</p> 
	 * @author :changjiang
	 * date 2014-8-6 下午2:32:03
	 * @param id
	 * @return
	 */
	public BkInfo findBkInfo(int id){
		return bkInfoDAO.findBkInfo(id);
	}
	
	/**
	 * 
	 * <p>Title: findBkInfoByName</p> 
	 * <p>Description: 根据书名查询一本书的信息</p> 
	 * @author :changjiang
	 * date 2014-8-7 上午10:13:10
	 * @param name
	 * @return
	 */
	public List<BkInfo> findBkInfoByName(String name){
		List<BkInfo> bkList = bkInfoDAO.findBkInfoByName(name);
		if(null==bkList||bkList.size()==0){
			bkList = bkInfoDAO.findBkInfoByLikeName(name);
		}
		return bkList;
	}
	
	/**
	 * 
	 * <p>Title: insertBkInfo</p> 
	 * <p>Description: 插入一本书籍信息</p> 
	 * @author :changjiang
	 * date 2014-8-13 下午5:17:22
	 * @return
	 */
	public int insertBkInfo(BkInfo bkInfo){
		return bkInfoDAO.insertBkInfo(bkInfo);
	}
	
	/**
	 * 
	 * <p>Title: findBkInfo</p> 
	 * <p>Description: 根据isbn查询一本书的详情</p> 
	 * @author :changjiang
	 * date 2014-8-19 上午12:29:49
	 * @param isbn
	 * @return
	 */
	public BkInfo findBkInfoByIsbn(String isbn){
		List<BkInfo> bkList = new ArrayList<BkInfo>();
		bkList = bkInfoDAO.findBkInfoByIsbn(isbn);
		BkInfo bkInfo = new BkInfo();
		if(bkList.size()>0){
			bkInfo = bkList.get(0);
			return bkInfo;
		}
		return bkInfo;
	}
	/**
	 * 
	 * <p>Title: findBkInfoBybookurl</p> 
	 * <p>Description: 根据bookurl查询一本书的详情</p> 
	 * @author :guandapeng
	 * date 2014-11-25 下午16:55:27 
	 * @param bookurl
	 * @return
	 */
	public BkInfo findBkInfoBybookurl(String bookurl){
		List<BkInfo> bkList = new ArrayList<BkInfo>();
		bkList = bkInfoDAO.findBkInfoBybookurl(bookurl);
		BkInfo bkInfo = new BkInfo();
		if(bkList.size()>0){
			bkInfo = bkList.get(0);
			return bkInfo;
		}
		bkInfo.setFlag(ResultUtils.DATAISNULL);
		return bkInfo;
	}
	/**
	 * 根据id集合查询电影信息集合
	 */
	public List<BkInfo> findBkInfosByIds(long ids[]){
		return bkInfoDAO.findBkInfosByIds(ids);
	}
}
