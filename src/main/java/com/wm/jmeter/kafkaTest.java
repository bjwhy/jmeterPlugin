package com.wm.jmeter;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;

import com.wm.test.kafka.KafkaProducer;

public class kafkaTest extends AbstractJavaSamplerClient {

	private KafkaProducer provider;

	@Override
	public SampleResult runTest(JavaSamplerContext context) {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// TODO Auto-generated method stub
		SampleResult sr = new SampleResult();
		sr.sampleStart();
		try {
			provider.produce();
			sr.setSuccessful(true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			sr.setSuccessful(false);
			e.printStackTrace();
		}
		sr.sampleEnd();

		return sr;
	}

	@Override
	public Arguments getDefaultParameters() {
		Arguments params = new Arguments();
		params.addArgument("zk", "10.5.10.214:9092,10.5.10.224:9092,10.5.10.234:9092");
		return params;
	}

	@Override
	public void setupTest(JavaSamplerContext context) {

		provider = new KafkaProducer(context.getParameter("zk"));

	}

	@Override
	public void teardownTest(JavaSamplerContext context) {
		super.teardownTest(context);
	}
}
