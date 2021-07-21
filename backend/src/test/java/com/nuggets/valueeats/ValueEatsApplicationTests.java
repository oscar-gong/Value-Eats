package com.nuggets.valueeats;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ValueEatsApplicationTests {
	@Autowired
	private HealthController controller;

	@Test
	void contextLoads() {
		assertThat(controller).isNotNull();
	}

}
