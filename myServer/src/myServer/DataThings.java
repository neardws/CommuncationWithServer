package myServer;

import java.util.ArrayList;

import com.qq.vip.singleangel.communcationwithserver.ClassDefined.HeartBeatsInfo;
/**
 * 存储部分数据量较小的数据，保存在全局变量中，之后考虑加上MySQL
 * @author singl
 *
 */
public class DataThings {
	private static ArrayList<HeartBeatsInfo> info = new ArrayList<HeartBeatsInfo>();
	private static ArrayList<Action> actions = new ArrayList<Action>();
	public static ArrayList<HeartBeatsInfo> getInfo(){
		return info;
	}
	
	/**
	 * 检查该Info是否在List中
	 * 如果在的话，看看信息是否相同，如果不同，更改
	 * @param info
	 */
	public static void addInfo(HeartBeatsInfo hbinfo) {
		boolean isIn = false;
		for(HeartBeatsInfo information: info) {
			if(information.getMacAdd().equals(hbinfo.getMacAdd())) {
				isIn = true;
				if(!information.getIpAdd().equals(hbinfo.getIpAdd())) {
					info.remove(information);
					info.add(hbinfo);
				}
			}
		}
		if(!isIn) {
			info.add(hbinfo);
		}
	}
	
	public static boolean addAction(String macAdd, String action) {
		if(action != null) {
			Action act = new Action(macAdd, action, "");
			actions.add(act);
			return true;
		}
		return false;
	}
	
}
