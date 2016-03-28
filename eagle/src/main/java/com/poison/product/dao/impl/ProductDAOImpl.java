package com.poison.product.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.poison.eagle.utils.ResultUtils;
import com.poison.product.dao.ProductDao;
import com.poison.product.model.Product;

public class ProductDAOImpl extends SqlMapClientDaoSupport implements ProductDao{

	private static final  Log LOG = LogFactory.getLog(ProductDAOImpl.class);

	/**
	 *  根据商品类型查询商品信息
	 */
	@Override
	public List<Product> findProductsByType(String ptype){
		List<Product> products = null;
		try{
			products = getSqlMapClientTemplate().queryForList("findProductsByType",ptype);
			
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			products = new ArrayList<Product>(1);
			Product product = new Product();
			product.setFlag(ResultUtils.QUERY_ERROR);
			products.add(product);
		}
		return products;
	}
	/**
	 * 根据商品id查询商品信息
	 */
	@Override
	public Product findProductByPid(long pid){
		Product product = null;
		try{
			product = (Product) getSqlMapClientTemplate().queryForObject("findProductByPid",pid);
			if(product!=null && product.getId()>0){
				product.setFlag(ResultUtils.SUCCESS);
			}else{
				product.setFlag(ResultUtils.DATAISNULL);
			}
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			product = new Product();
			product.setFlag(ResultUtils.QUERY_ERROR);
		}
		return product;
	}
	/**
	 * 根据商品价格和商品类型查询
	 */
	@Override
	public Product findProductByPriceAndType(long price,String type){
		Product product = null;
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			map.put("goldamount", price);
			map.put("ptype", type);
			product = (Product) getSqlMapClientTemplate().queryForObject("findProductByPriceAndType",map);
			if(product!=null && product.getId()>0){
				product.setFlag(ResultUtils.SUCCESS);
			}else{
				product.setFlag(ResultUtils.DATAISNULL);
			}
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			product = new Product();
			product.setFlag(ResultUtils.QUERY_ERROR);
		}
		return product;
	}
}
