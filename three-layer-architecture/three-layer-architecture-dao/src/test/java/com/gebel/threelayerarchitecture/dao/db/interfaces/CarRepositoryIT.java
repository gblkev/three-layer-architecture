package com.gebel.threelayerarchitecture.dao.db.interfaces;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CarRepositoryIT {
	
	@Autowired
	private CarRepository carRepository;
	
//	@Test
//	void test() {
//		System.out.println("ok");
//	}

}