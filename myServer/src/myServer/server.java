package myServer;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Queue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.qq.vip.singleangel.communcationwithserver.ClassDefined.HeartBeatsInfo;  

public class server implements Runnable{
	
	public static final String SERVER_IP = "120.78.167.211";
	public static final int SERVER_PORT = 34567;
	//服务器向客户端发送的命令
	public static final String START_DISCOVER = "StartDiscover";
	public static final String STOP_DISCOVER = "StopDiscover";
	public static final String START_CONNECT = "StartConnect";
	public static final String STOP_CONNECT = "StopConnect";
	
	private static ArrayList<HeartBeatsInfo> infoList = new ArrayList<HeartBeatsInfo>();
	private static ArrayList<Action> actions;
	
	public static HeartBeatsInfo myInfo = null;
	
	public server() {
		actions = new ArrayList<Action>();
	}
	
	public static boolean addAction(String macAdd, String action) {
		if(action != null) {
			Action act = new Action(macAdd, action, "");
			actions.add(act);
			return true;
		}
		return false;
	}
	
	/**
	 * 检查该Info是否在List中
	 * 如果在的话，看看信息是否相同，如果不同，更改
	 * @param info
	 */
	public static void AddInfo(HeartBeatsInfo info) {
		boolean isIn = false;
		for(HeartBeatsInfo information: infoList) {
			if(information.getMacAdd().equals(info.getMacAdd())) {
				isIn = true;
				if(!information.getIpAdd().equals(info.getIpAdd())) {
					infoList.remove(information);
					infoList.add(info);
				}
			}
		}
		if(!isIn) {
			infoList.add(info);
		}
	}
	
	
	
	public ArrayList<HeartBeatsInfo> getInfoList(){
		if(infoList != null) {
			return this.infoList;
		}
		return null;
	}
	
	public boolean doSomethingWithP2P(Action action) {
		boolean success = false;
		//ArrayList<HeartBeatsInfo> infoList = heartBeats.getInfoList();
		Socket connect = null;
		for(HeartBeatsInfo info : infoList) {
			if(action.getMacAdd().equals(info.getMacAdd())) {
				connect = (Socket)((HeartBeatsInfo) info).getSocket();
			}
		}
		if(connect != null) {
			try {
				OutputStream opStream = connect.getOutputStream();
				ObjectOutputStream objStream = new ObjectOutputStream(opStream);
				objStream.writeObject(action);
				objStream.flush();
				success = true;
				actions.remove(action);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				success = false;
			}
		}
		return success;
	}
	
	//主函数，开启服务器
	public static void main(String a[]) {
		//Thread hbThread = new Thread(new HeartBeats(this));
		//hbThread.start();
		Thread hbthread = new Thread(new hbThread());
		hbthread.start();
		Thread serverThread = new Thread(new server());
		serverThread.start();
		
	}

	public void run() {
		try {
			@SuppressWarnings("resource")
			ServerSocket serverSocket = new ServerSocket(33356); //使用端口34567进行Socket连接
			while(true) {       //保持连接状态
				Socket client = serverSocket.accept(); //client是连上服务器的客户端
				InetAddress inet = client.getInetAddress();
				String ipAdd = inet.getHostAddress().toString();
				try {
					Socket socket = client;
					
					//InputStream in = socket.getInputStream();
					//ObjectInputStream objIn = new ObjectInputStream(in);
					//HeartBeatsInfo info = (HeartBeatsInfo)objIn.readObject();
					
					//向客户端发送数据
					//之后考虑使用对象进行数据传输工作
					OutputStream out = socket.getOutputStream();
					ObjectOutputStream objOutput = new ObjectOutputStream(out);
					
				//	ArrayList<HeartBeatsInfo> infoList = heartBeats.getInfoList();
				/**	for(HeartBeatsInfo info : infoList) {
						if(info.getIpAdd().equals(ipAdd)) {
							objOutput.writeObject(info);
						}
					}*/
					
					//HeartBeatsInfo info = infoList.get(0);
					if(myInfo != null) {
						objOutput.writeObject(myInfo);
					}else{
						HeartBeatsInfo info = new HeartBeatsInfo("192.168.0.1","1A:2B:3C:52:41:98:12:32");
						objOutput.writeObject(info);
					}
					
					
					//HeartBeatsInfo info = new HeartBeatsInfo("192.168.0.1","1A:2B:3C:52:41:98:12:32");
					
					//objOutput.writeObject(info);
					out.close();  //关闭流
				}catch(Exception e) {
					e.printStackTrace();
				}finally {
					//client.close();  //关闭Socket
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static class hbThread implements Runnable{
		private static final int  HB_PORT = 34567; //传输心跳信息的端口
		
		public hbThread() {
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
									myInfo = info;
									/**
									HeartBeatsInfo info;
									try {
										info = (HeartBeatsInfo) oiStream.readObject();
										info.setSocket(client);
										AddInfo(info);
									} catch (ClassNotFoundException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									*/
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
}

