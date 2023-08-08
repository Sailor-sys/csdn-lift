package com.xzl.csdn.config;

import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

import java.util.ArrayList;
import java.util.List;

/**
 * @author:lid
 * @Date:17:01 2021/03/25
 * @Description
 **/
@Configuration
@EnableSwagger2WebMvc
public class Knife4jConfiguration {
    @Bean(value = "creditApi")
    public Docket creditApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder()
                        .description("监管接口")
                        .version("1.0")
                        .build())
                //分组名称
                .groupName("creditApi")
                .select()
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.any())
                .build()
                .globalOperationParameters(getParameterList());
    }


    private List<Parameter> getParameterList() {
        //在配置好的配置类中增加此段代码即可
        List<Parameter> parameterList = new ArrayList<>();
        //name表示名称，description表示描述
        ParameterBuilder ticketPar = new ParameterBuilder();
        ticketPar.name("token")
                .description("token")
                .modelRef(new ModelRef("string"))
                .parameterType("header")
                //required表示是否必填，defaultvalue表示默认值
                .required(false)
                .build();
        //添加完此处一定要把下边的带***的也加上否则不生效
        parameterList.add(ticketPar.build());
        return parameterList;
    }
}