package com.dt.user.mapper;

import com.dt.user.model.AccountStatusOptions;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AccountStatusOptionsMapper {

    /**
     * 查询状态信息
     * @param hd
     * @return
     */
    @Select("SELECT `id`,`name` FROM `account_status_options` WHERE h_id=#{id}")
    List<AccountStatusOptions> getAccountStatusOptionsInfo(@Param("id") Integer hd);

}
