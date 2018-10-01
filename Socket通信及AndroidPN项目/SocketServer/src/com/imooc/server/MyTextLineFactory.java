package com.imooc.server;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

public class MyTextLineFactory implements ProtocolCodecFactory {

	private MyTextLineEncoder mEncoder;
	private MyTextLineDecoder mDecoder;
	private MyTextLineCumulativeDecoder mCumulativeDecoder;
	
	public MyTextLineFactory() {
		mEncoder = new MyTextLineEncoder();
		mDecoder = new MyTextLineDecoder();
		mCumulativeDecoder = new MyTextLineCumulativeDecoder();
	}
	
	@Override
	public ProtocolDecoder getDecoder(IoSession arg0) throws Exception {
		return mCumulativeDecoder;
	}

	@Override
	public ProtocolEncoder getEncoder(IoSession arg0) throws Exception {
		return mEncoder;
	}

}
