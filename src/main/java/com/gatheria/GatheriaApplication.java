package com.gatheria;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.gatheria.mapper")
public class GatheriaApplication {
    public static void main(String[] args) {
        SpringApplication.run(GatheriaApplication.class, args);
    }
}
