package com.dt.user.mapper;

import com.dt.user.model.BasicPublicArea;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface BasicPublicAreaMapper {

    /**
     * 查询区域所有相关信息
     * @return
     */
    @Select("SELECT`area_id`,`area_number`,`area_name`,`area_eng`,`principal`,\n" +
            "`remark`,`status`,`create_date`,`create_id_user`,`modify_date`,\n" +
            "`modify_id_user`,`audit_date`,`audit_id_user`\n" +
            "FROM `basic_public_area`\n")
    List<BasicPublicArea> findByListArea();
}
