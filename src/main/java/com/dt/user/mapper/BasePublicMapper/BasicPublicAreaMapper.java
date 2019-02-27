package com.dt.user.mapper.BasePublicMapper;

import com.dt.user.dto.AreaDto;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

@Mapper
public interface BasicPublicAreaMapper {

    /**
     * 查询区域所有相关信息
     *
     * @return
     */
    @Select("SELECT`area_id`,`number`,`area_name`,`area_name_eng`,area_short_name_eng,status_id \n" +
            "FROM `basic_public_area`")
    @Results({
            @Result(column = "status_id", property = "systemLogStatus",
                    one = @One(
                            select = "com.dt.user.mapper.SystemLogStatusMapper.findSysStatusInfo",
                            fetchType = FetchType.EAGER
                    )
            )
    })
    List<AreaDto> findByListArea();

}
