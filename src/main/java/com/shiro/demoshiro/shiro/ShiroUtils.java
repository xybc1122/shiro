package com.shiro.demoshiro.shiro;
import com.shiro.demoshiro.doman.User;
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
    public static User getUser() {
        Object object = getSubjct().getPrincipal();
        return (User)object;
    }
    public static Integer getUserId() {
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
