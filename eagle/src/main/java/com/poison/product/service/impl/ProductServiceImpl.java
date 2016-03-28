package com.poison.product.service.impl;

import java.util.List;
import java.util.Map;

import com.poison.paycenter.model.PayLog;
import com.poison.product.domain.repository.ProductDomainRepository;
import com.poison.product.model.Product;
import com.poison.product.model.ProductOrder;
import com.poison.product.service.ProductService;

public class ProductServiceImpl implements ProductService{

	private ProductDomainRepository productDomainRepository;

	public void setProductDomainRepository(
			ProductDomainRepository productDomainRepository) {
		this.productDomainRepository = productDomainRepository;
	}

	/**
	 *
	 * @return
	 */
	@Override
	public List<Product> findProductsByType(String ptype){
		return productDomainRepository.findProductsByType(ptype);
	}

	/**
	 * @param
	 * @returno
	 */
	@Override
	public int insertIntoProductOrder(ProductOrder productOrder){
		return productDomainRepository.insertIntoProductOrder(productOrder);
	}
	
	/**
	 * @param
	 * @returno
	 */
	@Override
	public int updateProductOrderPaystatus(ProductOrder productOrder){
		return productDomainRepository.updateProductOrderPaystatus(productOrder);
	}
	
	/**
	 * 根据商品id查询商品信息
	 */
	@Override
	public Product findProductByPid(long pid){
		return productDomainRepository.findProductByPid(pid);
	}
	/**
	 * 根据商品价格和商品类型查询
	 */
	@Override
	public Product findProductByPriceAndType(long price,String type){
		return productDomainRepository.findProductByPriceAndType(price, type);
	}
	/**
	 * 根据tn号查询订单信息(主要用于苹果支付查询--用于苹果支付的原始收据的MD5加密数据查询)
	 */
	@Override
	public ProductOrder findProductOrderByTn(String tn){
		return productDomainRepository.findProductOrderByTn(tn);
	}
	/**
	 * 支付宝付款前的服务器逻辑
	 * @return
	 */
	@Override
	public Map<String, String> createAlipayOrder(final Map<String, String> inMap){
		return productDomainRepository.createAlipayOrder(inMap);
	}
	/**
	 * 微信支付，需要向微信下订单,微信付款前的服务器逻辑
	 * @param inMap
	 * @return
	 */
	@Override
	public Map<String, String> createWeixinPayOrder(final Map<String, String> inMap){
		return productDomainRepository.createWeixinPayOrder(inMap);
	}
	/**
	 * 苹果付款前的服务器逻辑
	 * @return
	 */
	@Override
	public Map<String, String> createAppleOrder(final Map<String, String> inMap){
		return productDomainRepository.createAppleOrder(inMap);
	}
	/**
	 * 付款后的异步通知支付结果(支付宝、微信)
	 * @param paylog
	 * @return
	 */
	@Override
	public Map<String, String> notifyResult(final PayLog paylog){
		return productDomainRepository.notifyResult(paylog);
	}
	/**
	 * 虚拟金币购买章节的事物逻辑
	 * @return
	 */
	@Override
	public Map<String, String> payStoryChapter(final Map<String, String> inMap){
		return productDomainRepository.payStoryChapter(inMap);
	}
}
