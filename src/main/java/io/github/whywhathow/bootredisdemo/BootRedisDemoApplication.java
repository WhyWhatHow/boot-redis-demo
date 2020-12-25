package io.github.whywhathow.bootredisdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude ={DataSourceAutoConfiguration.class})
public class BootRedisDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(BootRedisDemoApplication.class, args);
    }

}
