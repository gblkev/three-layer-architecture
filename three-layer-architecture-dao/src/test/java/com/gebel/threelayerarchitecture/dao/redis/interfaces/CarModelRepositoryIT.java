package com.gebel.threelayerarchitecture.dao.redis.interfaces;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import com.gebel.threelayerarchitecture.dao._test.AbstractIntegrationTest;

@SpringBootTest
@TestPropertySource("classpath:mysql/application-test-redis.properties")
class CarModelRepositoryIT extends AbstractIntegrationTest {

}