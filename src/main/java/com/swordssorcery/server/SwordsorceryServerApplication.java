package com.swordssorcery.server;

import com.swordssorcery.server.filter.SessionLoginFilter;
import com.swordssorcery.server.resolver.UserHandlerMethodArgumentResolver;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration.WebMvcAutoConfigurationAdapter;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

import java.util.ArrayList;
import java.util.List;

/*
 * TODO:
 *  - Write a saver/calculator for attribute modifiers. (A calculator an be better because then we can change the
 *    calculation method on the fly without too much db management.)
 */

@SpringBootApplication
@Configuration
@EnableWebMvc
@ComponentScan
public class SwordsorceryServerApplication extends WebMvcAutoConfigurationAdapter {

    public static void main(String[] args) {
        SpringApplication.run(SwordsorceryServerApplication.class, args);
    }

    @Bean
    public ShaPasswordEncoder shaPasswordEncoder() {
        return new ShaPasswordEncoder(256);
    }

    @Bean
    public FilterRegistrationBean filterSessionLoginBean() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        SessionLoginFilter sessionFilter = new SessionLoginFilter();

        registrationBean.setFilter(sessionFilter);

        ArrayList<String> urlPatterns = new ArrayList<>();
        urlPatterns.add("/user/logout");
        urlPatterns.add("/character/*");

        registrationBean.setUrlPatterns(urlPatterns);

        return registrationBean;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        super.addArgumentResolvers(argumentResolvers);

        argumentResolvers.add(getUserHandlerMethodArgumentResolver());
    }

    @Bean
    public UserHandlerMethodArgumentResolver getUserHandlerMethodArgumentResolver() {
        return new UserHandlerMethodArgumentResolver();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").addResourceLocations("/static/");
    }
}