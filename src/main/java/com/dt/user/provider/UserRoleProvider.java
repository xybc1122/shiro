package com.dt.user.provider;

import com.dt.user.model.UserRole;

import java.util.List;
import java.util.Map;

public class UserRoleProvider {

    /**
     * 添加信息
     *
     * @param urMap
     * @return
     */
    public String addUserRole(Map<String, Object> urMap) {
        UserRole ur;
        List<UserRole> urList = (List<UserRole>) urMap.get("userRoleList");
        StringBuilder sb = new StringBuilder();
        sb.append("insert into system_user_role_user(u_id,r_id)values");
        for (int i = 0; i < urList.size(); i++) {
            ur = urList.get(i);
            //一个员工添加多个角色
            if (null != ur.getrIds()) {
                List<Integer> rIds = urList.get(i).getrIds();
                for (int j = 0; j < rIds.size(); j++) {
                    Long rId = rIds.get(j).longValue();
                    sb.append("(" + ur.getuId() + "," + rId + "),");
                }
            }
            //一个角色添加多个员工
            if (null != ur.getuIds()) {
                List<Integer> uIds = urList.get(i).getuIds();
                for (int j = 0; j < uIds.size(); j++) {
                    Long uId = uIds.get(j).longValue();
                    sb.append("(" + uId + "," + ur.getrId() + "),");
                }
            }
        }
        return sb.toString().substring(0, sb.length() - 1);
    }
}
