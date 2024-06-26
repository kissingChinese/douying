package com.example;

import com.example.authority.AuthorityUtils;
import com.example.authority.BaseAuthority;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableAsync
@EnableAspectJAutoProxy
@SpringBootApplication
@EnableTransactionManagement
@MapperScan(basePackages = "com.example.mapper")
@EnableScheduling
public class DouyingApplication {
    public static void main(String[] args) {
        AuthorityUtils.setGlobalVerify(true,new BaseAuthority());
        SpringApplication.run(DouyingApplication.class, args);

    }


}
