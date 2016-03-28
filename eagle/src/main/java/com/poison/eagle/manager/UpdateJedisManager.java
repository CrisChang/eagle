package com.poison.eagle.manager;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.alibaba.rocketmq.client.exception.MQBrokerException;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.remoting.exception.RemotingException;
import com.keel.common.event.rocketmq.MessageRecv;
import com.keel.common.event.rocketmq.RocketProducer;
import com.poison.eagle.utils.MainSend;
import com.poison.ucenter.client.UcenterFacade;
import com.poison.ucenter.model.UserInfo;

public class UpdateJedisManager implements MessageRecv{
	private static final  Log LOG = LogFactory.getLog(UpdateJedisManager.class);
	
	private RocketProducer eagleProducer;
	public void setEagleProducer(RocketProducer eagleProducer) {
		this.eagleProducer = eagleProducer;
	}

	@Override
	public ConsumeStatus recv(String topic, String tags, String key, String body) {
		System.out.println("topic: " + topic + " tags:" + tags + " key:" + key + " body:" + body);
		return MessageRecv.ConsumeStatus.CONSUME_SUCCESS;
	}
}
