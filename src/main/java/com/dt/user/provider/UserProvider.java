package com.dt.user.provider;


import com.dt.user.dto.UserDto;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

import java.util.List;
import java.util.Map;
import java.util.Date;

public class UserProvider {


    public String findUsers(UserDto userDto) {
        return new SQL() {{
            SELECT("u.uid,u.name,u.user_name,u.create_date,u.account_status,u.landing_time," +
                    "GROUP_CONCAT(r.`r_name`)as rName,GROUP_CONCAT(r.`rid`)as rid,s.mobile_phone,u.effective_date,u.pwd_status");
            FROM("system_user_info AS u");
            LEFT_OUTER_JOIN("system_user_role_user AS ur ON(ur.u_id=u.uid)");
            LEFT_OUTER_JOIN("system_user_role AS r ON(r.rid=ur.r_id)");
            LEFT_OUTER_JOIN("`hr_archives_employee` AS s ON(u.uid=s.u_id)");
            //用户账号
            if (StringUtils.isNotBlank(userDto.getUserName())) {
                WHERE("POSITION('"+userDto.getUserName()+"' IN u.`user_name`)");
            }
            //用户名
            if (StringUtils.isNotBlank(userDto.getName())) {
                WHERE("POSITION('"+userDto.getName()+"' IN u.`name`)");
            }
            //角色名字
            if (StringUtils.isNotBlank(userDto.getrName())) {
                WHERE("POSITION('"+userDto.getrName()+"' IN r.r_name)");
            }
            //密码有效期
            if (userDto.getPwdStatus() != null) {
                WHERE("u.pwd_status=#{pwdStatus}");
                //始终有效
            } else if (userDto.isPwdAlways()) {
                WHERE("u.pwd_status=0");
            }
            //登陆时间
            if (userDto.getLandingTime() != null) {
                WHERE("u.landing_time=#{landingTime}");
            }
            //用户有效期间
            if (userDto.getEffectiveDate() != null) {
                WHERE("u.effective_date=#{effectiveDate}");
                //始终有效
            } else if (userDto.isuAlways()) {
                WHERE("u.effective_date=0");
            }
            //计算机名
            if (StringUtils.isNotBlank(userDto.getComputerName())) {
                WHERE("POSITION('"+userDto.getComputerName()+"' IN u.computer_name)");
            }
            //用户状态
            if (userDto.getAccountStatus() != null) {
                WHERE("u.account_status=#{accountStatus}");
            }
            //用户手机
            if (StringUtils.isNotBlank(userDto.getMobilePhone())) {
                WHERE("POSITION('"+userDto.getMobilePhone()+"' IN s.mobile_phone)");
            }
            WHERE("del_user=0");
            GROUP_BY("u.uid");
        }}.toString();
    }


    public String upUserInfo(Map<String, Object> userMap) {
        return new SQL() {{
            UPDATE("`system_user_info`");
            String name = (String) userMap.get("name");
            if (StringUtils.isNotBlank(name)) {
                SET("name=" + "'" + name + "'");
            }
            String pwd = (String) userMap.get("pwd");
            if (StringUtils.isNotBlank(pwd)) {
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
                Long effectiveDate = (Long) userMap.get("effectiveDate");
                SET("effective_date=" + effectiveDate);
            }
            //如果勾选密码始终有效
            Boolean checkedPwdAlways = (Boolean) userMap.get("checkedPwdAlways");
            if (checkedPwdAlways) {
                SET("pwd_status=" + 0);
            } else if (userMap.get("pwdAlwaysInput") != null) {
                Long pwdAlwaysInput = (Long) userMap.get("pwdAlwaysInput");
                SET("pwd_status=" + pwdAlwaysInput);
            }
            if (userMap.get("accountStatus") != null) {
                Integer accountStatus = (Integer) userMap.get("accountStatus");
                SET("account_status=" + accountStatus);
            }
            //勾选了首次登陆修改密码
            Boolean checkedUpPwd = (Boolean) userMap.get("checkedUpPwd");
            SET("is_first_login=" + checkedUpPwd);

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
