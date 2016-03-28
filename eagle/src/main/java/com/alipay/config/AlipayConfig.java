package com.alipay.config;

import java.util.HashMap;
import java.util.Map;

import com.poison.paycenter.constant.PaycenterConstant;

/* *
 *类名：AlipayConfig
 *功能：基础配置类
 *详细：设置帐户有关信息及返回路径
 *版本：3.3
 *日期：2012-08-10
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
	
 *提示：如何获取安全校验码和合作身份者ID
 *1.用您的签约支付宝账号登录支付宝网站(www.alipay.com)
 *2.点击“商家服务”(https://b.alipay.com/order/myOrder.htm)
 *3.点击“查询合作者身份(PID)”、“查询安全校验码(Key)”

 *安全校验码查看时，输入支付密码后，页面呈灰色的现象，怎么办？
 *解决方法：
 *1、检查浏览器配置，不让浏览器做弹框屏蔽设置
 *2、更换浏览器或电脑，重新登录查询。
 */

public class AlipayConfig {
	
	//↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
	// 合作身份者ID，以2088开头由16位纯数字组成的字符串
	public static String partner = "2088711548442079";
	// 商户的私钥
	public static String private_key = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAKqFlooLxtAhZ4Au3Xlca1LJ3ranFMzYehheHn39c8+g09MwPynu8Oyeqw0KBi8ypAn15NfHurEPyq5TNg4utxpJvRTAkBJYtBhOeXIZ0NIazpleR2KJZmXkBasns9pMdxKAgSH+WNP0KpiNF0/ftDlRIbzgRqXu106l8M4uAY+HAgMBAAECgYAJBAEekVrUnD+FpnStOvatrOCqlZDi97sU2L0R+jmKS72HV/Cu/eQdDpKKoPIMwsergyWzRusW+3fC2cFs5Ilns0jJ7+lYa/TnBYGRLiArkLKM1ymesCisEuYa5bh57v9iYmF8Q5088JhSzXTUI44AlCpjBxkKaoCbP0zecblMSQJBANwXReA+nY3Lk0CmA8A/+atsFpam7k8q9od+Zd48ZDeqtplRFviS51Fy+QnzCIycoM/ii2jcKymUnitQKXw3F8sCQQDGV+ujazdf51bZB+kEIfjOF7PurRlLpEGDOqugQABtNg4yaKmjnwWLy2Td0l+tXXpZR4cPPNtLNNFv5/VBb5e1AkATEzFLLiIZl/s7LyhEEUdiRAvxIqak1eRn+dx6VmGZMYKB5zVGdfT2ajCLXDTxeuG1Ir1SaUKUZ/0NY/U0ftHhAkEAnU+gUhW4brUiGbJ/asqy9mqwSE48MNVPLJD9DHWWoQftjKecXGzEzlJ8Zr7zKaJpZqQnh7thxq1Wq2YTDFpAvQJAfikgWOWf1H0U5T9HZXq0ih6wuGQqPCIdfZhhlAMlrqZ4nvyXUG6t/4ZMKj97w1xfFjL8zGKNG7hW3IuhMOuzyQ==";
									  	
	// 支付宝的公钥，无需修改该值
	public static String ali_public_key  = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";
											
	//↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
	

	// 调试用，创建TXT日志文件夹路径
	public static String log_path = "D:\\";

	// 字符编码格式 目前支持 gbk 或 utf-8
	public static String input_charset = "utf-8";
	
	// 签名方式 不需修改
	public static String sign_type = "RSA";

}
