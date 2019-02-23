package com.dt.user.controller.BasePublicController;

import com.dt.user.config.BaseApiService;
import com.dt.user.config.ResponseBase;
import com.dt.user.dto.PageDto;
import com.dt.user.model.BasePublicModel.BasicPublicArea;
import com.dt.user.service.BasePublicService.BasicPublicAreaService;
import com.dt.user.utils.PageInfoUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/reg")
public class BasicPublicAreaController {
    @Autowired
    private BasicPublicAreaService basicPublicAreaService;

    /**
     * 获得区域的信息
     *
     * @return
     */
    @PostMapping("/findByListRegion")
    public ResponseBase findByListCompany(@RequestBody PageDto pageDto) {
        List<BasicPublicArea> basicPublicAreaList;
        if (pageDto.getCurrentPage() != null && pageDto.getPageSize() != null) {
            PageHelper.startPage(pageDto.getCurrentPage(), pageDto.getPageSize());
            basicPublicAreaList = basicPublicAreaService.findByListArea();
            PageInfo<BasicPublicArea> pageInfo = new PageInfo<>(basicPublicAreaList);
            Integer currentPage = pageDto.getCurrentPage();
            return BaseApiService.setResultSuccess(PageInfoUtils.getPage(pageInfo, currentPage));
        }
        return BaseApiService.setResultSuccess("分页无参数", basicPublicAreaList);
    }

}
