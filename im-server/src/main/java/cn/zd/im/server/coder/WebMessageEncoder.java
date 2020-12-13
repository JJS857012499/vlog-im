package cn.zd.im.server.coder;

import cn.zd.im.server.model.Transportable;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;

import java.util.List;

/**
 * websocket发送消息前编码
 */
public class WebMessageEncoder extends MessageToMessageEncoder<Transportable> {

    private static final UnpooledByteBufAllocator BYTE_BUF_ALLOCATOR = new UnpooledByteBufAllocator(false);


    /**
     * Encode from one message to an other. This method will be called for each written message that can be handled
     * by this encoder.
     *
     * @param ctx the {@link ChannelHandlerContext} which this {@link MessageToMessageEncoder} belongs to
     * @param msg the message to encode to an other one
     * @param out the {@link List} into which the encoded msg should be added
     *            needs to do some kind of aggregation
     * @throws Exception is thrown if an error occurs
     */
    @Override
    protected void encode(ChannelHandlerContext ctx, Transportable msg, List<Object> out) {
        byte[] body = msg.getBody();
        ByteBuf buffer = BYTE_BUF_ALLOCATOR.buffer(body.length + 1);
        buffer.writeByte(msg.getType());
        buffer.writeBytes(body);
        out.add(new BinaryWebSocketFrame(buffer));
    }
}
