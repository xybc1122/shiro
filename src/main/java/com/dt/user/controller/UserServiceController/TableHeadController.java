package com.dt.user.controller.UserServiceController;

import com.dt.user.config.BaseApiService;
import com.dt.user.config.ResponseBase;
import com.dt.user.model.TableHead;
import com.dt.user.model.UserInfo;
import com.dt.user.service.TableHeadService;
import com.dt.user.utils.GetCookie;
import com.dt.user.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
public class TableHeadController {

    @Autowired
    private TableHeadService tableHeadService;

    /**
     * 通过uid 来查询 对应的表头信息
     *
     * @param id
     * @param request
     * @return
     */
    @GetMapping("/head")
    public ResponseBase findByHead(@RequestParam("menu_id") Long id, HttpServletRequest request) {
        UserInfo user = GetCookie.getUser(request);
        if (user == null) {
            return BaseApiService.setResultError("用户无效~");
        }
        List<TableHead> headList = tableHeadService.findByMenuIdHeadList(id, user.getUid());
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
     * 查询所有的表头信息  无查询条件
     *
     * @return
     */
    @GetMapping("/findHeads")
    public ResponseBase findHeads(@RequestParam("mId") String mid) {

        return BaseApiService.setResultSuccess(tableHeadService.findByHeadList(Long.parseLong(mid)));
    }
}
