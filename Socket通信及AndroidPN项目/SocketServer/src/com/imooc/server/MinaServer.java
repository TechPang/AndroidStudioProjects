package com.imooc.server;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

public class MinaServer {

	public static void main(String[] args) {

		try {
			//Mina使用基本功能只需要四步
			NioSocketAcceptor acceptor = new NioSocketAcceptor();   //第一步 new acceptor
			acceptor.setHandler(new MyServerHandler());   //第二步 会话管理及消息处理
			/*
			 * 第三步 设置拦截器
			 * 
			 * 可以获取所有的拦截器
			 * 所有信息都需要过滤处理才能收发
			 * 消息并不只是string object任意对象 但消息在网络上是以字节传输 所以需要过滤处理
			 * addLast添加新的拦截器 factory用于对数据进行解码 可以自己写 也可以使用内置的
			 */
			acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(new MyTextLineFactory()));
			acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 5);   //设置空闲状态
			acceptor.bind(new InetSocketAddress(9898));   //第四步 传入端口 绑定启动服务
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
