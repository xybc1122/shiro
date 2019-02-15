package com.dt.user.controller.UserServiceController;

import com.dt.user.config.BaseApiService;
import com.dt.user.config.ResponseBase;
import com.dt.user.dto.PageDto;
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
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RequestMapping("/api/user")
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
    public ResponseBase showUsers(@RequestBody PageDto pageDto) {
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
            userService.upUser(userMap);
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
            return BaseApiService.setResultSuccess("更新失败");
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
     * @param request
     * @return
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
     * @return
     */
    @PostMapping("/getDelUser")
    public ResponseBase getDelUser(@RequestBody PageDto pageDto) {
        PageHelper.startPage(pageDto.getCurrentPage(), pageDto.getPageSize());
        List<UserInfo> userDel = userService.findByDelUserInfo();
        PageInfo<UserInfo> pageInfo = new PageInfo<>(userDel);
        Integer currentPage = pageDto.getCurrentPage();
        return BaseApiService.setResultSuccess(PageInfoUtils.getPage(pageInfo, currentPage));
    }

    /**
     * 查询用户名字是否存在
     */
    @GetMapping("/getUserName")
    public ResponseBase getUserName(@RequestParam("userName") String userName) {
        UserInfo userInfoName = userService.getUserName(userName);
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
        //获得用户信息
        UserInfo user = GetCookie.getUser(request);
        if (user != null) {
            String userName = (String) userMap.get("userName");
            String pwd = (String) userMap.get("pwd");
            //首次登陆修改密码修改checked
            Boolean checkedUpPwd = (Boolean) userMap.get("checkedUpPwd");
            Boolean checkedUserAlways = (Boolean) userMap.get("checkedUserAlways");
            Boolean checkedPwdAlways = (Boolean) userMap.get("checkedPwdAlways");
            Integer staffValue = (Integer) userMap.get("staffValue");
            List<Integer> rolesId = (List<Integer>) userMap.get("rolesId");
            Long effectiveDate = (Long) userMap.get("effectiveDate");
            UserInfo userInfo = new UserInfo();
            //首次登陆是否修改密码
            if (checkedUpPwd) {
                userInfo.setFirstLogin(true);
            } else {
                userInfo.setFirstLogin(false);
            }
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
                userInfo.setEffectiveDate(effectiveDate);
            }
            //如果点击了   密码始终有效
            if (checkedPwdAlways) {
                userInfo.setPwdStatus(0L);
            } else {
                //前台会传2个类型参数 根据判断转换 来设计用户 密码有效时间
                if (userMap.get("pwdAlwaysInput") instanceof Integer) {
                    Integer pwdAlwaysInput = (Integer) userMap.get("pwdAlwaysInput");
                    userInfo.setPwdStatus(DateUtils.getRearDate(pwdAlwaysInput));
                } else {
                    String pwdAlwaysInput = (String) userMap.get("pwdAlwaysInput");
                    userInfo.setPwdStatus(DateUtils.getRearDate(Integer.parseInt(pwdAlwaysInput)));
                }
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
            return BaseApiService.setResultSuccess("新增成功~");
        }
        return BaseApiService.setResultError("token失效");
    }
}
