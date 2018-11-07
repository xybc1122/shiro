package com.dt.user;

import com.dt.user.model.TableHead;
import com.dt.user.service.MenuService;
import com.dt.user.service.TableHeadService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ServiceTest {

    @Autowired
    private MenuService menuService;

    @Autowired
    private TableHeadService tableHeadService;

    @Test
    public void findByPermsMenuService() {
      Set<String> perms=  menuService.findByPermsMenuService(1L);
      System.out.println(perms);
    }

    @Test
    public void findByMenuIdHeadList() {
       List<TableHead> s= tableHeadService.findByMenuIdHeadList(1L);
        for (TableHead a: s) {
            System.out.println(a);
        }
    }
}
