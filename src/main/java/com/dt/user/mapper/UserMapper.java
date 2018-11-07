package com.dt.user.mapper;

import com.dt.user.model.UserInfo;
import com.dt.user.provider.UserProvider;
import org.apache.ibatis.annotations.*;

import java.util.List;


@Mapper
public interface UserMapper {


    @Select("select uid, user_name,pwd,status,create_date,create_id_user,up_id_user,up_date,effective_date,pwd_status," +
            "user_status,account_Status,name  from user_info where user_name=#{userName}")
    @Results({
            //column数据的字段  property 实体类的字段
            @Result(id = true, column = "uid", property = "uid")
//            @Result(column="uid",property="roles",
//                    many=@Many(
//                            select="com.dt.user.mapper.RolesMapper.getAllRolesByUid",
//                            fetchType=FetchType.LAZY
//                    )
//            )
    })
    UserInfo findByUser(@Param("userName") String userName);

    @SelectProvider(type = UserProvider.class,method = "findUserRole")
    List<UserInfo> findByUserAndRole();
}
