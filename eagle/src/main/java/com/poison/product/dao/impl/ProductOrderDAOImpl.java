package com.poison.product.dao.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.poison.eagle.utils.ResultUtils;
import com.poison.product.dao.ProductOrderDao;
import com.poison.product.model.Product;
import com.poison.product.model.ProductOrder;

public class ProductOrderDAOImpl extends SqlMapClientDaoSupport implements ProductOrderDao{

	private static final  Log LOG = LogFactory.getLog(ProductOrderDAOImpl.class);

	/**
	 *  插入一个商品订单
	 */
	@Override
	public int insertIntoProductOrder(ProductOrder productOrder){
		int i = ResultUtils.ERROR;
		try{
			getSqlMapClientTemplate().insert("insertIntoProductOrder", productOrder);
			i = ResultUtils.SUCCESS;
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			i = ResultUtils.ERROR;
		}
		return i;
	}
	/**
	 * 更新一个订单的支付状态
	 */
	public int updateProductOrderPaystatus(ProductOrder productOrder){
		int i = ResultUtils.ERROR;
		try{
			getSqlMapClientTemplate().update("updateProductOrderPaystatus",productOrder);
			i = ResultUtils.SUCCESS;
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			i = ResultUtils.ERROR;
		}
		return i;
	}
	/**
	 * 根据id获取一个订单信息
	 */
	@Override
	public ProductOrder findProductOrderById(long id){
		ProductOrder productOrder = null;
		try{
			productOrder = (ProductOrder) getSqlMapClientTemplate().queryForObject("findProductOrderById",id);
			if(productOrder!=null && productOrder.getId()>0){
				productOrder.setFlag(ResultUtils.SUCCESS);
			}
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			productOrder = new ProductOrder();
			productOrder.setFlag(ResultUtils.QUERY_ERROR);
		}
		return productOrder;
	}
	/**
	 * 根据tn号查询订单信息(主要用于苹果支付查询--用于苹果支付的原始收据的MD5加密数据查询)
	 */
	@Override
	public ProductOrder findProductOrderByTn(String tn){
		ProductOrder productOrder = null;
		try{
			productOrder = (ProductOrder) getSqlMapClientTemplate().queryForObject("findProductOrderByTn",tn);
			if(productOrder!=null && productOrder.getId()>0){
				productOrder.setFlag(ResultUtils.SUCCESS);
			}
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			productOrder = new ProductOrder();
			productOrder.setFlag(ResultUtils.QUERY_ERROR);
		}
		return productOrder;
	}
}
