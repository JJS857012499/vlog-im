package cn.zd.im.config;


import cn.zd.im.handler.BindHandler;
import cn.zd.im.handler.SessionClosedHandler;
import cn.zd.im.server.handler.IMNioSocketAcceptor;
import cn.zd.im.server.handler.IMRequestHandler;
import cn.zd.im.server.model.IMSession;
import cn.zd.im.server.model.SentBody;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.HashMap;

@Configuration
public class IMConfig implements IMRequestHandler, ApplicationListener<ApplicationStartedEvent> {

	@Resource
	private ApplicationContext applicationContext;

	private final HashMap<String, IMRequestHandler> appHandlerMap = new HashMap<>();


	@Bean(destroyMethod = "destroy")
	public IMNioSocketAcceptor getNioSocketAcceptor(@Value("${im.app.port}") int port,
													@Value("${im.websocket.port}") int websocketPort) {

		return new IMNioSocketAcceptor.Builder()
				.setAppPort(port)
				.setWebsocketPort(websocketPort)
				.setOuterRequestHandler(this)
				.build();

	}

	@Override
	public void process(IMSession session, SentBody body) {
		
        IMRequestHandler handler = appHandlerMap.get(body.getKey());
		
		if(handler == null) {return ;}
		
		handler.process(session, body);
		
	}
	/*
	 * springboot启动完成之后再启动cim服务的，避免服务正在重启时，客户端会立即开始连接导致意外异常发生.
	 */
	@Override
	public void onApplicationEvent(ApplicationStartedEvent applicationStartedEvent) {

		appHandlerMap.put("client_bind",applicationContext.getBean(BindHandler.class));
		appHandlerMap.put("client_closed",applicationContext.getBean(SessionClosedHandler.class));

		applicationContext.getBean(IMNioSocketAcceptor.class).bind();
	}
}