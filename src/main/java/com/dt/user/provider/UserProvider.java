package com.dt.user.provider;


import com.dt.user.dto.UserDto;
import com.dt.user.utils.DateUtiils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;
import java.util.Date;

public class UserProvider {


    public String findUserRole() {


        return null;
    }


    public String findUsers(UserDto userDto) {
        return new SQL() {{
            SELECT("u.uid,u.name,u.user_name,u.create_date,u.account_status,u.landing_time," +
                    "GROUP_CONCAT(r.`r_name`)as rName,s.mobile_phone");
            FROM("user_info AS u");
            INNER_JOIN("user_role AS ur ON(ur.u_id=u.uid)");
            INNER_JOIN("role AS r ON(r.rid=ur.r_id)");
            LEFT_OUTER_JOIN("`staff` AS s ON(u.uid=s.u_id)");
            if (StringUtils.isNotBlank(userDto.getUserName())) {
                WHERE("u.user_name=#{userName}");
            }
            if (StringUtils.isNotBlank(userDto.getName())) {
                WHERE("u.name=#{name}");
            }
            if (StringUtils.isNotBlank(userDto.getrName())) {
                WHERE("r.r_name=#{rName}");
            }
            WHERE("del_user=0");
            GROUP_BY("u.uid");
        }}.toString();
    }


    public String upUserInfo(Map<String, Object> mapUser) {
        Long uid = Long.parseLong(mapUser.get("uid").toString());
        String name = mapUser.get("name").toString();
        String uLandingTime = mapUser.get("uLandingTime").toString();
        String uCreateDate = mapUser.get("uCreateDate").toString();
        Long createDate = DateUtiils.UTCLongODefaultString(uCreateDate);
        Long landingTime = DateUtiils.UTCLongODefaultString(uLandingTime);
        String uAccountStatus = mapUser.get("uAccountStatus").toString();
        return new SQL() {{
            UPDATE("`user_info`");
            if (StringUtils.isNotBlank(name)) {
                SET("name=" + "'" + name + "'");
            }
            if (landingTime != null) {
                SET("landing_time=" + landingTime);
            }
            if (createDate != null) {
                SET("create_date=" + createDate);
            }
            if (uAccountStatus != null) {
                SET("account_status=" + uAccountStatus);
            }
            WHERE("uid=" + uid);
        }}.toString();
    }

    public String upStaff(Map<String, Object> mapStaff) {
        Long uMobilePhone = Long.parseLong(mapStaff.get("uMobilePhone").toString());
        Long uid = Long.parseLong(mapStaff.get("uid").toString());
        return new SQL() {{
            UPDATE("`staff`");
            if (uMobilePhone != null) {
                SET("mobile_phone=" + uMobilePhone);
            }
            WHERE("u_id=" + uid);
        }}.toString();
    }


    public String findByRoleInfo(UserDto userDto) {
        return new SQL() {{
            SELECT(" r.r_name,GROUP_CONCAT(DISTINCT u.user_name)as userName,GROUP_CONCAT(DISTINCT m.name)as menuName FROM user_info AS u");
            INNER_JOIN("user_role AS ur ON ur.`u_id`=u.`uid`");
            INNER_JOIN("role AS r ON ur.`r_id`=r.`rid`");
            INNER_JOIN("role_menu AS rm ON rm.`r_id`=r.`rid`");
            INNER_JOIN("menu AS m ON m.`menu_id`=rm.`m_id`");
            GROUP_BY("r.r_name");
        }}.toString();
    }

    public String delUserInfo(Map<String, Object> mapDel) {
        String uidIds = mapDel.get("uidIds").toString();
        String[] newIds = uidIds.split(",");
        List<String> ids = java.util.Arrays.asList(newIds);
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE `user_info`\n" +
                "SET `del_user` = 1" +
                ",`del_date` = " + new Date().getTime() + "\n" +
                "WHERE uid in (");
        for (String id : ids) {
            if (ids.indexOf(id) > 0)
                sql.append(",");
            sql.append("'").append(id).append("'");
        }
        sql.append(")");
        return sql.toString();
    }
}
