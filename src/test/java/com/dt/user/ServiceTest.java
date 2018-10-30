package com.dt.user;

import com.dt.user.service.MenuService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ServiceTest {

    @Autowired
    private MenuService menuService;

    @Test
    public void findByPermsMenuService() {
      Set<String> perms=  menuService.findByPermsMenuService(1L);
      System.out.println(perms);
    }
}
