package com.dt.user.controller.UserServiceController;

import com.dt.user.config.BaseApiService;
import com.dt.user.config.ResponseBase;
import com.dt.user.model.TableHead;
import com.dt.user.service.TableHeadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class TableHeadController {

    @Autowired
    private TableHeadService tableHeadService;

    /**
     * 通过菜单ID来查询 对应的表头信息
     *
     * @return
     */
    @GetMapping("/head")
    public ResponseBase findByHead(@RequestParam("menu_id") Long mId) {
        List<TableHead> headList = tableHeadService.findByMenuIdHeadList(mId);
        return BaseApiService.setResultSuccess(headList);

    }

    /**
     * 通过一组menuId  in()查询一组菜单对应表头的数据
     *
     * @param mapHead
     * @return
     */
    @PostMapping("/getByHead")
    public ResponseBase getByHead(@RequestBody Map<String, Object> mapHead) {
        return BaseApiService.setResultSuccess(tableHeadService.getTableHeadList(mapHead));
    }

    /**
     * 通过一个菜单iD查询一个菜单对应表头的数据
     *
     * @param mid
     * @return
     */
    @GetMapping("/showByHead")
    public ResponseBase showByHead(@RequestParam("mId") Long mid) {
        return BaseApiService.setResultSuccess(tableHeadService.getTableHead(mid));
    }

    /**
     * 点击编辑 用菜单ID查询表头信息
     *
     * @return
     */
    @GetMapping("/findHeads")
    public ResponseBase findHeads(@RequestParam("mId") String mid) {
        return BaseApiService.setResultSuccess(tableHeadService.findByHeadList(Long.parseLong(mid)));
    }

    /**
     * 无条件查询所有表头
     */
    @GetMapping("/getHeadsList")
    public ResponseBase getHeadsList() {
        return BaseApiService.setResultSuccess(tableHeadService.findByHeadList(null));
    }
}
