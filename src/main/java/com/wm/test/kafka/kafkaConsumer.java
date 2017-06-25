package com.wm.test.kafka;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;

public class kafkaConsumer extends Thread {

	private String topic;

	private ExecutorService executor;

	private int numThreads = 3;

	public kafkaConsumer(String topic) {
		super();
		this.topic = topic;
	}

	@Override
	public void run() {
		ConsumerConnector consumer = createConsumer();
		Map<String, Integer> topicCountMap = new HashMap<String, Integer>(1);
		topicCountMap.put(topic, numThreads);
		Map<String, List<KafkaStream<byte[], byte[]>>> messageStreams = consumer.createMessageStreams(topicCountMap);
		List<KafkaStream<byte[], byte[]>> kafkaStreams = messageStreams.get(topic);

		executor = Executors.newFixedThreadPool(numThreads);

		for (KafkaStream<byte[], byte[]> stream : kafkaStreams) {
			executor.submit(new ConsumerTask(stream, numThreads));
		}
	}

	private ConsumerConnector createConsumer() {
		Properties props = new Properties();
		props.put("zookeeper.connect", "10.5.10.214:2181,10.5.10.224:2181,10.5.10.234:2181");// 声明zk
		props.put("group.id", "group1");// 必须要使用别的组名称，
										// 如果生产者和消费者都在同一组，则不能访问同一组内的topic数据
		props.put("zookeeper.session.timeout.ms", "5000");
		props.put("zookeeper.connection.timeout.ms", "10000");
		props.put("zookeeper.sync.time.ms", "2000");
		props.put("rebalance.backoff.ms", "5000");
		return Consumer.createJavaConsumerConnector(new ConsumerConfig(props));
	}

	public static void main(String[] args) {
		new kafkaConsumer("why_test").start();// 使用kafka集群中创建好的主题 test

	}

}