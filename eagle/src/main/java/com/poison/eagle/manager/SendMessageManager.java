package com.poison.eagle.manager;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import redis.clients.jedis.Jedis;

import com.alibaba.rocketmq.client.exception.MQBrokerException;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.remoting.exception.RemotingException;
import com.keel.common.cache.redis.JedisSimpleClient;
import com.keel.common.cache.redis.JedisWorker;
import com.keel.common.event.rocketmq.MessageRecv;
import com.keel.common.event.rocketmq.RocketProducer;
import com.poison.eagle.utils.MainSend;
import com.poison.ucenter.client.UcenterFacade;
import com.poison.ucenter.model.UserInfo;

public class SendMessageManager implements MessageRecv{
	private static final  Log LOG = LogFactory.getLog(SendMessageManager.class);
	private UcenterFacade ucenterFacade;
	
	private RocketProducer eagleProducer;
	
	private JedisSimpleClient momentsClient;
	
	public void setMomentsClient(JedisSimpleClient momentsClient) {
		this.momentsClient = momentsClient;
	}

	public void setUcenterFacade(UcenterFacade ucenterFacade) {
		this.ucenterFacade = ucenterFacade;
	}

	public void setEagleProducer(RocketProducer eagleProducer) {
		this.eagleProducer = eagleProducer;
	}

	public void sendMessage(String sendToken,String nickName,long userId,String atName,long resourceId,String type){
		UserInfo userInfo = ucenterFacade.findUserInfoByName(atName);
		String body = "";
		//if(null==userInfo){
			try {
				/*long recUserId = userInfo.getUserId();
				String resName = userInfo.getName();
				String resPushToken = userInfo.getPushToken();
				Map<String, String> map = new HashMap<String, String>();
				map.put("recId", recUserId+"");
				map.put("resName", resName);
				map.put("resPushToken", resPushToken);
				map.put("sendId", userId+"");
				map.put("sendName", nickName);
				map.put("sendPushToken", sendToken);
				map.put("resourceId", resourceId+"");
				map.put("type", type);
				ObjectMapper objectMaper = new ObjectMapper();*/
				body = "有人@你了，快去看看TA对你说了什么";//objectMaper.writeValueAsString(map);
				eagleProducer.send("msg", "at", null, body);
			
			} catch (MQClientException e) {
				e.printStackTrace();
			} catch (RemotingException e) {
				e.printStackTrace();
			} catch (MQBrokerException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} 
		//}
	}

	@Override
	public ConsumeStatus recv(String topic, String tags, String key, String body) {
		LOG.info("topic: " + topic + " tags:" + tags + " key:" + key + " body:" + body);
		System.out.println("topic: " + topic + " tags:" + tags + " key:" + key + " body:" + body);
		System.out.println("11111");
		//给其他的好友推送消息
		/*momentsClient.execute(new JedisWorker<Object>(){

			@Override
			public Object work(Jedis jedis) {
				// TODO Auto-generated method stub
				String str1 = jedis.lindex("2", 1);
				System.out.println(str1);
				jedis.lpush("2", "33333");
				System.out.println(jedis.llen("2"));
				//System.out.println(jedis.rpop("2"));
				List<String> list = jedis.lrange("2", 0, jedis.llen("2"));
				//jedis.hset(key, field, value);
				return null;
			}
			
		});*/
		/*MainSend mainSend = new MainSend();
		mainSend.sendMsgToStore(body, body);*/
		return MessageRecv.ConsumeStatus.CONSUME_SUCCESS;
	}
}
