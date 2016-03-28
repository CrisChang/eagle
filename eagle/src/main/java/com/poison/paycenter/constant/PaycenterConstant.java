package com.poison.paycenter.constant;

public class PaycenterConstant {
	
	/**
	 * 支付宝异步通知的url--打赏使用
	 */
	public static String NOTIFY_URL = "http://www.duyao001.om/pay/notifyalipay.do";
	/**
	 * 支付宝异步通知的url--购买商品使用
	 */
	public static String NOTIFY_URL_PRODUCT = "http://www.duyao001.om/product/notifyalipay.do";
	
	/**
	 * 
	 */
	public static final String TRADE_FINISHED = "TRADE_FINISHED";
	
	/**
	 * 
	 */
	public static final String TRADE_CLOSED = "TRADE_CLOSED";
	
	/**
	 * 
	 */
	public static final String TRADE_SUCCESS = "TRADE_SUCCESS";
	
	/**
	 * 
	 */
	public static final String WAIT_BUYER_PAY = "WAIT_BUYER_PAY";

	/**
	 * 商品名称
	 */
	public static final String SUBJECT = "reward";
	
	/**
	 * 支付类型，默认为1，商品购买
	 */
	public static final String PAYMENT_TYPE = "1";
	
	/**
	 * 商品详情
	 */
	public static final String BODY = "duyaoReward";

	public static final int ROLL_COUNT = 100;
	
	/**
	 * 请求/响应  1--请求
	 */
	public static int PAY_MARK_1 = 1;
	
	/**
	 * 请求/响应  2--响应
	 */
	public static int PAY_MARK_2 = 2;
	
	/**
	 * 交易记录状态 1--初始状态
	 */
	public static int LOG_STATUS_1 = 1;
	
	/**
	 * 交易记录状态 3--失败
	 */
	public static int LOG_STATUS_3 = 3;
	/**
	 * 交易记录状态 2--成功
	 */
	public static int LOG_STATUS_2 = 2;

	/**
	 * 企业账号 1--账号1（如支付宝账号XXX）
	 */
	public static int COMPANY_ACC_ID_1 = 1;
	
	/**
	 * 企业账号 2--账号2（如支付宝账号XXX）
	 */
	public static int COMPANY_ACC_ID_2 = 2;

	/**
	 * 企业在第三方签约ID 1--签约ID1（如在支付宝签约的唯一ID）
	 */
	public static int COMPANY_SIGN_ID_1 = 1;
	
	/**
	 * 企业在第三方签约ID 2--签约ID2（如在支付宝签约的唯一ID）
	 */
	public static int COMPANY_SIGN_ID_2 = 2;
	
	/**
	 * 交易状态 1--成功
	 */
	public static int TRADE_STATUS_SUCESS = 1;
	
	/**
	 * 交易状态 2--失败
	 */
	public static int TRADE_STATUS_FAIL = 2;
	
	/**
	 * 交易状态3--等待处理
	 */
	public static int TRADE_STATUS_PROCESSING = 3;
	
	/**
	 * 支付方式 1--支付宝
	 */
	public static int PAYMENT_TYPE_1 = 1;
	
	/**
	 * 支付方式 2--支付宝网银
	 */
	public static int PAYMENT_TYPE_2 = 2;
	
	/**
	 * 支付方式 3--微信支付
	 */
	public static int PAYMENT_TYPE_3 = 3;
	
	/**
	 * 支付方式 4--银联支付
	 */
	public static int PAYMENT_TYPE_4 = 4;
	
	/**
	 * 支付方式 9--其他
	 */
	public static int PAYMENT_TYPE_9 = 9;
	
	/**
	 * 交易类型 1--充值
	 */
	public static int TRADE_TYPE_1 = 1;
	
	/**
	 * 交易类型 2--付款
	 */
	public static int TRADE_TYPE_2 = 2;
	
	/**
	 * 交易类型3--收款
	 */
	public static int TRADE_TYPE_3 = 3;
	
	/**
	 * 交易类型4--转账
	 */
	public static int TRADE_TYPE_4 = 4;
	
	/**
	 * 交易状态1--成功
	 */
	public static int TRADE_STATUS_1 = 1;
	
	/**
	 * 交易状态2--失败
	 */
	public static int TRADE_STATUS_2 = 2;
	
	/**
	 * 交易状态3--待付
	 */
	public static int TRADE_STATUS_3 = 3;
	
	/**
	 * 交易状态4--待收
	 */
	public static int TRADE_STATUS_4 = 4;
	
	/**
	 * 隐藏标识0--正常
	 */
	public static int SHADOW_0 = 0;
	
	/**
	 * 隐藏标识1--系统隐藏
	 */
	public static int SHADOW_1 = 1;
	
	/**
	 * 打赏状态1--打赏成功
	 */
	public static int REWARD_STATUS_1 = 1;
	
	/**
	 * 打赏状态2--打赏失败
	 */
	public static int REWARD_STATUS_2 = 2;
	
	/**
	 * 打赏状态3--等待打赏
	 */
	public static int REWARD_STATUS_3 = 3;
	
	/**
	 * 用户类型 -- 打赏人
	 */
	public static int USER_TYPE_1 = 1;
	
	/**
	 * 用户类型 -- 被打赏人
	 */
	public static int USER_TYPE_2 = 2;
	
	/**
	 * 打赏种类1--用户充值打赏
	 */
	public static int REWARD_TYPE_1 = 1;
	
	/**
	 * 打赏种类2--摇一摇打赏
	 */
	public static int REWARD_TYPE_2 = 2;
	/**
	 * 打赏种类3--一键打赏(固定金额)
	 */
	public static int REWARD_TYPE_3 = 3;
	/**
	 * 打赏种类 4--自动打赏
	 */
	public static int REWARD_TYPE_4 = 4;
	
	/**
	 * 打赏金额上限
	 */
	public static int LIMIT_AMOUNT = 2000000;
	
	/**
	 * 1--书评
	 */
	public static int SOURCE_TYPE_1 = 1;
	
	/**
	 * 2--影评
	 */
	public static int SOURCE_TYPE_2 = 2;
	
	/**
	 * 3--文章
	 */
	public static int SOURCE_TYPE_3 = 3;
	
	/**
	 * 4--晒图
	 */
	public static int SOURCE_TYPE_4 = 4;
}
