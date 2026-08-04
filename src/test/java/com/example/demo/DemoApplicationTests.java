package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class DemoApplicationTests {

	@Autowired
	ApplicationContext applicationContext;

	@Test
	void contextLoads() {
	}

	@Test
	void demoAdminInitializerIsDisabledByDefault() {
		assertThat(applicationContext.containsBean("demoAdminInitializer")).isFalse();
	}

}
