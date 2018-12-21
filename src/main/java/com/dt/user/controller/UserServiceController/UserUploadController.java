package com.dt.user.controller.UserServiceController;

import com.dt.user.config.BaseApiService;
import com.dt.user.config.ResponseBase;
import com.dt.user.model.UserInfo;
import com.dt.user.model.UserUpload;
import com.dt.user.service.UserUploadService;
import com.dt.user.utils.GetCookie;
import com.dt.user.utils.JwtUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/upload")
public class UserUploadController {

    @Autowired
    private UserUploadService userUploadService;

    /**
     * 获取上传记录
     *
     * @param sId
     * @param seId
     * @return
     */
    @GetMapping("/getInfo")
    public ResponseBase getInfo(@RequestParam("sId") String sId, @RequestParam("seId") String seId, HttpServletRequest request) {
        String token = GetCookie.getToken(request);
        UserInfo user = JwtUtils.jwtUser(token);
        if (user == null) {
            return BaseApiService.setResultError("token 失效");
        }
        List<UserUpload> userUploadList = userUploadService.getUserUploadInfo(user.getUid(), Long.parseLong(sId), Long.parseLong(seId));
        return BaseApiService.setResultSuccess(userUploadList);
    }

    /**
     * 删除上传记录信息
     *
     * @param id
     * @return
     */
    @GetMapping("/delInfo")
    public ResponseBase delInfo(@RequestParam("id") String id) {
        if (StringUtils.isNotEmpty(id)) {
            int count = userUploadService.delUploadInfo(Long.parseLong(id));
            if (count != 0) {
                return BaseApiService.setResultSuccess("删除成功~");
            }
        }
        return BaseApiService.setResultError("删除失败~");
    }

}
