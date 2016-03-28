package com.keel.common.event.rocketmq;

import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.alibaba.rocketmq.client.exception.MQBrokerException;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.common.message.Message;
import com.alibaba.rocketmq.remoting.exception.RemotingException;

/**
 * 
 * */
public class RocketProducer {
	private static final  Log LOG = LogFactory.getLog(RocketProducer.class);
	
	private boolean state = false;
	
	//工程名
	private String groupName;
	//队列
	private Map<String, String> queue;
	//Nameserver
	private String namesrv;
	
	//生产者
	private DefaultMQProducer producer;
	
	/**
	 * 
	 * */
	private boolean existQueue(final String topic, final String tags){
		if (StringUtils.isBlank(topic) && StringUtils.isBlank(tags)) {
			return false;
		}
		
		String tempTags = queue.get(topic);
		if (StringUtils.isBlank(tags)) {
			return false;
		}
		
		String[] tag_list = StringUtils.split(tags, "||");
		for (int i = 0 ; i < tag_list.length; i++) {
			if(! StringUtils.contains(tempTags, tag_list[i])) {
				return false;
			}
		}
		
		return true;
	}
	
	@PostConstruct
	private void afterPropertiesSet() throws MQClientException{
		if( true == this.state) {
			return;
		}
		
		this.producer = new DefaultMQProducer(this.groupName);
		this.producer.setNamesrvAddr(this.namesrv);
		//FIXME: 由于mq本身的bug
		//this.producer.setDefaultTopicQueueNums(2);
		this.producer.start();
		
		this.state = true;
	}
	
	//TODO: 在推出钩子里调用
	@PreDestroy
	private void destroy(){
		if(false == state) {
			return;
		}
		this.producer.shutdown();
		this.producer = null;
		this.state = false;
	}
	
	/**
	 * 同步发送消息。
	 * 注意：send方法是同步调用，只要不抛异常就标识成功。但是发送成功也可会有多种状态。
	 * 注意：目前rocket只支持一个消息一个tag。
	 * 注意：key在topic环境下唯一。
	 * 
	 * TODO:
	 * 1. 返回失败：send方法抛出异常，需要调用方处理失败的情况；
	 * 2. 返回成功：send方法无抛异常，但是对于不同的成功状态需要加入不同的处理逻辑。本方法只是简单发送。
	 * @throws InterruptedException 
	 * @throws MQBrokerException 
	 * @throws RemotingException 
	 * @throws MQClientException 
	 * */
	public boolean send(String topic, String tags, String key, String body) throws MQClientException, RemotingException, MQBrokerException, InterruptedException {
		if (! existQueue(topic, tags)){
			throw new RuntimeException("topic: " + topic + " tags: " + tags + " is non-existent!");
		}
		
		Message msg = new Message(topic, tags, key, body.getBytes());
		
		SendResult sendResult = producer.send(msg);
		if(LOG.isDebugEnabled()) {
			LOG.debug("key: " + key + " SendResult: " + sendResult.toString());
		}
		return true;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	/**
	 * TODO: topic和tags中不能含有":"
	 * */
	public void setQueue(Map<String, String> queue) {
		this.queue = queue;
	}
	
	public void setNamesrv(String namesrv) {
		this.namesrv = namesrv;
	}

	public static void main(String[] argv){

	}
}
