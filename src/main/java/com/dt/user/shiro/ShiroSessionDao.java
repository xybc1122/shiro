package com.dt.user.shiro;

import com.dt.user.config.BaseRedisService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.CachingSessionDAO;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


public class ShiroSessionDao extends CachingSessionDAO {

    @Autowired
    private BaseRedisService baseRedisService;

    private ConcurrentMap<Serializable, Session> sessions;

    public ShiroSessionDao() {
        this.sessions = new ConcurrentHashMap<>();
    }
    /**
     * 踢出用户
     *
     * @param uName
     */
    public String kickOutUser(String uName) {
        try {
            //获得redis里面的session 会话
            String redisSessionId = baseRedisService.getStringKey("sId" + uName);
            if (StringUtils.isNotBlank(redisSessionId)) {
                Session session = doReadSession(redisSessionId);
                //删除这个踢出session
                if (session != null) {
                    session.stop();
                    delete(session);
                    //删除缓存session Id
                    baseRedisService.delData("sId" + uName);
                }
            }
        } catch (Exception e) {
            return null;
        }
        return "ok";
    }

    @Override
    protected void doUpdate(Session session) {

    }

    @Override
    protected void doDelete(Session session) {

    }

    @Override
    protected Serializable doCreate(Session session) {
        return null;
    }

    @Override
    protected Session doReadSession(Serializable sessionId) {
        System.out.println(sessions.get(sessionId));
        return sessions.get(sessionId);
    }
}
