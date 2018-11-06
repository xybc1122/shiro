package com.dt.user;

import com.dt.user.mapper.MenuMapper;
import com.dt.user.mapper.UserMapper;
import com.dt.user.model.Menu;
import com.dt.user.model.RoleMenu;
import com.dt.user.model.UserInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MapperTest {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private MenuMapper menuMapper;

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
        RoleMenu r =new RoleMenu();
        r.setmId(9L);
        r.setrId(3L);
        int count=menuMapper.saveMenu(r);
        // 查看结果
        System.out.println(count);

    }
}
