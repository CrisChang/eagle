package com.poison.product.client;

import java.util.List;
import java.util.Map;

import com.poison.paycenter.model.PayLog;
import com.poison.product.model.Product;
import com.poison.product.model.ProductOrder;

public interface ProductFacade {
	/**
	 *
	 * @return
	 */
	public List<Product> findProductsByType(String ptype);

	/**
	 * @param
	 * @returno
	 */
	public int insertIntoProductOrder(long pid,long uid,long amount,long virtualamount,String tn,int paystatus,String title,int status,String remark,String paymode,String ptype);
	
	/**
	 * @param
	 * @returno
	 */
	public int updateProductOrderPaystatus(int paystatus,int status,String paymode,String ordernum);
	/**
	 * 根据商品id查询商品信息
	 */
	public Product findProductByPid(long pid);
	/**
	 * 根据商品价格和商品类型查询
	 */
	public Product findProductByPriceAndType(long price,String type);
	/**
	 * 根据tn号查询订单信息(主要用于苹果支付查询--用于苹果支付的原始收据的MD5加密数据查询)
	 */
	public ProductOrder findProductOrderByTn(String tn);
	/**
	 * 支付宝付款前的服务器逻辑
	 * @return
	 */
	public Map<String, String> createAlipayOrder(final Map<String, String> inMap);
	/**
	 * 微信支付，需要向微信下订单,微信付款前的服务器逻辑
	 * @param inMap
	 * @return
	 */
	public Map<String, String> createWeixinPayOrder(final Map<String, String> inMap);
	/**
	 * 苹果付款前的服务器逻辑
	 * @return
	 */
	public Map<String, String> createAppleOrder(final Map<String, String> inMap);
	/**
	 * 付款后的异步通知支付结果(支付宝、微信)
	 * @param paylog
	 * @return
	 */
	public Map<String, String> notifyResult(final PayLog paylog);
	/**
	 * 虚拟金币购买章节的事物逻辑
	 * @return
	 */
	public Map<String, String> payStoryChapter(final Map<String, String> inMap);
}
