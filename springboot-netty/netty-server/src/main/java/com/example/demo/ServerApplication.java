package com.example.demo;

import com.cja.rpc.EnableRPC;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.example.demo.repository")
@EntityScan(basePackages = "com.cja.entity")
@EnableRPC
public class ServerApplication {


    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }


}
