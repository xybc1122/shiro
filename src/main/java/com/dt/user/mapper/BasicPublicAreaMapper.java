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
    @Select("SELECT\n" +
            "  `area_id`,\n" +
            "  `area_number`,\n" +
            "  `area_name`,\n" +
            "  `area_eng`,\n" +
            "  `principal`,\n" +
            "  `remark`,\n" +
            "  `status`,\n" +
            "  `create_date`,\n" +
            "  `create_id_user`,\n" +
            "  `modify_date`,\n" +
            "  `modify_id_user`,\n" +
            "  `audit_date`,\n" +
            "  `audit_id_user`\n" +
            "FROM `basic_public_area`\n")
    List<BasicPublicArea> findByListArea();
}
