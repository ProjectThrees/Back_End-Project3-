package com.studentmarketplace.backend;

import com.studentmarketplace.backend.config.TestJwtDecoderConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@Import(TestJwtDecoderConfig.class)
class BackendApplicationTests {

	@Test
	void contextLoads() {
	}

}
