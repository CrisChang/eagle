package com.keel.common.event.rocketmq;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.common.consumer.ConsumeFromWhere;
import com.alibaba.rocketmq.common.message.MessageExt;
import com.keel.common.event.rocketmq.MessageRecv.ConsumeStatus;

public class RocketPushConsumer {
	private static final  Log LOG = LogFactory.getLog(RocketPushConsumer.class);
	
	private boolean state = false;
	
	//工程名
	private String groupName;
	//配置信息：(topic:tags, ref)
	private Map<String, MessageRecv> configQueue;
	//Namesrv
	private String namesrv;
	
	//处理队列：(topic:tag, ref)
	private final Map<String, MessageRecv> queue;
	//消费者
	private DefaultMQPushConsumer consumer;

	public RocketPushConsumer() {
		super();
		
		this.queue = new HashMap<String, MessageRecv>();
	}

	@PostConstruct
	private void afterPropertiesSet() throws MQClientException{
		if( true == this.state) {
			return;
		}
		
		this.consumer = new DefaultMQPushConsumer(this.groupName);
		this.consumer.setNamesrvAddr(this.namesrv);
		
		
		/*
		 * 如：
		 * TopicTest1:TagA||TagC||TagD
		 * TopicTest2:*
		 * */
		for(Map.Entry<String, MessageRecv> config : this.configQueue.entrySet()) {
			String[] temp = StringUtils.split(config.getKey(), ':');
			String topic = temp[0];
			String tags = (temp.length > 1) ? temp[1] : "*";
			
			this.consumer.subscribe(topic, tags);
			
			String[] tagList = StringUtils.split(tags, "||");
			
			/*
			 * 如：
			 * TopicTest1:TagA
			 * TopicTest1:TagC
			 * TopicTest1:TagD
			 * TopicTest2:*
			 */
			for(String tag : tagList){
				String name = topic + ":" + tag;
				this.queue.put(name, config.getValue());
			}
		}
		
	    /*
        * 设置Consumer第一次启动是从队列头部开始消费还是队列尾部开始消费<br>
        * 如果非第一次启动，那么按照上次消费的位置继续消费
        */
		//this.consumer.setAllocateMessageQueueStrategy(allocateMessageQueueStrategy)
		//this.consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
		/*
         * 默认msgs里只有一条消息，可以通过设置consumeMessageBatchMaxSize参数来批量接收消息
         */
		this.consumer.setConsumeMessageBatchMaxSize(1);
		
		this.consumer.registerMessageListener(new MessageListenerConcurrently() {

			@Override
			public ConsumeConcurrentlyStatus consumeMessage(
					List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
				if(LOG.isDebugEnabled()) {
					LOG.debug("Receive New Messages: " + msgs);
				}
				
				/**
				 * TODO: 批量消息时加入循环逻辑
				 * */
				MessageExt msg = msgs.get(0);
				String topic = msg.getTopic();
				/**
				 * FIXME: 目前一个消息只含有一个tag
				 **/
				String tags = msg.getTags();
				String index = topic + ":" + (StringUtils.isNotBlank(tags) ? tags : "*");
				MessageRecv messageRecv = queue.get(index);
				
				if(null == messageRecv) {
					throw new RuntimeException("index: " + index + " is not matched messageRecv!");
				}

				ConsumeStatus status = messageRecv.recv(topic, tags, msg.getKeys(), new String(msg.getBody()));
				
				if(ConsumeStatus.CONSUME_SUCCESS == status){
					return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
				} else if(ConsumeStatus.RECONSUME_LATER == status) {
					return ConsumeConcurrentlyStatus.RECONSUME_LATER;
				} else {
					throw new RuntimeException("there is not correctly consume status!");
				}
			}
		}	
		);
		this.consumer.start();
		this.state = true;
	}
	
	//TODO: 在推出钩子里调用
	@PreDestroy
	private void destroy(){
		if(false == state) {
			return;
		}
		this.consumer.shutdown();
		this.consumer = null;
		this.configQueue = null;
		this.state = false;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public void setConfigQueue(Map<String, MessageRecv> configQueue) {
		this.configQueue = configQueue;
	}

	public void setNamesrv(String namesrv) {
		this.namesrv = namesrv;
	}
}
