package com.dt.user.mapper;

import com.dt.user.dto.UserDto;
import com.dt.user.model.UserInfo;
import com.dt.user.provider.UserProvider;
import org.apache.ibatis.annotations.*;

import java.util.List;


@Mapper
public interface UserMapper {

    /**
     * 查找用户信息
     * @param userName
     * @return
     */
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


    /**
     * 查找用户的角色
     * @return
     */
    @SelectProvider(type = UserProvider.class,method = "findUserRole")
    List<UserInfo> findByUserAndRole();

    /**
     * 查找 账号管理信息
     */
    @SelectProvider(type = UserProvider.class,method = "findUsers")
    List<UserInfo> findByUsers(UserDto userDto);


    /**
     * 更新登陆时间
     */
    @Update("UPDATE `mydb`.`user_info`\n" +
            "SET `landing_time` = #{landingTime}\n" +
            "WHERE `uid` = #{uid};")
    int upUserLandingTime(UserInfo userInfo);

    /**
     * 通过 id查询 用
     */
    @Select("SELECT uid,user_name,`status`,`name` FROM user_info WHERE uid=#{uid}")
    UserInfo getSingleUser(@Param("uid") int id);
}
