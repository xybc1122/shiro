package com.dt.user.provider;


import com.dt.user.dto.UserDto;
import com.dt.user.shiro.ShiroUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;
import java.util.List;
import java.util.Map;
import java.util.Date;

public class UserProvider {


    public String findUsers(UserDto userDto) {
        return new SQL() {{
            SELECT("u.uid,u.name,u.computer_name,u.user_name,u.create_date,u.account_status,u.landing_time,u.version," +
                    "GROUP_CONCAT(r.`r_name`)as rName,GROUP_CONCAT(r.`rid`)as rid,s.mobile_phone,u.user_expiration_date,u.pwd_validity_period");
            FROM("system_user_info AS u");
            LEFT_OUTER_JOIN("system_user_role_user AS ur ON(ur.u_id=u.uid)");
            LEFT_OUTER_JOIN("system_user_role AS r ON(r.rid=ur.r_id)");
            LEFT_OUTER_JOIN("`hr_archives_employee` AS s ON(u.uid=s.u_id)");
            //用户账号
            if (StringUtils.isNotBlank(userDto.getUserName())) {
                WHERE("POSITION('" + userDto.getUserName() + "' IN u.`user_name`)");
            }
            //用户名
            if (StringUtils.isNotBlank(userDto.getName())) {
                WHERE("POSITION('" + userDto.getName() + "' IN u.`name`)");
            }
            //角色名字
            if (StringUtils.isNotBlank(userDto.getrName())) {
                WHERE("POSITION('" + userDto.getrName() + "' IN r.r_name)");
            }
            //密码有效期
            if (userDto.getPwdValidityPeriods() != null && userDto.getPwdValidityPeriods().size() > 0) {
                WHERE("u.pwd_validity_period BETWEEN  " + userDto.getPwdValidityPeriods().get(0) + " AND " + userDto.getPwdValidityPeriods().get(1) + "");
                //始终有效
            } else if (userDto.isPwdAlways()) {
                WHERE("u.pwd_validity_period=0");
            }
            //登陆时间
            if (userDto.getLandingTimes() != null && userDto.getLandingTimes().size() > 0) {
                WHERE("u.landing_time BETWEEN  " + userDto.getLandingTimes().get(0) + " AND " + userDto.getLandingTimes().get(1) + "");
            }
            //用户有效期间
            if (userDto.getUserExpirationDates() != null && userDto.getUserExpirationDates().size() > 0) {
                WHERE("u.user_expiration_date BETWEEN  " + userDto.getUserExpirationDates().get(0) + " AND " + userDto.getUserExpirationDates().get(1) + "");
                //始终有效
            } else if (userDto.isuAlways()) {
                WHERE("u.user_expiration_date=0");
            }
            //计算机名
            if (StringUtils.isNotBlank(userDto.getComputerName())) {
                WHERE("POSITION('" + userDto.getComputerName() + "' IN u.computer_name)");
            }
            //用户状态
            if (userDto.getAccountStatus() != null) {
                WHERE("u.account_status=#{accountStatus}");
            }
            //用户手机
            if (StringUtils.isNotBlank(userDto.getMobilePhone())) {
                WHERE("POSITION('" + userDto.getMobilePhone() + "' IN s.mobile_phone)");
            }
            //创建时间
            if (userDto.getCreateDates() != null && userDto.getCreateDates().size() > 0) {
                WHERE("u.create_date BETWEEN  " + userDto.getCreateDates().get(0) + " AND " + userDto.getCreateDates().get(1) + "");
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
                String userName = (String) userMap.get("userName");
                if (StringUtils.isNotBlank(userName)) {
                    //md5盐值密码加密
                    Object resultPwd = ShiroUtils.settingSimpleHash(userName, pwd);
                    SET("pwd=" + "'" + resultPwd + "'");
                }
            }
            //如果勾选用户始终有效
            Boolean pwdAlways = (Boolean) userMap.get("pwdAlways");
            if (pwdAlways != null) {
                if (pwdAlways) {
                    SET("user_expiration_date=" + 0);
                } else if (userMap.get("userExpirationDate") != null) {
                    Long userExpirationDate = (Long) userMap.get("userExpirationDate");
                    SET("user_expiration_date=" + userExpirationDate);
                }
            }
            //如果勾选密码始终有效
            Boolean uAlways = (Boolean) userMap.get("uAlways");
            if (uAlways != null) {
                if (uAlways) {
                    SET("pwd_validity_period=" + 0);
                } else if (userMap.get("pwdValidityPeriod") != null) {
                    Long pwdValidityPeriod = (Long) userMap.get("pwdValidityPeriod");
                    SET("pwd_validity_period=" + pwdValidityPeriod);
                }
            }
            if (userMap.get("accountStatus") != null) {
                Integer accountStatus = (Integer) userMap.get("accountStatus");
                SET("account_status=" + accountStatus);
            }
            //勾选了首次登陆修改密码
            Boolean checkedUpPwd = (Boolean) userMap.get("checkedUpPwd");
            if (checkedUpPwd != null) {
                SET("is_first_login=" + checkedUpPwd);
            }
            Integer version = (Integer) userMap.get("version");
            SET("version=" + version + "+1");
            Integer uid = (Integer) userMap.get("uid");
            WHERE("version=" + version);
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
