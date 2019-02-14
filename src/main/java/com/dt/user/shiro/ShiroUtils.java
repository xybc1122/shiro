package com.dt.user.shiro;

import com.dt.user.config.BaseApiService;
import com.dt.user.model.UserInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ShiroUtils extends BaseApiService {

    @Autowired
    private SessionDAO sessionDAO;

    public static Subject getSubjct() {

        return SecurityUtils.getSubject();
    }

    public static UserInfo getUser() {
        Object object = getSubjct().getPrincipal();
        return (UserInfo) object;
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
                Session session = sessionDAO.readSession(redisSessionId);
                //删除这个踢出session
                if(session !=null){
                    session.stop();
                    sessionDAO.delete(session);
                    //删除缓存session Id
                    baseRedisService.delData("sId" + uName);
                }
            }
        }catch (Exception e){
            return null;
        }
        return "ok";
    }


    //获得用户ID
    public static Long getUserId() {
        return getUser().getUid();
    }

    public static void logout() {
        getSubjct().logout();
    }

}
