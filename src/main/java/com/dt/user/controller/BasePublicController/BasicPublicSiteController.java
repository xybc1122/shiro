package com.dt.user.controller.BasePublicController;

import com.dt.user.config.BaseApiService;
import com.dt.user.config.ResponseBase;
import com.dt.user.dto.SiteDto;
import com.dt.user.model.BasePublicModel.BasicPublicSite;
import com.dt.user.service.BasePublicService.BasicPublicSiteService;
import com.dt.user.utils.PageInfoUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/site")
public class BasicPublicSiteController {

    @Autowired
    private BasicPublicSiteService basicPublicSiteService;

    /**
     * 获得所有站点的信息
     *
     * @return
     */
    @PostMapping("/findByListSite")
    public ResponseBase findByListSite(@RequestBody SiteDto siteDto) {
        if (siteDto.getCurrentPage() != null && siteDto.getPageSize() != null) {
            PageHelper.startPage(siteDto.getCurrentPage(), siteDto.getPageSize());
            List<SiteDto> basicPublicSiteList = basicPublicSiteService.findBySiteList(siteDto);
            PageInfo<SiteDto> pageInfo = new PageInfo<>(basicPublicSiteList);
            Integer currentPage = siteDto.getCurrentPage();
            return BaseApiService.setResultSuccess(PageInfoUtils.getPage(pageInfo, currentPage));
        }
        return BaseApiService.setResultError("分页无参数");
    }

    /**
     * 通过店铺id获得所有站点信息
     *
     * @return
     */
    @GetMapping("/getByShopIdListSite")
    public ResponseBase getByShopIdListSite(@RequestParam("sId") String sId) {
        List<BasicPublicSite> shopIdSiteList = basicPublicSiteService.getShopIdTakeSiteList(Long.parseLong(sId));
        return BaseApiService.setResultSuccess(shopIdSiteList);
    }

}
