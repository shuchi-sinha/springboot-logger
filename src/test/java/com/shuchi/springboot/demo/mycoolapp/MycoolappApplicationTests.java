package com.shuchi.springboot.demo.mycoolapp;
import java.lang.reflect.Method;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration
@SpringBootTest
class MycoolappApplicationTests {
private static final  Logger logger=LoggerFactory.getLogger(MycoolappApplicationTests.class);
		
	@Test
	void contextLoads() {
		MycoolappApplication mycoolappApplication=new MycoolappApplication();
		Method[] m=mycoolappApplication.getClass().getMethods();
		for (Method method : m) {
		logger.info(method.getName());	
		}
		Assertions.assertEquals(m.length, 2);
		logger.info("number of methods are validated");
		Assertions.assertEquals(m[0], "getDailyWorkout");
		logger.info("name of methods are validated");
		Assertions.assertTrue(m[1].getName().contains("getDailyFortune"));
		
	}
}