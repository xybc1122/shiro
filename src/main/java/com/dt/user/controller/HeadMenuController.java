package com.dt.user.controller;

import com.dt.user.config.BaseApiService;
import com.dt.user.config.ResponseBase;
import com.dt.user.model.TbHeadMenu;
import com.dt.user.service.HeadMenuMapperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Map;

@RequestMapping("hm")
@RestController
public class HeadMenuController {

    @Autowired
    private HeadMenuMapperService headMenuMapperService;

    /**
     * 新增菜单跟表头关联数据
     * @param mhMap
     * @return
     */
    @SuppressWarnings("unchecked")
    @PostMapping("/saveHeadMenu")
    public ResponseBase saveHeadMenu(@RequestBody Map<String, Object> mhMap) {
        String mId = (String) mhMap.get("mId");
        List<Integer> thIds = (List<Integer>) mhMap.get("thIds");
        TbHeadMenu tbHeadMenu = new TbHeadMenu();
        for (int i = 0; i < thIds.size(); i++) {
            tbHeadMenu.setmId(Long.parseLong(mId));
            tbHeadMenu.setThId(thIds.get(i).longValue());
            headMenuMapperService.addHeadMenu(tbHeadMenu);
        }
        return BaseApiService.setResultSuccess("新增成功~");
    }

    /**
     * 删除菜单跟表头关联数据
     * @param mhMap
     * @return
     */
    @SuppressWarnings("unchecked")
    @PostMapping("/delTbHeadMenu")
    public ResponseBase delTbHeadMenu(@RequestBody Map<String, Object> mhMap) {
        String mId = (String) mhMap.get("mId");
        List<Integer> thIds = (List<Integer>) mhMap.get("thIds");
        TbHeadMenu tbHeadMenu = new TbHeadMenu();
        for (int i = 0; i < thIds.size(); i++) {
            tbHeadMenu.setmId(Long.parseLong(mId));
            tbHeadMenu.setThId(thIds.get(i).longValue());
                headMenuMapperService.delHeadMenu(tbHeadMenu);
        }
        return BaseApiService.setResultSuccess("删除成功~");
    }
}
