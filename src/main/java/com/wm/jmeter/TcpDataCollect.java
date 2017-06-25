package com.wm.jmeter;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;

import com.alibaba.fastjson.JSONObject;
import com.wm.constant.MessageConstant;
import com.wm.dto.MessageDto;
import com.wm.test.DataCollectTest.NettyTcpClient;

/**
 * @author haiyang.wu
 *
 */
public class TcpDataCollect extends AbstractJavaSamplerClient {
	private NettyTcpClient cli;

	@Override
	public SampleResult runTest(JavaSamplerContext context) {
		// TODO Auto-generated method stub

		String lon = context.getParameter("lon");
		String lat = context.getParameter("lat");
		long systime = Long.valueOf(context.getParameter("systime"));
		long gpstime = Long.valueOf(context.getParameter("gpstime"));
		int userID = Integer.valueOf(context.getParameter("userID"));
		int trackID = Integer.valueOf(context.getParameter("trackID"));

		JSONObject temp = new JSONObject();
		temp.put("lon", lon);
		temp.put("lat", lat);
		temp.put("systime", systime);
		temp.put("gpstime", gpstime);
		temp.put("userID", userID);
		temp.put("trackID", trackID);
		MessageDto messageDto = new MessageDto();
		messageDto.setMessageType(MessageConstant.MessageType.MESSAGE.getMsgCode());
		messageDto.setMessage(temp.toJSONString());

		SampleResult sr = new SampleResult();
		sr.setSampleLabel("TCP request");

		try {
			sr.sampleStart();
			messageDto = cli.sendMessage(messageDto);
			if (messageDto == null) {
				sr.setSuccessful(false);
			} else {
				sr.setSuccessful(true);
			}
		} catch (Throwable e) {
			sr.setSuccessful(false);
		} finally {
			sr.sampleEnd();
		}
		return sr;
	}

	@Override
	public Arguments getDefaultParameters() {
		Arguments params = new Arguments();
		params.addArgument("ip", "10.5.30.215");
		params.addArgument("port", "9000");
		params.addArgument("lon", "123.1");
		params.addArgument("lat", "87.2");
		params.addArgument("systime", String.valueOf(System.currentTimeMillis()));
		params.addArgument("gpstime", String.valueOf(System.currentTimeMillis()));
		params.addArgument("userID", "1");
		params.addArgument("trackID", "1");
		return params;
	}

	@Override
	public void setupTest(JavaSamplerContext context) {
		String ip = context.getParameter("ip");
		int port = Integer.valueOf(context.getParameter("port"));

		cli = new NettyTcpClient();
		cli.connect(ip, port);
	}

	@Override
	public void teardownTest(JavaSamplerContext context) {
		cli.close();
		super.teardownTest(context);
	}
}