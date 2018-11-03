package com.dt.user.shiro;

import com.dt.user.model.UserInfo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;


public class ShiroUtils {

    public static Subject getSubjct() {

        return SecurityUtils.getSubject();
    }

    public static UserInfo getUser() {
        Object object = getSubjct().getPrincipal();
        return (UserInfo) object;
    }

    public static Long getUserId() {

        return getUser().getUid();
    }

    public static void logout() {

        getSubjct().logout();
    }

}
