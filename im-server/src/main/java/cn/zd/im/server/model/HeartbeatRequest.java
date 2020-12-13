package cn.zd.im.server.model;


import cn.zd.im.server.constant.IMConstant;

import java.io.Serializable;

/**
 * 服务端心跳请求
 */
public class HeartbeatRequest implements Serializable, Transportable {

	private static final long serialVersionUID = 1L;
	private static final String TAG = "SERVER_HEARTBEAT_REQUEST";
	private static final String CMD_HEARTBEAT_RESPONSE = "SR";
	private static HeartbeatRequest object = new HeartbeatRequest();

	private HeartbeatRequest() {

	}

	public static HeartbeatRequest getInstance() {
		return object;
	}

	@Override
	public String toString() {
		return TAG;
	}

	@Override
	public byte[] getBody() {
		return CMD_HEARTBEAT_RESPONSE.getBytes();
	}

	@Override
	public byte getType() {
		return IMConstant.ProtobufType.S_H_RQ;
	}

}
