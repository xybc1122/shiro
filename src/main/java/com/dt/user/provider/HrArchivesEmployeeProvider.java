package com.dt.user.provider;

import org.apache.ibatis.jdbc.SQL;

import java.util.Map;

public class HrArchivesEmployeeProvider {


    public String upHrArchives(Map<String, Object> hrMap) {
        return new SQL() {{
            UPDATE("`hr_archives_employee`");
            if (hrMap.get("uMobilePhone") != null) {
                String uMobilePhone = (String) hrMap.get("uMobilePhone");
                SET("mobile_phone=" + Long.parseLong(uMobilePhone));
            }
            Integer uid = (Integer) hrMap.get("uid");
            WHERE("u_id=" + uid);
        }}.toString();
    }

}
