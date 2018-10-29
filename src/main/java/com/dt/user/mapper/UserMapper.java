package com.dt.user.mapper;

import com.dt.user.model.UserInfo;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;


@Mapper
public interface UserMapper {


    @Select("select uid, user_name,pwd,status,create_date,create_id_user,up_id_user,up_date,effective_date,pwd_status," +
            "user_status  from user_info where user_name=#{userName}")
    @Results({
            //column数据的字段  property 实体类的字段
            @Result(id = true, column = "uid", property = "uid"),
            @Result(column = "user_name", property = "userName"),
            @Result(column = "pwd", property = "pwd"),
            @Result(column = "status", property = "status"),
            @Result(column = "create_date", property = "createDate"),
            @Result(column = "create_id_user", property = "createIdUser"),
            @Result(column = "up_id_user", property = "upIdUser"),
            @Result(column = "up_date", property = "upDate"),
            @Result(column = "effective_date", property = "effectiveDate"),
            @Result(column = "pwd_status", property = "pwdStatus"),
            @Result(column = "user_status", property = "userStatus")
    })
    UserInfo findByUser(@Param("userName") String userName);



}
