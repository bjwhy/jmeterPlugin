package com.wm.test.mqtt;

import org.fusesource.mqtt.client.BlockingConnection;
import org.fusesource.mqtt.client.MQTT;
import org.fusesource.mqtt.client.QoS;

public class MqttProducer {
	private BlockingConnection conn;

	public static void main(String[] args) throws Exception {
		MQTT mqtt = new MQTT();
		mqtt.setHost("10.5.30.222", 1883);
		mqtt.setVersion("3.1.1");
		mqtt.setConnectAttemptsMax(3);
		mqtt.setReconnectAttemptsMax(3);

		BlockingConnection connection = mqtt.blockingConnection();
		connection.connect();

		connection.publish("/wm/bj/test", "Hello00000000".getBytes(), QoS.AT_LEAST_ONCE, false);

		Thread.sleep(100000);

	}

	public void connect(String ip, int port) throws Exception {
		MQTT mqtt = new MQTT();
		mqtt.setHost(ip, port);
		mqtt.setVersion("3.1.1");
		mqtt.setConnectAttemptsMax(3);
		mqtt.setReconnectAttemptsMax(3);

		conn = mqtt.blockingConnection();
		conn.connect();
	}

	public void close() throws Exception {
		conn.disconnect();
	}
}
