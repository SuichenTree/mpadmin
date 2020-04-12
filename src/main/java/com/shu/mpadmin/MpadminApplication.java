package com.shu.mpadmin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.shu.mpadmin.mapper")
public class MpadminApplication {

    public static void main(String[] args) {
        SpringApplication.run(MpadminApplication.class, args);
    }

}
