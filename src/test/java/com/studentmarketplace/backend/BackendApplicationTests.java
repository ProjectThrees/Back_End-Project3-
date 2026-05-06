package com.studentmarketplace.backend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
@ActiveProfiles("test")
class BackendApplicationTests {

	/**
	 * Resource server JWT config needs a {@link JwtDecoder} bean; production would use
	 * issuer/JWK properties. A mock is enough for {@code contextLoads()}.
	 */
	@MockitoBean
	private JwtDecoder jwtDecoder;

	@Test
	void contextLoads() {
	}

}
