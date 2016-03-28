package com.poison.eagle.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import org.apache.commons.lang.time.DateUtils;

import com.poison.paycenter.constant.PaycenterConstant;

/**
 * 金额转换类
 * @author Administrator
 *
 */
public class PaycenterUtil {

	private static String msg;
	
	/**
	 * 分转换为元
	 * @param fen
	 * @return
	 */
	public static String fenToYuan(int fen){
		float fens = fen;
		float fenss = fens/100;
		String str = fenss+"";
		return str;		
	}
	
	/**元转换为分
	 * @param yuan
	 * @return
	 */
	public static int yuanToFen(String yuan){
		float a = Float.valueOf(yuan);
		float b = a*100;
		int c = (int)b;
		return c;
	}
	
	/**
	 * 随机产生200分以内的数字
	 * @return
	 */
	public static int randomFen() {
		int i;
		Random random = new Random();
		i = random.nextInt(getIntAmount())+getFloatAmount();
		return i;
	}
	
	private static int getIntAmount() {		
		return Integer.valueOf(getResultMessage("intAmount"));		
	}
	private static int getFloatAmount() {		
		return Integer.valueOf(getResultMessage("floatAmount"));		
	}
	
	public static int getLimitAmount() {
		return Integer.valueOf(getResultMessage("dayLimitAmount"));
	}
	
	
	public static String getResultMessage(String keys){
		
		String key = String.valueOf(keys);
		InputStream is = null;
		try {
			is = MessageUtils.class.getResourceAsStream("/com/poison/eagle/utils/paycenterInfo.properties"); 
			Properties p = new Properties();
			p.load(is);
			msg = p.getProperty(key);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(is != null){
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		if(msg== null){
			msg = CommentUtils.ERROR_NOTUNKOWN;
		}
		return msg;
	}
	
	public static String getOrderNumberById(long id) {
		String orderNumber = getResultMessage("orderStr") + id;	
		return orderNumber;	
	}
	
	public static String str = "a_";
	
	public static void main2(String[] args) {
		System.out.println(getLimitAmount());
		System.out.println(DateUtils.addDays(new Date(), 1));
		
		String time = "2014-12-18 00:00:00";
		System.out.println(DateUtil.format(System.currentTimeMillis(), "yyyy-MM-dd"));
		System.out.println(DateUtil.formatLong(time, "yyyy-MM-dd hh:mm:ss"));
	}
	
	public static long getIdByOrderNumber(String orderNumber) {		
		return Long.valueOf(orderNumber.substring(getResultMessage("orderStr").length()));
	}
		
	public static String getPartner() {
		return getResultMessage("partner");		
	}
	
	public static String getAlipyNumber() {
		return getResultMessage("alipyNumber");		
	}
	
	
	public static String getAplipayValue(int key) {
		Map<Object, String> map = new HashMap<Object, String>();
		map.put(PaycenterConstant.COMPANY_ACC_ID_1, "86778115@qq.com");
		return map.get(key);
	}
	
	public static int getAplipayValue(String key) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(getAlipyNumber(),PaycenterConstant.COMPANY_ACC_ID_1);
		return (Integer) map.get(key);
	}
	
	public static int getPartnerIdInt(String number) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(getPartner(),PaycenterConstant.COMPANY_SIGN_ID_1);
		return (Integer) map.get(number);
	}
	
	public static long getNextDate() {
		long sysdate = System.currentTimeMillis();
		System.out.println(DateUtils.addDays(new Date(), 1));
		return sysdate;
	}
	
	/**
	 * 生成一个订单号
	 */
	public static String generateOrderNo(){
		String orderno = System.currentTimeMillis()+"";
		Random r = new Random();
		for(int i=0;i<7;i++){
			orderno = orderno+r.nextInt(10);
		}
		return orderno;
	}
}
