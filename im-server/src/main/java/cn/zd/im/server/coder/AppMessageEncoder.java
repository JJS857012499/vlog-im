package cn.zd.im.server.coder;


import cn.zd.im.server.constant.IMConstant;
import cn.zd.im.server.model.Transportable;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 服务端发送消息前编码
 */
public class AppMessageEncoder extends MessageToByteEncoder<Transportable> {

	/**
	 * Encode a message into a {@link ByteBuf}. This method will be called for each written message that can be handled
	 * by this encoder.
	 *
	 * @param ctx           the {@link ChannelHandlerContext} which this {@link MessageToByteEncoder} belongs to
	 * @param msg           the message to encode
	 * @param out           the {@link ByteBuf} into which the encoded message will be written
	 * @throws Exception    is thrown if an error occurs
	 */
	@Override
	protected void encode(final ChannelHandlerContext ctx, final Transportable msg, ByteBuf out){
		byte[] body = msg.getBody();
		byte[] header = createHeader(msg.getType(), body.length);
		out.writeBytes(header);
		out.writeBytes(body);
	}


	/**
	 * 创建消息头，结构为 TLV格式（Tag,Length,Value）
	 * 第一字节为消息类型
	 * 第二，三字节为消息长度分隔为高低位2个字节
	 */
	private byte[] createHeader(byte type, int length) {
		byte[] header = new byte[IMConstant.DATA_HEADER_LENGTH];
		header[0] = type;
		header[1] = (byte) (length & 0xff);
		header[2] = (byte) ((length >> 8) & 0xff);
		return header;
	}
}
