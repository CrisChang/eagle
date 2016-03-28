package com.poison.product.model;

import java.util.HashSet;
import java.util.Set;

import com.keel.common.lang.BaseDO;

/**
 * 商品
 * @author weizhensong
 *
 */
public class Product extends BaseDO {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3752875237260286510L;
	
	public static final String TYPE_GOLD="0";//金币类型商品(虚拟币类型商品，需要真钱付款购买)
	public static final String TYPE_REAL_MONEY="1";//需要真钱付款的商品
	public static final String TYPE_VIRTUAL_MONEY="2";//月票商品--金币购买
	public static final String TYPE_MONTH_TICKET="3";//打赏商品--金币购买
	private static final Set<String> types = new HashSet<String>(10);
	static{
		types.add(TYPE_GOLD);
		types.add(TYPE_REAL_MONEY);
		types.add(TYPE_VIRTUAL_MONEY);
		types.add(TYPE_MONTH_TICKET);
	}
	
	public static boolean containsType(String type){
		if(types.contains(type)){
			return true;
		}
		return false;
	}
	/**
	 * 主键id
	 */
	private long id;
	/**
	 * 商品标题
	 */
	private String title;
	/**
	 * 商品说明
	 */
	private String remark;
	/**
	 * 价格
	 */
	private long price;
	/**
	 * 虚拟价格
	 */
	private long virtualprice;
	/**
	 * 图片地址
	 */
	private String image;
	/**
	 * 库存数量
	 */
	private long stockamount;
	/**
	 * 套餐数量值
	 */
	private long packamount;
	/**
	 * 赠送数量
	 */
	private long giveamount;
	/**
	 * 是否删除
	 */
	private int isdel;
	/**
	 * 商品类型
	 */
	private String ptype;
	/**
	 * 创建时间
	 */
	private long createtime;
	/**
	 * 修改时间
	 */
	private long updatetime;
	
	private int flag;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getCreatetime() {
		return createtime;
	}
	public void setCreatetime(long createtime) {
		this.createtime = createtime;
	}
	public long getUpdatetime() {
		return updatetime;
	}
	public void setUpdatetime(long updatetime) {
		this.updatetime = updatetime;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public long getPrice() {
		return price;
	}
	public void setPrice(long price) {
		this.price = price;
	}
	public long getVirtualprice() {
		return virtualprice;
	}
	public void setVirtualprice(long virtualprice) {
		this.virtualprice = virtualprice;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public long getStockamount() {
		return stockamount;
	}
	public void setStockamount(long stockamount) {
		this.stockamount = stockamount;
	}
	public long getPackamount() {
		return packamount;
	}
	public void setPackamount(long packamount) {
		this.packamount = packamount;
	}
	public long getGiveamount() {
		return giveamount;
	}
	public void setGiveamount(long giveamount) {
		this.giveamount = giveamount;
	}
	public int getIsdel() {
		return isdel;
	}
	public void setIsdel(int isdel) {
		this.isdel = isdel;
	}
	public String getPtype() {
		return ptype;
	}
	public void setPtype(String ptype) {
		this.ptype = ptype;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	
}
