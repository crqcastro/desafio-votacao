package br.com.cesarcastro.votacao;

import io.github.cdimascio.dotenv.Dotenv;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class VotacaoApplicationTests {

    @BeforeAll
    static void setup() {
        Dotenv dotenv = Dotenv.configure()
                .filename("votacao-test.env")
                .load();
        System.setProperty("APP_ENDPOINT_BASE", dotenv.get("APP_ENDPOINT_BASE"));
        System.setProperty("SPRING_DATASOURCE_URL", dotenv.get("SPRING_DATASOURCE_URL"));
        System.setProperty("SPRING_DATASOURCE_USERNAME", dotenv.get("SPRING_DATASOURCE_USERNAME"));
        System.setProperty("SPRING_DATASOURCE_PASSWORD", dotenv.get("SPRING_DATASOURCE_PASSWORD"));
        System.setProperty("SPRING_JPA_DATABASE_PLATFORM", dotenv.get("SPRING_JPA_DATABASE_PLATFORM"));
        System.setProperty("SPRING_JPA_HIBERNATE_DDL_AUTO", dotenv.get("SPRING_JPA_HIBERNATE_DDL_AUTO"));
    }

    @Test
    void contextLoads() {
    }

}
