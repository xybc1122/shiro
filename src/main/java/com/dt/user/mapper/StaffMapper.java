package com.dt.user.mapper;

import com.dt.user.model.Staff;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface StaffMapper {
    /**
     * 获得员工信息 没有关联用户的
     */
    @Select("SELECT`s_id`,`u_id`,`pt_id`,`mobile_phone`,`s_name`FROM `staff` WHERE u_id IS NULL")
    List<Staff> GetStaffList();
}
