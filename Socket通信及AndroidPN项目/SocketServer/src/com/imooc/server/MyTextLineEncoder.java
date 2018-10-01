package com.imooc.server;

import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

public class MyTextLineEncoder implements ProtocolEncoder {

	@Override
	public void dispose(IoSession arg0) throws Exception {

	}

	@Override
	public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {
		String s = null;
		//如果message是String类型 直接强转赋值
		if(message instanceof String){
			s= (String) message;
		} 
		//如果不等于空时 进行转码工作
		if(s != null){
			//获取默认编码 避免重复调用
			CharsetEncoder charsetEncoder = (CharsetEncoder)session.getAttribute("encoder");
			if(charsetEncoder == null){
				charsetEncoder = Charset.defaultCharset().newEncoder();   //获取系统默认编码
				session.setAttribute("encoder", charsetEncoder);
			}
			IoBuffer ioBuffer = IoBuffer.allocate(s.length());   //根据字符串长度进行开辟内存
			ioBuffer.setAutoExpand(true);   //自动扩展
			ioBuffer.putString(s, charsetEncoder);   //存放发送的内容 获取编码格式
			ioBuffer.flip();
			out.write(ioBuffer);    //写入即可
		}
	}

}
