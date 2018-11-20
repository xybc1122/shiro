package com.dt.user.shiro;

import com.alibaba.fastjson.JSONObject;
import com.dt.user.config.ApplicationContextRegister;
import com.dt.user.config.BaseApiService;
import com.dt.user.config.BaseRedisService;
import com.dt.user.model.UserInfo;
import com.dt.user.service.MenuService;
import com.dt.user.service.RoleService;
import com.dt.user.service.UserService;
import com.dt.user.utils.GetCookie;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

import java.util.Set;

/**
 * AuthorizingRealm 用户认证
 */
public class UserRealm extends AuthorizingRealm {

    /**
     * 授权方法
     *
     * @param principals
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        System.out.println("```````````````````````````````````````++++++++++++++++++");
        Long userId = ShiroUtils.getUserId();
        RoleService roleService = ApplicationContextRegister.getBean(RoleService.class);
        MenuService menuService = ApplicationContextRegister.getBean(MenuService.class);
        //获得角色
        Set<String> roles = roleService.getAllRolesByUid(userId);
        //获得权限
        Set<String> perms = menuService.findByPermsMenuService(userId);
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.setRoles(roles);
        info.setStringPermissions(perms);
        return info;
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
        String userName = (String) token.getPrincipal();
        UserService userService = ApplicationContextRegister.getBean(UserService.class);
        //查询用户信息
        UserInfo user = userService.findByUser(userName);
        // 账号不存在 异常
        if (user == null) {
            throw new UnknownAccountException("用户或密码不正确");
        }
        // 账号锁定 异常
        if (user.getAccountStatus() == 1) {
            throw new LockedAccountException("账号已被锁定,请联系管理员");
        }
        if (user.getDelUser() == 1) {
            throw new ExpiredCredentialsException("账号凭着已过期/或删除 请联系管理员");
        }
        //盐值加密
        ByteSource salt = ByteSource.Util.bytes(user.getUserName());
        //参数1 user对象
        //参数2 数据库中获取的密码
        //参数3 盐值
        //参数4 父类的getName()
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user, user.getPwd(), salt, getName());
        return info;
    }

    public static void main(String[] args) {
        //盐值加密
        ByteSource salt = ByteSource.Util.bytes("bb");
        Object result = new SimpleHash("MD5", "8", salt, 1024);
        //d6f1c053e0a3faca08830aabca5f9885
        System.out.println(result);
    }
}
