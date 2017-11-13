package myServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.qq.vip.singleangel.communcationwithserver.ClassDefined.Action;
import com.qq.vip.singleangel.communcationwithserver.ClassDefined.HeartBeatsInfo;
import com.qq.vip.singleangel.communcationwithserver.ClassDefined.Request;

public class HandleRequest implements Runnable{
	private static int HAND_REQUEST_PORT = 22333;
	@Override
	public void run() {
		// TODO Auto-generated method stub
				try {
					ServerSocket serverSocket = new ServerSocket(HAND_REQUEST_PORT);
					while(true) {
						try {
							Socket requestClient = serverSocket.accept();
							
							Executor excutor = Executors.newFixedThreadPool(100);
							excutor.execute(new Runnable() {
								//处理Socket, 向客户端发送命令
								@Override
								public void run() {
									// TODO Auto-generated method stub
									Socket client = requestClient;
									try {
										InputStream inStream = client.getInputStream();
										ObjectInputStream oiStream = new ObjectInputStream(inStream);
										try {
											Request request = (Request) oiStream.readObject();
											String ip = client.getInetAddress().getHostAddress().toString();
											Action action = new Action(ip, Action.START_DISCOVER, null); 
											ObjectOutputStream otStream = new ObjectOutputStream(client.getOutputStream());
											otStream.writeObject(action);
											handleWithRequest(request);
										} catch (ClassNotFoundException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
									}catch(IOException e) {
										e.printStackTrace();
									}finally {
								
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
	
	public boolean handleWithRequest(Request request) {
		boolean success = false;
		switch(request.getRequestType()) {
			case Request.INITIATIVE:
				ArrayList<HeartBeatsInfo> infoList = DataThings.getInfo();
				for(HeartBeatsInfo info : infoList) {
					String ip = info.getIpAdd();
					Socket socket = info.getSocket();
					Action action = new Action(ip, Action.START_CONNECT, null); 
					//if (socket.isConnected()) {
						SendAction sendAction = new SendAction(socket, ip,action);
						Thread thread = new Thread(sendAction);
						thread.start();
			/**		}else {
						try {
							throw new Exception("Socket is not connected");
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} **/
					
				}
				
				/**String ipAdd = infoList.get(0).getIpAdd();
				Socket client = infoList.get(0).getSocket();
				Action action = new Action(ipAdd, Action.START_DISCOVER, null); */
				/**
				for (HeartBeatsInfo info : infoList) {
					Socket socket = info.getSocket();
					String ip = info.getIpAdd();
					new Thread(new Runnable() {
		                @Override
		                public void run() {
		                    while (true){
		                        OutputStream output  = null;
		                        try {
		                            output = socket.getOutputStream();
		                            ObjectOutputStream objectOutputStream = new ObjectOutputStream(output);
		                            Action action = new Action(ip, Action.START_DISCOVER, null); 
		                            objectOutputStream.writeObject(action);
		                            
		                        } catch (IOException e) {
		                            e.printStackTrace();
		                        } 

		                    }
		                }
		            }).start();
				}
				
				**/
				
			//	new SendAction(action, client).send();
				break;
			case Request.ON_DEMAND:
				break;
		}
		return success;
	}

}
