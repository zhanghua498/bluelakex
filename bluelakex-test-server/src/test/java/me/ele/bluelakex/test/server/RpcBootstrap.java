package me.ele.bluelakex.test.server;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class RpcBootstrap {

    @SuppressWarnings("resource")
	public static void main(String[] args) {
        new ClassPathXmlApplicationContext("spring.xml");
    }
}

