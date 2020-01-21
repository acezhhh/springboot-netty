package com.example.demo;

import com.example.demo.netty.NettyServer;
import com.cja.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.annotation.Resource;

@SpringBootApplication
@ComponentScan("com")
@EnableJpaRepositories(basePackages = "com")
@EntityScan(basePackages = "com")
public class ServerApplication implements CommandLineRunner {

    @Autowired
    private NettyServer nettyServer;

    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        nettyServer.start();
    }
}
