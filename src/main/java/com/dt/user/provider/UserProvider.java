package com.dt.user.provider;


import com.dt.user.model.UserInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;
public class UserProvider {


    public String findUserRole() {


        return null;
    }


    public String findUsers(UserInfo userInfo) {
        return new SQL() {{
            SELECT("u.uid,u.name,u.user_name,u.create_date,u.account_status," +
                    "r.r_name");
            FROM("user_info AS u");
            INNER_JOIN("user_role AS ur ON(ur.u_id=u.uid)");
            INNER_JOIN("role AS r ON(r.rid=ur.r_id)");
            if(StringUtils.isNotBlank(userInfo.getUserName())){
                WHERE("u.user_name=#{userName}");
            }
            if(StringUtils.isNotBlank(userInfo.getName())){
                WHERE("u.name=#{name}");
            }
            if(StringUtils.isNotBlank(userInfo.getrName())){
                WHERE("r.r_name=#{rName}");
            }
        }}.toString();
    }
}
