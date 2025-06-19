package com.qingjin;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
/*@MapperScan(value = "com.qingjin.mapper")*/
@Slf4j
public class ComputerServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ComputerServerApplication.class, args);
        log.info("server started");
    }

}
