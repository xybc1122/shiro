package com.dt.user.mapper.BasePublicMapper;

import com.dt.user.dto.ShopDto;
import com.dt.user.model.BasePublicModel.BasicPublicShop;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

@Mapper
public interface BasicPublicShopMapper {

    /**
     * 查询店铺所有相关信息
     *
     * @return
     */
    @Select("SELECT s.`shop_id`,s.`number`,s.`shop_name`," +
            "s.`shop_name_eng`,s.`shop_short_code`,s.`principal`,s.status_id,c.company_name,p.platform_type_name\n" +
            "FROM `basic_public_shop`AS s\n" +
            "LEFT JOIN `basic_public_company`AS c ON c.`company_id`=s.`company_id`" +
            "LEFT JOIN `basic_public_platform_type`AS p ON p.`platform_type_id`=s.`platform_type_id`")
    @Results({
            @Result(column = "status_id", property = "systemLogStatus",
                    one = @One(
                            select = "com.dt.user.mapper.SystemLogStatusMapper.findSysStatusInfo",
                            fetchType = FetchType.EAGER
                    )
            )
    })
    List<ShopDto> findByListShop();

    /**
     * 查询店铺名字
     * @return
     */
    @Select("SELECT `shop_id`,`shop_name` from `basic_public_shop`")
    List<BasicPublicShop> getByListShopName();
}
