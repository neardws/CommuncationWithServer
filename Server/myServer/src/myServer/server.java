package myServer;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class server implements Runnable{
	public void run() {
		try {
			ServerSocket serverSocket = new ServerSocket(34567); //ʹ�ö˿�34567����Socket����
			while(true) {       //��������״̬
				Socket client = serverSocket.accept(); //client�����Ϸ������Ŀͻ���
				try {
					//��ͻ��˷�������
					//֮����ʹ�ö���������ݴ��乤��
					PrintWriter out = new PrintWriter( new BufferedWriter
							( new OutputStreamWriter(client.getOutputStream())),true);
					out.println("message from my server");
					out.close();  //�ر���
				}catch(Exception e) {
					e.printStackTrace();
				}finally {
					client.close();  //�ر�Socket
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	//������������������
	public static void main(String a[]) {
		Thread serverThread = new Thread(new server());
		serverThread.start();
	}
}
