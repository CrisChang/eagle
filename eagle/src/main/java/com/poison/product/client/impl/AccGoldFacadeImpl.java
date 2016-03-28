package com.poison.product.client.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.poison.product.client.AccGoldFacade;
import com.poison.product.model.AccGold;
import com.poison.product.model.AccGoldRecord;
import com.poison.product.service.AccGoldService;

public class AccGoldFacadeImpl implements AccGoldFacade{

	private static final  Log LOG = LogFactory.getLog(AccGoldFacadeImpl.class);
	
	private AccGoldService accGoldService;

	public void setAccGoldService(AccGoldService accGoldService) {
		this.accGoldService = accGoldService;
	}

	/**
	 * @param
	 * @return
	 */
	@Override
	public int insertIntoAccGold(long userId,long sequence,long goldamount,String extendField1,String extendField2,String extendField3){
		AccGold accGold = new AccGold();
		accGold.setUserId(userId);
		accGold.setSequence(sequence);
		accGold.setGoldamount(goldamount);
		accGold.setExtendField3(extendField3);
		accGold.setExtendField2(extendField2);
		accGold.setExtendField1(extendField1);
		accGold.setCreatetime(System.currentTimeMillis());
		accGold.setChangetime(System.currentTimeMillis());
		return accGoldService.insertIntoAccGold(accGold);
	}

	/**
	 *
	 * @return
	 */
	@Override
	public AccGold findAccGoldByUserId(long userId){
		return accGoldService.findAccGoldByUserId(userId);
	}
	
	/**
	 * @param accGold
	 * @return
	 */
	@Override
	public int updateAccGoldByUserId(long userId,long goldamount){
		AccGold accGold = new AccGold();
		accGold.setUserId(userId);
		accGold.setGoldamount(goldamount);
		accGold.setChangetime(System.currentTimeMillis());
		return accGoldService.updateAccGoldByUserId(accGold);
	}

	/**
	 * @param accGold
	 * @return
	 */
	@Override
	public int addAccGoldByUserId(long userId,long goldamount){
		AccGold accGold = new AccGold();
		accGold.setUserId(userId);
		accGold.setGoldamount(goldamount);
		accGold.setChangetime(System.currentTimeMillis());
		return accGoldService.addAccGoldByUserId(accGold);
	}
	@Override
	public int reduceAccGoldByUserId(long userId,long goldamount){
		AccGold accGold = new AccGold();
		accGold.setUserId(userId);
		accGold.setGoldamount(goldamount);
		accGold.setChangetime(System.currentTimeMillis());
		return accGoldService.reduceAccGoldByUserId(accGold);
	}
	/**
	 * @param
	 * @returno
	 */
	@Override
	public int insertIntoGoldRecord(long userId,long tradeAmount,long restAmount,String ordernum,String remark,long sequence,String type,String changeDesc,String cause,int shadow,String extendField1,String extendField2,String extendField3){
		AccGoldRecord accGoldRecord = new AccGoldRecord();
		accGoldRecord.setUserId(userId);
		accGoldRecord.setTradeAmount(tradeAmount);
		accGoldRecord.setRestAmount(restAmount);
		accGoldRecord.setOrdernum(ordernum);
		accGoldRecord.setRemark(remark);
		accGoldRecord.setSequence(sequence);
		accGoldRecord.setType(type);
		accGoldRecord.setChangeDesc(changeDesc);
		accGoldRecord.setCause(cause);
		accGoldRecord.setShadow(shadow);
		accGoldRecord.setExtendField1(extendField1);
		accGoldRecord.setExtendField2(extendField2);
		accGoldRecord.setExtendField3(extendField3);
		return accGoldService.insertIntoGoldRecord(accGoldRecord);
	}
}
