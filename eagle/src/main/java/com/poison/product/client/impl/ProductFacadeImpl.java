package com.poison.product.client.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.poison.paycenter.model.PayLog;
import com.poison.product.client.ProductFacade;
import com.poison.product.model.Product;
import com.poison.product.model.ProductOrder;
import com.poison.product.service.ProductService;

public class ProductFacadeImpl implements ProductFacade{

	private static final  Log LOG = LogFactory.getLog(ProductFacadeImpl.class);
	
	private ProductService productService;

	public void setProductService(ProductService productService) {
		this.productService = productService;
	}

	/**
	 *
	 * @return
	 */
	@Override
	public List<Product> findProductsByType(String ptype){
		return productService.findProductsByType(ptype);
	}

	/**
	 * @param
	 * @returno
	 */
	@Override
	public int insertIntoProductOrder(long pid,long uid,long amount,long virtualamount,String tn,int paystatus,String title,int status,String remark,String paymode,String ptype){
		ProductOrder productOrder = new ProductOrder();
		productOrder.setPid(pid);
		productOrder.setUid(uid);
		productOrder.setAmount(amount);
		productOrder.setVirtualamount(virtualamount);
		productOrder.setTn(tn);
		productOrder.setPaystatus(paystatus);
		productOrder.setTitle(title);
		productOrder.setStatus(status);
		productOrder.setRemark(remark);
		productOrder.setPaymode(paymode);
		productOrder.setPtype(ptype);
		if(paystatus==1){
			productOrder.setPaytime(System.currentTimeMillis());
		}
		return productService.insertIntoProductOrder(productOrder);
	}
	
	/**
	 * @param
	 * @returno
	 */
	@Override
	public int updateProductOrderPaystatus(int paystatus,int status,String paymode,String ordernum){
		ProductOrder productOrder = new ProductOrder();
		productOrder.setPaystatus(paystatus);
		productOrder.setStatus(status);
		productOrder.setPaymode(paymode);
		productOrder.setOrdernum(ordernum);
		productOrder.setUpdatetime(System.currentTimeMillis());
		if(paystatus==1){
			productOrder.setPaytime(System.currentTimeMillis());
		}
		return productService.updateProductOrderPaystatus(productOrder);
	}
	/**
	 * 根据商品id查询商品信息
	 */
	@Override
	public Product findProductByPid(long pid){
		return productService.findProductByPid(pid);
	}
	/**
	 * 根据商品价格和商品类型查询
	 */
	@Override
	public Product findProductByPriceAndType(long price,String type){
		return productService.findProductByPriceAndType(price, type);
	}
	/**
	 * 根据tn号查询订单信息(主要用于苹果支付查询--用于苹果支付的原始收据的MD5加密数据查询)
	 */
	@Override
	public ProductOrder findProductOrderByTn(String tn){
		return productService.findProductOrderByTn(tn);
	}
	/**
	 * 支付宝付款前的服务器逻辑
	 * @return
	 */
	@Override
	public Map<String, String> createAlipayOrder(final Map<String, String> inMap){
		return productService.createAlipayOrder(inMap);
	}
	/**
	 * 微信支付，需要向微信下订单,微信付款前的服务器逻辑
	 * @param inMap
	 * @return
	 */
	@Override
	public Map<String, String> createWeixinPayOrder(final Map<String, String> inMap){
		return productService.createWeixinPayOrder(inMap);
	}
	/**
	 * 苹果付款前的服务器逻辑
	 * @return
	 */
	@Override
	public Map<String, String> createAppleOrder(final Map<String, String> inMap){
		return productService.createAppleOrder(inMap);
	}
	/**
	 * 付款后的异步通知支付结果(支付宝、微信)
	 * @param paylog
	 * @return
	 */
	@Override
	public Map<String, String> notifyResult(final PayLog paylog){
		return productService.notifyResult(paylog);
	}
	/**
	 * 虚拟金币购买章节的事物逻辑
	 * @return
	 */
	@Override
	public Map<String, String> payStoryChapter(final Map<String, String> inMap){
		return productService.payStoryChapter(inMap);
	}
}
