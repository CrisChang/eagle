package com.poison.product.dao;

import java.util.List;

import com.poison.product.model.Product;

/**
 * @author wzs
 *
 */
public interface ProductDao {

	/**
	 *
	 * @return
	 */
	public List<Product> findProductsByType(String ptype);
	/**
	 * 根据商品id查询商品信息
	 */
	public Product findProductByPid(long pid);
	/**
	 * 根据商品价格和商品类型查询
	 */
	public Product findProductByPriceAndType(long price,String type);
}
