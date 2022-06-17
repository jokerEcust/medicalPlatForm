package com.ecust;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Deque;

@SpringBootApplication
@EnableDubbo
@MapperScan(basePackages = {"com.ecust.mapper"})
public class DoctorApplication {
    public static void main(String[] args) {
        SpringApplication.run(DoctorApplication.class,args);
        System.out.println("doctor子系统启动成功");
    }
}
