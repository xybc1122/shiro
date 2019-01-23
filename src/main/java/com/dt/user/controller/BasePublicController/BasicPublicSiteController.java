package com.dt.user.controller.BasePublicController;

import com.dt.user.config.BaseApiService;
import com.dt.user.config.ResponseBase;
import com.dt.user.dto.PageDto;
import com.dt.user.model.BasePublicModel.BasicPublicShop;
import com.dt.user.model.BasePublicModel.BasicPublicSite;
import com.dt.user.service.BasePublicService.BasicPublicSiteService;
import com.dt.user.utils.PageInfoUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("site")
public class BasicPublicSiteController {

    @Autowired
    private BasicPublicSiteService basicPublicSiteService;

    /**
     * 获得所有站点的信息
     *
     * @return
     */
    @PostMapping("/findByListSite")
    public ResponseBase findByListSite(@RequestBody PageDto pageDto) {
        if (pageDto.getCurrentPage() != null && pageDto.getPageSize() != null) {
            PageHelper.startPage(pageDto.getCurrentPage(), pageDto.getPageSize());
            List<BasicPublicSite> basicPublicSiteList = basicPublicSiteService.findBySiteList();
            PageInfo<BasicPublicSite> pageInfo = new PageInfo<>(basicPublicSiteList);
            Integer currentPage = pageDto.getCurrentPage();
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
