package com.wt.cmd;

public abstract class NettyCallback {
	
	private static int callbackIndex;
	public int callbackId;

	public NettyCallback() {
		this.callbackId = callbackIndex;
		callbackIndex++;
	}

	public abstract void callback(String json);
}
