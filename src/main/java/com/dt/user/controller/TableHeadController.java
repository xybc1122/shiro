package com.dt.user.controller;

import com.dt.user.config.BaseApiService;
import com.dt.user.config.ResponseBase;
import com.dt.user.model.TableHead;
import com.dt.user.model.UserInfo;
import com.dt.user.service.TableHeadService;
import com.dt.user.utils.GetCookie;
import com.dt.user.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class TableHeadController {

    @Autowired
    private TableHeadService tableHeadService;

    @GetMapping("/head")
    public ResponseBase findByHead(@RequestParam("menu_id") Long id, HttpServletRequest request) {
        String token = GetCookie.getToken(request);
        UserInfo user = JwtUtils.jwtUser(token);
        if (user != null) {
            List<TableHead> headList = tableHeadService.findByMenuIdHeadList(id, user.getUid());
            return BaseApiService.setResultSuccess(headList);
        }
        return BaseApiService.setResultError(null);
    }

    @GetMapping("/getByHead")
    public ResponseBase getByHead(@RequestParam("menu_id") Long mid) {
        TableHead head = tableHeadService.getTableHeadList(mid);
        return BaseApiService.setResultSuccess(head);
    }
}
