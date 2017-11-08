package myServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

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
				break;
			case Request.ON_DEMAND:
				break;
		}
		return success;
	}

}
