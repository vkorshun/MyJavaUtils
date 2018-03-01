package com.wgsoftpro.configuration;

/**
 * Created by vkorshun on 21.05.2017.
 */

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@Configuration
@EnableWebMvc
@EnableTransactionManagement

// , WebMvcConfigurerAdapter WebMvcConfigurationSupport
public class WebConfig extends WebMvcConfigurerAdapter {
  @Override
  public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
    configurer.enable();
  }


  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    ResourceHandlerRegistration resourceRegistration = registry.addResourceHandler("/resources/**");
    resourceRegistration.addResourceLocations("/resources/");
  }

  @Bean
  public ViewResolver getViewResolver() {
    InternalResourceViewResolver resolver = new InternalResourceViewResolver();
    resolver.setPrefix("/WEB-INF/views/");
    resolver.setSuffix(".jsp");
    return resolver;
  }

}
