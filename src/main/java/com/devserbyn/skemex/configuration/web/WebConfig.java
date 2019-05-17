package com.devserbyn.skemex.configuration.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.TimeZone;

@Configuration
@EnableWebMvc
@EnableScheduling
@Slf4j
@ComponentScan(basePackages = "com.devserbyn.skemex")
@EnableGlobalMethodSecurity(prePostEnabled = true)
@PropertySource("classpath:dataSource.properties")
@PropertySource("classpath:bean.properties")
@PropertySource("classpath:security.properties")
public class WebConfig implements WebMvcConfigurer {

    @Value("#{corsURl}")
    public String corsURL;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
        registry.addResourceHandler("/webapp/**").addResourceLocations("/webapp/");
        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    @Bean
    public ViewResolver viewResolver() {

        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/");
        viewResolver.setSuffix(".html");
        return viewResolver;
    }

    @Override
    public void configureDefaultServletHandling(
            DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedOrigins("*")
                .allowedHeaders("*")
                .allowCredentials(true);
    }

    @Bean(name = "multipartResolver")
    public CommonsMultipartResolver multipartResolver(@Value("${bean.maxUploadSize}") long maxUploadSize) {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setMaxUploadSize(maxUploadSize);
        return multipartResolver;
    }

    @Bean
    public String corsURl(@Value("#{isDevMode}") boolean devMode,
                          @Value("${host.production}") String prodHost,
                          @Value("${host.development}") String devHost) {
        log.info("CORS allowed origin: {}", ((devMode) ? devHost : prodHost));
        return (devMode) ? devHost : prodHost;
    }

    @Bean
    public boolean isDevMode(@Value("${mode.development}") boolean devMode) {
        log.info("Application running in {} mode", ((devMode) ? "development" : "production"));
        return devMode;
    }
}
