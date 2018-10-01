package com.imooc.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer {

	BufferedWriter writer;   //因为无法设置final 只能全局
	BufferedReader reader;
	
	public static void main(String[] args) {

		SocketServer server = new SocketServer();
		server.startServer();
	}

	public void startServer(){
		ServerSocket serverSocket = null;
		Socket socket = null;
		try {
			serverSocket= new ServerSocket(9898);   //设置端口
			System.out.println("server started...");
			//不断的获取接入的客户端
			while (true) {
				socket = serverSocket.accept(); //阻塞状态 等待客户端接入
				manageConnection(socket);
			}
			
			//测试代码 定时任务 给客户端发送心跳
//			new Timer().schedule(new TimerTask() {
//				
//				@Override
//				public void run() {
//					try {
//						//在匿名类中 需要设置全局或者final 但final无法赋值
//						System.out.println("服务端已执行心跳操作");
//						writer.write("heart beat once...\n");
//						writer.flush();
//					} catch (IOException e) {
//						e.printStackTrace();
//					}
//				}
//			}, 3000, 3000);
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				serverSocket.close();
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	//管理客户端连接
	public void manageConnection(final Socket socket){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				
				try {
					System.out.println("client:" + socket.hashCode() + " is Connected");   //辨别客户端 哈希码唯一性
					reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
					String receivedMsg;
					while((receivedMsg = reader.readLine()) != null){
						System.out.println("client " + socket.hashCode() + ":" + receivedMsg);
						writer.write("server reply: " + receivedMsg + "\n");
						writer.flush();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}finally{
					try {
						reader.close();
						writer.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				
			}
		}).start();
	}
	
}
