package com.ecust.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@Configuration
public class ViewDemo {
    @Bean
    public InternalResourceViewResolver resolver(){
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB_INF/jsp/");
        viewResolver.setSuffix(".jsp");
        return viewResolver;
    }

}
