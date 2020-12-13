package cn.zd.im.server.model;

import java.io.Serializable;

/**
 * 客户端心跳响应
 */
public class HeartbeatResponse implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final String TAG = "CLIENT_HEARTBEAT_RESPONSE";
	private static HeartbeatResponse object = new HeartbeatResponse();

	private HeartbeatResponse() {
	}

	public static HeartbeatResponse getInstance() {
		return object;
	}

	@Override
	public String toString() {
		return TAG;
	}
	 
}
