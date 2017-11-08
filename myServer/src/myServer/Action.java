package myServer;

public class Action {
	private String macAdd;   		//命令接收的客户端的Mac地址
	private String action;			//命令
	private String connectMacAdd;	//如果是连接命令，接收命令的客户端要连接的客户端的Mac地址
	
	//服务器向客户端发送的命令
	public static final String START_DISCOVER = "StartDiscover";
	public static final String STOP_DISCOVER = "StopDiscover";
	public static final String START_CONNECT = "StartConnect";
	public static final String STOP_CONNECT = "StopConnect";
		
	public Action(String macAdd, String action, String connectMacAdd) {
		this.macAdd = macAdd;
		this.action = action;
		this.connectMacAdd = connectMacAdd;
	}
	
	public String getMacAdd() {
		return this.macAdd;
	}
	public String getAction() {
		return this.action;
	}
	public String getConnectMacAdd() {
		return this.connectMacAdd;
	}
	public void setMacAdd(String macAdd) {
		this.macAdd = macAdd;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public void setConnectMacAdd(String connectMacAdd) {
		this.connectMacAdd = connectMacAdd;
	}

}
