package com.dt.user.config;

import org.apache.commons.lang3.StringUtils;
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

    }

    /**
     * 退出会话时触发
     *
     * @param session
     */
    @Override
    public void onStop(Session session) {
        //首先获得 redis 账号
        String valueName = baseRedisService.getStringKey(session.getId().toString());
        if (StringUtils.isNotBlank(valueName)) {
            baseRedisService.delData("sId" + valueName);
            baseRedisService.delData(session.getId().toString());
        }
    }

    /**
     * 会话过期时触发
     *
     * @param session
     */
    @Override
    public void onExpiration(Session session) {
        //首先获得 redis 账号
        String valueName = baseRedisService.getStringKey(session.getId().toString());
        if (StringUtils.isNotBlank(valueName)) {
            baseRedisService.delData("sId" + valueName);
            baseRedisService.delData(session.getId().toString());
        }
    }
}
