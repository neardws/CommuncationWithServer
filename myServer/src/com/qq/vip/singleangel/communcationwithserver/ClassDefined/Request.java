package com.qq.vip.singleangel.communcationwithserver.ClassDefined;

import java.io.Serializable;

public class Request implements Serializable{
	private int requestType;
	public final static int ON_DEMAND = 1;
	public final static int INITIATIVE = 2;

	public Request(int requestType) {
		this.requestType = requestType;
	}
	public int getRequestType() {
		return this.requestType;
	}
	public void setRequestType(int requestType) {
		this.requestType = requestType;
	}
}
