package com.dt.user.mapper;

import com.dt.user.model.HrArchivesEmployee;
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
    @Select("SELECT`s_id`,`u_id`,`pt_id`,`employee_number`,`employee_name`,`employee_name_pinyin`,`employee_name_eng`,`sex`,`id_card`,`mobile_phone`,`nation_id`,`status`\n" +
            "FROM `hr_archives_employee`\n" +
            "WHERE `status` = 1 AND u_id IS NULL")
    List<HrArchivesEmployee> GetStaffList();

    /**
     * 绑定用户id跟员工id信息
     */
    @Update("UPDATE `hr_archives_employee`SET `u_id` = #{uid} WHERE `s_id` = #{sid}")
    int upStaffInfo(@Param("uid") Long uid, @Param("sid") Long sid);


//    /**
//     * 更新员工表信息
//     */
//    @UpdateProvider(type = UserProvider.class, method = "upStaff")
//    int upStaff(Map<String, Object> mapStaff);

}
