package com.dt.user.mapper.BasePublicMapper;

import com.dt.user.dto.SiteDto;
import com.dt.user.model.BasePublicModel.BasicPublicSite;
import com.dt.user.provider.BasicPublicSiteProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

@Mapper
public interface BasicPublicSiteMapper {
    /**
     * 查询所有站点信息 //包含区域信息
     */
    @SelectProvider(type = BasicPublicSiteProvider.class, method = "findSite")
    List<BasicPublicSite> findBySiteList(SiteDto siteDto);

    /**
     * 通过店铺id查询站点信息
     */
    @Select("SELECT\n" +
            "  se.`site_id`,\n" +
            "  se.`site_name`\n" +
            "FROM `basic_public_site` AS se\n" +
            "LEFT JOIN `basic_public_shop_site` AS ss ON ss.`site_id`=se.`site_id`\n" +
            "LEFT JOIN `basic_public_shop` AS s ON s.`shop_id`=ss.`shop_id`\n" +
            "WHERE s.`shop_id`=#{sId}")
    List<BasicPublicSite> getShopIdTakeSiteList(@Param("sId") Long sId);

    /**
     * 洲 业务
     * 通过url 去查询site ID
     *
     * @param url
     * @return
     */
    @Select("SELECT `site_id` FROM `basic_public_site`" +
            "WHERE url=#{url}")
    Long getSiteId(@Param("url") String url);

}
