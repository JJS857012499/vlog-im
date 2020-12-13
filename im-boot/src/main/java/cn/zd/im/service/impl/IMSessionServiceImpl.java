package cn.zd.im.service.impl;



import cn.zd.im.repository.SessionRepository;
import cn.zd.im.server.handler.IMNioSocketAcceptor;
import cn.zd.im.server.model.IMSession;
import cn.zd.im.service.IMSessionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


@Service
public class IMSessionServiceImpl implements IMSessionService {

 	@Resource
 	private IMNioSocketAcceptor nioSocketAcceptor;

 	@Resource
	private SessionRepository sessionRepository;

	@Override
	public void save(IMSession session) {
		sessionRepository.save(session);
	}

	/*
	 *
	 * @param account 用户id
	 * @return
	 */
	@Override
	public IMSession get(String account) {
		 
		 IMSession session = sessionRepository.get(account);

		 if (session != null){
			 session.setSession(nioSocketAcceptor.getManagedSession(session.getNid()));
		 }

		 return session;
	}
 
	@Override
	public void remove(String account) {
		sessionRepository.remove(account);
	}

	@Override
	public List<IMSession> list() {
		return sessionRepository.findAll();
	}
	 
}
