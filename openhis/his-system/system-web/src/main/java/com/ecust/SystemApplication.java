package com.ecust;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;

@SpringBootApplication
@MapperScan(basePackages = {"com.ecust.mapper"})
@EnableDubbo
@EnableHystrix //启用 hystrix
@EnableCircuitBreaker //启用 hystrix 的断路保护
public class SystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(SystemApplication.class,args);
        System.out.println("主系统启动成功");
    }
}
