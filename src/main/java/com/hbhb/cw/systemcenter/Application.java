package com.hbhb.cw.systemcenter;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;

/**
 * @author dxk
 */
@RefreshScope
@EnableScheduling
@EnableFeignClients
@SpringCloudApplication
@ComponentScan("com.hbhb")
@OpenAPIDefinition(servers = {@Server(url = "${springdoc.server-url}")})
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
