package com.gatheria.common.config;

import com.gatheria.common.jwt.JwtAuthenticationFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  private final JwtAuthenticationFilter jwtAuthenticationFilter;

  public WebConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
    this.jwtAuthenticationFilter = jwtAuthenticationFilter;
  }

  @Override
  public void addViewControllers(ViewControllerRegistry registry) {
    registry.addViewController("/register").setViewName("forward:/index.html");
    registry.addViewController("/").setViewName("forward:/index.html");
    registry.addViewController("/admin").setViewName("forward:/index.html");
    registry.addViewController("/login").setViewName("forward:/index.html");
    registry.addViewController("/dashboard").setViewName("forward:/index.html");
    registry.addViewController("/dashboard/instructor").setViewName("forward:/index.html");
    registry.addViewController("/dashboard/student").setViewName("forward:/index.html");
  }

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry.addResourceHandler("/**")
        .addResourceLocations("classpath:/static/");
  }

  @Bean
  public FilterRegistrationBean<JwtAuthenticationFilter> jwtFilter() {
    FilterRegistrationBean<JwtAuthenticationFilter> registrationBean = new FilterRegistrationBean<>();
    registrationBean.setFilter(jwtAuthenticationFilter);
    registrationBean.addUrlPatterns("/api/*");
    registrationBean.setOrder(1);
    return registrationBean;
  }
}