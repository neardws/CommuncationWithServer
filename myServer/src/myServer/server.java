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

public class Server implements Runnable{
	
	public static final String SERVER_IP = "120.78.167.211";
	public static final int SERVER_PORT = 34567;
	//��������ͻ��˷��͵�����
	public static final String START_DISCOVER = "StartDiscover";
	public static final String STOP_DISCOVER = "StopDiscover";
	public static final String START_CONNECT = "StartConnect";
	public static final String STOP_CONNECT = "StopConnect";
	
	public Server() {
	}
	
	/**
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
	*/
	
	//������������������
	public static void main(String a[]) {
		Thread hbThread = new Thread(new HeartBeats());
		hbThread.start();
		Thread serverThread = new Thread(new Server());
		serverThread.start();
		
	}

	public void run() {
		try {
			@SuppressWarnings("resource")
			ServerSocket serverSocket = new ServerSocket(33356); //ʹ�ö˿�34567����Socket����
			while(true) {       //��������״̬
				Socket client = serverSocket.accept(); //client�����Ϸ������Ŀͻ���
				InetAddress inet = client.getInetAddress();
				String ipAdd = inet.getHostAddress().toString();
				try {
					Socket socket = client;
					
					//InputStream in = socket.getInputStream();
					//ObjectInputStream objIn = new ObjectInputStream(in);
					//HeartBeatsInfo info = (HeartBeatsInfo)objIn.readObject();
					
					//��ͻ��˷�������
					//֮����ʹ�ö���������ݴ��乤��
					OutputStream out = socket.getOutputStream();
					ObjectOutputStream objOutput = new ObjectOutputStream(out); 
					
					//ArrayList<HeartBeatsInfo> infoList = heartBeats.getInfoList();
					/**
					for(HeartBeatsInfo info : infoList) {
						if(info.getIpAdd().equals(ipAdd)) {
							objOutput.writeObject(info);
						}
					} */
					ArrayList<HeartBeatsInfo> arrayList = DataThings.getInfo();
					
					if(!arrayList.isEmpty()) {
						String ip = "";
						String mac = "";
						for(HeartBeatsInfo info : arrayList) {
							ip = ip +"  " + info.getIpAdd();
							mac = mac +"  "+ info.getMacAdd();
						}
						HeartBeatsInfo info = new HeartBeatsInfo(ip,mac);
						objOutput.writeObject(info);
					}else {
						HeartBeatsInfo info = new HeartBeatsInfo("192.168.0.1","1A:2B:3C:52:41:98:12:32");
						objOutput.writeObject(info);
					}
					out.close();  //�ر���
				}catch(Exception e) {
					e.printStackTrace();
				}finally {
					//client.close();  //�ر�Socket
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}

