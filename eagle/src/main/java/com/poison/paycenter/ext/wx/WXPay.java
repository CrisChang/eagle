package com.poison.paycenter.ext.wx;

import java.util.HashMap;
import java.util.Map;

import com.poison.paycenter.ext.wx.business.DownloadBillBusiness;
import com.poison.paycenter.ext.wx.business.RefundBusiness;
import com.poison.paycenter.ext.wx.business.ScanPayBusiness;
import com.poison.paycenter.ext.wx.common.Configure;
import com.poison.paycenter.ext.wx.common.Signature;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import com.poison.paycenter.ext.wx.protocol.downloadbill_protocol.DownloadBillReqData;
import com.poison.paycenter.ext.wx.protocol.pay_protocol.ScanPayReqData;
import com.poison.paycenter.ext.wx.protocol.pay_query_protocol.ScanPayQueryReqData;
import com.poison.paycenter.ext.wx.protocol.refund_protocol.RefundReqData;
import com.poison.paycenter.ext.wx.protocol.refund_query_protocol.RefundQueryReqData;
import com.poison.paycenter.ext.wx.protocol.reverse_protocol.ReverseReqData;
import com.poison.paycenter.ext.wx.service.*;

/**
 * SDK总入口
 */
public class WXPay {

    /**
     * 初始化SDK依赖的几个关键配置
     * @param key 签名算法需要用到的秘钥
     * @param appID 公众账号ID
     * @param mchID 商户ID
     * @param sdbMchID 子商户ID，受理模式必填
     * @param certLocalPath HTTP证书在服务器中的路径，用来加载证书用
     * @param certPassword HTTP证书的密码，默认等于MCHID
     */
    public static void initSDKConfiguration(String key,String appID,String mchID,String sdbMchID,String certLocalPath,String certPassword){
        Configure.setKey(key);
        Configure.setAppID(appID);
        Configure.setMchID(mchID);
        Configure.setSubMchID(sdbMchID);
        Configure.setCertLocalPath(certLocalPath);
        Configure.setCertPassword(certPassword);
    }

    /**
     * 请求支付服务
     * @param scanPayReqData 这个数据对象里面包含了API要求提交的各种数据字段
     * @return API返回的数据
     * @throws Exception
     */
    public static String requestScanPayService(ScanPayReqData scanPayReqData) throws Exception{
        return new ScanPayService().request(scanPayReqData);
    }

    /**
     * 请求支付查询服务
     * @param scanPayQueryReqData 这个数据对象里面包含了API要求提交的各种数据字段
     * @return API返回的XML数据
     * @throws Exception
     */
	public static String requestScanPayQueryService(ScanPayQueryReqData scanPayQueryReqData) throws Exception{
		return new ScanPayQueryService().request(scanPayQueryReqData);
	}

    /**
     * 请求退款服务
     * @param refundReqData 这个数据对象里面包含了API要求提交的各种数据字段
     * @return API返回的XML数据
     * @throws Exception
     */
    public static String requestRefundService(RefundReqData refundReqData) throws Exception{
        return new RefundService().request(refundReqData);
    }

    /**
     * 请求退款查询服务
     * @param refundQueryReqData 这个数据对象里面包含了API要求提交的各种数据字段
     * @return API返回的XML数据
     * @throws Exception
     */
	public static String requestRefundQueryService(RefundQueryReqData refundQueryReqData) throws Exception{
		return new RefundQueryService().request(refundQueryReqData);
	}

    /**
     * 请求撤销服务
     * @param reverseReqData 这个数据对象里面包含了API要求提交的各种数据字段
     * @return API返回的XML数据
     * @throws Exception
     */
	public static String requestReverseService(ReverseReqData reverseReqData) throws Exception{
		return new ReverseService().request(reverseReqData);
	}

    /**
     * 请求对账单下载服务
     * @param downloadBillReqData 这个数据对象里面包含了API要求提交的各种数据字段
     * @return API返回的XML数据
     * @throws Exception
     */
    public static String requestDownloadBillService(DownloadBillReqData downloadBillReqData) throws Exception{
        return new DownloadBillService().request(downloadBillReqData);
    }

    /**
     * 直接执行被扫支付业务逻辑（包含最佳实践流程）
     * @param scanPayReqData 这个数据对象里面包含了API要求提交的各种数据字段
     * @param resultListener 商户需要自己监听被扫支付业务逻辑可能触发的各种分支事件，并做好合理的响应处理
     * @throws Exception
     */
    public static void doScanPayBusiness(ScanPayReqData scanPayReqData, ScanPayBusiness.ResultListener resultListener) throws Exception {
        new ScanPayBusiness().run(scanPayReqData, resultListener);
    }

    /**
     * 调用退款业务逻辑
     * @param refundReqData 这个数据对象里面包含了API要求提交的各种数据字段
     * @param resultListener 业务逻辑可能走到的结果分支，需要商户处理
     * @throws Exception
     */
    public static void doRefundBusiness(RefundReqData refundReqData, RefundBusiness.ResultListener resultListener) throws Exception {
        new RefundBusiness().run(refundReqData,resultListener);
    }

    /**
     * 运行退款查询的业务逻辑
     * @param refundQueryReqData 这个数据对象里面包含了API要求提交的各种数据字段
     * @param resultListener 商户需要自己监听被扫支付业务逻辑可能触发的各种分支事件，并做好合理的响应处理
     * @throws Exception
     */
    /*public static void doRefundQueryBusiness(RefundQueryReqData refundQueryReqData,RefundQueryBusiness.ResultListener resultListener) throws Exception {
        new RefundQueryBusiness().run(refundQueryReqData,resultListener);
    }*/

    /**
     * 请求对账单下载服务
     * @param downloadBillReqData 这个数据对象里面包含了API要求提交的各种数据字段
     * @param resultListener 商户需要自己监听被扫支付业务逻辑可能触发的各种分支事件，并做好合理的响应处理
     * @return API返回的XML数据
     * @throws Exception
     */
    public static void doDownloadBillBusiness(DownloadBillReqData downloadBillReqData,DownloadBillBusiness.ResultListener resultListener) throws Exception {
        new DownloadBillBusiness().run(downloadBillReqData,resultListener);
    }
    
    //对微信支付结果异步通知进行验证
    public static boolean verify(Map<String,Object> params){
    	if(params!=null && params.size()>0){
    		String sign = (String)params.get("sign");
    		params.put("sign", "");
    		params.remove("sign");
    		String checksign = Signature.getSign(params);
    		if(sign!=null && sign.trim().length()>0 && sign.equals(checksign)){
    			return true;
    		}
    	}
    	return false;
    }
    
    public static String changeObjectToXml(Object xmlObj){
    	 //解决XStream对出现双下划线的bug
        XStream xStreamForRequestPostData = new XStream(new DomDriver("UTF-8", new XmlFriendlyNameCoder("-_", "_")));

        //将要提交给API的数据对象转换成XML格式数据Post给API
        String xmlstr = xStreamForRequestPostData.toXML(xmlObj);
        
        return xmlstr;
    }
    
    public static void main(String args[]){
    	Map<String,String> resMap = new HashMap<String,String>();
    	resMap.put("return_code", "FAIL");
		resMap.put("return_msg", "签名失败");
    	String a = changeObjectToXml(resMap);
    	System.out.println(a);
    }

}
