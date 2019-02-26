package com.dt.user.mapper;

import com.dt.user.model.SystemLogStatus;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SystemLogStatusMapper {

    @Select("SELECT\n" +
            "`status_id`,`remark`,`status`,\n" +
            "`create_date`,`create_user`,`modify_date`,\n" +
            "`modify_user`,`audit_date`,`audit_user`\n" +
            "FROM `system_log_status` where status_id = #{statusId}")
    SystemLogStatus findSysStatusInfo(@Param("statusId") Long statusId);
}
