package cn.zd.im.handler;


import cn.zd.im.push.IMMessagePusher;
import cn.zd.im.server.constant.IMConstant;
import cn.zd.im.server.handler.IMRequestHandler;
import cn.zd.im.server.model.IMSession;
import cn.zd.im.server.model.Message;
import cn.zd.im.server.model.ReplyBody;
import cn.zd.im.server.model.SentBody;
import cn.zd.im.service.IMSessionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Objects;


/*
 * 账号绑定实现
 * 
 */
@Component
public class BindHandler implements IMRequestHandler {

	private final Logger logger = LoggerFactory.getLogger(BindHandler.class);

	@Resource
	private IMSessionService IMSessionService;

	@Value("${server.host}")
	private String host;
	
	@Resource
	private IMMessagePusher defaultMessagePusher;
	
	@Override
	public void process(IMSession newSession, SentBody body) {

		ReplyBody reply = new ReplyBody();
		reply.setKey(body.getKey());
		reply.setCode(HttpStatus.OK.value());
		reply.setTimestamp(System.currentTimeMillis());
		
		try {

			String account = body.get("account");
			newSession.setAccount(account);
			newSession.setDeviceId(body.get("deviceId"));
			newSession.setHost(host);
			newSession.setChannel(body.get("channel"));
			newSession.setDeviceModel(body.get("device"));
			newSession.setClientVersion(body.get("appVersion"));
			newSession.setSystemVersion(body.get("osVersion"));
			newSession.setBindTime(System.currentTimeMillis());
			/*
			 * 由于客户端断线服务端可能会无法获知的情况，客户端重连时，需要关闭旧的连接
			 */
			IMSession oldSession = IMSessionService.get(account);

			/*
			 * 如果是账号已经在另一台终端登录。则让另一个终端下线
			 */

			if (oldSession != null && fromOtherDevice(newSession,oldSession) && oldSession.isConnected()) {
				sendForceOfflineMessage(oldSession, account, newSession.getDeviceModel());
			}
			
			/*
			 * 有可能是同一个设备重复连接，则关闭旧的链接，这种情况一般是客户端断网，联网又重新链接上来，之前的旧链接没有来得及通过心跳机制关闭，在这里手动关闭
			 * 条件1，连接来自是同一个设备
			 * 条件2.2个连接都是同一台服务器
			 */
			
			if (oldSession != null && !fromOtherDevice(newSession,oldSession) && Objects.equals(oldSession.getHost(),host)) {
				closeQuietly(oldSession);
			}

			IMSessionService.save(newSession);
			

		} catch (Exception exception) {
			reply.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			logger.error("Bind has error",exception);
		}
	 
		newSession.write(reply);
	}

	private boolean fromOtherDevice(IMSession oldSession , IMSession newSession) {
			
			return !Objects.equals(oldSession.getDeviceId(), newSession.getDeviceId());
	}
	 
	private void sendForceOfflineMessage(IMSession oldSession, String account, String deviceModel) {

		Message msg = new Message();
		msg.setAction(IMConstant.MessageAction.ACTION_OFFLINE);
		msg.setReceiver(account);
		msg.setSender("system");
		msg.setContent(deviceModel);
		msg.setId(System.currentTimeMillis());
		
		defaultMessagePusher.push(msg);
		
		closeQuietly(oldSession);

	}

	private void closeQuietly(IMSession oldSession) {
		if (oldSession.isConnected() && Objects.equals(host, oldSession.getHost())) {
			oldSession.setAttribute(IMConstant.KEY_QUIETLY_CLOSE,true);
			oldSession.closeOnFlush();
		}
	}

}
