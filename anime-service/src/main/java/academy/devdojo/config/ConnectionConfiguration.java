package academy.devdojo.config;

import external.dependency.Connection;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class ConnectionConfiguration {
    @Value("${database.url}")
    private String url;
    @Value("${database.username}")
    private String username;
    @Value("${database.password}")
    private String password;

    @Bean
    @Profile("mysql")
    public Connection connectionMySql() {
        return new Connection(url, username, password);
    }

    @Bean(name = "connectionMongoDB")
//    @Primary
    @Profile("mongo")
    public Connection connectionMongo() {
        return new Connection("localhost", "devdojoMongo", "password");
    }
}