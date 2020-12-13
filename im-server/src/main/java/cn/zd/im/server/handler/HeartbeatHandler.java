package cn.zd.im.server.handler;


import cn.zd.im.server.model.IMSession;
import cn.zd.im.server.model.SentBody;

/**
 * 心跳handler，主要是让netty重置cheannel的空闲时间
 */
public class HeartbeatHandler implements IMRequestHandler {

    /**
     * 处理收到客户端从长链接发送的数据
     */
	@Override
    public void process(IMSession session, SentBody body) {}
}
