package myServer;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class server implements Runnable{
	public void run() {
		try {
			ServerSocket serverSocket = new ServerSocket(34567); //使用端口34567进行Socket连接
			while(true) {       //保持连接状态
				Socket client = serverSocket.accept(); //client是连上服务器的客户端
				try {
					//向客户端发送数据
					//之后考虑使用对象进行数据传输工作
					PrintWriter out = new PrintWriter( new BufferedWriter
							( new OutputStreamWriter(client.getOutputStream())),true);
					out.println("message from my server");
					out.close();  //关闭流
				}catch(Exception e) {
					e.printStackTrace();
				}finally {
					client.close();  //关闭Socket
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	//主函数，开启服务器
	public static void main(String a[]) {
		Thread serverThread = new Thread(new server());
		serverThread.start();
	}
}
