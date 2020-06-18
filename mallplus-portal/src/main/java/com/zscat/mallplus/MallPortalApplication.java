package com.zscat.mallplus;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@SpringBootApplication
@MapperScan({"com.zscat.mallplus.mapper", "com.zscat.mallplus.ums.mapper", "com.zscat.mallplus.sms.mapper", "com.zscat.mallplus.cms.mapper", "com.zscat.mallplus.sys.mapper", "com.zscat.mallplus.oms.mapper", "com.zscat.mallplus.pms.mapper", "com.zscat.mallplus.water.mapper"})
@EnableTransactionManagement
@EnableScheduling
public class MallPortalApplication extends SpringBootServletInitializer {


    public static void main(String[] args) {
        System.out.println("start-------------");
        SpringApplication.run(MallPortalApplication.class, args);
        System.out.println("end-------------");
    }

   /* @Bean
    public StartupRunner startupRunner() {
        return new StartupRunner();
    }*/
   @Override
   protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
       return application.sources(MallPortalApplication.class);
   }
}
