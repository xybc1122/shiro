package com.dt.user.mapper;

import com.dt.user.dto.UserDto;
import com.dt.user.model.UserInfo;
import com.dt.user.provider.UserProvider;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;
import java.util.Map;


@Mapper
public interface UserMapper {

    /**
     * 查找用户信息
     *
     * @param userName
     * @return
     */
    @Select("select uid, user_name,pwd,status,create_date,create_id_user,up_id_user,up_date,effective_date,pwd_status," +
            "user_status,account_Status,name  from user_info where user_name=#{userName}")
    UserInfo findByUser(@Param("userName") String userName);


    /**
     * 查找用户的角色
     *
     * @return
     */
    @SelectProvider(type = UserProvider.class, method = "findUserRole")
    List<UserInfo> findByUserAndRole();

    /**
     * 查找 账号管理信息
     */
    @SelectProvider(type = UserProvider.class, method = "findUsers")
    List<UserInfo> findByUsers(UserDto userDto);


    /**
     * 更新登陆时间
     */
    @Update("UPDATE `mydb`.`user_info`\n" +
            "SET `landing_time` = #{landingTime}\n" +
            "WHERE `uid` = #{uid};")
    int upUserLandingTime(UserInfo userInfo);

    /**
     * 通过 id查询 用户 跟角色
     */

    @Select("SELECT uid,user_name,`status`,`name` FROM user_info WHERE uid=#{uid}")
    @Results({
            @Result(id=true,column="uid",property="uid"),
            @Result(column="uid",property="roles",
                    many=@Many(
                            select="com.dt.user.mapper.RolesMapper.getAllRolesByUid",
                            fetchType=FetchType.LAZY
                    )
            )
    })
    UserInfo getSingleUser(@Param("uid") Long uid);


    /**
     * 更新用户信息
     */
    @UpdateProvider(type = UserProvider.class, method = "upUserInfo")
    int upUser(Map<String, Object> mapUser);


    /**
     * 更新员工表信息
     */
    @UpdateProvider(type = UserProvider.class, method = "upStaff")
    int upStaff(Map<String, Object> mapStaff);

    /**
     * 查询一个角色下的所有用户跟 菜单
     *
     * @param userDto
     * @return
     */
    @SelectProvider(type = UserProvider.class, method = "findByRoleInfo")
    List<UserInfo> findByRoleInfo(UserDto userDto);

}
