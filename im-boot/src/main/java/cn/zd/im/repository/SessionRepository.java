package cn.zd.im.repository;

import cn.zd.im.server.model.IMSession;
import org.springframework.stereotype.Repository;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/*
 * 正式场景下，使用redis或者数据库来存储session信息
 */
@Repository
public class SessionRepository {

    private ConcurrentHashMap<String, IMSession> map = new ConcurrentHashMap<>();


    public void save(IMSession session){
        map.put(session.getAccount(),session);
    }

    public IMSession get(String account){
        return map.get(account);
    }

    public void remove(String account){
        map.remove(account);
    }

    public List<IMSession> findAll(){
        return new LinkedList<>(map.values());
    }
}
