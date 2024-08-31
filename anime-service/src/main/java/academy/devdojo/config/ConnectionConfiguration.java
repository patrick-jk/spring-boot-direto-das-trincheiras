package academy.devdojo.config;

import external.dependency.Connection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConnectionConfiguration {

    @Bean
    public Connection connectionMySql() {
        return new Connection("localhost", "devdojoMySQL", "password");
    }

    @Bean(name = "connectionMongoDB")
//    @Primary
    public Connection connectionMongo() {
        return new Connection("localhost", "devdojoMongo", "password");
    }
}