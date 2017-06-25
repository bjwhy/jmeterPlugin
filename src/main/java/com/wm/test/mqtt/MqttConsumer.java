package com.wm.test.mqtt;

import org.fusesource.mqtt.client.Future;
import org.fusesource.mqtt.client.FutureConnection;
import org.fusesource.mqtt.client.MQTT;
import org.fusesource.mqtt.client.Message;
import org.fusesource.mqtt.client.QoS;
import org.fusesource.mqtt.client.Topic;

public class MqttConsumer {
	private FutureConnection conn;

	public static void main(String[] args) throws Exception {
		MQTT mqtt = new MQTT();
		mqtt.setHost("10.5.30.206", 1883);
		mqtt.setVersion("3.1.1");
		mqtt.setConnectAttemptsMax(3);
		mqtt.setReconnectAttemptsMax(3);
		// mqtt.setCleanSession(false);
		//
		mqtt.setClientId("11111111111111111111");

		FutureConnection connection = mqtt.futureConnection();
		Future<Void> f1 = connection.connect();
		f1.await();

		Future<byte[]> f2 = connection.subscribe(new Topic[] { new Topic(("/wm/bj/test"), QoS.AT_LEAST_ONCE) });
		f2.await();

		while (connection.isConnected()) {
			Future<Message> receive = connection.receive();
			Message message = receive.await();
			message.ack();
			System.out.println(new String(message.getPayload()));
		}
	}

	public void connect(String ip, int port) throws Exception {
		MQTT mqtt = new MQTT();
		mqtt.setHost(ip, port);
		mqtt.setVersion("3.1.1");
		mqtt.setConnectAttemptsMax(3);
		mqtt.setReconnectAttemptsMax(3);

		conn = mqtt.futureConnection();
		Future<Void> f1 = conn.connect();
		f1.await();
	}

	public void close() {
		conn.disconnect();
	}
}
