package com.wm.test.kafka;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

public class KafkaProducer {
	private final Producer<String, String> producer;
	private static String topic = "why_test";
	private static List<KeyedMessage<String, String>> messageList = new ArrayList<KeyedMessage<String, String>>(10);

	static {

		for (int j = 0; j < 3; j++) {

			for (int i = 0; i < 10; i++) {

				messageList.add(new KeyedMessage<String, String>(topic, "partition[" + j + "]",
						"send_p[The " + i + " message]"));
			}
		}
	}

	public static ProducerConfig createProducerConfig(String broker) {
		Properties props = new Properties();
		props.put("metadata.broker.list", broker);
		props.put("serializer.class", "kafka.serializer.StringEncoder");
		props.put("request.required.acks", "-1");
		props.put("producer.type", "async");
		props.put("compression.codec", "2");
		return new ProducerConfig(props);
	}

	public KafkaProducer(String zk) {
		producer = new Producer<String, String>(createProducerConfig(zk));
	}

	public void produce() throws Exception {

		int len = messageList.size();

		producer.send(messageList.subList(0, len / 3));
		producer.send(messageList.subList(len / 3, (len << 1) / 3));
		producer.send(messageList.subList((len << 1) / 3, len));
	}

	public static void main(String args[]) throws Exception {
		KafkaProducer pro = new KafkaProducer("10.5.10.214:9092,10.5.10.224:9092,10.5.10.234:9092");
		pro.produce();
	}
}
