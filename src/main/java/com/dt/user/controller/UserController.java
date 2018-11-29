package com.dt.user.controller;

import com.dt.user.config.BaseApiService;
import com.dt.user.config.ResponseBase;
import com.dt.user.dto.UserDto;
import com.dt.user.model.UserInfo;
import com.dt.user.model.UserRole;
import com.dt.user.service.StaffService;
import com.dt.user.service.UserRoleService;
import com.dt.user.service.UserService;
import com.dt.user.utils.DateUtils;
import com.dt.user.utils.GetCookie;
import com.dt.user.utils.JwtUtils;
import com.dt.user.utils.PageInfoUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RequestMapping("/user")
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private StaffService staffService;

    @Autowired
    private UserRoleService userRoleSerivce;

    @RequestMapping("/index")
    public ResponseBase index() {
        return BaseApiService.setResultSuccess("我来到了用户页面");
    }

    /**
     * 查看用户信息
     *
     * @param userDto
     * @return
     */
    @PostMapping("/show")
    public ResponseBase showUsers(@RequestBody UserDto userDto) {
        PageHelper.startPage(userDto.getCurrentPage(), userDto.getPageSize());
        List<UserInfo> listUser = userService.findByUsers(userDto);
        //获得一些信息
        PageInfo<UserInfo> pageInfo = new PageInfo<>(listUser);
        Integer currentPage = userDto.getCurrentPage();
        return BaseApiService.setResultSuccess(PageInfoUtils.getPage(pageInfo, currentPage));
    }

    /**
     * 更新用户信息
     *
     * @return
     */
    //shiro权限控制
    @RequiresPermissions("sys:user:up")
    @PostMapping("/upUserInfo")
    public ResponseBase userInfoUp(@RequestBody Map<String, Object> userMap) {
        System.out.println(userMap);
        userService.upUser(userMap);
        return BaseApiService.setResultSuccess();
    }

    /**
     * 删除用户信息
     *
     * @param uidIds
     * @return
     */
    //shiro权限控制
    @RequiresPermissions("user:del")
    @PostMapping("/delUserInfo")
    public ResponseBase userInfoDel(@RequestBody Map<String, Object> delMap) {
        int count = userService.delUserInfo(delMap.get("ids").toString());
        if (count > 0) {
            return BaseApiService.setResultSuccess(count);
        }
        return BaseApiService.setResultError("删除失败~");
    }

    /**
     * 恢复用户信息
     *
     * @param uidIds
     * @return
     */
    //shiro权限控制
    @PostMapping("/reUserInfo")
    public ResponseBase userInfoRe(@RequestBody Map<String, Object> reMap) {
        int count = userService.reUserInfo(reMap.get("ids").toString());
        if (count > 0) {
            return BaseApiService.setResultSuccess(count);
        }
        return BaseApiService.setResultError("恢复失败~");
    }

    /**
     * 获得一个用户的信息
     *
     * @param request
     * @return
     */
    @GetMapping("/getUser")
    public ResponseBase getUser(HttpServletRequest request) {
        String token = GetCookie.getToken(request);
        if (StringUtils.isNotEmpty(token)) {
            UserInfo user = JwtUtils.jwtUser(token);
            if (user != null) {
                UserInfo userInfo = userService.getSingleUser(user.getUid());
                return BaseApiService.setResultSuccess(userInfo);
            }
        }
        return BaseApiService.setResultError("token无效~~");
    }


    /**
     * 获得历史删除的用户信息
     *
     * @return
     */
    @PostMapping("/getDelUser")
    public ResponseBase getDelUser(@RequestBody UserDto userDto) {
        PageHelper.startPage(userDto.getCurrentPage(), userDto.getPageSize());
        List<UserInfo> userDel = userService.findByDelUserInfo();
        PageInfo<UserInfo> pageInfo = new PageInfo<>(userDel);
        Integer currentPage = userDto.getCurrentPage();
        return BaseApiService.setResultSuccess(PageInfoUtils.getPage(pageInfo, currentPage));
    }

    /**
     * 查询用户名字是否存在
     */
    @GetMapping("/getUserName")
    public ResponseBase getUserName(@RequestParam("userName") String userName) {
        UserInfo userInfoName = userService.GetUserName(userName);
        return BaseApiService.setResultSuccess(userInfoName);
    }

    /**
     * 新增用户
     */
    @SuppressWarnings("unchecked")
    @Transactional //事物
    @PostMapping("/saveUserInfo")
    public ResponseBase saveUserInfo(@RequestBody Map<String, Object> userMap, HttpServletRequest request) {
        //获得登陆的时候 生成的token
        String token = GetCookie.getToken(request);
        UserInfo user = JwtUtils.jwtUser(token);
        if (user != null) {
            String userName = (String) userMap.get("userName");
            String pwd = (String) userMap.get("pwd");
            //首次登陆修改密码修改checked
            Boolean checkedUpPwd = (Boolean) userMap.get("checkedUpPwd");
            Boolean checkedUserAlways = (Boolean) userMap.get("checkedUserAlways");
            Boolean checkedPwdAlways = (Boolean) userMap.get("checkedPwdAlways");
            LinkedHashMap staffValue = (LinkedHashMap) userMap.get("staffValue");
            List<Integer> rolesId = (List<Integer>) userMap.get("rolesId");
            String pwdUserDate = (String) userMap.get("pwdUserDate");
            UserInfo userInfo = new UserInfo();
            userInfo.setUserName(userName);
            //md5盐值密码加密
            ByteSource salt = ByteSource.Util.bytes(userName);
            Object result = new SimpleHash("MD5", pwd, salt, 1024);
            userInfo.setPwd(result.toString());
            userInfo.setCreateDate(new Date().getTime());
            userInfo.setCreateIdUser(user.getUid());
            //如果点击了   用户始终有效
            if (checkedUserAlways) {
                userInfo.setEffectiveDate(0L);
            } else {
                //设置 用户有效时间
                userInfo.setEffectiveDate(DateUtils.UTCLongODefaultString(pwdUserDate));
            }
            //如果点击了   密码始终有效
            if (checkedPwdAlways) {
                userInfo.setPwdStatus(0L);
            } else {
                //前台会传2个类型参数 根据判断转换 来设计用户 密码有效时间
                if (userMap.get("pwdAlwaysInput") instanceof Integer) {
                    Integer pwdAlwaysInput = (Integer) userMap.get("pwdAlwaysInput");
                    userInfo.setPwdStatus(DateUtils.GetRearDate(pwdAlwaysInput));
                } else {
                    String pwdAlwaysInput = (String) userMap.get("pwdAlwaysInput");
                    userInfo.setPwdStatus(DateUtils.GetRearDate(Integer.parseInt(pwdAlwaysInput)));
                }
            }
            userInfo.setName(staffValue.get("sName").toString());
            //更新员工信息
            userService.saveUserInfo(userInfo);
            Long uid = userInfo.getUid();
            Long sid = Long.parseLong(staffValue.get("sId").toString());
            //关联员工信息
            staffService.upStaffInfo(uid, sid);
            UserRole userRole = new UserRole();
            for (Integer role : rolesId) {
                userRole.setuId(uid);
                userRole.setrId(role.longValue());
                //新增角色信息
                userRoleSerivce.addUserRole(userRole);
            }
            return BaseApiService.setResultSuccess("新增成功~");
        }
        return BaseApiService.setResultError("token失效");
    }
}
