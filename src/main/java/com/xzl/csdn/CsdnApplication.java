package com.xzl.csdn;

import com.xzl.zq.common.utils.enums.DictionaryEnumHolder;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.PostConstruct;

@MapperScan({"com.xzl.csdn.mapper","com.xzl.csdn.**.mapper*"})
@SpringBootApplication(scanBasePackages = {"com.xzl.csdn.utils","com.xzl.csdn","com.xzl.zq","com.xzl"})
@EnableScheduling
@ServletComponentScan
@EnableAsync
@EnableTransactionManagement
@EnableFeignClients({"com.xzl.csdn","com.xzl.zq"})
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
public class CsdnApplication {

    public static void main(String[] args) {
        SpringApplication.run(CsdnApplication.class, args);
    }

    @PostConstruct
    public void init(){
        DictionaryEnumHolder.init("com.xzl.csdn");
    }

}

