package cn.zd.im.handler;



import cn.zd.im.server.constant.IMConstant;
import cn.zd.im.server.handler.IMRequestHandler;
import cn.zd.im.server.model.IMSession;
import cn.zd.im.server.model.SentBody;
import cn.zd.im.service.IMSessionService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Objects;


/*
 * 断开连接，清除session
 * 
 */
@Component
public class SessionClosedHandler implements IMRequestHandler {

	@Resource
	private IMSessionService IMSessionService;
	
	@Override
	public void process(IMSession ios, SentBody message) {
		Object quietly = ios.getAttribute(IMConstant.KEY_QUIETLY_CLOSE);
		if (Objects.equals(quietly, true)) {
			return;
		}

		Object account = ios.getAttribute(IMConstant.KEY_ACCOUNT);
		if (account == null) {
			return;
		}

		IMSessionService.remove(account.toString());
	}

}
