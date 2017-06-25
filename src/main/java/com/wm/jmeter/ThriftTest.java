package com.wm.jmeter;

import java.util.HashMap;
import java.util.Map;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;

import com.wm.test.thrift.NettyClient;
import com.wm.test.thrift.Request;
import com.wm.test.thrift.Response;

public class ThriftTest extends AbstractJavaSamplerClient {

	private NettyClient cli;

	@Override
	public SampleResult runTest(JavaSamplerContext context) {
		// TODO Auto-generated method stub

		Map<String, String> param = new HashMap<String, String>();

		Request msg = new Request("", "", "/hello", param);

		SampleResult sr = new SampleResult();
		sr.sampleStart();

		Response rs = cli.sendMsg(msg);

		sr.sampleEnd();

		if (rs == null) {
			sr.setSuccessful(false);
		} else {
			sr.setSuccessful(true);
			sr.setResponseData(rs.result, "utf-8");
		}

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

		cli = new NettyClient();
		cli.tcpConnect(ip, port);
	}

	@Override
	public void teardownTest(JavaSamplerContext context) {
		super.teardownTest(context);
		cli.stop();
	}
}
