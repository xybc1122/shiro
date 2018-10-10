package com.shiro.demoshiro.shiro;

import com.shiro.demoshiro.config.ApplicationContextRegister;
import com.shiro.demoshiro.doman.User;
import com.shiro.demoshiro.mapper.UserMapper;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * AuthorizingRealm 用户认证
 */

public class SecondRealm extends AuthorizingRealm {
    /**
     * 授权方法
     *
     * @param principals
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        return null;
    }

    /**
     * doGetAuthenticationInfo 认证方法
     *
     * @param token
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        System.out.println("----------------->SecondRealm doGetAuthenticationInfo");
        //获得表单的用户输入
        String userName = (String) token.getPrincipal();
        UserMapper userMapper = ApplicationContextRegister.getBean(UserMapper.class);

        //查询用户信息
        User user = userMapper.findByUser(userName);
        // 账号不存在 异常
        if (user == null) {
            throw new UnknownAccountException("用户或密码不正确");
        }
        // 账号锁定 异常
        if (user.getState() == 0) {
            throw new LockedAccountException("账号已被锁定,请联系管理员");
        }
        //盐值加密
        ByteSource salt = ByteSource.Util.bytes(user.getUsername());
        //参数1 user对象
        //参数2 数据库中获取的密码
        //参数3 盐值
        //参数4 父类的getName()
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user, user.getPassword(), salt, getName());
        return info;
    }

    public static void main(String[] args) {
        //盐值加密
        ByteSource salt = ByteSource.Util.bytes("ok");
        Object result = new SimpleHash("SHA1", "8", salt, 1024);
        System.out.println(result);
    }
}
