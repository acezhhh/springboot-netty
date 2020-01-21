package com.example.demo;

import com.cja.netty.NettyClient;
import com.cja.netty.NettyUtil;
import com.cja.rpc.RPCClientRegistrar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

@SpringBootApplication
@ComponentScan("com")
@Import({RPCClientRegistrar.class})
public class ClientApplication implements CommandLineRunner {

    @Qualifier("asyncServiceExecutor")
    @Autowired
    private Executor executor;


    public static void main(String[] args) {
        SpringApplication.run(ClientApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        ConcurrentHashMap<String, NettyClient> nettys = NettyUtil.getResultSyncMap();
        nettys.forEach((key, value) -> {
            CompletableFuture.supplyAsync(() -> {
                value.start();
                return null;
            }, executor);
        });
    }

}
