package com.acadaman.install.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.web.servlet.DispatcherServlet;
import com.acadaman.install.mvc.*;
import com.acadaman.install.InstallService;

@Configuration
@EnableWebMvc
public class WebMvcConfig extends WebMvcConfigurerAdapter {

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public AcadamanSchool acadamanSchool() {
        AcadamanSchool acadamanSchool = new AcadamanSchool();
        return acadamanSchool;
    }

    @Bean
    public InstallService installService() {
        InstallService installService = new InstallService();
        return installService;
    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    @Bean
    // Only used when running in embedded servlet
    public DispatcherServlet dispatcherServlet() {
        return new DispatcherServlet();
    }
}
