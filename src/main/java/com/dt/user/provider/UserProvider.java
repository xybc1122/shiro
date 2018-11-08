package com.dt.user.provider;

import com.dt.user.model.UserInfo;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.web.bind.annotation.ResponseBody;

public class UserProvider {


    public String findUserRole() {


        return null;
    }


    public String findUsers(UserInfo userInfo) {
        return new SQL() {{

        }}.toString();
    }
}
