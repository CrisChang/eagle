package com.keel.common.event.rocketmq;

/**
 * 继承该接口时一定要注意线程安全！
 * 继承该接口时一定要保证消息处理的幂等行！
 * */
public interface MessageRecv {
    
	public ConsumeStatus recv(String topic, String tags, String key, String body);
	
	public enum ConsumeStatus {
	    CONSUME_SUCCESS,
	    RECONSUME_LATER,
	}
}
