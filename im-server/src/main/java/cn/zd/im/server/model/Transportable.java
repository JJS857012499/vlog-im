package cn.zd.im.server.model;

/**
 * 需要向另一端发送的结构体
 */
public interface Transportable {
	/**
	 * 消息体字节数组
	 * @return
	 */
	byte[] getBody();

	/**
	 * 消息类型
	 * @return
	 */
	byte getType();
}
