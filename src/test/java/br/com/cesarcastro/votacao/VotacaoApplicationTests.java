package br.com.cesarcastro.votacao;

import io.github.cdimascio.dotenv.Dotenv;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@Disabled
class VotacaoApplicationTests {

	@BeforeAll
	static void setup() {
		Dotenv dotenv = Dotenv.configure()
				.filename("votacao-test.env")
				.load();
		System.setProperty("APP-ENDPOINT-BASE", dotenv.get("APP-ENDPOINT-BASE"));
	}

	@Test
	void contextLoads() {
	}

}
