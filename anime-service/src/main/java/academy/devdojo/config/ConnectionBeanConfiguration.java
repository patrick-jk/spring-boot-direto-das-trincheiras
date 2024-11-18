package academy.devdojo.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Configuration
@RequiredArgsConstructor
public class ConnectionBeanConfiguration {
    private final ConnectionConfigurationProperties configurationProperties;

    @Bean
//    @Profile("mysql")
    @Primary
    public Connection connectionMySql() {
        return new Connection(configurationProperties.url(),
                configurationProperties.username(),
                configurationProperties.password());
    }

    @Bean(name = "connectionMongoDB")
//    @Primary
    @Profile("mongo")
    public Connection connectionMongo() {
        return new Connection(configurationProperties.url(),
                configurationProperties.username(),
                configurationProperties.password());
    }
}