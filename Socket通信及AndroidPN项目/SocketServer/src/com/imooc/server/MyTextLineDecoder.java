package com.imooc.server;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

public class MyTextLineDecoder implements ProtocolDecoder {

	@Override
	public void decode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
		int startPosition = in.position();   //获取读取位置
		//进入读取字节的循环
		while(in.hasRemaining()){
			byte b = in.get();   //获取字节
			if(b == '\n'){
				int currentPosition = in.position();   //获取当前位置
				int limit = in.limit();   //获取终点
				//截取字节
				in.position(startPosition);
				in.limit(currentPosition);
				IoBuffer buf = in.slice();
				//对字节进行处理
				byte[] dest = new byte[buf.limit()];
				buf.get(dest);   //接收字节数组
				String str = new String(dest);   //赋值到String
				out.write(str);
				//需要还原位置
				in.position(currentPosition);
				in.limit(limit);
			}
		}
	}

	@Override
	public void dispose(IoSession arg0) throws Exception {

	}

	@Override
	public void finishDecode(IoSession arg0, ProtocolDecoderOutput arg1) throws Exception {

	}

}
