package com.obi.pandanow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.data.jdbc.DataJdbcRepositoriesAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, DataJdbcRepositoriesAutoConfiguration.class})
public class PandaNowApplication {
    public static void main(String[] args) {
        SpringApplication.run(PandaNowApplication.class, args);
    }
}