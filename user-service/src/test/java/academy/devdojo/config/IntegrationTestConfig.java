package academy.devdojo.config;

import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@Import(TestcontainersConfiguration.class)
@ActiveProfiles("itest")
public class IntegrationTestConfig {
}