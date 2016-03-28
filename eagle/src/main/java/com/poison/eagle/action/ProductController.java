package com.poison.eagle.action;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.poison.eagle.manager.ProductManager;
import com.poison.eagle.utils.BaseController;
import com.poison.eagle.utils.CommentUtils;
/**
 * 商品相关
 * @author Administrator
 *
 */
@Controller  
@RequestMapping(CommentUtils.REQUEST_FROM_MOBLIE)
public class ProductController extends BaseController {
	
	private static final  Log LOG = LogFactory.getLog(ProductController.class);
	
	private ProductManager productManager;
	
	public void setProductManager(ProductManager productManager) {
		this.productManager = productManager;
	}


	/**
	 * @param request
	 * @param response
	 * @param res 客户端传过来的json数据
	 * @return
	 */
	@RequestMapping(value="/clientview/get_gold_products",method=RequestMethod.POST,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String getGoldProducts(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{
		String reqs = "";
		long uid=16l;
		//获取用户id
		if(checkUserIsLogin(null)){
			uid = getUserId();
		}else{
			uid = 0;
			LOG.error(CommentUtils.ERROR_USERNOTLOGIN);
			return RES_USER_NOTLOGIN;
		}
		//获取客户端json数据
		try {
			reqs = request.getParameter("req");
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(CommentUtils.ERROR_DATANOTGET);
			//return RES_DATA_NOTGET;
		}
		//调用manager方法获取返回数据
		String res = productManager.getGoldProducts(reqs, uid);
		return res;
	}
	
	/**
	 * 获取商品列表
	 * @param request
	 * @param response
	 * @param res 客户端传过来的json数据
	 * @return
	 */
	@RequestMapping(value="/clientview/get_products",method=RequestMethod.POST,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String getProducts(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{
		String reqs = "";
		long uid=16l;
		//获取用户id
		if(checkUserIsLogin(null)){
			uid = getUserId();
		}else{
			uid = 0;
			LOG.error(CommentUtils.ERROR_USERNOTLOGIN);
			return RES_USER_NOTLOGIN;
		}
		//获取客户端json数据
		try {
			reqs = request.getParameter("req");
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(CommentUtils.ERROR_DATANOTGET);
			//return RES_DATA_NOTGET;
		}
		//调用manager方法获取返回数据
		String res = productManager.getProducts(reqs, uid);
		return res;
	}
	
	
	//购买商品--包含虚拟金币和虚拟商品
	@RequestMapping(value="/clientaction/buy_product",method=RequestMethod.POST,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String buyProduct(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{
		String reqs = "";
		long uid=16l;
		//获取用户id
		if(checkUserIsLogin(null)){
			uid = getUserId();
		}else{
			uid = 0;
			LOG.error(CommentUtils.ERROR_USERNOTLOGIN);
			return RES_USER_NOTLOGIN;
		}
		//获取客户端json数据
		try {
			reqs = request.getParameter("req");
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(CommentUtils.ERROR_DATANOTGET);
			//return RES_DATA_NOTGET;
		}
		//调用manager方法获取返回数据
		String res = productManager.createProductOrder(reqs, uid);
		return res;
	}
	
	/**
	 * 支付宝通知信息--付款后
	 * @param request
	 * @param response
	 * @return
	 */
	//
	@RequestMapping(value="/product/notifyalipay.do",method=RequestMethod.POST,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String notifyTradeResult(HttpServletRequest request, HttpServletResponse response) {
		//获取客户端json数据
		Map requestParams = request.getParameterMap();
		LOG.info(requestParams);
		System.out.println("notifyTradeResult:"+requestParams);
		String res = productManager.notifyPayResult(requestParams);
		System.out.println(res);
		LOG.info(res);
		return res;
	}
	
	/**
	 * 微信返回支付结果信息
	 * @param request
	 * @param response
	 * @param body
	 * @return
	 */
	@RequestMapping(value="/product/notifywxpay.do",method=RequestMethod.POST,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String notifyWeixinPayResult(HttpServletRequest request, HttpServletResponse response,@RequestBody String body) {
		//PaycenterController.notifyxml=body;
		//获取客户端json数据
		//Map requestParams = request.getParameterMap();
		//LOG.info(requestParams);
		//System.out.println("notifyWeixinTradeResult:"+requestParams);
		//body = "<?xml   version=\"1.0\"   encoding=\"UTF8\"?><xml><appid><![CDATA[wx1441086740e20837]]></appid><attach><![CDATA[支付测试]]></attach><bank_type><![CDATA[CFT]]></bank_type><fee_type><![CDATA[CNY]]></fee_type><is_subscribe><![CDATA[Y]]></is_subscribe><mch_id><![CDATA[1231579302]]></mch_id><nonce_str><![CDATA[5d2b6c2a8db53831f7eda20af46e531c]]></nonce_str><openid><![CDATA[oUpF8uMEb4qRXf22hE3X68TekukE]]></openid><out_trade_no><![CDATA[a_100000000000191]]></out_trade_no><result_code><![CDATA[SUCCESS]]></result_code><return_code><![CDATA[SUCCESS]]></return_code><sign><![CDATA[B4E1768B110EFE52E4A741759350F7B6]]></sign><sub_mch_id><![CDATA[10000100]]></sub_mch_id><time_end><![CDATA[20140903131540]]></time_end><total_fee>1</total_fee><trade_type><![CDATA[JSAPI]]></trade_type><transaction_id><![CDATA[1004400740201409030005092168]]></transaction_id></xml>";
		String res = productManager.notifyWeixinPayResult(body);
		//paycenterManager.test();
		//System.out.println(res);
		LOG.info(res);
		return res;
	}
	/**
	 * 主动查询微信支付结果信息
	 */
	@RequestMapping(value="/product/querywxpay",method=RequestMethod.POST,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String queryWeixinTradeResult(HttpServletRequest request, HttpServletResponse response,@RequestBody String body) {
		String reqs = "";
		long uid = 0;
		//获取用户id
		if(checkUserIsLogin(null)){
			uid = getUserId();
		}else{
			uid = 0;
			return RES_USER_NOTLOGIN;
		}
		//获取客户端json数据
		try {
			reqs = request.getParameter("req");
			//System.out.println("queryWeixinTradeResult:"+reqs);
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(CommentUtils.ERROR_DATANOTGET);
			return RES_DATA_NOTGET;
		}
		//reqs = "{\"req\":{\"data\":{\"user_id\":\"15\",\"total_fee\":\"1000\",\"service\":\"com.pay\",\"reward_user_id\":\"15\"}}}";
		LOG.info(reqs);
		String res = productManager.queryWeixinPayResult(reqs, uid);
		System.out.println(res);
		LOG.info(res);
		return res;
	}
	
	//主动查询苹果支付是否成功
	@RequestMapping(value="/product/queryapplepay",method=RequestMethod.POST,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String queryApplePay(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{
		String reqs = "";
		long uid=16l;
		//获取用户id
		if(checkUserIsLogin(null)){
			uid = getUserId();
		}else{
			uid = 0;
			LOG.error(CommentUtils.ERROR_USERNOTLOGIN);
			return RES_USER_NOTLOGIN;
		}
		//获取客户端json数据
		try {
			reqs = request.getParameter("req");
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(CommentUtils.ERROR_DATANOTGET);
			//return RES_DATA_NOTGET;
		}
		//调用manager方法获取返回数据
		String res = productManager.IOSVerify(reqs, uid);
		return res;
	}
	
	/**
	 * @param request
	 * @param response
	 * @param res 客户端传过来的json数据
	 * @return
	 */
	@RequestMapping(value="/clientview/get_acc_gold",method=RequestMethod.POST,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String getAccGold(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{
		String reqs = "";
		long uid=16l;
		//获取用户id
		if(checkUserIsLogin(null)){
			uid = getUserId();
		}else{
			uid = 0;
			LOG.error(CommentUtils.ERROR_USERNOTLOGIN);
			return RES_USER_NOTLOGIN;
		}
		//获取客户端json数据
		try {
			reqs = request.getParameter("req");
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(CommentUtils.ERROR_DATANOTGET);
			//return RES_DATA_NOTGET;
		}
		//调用manager方法获取返回数据
		String res = productManager.getAccGold(reqs, uid);
		return res;
	}
	
	/**
	 * @param request
	 * @param response
	 * @param res 客户端传过来的json数据
	 * @return
	 */
	@RequestMapping(value="/clientaction/pay_story_chapter",method=RequestMethod.POST,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String payStoryChapter(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{
		String reqs = "";
		long uid=16l;
		//获取用户id
		if(checkUserIsLogin(null)){
			uid = getUserId();
		}else{
			//uid = 0;
			//LOG.error(CommentUtils.ERROR_USERNOTLOGIN);
			//return RES_USER_NOTLOGIN;
		}
		//获取客户端json数据
		try {
			reqs = request.getParameter("req");
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(CommentUtils.ERROR_DATANOTGET);
			//return RES_DATA_NOTGET;
		}
		//调用manager方法获取返回数据
		String res = productManager.payStoryChapter(reqs, uid);
		return res;
	}
}
