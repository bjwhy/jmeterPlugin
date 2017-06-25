package com.wm.jmeter;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;

import com.wm.test.mqtt.MqttConsumer;

public class MqttTest extends AbstractJavaSamplerClient {

	private MqttConsumer provider;

	@Override
	public SampleResult runTest(JavaSamplerContext context) {
		// TODO Auto-generated method stub
		SampleResult sr = new SampleResult();

		return sr;
	}

	@Override
	public Arguments getDefaultParameters() {
		Arguments params = new Arguments();
		params.addArgument("ip", "10.5.30.222");
		params.addArgument("port", "1883");
		return params;
	}

	@Override
	public void setupTest(JavaSamplerContext context) {
		String ip = context.getParameter("ip");
		int port = Integer.valueOf(context.getParameter("port"));

		provider = new MqttConsumer();
		try {
			provider.connect(ip, port);
			Thread.sleep(999999999);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void teardownTest(JavaSamplerContext context) {
		provider.close();
		super.teardownTest(context);
	}
}
