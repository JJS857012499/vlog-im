package cn.zd.im.server.coder;


import cn.zd.im.server.constant.IMConstant;
import cn.zd.im.server.model.HeartbeatResponse;
import cn.zd.im.server.model.SentBody;
import cn.zd.im.server.model.proto.SentBodyProto;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * 服务端接收消息路由解码，通过消息类型分发到不同的真正解码器
 */
public class AppMessageDecoder extends ByteToMessageDecoder {


    /**
     * Decode the from one {@link ByteBuf} to an other. This method will be called till either the input
     * {@link ByteBuf} has nothing to read when return from this method or till nothing was read from the input
     * {@link ByteBuf}.
     *
     * @param ctx           the {@link ChannelHandlerContext} which this {@link ByteToMessageDecoder} belongs to
     * @param in            the {@link ByteBuf} from which to read data
     * @param out           the {@link List} to which decoded messages should be added
     * @throws Exception    is thrown if an error occurs
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

        /*
         * 消息体不足3位，发生断包情况
         */
        if (in.readableBytes() < IMConstant.DATA_HEADER_LENGTH) {
            return;
        }

        in.markReaderIndex();

        byte type = in.readByte();

        byte lv = in.readByte();
        byte hv = in.readByte();
        int length = getContentLength(lv, hv);

        /*
         * 发生断包情况，等待接收完成
         */
        if (in.readableBytes() < length) {
            in.resetReaderIndex();
            return;
        }

        byte[] dataBytes = new byte[length];
        in.readBytes(dataBytes);


        Object message = mappingMessageObject(dataBytes, type);

        out.add(message);
    }

    public Object mappingMessageObject(byte[] data, byte type) throws Exception {

        if (IMConstant.ProtobufType.C_H_RS == type) {
            return HeartbeatResponse.getInstance();
        }

        SentBodyProto.Model bodyProto = SentBodyProto.Model.parseFrom(data);
        SentBody body = new SentBody();
        body.setKey(bodyProto.getKey());
        body.setTimestamp(bodyProto.getTimestamp());
        body.putAll(bodyProto.getDataMap());

        return body;
    }

    /**
     * 解析消息体长度
     * 最大消息长度为2个字节表示的长度，即为65535
     *
     * @param lv 低位1字节消息长度
     * @param hv 高位1字节消息长度
     * @return 消息的真实长度
     */
    private int getContentLength(byte lv, byte hv) {
        int l = (lv & 0xff);
        int h = (hv & 0xff);
        return l | h << 8;
    }

}
