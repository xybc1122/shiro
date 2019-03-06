package com.dt.user.config;

import com.dt.user.model.UserInfo;
import com.dt.user.shiro.ShiroUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListener;

import java.util.concurrent.atomic.AtomicInteger;

public class ShiroSessionListener extends BaseApiService implements SessionListener {

    /**
     * 统计在线人数
     * juc包下线程安全自增
     */
    private final AtomicInteger sessionCount = new AtomicInteger(0);

    /**
     * 会话创建时触发
     *
     * @param session
     */
    @Override
    public void onStart(Session session) {
        System.out.println("ShiroSessionListener session {} 被创建" + session.getId());
        //会话创建，在线人数加一
        sessionCount.incrementAndGet();
    }

    /**
     * 退出会话时触发
     *
     * @param session
     */
    @Override
    public void onStop(Session session) {
        System.out.println("ShiroSessionListener session {} 被退出" + session.getId());
        baseRedisService.delData(session.getId().toString());
//        UserInfo u = ShiroUtils.getUser();
//        baseRedisService.delData(u.getUserName());
        //会话退出,在线人数减一
        sessionCount.decrementAndGet();
    }

    /**
     * 会话过期时触发
     *
     * @param session
     */
    @Override
    public void onExpiration(Session session) {
        System.out.println("ShiroSessionListener session {} 被过期" + session.getId());
        baseRedisService.delData(session.getId().toString());
//        UserInfo u = ShiroUtils.getUser();
//        baseRedisService.delData(u.getUserName());
        //会话过期,在线人数减一
        sessionCount.decrementAndGet();
    }

    /**
     * 获取在线人数使用
     *
     * @return
     */
    public AtomicInteger getSessionCount() {
        return sessionCount;
    }

}
