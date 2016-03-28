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

import com.poison.eagle.manager.PaycenterManager;
import com.poison.eagle.utils.BaseController;
import com.poison.eagle.utils.CommentUtils;

/**
 * 
 * @author yan_dz
 *
 */
@Controller
public class PaycenterController extends BaseController{
	
	private static final  Log LOG = LogFactory.getLog(PaycenterController.class);
	
	private PaycenterManager paycenterManager;

	public void setPaycenterManager(PaycenterManager paycenterManager) {
		this.paycenterManager = paycenterManager;
	}	
	//
	/**
	 * 生成充值订单，并记录打赏明细
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	//
	@RequestMapping(value=CommentUtils.REQUEST_FROM_MOBLIE+"/clientview/reward_author",method=RequestMethod.POST, produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String createReloadOrderNumber(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		String reqs = "";
		long uid = 0;
		//获取用户id
		if(checkUserIsLogin()){
			uid = getUserId();
		}else{
			uid = 0;
			return RES_USER_NOTLOGIN;
		}
		//获取客户端json数据
		try {
			reqs = request.getParameter("req");
			System.out.println("createReloadOrderNumber:"+reqs);
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(CommentUtils.ERROR_DATANOTGET);
			return RES_DATA_NOTGET;
		}
		//reqs = "{\"req\":{\"data\":{\"user_id\":\"15\",\"total_fee\":\"1000\",\"service\":\"com.pay\",\"reward_user_id\":\"15\"}}}";
		LOG.info(reqs);
		String res = paycenterManager.createReloadOrderInfo(reqs, uid);
		System.out.println(res);
		LOG.info(res);
		return res;
	}
	/**
	 * 支付宝通知信息，修改打赏业务状态，并修改账户信息
	 * @param request
	 * @param response
	 * @return
	 */
	//
	@RequestMapping(value="/pay/notifyalipay.do",method=RequestMethod.POST,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String notifyTradeResult(HttpServletRequest request, HttpServletResponse response) {
		//获取客户端json数据
		Map requestParams = request.getParameterMap();
		LOG.info(requestParams);
		System.out.println("notifyTradeResult:"+requestParams);
		String res = paycenterManager.notifyPayResult(requestParams);
		System.out.println(res);
		LOG.info(res);
		return res;
	}
	
	
	/**
	 * 生成充值订单，并记录打赏明细 ---微信支付
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	//
	@RequestMapping(value=CommentUtils.REQUEST_FROM_MOBLIE+"/clientview/weixin_reward",method=RequestMethod.POST, produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String createWeixinReloadOrderNumber(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		String reqs = "";
		long uid = 0;
		//获取用户id
		if(checkUserIsLogin()){
			uid = getUserId();
		}else{
			uid = 0;
			return RES_USER_NOTLOGIN;
		}
		//获取客户端json数据
		try {
			reqs = request.getParameter("req");
			System.out.println("createWeixinReloadOrderNumber:"+reqs);
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(CommentUtils.ERROR_DATANOTGET);
			return RES_DATA_NOTGET;
		}
		//reqs = "{\"req\":{\"data\":{\"user_id\":\"15\",\"total_fee\":\"1000\",\"service\":\"com.pay\",\"reward_user_id\":\"15\"}}}";
		LOG.info(reqs);
		String res = paycenterManager.createWeixinReloadOrderInfo(reqs, uid);
		System.out.println(res);
		LOG.info(res);
		return res;
	}
	
	/**
	 * 微信支付结果异步通知信息，修改打赏业务状态，并修改账户信息
	 * @param request
	 * @param response
	 * @return
	 */
	//
	@RequestMapping(value="/pay/notifyweixinpay.do",method=RequestMethod.POST,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String notifyWeixinTradeResult(HttpServletRequest request, HttpServletResponse response,@RequestBody String body) {
		PaycenterController.notifyxml=body;
		//获取客户端json数据
		Map requestParams = request.getParameterMap();
		LOG.info(requestParams);
		System.out.println("notifyWeixinTradeResult:"+requestParams);
		//body = "<?xml   version=\"1.0\"   encoding=\"UTF8\"?><xml><appid><![CDATA[wx1441086740e20837]]></appid><attach><![CDATA[支付测试]]></attach><bank_type><![CDATA[CFT]]></bank_type><fee_type><![CDATA[CNY]]></fee_type><is_subscribe><![CDATA[Y]]></is_subscribe><mch_id><![CDATA[1231579302]]></mch_id><nonce_str><![CDATA[5d2b6c2a8db53831f7eda20af46e531c]]></nonce_str><openid><![CDATA[oUpF8uMEb4qRXf22hE3X68TekukE]]></openid><out_trade_no><![CDATA[a_100000000000191]]></out_trade_no><result_code><![CDATA[SUCCESS]]></result_code><return_code><![CDATA[SUCCESS]]></return_code><sign><![CDATA[B4E1768B110EFE52E4A741759350F7B6]]></sign><sub_mch_id><![CDATA[10000100]]></sub_mch_id><time_end><![CDATA[20140903131540]]></time_end><total_fee>1</total_fee><trade_type><![CDATA[JSAPI]]></trade_type><transaction_id><![CDATA[1004400740201409030005092168]]></transaction_id></xml>";
		String res = paycenterManager.notifyWeixinPayResult(body);
		//paycenterManager.test();
		System.out.println(res);
		LOG.info(res);
		return res;
	}
	
	/**
	 * 微信支付结果主动查询，修改打赏业务状态，并修改账户信息
	 * @param request
	 * @param response
	 * @return
	 */
	//
	@RequestMapping(value=CommentUtils.REQUEST_FROM_MOBLIE+"/clientview/queryweixinpay",method=RequestMethod.POST,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String queryWeixinTradeResult(HttpServletRequest request, HttpServletResponse response,@RequestBody String body) {
		String reqs = "";
		long uid = 0;
		//获取用户id
		if(checkUserIsLogin()){
			uid = getUserId();
		}else{
			uid = 0;
			return RES_USER_NOTLOGIN;
		}
		//获取客户端json数据
		try {
			reqs = request.getParameter("req");
			System.out.println("queryWeixinTradeResult:"+reqs);
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(CommentUtils.ERROR_DATANOTGET);
			return RES_DATA_NOTGET;
		}
		//reqs = "{\"req\":{\"data\":{\"user_id\":\"15\",\"total_fee\":\"1000\",\"service\":\"com.pay\",\"reward_user_id\":\"15\"}}}";
		LOG.info(reqs);
		String res = paycenterManager.queryWeixinPayResult(reqs, uid);
		System.out.println(res);
		LOG.info(res);
		return res;
	}
	
	/**
	 * 查询固定打赏金额
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	//
	@RequestMapping(value=CommentUtils.REQUEST_FROM_MOBLIE+"/clientview/getrewardfixedamount",method=RequestMethod.POST,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String getrewardfixedamount(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		String reqs = "";
		long uid = 0;
		//获取用户id
		if(checkUserIsLogin()){
			uid = getUserId();
		}else{
			uid = 0;
			return RES_USER_NOTLOGIN;
		}
		//获取客户端json数据
		try {
			reqs = request.getParameter("req");
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(CommentUtils.ERROR_DATANOTGET);
			return RES_DATA_NOTGET;
		}
		//reqs = "{\"req\":{\"data\":{\"user_id\":\"15\",\"user_type\":\"1\"}}}";
		LOG.info(reqs);
		String res = paycenterManager.getRewardFixedAmount(reqs);
		System.out.println(res);
		LOG.info(res);
		return res;
	}
	
	/**
	 * 查询打赏条数
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	//
	@RequestMapping(value=CommentUtils.REQUEST_FROM_MOBLIE+"/clientview/selectCountReward",method=RequestMethod.POST,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String selectCountReward(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		String reqs = "";
		long uid = 0;
		//获取用户id
		if(checkUserIsLogin()){
			uid = getUserId();
		}else{
			uid = 0;
			return RES_USER_NOTLOGIN;
		}
		//获取客户端json数据
		try {
			reqs = request.getParameter("req");
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(CommentUtils.ERROR_DATANOTGET);
			return RES_DATA_NOTGET;
		}
		//reqs = "{\"req\":{\"data\":{\"user_id\":\"15\",\"user_type\":\"1\"}}}";
		LOG.info(reqs);
		System.out.println("selectCountReward:" + reqs);
		String res = paycenterManager.selectCountReward(reqs);
		System.out.println(res);
		LOG.info(res);
		return res;
	}
	/**
	 * 查询打赏明细
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	//
	@RequestMapping(value=CommentUtils.REQUEST_FROM_MOBLIE+"/clientview/selectRewardDetail",method=RequestMethod.POST,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String selectRewardDetail(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		String reqs = "";
		long uid = 0;
		//获取用户id
		if(checkUserIsLogin()){
			uid = getUserId();
		}else{
			uid = 0;
			return RES_USER_NOTLOGIN;
		}
		//获取客户端json数据
		try {
			reqs = request.getParameter("req");
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(CommentUtils.ERROR_DATANOTGET);
			return RES_DATA_NOTGET;
		}
		//reqs = "{\"req\":{\"data\":{\"user_id\":\"15\",\"user_type\":\"1\"}}}";
		LOG.info(reqs);
		String res = paycenterManager.selectRewardDetail(reqs);
		System.out.println(res);
		LOG.info(res);
		return res;
	}
	
	/**
	 * 生成摇一摇金额等信息
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	//
	@RequestMapping(value=CommentUtils.REQUEST_FROM_MOBLIE+"/clientview/createRollInfo",method=RequestMethod.POST,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String createRollInfo(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		String reqs = "";
		long uid = 0;
		//获取用户id
		if(checkUserIsLogin()){
			uid = getUserId();
		}else{
			uid = 0;
			return RES_USER_NOTLOGIN;
		}
		//获取客户端json数据
		try {
			reqs = request.getParameter("req");
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(CommentUtils.ERROR_DATANOTGET);
			return RES_DATA_NOTGET;
		}
		//reqs = "{\"req\":{\"data\":{\"user_id\":\"15\",\"user_name\":\"yinnan\",\"reward_user_id\":\"15\",\"reward_user_name\":\"yinnan\"}}}";
		LOG.info(reqs);
		String res = paycenterManager.createRollInfo(reqs,uid);
		System.out.println(res);
		LOG.info(res);
		return res;
	}
	
	/**
	 * 摇一摇打赏成功返回信息
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	//
	@RequestMapping(value=CommentUtils.REQUEST_FROM_MOBLIE+"/clientview/backRollSuccess",method=RequestMethod.POST,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String backRollSuccess(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		String reqs = "";
		long uid = 0;
		//获取用户id
		if(checkUserIsLogin()){
			uid = getUserId();
		}else{
			uid = 0;
			return RES_USER_NOTLOGIN;
		}
		//获取客户端json数据
		try {
			reqs = request.getParameter("req");
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(CommentUtils.ERROR_DATANOTGET);
			return RES_DATA_NOTGET;
		}
		//reqs = "{\"req\":{\"data\":{\"user_id\":\"15\",\"user_name\":\"yinnan\",\"reward_id\":\"113846530175270912\",\"postscript\":\"yinnan\"}}}";
		LOG.info(reqs);
		System.out.println(reqs);
		String res = paycenterManager.backRollSuccess(reqs);
		System.out.println(reqs);
		LOG.info(res);
		return res;
	}
	
	//===================以下是提现相关代码========================================
	//查询是否设置了提现密码==============
	//设置提现密码=================
	//修改提现密码=====================
	//提交提现订单===========================
	//查询提现记录列表==========================
	//查询当月是否已经申请过提现=============================
	//查询账户余额=======================
	/**
	 * 查询是否设置了提现密码
	 */
	@RequestMapping(value=CommentUtils.REQUEST_FROM_MOBLIE+"/clientview/getTakePasswordStatus",method=RequestMethod.POST,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String getTakePasswordStatus(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		String reqs = "";
		long uid = 0;
		//获取用户id
		if(checkUserIsLogin()){
			uid = getUserId();
		}else{
			uid = 0;
			return RES_USER_NOTLOGIN;
		}
		//获取客户端json数据
		try {
			reqs = request.getParameter("req");
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(CommentUtils.ERROR_DATANOTGET);
			return RES_DATA_NOTGET;
		}
		//reqs = "{\"req\":{\"data\":{\"user_id\":\"15\",\"user_type\":\"1\"}}}";
		LOG.info(reqs);
		String res = paycenterManager.getTakePasswordStatus(reqs, uid);
		System.out.println(res);
		LOG.info(res);
		return res;
	}
	
	/**
	 * 设置提现密码
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	//
	@RequestMapping(value=CommentUtils.REQUEST_FROM_MOBLIE+"/clientview/setTakePassword",method=RequestMethod.POST,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String setTakePassword(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		String reqs = "";
		long uid = 0;
		//获取用户id
		if(checkUserIsLogin()){
			uid = getUserId();
		}else{
			uid = 0;
			return RES_USER_NOTLOGIN;
		}
		//获取客户端json数据
		try {
			reqs = request.getParameter("req");
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(CommentUtils.ERROR_DATANOTGET);
			return RES_DATA_NOTGET;
		}
		//reqs = "{\"req\":{\"data\":{\"user_id\":\"15\",\"user_type\":\"1\"}}}";
		LOG.info(reqs);
		String res = paycenterManager.setTakePassword(reqs, uid);
		System.out.println(res);
		LOG.info(res);
		return res;
	}
	/**
	 * 修改提现密码
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	//
	@RequestMapping(value=CommentUtils.REQUEST_FROM_MOBLIE+"/clientview/updateTakePassword",method=RequestMethod.POST,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String updateTakePassword(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		String reqs = "";
		long uid = 0;
		//获取用户id
		if(checkUserIsLogin()){
			uid = getUserId();
		}else{
			uid = 0;
			return RES_USER_NOTLOGIN;
		}
		//获取客户端json数据
		try {
			reqs = request.getParameter("req");
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(CommentUtils.ERROR_DATANOTGET);
			return RES_DATA_NOTGET;
		}
		//reqs = "{\"req\":{\"data\":{\"user_id\":\"15\",\"user_type\":\"1\"}}}";
		LOG.info(reqs);
		String res = paycenterManager.updateTakePassword(reqs, uid);
		System.out.println(res);
		LOG.info(res);
		return res;
	}
	/**
	 * 申请当月是否超过提现次数
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	//
	@RequestMapping(value=CommentUtils.REQUEST_FROM_MOBLIE+"/clientview/getTakeStatus",method=RequestMethod.POST,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String getTakeStatus(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		String reqs = "";
		long uid = 0;
		//获取用户id
		if(checkUserIsLogin()){
			uid = getUserId();
		}else{
			uid = 0;
			return RES_USER_NOTLOGIN;
		}
		//获取客户端json数据
		try {
			reqs = request.getParameter("req");
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(CommentUtils.ERROR_DATANOTGET);
			return RES_DATA_NOTGET;
		}
		//reqs = "{\"req\":{\"data\":{\"user_id\":\"15\",\"user_type\":\"1\"}}}";
		LOG.info(reqs);
		String res = paycenterManager.getTakeStatus(reqs, uid);
		System.out.println(res);
		LOG.info(res);
		return res;
	}
	/**
	 * 申请提现
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	//
	@RequestMapping(value=CommentUtils.REQUEST_FROM_MOBLIE+"/clientview/applyTake",method=RequestMethod.POST,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String applyTake(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		String reqs = "";
		long uid = 0;
		//获取用户id
		if(checkUserIsLogin()){
			uid = getUserId();
		}else{
			uid = 0;
			return RES_USER_NOTLOGIN;
		}
		//获取客户端json数据
		try {
			reqs = request.getParameter("req");
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(CommentUtils.ERROR_DATANOTGET);
			return RES_DATA_NOTGET;
		}
		//reqs = "{\"req\":{\"data\":{\"user_id\":\"15\",\"user_type\":\"1\"}}}";
		LOG.info(reqs);
		String res = paycenterManager.saveTakeRecord(reqs, uid);
		System.out.println(res);
		LOG.info(res);
		return res;
	}
	/**
	 * 查询账户余额
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	//
	@RequestMapping(value=CommentUtils.REQUEST_FROM_MOBLIE+"/clientview/getAccAmt",method=RequestMethod.POST,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String getAccAmt(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		String reqs = "";
		long uid = 0;
		//获取用户id
		if(checkUserIsLogin()){
			uid = getUserId();
		}else{
			uid = 0;
			return RES_USER_NOTLOGIN;
		}
		//获取客户端json数据
		try {
			reqs = request.getParameter("req");
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(CommentUtils.ERROR_DATANOTGET);
			return RES_DATA_NOTGET;
		}
		//reqs = "{\"req\":{\"data\":{\"user_id\":\"15\",\"user_type\":\"1\"}}}";
		LOG.info(reqs);
		String res = paycenterManager.getAccAmt(reqs, uid);
		System.out.println(res);
		LOG.info(res);
		return res;
	}
	/**
	 * 查询提现记录列表
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	//
	@RequestMapping(value=CommentUtils.REQUEST_FROM_MOBLIE+"/clientview/getAccTakeRecords",method=RequestMethod.POST,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String getAccTakeRecords(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		String reqs = "";
		long uid = 0;
		//获取用户id
		if(checkUserIsLogin()){
			uid = getUserId();
		}else{
			uid = 0;
			return RES_USER_NOTLOGIN;
		}
		//获取客户端json数据
		try {
			reqs = request.getParameter("req");
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(CommentUtils.ERROR_DATANOTGET);
			return RES_DATA_NOTGET;
		}
		//reqs = "{\"req\":{\"data\":{\"user_id\":\"15\",\"user_type\":\"1\"}}}";
		LOG.info(reqs);
		String res = paycenterManager.getAccTakeRecords(reqs, uid);
		System.out.println(res);
		LOG.info(res);
		return res;
	}
	
	
	public static String payreturnxml = "";
	public static String notifyxml = "";
	public static String queryreturnxml = "";
	//返回微信的返回数据
	@RequestMapping(value=CommentUtils.REQUEST_FROM_MOBLIE+"/clientview/weixinpaytest",produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String weixinpaytest(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		return "payreturnxml:"+payreturnxml+"\r\nnotifyxml:"+notifyxml+"\r\nqueryreturnxml:"+queryreturnxml;
	}
}
