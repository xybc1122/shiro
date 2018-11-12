package com.dt.user.provider;


import com.dt.user.dto.UserDto;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;
public class UserProvider {


    public String findUserRole() {


        return null;
    }


    public String findUsers(UserDto userDto) {
        return new SQL() {{
            SELECT("u.uid,u.name,u.user_name,u.create_date,u.account_status,u.landing_time," +
                    "r.r_name");
            FROM("user_info AS u");
            INNER_JOIN("user_role AS ur ON(ur.u_id=u.uid)");
            INNER_JOIN("role AS r ON(r.rid=ur.r_id)");
            if(StringUtils.isNotBlank(userDto.getUserName())){
                WHERE("u.user_name=#{userName}");
            }
            if(StringUtils.isNotBlank(userDto.getName())){
                WHERE("u.name=#{name}");
            }
            if(StringUtils.isNotBlank(userDto.getrName())){
                WHERE("r.r_name=#{rName}");
            }
        }}.toString();
    }
}
