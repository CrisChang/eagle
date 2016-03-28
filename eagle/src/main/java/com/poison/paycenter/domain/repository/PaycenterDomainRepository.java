package com.poison.paycenter.domain.repository;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.alipay.config.AlipayConfig;
import com.alipay.sign.RSA;
import com.alipay.util.AlipayCore;
import com.keel.common.event.rocketmq.RocketProducer;
import com.poison.eagle.action.PaycenterController;
import com.poison.eagle.manager.UserJedisManager;
import com.poison.eagle.manager.otherinterface.PushManager;
import com.poison.eagle.utils.DateUtil;
import com.poison.eagle.utils.JedisConstant;
import com.poison.eagle.utils.PaycenterUtil;
import com.poison.eagle.utils.ResultUtils;
import com.poison.eagle.utils.StringUtils;
import com.poison.paycenter.constant.PaycenterConstant;
import com.poison.paycenter.constant.ReturnMapUtil;
import com.poison.paycenter.dao.AccAmtDao;
import com.poison.paycenter.dao.AccSetDao;
import com.poison.paycenter.dao.AccTakeRecordDao;
import com.poison.paycenter.dao.AccTakeSetDao;
import com.poison.paycenter.dao.AmtRecordDao;
import com.poison.paycenter.dao.CompanyAccountDao;
import com.poison.paycenter.dao.CompanyAccountLogDao;
import com.poison.paycenter.dao.ControlUserStatusDao;
import com.poison.paycenter.dao.LegendHeroDao;
import com.poison.paycenter.dao.PayLogDao;
import com.poison.paycenter.dao.PayRecordDao;
import com.poison.paycenter.dao.RewardDetailDao;
import com.poison.paycenter.dao.RewardPesonStatisticalDao;
import com.poison.paycenter.dao.RewardStatisticalDao;
import com.poison.paycenter.ext.wx.WXPay;
import com.poison.paycenter.ext.wx.common.Configure;
import com.poison.paycenter.ext.wx.common.RandomStringGenerator;
import com.poison.paycenter.ext.wx.common.Signature;
import com.poison.paycenter.ext.wx.common.XMLParser;
import com.poison.paycenter.ext.wx.protocol.pay_protocol.ScanPayReqData;
import com.poison.paycenter.model.AccAmt;
import com.poison.paycenter.model.AccSet;
import com.poison.paycenter.model.AccTakeRecord;
import com.poison.paycenter.model.AccTakeSet;
import com.poison.paycenter.model.AmtRecord;
import com.poison.paycenter.model.CompanyAccount;
import com.poison.paycenter.model.CompanyAccountLog;
import com.poison.paycenter.model.ControlUserStatus;
import com.poison.paycenter.model.LegendHero;
import com.poison.paycenter.model.PayLog;
import com.poison.paycenter.model.PayRecord;
import com.poison.paycenter.model.RewardDetail;
import com.poison.paycenter.model.RewardPesonStatistical;
import com.poison.paycenter.model.RewardStatistical;
import com.poison.ucenter.dao.UserInfoDAO;
import com.poison.ucenter.model.UserInfo;

/**
 * @author yan_dz
 *
 */
public class PaycenterDomainRepository {
	
	private static final Log LOG = LogFactory.getLog(PaycenterDomainRepository.class);
	
	
	private int sellerId = PaycenterConstant.COMPANY_ACC_ID_1;				//判断0代表一个支付宝账号
	private int parterId = PaycenterConstant.PAYMENT_TYPE_1;            //企业支付宝身份ID
	
	private TransactionTemplate paycenterMysqlTransactionTemplate;	
	private AccAmtDao accAmtDao;
	private AmtRecordDao amtRecordDao;
	private PayLogDao payLogDao;
	private PayRecordDao payRecordDao;
	private RewardDetailDao rewardDetailDao;
	private CompanyAccountDao companyAccountDao;
	private CompanyAccountLogDao companyAccountLogDao;
	private ControlUserStatusDao controlUserStatusDao;
	private RewardStatisticalDao rewardStatisticalDao;
	private RewardPesonStatisticalDao rewardPesonStatisticalDao;
	private String notify_url;
	private String dayLimitCount;
	private int pageSize;
	private PushManager pushManager;	
	private UserJedisManager userJedisManager;
	private AccTakeRecordDao accTakeRecordDao;
	private AccTakeSetDao accTakeSetDao;
	private RocketProducer eagleProducer;
	private AccSetDao accSetDao;
	private LegendHeroDao legendHeroDao;
	private UserInfoDAO userInfoDAO;
	
	
	public void setEagleProducer(RocketProducer eagleProducer) {
		this.eagleProducer = eagleProducer;
	}

	public void setUserJedisManager(UserJedisManager userJedisManager) {
		this.userJedisManager = userJedisManager;
	}

	public void setPushManager(PushManager pushManager) {
		this.pushManager = pushManager;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public void setRewardPesonStatisticalDao(
			RewardPesonStatisticalDao rewardPesonStatisticalDao) {
		this.rewardPesonStatisticalDao = rewardPesonStatisticalDao;
	}

	public void setRewardStatisticalDao(RewardStatisticalDao rewardStatisticalDao) {
		this.rewardStatisticalDao = rewardStatisticalDao;
	}

	public void setNotify_url(String notify_url) {
		this.notify_url = notify_url;
	}

	public void setDayLimitCount(String dayLimitCount) {
		this.dayLimitCount = dayLimitCount;
	}

	public void setCompanyAccountLogDao(CompanyAccountLogDao companyAccountLogDao) {
		this.companyAccountLogDao = companyAccountLogDao;
	}

	public void setControlUserStatusDao(ControlUserStatusDao controlUserStatusDao) {
		this.controlUserStatusDao = controlUserStatusDao;
	}

	public void setCompanyAccountDao(CompanyAccountDao companyAccountDao) {
		this.companyAccountDao = companyAccountDao;
	}

	public void setAccAmtDao(AccAmtDao accAmtDao) {
		this.accAmtDao = accAmtDao;
	}

	public void setAmtRecordDao(AmtRecordDao amtRecordDao) {
		this.amtRecordDao = amtRecordDao;
	}

	public void setPayLogDao(PayLogDao payLogDao) {
		this.payLogDao = payLogDao;
	}

	public void setPayRecordDao(PayRecordDao payRecordDao) {
		this.payRecordDao = payRecordDao;
	}

	public void setRewardDetailDao(RewardDetailDao rewardDetailDao) {
		this.rewardDetailDao = rewardDetailDao;
	}
	
	public void setPaycenterMysqlTransactionTemplate(
			TransactionTemplate paycenterMysqlTransactionTemplate) {
		this.paycenterMysqlTransactionTemplate = paycenterMysqlTransactionTemplate;
	}

	public void setAccTakeRecordDao(AccTakeRecordDao accTakeRecordDao) {
		this.accTakeRecordDao = accTakeRecordDao;
	}

	public void setAccTakeSetDao(AccTakeSetDao accTakeSetDao) {
		this.accTakeSetDao = accTakeSetDao;
	}
	public void setAccSetDao(AccSetDao accSetDao) {
		this.accSetDao = accSetDao;
	}

	/**
	 * 账户资金变更记录表
	 * @param paylog
	 * @param payRecord 
	 * @return1111
	 */
	private AmtRecord getParametersAmtRecord(PayLog paylog, long serialId, int tradeType, int tradeStatus, int tradeAmount) {
		AmtRecord amtRecord = new AmtRecord();
		amtRecord.setUserId(paylog.getUserId());
		amtRecord.setTradeType(tradeType);
		amtRecord.setSerialId(serialId);
		amtRecord.setOutTradeNo(paylog.getOutTradeNo());
		amtRecord.setTradeAmount(tradeAmount);
		amtRecord.setTradeTime(paylog.getLogCreate());
		amtRecord.setTradeStatus(tradeStatus);
		amtRecord.setTradeDesc("");
		return amtRecord;
	}

	/**
	 * 账户资金表
	 * @param paylog
	 * @return
	 */

	/**
	 * @param paylog
	 * @param pAY_MARK_1
	 * @param lOG_STATUS_2
	 * @param id
	 * @return
	 */
	private PayLog getParametersPayLog1(PayLog paylog, int payMark, int logStatus) {
		paylog.setPayMark(payMark);
		paylog.setLogStatus(logStatus);
		paylog.setSellerId(sellerId);
		paylog.setParterId(parterId);
		return paylog;
	}


	/**
	 * 支付宝异步通知支付结果
	 * @param paylog
	 * @return
	 */
	public Map<String, String> notifyResult(final PayLog paylog) {
		LOG.info("PaycenterDomainRepository.notifyResult:"+paylog);
		//充值
		Map<String, String> resMap = payment(paylog);
		if (Integer.valueOf(resMap.get("flag")) != ResultUtils.SUCCESS) {
			return resMap;
		}
		Map<String, String> resMap1 = order(paylog);
		
		return resMap1;
		
	}

	public Map<String, String> order(final PayLog paylog) {
		final Map<String, String> map = new HashMap<String, String>();
		paycenterMysqlTransactionTemplate.execute(new TransactionCallback<Map<String, String>>() {
			@Override
			public Map<String, String> doInTransaction(TransactionStatus status) {
				int flag = 1000;
				try {		
					final long sysdate = System.currentTimeMillis();				
					/**
					 * 处理打赏，首先查询原打赏信息，获取打赏人和被打赏人及打赏金额，
					 * 将打赏业务表中打赏状态改为成功，然后扣除账户表中打赏人的账户余额，
					 * 增加被打赏人的打赏余额，同时各自生成一条账户资金变更记录
					 */
					//1修改打赏业务表状态
					String outTradeNo = paylog.getOutTradeNo();
					RewardDetail rewardDetail = rewardDetailDao.selectRewardDetailInfoByUserId(outTradeNo);
					if (rewardDetail.getFlag() != ResultUtils.SUCCESS) {
						map.put("flag", String.valueOf(rewardDetail.getFlag()));
						return map;
					}
					long userId = rewardDetail.getSendUserId();				//fukuanren
					long recvId = rewardDetail.getReceiveUserId();			//收款人
					int rewardAmt = rewardDetail.getSendAmt();				//打赏金额
					rewardDetail.setSendStatus(PaycenterConstant.REWARD_STATUS_1);					
					flag = rewardDetailDao.updateRewardDetail(rewardDetail);
					if (flag != ResultUtils.SUCCESS) {
						status.setRollbackOnly();	
						map.put("flag", String.valueOf(flag));
						return map;
					}
					//2修改账户表中的付款人账户金额
					Map<String, Object> accAmtMap1 = new HashMap<String, Object>();
					accAmtMap1.put("sendId", userId);
					accAmtMap1.put("sendAmt", rewardAmt);
					accAmtMap1.put("sysdate", sysdate);
					flag = accAmtDao.updateAccAmtForPayer(accAmtMap1);
					if (flag != ResultUtils.SUCCESS) {
						status.setRollbackOnly();	
						map.put("flag", String.valueOf(flag));
						return map;
					}
					//3同时记录一条付款人账户变更日志
					AmtRecord amtRecord1 = getParametersAmtRecord(paylog, rewardDetail.getId(), PaycenterConstant.TRADE_TYPE_2, PaycenterConstant.TRADE_STATUS_1, rewardAmt);
					flag = amtRecordDao.insertAmtRecord(amtRecord1);
					if (flag != ResultUtils.SUCCESS) {
						status.setRollbackOnly();	
						map.put("flag", String.valueOf(flag));
						return map;
					}
					//4查询账户表中用户是否存在，如果存在，将收款人的打赏余额和本次金额相加，如果没有，生成一条账户信息
					AccAmt accAmt2 = accAmtDao.selectAccAmtInfoByUserId(recvId);	
					int rewardAmount;
					if (accAmt2.getFlag()==ResultUtils.SUCCESS) {
						Map<String, Object> accAmtMap = new HashMap<String, Object>();
						accAmtMap.put("userId", recvId);
						accAmtMap.put("rewardAmt", rewardAmt);
						accAmtMap.put("sysdate", sysdate);
						flag = accAmtDao.updateAccAmt2(accAmtMap);
					}else if(accAmt2.getFlag()==ResultUtils.DATAISNULL){
						AccAmt accAmt3 = new AccAmt();
						rewardAmount = rewardAmt;
						accAmt3 = getParametersAccAmt(recvId, 0, rewardAmount,sysdate);
						flag = accAmtDao.insertAccAmt(accAmt3);
					} else {
						status.setRollbackOnly();	
						map.put("flag", String.valueOf(flag));
						return map;
					}
					if (flag != ResultUtils.SUCCESS) {
						status.setRollbackOnly();	
						map.put("flag", String.valueOf(flag));
						return map;
					}
					//5记录一条资金变更记录信息
					AmtRecord amtRecord2 = getParametersAmtRecord(recvId, rewardDetail.getId(), PaycenterConstant.TRADE_TYPE_3, PaycenterConstant.TRADE_STATUS_1, rewardAmt,sysdate);
					flag = amtRecordDao.insertAmtRecord(amtRecord2);
					if (flag != ResultUtils.SUCCESS) {
						status.setRollbackOnly();	
						map.put("flag", String.valueOf(flag));
						return map;
					}					
					//6更新打赏统计信息
					long sourceId = rewardDetail.getSourceId();
					int sourceType = rewardDetail.getSourceType();
					Map<String, Object> input = new HashMap<String, Object>();
					//input.put("userId", recvId);
					input.put("sourceId", rewardDetail.getSourceId());
					
					RewardStatistical rewardStatistical = rewardStatisticalDao.selectMoneyBySourceId(input);
					flag = rewardStatistical.getFlag();
					if (flag !=ResultUtils.SUCCESS && flag == ResultUtils.DATAISNULL) {
						/*input.put("amt", rewardAmt);
						input.put("count", 1);
						input.put("sourceType", rewardDetail.getSourceType());*/
						rewardStatistical = new RewardStatistical();
						rewardStatistical.setSourceId(sourceId);
						rewardStatistical.setSourceType(sourceType);
						rewardStatistical.setTotalAmt(rewardAmt);
						rewardStatistical.setTotalCount(1);
						flag = rewardStatisticalDao.insertRewardStatistical(rewardStatistical);
					} else if(flag == ResultUtils.SUCCESS){
						input.put("amt", rewardAmt);
						input.put("count", 1);
						input.put("sourceType", rewardDetail.getSourceType());
						flag = rewardStatisticalDao.updateRewardStatistical(input);						
					} else {
						status.setRollbackOnly();	
						map.put("flag", String.valueOf(flag));
						return map;
					}
					
					if (flag != ResultUtils.SUCCESS) {
						status.setRollbackOnly();	
						map.put("flag", String.valueOf(flag));
						return map;
					}	
					
					if (recvId == userId) {
						Map<String, Object> inputMap = new HashMap<String, Object>();
						inputMap.put("userId", userId);
						RewardPesonStatistical rewardPesonStatistical = rewardPesonStatisticalDao.selectInfo(inputMap);
						flag = rewardPesonStatistical.getFlag();
						if (flag !=ResultUtils.SUCCESS && flag == ResultUtils.DATAISNULL) {
							rewardPesonStatistical = new RewardPesonStatistical();
							rewardPesonStatistical.setUserId(userId);
							rewardPesonStatistical.setTotalCollAmt(rewardAmt);
							rewardPesonStatistical.setTotalCollCount(1);
							rewardPesonStatistical.setTotalPayAmt(rewardAmt);
							rewardPesonStatistical.setTotalPayCount(1);
							flag = rewardPesonStatisticalDao.insertInfo(rewardPesonStatistical);
						} else if (flag ==ResultUtils.SUCCESS){
							rewardPesonStatistical = new RewardPesonStatistical();
							rewardPesonStatistical.setUserId(userId);
							rewardPesonStatistical.setTotalCollAmt(rewardAmt);
							rewardPesonStatistical.setTotalCollCount(1);
							rewardPesonStatistical.setTotalPayAmt(rewardAmt);
							rewardPesonStatistical.setTotalPayCount(1);
							flag = rewardPesonStatisticalDao.updateInfo(rewardPesonStatistical);						
						} else {
							status.setRollbackOnly();	
							map.put("flag", String.valueOf(flag));
							return map;
						}
						
						if (flag != ResultUtils.SUCCESS) {
							status.setRollbackOnly();	
							map.put("flag", String.valueOf(flag));
							return map;
						}
					}else {
						//7更新打赏人统计信息
						Map<String, Object> inputMap = new HashMap<String, Object>();
						inputMap.put("userId", userId);
						RewardPesonStatistical rewardPesonStatistical = rewardPesonStatisticalDao.selectInfo(inputMap);
						flag = rewardPesonStatistical.getFlag();
						if (flag !=ResultUtils.SUCCESS && flag == ResultUtils.DATAISNULL) {
							rewardPesonStatistical = new RewardPesonStatistical();
							rewardPesonStatistical.setUserId(userId);
							rewardPesonStatistical.setTotalPayAmt(rewardAmt);
							rewardPesonStatistical.setTotalPayCount(1);
							flag = rewardPesonStatisticalDao.insertInfo(rewardPesonStatistical);
						} else if (flag ==ResultUtils.SUCCESS){
							inputMap.put("totalPayAmt", rewardAmt);
							inputMap.put("totalPayCount", 1);
							flag = rewardPesonStatisticalDao.updatePayInfo(inputMap);						
						} else {
							status.setRollbackOnly();	
							map.put("flag", String.valueOf(flag));
							return map;
						}
						
						if (flag != ResultUtils.SUCCESS) {
							status.setRollbackOnly();	
							map.put("flag", String.valueOf(flag));
							return map;
						}
						//8更新被打赏人统计信息
						Map<String, Object> inputMap1 = new HashMap<String, Object>();
						inputMap1.put("userId", recvId);					
						RewardPesonStatistical rewardPesonStatistical1 = rewardPesonStatisticalDao.selectInfo(inputMap1);
						flag = rewardPesonStatistical1.getFlag();
						if (flag !=ResultUtils.SUCCESS && flag == ResultUtils.DATAISNULL) {
							rewardPesonStatistical = new RewardPesonStatistical();
							rewardPesonStatistical.setUserId(recvId);
							rewardPesonStatistical.setTotalCollAmt(rewardAmt);
							rewardPesonStatistical.setTotalCollCount(1);
							flag = rewardPesonStatisticalDao.insertInfo(rewardPesonStatistical);
						} else if (flag ==ResultUtils.SUCCESS){
							inputMap1.put("totalCollAmt", rewardAmt);
							inputMap1.put("totalCollCount", 1);
							flag = rewardPesonStatisticalDao.updateCollInfo(inputMap1);						
						} else {
							status.setRollbackOnly();	
							map.put("flag", String.valueOf(flag));
							return map;
						}
						if (flag != ResultUtils.SUCCESS) {
							status.setRollbackOnly();	
							map.put("flag", String.valueOf(flag));
							return map;
						}
					}
					
					//推送打赏金额的通知
					JSONObject json = new JSONObject();
					json.put("uid", userId);
					json.put("toUid", recvId);
					json.put("rid", rewardDetail.getSourceId());
					json.put("type", String.valueOf(rewardDetail.getSourceType()));
					json.put("pushType", "60");
					json.put("context", rewardAmt/100.0);
					json.toString();
					eagleProducer.send("pushMessage", "toBody", "", json.toString());
					
					//pushManager.pushGiveMSG(userId, recvId, rewardDetail.getSourceId(), String.valueOf(rewardDetail.getSourceType()),rewardAmt/100.0);
					//增加打赏的消息提醒
					userJedisManager.incrOneUserInfo(recvId, JedisConstant.USER_HASH_REWARD_NOTICE);
					
				} catch (Exception e) {
					LOG.debug(e);
					status.setRollbackOnly();
					map.put("flag", String.valueOf(ResultUtils.ERROR));
					return map;
				}
				map.put("flag", String.valueOf(flag));
				map.put("success", "success");
				LOG.info(map);
				return map;
			}		
		});
		return map;
	}

	public Map<String, String> payment(final PayLog paylog) {
		final Map<String, String> map = new HashMap<String, String>();
		paycenterMysqlTransactionTemplate.execute(new TransactionCallback<Map<String, String>>() {
			@Override
			public Map<String, String> doInTransaction(TransactionStatus status) {
				int flag = 1000;
				try {		
					final long sysdate = System.currentTimeMillis();
					/**
					 * 一、记录一条支付宝返回信息日志到支付宝记录表,修改充值表记录，并修改账户资金，记录
					 * 一条账户资金变更信息
					 */
					//1获取付款人的用户ID
					String outTradeNo = paylog.getOutTradeNo();
					long payRecordId = PaycenterUtil.getIdByOrderNumber(outTradeNo);
					PayRecord payRecord = payRecordDao.selectPayRecordInfoByOutTradeId(payRecordId, PaycenterConstant.TRADE_STATUS_PROCESSING);
					if (payRecord.getFlag() != ResultUtils.SUCCESS) {
						map.put("flag", String.valueOf(payRecord.getFlag()));
						return map;
					}
					long userId = payRecord.getUserId();
					String userName = payRecord.getUserName();
					int sendAmt = payRecord.getTotalFee();															
					//2生成一条响应记录
					PayLog paylog1 = new PayLog();	
					paylog.setUserId(userId);
					paylog.setUserName(userName);
					paylog.setLogCreate(sysdate);
					paylog1 = getParametersPayLog1(paylog, PaycenterConstant.PAY_MARK_2, PaycenterConstant.LOG_STATUS_2);
					flag = payLogDao.insertPayLog(paylog1);
					if (flag != ResultUtils.SUCCESS) {
						status.setRollbackOnly();	
						map.put("flag", String.valueOf(flag));
						return map;
					}
					//3同时将充值记录表改为成功
					payRecord.setTradeNo(paylog.getTradeNo());
					payRecord.setTradeTime(sysdate);
					payRecord.setTradeStatus(PaycenterConstant.TRADE_STATUS_1);
					flag = payRecordDao.updatePayRecord(payRecord);
					if (flag != ResultUtils.SUCCESS) {
						status.setRollbackOnly();	
						map.put("flag", String.valueOf(flag));
						return map;
					}
					//4查询账户表中用户是否存在，如果存在，将付款人的账户余额和本次金额相加，如果没有，生成一条账户信息
					AccAmt accAmt  = accAmtDao.selectAccAmtInfoByUserId(userId);	
					int accountAmt;
					if (accAmt.getFlag()==ResultUtils.SUCCESS) {
						Map<String, Object> accAmtMap = new HashMap<String, Object>();
						accAmtMap.put("rewardAmt", sendAmt);
						accAmtMap.put("sysdate", sysdate);
						accAmtMap.put("userId", accAmt.getUserId());
						flag = accAmtDao.updateAccAmt(accAmtMap);
					}else if (accAmt.getFlag() == ResultUtils.DATAISNULL){
						AccAmt accAmt1 = new AccAmt();
						accountAmt = sendAmt;
						accAmt1 = getParametersAccAmt(userId, accountAmt, 0, paylog.getLogCreate());
						flag = accAmtDao.insertAccAmt(accAmt1);
					}else {
						status.setRollbackOnly();	
						map.put("flag", String.valueOf(flag));
						return map;
					}
					if (flag != ResultUtils.SUCCESS) {
						status.setRollbackOnly();	
						map.put("flag", String.valueOf(flag));
						return map;
					}
					//5记录一条资金变更记录信息
					AmtRecord amtRecord = getParametersAmtRecord(paylog, payRecord.getId(), PaycenterConstant.TRADE_TYPE_1, PaycenterConstant.TRADE_STATUS_1, sendAmt);
					flag = amtRecordDao.insertAmtRecord(amtRecord);
					if (flag != ResultUtils.SUCCESS) {
						status.setRollbackOnly();	
						map.put("flag", String.valueOf(flag));
						return map;
					}
					status.isCompleted();
				} catch (Exception e) {
					LOG.debug(e);
					status.setRollbackOnly();
					map.put("flag", String.valueOf(ResultUtils.ERROR));
					return map;
				}
				map.put("flag", String.valueOf(flag));
				map.put("success", "success");
				LOG.info(map);
				return map;
			}		
		});
		return map;
	}

	/**
	 * 查询总打赏条数和当天打赏条数
	 * @param rewardDetail
	 * @return
	 */
	public Map<String, Object> selectCountReward(Map<String, Object> inMap) {
		Map<String, Object> outMap = new HashMap<String, Object>();
		
		
		long sysdate = System.currentTimeMillis();
		Map<String, Object> reMap = new HashMap<String, Object>();
		Map<String, Object> reMap2 = new HashMap<String, Object>();
			
		long userId = Long.valueOf((String)inMap.get("userId"));
		reMap = rewardDetailDao.selectOtherPersonRewardCount(userId, PaycenterConstant.REWARD_STATUS_1);
		if((Integer)reMap.get("flag") != ResultUtils.SUCCESS) {
			outMap.put("flag", String.valueOf(reMap2.get("flag")));
			return outMap;
		}
		reMap2 = rewardDetailDao.selectOtherPersonDayRewardCount(userId, PaycenterConstant.REWARD_STATUS_1, startDate(sysdate), endDate(sysdate));
		if((Integer)reMap2.get("flag") != ResultUtils.SUCCESS) {
			outMap.put("flag", String.valueOf(reMap2.get("flag")));
			return outMap;
		}
			

		outMap.put("flag", String.valueOf(ResultUtils.SUCCESS));
		outMap.put("rewardCount", String.valueOf(reMap.get("count")));
		outMap.put("dayRewardCount", String.valueOf(reMap2.get("dayCount")));
		return outMap;
	}
	
	/**
	 * 获取当天起始时间
	 * @return
	 */
	public static long startDate(long sysdate){
		String date = DateUtil.format(sysdate, "yyyy-MM-dd")+" 00:00:00";
		long date1 = DateUtil.formatLong(date, "yyyy-MM-dd HH:mm:ss");
		return date1;
	}
	
	/**
	 * 获取当天结束时间
	 * @return
	 */
	public static long endDate(long sysdate) {
		String date = DateUtil.format(sysdate, "yyyy-MM-dd")+" 23:59:59";		
		long date1 = DateUtil.formatLong(date, "yyyy-MM-dd HH:mm:ss");
		return date1;
	}
	
	public static long nextDate(long sysdate) {
		Date date = DateUtils.addDays(new Date(sysdate), 1);
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		String date1 = sdf.format(date).toString()+" 00:00:00";
		long date2 = DateUtil.formatLong(date1, "yyyy-MM-dd HH:mm:ss");
		return date2;
	}
	
	public static long lastDate(long sysdate) {
		Date date = DateUtils.addDays(new Date(sysdate), -1);
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		String date1 = sdf.format(date).toString()+" 23:59:59";
		long date2 = DateUtil.formatLong(date1, "yyyy-MM-dd HH:mm:ss");
		return date2;
	}

	/**
	 * 查询打赏业务明细
	 * @param inMap
	 * @return
	 */
	public Map<String, Object> selectRewardDetail(Map<String, Object> inMap) {
		try {				
			String userTypeStr = (String) inMap.get("userType");
			List<RewardDetail> rewardDetailList = new ArrayList<RewardDetail>();
			if (userTypeStr != null) {
				int userType = Integer.valueOf(userTypeStr);
				long userId = Long.valueOf((String) inMap.get("userId"));				
				if (userType == PaycenterConstant.USER_TYPE_1) {
					rewardDetailList = rewardDetailDao.selectRewardOtherPersonDetail(userId, PaycenterConstant.REWARD_STATUS_1);
				}else if (userType == PaycenterConstant.USER_TYPE_2) {
					rewardDetailList = rewardDetailDao.selectOtherPersonRewardDetail(userId, PaycenterConstant.REWARD_STATUS_1);
				}
			}else {
				long sourceId = Long.valueOf((String) inMap.get("sourceId"));
				int sourceType = Integer.valueOf((String) inMap.get("sourceType"));
				rewardDetailList = rewardDetailDao.selectRewardTypeDetail(sourceId, PaycenterConstant.REWARD_STATUS_1, sourceType);
			}
			Map<String, Object> output = ReturnMapUtil.getSuccessMap();
			output.put("rewardDetailList", rewardDetailList);
			return output;
		}catch (Exception e) {
			return ReturnMapUtil.getErrorMap(ResultUtils.ERROR);
		}	
	}

	/**
	 * 生成摇一摇信息
	 * 首先
	 * @param rewardDetail
	 * @return
	 */
	public Map<String, Object> createRollInfo(final Map<String, Object> inMap) {
		LOG.info("PaycenterDomainRepository.createRollInfo:"+inMap);
		final long sysdate = System.currentTimeMillis();
		final Map<String, Object> returnMap = ReturnMapUtil.getSuccessMap();
		paycenterMysqlTransactionTemplate.execute(new TransactionCallback<Map<String, Object>>() {
			@Override
			public Map<String, Object> doInTransaction(TransactionStatus status) {
				List<CompanyAccount> companyAccountList1;
				int flag = 1000;
				try {
					//将今天大于当天限制金额的置为0，将时间置为第二天，把最后修改时间为今天之前的时间修改为今天，并将金额置为0
					companyAccountList1 = companyAccountDao.selectCompanyAccountList1(PaycenterUtil.getLimitAmount(), endDate(sysdate), startDate(sysdate));
					for (CompanyAccount companyAccount1 : companyAccountList1) {
						Map<String, Object> input = new HashMap<String, Object>();
						input.put("id", companyAccount1.getId());
						input.put("dayOutTotalAmount", companyAccount1.getDayOutTotalAmount());
						input.put("amt", 0);
						input.put("sysdate", nextDate(sysdate));
						flag = companyAccountDao.updateCompanyAccountOther(input);
						if (flag != ResultUtils.SUCCESS) {
							return ReturnMapUtil.getErrorMap(ResultUtils.SYSTEM_BUSY);
						}
					}
					companyAccountList1 = companyAccountDao.selectCompanyAccountList1(lastDate(sysdate));
					for (CompanyAccount companyAccount1 : companyAccountList1) {
						Map<String, Object> input = new HashMap<String, Object>();
						input.put("id", companyAccount1.getId());
						input.put("dayOutTotalAmount", companyAccount1.getDayOutTotalAmount());
						input.put("amt", 0);
						input.put("sysdate", startDate(sysdate));
						flag = companyAccountDao.updateCompanyAccountOther(input);
						if (flag != ResultUtils.SUCCESS) {
							return ReturnMapUtil.getErrorMap(ResultUtils.SYSTEM_BUSY);
						}
					}
					
				} catch (Exception e1) {
					LOG.error(e1);
					return ReturnMapUtil.getErrorMap(ResultUtils.ERROR);
				}
				try {	
					/**
					 * 1.判断用户当天已经摇了多少次，如果大于3次，则不能继续摇
					 */
					long userId = Long.valueOf((String)inMap.get("userId"));
					String userName = (String)inMap.get("userName");
					long sourceId = 0;
					if (inMap.get("sourceId") != null) {
						sourceId = Long.valueOf((String)inMap.get("sourceId"));
					}
					String sourceName = (String) inMap.get("sourceName");
					int sourceType = 0;
					if (inMap.get("sourceType") != null) {
						sourceType = Integer.valueOf((String)inMap.get("sourceType"));
					}
					
					ControlUserStatus controlUserStatus = controlUserStatusDao.selectUserStatus(userId);
					//数据查询为空代表可以继续摇一摇生成一条初始数据，摇一摇次数等信息为空					
					if (controlUserStatus.getFlag() != ResultUtils.SUCCESS) {
						if (controlUserStatus.getFlag() != ResultUtils.DATAISNULL) {
							returnMap.put("flag", controlUserStatus.getFlag());
							return returnMap;
							
						}
					}
					if (controlUserStatus.getFlag() == ResultUtils.SUCCESS){
						int count = controlUserStatus.getRollCount();
						//如果时间小于今天的23:59:59,并且次数大于3，返回摇一摇已超过最大次数，不能继续摇
						if (count >=Integer.valueOf(dayLimitCount) && controlUserStatus.getRollLastTime()>startDate(sysdate) && controlUserStatus.getRollCount()<endDate(sysdate)) {
							//定义一个错误码 ,已超过摇一摇次数，不能继续摇
							returnMap.put("flag", ResultUtils.OVER_COUNT);
							return returnMap;
						} 
						if (controlUserStatus.getRollLastTime()<startDate(sysdate)){
							//如果时间小于今天的最小时间，将摇一摇的记录次数变为0，并将时间变为今天初始时间
							flag = controlUserStatusDao.updateControlUserStatus(userId,0, startDate(sysdate));
							if (flag != ResultUtils.SUCCESS) {
								returnMap.put("flag", flag);
								return returnMap;
							}
						} 	
					}
					
					//1随机生成一个金额
					int rewardAmount = PaycenterUtil.randomFen();
					//从企业账户表中查询账户余额不为0的账户,并且当天出账金额小于限制金额的账户，另外最后出账时间属于当天时间
					List<CompanyAccount> companyAccountList = companyAccountDao.selectCompanyAccountList(0,PaycenterUtil.getLimitAmount(),startDate(sysdate),endDate(sysdate));
					if (companyAccountList.size()<=0) {
						//定义一个错误码 超过企业规定的当日总额限制，请明天再试
						returnMap.put("flag", ResultUtils.OVER_DAY_TOTAL);
						return returnMap;
					}
					Random random = new Random();
					CompanyAccount companyAccount = companyAccountList.get(random.nextInt(companyAccountList.size()));
					//判断随机获取的企业信息的账户余额是否比摇一摇金额小,
					if (companyAccount.getAccountAmt() < rewardAmount) {
						rewardAmount = companyAccount.getAccountAmt();
					}
					
					//生成一条摇一摇打赏信息
					RewardDetail rewardDetail = getParameterRewardDetail(inMap,PaycenterConstant.REWARD_STATUS_3,sysdate);
					rewardDetail.setSourceId(sourceId);
					rewardDetail.setSourceName(sourceName);
					rewardDetail.setSourceType(sourceType);
					rewardDetail.setSendUserId(userId);					
					rewardDetail.setSendUserName(userName);
					rewardDetail.setCompanyId(companyAccount.getUserId());
					rewardDetail.setCompanyName(companyAccount.getUserName());
					rewardDetail.setSendAmt(rewardAmount);		
					rewardDetail.setRewardType(PaycenterConstant.REWARD_TYPE_2);
					Map<String, Object> rewardMap = rewardDetailDao.insertRewardDetailNew(rewardDetail);
					if ((Integer)rewardMap.get("flag") != ResultUtils.SUCCESS) {
						returnMap.put("flag", flag);
						return returnMap;
					}
					returnMap.put("rewardId", String.valueOf(rewardMap.get("id")));
					returnMap.put("rewardAmount", String.valueOf(rewardAmount));
					returnMap.put("companyId", String.valueOf(companyAccount.getUserId()));
					returnMap.put("companyName", companyAccount.getUserName());
					returnMap.put("rewardUserId", String.valueOf(rewardDetail.getReceiveUserId()));
					returnMap.put("rewardUserName", rewardDetail.getReceiveUserName());
					if (inMap.get("sourceId") != null) {
						returnMap.put("sourceId", String.valueOf(rewardDetail.getSourceId()));
						returnMap.put("sourceName", rewardDetail.getSourceName());
						returnMap.put("sourceType", String.valueOf(rewardDetail.getSourceType()));
					}
					return returnMap;
				} catch (Exception e) {
					LOG.error(e);
					status.setRollbackOnly();		
					returnMap.put("flag", ResultUtils.ERROR);
					return returnMap;
				}
			}				
		});
		return returnMap;
	}
	private RewardDetail getParameterRewardDetail(
			Map<String, Object> inMap, int rewardStatus, long sysdate) {
		RewardDetail rewardDetail = new RewardDetail();
		rewardDetail.setSendStatus(rewardStatus);
		rewardDetail.setReceiveUserId(Long.valueOf((String)inMap.get("rewardUserId")));
		rewardDetail.setReceiveUserName((String)inMap.get("rewardUserName"));
		rewardDetail.setSendTime(sysdate);
		return rewardDetail;
	}
	
	private ControlUserStatus getParameterUserStatus(long userId,
			String userName, int rollCount, long sysdate) {
		ControlUserStatus controlUserStatus = new ControlUserStatus();
		controlUserStatus.setUserId(userId);
		controlUserStatus.setUserName(userName);
		controlUserStatus.setRollCount(rollCount);
		controlUserStatus.setRollLastTime(sysdate);
		return controlUserStatus;
	}		
	
	/**
	 * @param map
	 * @param accountAmt
	 * @param rewardAmount
	 * @param sysdate
	 * @return
	 */
	private AccAmt getParametersAccAmt(long userId, int accountAmt, int rewardAmount, long sysdate) {
		AccAmt accAmt = new AccAmt();
		accAmt.setUserId(userId);
		accAmt.setAccountAmt(accountAmt);
		accAmt.setRewardAmount(rewardAmount);
		accAmt.setChangeTime(sysdate);
		return accAmt;
	}
	
	/**
	 * @param paylog
	 * @param serialId
	 * @param tradeType
	 * @param tradeStatus
	 * @param tradeAmount
	 * @return22222222
	 */
	private AmtRecord getParametersAmtRecord(long userId, long serialId, int tradeType, int tradeStatus, int tradeAmount, long sysdate) {
		AmtRecord amtRecord = new AmtRecord();
		amtRecord.setUserId(userId);
		amtRecord.setTradeType(tradeType);
		amtRecord.setSerialId(0);
		amtRecord.setOutTradeNo(null);
		amtRecord.setTradeAmount(tradeAmount);
		amtRecord.setTradeTime(sysdate);
		amtRecord.setTradeStatus(tradeStatus);
		amtRecord.setTradeDesc("");
		return amtRecord;
	}

	/**
	 * @param inMap
	 * @return
	 */
	public Map<String, String> createReloadOrderInfoByMap(final Map<String, String> inMap) {
		LOG.info("PaycenterDomainRepository.createReloadOrderInfoByMap:"+inMap);
		//组织发往支付宝的请求参数
		final Map<String, String> requestParameter = new HashMap<String, String>();
		paycenterMysqlTransactionTemplate.execute(new TransactionCallback<Map<String, String>>() {				
			@Override
			public Map<String, String> doInTransaction(TransactionStatus status) {		
				int flag = 1000;
				try {		
					//获取所有参数
					long sysdate = System.currentTimeMillis();
					String userId = (String)inMap.get("userId");
					String userName = (String)inMap.get("userName");
					String totalFee = (String)inMap.get("totalFee");
					String service = (String)inMap.get("service");
					String rewardUserId = (String)inMap.get("rewardUserId");
					String rewardPostscript = (String)inMap.get("rewardPostscript");
					String rewardUserName = (String)inMap.get("rewardUserName");
					String sourceId = (String)inMap.get("sourceId");
					String sourceName = (String)inMap.get("sourceName");
					String sourceType = (String)inMap.get("sourceType");
					String rewardtype = (String)inMap.get("rewardtype");
					/**
					 * 生成一个充值记录信息,并获取充值ID，以便获取发往支付宝订单号
					 */
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("partnerId", PaycenterUtil.getPartnerIdInt(PaycenterUtil.getPartner()));
					map.put("sellerId", PaycenterUtil.getAplipayValue(PaycenterUtil.getAlipyNumber()));
					map.put("userId", Long.valueOf(userId));
					map.put("userName", userName);
					map.put("totalFee", Integer.valueOf(totalFee));	
					map.put("tradeStatus", PaycenterConstant.TRADE_STATUS_PROCESSING);
					map.put("paymentType", PaycenterConstant.PAYMENT_TYPE_1);
					
					PayRecord payRecord = getParametersPayRecordNew(map);					
					Map<String, Object> rePayRecordMap = payRecordDao.insertPayRecordNew(payRecord);
					flag = (Integer) rePayRecordMap.get("flag");
					if (flag != ResultUtils.SUCCESS) {
						status.setRollbackOnly();	
						requestParameter.put("flag", String.valueOf(flag));
						return requestParameter;
					}					
					//生成发往支付宝参数
					Map<String, Object> map1 = new HashMap<String, Object>();
					map1.put("id", rePayRecordMap.get("id"));
					map1.put("service", service);
					map1.put("totalFee", Integer.valueOf(totalFee));
					Map<String, String> reMap = getParametersReturnParameter(map1);
					
					/**
					 * 记录发往支付宝的所有参数
					 */		
					Map<String, Object> map2 = new HashMap<String, Object>();
					map2.putAll(reMap);
					map2.put("userId", Long.valueOf(userId));
					map2.put("userName", userName);
					map2.put("sellerId", PaycenterUtil.getAplipayValue(PaycenterUtil.getAlipyNumber()));
					map2.put("parterId", PaycenterUtil.getPartnerIdInt(PaycenterUtil.getPartner()));
					map2.put("totalFee", Integer.valueOf(totalFee));
					
					PayLog paylogNew = getParametersPayLogNew(map2, PaycenterConstant.PAY_MARK_1, PaycenterConstant.LOG_STATUS_1);
					flag = payLogDao.insertPayLog(paylogNew);	
					paylogNew.setLogCreate(sysdate);
					if (flag != ResultUtils.SUCCESS) {
						status.setRollbackOnly();
						requestParameter.put("flag", String.valueOf(flag));
						return requestParameter;
					}
					/**
					 * 记录一条打赏业务
					 */
					RewardDetail rewardDetailNew = new RewardDetail();
					rewardDetailNew.setSendUserId(Long.valueOf(userId));
					rewardDetailNew.setSendUserName(userName);
					rewardDetailNew.setSendAmt(Integer.valueOf(totalFee));
					rewardDetailNew.setOutTradeNo(paylogNew.getOutTradeNo());
					rewardDetailNew.setReceiveUserId(Long.valueOf(rewardUserId));
					rewardDetailNew.setReceiveUserName(rewardUserName);
					rewardDetailNew.setPostscript(rewardPostscript);
					if (sourceId != null) {
						rewardDetailNew.setSourceId(Long.valueOf(sourceId));
						rewardDetailNew.setSourceName(sourceName);						
					}
					if (sourceType != null) {
						rewardDetailNew.setSourceType(Integer.valueOf(sourceType));
					}
					rewardDetailNew.setSendStatus(PaycenterConstant.REWARD_STATUS_3);
					if((PaycenterConstant.REWARD_TYPE_3+"").equals(rewardtype)){
						//如果是一键打赏
						rewardDetailNew.setRewardType(PaycenterConstant.REWARD_TYPE_3);
					}else{
						rewardDetailNew.setRewardType(PaycenterConstant.REWARD_TYPE_1);
					}
					rewardDetailNew.setSendTime(sysdate);
					flag = rewardDetailDao.insertRewardDetail(rewardDetailNew);
					if (flag != ResultUtils.SUCCESS) {
						status.setRollbackOnly();	
						requestParameter.put("flag", String.valueOf(flag));
						return requestParameter;
					}	
					requestParameter.putAll(reMap);
					requestParameter.put("flag", String.valueOf(flag));
					return requestParameter;
				}catch (Exception e) {
					LOG.error(e);
					status.setRollbackOnly();
					requestParameter.put("flag", String.valueOf(ResultUtils.ERROR));
					return requestParameter;
				}
				
			}
		});	
		LOG.info(requestParameter);
		return requestParameter;	
	}
	
	
	/**
	 * 微信支付，需要向微信下订单
	 * @param inMap
	 * @return
	 */
	public Map<String, String> createWeixinReloadOrderInfoByMap(final Map<String, String> inMap) {
		LOG.info("PaycenterDomainRepository.createWeixinReloadOrderInfoByMap:"+inMap);
		//组织发往微信的请求参数
		final Map<String, String> requestParameter = new HashMap<String, String>();
		paycenterMysqlTransactionTemplate.execute(new TransactionCallback<Map<String, String>>() {				
			@Override
			public Map<String, String> doInTransaction(TransactionStatus status) {		
				int flag = 1000;
				try {
					
					//获取所有参数
					long sysdate = System.currentTimeMillis();
					String userId = (String)inMap.get("userId");
					String userName = (String)inMap.get("userName");
					String totalFee = (String)inMap.get("totalFee");
					//String service = (String)inMap.get("service");
					String rewardUserId = (String)inMap.get("rewardUserId");
					String rewardPostscript = (String)inMap.get("rewardPostscript");
					String rewardUserName = (String)inMap.get("rewardUserName");
					String sourceId = (String)inMap.get("sourceId");
					String sourceName = (String)inMap.get("sourceName");
					String sourceType = (String)inMap.get("sourceType");
					String ip = (String)inMap.get("ip");
					String rewardtype = (String)inMap.get("rewardtype");
					
					/**
					 * 生成一个充值记录信息,并获取充值ID，以便获取发往微信的订单号
					 */
					Map<String, Object> map = new HashMap<String, Object>();
					int mchid = 0;
					if(StringUtils.isInteger(Configure.getMchid())){
						mchid = Integer.valueOf(Configure.getMchid());
					}
					map.put("partnerId", 0);
					map.put("sellerId", 0);
					map.put("userId", Long.valueOf(userId));
					map.put("userName", userName);
					map.put("totalFee", Integer.valueOf(totalFee));	
					map.put("tradeStatus", PaycenterConstant.TRADE_STATUS_PROCESSING);
					map.put("paymentType", PaycenterConstant.PAYMENT_TYPE_1);
					map.put("ip", ip);
					
					PayRecord payRecord = getParametersPayRecordNew(map);					
					Map<String, Object> rePayRecordMap = payRecordDao.insertPayRecordNew(payRecord);
					flag = (Integer) rePayRecordMap.get("flag");
					if (flag != ResultUtils.SUCCESS) {
						status.setRollbackOnly();	
						requestParameter.put("flag", String.valueOf(flag));
						return requestParameter;
					}
					
					
					//向微信下订单的参数
					ScanPayReqData scanPayReqData = getRequestWeixinParameters(rePayRecordMap,map);
					
					
					Map<String, String> reMap = new HashMap<String,String>();
					Map<String,Object> signmap = new HashMap<String,Object>();
					reMap.put("appid",Configure.getAppid());
					reMap.put("partnerid",Configure.getMchid());
					reMap.put("package", "Sign=WXPay");
					String noncestr = RandomStringGenerator.getRandomStringByLength(32);
					String timestamp = System.currentTimeMillis()/1000+"";
					reMap.put("noncestr",noncestr);
					reMap.put("timestamp",timestamp);
					
					signmap.put("appid",Configure.getAppid());
					signmap.put("partnerid",Configure.getMchid());
					signmap.put("package", "Sign=WXPay");
					signmap.put("noncestr",noncestr);
					signmap.put("timestamp",timestamp);
					
					try{
						//请求微信支付服务器进行下单操作(暂时有个问题，请求参数不能带中文，如果有中文会有乱码问题导致微信服务器验证签名错误)
						String xmlString = WXPay.requestScanPayService(scanPayReqData);
						PaycenterController.payreturnxml=xmlString;
						//String xmlString = "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg><appid><![CDATA[wx2421b1c4370ec43b]]></appid><mch_id><![CDATA[10000100]]></mch_id><nonce_str><![CDATA[IITRi8Iabbblz1Jc]]></nonce_str><sign><![CDATA[7921E432F65EB8ED0CE9755F0E86D72F]]></sign><result_code><![CDATA[SUCCESS]]></result_code><prepay_id><![CDATA[wx201411101639507cbf6ffd8b0779950874]]></prepay_id><trade_type><![CDATA[JSAPI]]></trade_type></xml>";
						if(xmlString!=null && xmlString.trim().startsWith("<xml>")){
							xmlString = "<?xml   version=\"1.0\"   encoding=\"gb2312\"?>"+xmlString;
						}
						//需要解析返回值
						Map<String,Object> resultMap = XMLParser.getMapFromXML(xmlString);
						
						if(resultMap!=null && resultMap.size()>0){
							if("SUCCESS".equals(resultMap.get("return_code")) && "SUCCESS".equals(resultMap.get("result_code"))){
								//网络向微信下订单成功
								String prepay_id  = (String) resultMap.get("prepay_id");
								reMap.put("prepayid",prepay_id);
								signmap.put("prepayid",prepay_id);
								String sign = Signature.getSign(signmap);
								reMap.put("sign",sign);
							}else{
								flag = ResultUtils.ERROR;
								status.setRollbackOnly();	
								requestParameter.put("flag", String.valueOf(flag));
								requestParameter.put("error","向微信下订单出现异常");
								return requestParameter;
							}
						}else{
							flag = ResultUtils.ERROR;
							status.setRollbackOnly();	
							requestParameter.put("flag", String.valueOf(flag));
							requestParameter.put("error","向微信下订单出现异常");
							return requestParameter;
						}
						
					}catch(Exception e){
						e.printStackTrace();
						LOG.error(e);
						flag = ResultUtils.ERROR;
						status.setRollbackOnly();	
						requestParameter.put("flag", String.valueOf(flag));
						requestParameter.put("error","向微信下订单出现异常");
						return requestParameter;
					}
					
					/**
					 * 记录发往支付宝的所有参数
					 */
					Map<String, Object> map2 = new HashMap<String, Object>();
					map2.put("userId", Long.valueOf(userId));
					map2.put("userName", userName);
					map2.put("totalFee", Integer.valueOf(totalFee));
					map2.put("sellerId", 0);
					map2.put("parterId", 0);
					
					PayLog paylogNew = getWeixinParametersPayLogNew(scanPayReqData,map2, PaycenterConstant.PAY_MARK_1, PaycenterConstant.LOG_STATUS_1);
					flag = payLogDao.insertPayLog(paylogNew);	
					paylogNew.setLogCreate(sysdate);
					if (flag != ResultUtils.SUCCESS) {
						status.setRollbackOnly();	
						requestParameter.put("flag", String.valueOf(flag));
						return requestParameter;
					}					
					/**
					 * 记录一条打赏业务
					 */					
					RewardDetail rewardDetailNew = new RewardDetail();
					rewardDetailNew.setSendUserId(Long.valueOf(userId));
					rewardDetailNew.setSendUserName(userName);
					rewardDetailNew.setSendAmt(Integer.valueOf(totalFee));
					rewardDetailNew.setOutTradeNo(paylogNew.getOutTradeNo());
					rewardDetailNew.setReceiveUserId(Long.valueOf(rewardUserId));
					rewardDetailNew.setReceiveUserName(rewardUserName);
					rewardDetailNew.setPostscript(rewardPostscript);
					if (sourceId != null) {
						rewardDetailNew.setSourceId(Long.valueOf(sourceId));
						rewardDetailNew.setSourceName(sourceName);						
					}
					if (sourceType != null) {
						rewardDetailNew.setSourceType(Integer.valueOf(sourceType));
					}
					rewardDetailNew.setSendStatus(PaycenterConstant.REWARD_STATUS_3);
					if((PaycenterConstant.REWARD_TYPE_3+"").equals(rewardtype)){
						//如果是一键打赏
						rewardDetailNew.setRewardType(PaycenterConstant.REWARD_TYPE_3);
					}else{
						rewardDetailNew.setRewardType(PaycenterConstant.REWARD_TYPE_1);
					}
					rewardDetailNew.setSendTime(sysdate);
					flag = rewardDetailDao.insertRewardDetail(rewardDetailNew);
					if (flag != ResultUtils.SUCCESS) {
						status.setRollbackOnly();	
						requestParameter.put("flag", String.valueOf(flag));
						return requestParameter;
					}
					requestParameter.putAll(reMap);
					requestParameter.put("out_trade_no", scanPayReqData.getOut_trade_no());
					requestParameter.put("flag", String.valueOf(flag));
					return requestParameter;
				}catch (Exception e) {
					e.printStackTrace();
					LOG.error(e);
					status.setRollbackOnly();
					requestParameter.put("flag", String.valueOf(ResultUtils.ERROR));
					return requestParameter;
				}
				
			}
		});	
		LOG.info(requestParameter);
		return requestParameter;
	}
	
	
	/**
	 * 自动打赏
	 * @param inMap
	 * @return
	 */
	public boolean autoReward(final Map<String, String> inMap) {
		LOG.info("PaycenterDomainRepository.autoReward:"+inMap);
		//
		final Map<String, String> requestParameter = new HashMap<String, String>();
		final Map<String,Object> paylogmap = new HashMap<String,Object>();
		paycenterMysqlTransactionTemplate.execute(new TransactionCallback<Map<String, String>>() {
			@Override
			public Map<String, String> doInTransaction(TransactionStatus status) {		
				int flag = 1000;
				try {		
					//获取所有参数
					long sysdate = System.currentTimeMillis();
					String userId = "";//写死的打赏用户id
					String userName = "";//写死的打赏人名称
					String totalFee = "";//写死的打赏金额
					String service = "";
					String rewardUserId = (String)inMap.get("rewardUserId");
					String rewardPostscript = "";
					String rewardUserName = (String)inMap.get("rewardUserName");
					String sourceId = (String)inMap.get("sourceId");
					String sourceName = (String)inMap.get("sourceName");
					String sourceType = (String)inMap.get("sourceType");
					String rewardtype = PaycenterConstant.REWARD_TYPE_4+"";
					/**
					 * 生成一个充值记录信息,并获取充值ID，以便获取发往支付宝订单号
					 */
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("partnerId", PaycenterUtil.getPartnerIdInt(PaycenterUtil.getPartner()));
					map.put("sellerId", PaycenterUtil.getAplipayValue(PaycenterUtil.getAlipyNumber()));
					map.put("userId", Long.valueOf(userId));
					map.put("userName", userName);
					map.put("totalFee", Integer.valueOf(totalFee));	
					map.put("tradeStatus", PaycenterConstant.TRADE_STATUS_PROCESSING);
					map.put("paymentType", PaycenterConstant.PAYMENT_TYPE_1);
					
					PayRecord payRecord = getParametersPayRecordNew(map);					
					Map<String, Object> rePayRecordMap = payRecordDao.insertPayRecordNew(payRecord);
					flag = (Integer) rePayRecordMap.get("flag");
					if (flag != ResultUtils.SUCCESS) {
						status.setRollbackOnly();	
						requestParameter.put("flag", String.valueOf(flag));
						return requestParameter;
					}					
					//生成发往支付宝参数
					Map<String, Object> map1 = new HashMap<String, Object>();
					map1.put("id", rePayRecordMap.get("id"));
					map1.put("service", service);
					map1.put("totalFee", Integer.valueOf(totalFee));
					Map<String, String> reMap = getParametersReturnParameter(map1);
					
					/**
					 * 记录发往支付宝的所有参数
					 */		
					Map<String, Object> map2 = new HashMap<String, Object>();
					map2.putAll(reMap);
					map2.put("userId", Long.valueOf(userId));
					map2.put("userName", userName);
					map2.put("sellerId", PaycenterUtil.getAplipayValue(PaycenterUtil.getAlipyNumber()));
					map2.put("parterId", PaycenterUtil.getPartnerIdInt(PaycenterUtil.getPartner()));
					map2.put("totalFee", Integer.valueOf(totalFee));
					
					PayLog paylogNew = getParametersPayLogNew(map2, PaycenterConstant.PAY_MARK_1, PaycenterConstant.LOG_STATUS_1);
					flag = payLogDao.insertPayLog(paylogNew);	
					paylogNew.setLogCreate(sysdate);
					if (flag != ResultUtils.SUCCESS) {
						status.setRollbackOnly();
						requestParameter.put("flag", String.valueOf(flag));
						return requestParameter;
					}
					paylogmap.put("paylog", paylogNew);
					/**
					 * 记录一条打赏业务
					 */
					RewardDetail rewardDetailNew = new RewardDetail();
					rewardDetailNew.setSendUserId(Long.valueOf(userId));
					rewardDetailNew.setSendUserName(userName);
					rewardDetailNew.setSendAmt(Integer.valueOf(totalFee));
					rewardDetailNew.setOutTradeNo(paylogNew.getOutTradeNo());
					rewardDetailNew.setReceiveUserId(Long.valueOf(rewardUserId));
					rewardDetailNew.setReceiveUserName(rewardUserName);
					rewardDetailNew.setPostscript(rewardPostscript);
					if (sourceId != null) {
						rewardDetailNew.setSourceId(Long.valueOf(sourceId));
						rewardDetailNew.setSourceName(sourceName);						
					}
					if (sourceType != null) {
						rewardDetailNew.setSourceType(Integer.valueOf(sourceType));
					}
					rewardDetailNew.setSendStatus(PaycenterConstant.REWARD_STATUS_3);
					rewardDetailNew.setRewardType(PaycenterConstant.REWARD_TYPE_4);
					rewardDetailNew.setSendTime(sysdate);
					flag = rewardDetailDao.insertRewardDetail(rewardDetailNew);
					if (flag != ResultUtils.SUCCESS) {
						status.setRollbackOnly();	
						requestParameter.put("flag", String.valueOf(flag));
						return requestParameter;
					}
					requestParameter.put("flag", String.valueOf(flag));
					return requestParameter;
				}catch (Exception e) {
					LOG.error(e);
					status.setRollbackOnly();
					requestParameter.put("flag", String.valueOf(ResultUtils.ERROR));
					return requestParameter;
				}
				
			}
		});
		
		if((ResultUtils.SUCCESS+"").equals(requestParameter.get("flag"))){
			//修改为付款记录
			PayLog paylog = (PayLog) paylogmap.get("paylog");
			if(paylog!=null){
				Map<String,String> map = notifyResult(paylog);
				if((ResultUtils.SUCCESS+"").equals(map.get("flag"))){
					return true;
				}
			}
		}
		LOG.info(requestParameter);
		return false;	
	}
	
	//获取后台发往微信的请求参数
	protected ScanPayReqData getRequestWeixinParameters(Map<String, Object> map,Map<String, Object> map2) {
		String authCode = "";
		String body="reward";//商品或支付单简要描述 
		String attach="";//附加数据
		String outTradeNo = PaycenterUtil.getOrderNumberById((Long)map.get("id"));//订单号
		int totalFee=(Integer)map2.get("totalFee");//金额，单位分
		String deviceInfo="";//终端设备号,可不传
		String spBillCreateIP=(String) map2.get("ip");//app端ip地址，必传
		String timeStart="";//交易起始时间 ，可不传
		String timeExpire="";//订单失效时间，可不传
		String goodsTag="";//商品标记，可不传
		String notifyUrl = "http://m.duyao001.com/pay/notifyweixinpay.do";//支付结果异步通知地址
		String tradeType = "APP";//交易类型
		ScanPayReqData scanPayReqData = new ScanPayReqData(authCode, body, attach, outTradeNo, totalFee, deviceInfo, spBillCreateIP, timeStart, timeExpire, goodsTag,notifyUrl,tradeType,null);												//
		return scanPayReqData;
	}

	protected PayLog getParametersPayLogNew(Map<String, Object> reMap,
			 int pAY_MARK_1, int lOG_STATUS_1) {
		PayLog paylog = new PayLog();
		paylog.setLogStatus(lOG_STATUS_1);
		paylog.setUserId((Long)reMap.get("userId"));
		paylog.setUserName((String)reMap.get("userName"));
		paylog.setOutTradeNo((String)reMap.get("out_trade_no"));
		paylog.setPayMark(pAY_MARK_1);
		paylog.setService((String)reMap.get("service"));
		paylog.setSellerId((Integer)reMap.get("sellerId"));
		paylog.setParterId((Integer)reMap.get("parterId"));
		paylog.setInputCharset((String)reMap.get("_input_charset"));
		paylog.setSignType((String)reMap.get("sign_type"));
		paylog.setSign((String)reMap.get("sign"));
		paylog.setNotifyUrl((String)reMap.get("notify_url"));
		paylog.setAppId((String)reMap.get("app_id"));
		paylog.setAppenv((String)reMap.get("appenv"));
		paylog.setSubject((String)reMap.get("subject"));
		paylog.setPaymentType((String)reMap.get("payment_type"));
		paylog.setTotalFee((Integer)reMap.get("totalFee"));
		paylog.setItBPay(null);
		paylog.setExternToken(null);
		paylog.setPaymethod(null);
		return paylog;
	}
	
	protected PayLog getWeixinParametersPayLogNew(ScanPayReqData scanPayReqData,Map<String, Object> reMap,
			 int pAY_MARK_1, int lOG_STATUS_1) {
		PayLog paylog = new PayLog();
		paylog.setLogStatus(lOG_STATUS_1);
		paylog.setUserId((Long)reMap.get("userId"));
		paylog.setUserName((String)reMap.get("userName"));
		paylog.setOutTradeNo(scanPayReqData.getOut_trade_no());
		paylog.setPayMark(pAY_MARK_1);
		paylog.setService("");
		paylog.setSellerId((Integer)reMap.get("sellerId"));
		paylog.setParterId((Integer)reMap.get("parterId"));
		paylog.setInputCharset("");
		paylog.setSignType("");
		paylog.setSign(scanPayReqData.getSign());
		paylog.setNotifyUrl(scanPayReqData.getNotify_url());
		paylog.setAppId(scanPayReqData.getAppid());
		paylog.setAppenv("");
		paylog.setSubject(scanPayReqData.getBody());
		paylog.setPaymentType(scanPayReqData.getTrade_type());
		paylog.setTotalFee(scanPayReqData.getTotal_fee());
		paylog.setItBPay(null);
		paylog.setExternToken(null);
		paylog.setPaymethod("weixin");
		paylog.setExtendField1("weixin");
		return paylog;
	}

	protected Map<String, String> getParametersReturnParameter(Map<String, Object> map) {
		String outTradeNo = PaycenterUtil.getOrderNumberById((Long)map.get("id"));       	 				//发往支付宝的订单号		
		Map<String, String> sArray = new LinkedHashMap<String, String>();
		sArray.put("partner", PaycenterUtil.getPartner());													//
		sArray.put("seller_id", PaycenterUtil.getAlipyNumber());										//
		sArray.put("out_trade_no", outTradeNo);															//
		sArray.put("subject", PaycenterConstant.SUBJECT);														//		
		sArray.put("body", PaycenterConstant.BODY);															//
		sArray.put("total_fee", PaycenterUtil.fenToYuan((Integer)map.get("totalFee")));
		sArray.put("notify_url", notify_url);											//
		sArray.put("service", (String)map.get("service"));		//mobile.securitypay.pay
		sArray.put("payment_type", PaycenterConstant.PAYMENT_TYPE);											//
		sArray.put("_input_charset", AlipayConfig.input_charset);										//
		Map<String, String> requestMap = AlipayCore.paraFilter(sArray);									
		String requestStr = AlipayCore.createLinkString(requestMap);	
		Map<String, String> reMap = new HashMap<String, String>();
		reMap.putAll(sArray);
		reMap.put("orderSpec", requestStr);
		String sign = RSA.sign(requestStr, AlipayConfig.private_key, AlipayConfig.input_charset);		
		reMap.put("sign_type", "RSA");																	//
		reMap.put("sign", sign);											//	
		return reMap;
	}

	private PayRecord getParametersPayRecordNew( Map<String, Object> map) {
		PayRecord payRecord = new PayRecord();
		payRecord.setParterId((Integer) map.get("partnerId"));
		payRecord.setSellerId((Integer) map.get("sellerId"));
		payRecord.setUserId((Long) map.get("userId"));
		payRecord.setUserName((String) map.get("userName"));
		payRecord.setTotalFee((Integer) map.get("totalFee"));
		payRecord.setTradeStatus((Integer)map.get("tradeStatus"));
		payRecord.setPaymentType((Integer) map.get("paymentType"));		
		return payRecord;
	}
	
	
	/**
	 * 
	 * @param reqMap
	 * @return
	 */
	public Map<String, Object> backRollInfo(final Map<String, Object> reqMap) {
		LOG.info("PaycenterDomainRepository.backRollInfo:"+reqMap);
		final Map<String, Object> resMap = new HashMap<String, Object>();
		paycenterMysqlTransactionTemplate.execute(new TransactionCallback<Map<String, Object>>() {
			@Override
			public Map<String, Object> doInTransaction(TransactionStatus status) {
				int flag = 1000;
				try {
					long sysdate = System.currentTimeMillis();
					long userId = Long.valueOf((String)reqMap.get("userId"));
					String userName = (String) reqMap.get("userName");
					String postscript = (String) reqMap.get("postscript");
					String rewardIdStr = (String)reqMap.get("rewardId");
					long rewardId = Long.valueOf(rewardIdStr);
					//查询业务表获取原打赏信息
					RewardDetail rewardDetail = rewardDetailDao.selectRewardById(userId,rewardId, PaycenterConstant.REWARD_STATUS_3);
					LOG.info("PaycenterDomainRepository.rewardDetail:"+rewardDetail);
					if(rewardDetail.getFlag() != ResultUtils.SUCCESS) {
						resMap.put("flag", String.valueOf(rewardDetail.getFlag()));
						return resMap;
					}
					//修改原打赏状态
					rewardDetail.setSendStatus(PaycenterConstant.REWARD_STATUS_1);	
					rewardDetail.setPostscript(postscript);
					flag = rewardDetailDao.updateRewardDetail(rewardDetail);
					if (flag != ResultUtils.SUCCESS) {
						status.setRollbackOnly();	
						resMap.put("flag", String.valueOf(flag));
						return resMap;
					}
					//修改用户摇一摇次数记录摇一摇并记录当前摇一摇时间
					ControlUserStatus controlUserStatus = controlUserStatusDao.selectUserStatus(userId);
					if (controlUserStatus.getFlag() == ResultUtils.DATAISNULL) {
						//记录一条摇一摇数据并记录一次摇一摇次数
						ControlUserStatus controlUserStatusNew = getParameterUserStatus(userId, userName, 1, sysdate);
						flag = controlUserStatusDao.insertControlUserStatus(controlUserStatusNew);
						if (flag != ResultUtils.SUCCESS) {
							status.setRollbackOnly();	
							resMap.put("flag", String.valueOf(flag));
							return resMap;
						}
					} else if (controlUserStatus.getFlag() == ResultUtils.SUCCESS) {
						int count = controlUserStatus.getRollCount()+1;
						flag = controlUserStatusDao.updateControlUserStatus(userId, count, sysdate);
						if (flag != ResultUtils.SUCCESS) {
							status.setRollbackOnly();	
							resMap.put("flag", String.valueOf(flag));
							return resMap;
						}
					} else {
						status.setRollbackOnly();	
						resMap.put("flag", String.valueOf(flag));
						return resMap;
					}
					long recvId = rewardDetail.getReceiveUserId();
					int rewardAmt = rewardDetail.getSendAmt();
					long companyId = rewardDetail.getCompanyId();
					String companyName = rewardDetail.getCompanyName();
					//修改企业账户账户余额、当日出账总额，及总出账金额并且设定当日出账总额不能大于限制金额
					Map<String, Object> comAccMap = new HashMap<String, Object>();
					comAccMap.put("companyId", companyId);
					comAccMap.put("rewardAmt", rewardAmt);
					comAccMap.put("sysdate", sysdate);
					comAccMap.put("limitAmount", PaycenterUtil.getLimitAmount());
					flag = companyAccountDao.updateCompanyAccount(comAccMap);
					if (flag != ResultUtils.SUCCESS) {
						status.setRollbackOnly();	
						resMap.put("flag", String.valueOf(flag));
						return resMap;
					}					
					//记录一条企业账户变更记录
					CompanyAccountLog companyAccLog = new CompanyAccountLog();
					companyAccLog.setCompanyId(companyId);
					companyAccLog.setCompanyName(companyName);
					companyAccLog.setTradeType(PaycenterConstant.TRADE_TYPE_2);
					companyAccLog.setSerialId(rewardDetail.getId());
					companyAccLog.setTradeAmount(rewardAmt);
					companyAccLog.setTradeTime(sysdate);
					companyAccLog.setTradeStatus(PaycenterConstant.TRADE_STATUS_1);
					flag = companyAccountLogDao.insertCompanyAccLog(companyAccLog);
					if (flag != ResultUtils.SUCCESS) {
						status.setRollbackOnly();	
						resMap.put("flag", String.valueOf(flag));
						return resMap;
					}
					
					//修改被打赏人打赏余额	
					//在创建用户时，可以将用户直接加入到账户表
					AccAmt accAmt2 = accAmtDao.selectAccAmtInfoByUserId(recvId);	
					int rewardAmount;
					if (accAmt2.getFlag()==ResultUtils.SUCCESS) {
						Map<String, Object> accAmtMap = new HashMap<String, Object>();
						accAmtMap.put("userId", recvId);
						accAmtMap.put("rewardAmt", rewardAmt);
						accAmtMap.put("sysdate", sysdate);
						flag = accAmtDao.updateAccAmt2(accAmtMap);
					}else if(accAmt2.getFlag()==ResultUtils.DATAISNULL){
						AccAmt accAmt3 = new AccAmt();
						rewardAmount = rewardAmt;
						accAmt3 = getParametersAccAmt(recvId, 0, rewardAmount, sysdate);
						flag = accAmtDao.insertAccAmt(accAmt3);
					} else {
						status.setRollbackOnly();	
						resMap.put("flag", String.valueOf(flag));
						return resMap;
					}
					if (flag != ResultUtils.SUCCESS) {
						status.setRollbackOnly();	
						resMap.put("flag", String.valueOf(flag));
						return resMap;
					}
					//5记录一条收款人资金变更记录信息
					AmtRecord amtRecord2 = getParametersAmtRecord(recvId, rewardDetail.getId(), PaycenterConstant.TRADE_TYPE_3, PaycenterConstant.TRADE_STATUS_1, rewardAmt, sysdate);
					flag = amtRecordDao.insertAmtRecord(amtRecord2);
					if (flag != ResultUtils.SUCCESS) {
						status.setRollbackOnly();	
						resMap.put("flag", String.valueOf(flag));
						return resMap;
					}					
					//6更新打赏资源统计信息
					//查询该资源或被打赏人信息是否存在					
					long sourceId = rewardDetail.getSourceId();
					int sourceType = rewardDetail.getSourceType();
					Map<String, Object> input = new HashMap<String, Object>();
					input.put("sourceId", rewardDetail.getSourceId());
					//LOG.info("PaycenterDomainRepository.sourceType:"+rewardDetail.getSourceType());
					
					RewardStatistical rewardStatistical = rewardStatisticalDao.selectMoneyBySourceId(input);
					flag = rewardStatistical.getFlag();
					if (flag !=ResultUtils.SUCCESS && flag == ResultUtils.DATAISNULL) {
						rewardStatistical = new RewardStatistical();
						rewardStatistical.setSourceId(sourceId);
						rewardStatistical.setSourceType(sourceType);
						rewardStatistical.setTotalAmt(rewardAmt);
						rewardStatistical.setTotalCount(1);
						//LOG.info("PaycenterDomainRepository.insertRewardStatistical:"+input);
						flag = rewardStatisticalDao.insertRewardStatistical(rewardStatistical);						
					} else {
						input.put("amt", rewardAmt);
						input.put("count", 1);
						input.put("sourceType", rewardDetail.getSourceType());
						LOG.info("PaycenterDomainRepository.updateRewardStatistical:"+input);
						flag = rewardStatisticalDao.updateRewardStatistical(input);						
					}
					
					if (flag != ResultUtils.SUCCESS) {
						status.setRollbackOnly();	
						resMap.put("flag", String.valueOf(flag));
						return resMap;
					}
					
					if (recvId == userId) {
						Map<String, Object> inputMap = new HashMap<String, Object>();
						inputMap.put("userId", userId);
						RewardPesonStatistical rewardPesonStatistical = rewardPesonStatisticalDao.selectInfo(inputMap);
						flag = rewardPesonStatistical.getFlag();
						if (flag !=ResultUtils.SUCCESS && flag == ResultUtils.DATAISNULL) {
							rewardPesonStatistical = new RewardPesonStatistical();
							rewardPesonStatistical.setUserId(userId);
							rewardPesonStatistical.setTotalCollAmt(rewardAmt);
							rewardPesonStatistical.setTotalCollCount(1);
							rewardPesonStatistical.setTotalPayAmt(rewardAmt);
							rewardPesonStatistical.setTotalPayCount(1);
							flag = rewardPesonStatisticalDao.insertInfo(rewardPesonStatistical);
						} else {
							rewardPesonStatistical = new RewardPesonStatistical();
							rewardPesonStatistical.setUserId(userId);
							rewardPesonStatistical.setTotalCollAmt(rewardAmt);
							rewardPesonStatistical.setTotalCollCount(1);
							rewardPesonStatistical.setTotalPayAmt(rewardAmt);
							rewardPesonStatistical.setTotalPayCount(1);
							flag = rewardPesonStatisticalDao.updateInfo(rewardPesonStatistical);						
						}
						
						if (flag != ResultUtils.SUCCESS) {
							status.setRollbackOnly();	
							resMap.put("flag", String.valueOf(flag));
							return resMap;
						}
					}else {
						//7更新打赏人统计信息
						Map<String, Object> inputMap = new HashMap<String, Object>();
						inputMap.put("userId", userId);
						RewardPesonStatistical rewardPesonStatistical = rewardPesonStatisticalDao.selectInfo(inputMap);
						flag = rewardPesonStatistical.getFlag();
						if (flag !=ResultUtils.SUCCESS && flag == ResultUtils.DATAISNULL) {
							rewardPesonStatistical = new RewardPesonStatistical();
							rewardPesonStatistical.setUserId(userId);
							rewardPesonStatistical.setTotalPayAmt(rewardAmt);
							rewardPesonStatistical.setTotalPayCount(1);
							/*inputMap.put("totalPayAmt", rewardAmt);
							inputMap.put("totalPayCount", 1);*/
							flag = rewardPesonStatisticalDao.insertInfo(rewardPesonStatistical);
						} else {
							inputMap.put("totalPayAmt", rewardAmt);
							inputMap.put("totalPayCount", 1);
							flag = rewardPesonStatisticalDao.updatePayInfo(inputMap);						
						}
						
						if (flag != ResultUtils.SUCCESS) {
							status.setRollbackOnly();	
							resMap.put("flag", String.valueOf(flag));
							return resMap;
						}
						//8更新被打赏人统计信息
						Map<String, Object> inputMap1 = new HashMap<String, Object>();
						inputMap1.put("userId", recvId);					
						RewardPesonStatistical rewardPesonStatistical1 = rewardPesonStatisticalDao.selectInfo(inputMap1);
						flag = rewardPesonStatistical1.getFlag();
						if (flag !=ResultUtils.SUCCESS && flag == ResultUtils.DATAISNULL) {
							rewardPesonStatistical = new RewardPesonStatistical();
							rewardPesonStatistical.setUserId(recvId);
							rewardPesonStatistical.setTotalCollAmt(rewardAmt);
							rewardPesonStatistical.setTotalCollCount(1);
							/*inputMap1.put("totalCollAmt", rewardAmt);
							inputMap1.put("totalCollCount", 1);*/
							flag = rewardPesonStatisticalDao.insertInfo(rewardPesonStatistical);
						} else {
							inputMap1.put("totalCollAmt", rewardAmt);
							inputMap1.put("totalCollCount", 1);
							flag = rewardPesonStatisticalDao.updateCollInfo(inputMap1);						
						}
						if (flag != ResultUtils.SUCCESS) {
							status.setRollbackOnly();	
							resMap.put("flag", String.valueOf(flag));
							return resMap;
						}
					}
					
					//推送打赏金额的通知
					JSONObject json = new JSONObject();
					json.put("uid", userId);
					json.put("toUid", recvId);
					json.put("rid", rewardDetail.getSourceId());
					json.put("type", String.valueOf(rewardDetail.getSourceType()));
					json.put("pushType", "60");
					json.put("context", rewardAmt/100.0);
					json.toString();
					eagleProducer.send("pushMessage", "toBody", "", json.toString());
					
					//pushManager.pushGiveMSG(userId, recvId, rewardDetail.getSourceId(), String.valueOf(rewardDetail.getSourceType()),rewardAmt/100.0);
					//增加打赏的消息提醒
					userJedisManager.incrOneUserInfo(recvId, JedisConstant.USER_HASH_REWARD_NOTICE);
				} catch(Exception e) {
					LOG.error(e);
					status.setRollbackOnly();		
					resMap.put("flag", String.valueOf(ResultUtils.ERROR));
					return resMap;
				}
				resMap.put("flag", String.valueOf(flag));
				return resMap;	
			}
		});
		return resMap;
	}

	/**
	 * @param userId
	 * @return
	 */
	public RewardPesonStatistical getMoneyByUserId(long userId) {
		LOG.info("PaycenterDomainRepository.getMoneyByUserId:"+userId);
		Map<String, Object> inMap = new HashMap<String, Object>();
		inMap.put("userId", userId);
		inMap.put("sourceId", 0);
		RewardPesonStatistical rewardPesonStatistical = rewardPesonStatisticalDao.selectInfo(inMap);
		return rewardPesonStatistical;
	}

	/**
	 * @param sourceId
	 * @return
	 */
	public RewardStatistical getMoneyBySourceId(long sourceId) {
		LOG.info("PaycenterDomainRepository.getMoneyBySourceId:"+sourceId);
		Map<String, Object> inMap = new HashMap<String, Object>();
		inMap.put("sourceId", sourceId);
		RewardStatistical rewardStatistical = rewardStatisticalDao.selectMoneyBySourceId(inMap);
		return rewardStatistical;
	}

	/**
	 * @param sourceId
	 * @return
	 */
	public List<RewardDetail> getRewardListInfoBySourceId(long sourceId, Long lastId) {
		Map<String, Object> inMap = new HashMap<String, Object>();
		inMap.put("sourceId", sourceId);
		inMap.put("sendStatus", PaycenterConstant.REWARD_STATUS_1);
		inMap.put("id", lastId);
		List<RewardDetail> rewardListInfoBySourceId = rewardDetailDao.selectRewardList(inMap);		
		return rewardListInfoBySourceId;
	}

	/**
	 * @param userId
	 * @return
	 */
	public List<RewardDetail> getRewardListInfoByUserId(long userId, Long lastId) {
		Map<String, Object> inMap = new HashMap<String, Object>();
		inMap.put("sendUserId", userId);
		inMap.put("sendStatus", PaycenterConstant.REWARD_STATUS_1);
		inMap.put("id", lastId);
		List<RewardDetail> rewardListInfoByUserId = rewardDetailDao.selectRewardListByUserId(inMap);		
		return rewardListInfoByUserId;
	}

	public List<RewardDetail> getRewardListInfoByRecvUserId(long recvUserId, Long lastId) {
		Map<String, Object> inMap = new HashMap<String, Object>();
		inMap.put("receiveUserId", recvUserId);
		inMap.put("sendStatus", PaycenterConstant.REWARD_STATUS_1);
		inMap.put("id", lastId);
		List<RewardDetail> rewardListInfoByUserId = rewardDetailDao.selectRewardListByRecvUserId(inMap);		
		return rewardListInfoByUserId;
	}

	public int getAccAmt(long userId) {
		AccAmt accAmt = accAmtDao.selectAccAmtInfoByUserId(userId);
		int totalAmt = 0;
		if (accAmt.getFlag()==ResultUtils.SUCCESS) {
			totalAmt = accAmt.getRewardAmount()+accAmt.getAccountAmt();
		}
		return totalAmt;
	}
	public AccAmt getAccAmtInfo(long userId) {
		AccAmt accAmt = accAmtDao.selectAccAmtInfoByUserId(userId);
		return accAmt;
	}
	public List<RewardPesonStatistical> getCollListInfoByUserIdDesc(
			long startIndex) {
		List<RewardPesonStatistical> collListInfo= new ArrayList<RewardPesonStatistical>();
		collListInfo = rewardPesonStatisticalDao.selectCollInfoListByUserIdDesc(startIndex);
		LOG.info("PaycenterDomainRepository.getCollListInfoByUserIdDesc:"+collListInfo);
		return collListInfo;
	}
	public List<RewardPesonStatistical> getPayListInfoByUserIdDesc(
			long startIndex) {
		List<RewardPesonStatistical> payListInfo = new ArrayList<RewardPesonStatistical>();
		payListInfo = rewardPesonStatisticalDao.selectPayInfoListByUserIdDesc(startIndex);	
		LOG.info("PaycenterDomainRepository.getPayListInfoByUserIdDesc:"+payListInfo);
		return payListInfo;
	}

	/**
	 * 
	 * @param userId
	 * @return
	 */
	public int getDayMoneyByUserId(long userId) {
		long sysdate = System.currentTimeMillis();
		Map<String, Object> inMap = new HashMap<String, Object>();
		inMap.put("userId", userId);
		inMap.put("startDate", startDate(sysdate));
		inMap.put("endDate", endDate(sysdate));
		inMap.put("sendStatus", PaycenterConstant.REWARD_STATUS_1);
		//Map<String, Object> outMap = rewardDetailDao.getDayMoneyByUserId(inMap);
		List<RewardDetail> rewardDetailList = rewardDetailDao.selectDayRewardList(inMap);
		int totalAmt = 0;
		if (rewardDetailList.size()>0) {
			for(RewardDetail rewardDetail : rewardDetailList) {
				int amount = rewardDetail.getSendAmt();
				totalAmt = totalAmt+amount;
			}
		}
		return totalAmt;
	}

	/**
	 * 资源被打赏总数
	 * @param sourceId
	 * @return
	 */
	public int getRewardCountBySourceId(long sourceId) {
		RewardStatistical rewardStatistical =rewardStatisticalDao.selectCountBySourceId(sourceId);
		return rewardStatistical.getTotalCount();
	}

	/**
	 * 打赏别人总数
	 * @param userId
	 * @return
	 */
	public int getRewardCountByUserId(long userId) {
		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("userId", userId);
		RewardPesonStatistical rewardPesonStatistical = rewardPesonStatisticalDao.selectInfo(inputMap);
		return rewardPesonStatistical.getTotalPayCount();
	}

	/**
	 * 被别人打赏总数
	 * @param userId
	 * @return
	 */
	public int getRewardCountByRecvUserId(long userId) {
		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("userId", userId);
		RewardPesonStatistical rewardPesonStatistical = rewardPesonStatisticalDao.selectInfo(inputMap);
		return rewardPesonStatistical.getTotalCollCount();
	}

	public List<RewardStatistical> getRewardStatisticalListBySourceIdDesc(long start) {
		List<RewardStatistical> sourceList = new ArrayList<RewardStatistical>();
		sourceList = rewardStatisticalDao.selectRewardStatisticalListBySourceIdDesc(start);
		LOG.info("PaycenterDomainRepository.getRewardStatisticalListBySourceIdDesc:"+sourceList);
		return sourceList;
	}
	
	/**
	 * 获取资源被打赏的次数
	 * 
	 * @param sourceId
	 * @param sendStatus
	 * @param sourceType
	 * @author zhangqi
	 * @return
	 */
	public int selectRewardTypeCount(long sourceId, int sendStatus, int sourceType){
		int count  =0;
		try{
			Map<String, Object> countMap=rewardDetailDao.selectRewardTypeCount(sourceId, sendStatus, sourceType);
			int flag = (Integer)countMap.get("flag");
			if(flag == ResultUtils.SUCCESS){
				count = (Integer)countMap.get("dayCount");
			}
		}catch (Exception e) {
			e.printStackTrace();
			count = -1;
		}
		return count;
	}
	//================以下是提现相关的功能==========
	
	/**
	 * @param set
	 * @return
	 */
	public int insertAccTakeSet(AccTakeSet set){
		return accTakeSetDao.insertAccTakeSet(set);
	}
	
	/**
	 * @param set
	 * @return
	 */
	public int updateAccTakeSet(AccTakeSet set){
		return accTakeSetDao.updateAccTakeSet(set);
	}
	
	/**
	 * @param uid
	 * @return
	 */
	public AccTakeSet selectAccTakeSetByUserId(long uid){
		return accTakeSetDao.selectAccTakeSetByUserId(uid);
	}
	
	/**
	 * @param accTakeRecord
	 * @return
	 */
	public int insertAccTakeRecord(final AccTakeRecord accTakeRecord){
		final long userId = accTakeRecord.getUserId();
		final Map<String, String> map = new HashMap<String, String>();
		int flag = ResultUtils.ERROR;
		paycenterMysqlTransactionTemplate.execute(new TransactionCallback<Map<String, String>>() {
			@Override
			public Map<String, String> doInTransaction(TransactionStatus status) {
				int flag = 1000;
				try {
					/**
					 * 先查询出账户信息，更新余额，然后生成一条提现记录
					 */
					int takefee = (int) accTakeRecord.getTakeFee();//long强制转int可能有问题
					AccAmt accAmt = accAmtDao.selectAccAmtInfoByUserId(userId);
					int accamt = accAmt.getRewardAmount();
					if(accamt<takefee){
						//
						status.setRollbackOnly();	
						map.put("flag", String.valueOf(ResultUtils.ERROR));
						return map;
					}
					accAmt.setRewardAmount(accAmt.getRewardAmount()-takefee);
					flag = accAmtDao.updateAccAmt(accAmt);
					if (flag != ResultUtils.SUCCESS) {
						status.setRollbackOnly();	
						map.put("flag", String.valueOf(flag));
						return map;
					}
					flag = accTakeRecordDao.insertAccTakeRecord(accTakeRecord);
					if (flag != ResultUtils.SUCCESS) {
						status.setRollbackOnly();	
						map.put("flag", String.valueOf(flag));
						return map;
					}
					map.put("flag", String.valueOf(flag));
					return map;
				} catch (Exception e) {
					e.printStackTrace();
					LOG.debug(e);
					status.setRollbackOnly();
					map.put("flag", String.valueOf(ResultUtils.ERROR));
					return map;
				}
			}
		});
		String flagstr = map.get("flag");
		if(flagstr!=null && flagstr.matches("^[0-9]+$")){
			flag = Integer.valueOf(flagstr);
		}
		return flag;
	}
	
	/**
	 * @param uid
	 * @return
	 */
	public List<AccTakeRecord> findAccTakeRecords(long uid,long start,int limit){
		return accTakeRecordDao.findAccTakeRecords(uid,start,limit);
	}
	/**
	 * 
	 * @Title: findAccTakeRecords 
	 * @Description: TODO
	 * @author weizhensong
	 * @date 2015-3-25 下午2:45:50
	 * @param @param uid
	 * @param @param tradeStatus
	 * @param @return
	 * @return List<AccTakeRecord>
	 * @throws
	 */
	public List<AccTakeRecord> findAccTakeRecords(long uid,int tradeStatus,long start,int limit){
		return accTakeRecordDao.findAccTakeRecords(uid, tradeStatus,start,limit);
	}

	/**
	 * 根据提现订单号和用户id查询
	 * @param uid
	 * @param takeno 
	 * @return
	 */
	public AccTakeRecord selectAccTakeRecordInfoBytakeNo(long uid,String takeno){
		return accTakeRecordDao.selectAccTakeRecordInfoBytakeNo(uid, takeno);
	}
	/**
	 * 查询一个时间段的某个用户的提现订单数量
	 */
	public int selectAccTakeRecordCount(long uid,long starttime,long endtime){
		return accTakeRecordDao.selectAccTakeRecordCount(uid, starttime, endtime);
	}
	/**
	 * 查询某个用户的提现总额
	 */
	public long getAccTakeTotal(long uid){
		return accTakeRecordDao.getAccTakeTotal(uid);
	}
	/**
	 * 根据交易状态查询提现总额(等待、成功、失败)
	 */
	public long getAccTakeTotal(long uid,int tradeStatus){
		return accTakeRecordDao.getAccTakeTotal(uid, tradeStatus);
	}
	/**
	 * 查询账户总设置信息
	 * @return
	 */
	public AccSet getAccSet(){
		return accSetDao.getAccSet();
	}
	
	public int insertIntoLegendHero(LegendHero hero) {
		return legendHeroDao.insertIntoLegendHero(hero);
	}

	public List<LegendHero> findAllLegendHero() {
		List<LegendHero> heroList = new ArrayList<LegendHero>();
		List<LegendHero> heroListProxy = legendHeroDao.findAllLegendHero();
		for (LegendHero hero : heroListProxy) {
			UserInfo info = userInfoDAO.findUserInfo(null, hero.getUserId());
			if (info.getFlag() == ResultUtils.SUCCESS) {
				hero.setUser(info);
			}
			heroList.add(hero);
		}
		return heroList;
	}

	public LegendHero findLegendHeroById(long id) {
		return legendHeroDao.findLegendHeroById(id);
	}

	public LegendHero findLegendHeroByUserId(long userId) {
		return legendHeroDao.findLegendHeroByUserId(userId);
	}

	public int updateLegendHeroById(LegendHero hero) {
		return legendHeroDao.updateLegendHeroById(hero);
	}

	public int updateLegendHeroByUserId(LegendHero hero) {
		return legendHeroDao.updateLegendHeroByUserId(hero);
	}

	public int deleteLegendHeroById(long id) {
		return legendHeroDao.deleteLegendHeroById(id);
	}

	public int deleteLegendHeroByUserId(long userId) {
		return legendHeroDao.deleteLegendHeroByUserId(userId);
	}
}
