package com.dt.user;

import com.dt.user.mapper.UserMapper;
import com.dt.user.model.UserInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoShiroApplicationTests {
    @Autowired
    private UserMapper userMapper;

    @Test
    public void contextLoads() {
        UserInfo userInfo= userMapper.findByUser("tt");
        System.out.println(userInfo.toString());
    }

}
