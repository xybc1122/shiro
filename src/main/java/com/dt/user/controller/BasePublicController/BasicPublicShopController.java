package com.dt.user.controller.BasePublicController;

import com.dt.user.config.BaseApiService;
import com.dt.user.config.ResponseBase;
import com.dt.user.dto.PageDto;
import com.dt.user.model.BasePublicModel.BasicPublicCompany;
import com.dt.user.model.BasePublicModel.BasicPublicCurrency;
import com.dt.user.model.BasePublicModel.BasicPublicShop;
import com.dt.user.service.BasePublicService.BasicPublicShopService;
import com.dt.user.utils.PageInfoUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("shop")
public class BasicPublicShopController {
    @Autowired
    private BasicPublicShopService basicPublicShopService;

    /**
     * 获得店铺信息
     *
     * @return
     */
    @PostMapping("/findByListShop")
    public ResponseBase findByListShop(@RequestBody PageDto pageDto) {
        if (pageDto.getCurrentPage() != null && pageDto.getPageSize() != null) {
            PageHelper.startPage(pageDto.getCurrentPage(), pageDto.getPageSize());
            List<BasicPublicShop> basicPublicShopList = basicPublicShopService.findByListShop();
            PageInfo<BasicPublicShop> pageInfo = new PageInfo<>(basicPublicShopList);
            Integer currentPage = pageDto.getCurrentPage();
            return BaseApiService.setResultSuccess(PageInfoUtils.getPage(pageInfo, currentPage));
        }
        return BaseApiService.setResultError("分页无参数");
    }

}
