package com.poison.product.model;

import java.util.HashSet;
import java.util.Set;

import com.keel.common.lang.BaseDO;

/**
 * 商品
 * @author weizhensong
 *
 */
public class ProductOrder extends BaseDO {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3752875237260286510L;
	
	/**
	 * 微信支付方式
	 */
	public static final String PAYMODE_WX="wx";//微信支付方式
	/**
	 * 支付宝
	 */
	public static final String PAYMODE_ALIPAY="alipay";//支付宝
	/**
	 * 苹果支付
	 */
	public static final String PAYMODE_APPLE="apple";//苹果支付
	/**
	 * 虚拟金币
	 */
	public static final String PAYMODE_VIRTUAL="virtual";//虚拟金币支付
	
	private static final Set<String> PAYMODES = new HashSet<String>(2);
	static{
		PAYMODES.add(PAYMODE_WX);
		PAYMODES.add(PAYMODE_ALIPAY);
		PAYMODES.add(PAYMODE_APPLE);
		PAYMODES.add(PAYMODE_VIRTUAL);
	}
	
	public static boolean hasPayMode(String paymode){
		if(paymode!=null && PAYMODES.contains(paymode)){
			return true;
		}
		return false;
	}
	
	/**
	 * 主键id
	 */
	private long id;
	/**
	 * 商品id
	 */
	private long pid;
	/**
	 * 商品数量
	 */
	private int num;
	/**
	 * 用户id
	 */
	private long uid;
	/**
	 * 订单号
	 */
	private String ordernum;
	/**
	 * 订单金额（真钱）
	 */
	private long amount;
	/**
	 * 订单金额（虚拟金币）
	 */
	private long virtualamount;
	/**
	 * 第三方支付流水号(如果是苹果支付，则为base64加密的原始收据数据)
	 */
	private String tn;
	/**
	 * 订单支付状态
	 */
	private int paystatus;
	/**
	 * 订单标题
	 */
	private String title;
	/**
	 * 订单状态
	 */
	private int status;
	/**
	 * 是否删除
	 */
	private int isdel;
	/**
	 * 创建时间
	 */
	private long createtime;
	/**
	 * 修改时间
	 */
	private long updatetime;
	/**
	 * 支付时间
	 */
	private long paytime;
	/**
	 * 订单说明
	 */
	private String remark;
	/**
	 * 支付方式
	 */
	private String paymode;
	/**
	 * 商品类型
	 */
	private String ptype;
	
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
	public int getIsdel() {
		return isdel;
	}
	public void setIsdel(int isdel) {
		this.isdel = isdel;
	}
	public long getPid() {
		return pid;
	}
	public void setPid(long pid) {
		this.pid = pid;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public long getUid() {
		return uid;
	}
	public void setUid(long uid) {
		this.uid = uid;
	}
	public String getOrdernum() {
		return ordernum;
	}
	public void setOrdernum(String ordernum) {
		this.ordernum = ordernum;
	}
	public long getAmount() {
		return amount;
	}
	public void setAmount(long amount) {
		this.amount = amount;
	}
	public long getVirtualamount() {
		return virtualamount;
	}
	public void setVirtualamount(long virtualamount) {
		this.virtualamount = virtualamount;
	}
	public String getTn() {
		return tn;
	}
	public void setTn(String tn) {
		this.tn = tn;
	}
	public int getPaystatus() {
		return paystatus;
	}
	public void setPaystatus(int paystatus) {
		this.paystatus = paystatus;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public long getPaytime() {
		return paytime;
	}
	public void setPaytime(long paytime) {
		this.paytime = paytime;
	}
	public String getPaymode() {
		return paymode;
	}
	public void setPaymode(String paymode) {
		this.paymode = paymode;
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
