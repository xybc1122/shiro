package com.shiro.demoshiro;

import com.shiro.demoshiro.doman.User;
import com.shiro.demoshiro.mapper.UserMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoShiroApplicationTests {
    @Autowired
    private UserMapper userMapper;

    @Test
    public void contextLoads() {
        Map<Integer, User> user = userMapper.findByUserMap();
        System.out.println(user);
    }

}
