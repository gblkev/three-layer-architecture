package com.gebel.threelayerarchitecture.dao.db.interfaces;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

// TODO utiliser testcontainers plutôt?
@DataJpaTest
class ColorRepositoryIT {
	
	@Autowired
	private CarRepository carRepository;
	
//	@Test
//	void test() {
//		System.out.println("ok");
//	}

}