package com.dt.user;

import com.dt.user.mapper.MenuMapper;
import com.dt.user.mapper.TableHeadMapper;
import com.dt.user.mapper.UserMapper;
import com.dt.user.model.Menu;
import com.dt.user.model.RoleMenu;
import com.dt.user.model.TableHead;
import com.dt.user.model.UserInfo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MapperTest {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private TableHeadMapper tableHeadMapper;

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
        List<TableHead> s = tableHeadMapper.findByHeader();
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
        int page =1;
        int size =10;
        List<UserInfo> listUser = userMapper.findByUsers(null);
        PageHelper.startPage(page,size);
        //获得一些信息
        PageInfo<UserInfo> pageInfo= new PageInfo<>(listUser);
        Map<String,Object> data = new HashMap<>();
        data.put("total_size",pageInfo.getTotal());//总条数
        data.put("total_page",pageInfo.getPages());//总页数
        data.put("current_page",page);//当前页
        data.put("data",pageInfo.getList());//数据
        for (UserInfo u: pageInfo.getList()) {
            System.out.println(u);
        }
    }
}
