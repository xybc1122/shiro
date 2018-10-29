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
            @Result(column = "status", property = "status")
    })
    UserInfo findByUser(@Param("userName") String userName);



}
