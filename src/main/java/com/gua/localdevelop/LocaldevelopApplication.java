package com.gua.localdevelop;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan({"com.gua.localdevelop.dao.mapper"})
@SpringBootApplication
public class LocaldevelopApplication {

    public static void main(String[] args) {
        SpringApplication.run(LocaldevelopApplication.class, args);
    }

}
