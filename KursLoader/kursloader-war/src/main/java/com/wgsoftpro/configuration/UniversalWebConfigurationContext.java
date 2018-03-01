package com.wgsoftpro.configuration;

import org.springframework.context.annotation.*;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Created by vkorshun on 18.02.2017.
 */

@Configuration
//@EnableWebMvc
@EnableAsync
@EnableScheduling
@ComponentScan(basePackages = {"com.wgsoftpro"})
//, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = OpenSessionMonitor.class))
@Import(WebConfig.class)
public class UniversalWebConfigurationContext  {

//  @Override/
  //public void addResourceHandlers(ResourceHandlerRegistry registry) {
 //   registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
 // }


/*  @Override
  public void addResourceHandlers(final ResourceHandlerRegistry registry) {
    registry.addResourceHandler("/WEB-INF/resources/**").addResourceLocations("/WEB-INF/resources/");
//        .resourceChain(false)
//        .addResolver(new WebJarsResourceResolver())
//        .addResolver(new PathResourceResolver());
  }*/




}
