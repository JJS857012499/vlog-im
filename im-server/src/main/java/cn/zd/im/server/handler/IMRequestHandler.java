package cn.zd.im.server.handler;

import cn.zd.im.server.model.IMSession;
import cn.zd.im.server.model.SentBody;

/**
 * 请求处理接口,所有的请求实现必须实现此接口
 */
public interface IMRequestHandler {

    /**
     * 处理收到客户端从长链接发送的数据
     */
    void process(IMSession session, SentBody message);
}
