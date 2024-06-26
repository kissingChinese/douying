package com.example.config;

import com.example.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private UserService userService;


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AdminInterceptor(userService))
                .addPathPatterns("/admin/**","/authorize/**")
                .addPathPatterns("/douying/**")
                .excludePathPatterns("/douying/login/**","/douying/index/**","/douying/cdn/**", "/douying/file/**");

    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {

        String[] url = {"http://123.207.11.32:11111","http://123.207.11.32:5378","http://127.0.0.1:5378","http://localhost:5378"};

        registry.addMapping("/**")
                .allowedOrigins(url)
                .allowCredentials(true)
                .allowedMethods("*")   // 允许跨域的方法，可以单独配置
                .allowedHeaders("*");  // 允许跨域的请求头，可以单独配置;
    }
}
