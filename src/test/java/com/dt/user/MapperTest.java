package com.dt.user;

import com.dt.user.mapper.BasicPublicCompanyMapper;
import com.dt.user.mapper.MenuMapper;
import com.dt.user.mapper.TableHeadMapper;
import com.dt.user.mapper.UserMapper;
import com.dt.user.model.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MapperTest {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private TableHeadMapper tableHeadMapper;

    @Autowired
    private BasicPublicCompanyMapper basicPublicCompanyMapper;

    @Test
    public void findByUser() {
        UserInfo userInfo = userMapper.findByUser("tt");
        System.out.println(userInfo.toString());
    }

    @Test
    public void findByPermsMenu() {
        List<String> perms = menuMapper.findByPermsMenu(1L);
        for (String perm : perms) {
            System.out.println(perm);

        }
    }

    @Test
    public void testQueryMenuList() {
        // 原始的数据
        List<Menu> rootMenu = menuMapper.queryMenuList(null);
        // 查看结果
        for (Menu menu : rootMenu) {
            System.out.println(menu);
        }
    }

    @Test
    public void testFindQueryByRoleId() {
        // 原始的数据
        List<Menu> rootMenu = menuMapper.findQueryByRoleId(3L);
        // 查看结果
        for (Menu menu : rootMenu) {
            System.out.println(menu);
        }
    }

    @Test
    public void testSave() {
        // 原始的数据
        RoleMenu r = new RoleMenu();
        r.setmId(9L);
        r.setrId(2L);
        int count = menuMapper.saveMenu(r);
        // 查看结果
        System.out.println(count);

    }

    @Test
    public void findByHeader() {
        // 原始的数据
        List<TableHead> s = tableHeadMapper.findByHeader(1L);
        for (TableHead t : s) {
            System.out.println(s);
        }
    }

    @Test
    public void findMenuList() {
        // 原始的数据
        List<Menu> list = menuMapper.findMenuList();
        for (Menu i : list) {
            System.out.println(i);
        }
    }

    @Test
    public void findUsers() {
        int page = 1;
        int size = 10;
        List<UserInfo> listUser = userMapper.findByUsers(null);
        PageHelper.startPage(page, size);
        //获得一些信息
        PageInfo<UserInfo> pageInfo = new PageInfo<>(listUser);
        Map<String, Object> data = new HashMap<>();
        data.put("total_size", pageInfo.getTotal());//总条数
        data.put("total_page", pageInfo.getPages());//总页数
        data.put("current_page", page);//当前页
        data.put("data", pageInfo.getList());//数据
        for (UserInfo u : pageInfo.getList()) {
            System.out.println(u);
        }
    }

    @Test
    public void upTime() {
        UserInfo info = new UserInfo();
        info.setLandingTime(new Date().getTime());
        info.setUid(1L);
        int a = userMapper.upUserLandingTime(info);
    }

    @Test
    public void findByRoleInfo() {
        List<UserInfo> u = userMapper.findByRoleInfo(null);
        System.out.println(u.size());
    }

    @Test
    public void delUserInfo() {
        String uidIds = "2,3,4,5,6,7";
        int u = userMapper.delUserInfo(uidIds);
        System.out.println(u);
    }

    @Test
    public void findByListCompany() {
        List<BasicPublicCompany> s = basicPublicCompanyMapper.findByListCompany();
        for (BasicPublicCompany a:s) {
            System.out.println(a);
        }
    }
}
