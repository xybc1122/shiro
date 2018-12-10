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
            "account_Status,name,del_user  from system_user_info where user_name=#{userName}")
    UserInfo findByUser(@Param("userName") String userName);

    /**
     * 查找所有用户信息
     */
    @Select("select uid ,user_name  from system_user_info")
    List<UserInfo> getByUsers();

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
    @Update("UPDATE `system_user_info`\n" +
            "SET `landing_time` = #{landingTime}\n" +
            "WHERE `uid` = #{uid};")
    int upUserLandingTime(UserInfo userInfo);

    /**
     * 通过 id查询 用户 跟角色
     */

    @Select("SELECT uid,user_name,`status`,`name` FROM system_user_info WHERE uid=#{uid}")
    @Results({
            @Result(id = true, column = "uid", property = "uid"),
            @Result(column = "uid", property = "roles",
                    many = @Many(
                            select = "com.dt.user.mapper.RoleMapper.getAllRolesByUid",
                            fetchType = FetchType.LAZY
                    )
            )
    })
    UserInfo getSingleUser(@Param("uid") Long uid);

    /**
     * 更新用户信息
     */
    @UpdateProvider(type = UserProvider.class, method = "upUserInfo")
    int upUser(Map<String, Object> userMap);

    /**
     * 单个删除或批量删除用户信息
     */
    @UpdateProvider(type = UserProvider.class, method = "delUserInfo")
    int delUserInfo(@Param("uidIds") String uidIds);

    /**
     * 单个恢复或批量恢复用户信息
     */
    @UpdateProvider(type = UserProvider.class, method = "reUserInfo")
    int reUserInfo(@Param("uidIds") String uidIds);

    /**
     * 查询被删除的用户信息
     */
    @Select("select uid, user_name,del_date,name from system_user_info where del_user=1")
    List<UserInfo> findByDelUserInfo();

    /**
     * 注册用户验证用户是否存在
     */
    @Select("SELECT uid FROM system_user_info WHERE user_name=#{userName}")
    UserInfo getUserName(@Param("userName") String userName);

    /**
     * 新增一个用户
     */
    @Insert("insert into system_user_info(user_name,pwd,create_date,create_id_user,effective_date,pwd_status,account_status,name) "
            + "values(#{userName},#{pwd},#{createDate},#{createIdUser},#{effectiveDate},#{pwdStatus},#{accountStatus},#{name})")
    @Options(useGeneratedKeys = true, keyProperty = "uid", keyColumn = "uid")
    int saveUserInfo(UserInfo userInfo);
}
