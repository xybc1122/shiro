package com.dt.user.controller.BasePublicController;

import com.dt.user.config.BaseApiService;
import com.dt.user.config.ResponseBase;
import com.dt.user.dto.ShopDto;
import com.dt.user.dto.UserDto;
import com.dt.user.model.BasePublicModel.BasicPublicShop;
import com.dt.user.service.BasePublicService.BasicPublicShopService;
import com.dt.user.utils.PageInfoUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shop")
public class BasicPublicShopController {
    @Autowired
    private BasicPublicShopService basicPublicShopService;

    /**
     * 获得店铺信息
     *
     * @return
     */
    @PostMapping("/findByListShop")
    public ResponseBase findByListShop(@RequestBody ShopDto shopDto) {
        if (shopDto.getCurrentPage() != null && shopDto.getPageSize() != null) {
            PageHelper.startPage(shopDto.getCurrentPage(), shopDto.getPageSize());
            List<ShopDto> basicPublicShopList = basicPublicShopService.findByListShop();
            PageInfo<ShopDto> pageInfo = new PageInfo<>(basicPublicShopList);
            Integer currentPage = shopDto.getCurrentPage();
            return BaseApiService.setResultSuccess(PageInfoUtils.getPage(pageInfo, currentPage));
        }
        return BaseApiService.setResultError("分页无参数");
    }

    /**
     * 获得店铺名字
     * @return
     */
    @GetMapping("/getListShopName")
    public ResponseBase findByListShop() {
        List<BasicPublicShop> nameList = basicPublicShopService.getByListShopName();
        return BaseApiService.setResultSuccess(nameList);
    }

}
