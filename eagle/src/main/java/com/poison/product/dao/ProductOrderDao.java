package com.poison.product.dao;

import com.poison.product.model.ProductOrder;

/**
 * @author wzs
 *
 */
public interface ProductOrderDao {

	/**
	 * @param
	 * @returno
	 */
	public int insertIntoProductOrder(ProductOrder productOrder);
	
	/**
	 * @param
	 * @returno
	 */
	public int updateProductOrderPaystatus(ProductOrder productOrder);
	/**
	 * 根据id获取一个订单信息
	 */
	public ProductOrder findProductOrderById(long id);
	/**
	 * 根据tn号查询订单信息(主要用于苹果支付查询--用于苹果支付的原始收据的MD5加密数据查询)
	 */
	public ProductOrder findProductOrderByTn(String tn);
}
