package com.wm.test.kafka;

import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;

public class ConsumerTask implements Runnable {
	private KafkaStream<byte[], byte[]> m_stream;
	private int m_threadNumber;

	public ConsumerTask(KafkaStream<byte[], byte[]> a_stream, int a_threadNumber) {
		m_threadNumber = a_threadNumber;
		m_stream = a_stream;
	}

	public void run() {
		ConsumerIterator<byte[], byte[]> it = m_stream.iterator();
		while (it.hasNext())
			System.out.println("Thread " + m_threadNumber + ": " + new String(it.next().message()));
	}
}