package com.dt.user.mapper;

import com.dt.user.model.Staff;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface StaffMapper {
    /**
     * 获得员工信息 没有关联用户的
     */
    @Select("SELECT`s_id`,`u_id`,`pt_id`,`mobile_phone`,`s_name`FROM `staff` WHERE u_id IS NULL")
    List<Staff> GetStaffList();

    /**
     * 绑定用户id跟员工id信息
     */
    @Update("UPDATE `staff`SET `u_id` = #{uid} WHERE `s_id` = #{sid}")
    int upStaffInfo(@Param("uid") Long uid, @Param("sid") Long sid);


//    /**
//     * 更新员工表信息
//     */
//    @UpdateProvider(type = UserProvider.class, method = "upStaff")
//    int upStaff(Map<String, Object> mapStaff);

}
