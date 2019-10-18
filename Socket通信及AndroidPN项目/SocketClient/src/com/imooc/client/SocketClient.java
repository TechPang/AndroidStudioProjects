package com.imooc.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class SocketClient {

	public static void main(String[] args) {
		
		SocketClient client = new SocketClient();
		
		client.start();   //避免后期写变量及方法需要静态 代码也有条理性

	}

	
	/*
	 * shift+alt+z 导入代码块
	 */
	
	public void start(){
		BufferedReader inputReader = null;
		BufferedReader reader = null;   //用于读取服务器
		BufferedWriter writer = null;
		Socket socket = null;
		try {
			socket = new Socket("192.168.1.102", 9898);   //设置访问地址及端口
			writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			inputReader = new BufferedReader(new InputStreamReader(System.in));
			startServerReplyListener(reader);
			String inputContent;
			int count  = 0;
			while(!(inputContent = inputReader.readLine()).equals("bye")){
//				System.out.println(inputContent);
				writer.write(inputContent);   //因为读取方式为一行 需要加换行符 否则会一直读取
				//测试数据丢失 偶数才加\n
				if(count % 2 == 0){
					writer.write("\n");
				}
				count++;
				writer.flush();   //清空缓存区完成写入操作
				//获取服务端数据
//				String response = reader.readLine();
//				System.out.println(response);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			//养成好习惯 读写流及连接 最后都要关闭
			try {
				inputReader.close();
				writer.close();
				reader.close();
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void startServerReplyListener(final BufferedReader reader){   //匿名类需要写final才能使用
		//开启子线程 不能影响主线程运行
		new Thread(new Runnable() {
			public void run() {
				try {
					String response;
					//while死循环 不断地从服务器获取内容
					while((response = reader.readLine()) != null){
						System.out.println(response);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	
}
