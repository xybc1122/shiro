package com.dt.user.controller.BasePublicController;

import com.dt.user.config.BaseApiService;
import com.dt.user.config.ResponseBase;
import com.dt.user.dto.ProductDto;
import com.dt.user.service.BasePublicService.BasicPublicProductService;
import com.dt.user.utils.PageInfoUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/product")
@RestController
public class BasicPublicProductController {

    @Autowired
    private BasicPublicProductService productService;

    /**
     * 获得产品信息
     *
     * @return
     */
    @PostMapping("/findByListProduct")
    public ResponseBase findByListProduct(@RequestBody ProductDto productDto) {
        if (productDto.getCurrentPage() != null && productDto.getPageSize() != null) {
            PageHelper.startPage(productDto.getCurrentPage(), productDto.getPageSize());
            List<ProductDto> basicPublicCurrencies = productService.findProductInfo(productDto);
            PageInfo<ProductDto> pageInfo = new PageInfo<>(basicPublicCurrencies);
            Integer currentPage = productDto.getCurrentPage();
            return BaseApiService.setResultSuccess(PageInfoUtils.getPage(pageInfo, currentPage));
        }
        return BaseApiService.setResultError("分页无参数");
    }


}
