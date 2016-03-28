package com.keel.common.event.rocketmq;

public class TestMessageRecv implements MessageRecv {

	@Override
	public ConsumeStatus recv(String topic, String tags, String key, String body) {
		System.out.println("topic: " + topic + " tags:" + tags + " key:" + key + " body:" + body);
		return MessageRecv.ConsumeStatus.CONSUME_SUCCESS;
	}
}
