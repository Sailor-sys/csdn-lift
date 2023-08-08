package com.xzl.csdn.config;
//
//import com.xzl.zq.common.log.RequestLogInterceptor;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.*;
//
///**
// * @author：lianp
// * @description：
// * @date：10:03 2018/5/30
// */
//
//@Configuration
//public class WebMvcConfig extends WebMvcConfigurationSupport {
//    /**
//     * 描述：允许跨域
//     */
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**").allowedMethods("*").allowedOrigins("*").allowedHeaders("*").allowCredentials(true);
//    }
//
//    @Override
//    public void addViewControllers(ViewControllerRegistry registry) {
//        registry.addViewController("/login").setViewName("login");
//        registry.addViewController("/ws").setViewName("/ws");
//        registry.addViewController("/ws2").setViewName("/ws2");
//        registry.addViewController("/ws3").setViewName("/ws3");
//    }
//
//    @Override
//    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
//        registry.addResourceHandler("doc.html").addResourceLocations("classpath:/META-INF/resources/");
//        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
//        super.addResourceHandlers(registry);
//    }
//
//    @Override
//    protected void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(new RequestLogInterceptor()).addPathPatterns("/**");
//    }
//
//}