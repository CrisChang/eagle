package com.poison.eagle.manager;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONException;
import org.json.JSONObject;

import com.alibaba.rocketmq.client.exception.MQBrokerException;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.remoting.exception.RemotingException;
import com.keel.common.event.rocketmq.MessageRecv;
import com.keel.common.event.rocketmq.RocketProducer;
import com.poison.eagle.manager.otherinterface.PushManager;
import com.poison.eagle.utils.MainSend;
import com.poison.ucenter.client.UcenterFacade;
import com.poison.ucenter.model.UserInfo;

public class PushMessageManager implements MessageRecv{
	private static final  Log LOG = LogFactory.getLog(PushMessageManager.class);
	
	private RocketProducer eagleProducer;
	private PushManager pushManager;
	public void setPushManager(PushManager pushManager) {
		this.pushManager = pushManager;
	}
	public void setEagleProducer(RocketProducer eagleProducer) {
		this.eagleProducer = eagleProducer;
	}

	@Override
	public ConsumeStatus recv(String topic, String tags, String key, String body) {
		long begin = System.currentTimeMillis();
		
		JSONObject json = null;
		Long uid=0l;
		Long toUid=0l;
		Long rid=0l;
		String type="";
		String pushType="";
		String pushContext = "";
		try {
			json = new JSONObject(body);
			uid = json.getLong("uid");
			toUid = json.getLong("toUid");
			rid = json.getLong("rid");
			type = json.getString("type");
			pushType = json.getString("pushType");
			pushContext = json.getString("context");
		} catch (JSONException e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
			pushContext = "";
		}
		
		if(PushManager.PUSH_COMMENT_TYPE.equals(pushType)|| PushManager.PUSH_COMMENT_TO_TYPE.equals(pushType)
				||PushManager.PUSH_GIVE_TYPE.equals(pushType)||PushManager.PUSH_AT_TYPE.equals(pushType)){
			//||PushManager.PUSH_PARISE_TYPE.equals(pushType)
			//pushManager.pushResourceMSG(uid, toUid, rid, type, pushType,pushContext);
			pushManager.jpushResourceMSG(uid, toUid, rid, type, pushType, pushContext);
		}
		
		//System.out.println("topic: " + topic + " tags:" + tags + " key:" + key + " body:" + body);
		
		long end = System.currentTimeMillis();
		//System.out.println("异步推送耗时："+(end-begin));
		return MessageRecv.ConsumeStatus.CONSUME_SUCCESS;
	}
}
