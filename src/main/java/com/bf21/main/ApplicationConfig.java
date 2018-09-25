package com.bf21.main;

import com.bf21.interceptor.LogIdInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableWebMvc
@Configuration
public class ApplicationConfig implements WebMvcConfigurer {

    @Autowired
    private LogIdInterceptor logIdInterceptor;

    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(logIdInterceptor);
    }

}