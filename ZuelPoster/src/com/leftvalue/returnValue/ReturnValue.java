package com.leftvalue.returnValue;

import com.alibaba.fastjson.JSON;

public class ReturnValue {
	public static final ReturnValue s_success = new ReturnValue("1", "well done");
	public static final ReturnValue s_fail = new ReturnValue("-1", "服务器不想理你并给你抛出了一个错误");
	private String state;
	private String info;

	public static ReturnValue std_fail(String info) {
		return new ReturnValue("-1", info);
	}

	public static ReturnValue std_success(String info) {
		return new ReturnValue("1", info);
	}

	public ReturnValue(String state, String info) {
		this.state = state;
		this.info = info;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}

}
