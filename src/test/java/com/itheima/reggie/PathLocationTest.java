package com.itheima.reggie;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author haoy
 * @description
 * @date 2022/12/1 22:21
 */
@SpringBootTest
class PathLocationTest {
    @Test
    void test1(){
        System.out.println(this.getClass().getClassLoader().getResource("").getPath());
    }
}
