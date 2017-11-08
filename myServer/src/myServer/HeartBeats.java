package myServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.qq.vip.singleangel.communcationwithserver.ClassDefined.HeartBeatsInfo;

/**
 * 客户端与服务器保持心跳连接，客户端给服务器发送自己的MAC地址
 * IP地址由Socket中获得
 * @author singl
 *
 */
public class HeartBeats implements Runnable{
	private static final int  HB_PORT = 34567; //传输心跳信息的端口
	
	public HeartBeats() {
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
					try {
						@SuppressWarnings("resource")
						ServerSocket serverSocket = new ServerSocket(HB_PORT);
						while(true) {
							try {
								Socket heartClient = serverSocket.accept();
								
								Executor excutor = Executors.newFixedThreadPool(100);
								excutor.execute(new Runnable() {
									//处理Socket, 向客户端发送命令
									@Override
									public void run() {
										// TODO Auto-generated method stub
										Socket client = heartClient;
										try {
											InputStream inStream = client.getInputStream();
											ObjectInputStream oiStream = new ObjectInputStream(inStream);
											HeartBeatsInfo info = (HeartBeatsInfo) oiStream.readObject();
											/**
											 * 报错，java.io.NotSerializableException: java.net.Socket
											 */
											info.setSocket(client);  
											DataThings.addInfo(info);
										}catch(IOException e) {
											e.printStackTrace();
										} catch (ClassNotFoundException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}finally {
											//保持长连接
											/**
											try {
												if(client != null) {
													client.close();
												}
											}catch(IOException e) {
												e.printStackTrace();
											}**/
										}
										
									}
									
								});
							}catch(IOException e) {
								e.printStackTrace();
							}
						}
					}catch(IOException e) {
						e.printStackTrace();
					}
	}
	
	
	
	
}
