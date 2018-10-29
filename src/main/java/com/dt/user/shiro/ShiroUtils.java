package com.dt.user.shiro;
import com.dt.user.model.UserInfo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

import java.security.Principal;
import java.util.Collection;
import java.util.List;

public class ShiroUtils {
    @Autowired
    private static SessionDAO sessionDAO;

    public static Subject getSubjct() {
        return SecurityUtils.getSubject();
    }
    public static UserInfo getUser() {
        Object object = getSubjct().getPrincipal();
        return (UserInfo)object;
    }
    public static Long getUserId() {

        return getUser().getUid();
    }
    public static void logout() {
        getSubjct().logout();
    }

    public static List<Principal> getPrinciples() {
        List<Principal> principals = null;
        Collection<Session> sessions = sessionDAO.getActiveSessions();
        return principals;
    }
    
    public static boolean isAdminAndOperator() {
    	Subject subject = getSubjct();
    	boolean flag1 = subject.hasRole("admin");
		boolean flag2 = subject.hasRole("operator");
		return flag1|flag2;
    }
}
