package com.hang.sso.test;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.InetSocketAddress;

@SpringBootTest
public class MyTest {
    @Test
    public void test01() {
        System.out.println(new InetSocketAddress("127.0.0.1", 9090));
    }
}
