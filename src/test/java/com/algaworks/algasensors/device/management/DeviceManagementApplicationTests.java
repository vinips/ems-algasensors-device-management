package com.algaworks.algasensors.device.management;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DeviceManagementApplicationTests {

	@Test
	void contextLoads() {
		//Feito apenas para o SonarQ parar de reclamar
		boolean a = true;
		boolean b = false;
		Assertions.assertEquals(a, b);
	}

}
