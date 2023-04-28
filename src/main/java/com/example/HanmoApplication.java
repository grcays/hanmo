package com.example;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.example.mapper") @SpringBootApplication
@Slf4j
public class HanmoApplication {

	public static void main(String[] args) {
		SpringApplication.run(HanmoApplication.class, args);
		log.info("HY启动成功");

	}

}
