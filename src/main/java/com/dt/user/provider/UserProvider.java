package com.dt.user.provider;


import com.dt.user.dto.UserDto;
import com.dt.user.utils.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

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
                    "GROUP_CONCAT(r.`r_name`)as rName,GROUP_CONCAT(r.`rid`)as rid,s.mobile_phone,u.effective_date,u.pwd_status");
            FROM("system_user_info AS u");
            LEFT_OUTER_JOIN("system_user_role_user AS ur ON(ur.u_id=u.uid)");
            LEFT_OUTER_JOIN("system_user_role AS r ON(r.rid=ur.r_id)");
            LEFT_OUTER_JOIN("`hr_archives_employee` AS s ON(u.uid=s.u_id)");
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


    public String upUserInfo(Map<String, Object> userMap) {
        return new SQL() {{
            UPDATE("`system_user_info`");
            if (userMap.get("pwd") != null && userMap.get("pwd") != "") {
                String pwd = (String) userMap.get("pwd");
                String userName = (String) userMap.get("uName");
                //md5盐值密码加密
                ByteSource salt = ByteSource.Util.bytes(userName);
                Object result = new SimpleHash("MD5", pwd, salt, 1024);
                SET("pwd=" + "'" + result + "'");
            }
            //如果勾选用户始终有效
            Boolean checkedUserAlways = (Boolean) userMap.get("checkedUserAlways");
            if (checkedUserAlways) {
                SET("effective_date=" + 0);
            } else if (userMap.get("effectiveDate") != null) {
                if (userMap.get("effectiveDate") instanceof Long) {
                    Long effectiveDate = (Long) userMap.get("effectiveDate");
                    SET("effective_date=" + effectiveDate);
                }
            }
            //如果勾选密码始终有效
            Boolean checkedPwdAlways = (Boolean) userMap.get("checkedPwdAlways");
            if (checkedPwdAlways) {
                SET("pwd_status=" + 0);
            } else if (userMap.get("pwdAlwaysInput") != null) {
                if (userMap.get("pwdAlwaysInput") instanceof Long) {
                    Integer pwdAlwaysInput = (Integer) userMap.get("pwdAlwaysInput");
                    SET("pwd_status=" + DateUtils.getRearDate(pwdAlwaysInput));
                }
            }
            if (userMap.get("accountStatus") != null) {
                Integer accountStatus = (Integer) userMap.get("accountStatus");
                SET("account_status=" + accountStatus);
            }
            Integer uid = (Integer) userMap.get("uid");
            WHERE("uid=" + uid);
        }}.toString();
    }


    public String findByRoleInfo(UserDto userDto) {
        return new SQL() {{
            SELECT(" r.r_name,GROUP_CONCAT(DISTINCT u.user_name)as userName,GROUP_CONCAT(DISTINCT m.name)as menuName FROM system_user_info AS u");
            INNER_JOIN("system_user_role_user AS ur ON ur.`u_id`=u.`uid`");
            INNER_JOIN("system_user_role AS r ON ur.`r_id`=r.`rid`");
            LEFT_OUTER_JOIN("system_user_role_menu AS rm ON rm.`r_id`=r.`rid`");
            LEFT_OUTER_JOIN("menu AS m ON m.`menu_id`=rm.`m_id`");
            GROUP_BY("r.rid");
        }}.toString();
    }

    public String delUserInfo(Map<String, Object> mapDel) {
        String uidIds = mapDel.get("uidIds").toString();
        String[] newIds = uidIds.split(",");
        List<String> ids = java.util.Arrays.asList(newIds);
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE `system_user_info`\n" +
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

    public String reUserInfo(Map<String, Object> mapDel) {
        String uidIds = mapDel.get("uidIds").toString();
        String[] newIds = uidIds.split(",");
        List<String> ids = java.util.Arrays.asList(newIds);
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE `system_user_info`\n" +
                "SET `del_user` = 0" +
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
