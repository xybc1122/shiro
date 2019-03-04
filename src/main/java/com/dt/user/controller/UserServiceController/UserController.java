package com.dt.user.controller.UserServiceController;

import com.dt.user.config.BaseApiService;
import com.dt.user.config.ResponseBase;
import com.dt.user.dto.UserDto;
import com.dt.user.model.UserInfo;
import com.dt.user.model.UserRole;
import com.dt.user.service.HrArchivesEmployeeService;
import com.dt.user.service.UserRoleService;
import com.dt.user.service.UserService;
import com.dt.user.shiro.ShiroUtils;
import com.dt.user.utils.DateUtils;
import com.dt.user.utils.GetCookie;
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
    private ShiroUtils shiroUtils;
    @Autowired
    private UserService userService;

    @Autowired
    private HrArchivesEmployeeService hrService;

    @Autowired
    private UserRoleService userRoleService;

    @RequestMapping("/index")
    public ResponseBase index() {
        return BaseApiService.setResultSuccess("我来到了用户页面");
    }

    /**
     * 获取用户管理信息的一些信息
     *
     * @param pageDto
     * @return
     */
    //shiro权限控制
    @RequiresPermissions("sys:add")
    @PostMapping("/show")
    public ResponseBase showUsers(@RequestBody UserDto pageDto) {
        if (pageDto.getCurrentPage() == null || pageDto.getPageSize() == null) {
            return BaseApiService.setResultError("分页参数失效");
        }
        PageHelper.startPage(pageDto.getCurrentPage(), pageDto.getPageSize());
        List<UserInfo> listUser = userService.findByUsers(pageDto);
        //获得一些信息
        PageInfo<UserInfo> pageInfo = new PageInfo<>(listUser);
        Integer currentPage = pageDto.getCurrentPage();
        return BaseApiService.setResultSuccess(PageInfoUtils.getPage(pageInfo, currentPage));
    }


    /**
     * 获得所有用户信息
     *
     * @return
     */
    @GetMapping("/getUsers")
    public ResponseBase getUsers() {
        return BaseApiService.setResultSuccess(userService.getByUsers());
    }

    /**
     * 更新用户信息
     *
     * @return
     */
    //shiro权限控制
    @RequiresPermissions("sys:up")
    @PostMapping("/upUserInfo")
    @Transactional
    public ResponseBase userInfoUp(@RequestBody Map<String, Object> userMap) {
        //更新用户信息
        try {
            int updateResult = userService.upUser(userMap);
            if (updateResult != 1) {
                throw new Exception("更新失败,请重新操作");
            }
            String uMobilePhone = (String) userMap.get("uMobilePhone");
            if (StringUtils.isNotBlank(uMobilePhone)) {
                //更新员工信息
                hrService.upHrInfo(userMap);
            }
            //先判断是否为空
            String pwd = (String) userMap.get("pwd");
            //如果不是空 说明已经修改了密码
            if (StringUtils.isNotBlank(pwd)) {
                //踢出用户 如果是null  说明没有这个用户在线 //这里还有个问题要更新 记住我用户 踢不出去
                shiroUtils.kickOutUser((String) userMap.get("uName"));
            }
            return BaseApiService.setResultSuccess("更新成功");
        } catch (Exception e) {
            return BaseApiService.setResultError(e.getMessage());
        }
    }


    /**
     * 删除用户信息
     *
     * @return
     */
    //shiro权限控制
    @RequiresPermissions("sys:del")
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
     * @param request request对象
     * @return JSON 对象
     */
    @GetMapping("/getUser")
    public ResponseBase getUser(HttpServletRequest request) {
        UserInfo user = GetCookie.getUser(request);
        if (user == null) {
            return BaseApiService.setResultError("用户无效~");
        }
        UserInfo userInfo = userService.getSingleUser(user.getUid());
        return BaseApiService.setResultSuccess(userInfo);
    }


    /**
     * 获得历史删除的用户信息
     *
     * @param pageDto 用户对象
     * @return JSON 对象
     */
    @PostMapping("/getDelUser")
    public ResponseBase getDelUser(@RequestBody UserDto pageDto) {
        PageHelper.startPage(pageDto.getCurrentPage(), pageDto.getPageSize());
        List<UserInfo> userDel = userService.findByDelUserInfo();
        PageInfo<UserInfo> pageInfo = new PageInfo<>(userDel);
        Integer currentPage = pageDto.getCurrentPage();
        return BaseApiService.setResultSuccess(PageInfoUtils.getPage(pageInfo, currentPage));
    }

    /**
     * 查询用户名字是否存在
     *
     * @param userName 账号名
     * @return JSON 对象
     */
    @GetMapping("/getUserName")
    public ResponseBase getUserName(@RequestParam("userName") String userName) {
        UserInfo userInfoName = userService.getUserName(userName);
        return BaseApiService.setResultSuccess(userInfoName);
    }

    /**
     * 新增用户
     *
     * @param userMap 前端传的数据
     * @param request request 对象
     * @return JSON 对象
     */
    @SuppressWarnings("unchecked")
    @Transactional //事物
    @PostMapping("/saveUserInfo")
    public ResponseBase saveUserInfo(@RequestBody Map<String, Object> userMap, HttpServletRequest request) {
        //获得登陆的时候 生成的token
        //获得用户信息
        UserInfo user = GetCookie.getUser(request);
        if (user == null) {
            return BaseApiService.setResultError("用户token失效");
        }
        String userName = (String) userMap.get("userName");
        if (StringUtils.isBlank(userName)) {
            return BaseApiService.setResultError("新增失败");
        }
        String pwd = (String) userMap.get("pwd");
        if (StringUtils.isBlank(pwd)) {
            return BaseApiService.setResultError("新增失败");
        }
        //首次登陆修改密码修改checked
        Boolean checkedUpPwd = (Boolean) userMap.get("checkedUpPwd");
        if (checkedUpPwd == null) {
            return BaseApiService.setResultError("新增失败");
        }
        Boolean checkedUserAlways = (Boolean) userMap.get("checkedUserAlways");
        if (checkedUserAlways == null) {
            return BaseApiService.setResultError("新增失败");
        }
        Boolean checkedPwdAlways = (Boolean) userMap.get("checkedPwdAlways");
        if (checkedPwdAlways == null) {
            return BaseApiService.setResultError("新增失败");
        }
        Integer staffValue = (Integer) userMap.get("staffValue");
        if (staffValue == null) {
            return BaseApiService.setResultError("新增失败");
        }
        List<Integer> rolesId = (List<Integer>) userMap.get("rolesId");
        if (rolesId == null) {
            return BaseApiService.setResultError("新增失败");
        }
        //这里前端会传空字符串 或者 Long类型数据 要判断
        UserInfo userInfo = new UserInfo();
        //首次登陆是否修改密码
        if (checkedUpPwd) {
            userInfo.setFirstLogin(true);
        } else {
            userInfo.setFirstLogin(false);
        }
        userInfo.setUserName(userName);
        Object result = ShiroUtils.settingSimpleHash(userName, pwd);
        userInfo.setPwd(result.toString());
        userInfo.setCreateDate(new Date().getTime());
        userInfo.setCreateIdUser(user.getUid());
        //如果点击了   用户始终有效
        if (checkedUserAlways) {
            userInfo.setEffectiveDate(0L);
        } else {
            Long effectiveDate = (Long) userMap.get("effectiveDate");
            //设置 用户有效时间
            userInfo.setEffectiveDate(effectiveDate);
        }
        //如果点击了   密码始终有效
        if (checkedPwdAlways) {
            userInfo.setPwdStatus(0L);
        } else {
            //前台会传2个类型参数 根据判断转换 来设计 用户 密码有效时间
            Integer pwdAlwaysInput = (Integer) userMap.get("pwdAlwaysInput");
            userInfo.setPwdStatus(DateUtils.getRearDate(pwdAlwaysInput));
        }
        //新增用户
        userService.saveUserInfo(userInfo);
        Long uid = userInfo.getUid();
        Long sid = staffValue.longValue();
        //关联员工信息 更新
        hrService.bindHrInfo(uid, sid);
        //新增角色信息
        List<UserRole> urList = new ArrayList<>();
        UserRole userRole = new UserRole();
        userRole.setuId(uid);
        userRole.setrIds(rolesId);
        urList.add(userRole);
        userRoleService.addUserRole(urList);
        return BaseApiService.setResultSuccess("新增成功");
    }

    /**
     * 设置了 首次登陆修改密码的接口
     * @param userInfo 前端传的对象
     * @return
     */
    @PostMapping("/upPwd")
    public ResponseBase upUserPwd(@RequestBody UserInfo userInfo) {
        if (StringUtils.isNotBlank(userInfo.getPwd()) && StringUtils.isNotBlank(userInfo.getUserName())) {
            //md5盐值密码加密
            Object resultPwd = ShiroUtils.settingSimpleHash(userInfo.getUserName(), userInfo.getPwd());
            //更新用户
            int uCount = userService.upUserPwd(userInfo.getUid(), resultPwd.toString());
            if (uCount > 0) {
                return BaseApiService.setResultSuccess("密码修改成功");
            }
        }
        return BaseApiService.setResultError("密码修改失败");
    }
}
