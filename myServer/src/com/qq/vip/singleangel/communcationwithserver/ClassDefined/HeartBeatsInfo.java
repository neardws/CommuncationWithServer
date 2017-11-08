package com.qq.vip.singleangel.communcationwithserver.ClassDefined;

import java.io.Serializable;
import java.net.Socket;

/**
 * 该类是客户端定时发送给服务器的心跳信息
 * 暂时是只有IP地址和MAC地址，之后可以添加信息，比如由是否需要状态同步等信息
 * @author singl
 *
 */
public class HeartBeatsInfo implements Serializable{
	private String macAdd;  
	private String ipAdd;
	private Socket client;
	
	public HeartBeatsInfo(String macAdd, String ipAdd) {
		this.macAdd = macAdd;
		this.ipAdd = ipAdd;
		this.client = null;
	}
	
	public String getMacAdd() {
		return this.macAdd;
	}
	public String getIpAdd() {
		return this.ipAdd;
	}
	public Socket getSocket() {
		return this.client;
	}
	public void setMacAdd(String macAdd) {
		this.macAdd = macAdd;
	}
	public void setIpAdd(String ipAdd) {
		this.ipAdd = ipAdd;
	}
	public void setSocket(Socket client) {
		this.client = client;
	}
	
}
